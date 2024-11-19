package com.video.CodeHelp.Service.ListingService;

import com.video.CodeHelp.Service.ListingService.pojo.IListingResponse;

import java.util.List;

public interface IListingService {
  public List<IListingResponse> getListing(Integer limit, Integer offset);
  public Long getTotalCount();
}
