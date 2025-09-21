package com.video.CodeHelp.Pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionBody {
  private String id;
  private String questionHeading;
  private String difficulty;
  private String description;
  private Long likes = 0L;
  private Long dislikes = 0L;
  private String functionName;
}
