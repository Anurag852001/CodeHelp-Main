package com.video.CodeHelp.Service.Factory.CompilerFactory;

import com.video.CodeHelp.Pojo.JavaSourceFromString;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;

public class JavaCompilerService implements ICompilerService{
  @Override
  public String compileCode(String codeSnippet) {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    // Redirect System.out and System.err to capture output and errors
    PrintStream originalOut = System.out;
    PrintStream originalErr = System.err;
    System.setOut(printStream);
    System.setErr(printStream);

    // Create a temporary directory to store compiled classes
    File tempDir = new File(System.getProperty("java.io.tmpdir"), "dynamic_classes");
    tempDir.mkdirs();

    // Wrap the code snippet inside a class
    String wrappedCode = wrapCode(codeSnippet);

    String className = "Solution"; // Class name used for the compiled code
    JavaFileObject javaFile = new JavaSourceFromString(className, wrappedCode);

    // Set up the file manager to place compiled classes into the temporary directory
    try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
      fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(tempDir));

      // Compile the source code
      boolean success = compiler.getTask(null, fileManager, null,
        List.of("-proc:none", "-Xlint:-options"), // Suppress annotation processing warnings
        null, Collections.singletonList(javaFile)).call();

      String result;
      if (success) {
        try {
          // Load the compiled class using URLClassLoader from the temporary directory
          URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{tempDir.toURI().toURL()});
          Class<?> compiledClass = classLoader.loadClass(className);

          // Find and invoke the main method of the compiled class
          Method mainMethod = compiledClass.getDeclaredMethod("main", String[].class);
          mainMethod.invoke(null, (Object) new String[]{}); // Pass an empty array to main

          result = outputStream.toString(); // Capture the output from System.out
        } catch (Exception e) {
          result = "Error executing code: " + e.toString();
        }
      } else {
        result = "Compilation failed: \n" + outputStream.toString();
      }

      // Reset System.out and System.err back to the original
      System.setOut(originalOut);
      System.setErr(originalErr);

      return result;
    } catch (Exception e) {
      return "Error setting up file manager: " + e.toString();
    }
  }


  private String wrapCode(String code) {
    // Check if the code already has a class declaration
    if (code.contains("class ")) {
      return code; // Return as-is if it seems to be a complete class
    }

    // Common Java imports for the wrapped code
    String imports = String.join("\n",
      "import java.util.*;",      // Collections classes
      "import java.io.*;",        // Input/Output classes
      "import java.math.*;",      // Math-related classes
      "import java.util.stream.*;" // Stream API classes
    );

    // Wrap the code snippet in a class and a main method
    return imports + "\n\n" +
      "public class Solution {\n" +
      "    public static void main(String[] args) {\n" +
      "        " + code + "\n" +
      "    }\n" +
      "}";
  }
}
