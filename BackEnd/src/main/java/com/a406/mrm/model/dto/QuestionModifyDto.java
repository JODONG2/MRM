package com.a406.mrm.model.dto;

import com.a406.mrm.model.entity.Board;
import com.a406.mrm.model.entity.Question;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionModifyDto {

    public QuestionModifyDto(Question question) {
        this.title = question.getTitle();
        this.content = question.getContent();
        this.picture = question.getPicture();
        this.status = question.getStatus();
        this.id = question.getId();
        this.user_id = question.getUser().getId();
    }

    private String title;
    private String content;
    private String picture;
    private int status;
    private int id;
    private String user_id;

}