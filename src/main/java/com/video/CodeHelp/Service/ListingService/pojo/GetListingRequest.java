package com.video.CodeHelp.Service.ListingService.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.video.CodeHelp.Service.ListingService.enums.ListingServiceEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetListingRequest {
  private Integer count;
  private Integer page;
  private ListingServiceEnums listingEnum;
}
