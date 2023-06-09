package com.a406.mrm.service;

import com.a406.mrm.model.dto.*;
import com.a406.mrm.model.entity.Board;
import com.a406.mrm.model.entity.Room;
import com.a406.mrm.model.entity.User;
import com.a406.mrm.repository.BoardRepository;
import com.a406.mrm.repository.RoomRepository;
import com.a406.mrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Override
    public BoardResponseCommentDto join(String title, String content, String user_id, int room_id, MultipartFile picture) throws Exception {
        String uuid =  null;
        if(picture != null){
            uuid = UUID.randomUUID().toString()+"."+picture.getOriginalFilename().substring(picture.getOriginalFilename().lastIndexOf(".")+1);
            String absPath = "/img_dir/"+uuid;
//            String absPath = "/Users/dhwnsgh/Desktop/S08P12A406/BackEnd/src/main/resources/img"+uuid;
            try {
                picture.transferTo(new File(absPath));
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        User user = userRepository.findById(user_id).get();
        Room room = roomRepository.findById(room_id).get();
        BoardResponseCommentDto boardResponseCommentDto = null;

        if(room != null &&user != null){
            Board board = new Board(title, content, uuid, user, room);
            board = boardRepository.save(board);
            boardResponseCommentDto = new BoardResponseCommentDto(board);
        }

        return boardResponseCommentDto;
    }

    @Override
    public boolean delete(int id, String user_id) throws Exception {
        Board board = boardRepository.findById(id);

        if (board != null && board.getUser().getId().equals(user_id)){
            boardRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public BoardResponseCommentDto update(int id, String content, MultipartFile picture, String title, String user_id) throws Exception {
        String uuid =  null;
        if(picture != null){
            uuid = UUID.randomUUID().toString()+"."+picture.getOriginalFilename().substring(picture.getOriginalFilename().lastIndexOf(".")+1);
            String absPath = "/img_dir/"+uuid;
//            String absPath = "/Users/dhwnsgh/Desktop/S08P12A406/BackEnd/src/main/resources/img"+uuid;
            try {
                picture.transferTo(new File(absPath));
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        Board board = boardRepository.findById(id);
        BoardResponseCommentDto boardModifyDto = null;

        if (board != null && board.getUser().getId().equals(user_id)){
            board.setTitle(title);
            board.setContent(content);
            board.setPicture(uuid);
            board = boardRepository.save(board);
            boardModifyDto = new BoardResponseCommentDto(board);
        }

        return boardModifyDto;
    }

    @Override
    public Page<BoardResponseDto> listBoard_Pageable(int room_id, Pageable pageable){
        return boardRepository.findByRoomIdOrderByIdDesc(room_id, pageable);
    }

    @Override
    public BoardResponseCommentDto BoardDetail(int board_id) throws Exception {
        Board board = boardRepository.findById(board_id);
        BoardResponseCommentDto result = null;

        if(board != null){
            int views = board.getViews();
            views++;
            board.setViews(views);
            board = boardRepository.save(board);
            result = new BoardResponseCommentDto(board);
        }

        return result;
    }


}