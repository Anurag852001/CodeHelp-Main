package com.video.CodeHelp.Service.ListingService;

import com.video.CodeHelp.Enums.ApplicationErrorEnums;
import com.video.CodeHelp.Exception.CodeHelpException;
import com.video.CodeHelp.Service.ListingService.enums.ListingServiceEnums;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ListingFactory {

  private final IListingService questionListingService;

  @Inject
  public ListingFactory(IListingService questionListingService){
    this.questionListingService = questionListingService;
  }

  public IListingService getListingService(ListingServiceEnums listingServiceEnum){
    switch (listingServiceEnum){
      case QUESTION_LISTING:
        return questionListingService;
      default:
        log.info("Not implemented");
        throw new CodeHelpException(ApplicationErrorEnums.NOT_SUPPORTED);
    }
  }

}
