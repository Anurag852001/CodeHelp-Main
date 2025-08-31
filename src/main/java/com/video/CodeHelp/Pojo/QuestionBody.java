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
  private Long likes;
  private Long dislikes;
  private String functionName;
}
