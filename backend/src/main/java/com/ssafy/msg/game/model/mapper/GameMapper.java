package com.ssafy.msg.game.model.mapper;

import com.ssafy.msg.chat.model.dto.RoomDto;
import com.ssafy.msg.chat.model.dto.RoomResponseDto;
import com.ssafy.msg.game.model.dto.*;
import com.ssafy.msg.user.model.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface GameMapper {
    List<ParticipantDto> getParticipants(String roomId) throws SQLException;

    boolean getRandomGameApplyStatus(int userId) throws  SQLException;

    void applyRandomGame(UserDto userDto) throws SQLException;

    boolean isParticipantInRoom(EnterGroupRoomDto enterGroupRoomDto) throws SQLException;

    List<RandomNameDto> getRandomNicknames(int limit) throws SQLException;

    RandomNameDto getRandomRoomName() throws SQLException;

    int insertParticipants(List<ParticipantDto> participants) throws SQLException;

    void deleteParticipant(int userId) throws SQLException;

    List<RoomResponseDto> getUserRooms(int userId) throws SQLException;

    List<VoteResultDto> getRoomVote(String roomId) throws SQLException;

    ParticipantDto getParticipant(ParticipantReceiveDto participantReceiveDto) throws SQLException;

    List<AliveParticipantDto> getAliveParticipants(String roomId) throws SQLException;

    void normalVote(int participantId, int targetId, Integer day) throws SQLException;
    void mafiaVote(int participantId, int targetId, Integer day) throws SQLException;
    void doctorVote(int participantId, int targetId, Integer day) throws SQLException;
    void reporterVote(int participantId, int targetId, Integer day) throws SQLException;

    ParticipantDto getParticipantWithPId(int participantId) throws SQLException;

    MyVoteDto getMyVote(int participantId, int day) throws SQLException;

    int getRandomMission(String roomId) throws SQLException;

    void createNewMission(NewMissionDto newMissionDto) throws SQLException;

    Integer isAlive(int participantId) throws SQLException;

    MissionResultDto getMyMission(int participantId, int day) throws SQLException;

    Integer getMaxDay(int participantId) throws SQLException;

    Integer getMaxDayByRoomId(String roomId) throws SQLException;

    List<UserEndGameDto> getUserEndGame(int userId) throws SQLException;

    Integer completeMission(int participantId, int day) throws SQLException;

    List<ParticipantIdDto> getParticipantsInRoom(String roomId) throws SQLException;

    void updateStartTime(String roomId) throws SQLException;

    void updateParticipant(UpdateParticipantDto updateParticipantDto) throws SQLException;

    int getFlagNight(String roomId) throws SQLException;

    GetAbilityResultDto getAbilityAvailability(GetAbilityParamDto getAbilityParamDto) throws SQLException;

    Integer checkSaboteur(int participantId) throws SQLException;

    void setAbility(int participantId, int value) throws SQLException;

    void createMsgArticle(CreateMsgArticleDto createMsgArticleDto) throws SQLException;
}
