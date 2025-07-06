package com.video.CodeHelp.Service;

import com.video.CodeHelp.Dao.MainCodeVariablesDao;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.MainCodeVariable;
import com.video.CodeHelp.utils.CachingUtils;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Slf4j
public class MainCodeVariableService {
  private final MainCodeVariablesDao mainCodeVariablesDao;
  private final CachingService cachingService;

  @Inject
  public MainCodeVariableService(MainCodeVariablesDao mainCodeVariablesDao, CachingService cachingService) {
    this.mainCodeVariablesDao = mainCodeVariablesDao;
    this.cachingService = cachingService;
  }

  public List<MainCodeVariable> getVariables(Long qid, CompilerTypeEnums language) {
    List<MainCodeVariable> mainCodeVariables = (List<MainCodeVariable>) cachingService.getFromCache(CachingUtils.getCacheKeyForMainCodeVariables(qid, language), CacheTypeEnums.TWO_HUNDERED_CACHE);
    if (mainCodeVariables == null) {
      mainCodeVariables = mainCodeVariablesDao.getMainCodeVariablesByLanguageAndQId(language, qid);
      cachingService.populateInCache(CachingUtils.getCacheKeyForMainCodeVariables(qid, language), mainCodeVariables, CacheTypeEnums.TWO_HUNDERED_CACHE);
    }
    return mainCodeVariables;
  }

  public void saveVariables(List<MainCodeVariable> variables) {
    mainCodeVariablesDao.saveMainCodeVariables(variables);
  }

  public void updateVariable(MainCodeVariable variables) {
    mainCodeVariablesDao.updateMainCodeVariables(variables);
  }

  public List<String> getFormattedVariables(Long qid,CompilerTypeEnums compilerTypeEnums){
    List<MainCodeVariable> variables = getVariables(qid,compilerTypeEnums);
    switch(compilerTypeEnums){
      case JAVA:
        return formatJavaMainCodeVariables(variables);
      case PYTHON:
        return formatPythonMainCodeVariables(variables);
      case CPP:
        return formatCppMainCodeVariables(variables);
      default:
        log.error("This language is not supported yet");
        return null;
    }
  }


  public List<String> formatJavaMainCodeVariables(List<MainCodeVariable> variables){
    //first lets sort them
    variables.sort(Comparator.comparing(MainCodeVariable::getVariableNumber));
    return variables.stream().map(variable->{
      switch (variable.getType()){
        case INTEGER_ARRAY:
          return "int[] "+ variable.getName();
        case STRING:
          return "string " + variable.getName() + " ";
        case INTEGER:
          return "int " + variable.getName() + " ";
        case BOOLEAN:
          return "boolean " + variable.getName() + " ";
        case FLOAT:
          return "float " + variable.getName() + " ";
        default:
          log.error("This data type is not supported yet");
          return null;
      }
    }).collect(Collectors.toList());
  }

  public List<String> formatPythonMainCodeVariables(List<MainCodeVariable> variables){
   return null;
  }

  public List<String> formatCppMainCodeVariables(List<MainCodeVariable> variables){
    return null;
  }

  static Object sync = new Object();
  static Boolean state = false;

  public static void main(String[] args) throws InterruptedException {
    Runnable runnable = () -> {
      for (char c = 'a'; c <= 'z'; c++) {
        try {
          printChar(c);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    };

    Runnable runnable1 = () -> {
      for (int i = 0; i < 26; i++) {
        try {
          printNumber(i);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    };

    Thread t1 = new Thread(runnable);
    Thread t2 = new Thread(runnable1);

    t1.start();
    t2.start();


  }

  private static void printNumber(int number) throws InterruptedException {
    synchronized (sync) {
      while (true) {
        System.out.println("Number");
        if (state) {
          System.out.println(number);
          state = false;
          sync.notify();
          return;
        } else {
          System.out.println("Here");
          sync.wait();
        }
      }
    }
  }

  private static void printChar(char c) throws InterruptedException {
    synchronized (sync) {
      while (true) {

        if (!state) {
          System.out.println(c);
          state = true;
//          sync.notify();
          return;
        } else {
          sync.wait();
        }
      }
    }
  }

}
