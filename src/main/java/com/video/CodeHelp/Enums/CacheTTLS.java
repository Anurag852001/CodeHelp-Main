package com.video.CodeHelp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheTTLS {
  TEN_MINUTES_CACHE(600L),ONE_MINUTE_CACHE(60L),ONE_DAY_CACHE(18400L),TWO_HUNDERED_DAYS_CAHCE(20400L);
  Long time;


  public static CacheTTLS getCacheTTL(Long time) {
    for (CacheTTLS cacheTTL : CacheTTLS.values()) {
      if (cacheTTL.getTime() == time) {
        return cacheTTL;
      }
    }
    return null;
  }
}
