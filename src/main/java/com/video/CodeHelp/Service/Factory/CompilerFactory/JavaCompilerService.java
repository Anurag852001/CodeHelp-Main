package com.video.CodeHelp.Service.Factory.CompilerFactory;

import com.video.CodeHelp.Constants.DataConstants;
import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Enums.ConfigTypeEnum;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Pojo.JavaSourceFromString;
import com.video.CodeHelp.Service.ConfigService;
import com.video.CodeHelp.Service.Factory.WrapperCodeFactory.WrapperFactory;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JavaCompilerService implements ICompilerService{

  private WrapperFactory wrapperFactory;
  private ConfigService configService;

  @Inject
  public JavaCompilerService(WrapperFactory wrapperFactory,ConfigService configService){
    this.wrapperFactory = wrapperFactory;
    this.configService = configService;
  }



  @Override
  public String compileCode(String codeSnippet) {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    ByteArrayOutputStream compileOutput = new ByteArrayOutputStream();
    ByteArrayOutputStream executionOutput = new ByteArrayOutputStream();


    Writer compileWriter = new OutputStreamWriter(compileOutput);
    PrintStream executionPrintStream = new PrintStream(executionOutput);


    String wrappedCode = wrapCode(codeSnippet, new ArrayList<>());
    String className = "Solution";
    JavaFileObject javaFile = new JavaSourceFromString(className, wrappedCode);

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
        return "Compilation failed:\n" + diagnostics.getDiagnostics().stream()
          .map(d -> d.getMessage(null))
          .collect(Collectors.joining("\n"));
      }

      // Load the compiled class and run its main method
      try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(System.getProperty("java.io.tmpdir")).toURI().toURL()})) {
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

        return executionOutput.toString(); // Return the execution output
      } catch (Exception e) {
        return "Error executing code: " + e.toString();
      }
    } catch (Exception e) {
      log.error("Error executing code " ,e);
      throw new CodeHelpException(ApplicationErrorEnums.CODE_COMPILING_ERROR);
    }
  }


  private String wrapCode(String code,List<String> inputs) {
    //firstly we will start with the basic code from config
    Long startTime = System.currentTimeMillis();
    String basicCode1 = configService.getCodeHelpConfig(DataConstants.WRAPPER_CONFIG_JAVA_1,ConfigTypeEnum.WRAPPER_CONFIG.name()).getConfigValue();
    String basicCode2 = configService.getCodeHelpConfig(DataConstants.WRAPPER_CONFIG_JAVA_2,ConfigTypeEnum.WRAPPER_CONFIG.name()).getConfigValue();
    String basicCode3 = configService.getCodeHelpConfig(DataConstants.WRAPPER_CONFIG_JAVA_3,ConfigTypeEnum.WRAPPER_CONFIG.name()).getConfigValue();

    StringBuilder stringBuilder = new StringBuilder();
    attachCode(stringBuilder,basicCode1);
    attachCode(stringBuilder,basicCode2);
    attachCode(stringBuilder,code);
    attachCode(stringBuilder,basicCode3);
    log.info("Time took to wrap code : {}",System.currentTimeMillis()-startTime);
    return stringBuilder.toString();

  }

  public void attachCode(StringBuilder stringBuilder, String code) {

    String[] lines = code.split("\n");
    for (String line : lines) {
      stringBuilder.append(line).append(System.lineSeparator());
    }
  }
}
