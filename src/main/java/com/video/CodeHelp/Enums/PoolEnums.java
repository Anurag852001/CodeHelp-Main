package com.video.CodeHelp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PoolEnums {
    TRACK_QUESTIONS_POOL(25),
    GENERATE_TEST_CASES_POOL(10),
    MONGO_POOL(10);

    final int parallelism;
}
