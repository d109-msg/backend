package com.ssafy.msg.game.model.mapper;

import com.ssafy.msg.game.model.dto.ParticipantDto;
import com.ssafy.msg.game.model.dto.RandomNameDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface GameMapper {
    List<RandomNameDto> getRandomNicknames(int limit) throws SQLException;

    RandomNameDto getRandomRoomName() throws SQLException;

    int insertParticipants(List<ParticipantDto> participants) throws SQLException;
}