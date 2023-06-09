package com.a406.mrm.model.dto;

import com.a406.mrm.model.entity.Board;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@NoArgsConstructor
public class BoardResponseDto {

    public BoardResponseDto(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.createTime = board.getCreateTime();
        this.views = board.getViews();
        this.user = board.getUser().getId();
//        this.comments = board.getComments().stream().map(x -> x.getContent()).collect(Collectors.toList());
    }

    private int id;
    private String title;
    private LocalDateTime createTime;
    private int views;
    private String user;
//    private List<String> comments = new ArrayList<>();
}
