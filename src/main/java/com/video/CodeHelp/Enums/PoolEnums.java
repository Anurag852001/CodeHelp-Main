package com.video.CodeHelp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PoolEnums {
    TRACK_QUESTIONS_POOL(25);

    final int parallelism;
}
