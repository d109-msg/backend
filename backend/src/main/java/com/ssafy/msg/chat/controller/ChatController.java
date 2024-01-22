package com.ssafy.msg.chat.controller;


import com.ssafy.msg.chat.model.dto.CreateRoomDto;
import com.ssafy.msg.chat.model.dto.OpponentDto;
import com.ssafy.msg.chat.model.dto.RoomDto;
import com.ssafy.msg.chat.model.service.ChatService;
import com.ssafy.msg.user.model.dto.FollowDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Controller 설정
 * 1. @RestController로 REST API 컨트롤러 등록
 * 2. @RequestMapping으로 URI 매핑
 * 3. 의존성 주입이 필요한 경우 @RequiredArgsConstructor로 생성자 주입 (UserController에서는 jwtUtil, oauth2Utils, userService에 대한 의존성 주입)
 * 4. @Slf4j로 Logger 사용 (log.info, log.error, log.error 등으로 로그 출력)
 */
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j

/*
 * Swagger 설정
 * 1. @Tag로 API를 그룹화 (회원 CRUD API를 User로 그룹화, 게시물 CRUD API를 Article로 그룹화 등)
 */
@Tag(name="Chat", description="일대일 채팅 관련 API")
public class ChatController {

    /*
     * 요청 처리 메소드 설정
     * 1. @GetMapping, @PostMapping, @PatchMapping, @PutMapping, @DeleteMapping으로 HTTP 요청 메소드와 URI 매핑
     */

    /*
     * Swagger 설정
     * 1. @Operation으로 API 요약과 설명 작성
     * 2. @ApiResponses로 ApiResponse 그룹화
     * 3. @ApiResponse로 HTTP 응답 코드, 설명, 응답 DTO 작성
     * 4. 데이터를 DTO로 받는 것이 아니라 URL의 PathVariable이나 RequestParam으로 받는 경우 @Parameter로 설명 작성
     */

    /*
     * 1. 메소드 시작 시 로그 출력
     * 2. 받은 파라미터 출력 (Receive code, Receive provider)
     */

    /*
     * 1. 에러 발생할 수 있을 때 try-catch-finally 구문 사용
     * 2. 중간중간 로그 출력은 하면 좋지만 너무 많으면 가독성 떨어지므로 적당히 하는 것이 좋을 듯함 (애매하면 그냥 처음과 끝에만 출력)
     * 3. 에러 처리 시에는 중요도에 따라 log.warn, log.error로 나눠서 로그 출력
     */

    private final ChatService chatService;

    @Operation(summary = "일대일 채팅방 단 건 조회", description = "일대일 채팅방 단 건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일대일 채팅방 단 건 조회 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "일대일 채팅방 단 건 조회 실패", content = @Content) })
    @PostMapping("/personal")
    public ResponseEntity<?> getPersonalRoom(@Valid HttpServletRequest request, @RequestBody OpponentDto opponentDto) {
        log.info("getPersonalRoom() -> Start");
        log.info("getPersonalRoom() -> Receive opponentDto : {}", opponentDto);

        String emailId = (String) request.getAttribute("emailId");
        CreateRoomDto createRoomDto = CreateRoomDto.builder()
                .userId1(emailId)
                .userId2(opponentDto.getEmailId()).build();

        RoomDto roomDto = null;

        try {
            roomDto = chatService.getPersonalRoom(createRoomDto);
            log.info("getPersonalRoom() -> Success");
            return new ResponseEntity<>(roomDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("getPersonalRoom() -> Exception : {}", e);
            return new ResponseEntity<>(roomDto, HttpStatus.BAD_REQUEST);
        } finally {
            log.info("getPersonalRoom() -> End");
        }
    }


    @Operation(summary = "일대일 채팅방 목록 조회", description = "회원 이메일에 따른 일대일 채팅방 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일대일 채팅방 목록 조회 성공", content = @Content),
            @ApiResponse(responseCode = "404", description = "일대일 채팅방 목록 조회 실패", content = @Content) })
    @GetMapping("/personal")
    public ResponseEntity<?> getPersonalRoomsInfoByEmailId(HttpServletRequest request) {
        log.info("getPersonalRoomsInfoByEmailId() -> Start");

        String emailId = (String) request.getAttribute("emailId");
        log.info("getPersonalRoomsInfoByEmailId() -> Receive emailId : {}", emailId);

        List<RoomDto> roomDtoList = null;
        try {
            roomDtoList = chatService.getPersonalRoomsInfoByEmailId(emailId);
            log.info("getPersonalRoomsInfoByEmailId() -> Success");
            return new ResponseEntity<>(roomDtoList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("getPersonalRoomsInfoByEmailId() -> Exception : {}", e);
            return new ResponseEntity<>(roomDtoList, HttpStatus.BAD_REQUEST);
        }


    }


}