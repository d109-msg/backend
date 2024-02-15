package com.ssafy.msg.message.model.service;

import com.ssafy.msg.article.util.S3Util;
import com.ssafy.msg.game.model.dto.ParticipantDto;
import com.ssafy.msg.message.model.dto.*;
import com.ssafy.msg.message.model.entity.MessageEntity;
import com.ssafy.msg.message.model.entity.MessageImageEntity;
import com.ssafy.msg.message.model.mapper.MessageMapper;
import com.ssafy.msg.message.model.repo.MessageRepository;
import com.ssafy.msg.message.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final SimpMessageSendingOperations sendingOperations;
    private final S3Util s3Util;
    private final DateTimeUtil dateTimeUtil;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public void sendMessage(MessageRequestDto messageRequestDto, int userId){
        MessageEntity messageEntity = MessageEntity.builder()
                .roomId(messageRequestDto.getRoomId())
                .userId(userId)
                .flagMafia(messageRequestDto.getFlagMafia())
                .createTime(dateTimeUtil.getCurrentDateTime())
                .build();

        // 텍스트 메시지
        if (messageRequestDto.getBase64Images().isEmpty()){
            messageEntity.setDataType("chat");
            messageEntity.setContent(messageRequestDto.getContent());
        }
        // 이미지 메시지
        else{
            messageEntity.setDataType("image");

            List<MessageImageEntity> messageImageEntities = new ArrayList<>();

            for (String base64Image: messageRequestDto.getBase64Images()){
                String uuid = s3Util.saveMessageImage(base64Image);
                String url = s3Util.getUrl(uuid);

                MessageImageEntity messageImageEntity = MessageImageEntity.builder()
                        .uuid(uuid)
                        .url(url)
                        .build();

                messageImageEntities.add(messageImageEntity);
            }

            messageEntity.setMessageImageEntities(messageImageEntities);
        }

        messageRepository.save(messageEntity);

        sendingOperations.convertAndSend("/sub/"+messageEntity.toDto().getRoomId(), messageEntity.toDto());
        log.info(messageEntity.toDto().getRoomId() + " - " + messageEntity.toDto());
    }

    @Override
    public void sendEnterNotice(ParticipantDto participantDto){
        MessageEntity messageEntity = MessageEntity.builder()
                .roomId(participantDto.getRoomId())
                .userId(1)
                .dataType("notice")
                .noticeType("enter")
                .createTime(dateTimeUtil.getCurrentDateTime())
                .content(participantDto.getNickname()+"님이 입장하였습니다.").build();

        messageRepository.save(messageEntity);

        sendingOperations.convertAndSend("/sub/"+messageEntity.toDto().getRoomId(), messageEntity.toDto());
        log.info(messageEntity.toDto().getRoomId() + " - " + messageEntity.toDto().getContent());
    }

    @Override
    public void sendInvitation(int userId, String chatRoomId, String gameRoomId) {
        MessageEntity messageEntity = MessageEntity.builder()
                .roomId(chatRoomId)
                .userId(userId)
                .dataType("invite")
                .createTime(dateTimeUtil.getCurrentDateTime())
                .content(gameRoomId).build();

        messageRepository.save(messageEntity);

        sendingOperations.convertAndSend("/sub/"+messageEntity.toDto().getRoomId(), messageEntity.toDto());
        log.info(messageEntity.toDto().getRoomId() + " - " + messageEntity.toDto().getContent());
    }

    @Override
    public void sendDayNotice(String dayOrNight, String roomId) throws SQLException {

        MessageEntity messageEntity = null;

        if (dayOrNight.equals("아침")){
            int day = messageMapper.calDay(roomId) + 1;

            messageEntity = MessageEntity.builder()
                    .roomId(roomId)
                    .userId(1)
                    .dataType("notice")
                    .noticeType("day")
                    .createTime(dateTimeUtil.getCurrentDateTime())
                    .content(day+"일차 " + dayOrNight + "이 되었습니다.").build();
        }else if (dayOrNight.equals("밤")){
            int day = messageMapper.calDay(roomId);

            messageEntity = MessageEntity.builder()
                    .roomId(roomId)
                    .userId(1)
                    .dataType("notice")
                    .noticeType("night")
                    .createTime(dateTimeUtil.getCurrentDateTime())
                    .content(day+"일차 " + dayOrNight + "이 되었습니다.").build();
        }

        messageRepository.save(messageEntity);

        sendingOperations.convertAndSend("/sub/"+messageEntity.toDto().getRoomId(), messageEntity.toDto());
        log.info(messageEntity.toDto().getRoomId() + " - " + messageEntity.toDto().getContent());
    }

    @Override
    public void sendStartNotice(String roomId){
        MessageEntity messageEntity = MessageEntity.builder()
                .roomId(roomId)
                .userId(1)
                .dataType("notice")
                .noticeType("start")
                .createTime(dateTimeUtil.getCurrentDateTime())
                .content("게임이 시작되었습니다.").build();

        messageRepository.save(messageEntity);

        sendingOperations.convertAndSend("/sub/"+messageEntity.toDto().getRoomId(), messageEntity.toDto());
        log.info(messageEntity.toDto().getRoomId() + " - " + messageEntity.toDto().getContent());
    }

    @Override
    public void sendEndNotice(String roomId){
        MessageEntity messageEntity = MessageEntity.builder()
                .roomId(roomId)
                .userId(1)
                .dataType("notice")
                .noticeType("end")
                .createTime(dateTimeUtil.getCurrentDateTime())
                .content("게임이 종료되었습니다.").build();

        messageRepository.save(messageEntity);

        sendingOperations.convertAndSend("/sub/"+messageEntity.toDto().getRoomId(), messageEntity.toDto());
        log.info(messageEntity.toDto().getRoomId() + " - " + messageEntity.toDto().getContent());
    }

    @Override
    public void sendGameNotice(String roomId, String text){
        MessageEntity messageEntity = MessageEntity.builder()
                .roomId(roomId)
                .userId(1)
                .dataType("notice")
                .noticeType("game")
                .createTime(dateTimeUtil.getCurrentDateTime())
                .content(text).build();

        messageRepository.save(messageEntity);

        sendingOperations.convertAndSend("/sub/"+messageEntity.toDto().getRoomId(), messageEntity.toDto());
        log.info(messageEntity.toDto().getRoomId() + " - " + messageEntity.toDto().getContent());
    }

    @Override
    public void updateLastMessageId(LastMessageUpdateDto lastMessageUpdateDto) throws SQLException {
        messageMapper.updateLastMessageId(lastMessageUpdateDto);
    }
}
