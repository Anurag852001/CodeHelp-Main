package com.video.CodeHelp.Service.CompilerService;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Enums.ConfigTypeEnum;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Pojo.CodeCompilingRequest;
import com.video.CodeHelp.Pojo.JavaSourceFromString;
import com.video.CodeHelp.Service.ConfigService;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.WrapperFactory;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.enums.WrapperCodeEnums;
import com.video.CodeHelp.Service.MainCodeVariableService;
import com.video.CodeHelp.Service.TestCaseService.ITestCaseService;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.tools.*;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JavaCompilerService implements ICompilerService {

  private WrapperFactory wrapperFactory;
  private ConfigService configService;
  private MainCodeVariableService mainCodeVariableService;
  private ITestCaseService testCaseService;

  @Inject
  public JavaCompilerService(WrapperFactory wrapperFactory, ConfigService configService, MainCodeVariableService mainCodeVariableService,
                             ITestCaseService testCaseService) {
    this.wrapperFactory = wrapperFactory;
    this.configService = configService;
    this.mainCodeVariableService = mainCodeVariableService;
    this.testCaseService = testCaseService;
  }


  @Override
  public String compileCode(CodeCompilingRequest request) {
    String wrappedCode = wrapCode(request.getCode(), new ArrayList<>(), request.getQid(), request);
    log.info("final wrappedCode:{} ", wrappedCode);
    return runSimpleCode(wrappedCode);
  }

  @Override
  public String runSimpleCode(String code) {
    try {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      ByteArrayOutputStream compileOutput = new ByteArrayOutputStream();
      ByteArrayOutputStream executionOutput = new ByteArrayOutputStream();
      Writer compileWriter = new OutputStreamWriter(compileOutput);
      PrintStream executionPrintStream = new PrintStream(executionOutput);
      log.info("code to be run:{} ", code);
      String className = "Solution";
      JavaFileObject javaFile = new JavaSourceFromString(className, code);

      try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
        // Set class output location to in-memory ByteArray instead of disk
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(new File(System.getProperty("java.io.tmpdir"))));

        // Set up a diagnostic listener to capture compiler diagnostics directly
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        boolean success = compiler.getTask(compileWriter, fileManager, diagnostics,
          List.of("-proc:none", "-Xlint:-options"), // Suppress annotation processing warnings
          null, List.of(javaFile)).call();

        compileWriter.flush(); // Flush the writer to capture compilation output

        // Check compilation success and handle errors directly from diagnostics
        if (!success) {
          throw new CodeHelpException( "Compilation failed:\n" + diagnostics.getDiagnostics().stream()
            .map(d -> d.getMessage(null))
            .collect(Collectors.joining("\n")));
        }

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(System.getProperty("java.io.tmpdir")).toURI().toURL()});
        // Redirect output streams to capture execution output separately
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        System.setOut(executionPrintStream);
        System.setErr(executionPrintStream);

        // Execute the main method
        classLoader.loadClass(className).getDeclaredMethod("main", String[].class).invoke(null, (Object) new String[]{});

        // Restore the original System.out and System.err
        System.setOut(originalOut);
        System.setErr(originalErr);

        return executionOutput.toString();
      }
    } catch (CodeHelpException e){
      log.error(e.getMessage(), e);
      throw new CodeHelpException(e.getMessage());
    } catch (Exception e) {
      log.error("Error executing code ", e);
      throw new CodeHelpException(ApplicationErrorEnums.CODE_COMPILING_ERROR+e.getMessage());
    }
  }


  private String wrapCode(String code, List<String> inputs, Long qid, CodeCompilingRequest request) {
    //firstly we will start with the basic code from config
    Long startTime = System.currentTimeMillis();
    String basicCode1 = configService.getCodeHelpConfig(DataConstants.WRAPPER_CONFIG_JAVA_1, ConfigTypeEnum.WRAPPER_CONFIG.name()).getConfigValue();
    String basicCode2 = configService.getCodeHelpConfig(DataConstants.WRAPPER_CONFIG_JAVA_2, ConfigTypeEnum.WRAPPER_CONFIG.name()).getConfigValue();
    String basicCode3 = configService.getCodeHelpConfig(DataConstants.WRAPPER_CONFIG_JAVA_3, ConfigTypeEnum.WRAPPER_CONFIG.name()).getConfigValue();
    String basicCode4 = configService.getCodeHelpConfig(DataConstants.WRAPPER_CONFIG_JAVA_4, ConfigTypeEnum.WRAPPER_CONFIG.name()).getConfigValue();

    StringBuilder stringBuilder1 = new StringBuilder();
    attachCode(stringBuilder1, basicCode1);
    attachCode(stringBuilder1, code);
    attachCode(stringBuilder1, basicCode2);
    attachCode(stringBuilder1, basicCode3);
    attachTestCase(stringBuilder1, request);
    String codeToBeWrappedWithMainCode = stringBuilder1.toString();
    String newCode = wrapperFactory.getWrapperService(WrapperCodeEnums.MAIN_CODE).wrapCode(codeToBeWrappedWithMainCode, qid, CompilerTypeEnums.JAVA, inputs);
    StringBuilder stringBuilder2 = new StringBuilder().append(newCode);
    attachCode(stringBuilder2, basicCode4);
    log.info("Time took to wrap code : {}", System.currentTimeMillis() - startTime);
    return stringBuilder2.toString();

  }

  public void attachCode(StringBuilder stringBuilder, String code) {

    String[] lines = code.split("\n");
    for (String line : lines) {
      stringBuilder.append(line).append(System.lineSeparator());
    }
  }

  public void attachTestCase(StringBuilder stringBuilder, CodeCompilingRequest request) {
    stringBuilder.append(System.lineSeparator());
    List<String> variables = mainCodeVariableService.getFormattedVariables(request.getQid(), CompilerTypeEnums.JAVA);
    List<String> testCases = testCaseService.getFormattedTestCase(request.getTestCase(), CompilerTypeEnums.JAVA);
    for (int i = 0; i < variables.size(); i++) {
      stringBuilder.append(variables.get(i)).append(" = ").append(testCases.get(i)).append(System.lineSeparator());
    }
    stringBuilder.append(System.lineSeparator());
  }
}
