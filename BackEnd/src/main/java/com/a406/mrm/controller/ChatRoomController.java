package com.a406.mrm.controller;

import com.a406.mrm.model.dto.ChatMessageResponseDto;
import com.a406.mrm.service.ChatMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@Api("Chat Room Controller API")
@RequiredArgsConstructor
public class ChatRoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final ChatMessageService chatMessageService;

    /**
     * @param roomId
     *          를 통해 채팅방의 모든 채팅 내역을 가져온다
     * @return chats : 채팅방의 모든 채팅 내역을 반환한다
     */
    @ApiOperation("Enter Chat Room")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<Map<String, Object>> roomDetail(
            @PathVariable @ApiParam("Room ID") int roomId) {
        logger.info("[roomDetail] Enter Chatting Room - roomId : "+roomId);

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            // 해당 room의 채팅 내역을 가져온다.
            List<ChatMessageResponseDto> chats = chatMessageService.findAllChat(roomId);

            resultMap.put("chats", chats);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("error", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

}