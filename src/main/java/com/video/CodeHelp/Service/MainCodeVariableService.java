package com.video.CodeHelp.Service;

import com.video.CodeHelp.Dao.MainCodeVariablesDao;
import com.video.CodeHelp.Enums.CacheTypeEnums;
import com.video.CodeHelp.Enums.CompilerTypeEnums;
import com.video.CodeHelp.Pojo.MainCodeVariable;
import com.video.CodeHelp.utils.CachingUtils;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
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

}
