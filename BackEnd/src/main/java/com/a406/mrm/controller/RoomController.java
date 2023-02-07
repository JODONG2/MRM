package com.a406.mrm.controller;

import com.a406.mrm.model.dto.*;
import com.a406.mrm.model.entity.Room;
import com.a406.mrm.model.entity.Todo;
import com.a406.mrm.repository.*;
import com.a406.mrm.service.RoomService;
import com.a406.mrm.service.TodoService;
import com.a406.mrm.service.TodoTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/room"})
@Api("room Controller API")
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    @Autowired
    private RoomService roomService;
    @Autowired
    private TodoService todoService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserHasRoomRepository userHasRoomRepository;
    @Autowired
    private TodoTimeRepository todoTimeRepository;

    @Autowired
    private TodoTimeService todoTimeService;

    @ApiOperation("make a room(=group)")
    @PostMapping(value = "{userId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addRoom(//@RequestHeader(value="Authorization") String token,
                                       @PathVariable("userId") String userId,
//                                       @RequestBody @ApiParam("room register information") RoomRequestDto roomRequestDto,
                                     @RequestParam String name,
                                     @RequestParam String intro,
                                     @RequestParam MultipartFile profile
                                     ) {
//        logger.debug("new Room information : {}", roomRequestDto.toString());
        RoomRequestDto roomRequestDto = new RoomRequestDto(intro,name);
        RoomResponseDto addRoomResult = new RoomResponseDto(roomService.makeRoom(roomRequestDto,userId,profile));
        return ResponseEntity.status(HttpStatus.CREATED).body(addRoomResult);
    }
    @ApiOperation("enter the room(=group)")
    @PostMapping("enter/{roomId}/{userId}")
    public ResponseEntity<?> enterRoom(@PathVariable("userId") String userId,
                                        @RequestParam @ApiParam("room entry code") String roomCode,
                                        @PathVariable @ApiParam("room id") int roomId) {
        // front에서 전해준 entry code는 (code+id) 처리가 되어 있다
        logger.debug("Room Entry Code information : {}", roomCode);

        // 반환 내용을 어떻게 할지 고민....
        // 해당 코드를 가진 room이 없다면 retrun
        if(!roomService.existsRoomByIdAndCode(roomId, roomCode))
            return ResponseEntity.status(HttpStatus.OK).body(false);

        // 이미 방에 입장되어 있다면 return
        if(roomService.existsUserHasRoomByRoomIdAndUserId(roomId, userId))
            return ResponseEntity.status(HttpStatus.OK).body(false);

        // 있다면 room에 입장 처리 후 room 정보 반환
        RoomResponseDto enterRoomResult = new RoomResponseDto(roomService.enterRoom(roomId, userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(enterRoomResult);
    }
    @ApiOperation("refresh entry code of room(=group)")
    @PatchMapping("/{roomId}/code")
    public ResponseEntity<?> refreshCode(@PathVariable("roomId") int roomId) {
        logger.debug("Room Id information : {}", roomId);
        String afterEntryCode = roomService.updateCode(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(afterEntryCode);
    }
    @ApiOperation("Delete room")
    @DeleteMapping("{roomId}")
    public ResponseEntity<?> removeGroup(@PathVariable("roomId") int roomId){
        roomService.removeRoom(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    // /room/{roomId}/name
    @ApiOperation("modify room name")
    @PatchMapping("{roomId}/name")
    public ResponseEntity<?> modifyName(@PathVariable("roomId") int roomId,
                                        @RequestBody String name){
        return ResponseEntity.status(HttpStatus.OK).body(roomService.modifyName(roomId,name));
    }

    @ApiOperation("modify room intro")
    @PatchMapping("{roomId}/intro")
    public ResponseEntity<?> modifyIntro(@PathVariable("roomId") int roomId,
                                         @RequestBody String intro){
        return ResponseEntity.status(HttpStatus.OK).body(roomService.modifyIntro(roomId,intro));
    }

    @ApiOperation("modfy room profile")

    @PatchMapping("/profile/{roomId}")
    public ResponseEntity<?> modifyProfile(@PathVariable("roomId") int roomId, @RequestParam MultipartFile profile){
        return ResponseEntity.status(HttpStatus.OK).body(roomService.modifyProfile(roomId,profile));
    }

    @ApiOperation("first login - my room")
    @GetMapping("/my/first/{userId}")
    public ResponseEntity<?> getMyRoom(@PathVariable("userId") String userId){
        logger.debug("User id : {}", userId);
        List<Todo> todos = todoService.getTodoAll(userId);
        logger.info("todos size : {}", todos.size());
        //service로 빼는게 좋을거 같은데..
        List<Room> rooms = userHasRoomRepository.findByUserId(userId)
                        .stream().map(r -> r.getRoom()).collect(Collectors.toList());
        logger.debug("Todos count : {}", todos.size());
        logger.debug("Rooms count : {}", rooms.size());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MyRoomFirstDto(todos,rooms,userRepository.findById(userId).get()));
    }
    @ApiOperation("my room - without room information")
    @GetMapping("/my/{userId}")
    public ResponseEntity<?> loginMyRoom(@PathVariable("userId") String userId){
        logger.debug("login User Id : {}", userId);
        List<Todo> todos = todoService.getTodoAll(userId);
        logger.debug("Todos count : {}", todos.size());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MyRoomDto(todos,userRepository.findById(userId).get()));
    }

    @GetMapping
    public ResponseEntity<?> RoomListAll(){
        List<RoomMoveResponseDto> result = roomService.RoomListAll();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @GetMapping("/{room_id}")
    public ResponseEntity<?> SearchMyRoom(@PathVariable("room_id") int room_id){
        Room room = roomRepository.findById(room_id).get();
        RoomMoveResponseDto move = new RoomMoveResponseDto(room);
        return ResponseEntity.status(HttpStatus.OK).body(move);
    }

}
