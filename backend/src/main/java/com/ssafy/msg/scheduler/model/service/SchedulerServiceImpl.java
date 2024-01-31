package com.ssafy.msg.scheduler.model.service;

import com.ssafy.msg.chat.model.dto.RoomDto;
import com.ssafy.msg.game.model.dto.ParticipantDto;
import com.ssafy.msg.game.model.dto.RandomNameDto;
import com.ssafy.msg.game.model.dto.RoomStartReceiveDto;
import com.ssafy.msg.game.model.service.GameService;
import com.ssafy.msg.message.model.mapper.MessageMapper;
import com.ssafy.msg.message.model.service.MessageService;
import com.ssafy.msg.scheduler.model.dto.UpdateWinFlagDto;
import com.ssafy.msg.scheduler.model.mapper.SchedulerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService{

    private final SchedulerMapper schedulerMapper;
    private final MessageMapper messageMapper;

    private final GameService gameService;
    private final MessageService messageService;

    @Scheduled(cron = "0 0 8 * * ?")
    @Override
    public void gameAM8() throws Exception {

        // end_time이 null인 roomId 조회
        List<String> unendRoom = schedulerMapper.getUnendRoom();

        for (String roomId: unendRoom) {
            // 투표 결과 처리
            manageMafiaDoctorVote(roomId);;
        }

        startRandomGame();

    }

    @Scheduled(cron = "0 0 18 * * ?")
    @Override
    public void gamePM6() throws SQLException {
        // end_time이 null인 roomId 조회
        List<String> unendRoom = schedulerMapper.getUnendRoom();

        for (String roomId: unendRoom) {
            // 미션 미수행자 알림
            List<ParticipantDto> nonCompleters = schedulerMapper.getNonCompleter(roomId);

            int countNonCompleter = nonCompleters.size();
            if (countNonCompleter > 0){
                StringBuilder message = new StringBuilder(nonCompleters.get(0).getNickname());
                for (int i = 1; i<countNonCompleter; i++){
                    message.append("님, ").append(nonCompleters.get(i).getNickname());
                }
                message.append("님이 아직 미션을 수행하지 않았습니다.");
                messageService.sendGameNotice(roomId, message.toString());
            }
        }
    }

    @Scheduled(cron = "0 0 20 * * ?")
    @Override
    public void gamePM8() throws Exception {

        // end_time이 null인 roomId 조회
        List<String> unendRoom = schedulerMapper.getUnendRoom();

        for (String roomId: unendRoom) {
            // 투표 결과 처리 및 미션 미수행자 관리
            manageNormalVote(roomId);
        }

    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void gameAM12() throws SQLException {
        // end_time이 전날인 roomId의 available change
        schedulerMapper.updateFlagAvailable();

    }



    /**
     * 대기방 인원 확인 및 룸 생성 + 직업 배정 + 시작 알림
     * @param
     * @return void
     */
    @Override
    public void startRandomGame() throws Exception {
        // 대기방 유저 조회
        List<Integer> waitingUsersId = schedulerMapper.getWaitingUsersId();

        // 생성 가능한 방의 수 계산
        int availableRoom = waitingUsersId.size()/7;

        // 대기방의 유저 7명을 받아와, 방을 만들고 배정 - 방의 수만큼 반복
        for(int i = 0; i < availableRoom; i++){
            RandomNameDto randomRoomName = gameService.getRandomRoomName();

            String roomId = UUID.randomUUID().toString();
            RoomDto roomDto = RoomDto.builder()
                    .id(roomId)
                    .dataType("랜덤")
                    .title(randomRoomName.getFirstName() + " " + randomRoomName.getLastName())
                    .imageUrl(randomRoomName.getImgUrl())
                    .build();
            schedulerMapper.createRoom(roomDto);

            RoomStartReceiveDto roomStartReceiveDto = new RoomStartReceiveDto(roomId, waitingUsersId.subList(i*7, i*7+7));

            gameService.gameStart(roomStartReceiveDto);
            messageService.sendStartNotice(roomId);

            newDayMission(roomId);

        }
    }

    /**
     * 아침 알림 + 새로운 미션 배정
     * @param roomId
     * @throws Exception
     */
    public void newDayMission(String roomId) throws Exception {
        // [알림] n일차 아침이 되었습니다.
        // AM 시민 투표 알림
        messageService.sendDayNotice("아침", roomId);
        messageService.sendGameNotice(roomId, "마피아 의심자를 지목해주세요.");

        int day = messageMapper.calDay(roomId) + 1;
        gameService.createNewMission(roomId, day);
    }

    /**
     * 마피아 지목 결과 (PM 8)
     * @param roomId
     * @throws SQLException
     */
    public void manageNormalVote(String roomId) throws Exception {

        List<Integer> normalVote = schedulerMapper.getNormalVoteResult(roomId);
        int countNormalVote = normalVote.size();

        // 미션 미수행자 리스트
        List<ParticipantDto> nonCompleters = schedulerMapper.getNonCompleter(roomId);
        List<Integer> nonCompletersId = nonCompleters.stream().map(ParticipantDto::getId).toList();

        if (countNormalVote != 1) {
            // 최종 마피아 의심 대상 다수 or 투표 null
            messageService.sendGameNotice(roomId, "아무도 죽지 않았습니다.");
        } else {
            int targetId = normalVote.get(0);
            ParticipantDto participantDto;

            int index = nonCompletersId.indexOf(targetId);

            // 최종 마피아 의심 대상이 미션 수행자인 경우
            if (index == -1) {
                schedulerMapper.killParticipant(targetId);
                participantDto = schedulerMapper.getParticipant(targetId);
            }
            // 최종 마피아 의심 대상이 미션 미수행자인 경우
            else {
                schedulerMapper.manageNonCompleter(targetId);
                participantDto = nonCompleters.get(index);
                nonCompleters.remove(index);
            }

            if (participantDto.getJobId().equals("마피아")){
                messageService.sendGameNotice(roomId, participantDto.getNickname()+"님은 마피아였습니다!");
            } else{
                messageService.sendGameNotice(roomId, "선량한 시민 " +participantDto.getNickname()+"님이 처형당했습니다.");
            }
        }

        for(ParticipantDto nonCompleter: nonCompleters){
            schedulerMapper.manageNonCompleter(nonCompleter.getId());
            if (nonCompleter.getJobId().equals("마피아")){
                messageService.sendGameNotice(roomId, "마피아 " + nonCompleter.getNickname()+"님이 실종되었습니다.");
            } else{
                messageService.sendGameNotice(roomId, "시민 " +nonCompleter.getNickname()+"님이 실종되었습니다.");
            }
        }

        manageGameEnd(roomId, true);

    }


    /**
     * 의사와 타겟 지목 결과 (AM 8)
     * @param roomId
     * @throws SQLException
     */
    public void manageMafiaDoctorVote(String roomId) throws Exception {
        List<Integer> mafiaVotes = schedulerMapper.getMafiaVoteResult(roomId);
        List<Integer> doctorVotes = schedulerMapper.getDoctorVoteResult(roomId);

        // 마피아 의견 갈림
        if (mafiaVotes.size() != 1){
            messageService.sendGameNotice(roomId, "평화로운 밤이었습니다.");
            newDayMission(roomId);
        }else{
            ParticipantDto target = schedulerMapper.getParticipant(mafiaVotes.get(0));
            if (doctorVotes.size() == 1 && doctorVotes.get(0) == target.getId()){
                // 의사 의견 통일 -> 살리기
                messageService.sendGameNotice(roomId, "마피아가 " + target.getNickname() + "님을 죽이려 시도했지만 의사의 치료를 받아 살아났습니다!");
                newDayMission(roomId);
            }else{
                // 유저 flag_die 변경, 시민이었습니다 or 마피아였습니다
                schedulerMapper.killParticipant(target.getId());
                messageService.sendGameNotice(roomId, target.getNickname() + "님이 마피아에게 당했습니다.");

                manageGameEnd(roomId, false);
            }
        }
    }

    /**
     * 게임 종료 여부 판별 (PM 8, AM 8)
     * @param roomId
     * @param isNight
     * @throws SQLException
     */
    public void manageGameEnd(String roomId, boolean isNight) throws Exception {
        // 모두 동시에 죽는다면 => 무승부
        int aliveParticipant = schedulerMapper.getAliveParticipant(roomId);
        if (aliveParticipant == 0){
            endGame(-1, roomId);
        }else{
            // 죽지 않은 마피아 수가 0이라면 => 시민 승리
            int aliveMafia = schedulerMapper.getAliveMafia(roomId);
            if (aliveMafia == 0){
                endGame(0, roomId);
            }else{
                int isMafiaWin = schedulerMapper.isMafiaWin(roomId);
                if (isMafiaWin == 1){
                    // 죽지 않은 마피아 수 >= 죽지 않은 시민 수 => 마피아 승리
                    endGame(1, roomId);
                }else{
                    // 죽지 않은 마피아 수 < 죽지 않은 시민 수 => 게임 진행
                    if (isNight){
                        // [알림] n일차 밤이 되었습니다.
                        // PM 마피아와 의사 투표 알림
                        messageService.sendDayNotice("밤", roomId);
                        messageService.sendGameNotice(roomId, "타겟을 지정해주세요.");
                        messageService.sendGameNotice(roomId, "살릴 사람을 선택해주세요.");
                    }else {
                        newDayMission(roomId);
                    }
                }
            }
        }
    }

    /**
     * 게임 종료
     * @param isMafiaWin
     * @param roomId
     * @throws SQLException
     */
    public void endGame(int isMafiaWin, String roomId) throws SQLException {
        // 해당 roomId의 participant들의 flag_win 변경
        schedulerMapper.updateWinFlag(UpdateWinFlagDto.builder()
                .isMafiaWin(isMafiaWin)
                .roomId(roomId).build());

        // 해당 roomId의 end_time 변경
        schedulerMapper.updateEndTime(roomId);
        // 게임 종료 메시지 전달
        if (isMafiaWin == 1){
            messageService.sendGameNotice(roomId, "마피아가 승리하였습니다.");
        }else if (isMafiaWin == 0){
            messageService.sendGameNotice(roomId, "시민이 승리하였습니다.");
        }else {
            messageService.sendGameNotice(roomId, "모두 패배하였습니다.");
        }

        messageService.sendEndNotice(roomId);
    }
}
