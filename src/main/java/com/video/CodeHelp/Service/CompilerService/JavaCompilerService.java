package com.video.CodeHelp.Service.CompilerService;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Enums.ConfigTypeEnum;
import com.video.CodeHelp.Enums.TestCaseType;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Pojo.*;
import com.video.CodeHelp.Pojo.Responses.SubmitCodeResponse;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.WrapperFactory;
import com.video.CodeHelp.Service.CachePopulationService.WrapperCodeService.enums.WrapperCodeEnums;
import com.video.CodeHelp.Service.ConfigService;
import com.video.CodeHelp.Service.MainCodeVariableService;
import com.video.CodeHelp.Service.TestCaseService.ITestCaseService;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.tools.*;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.video.CodeHelp.Enums.CompilerTypeEnums.JAVA;

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
  public SubmitCodeResponse compileCode(CodeCompilingRequest request) {
    SubmitCodeResponse correctCodeSubmitResponse = wrapAndSubmit(request.getCorrectCode(),request.getQid(),request.getTestCase(),true,true);
    //for every testcase lets set correct solutions
    for(int i = 0 ; i <request.getTestCase().size();i++){
      request.getTestCase().get(i).setSolution(correctCodeSubmitResponse.getExpectedResultOfTestCase().get(i));
    }
    SubmitCodeResponse submitCodeResponse = wrapAndSubmit(request.getCode(),request.getQid(),request.getTestCase(),true,false);
//    log.info("final wrappedCode:{} ", wrappedCode);
    return submitCodeResponse;
  }

  @Override
  public String runSimpleCode(String code) {
    try {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      ByteArrayOutputStream compileOutput = new ByteArrayOutputStream();
      ByteArrayOutputStream executionOutput = new ByteArrayOutputStream();
      Writer compileWriter = new OutputStreamWriter(compileOutput);
      PrintStream executionPrintStream = new PrintStream(executionOutput);
//      log.info("code to be run:{} ", code);
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
          throw new CodeHelpException("Compilation failed:\n" + diagnostics.getDiagnostics().stream()
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
    } catch (CodeHelpException e) {
      log.error(e.getMessage(), e);
      throw new CodeHelpException(e.getMessage());
    } catch (Exception e) {
      log.error("Error executing code ", e);
      throw new CodeHelpException(ApplicationErrorEnums.CODE_COMPILING_ERROR + e.getMessage());
    }
  }

  @Override
  public SubmitCodeResponse submitCode(SubmitCodeRequest request) {
    try {
      List<TestCase> testCases = testCaseService.getTestCases(request.getQid(), request.getCompilerType(), TestCaseType.MAIN_TESTCASE);
      return wrapAndSubmit(request.getCode(),request.getQid(),testCases,false,false);
    } catch (Exception e) {
      log.error("Error while submitting" ,e);
      throw new CodeHelpException(ApplicationErrorEnums.SOMETHING_WENT_WRONG);
    }
  }


  private SubmitCodeResponse wrapAndSubmit(String code,Long qid,List<TestCase> testCase,boolean getResultOfAll,boolean isCorrectCodeSubmission) {
    //firstly we will start with the basic code from config

    long startTime = System.currentTimeMillis();
    StringBuilder stringBuilder1 = getWrappedWithBasicClass(code);
    Integer passedTestCases = 0;
    String expectedLastTestCaseResult = null;
    boolean failed = false;

    String basicCode4 = configService.getCodeHelpConfig(DataConstants.WRAPPER_CONFIG_JAVA_4, ConfigTypeEnum.WRAPPER_CONFIG.name()).getConfigValue();
    List<List<String>> formattedTestCase = testCaseService.getFormattedTestCase(testCase,qid, JAVA);
    List<String> resultOfAll = new ArrayList<>();
    List<String> expectedResultOfAll = new ArrayList<>();
    for(int i = 0;i < formattedTestCase.size(); i++) {
      attachTestCase(stringBuilder1, formattedTestCase.get(i),qid);
      String codeToBeWrappedWithMainCode = stringBuilder1.toString();
      String newCode = wrapperFactory.getWrapperService(WrapperCodeEnums.MAIN_CODE).wrapCode(codeToBeWrappedWithMainCode, qid, JAVA);
      StringBuilder stringBuilder2 = new StringBuilder().append(newCode);
      attachCode(stringBuilder2, basicCode4);
      String currentResult =  runSimpleCode(stringBuilder2.toString());
      if(currentResult.equalsIgnoreCase(testCase.get(i).getSolution())){
        passedTestCases++;
        resultOfAll.add(currentResult);
      } else if(!getResultOfAll && !isCorrectCodeSubmission) {
        failed = true;
        expectedLastTestCaseResult = testCase.get(i).getSolution();
        break;
      } else if(!isCorrectCodeSubmission) {
        failed = true;
      }
      expectedResultOfAll.add(testCase.get(i).getSolution());
    }

    Long timeTaken = System.currentTimeMillis() - startTime;
    log.info("Time took to execute all testcases : {}", timeTaken);
    SubmitCodeResponse submitCodeResponse = SubmitCodeResponse.builder().testCasesPassed(passedTestCases)
            .totalTestCases(testCase.size())
            .expectedLastTestCaseResultBeforeFailure(expectedLastTestCaseResult)
            .failed(failed)
            .timeTake(timeTaken)
            .build();
    if(getResultOfAll){
      submitCodeResponse.setExpectedResultOfTestCase(expectedResultOfAll);
      submitCodeResponse.setResultOfEachTestCase(resultOfAll);
    }
    return submitCodeResponse;
  }

  private StringBuilder getWrappedWithBasicClass(String code){
    String basicCode1 = configService.getCodeHelpConfig(DataConstants.WRAPPER_CONFIG_JAVA_1, ConfigTypeEnum.WRAPPER_CONFIG.name()).getConfigValue();
    String basicCode2 = configService.getCodeHelpConfig(DataConstants.WRAPPER_CONFIG_JAVA_2, ConfigTypeEnum.WRAPPER_CONFIG.name()).getConfigValue();
    String basicCode3 = configService.getCodeHelpConfig(DataConstants.WRAPPER_CONFIG_JAVA_3, ConfigTypeEnum.WRAPPER_CONFIG.name()).getConfigValue();


    StringBuilder stringBuilder = new StringBuilder();
    attachCode(stringBuilder, basicCode1);
    attachCode(stringBuilder, code);
    attachCode(stringBuilder, basicCode2);
    attachCode(stringBuilder, basicCode3);
    return stringBuilder;
  }

  public void attachCode(StringBuilder stringBuilder, String code) {

    String[] lines = code.split("\n");
    for (String line : lines) {
      stringBuilder.append(line).append(System.lineSeparator());
    }
  }

  public void attachTestCase( StringBuilder stringBuilder, List<String> testCases,Long qid) {
    stringBuilder.append(System.lineSeparator());
    List<String> variables = mainCodeVariableService.getFormattedVariables(qid, JAVA);

    if(CollectionUtils.isEmpty(testCases)){
      log.info("No test cases found to test");
      throw new CodeHelpException("No test cases found");
    }
    for (int i = 0; i < testCases.size(); i++) {
      stringBuilder.append(variables.get(i)).append(" = ").append(testCases.get(i)).append(System.lineSeparator());
    }
    stringBuilder.append(System.lineSeparator());
  }
}
