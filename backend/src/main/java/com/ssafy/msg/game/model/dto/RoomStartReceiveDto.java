package com.ssafy.msg.game.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomStartReceiveDto {
    private String roomId;
    private String roomTitle;
    private List<Integer> userList;
}
