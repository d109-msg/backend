package com.ssafy.msg.game.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private Integer id;
    private String roomId;
    private String userEmailId;
    private Integer lastMessageId;
    private Integer flagDie;
    private Integer flagWin;
    private String jobId;
    private String nickname;
    private String imageUrl;
}