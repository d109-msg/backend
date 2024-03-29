package com.ssafy.msg.chat.model.service;

import com.ssafy.msg.chat.model.dto.CreateRoomDto;
import com.ssafy.msg.chat.model.dto.RoomDto;
import com.ssafy.msg.chat.model.dto.RoomResponseDto;
import com.ssafy.msg.chat.model.mapper.ChatMapper;
import com.ssafy.msg.game.model.dto.ParticipantDto;
import com.ssafy.msg.message.model.entity.MessageEntity;
import com.ssafy.msg.message.model.repo.MessageRepository;
import com.ssafy.msg.notification.model.service.NotificationService;
import com.ssafy.msg.user.model.dto.UserDto;
import com.ssafy.msg.user.model.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements  ChatService{

    private final MessageRepository messageRepository;

    private final NotificationService notificationService;

    private final ChatMapper chatMapper;
    private final UserMapper userMapper;

    // 상대방과의 채팅방이 있으면 roomDto return
    // 상대방과의 채팅방이 없으면 생성 후 return
    @Override
    public Map getPersonalRoom(CreateRoomDto createRoomDto) throws Exception {
        RoomDto roomDto = chatMapper.findRoom(createRoomDto);

        Map<String, Object> map = new HashMap<String,Object>();
        if (roomDto == null) {
            map.put("isNew", true);
            map.put("roomDto", createEnterPersonalRoom(createRoomDto));
        }else{
            map.put("isNew", false);
            map.put("roomDto", roomDto);
        }
        return map;
    }

    // 상대방과의 채팅방 생성
    public RoomDto createEnterPersonalRoom(CreateRoomDto createRoomDto) throws Exception {
        UserDto user1 = userMapper.findUserById(createRoomDto.getUserId1());
        UserDto user2 = userMapper.findUserById(createRoomDto.getUserId2());

        String roomId = UUID.randomUUID().toString();
        RoomDto roomDto = RoomDto.builder()
                .id(roomId)
                .dataType("개인")
                .title(user2.getNickname())
                .imageUrl(user2.getImageUrl())
                .build();
        chatMapper.createRoom(roomDto);

        ParticipantDto participant1 = ParticipantDto.builder()
                .roomId(roomId)
                .userId(createRoomDto.getUserId1())
                .imageUrl(user1.getImageUrl())
                .nickname(user1.getNickname())
                .build();
        ParticipantDto participant2 = ParticipantDto.builder()
                .roomId(roomId)
                .userId(createRoomDto.getUserId2())
                .imageUrl(user2.getImageUrl())
                .nickname(user2.getNickname())
                .build();
        chatMapper.enterRoom(participant1);
        chatMapper.enterRoom(participant2);

        // WebSocket/STOMP 메시지 전송
        notificationService.sendRoomSubscribeRequest(createRoomDto.getUserId1(), roomId);
        notificationService.sendRoomSubscribeRequest(createRoomDto.getUserId2(), roomId);

        return chatMapper.getRoom(roomId);
    }


    // 유저 이메일 아이디에 따른 일대일 채팅방 목록 조회
    @Override
    public List<RoomResponseDto> getPersonalRoomsInfoById(int id) throws Exception {
        List<RoomResponseDto> personalRoomList = chatMapper.getPersonalRoomsInfoById(id);

        for (RoomResponseDto room: personalRoomList){
            MessageEntity messageEntity = messageRepository.findLastMessageByRoomId(room.getId());

            // 기존 메시지가 있는 경우
            if (messageEntity != null){
                // 새로운 메시지가 있는 경우
                if (room.getLastMessageId() == null
                || new ObjectId(messageEntity.getId()).getTimestamp() > new ObjectId(room.getLastMessageId()).getTimestamp()){
                    // 이미지 메시지인 경우
                    if(messageEntity.getContent() == null){
                        room.setLastMessage("사진");
                    }else{
                        room.setLastMessage(messageEntity.getContent());
                    }
                    room.setLastMessageCreateTime(messageEntity.getCreateTime());
                    room.setFlagNewMessage(1);
                }else{
                    if (messageEntity.getContent() == null){
                        room.setLastMessage("사진");
                    }else{
                        room.setLastMessage(messageEntity.getContent());
                    }
                    room.setLastMessageCreateTime(messageEntity.getCreateTime());
                }

            }
        }

        return personalRoomList;
    }
}
