package com.ssafy.msg.game.model.service;

import com.ssafy.msg.article.util.OpenAiUtil;
import com.ssafy.msg.chat.model.dto.CreateRoomDto;
import com.ssafy.msg.chat.model.dto.RoomDto;
import com.ssafy.msg.chat.model.dto.RoomResponseDto;
import com.ssafy.msg.chat.model.mapper.ChatMapper;
import com.ssafy.msg.chat.model.service.ChatService;
import com.ssafy.msg.game.exception.GroupRoomDuplicateException;
import com.ssafy.msg.game.exception.GroupRoomFullException;
import com.ssafy.msg.game.exception.GroupRoomNotFoundException;
import com.ssafy.msg.game.model.dto.*;
import com.ssafy.msg.game.model.mapper.GameMapper;
import com.ssafy.msg.game.util.GameUtil;

import com.ssafy.msg.message.model.entity.MessageEntity;
import com.ssafy.msg.message.model.mapper.MessageMapper;
//import com.ssafy.msg.message.model.service.MessageService;
import com.ssafy.msg.message.model.repo.MessageRepository;
import com.ssafy.msg.message.model.service.MessageService;
import com.ssafy.msg.notification.model.service.NotificationService;
import com.ssafy.msg.scheduler.model.mapper.SchedulerMapper;
import com.ssafy.msg.user.model.dto.UserDto;
import com.ssafy.msg.user.model.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService{

    private final GameMapper gameMapper;
    private final ChatMapper chatMapper;
    private final UserMapper userMapper;
    private final SchedulerMapper schedulerMapper;
    private final MessageMapper messageMapper;

    private final ChatService chatService;
    private final MessageService messageService;
    private final NotificationService notificationService;

    private final MessageRepository messageRepository;

    private final OpenAiUtil openAiUtil;


    @Override
    public List<ParticipantDto> getParticipants(String roomId) throws Exception {
        return gameMapper.getParticipants(roomId);
    }

    @Override
    public List<ParticipantWithFollowDto> getParticipantWithFollow(String roomId, int userId) throws Exception {
        return gameMapper.getParticipantWithFollow(roomId, userId);
    }

    @Override
    public boolean getRandomGameApplyStatus(int userId) throws Exception {
        return gameMapper.getRandomGameApplyStatus(userId);
    }

    @Override
    public boolean applyRandomGame(int userId) throws Exception {
        boolean randomGameApplyStatus = gameMapper.getRandomGameApplyStatus(userId);
        if (randomGameApplyStatus){
            return false;
        }else{
            UserDto user = userMapper.findUserById(userId);
            gameMapper.applyRandomGame(user);

            if (getTime(8, 13)){
                startRandomGame();
            }

            return true;
        }
    }

    @Override
    public boolean cancelRandomGame(int userId) throws Exception {
        boolean randomGameApplyStatus = gameMapper.getRandomGameApplyStatus(userId);
        if (randomGameApplyStatus){
            gameMapper.deleteParticipant(userId);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String useAbility(int participantId, int targetId) throws Exception {
        Integer day = gameMapper.getMaxDay(participantId);
        log.info("getAbilityTarget() day : {}", day);

        //participantId, targetId 검증
        if(day == null) {
            log.info("useAbility() player is dead");
            return "player is dead";
        }

        Integer targetAlive = gameMapper.isAlive(targetId);
        if(targetAlive != null && targetAlive != 0 || targetId == 0){
            log.info("useAbility() target is dead");
            return "target error";
        }

        GetAbilityResultDto abilityResult = gameMapper.getAbilityAvailability(new GetAbilityParamDto(participantId, day));
        String job = abilityResult.getJobId();
        log.info("useAbility() job : {}", job);

        //능력을 이미 사용한 후
        if(abilityResult.getAbility() != 0){
            log.info("useAbility() 능력을 이미 사용함");
            return "already used";
        }

//        gameMapper.setAbility(participantId, 1);
        switch (job) {
            case "경찰" -> {
                // 경찰의 능력 코드
                ParticipantDto targetDto = gameMapper.getParticipantWithPId(targetId);
                log.info("useAbility() target 이름 : {}", targetDto.getNickname());

                gameMapper.setAbility(participantId, targetId);
                String targetJob = targetDto.getJobId();

                if (GameUtil.getRoleType(targetJob).equals("마피아")) {
                    return targetDto.getNickname() + "님은 마피아가 맞습니다.";
                } else {
                    return targetDto.getNickname() + "님은 마피아가 아닙니다.";
                }
            }
            case "미치광이" -> {
                // 미치광이의 능력 코드
                ParticipantDto targetDto = gameMapper.getParticipantWithPId(targetId);
                log.info("useAbility() target 이름 : {}", targetDto.getNickname());

                gameMapper.setAbility(participantId, targetId);
                String result = "";

                if (targetId == participantId) {
                    //미치광이가 본인을 찍었다면
                    result = targetDto.getNickname() + "님은 마피아가 아닙니다.";
                } else {
                    //랜덤으로 마피아 여부 리턴
                    Random random = new Random(targetId); // ID를 시드로 사용

                    int num = random.nextInt(25);

                    log.info("useAbility() random : {}", num);

                    if (num % 2 == 0) {
                        result = targetDto.getNickname() + "님은 마피아가 맞습니다.";
                    } else {
                        result = targetDto.getNickname() + "님은 마피아가 아닙니다.";

                    }
                }

                log.info("useAbility() {}", result);
                return result;
            }
            case "훼방꾼" -> {
                // 훼방꾼의 능력 코드
                gameMapper.setAbility(participantId, 1);
                return "훼방꾼 능력을 사용했습니다. 내일 시민들은 마피아 미션을 수행하게 됩니다.";
            }
            case "건달" -> {
                ParticipantDto targetDto = gameMapper.getParticipantWithPId(targetId);
                log.info("useAbility() target 이름 : {}", targetDto.getNickname());

                // 건달의 능력 코드
                gameMapper.setAbility(participantId, targetId);
                return targetDto.getNickname() + "님의 표를 빼앗았습니다. " + targetDto.getNickname() + "님은 내일 투표할 수 없습니다.";
            }
            case "판사" -> {
                // 판사의 능력 코드
                gameMapper.setAbility(participantId, 1);
                return "판사의 능력을 사용했습니다. 당신이 선택한 사람이 마피아로 지목됩니다.";
            }
            case "변장술사" -> {
                // 변장술사의 능력 코드
                gameMapper.setAbility(participantId, 1);
                return "변장술사의 능력을 사용했습니다. 오늘 당신이 마피아로 지목당하면 당신이 투표한 사람으로 되살아납니다.";
            }
            case "스파이" -> {
                ParticipantDto targetDto = gameMapper.getParticipantWithPId(targetId);
                log.info("useAbility() target 이름 : {}", targetDto.getNickname());

                // 스파이의 능력 코드
                gameMapper.setAbility(participantId, targetId);
                return "내일 " + targetDto.getNickname() + "의 직업을 알아냅니다.";
            }
            case "정치인" -> {
                // 정치인의 능력 코드
            }
            case "군인" -> {
                // 군인의 능력 코드
            }
            default -> {
                return "no ability";
            }
        }

        return "no ability";
    }

    /**
     * 입력된 participant가 가진 직업과 밤 여부, 능력 사용 여부 등을 종합하여 현재 능력을 사용할 수 있는 상태인지 리턴합니다.
     * @param participantId participantId 입력
     * @return status 능력 사용 가능 여부, message 메시지, flagTarget 대상을 지목해야하는 능력 이라면 true
     * @throws Exception
     */
    @Override
    public AbilityTargetResponseDto getAbilityTarget(int participantId) throws Exception {
        Integer day = getMaxDay(participantId);
        log.info("getAbilityTarget() day : {}", day);
        
        if(day == null) {
            log.info("getAbilityTarget() player is dead");

            return AbilityTargetResponseDto.builder()
                    .status(false)
                    .message("player is dead")
                    .flagTarget(false)
                    .build();
        }

        AbilityTargetResponseDto resultDto = new AbilityTargetResponseDto();
        GetAbilityResultDto abilityResult = gameMapper.getAbilityAvailability(new GetAbilityParamDto(participantId, day));
        resultDto.setMessage("no ability");

        String job = abilityResult.getJobId();
        log.info("getAbilityTarget() job : {}", job);

        if(abilityResult.getAbility() == -1) {
            //능력을 이미 사용함
            log.info("getAbilityTarget() 능력을 이미 사용함");

            resultDto.setStatus(false);
            resultDto.setMessage("능력을 이미 사용했습니다.");
            resultDto.setFlagTarget(false);
        } else if (abilityResult.getAbility() > 0) {
            //능력을 사용 중
            log.info("getAbilityTarget() 능력 사용 중");
            resultDto.setMessage("능력을 사용했습니다.");
            resultDto.setStatus(false);
            resultDto.setFlagTarget(false);

            if(job.equals("경찰")) {
                ParticipantDto targetDto = gameMapper.getParticipantWithPId(abilityResult.getAbility());

                String targetJob = targetDto.getJobId();

                if (GameUtil.getRoleType(targetJob).equals("마피아")) {
                    resultDto.setMessage(targetDto.getNickname() + "님은 마피아가 맞습니다.");
                } else {
                    resultDto.setMessage(targetDto.getNickname() + "님은 마피아가 아닙니다.");
                }

            } else if(job.equals("미치광이")) {
                int targetId = abilityResult.getAbility();
                ParticipantDto targetDto = gameMapper.getParticipantWithPId(targetId);

                String result = "";

                if (targetId == participantId) {
                    //미치광이가 본인을 찍었다면
                    result = targetDto.getNickname() + "는 마피아가 아닙니다.";
                } else {
                    //랜덤으로 마피아 여부 리턴
                    Random random = new Random(targetId); // ID를 시드로 사용

                    int num = random.nextInt(25);

                    log.info("useAbility() random : {}", num);

                    if (num % 2 == 0) {
                        result = targetDto.getNickname() + "는 마피아가 맞습니다.";
                    } else {
                        result = targetDto.getNickname() + "는 마피아가 아닙니다.";
                    }
                }
//                if (!GameUtil.getRoleType(targetDto.getJobId()).equals("마피아")) {
//                    resultDto.setMessage(targetDto.getNickname() + "님은 마피아가 맞습니다.");
//                } else {
//                    resultDto.setMessage(targetDto.getNickname() + "님은 마피아가 아닙니다.");
//                }

                resultDto.setMessage(result);
            } else if(job.equals("스파이")) {
                ParticipantDto targetDto = gameMapper.getParticipantWithPId(abilityResult.getAbility());
                
                if(abilityResult.getFlagNight() == 0) {
                    //낮이라면
                    resultDto.setMessage(targetDto.getNickname() + "님의 직업은 " + targetDto.getJobId() + "입니다.");
                } else {
                    //밤이라면
                    resultDto.setMessage("내일 " + targetDto.getNickname() + "의 직업을 알아냅니다.");
                }
            } else if (job.equals("건달")) {
                ParticipantDto targetDto = gameMapper.getParticipantWithPId(abilityResult.getAbility());
                if(abilityResult.getFlagNight() == 0) {
                    //낮이라면
                    resultDto.setMessage(targetDto.getNickname() + "님의 표를 빼앗았습니다. " + targetDto.getNickname() + "님은 오늘 투표할 수 없습니다.");
                } else {
                    //밤이라면
                    resultDto.setMessage(targetDto.getNickname() + "님의 표를 빼앗았습니다. " + targetDto.getNickname() + "님은 내일 투표할 수 없습니다.");
                }
            } else if(job.equals("판사")){
                resultDto.setMessage("판사의 능력을 사용했습니다. 당신이 선택한 사람이 마피아로 지목됩니다.");
            } else if(job.equals("변장술사")){
                resultDto.setMessage("변장술사의 능력을 사용했습니다. 오늘 당신이 마피아로 지목당하면 당신이 투표한 사람으로 되살아납니다.");
            } else if(job.equals("훼방꾼")){
                resultDto.setMessage("훼방꾼 능력을 사용했습니다. 내일 시민들은 마피아 미션을 수행하게 됩니다.");
            }
            else {
                resultDto.setStatus(false);
                resultDto.setMessage("능력을 이미 사용했습니다.");
            }

        } else if (abilityResult.getAbility() == 0) {
            //능력을 사용하기 전
            log.info("getAbilityTarget() 능력 사용 전");

            //능력 사용 전 직업별 상태 체크
            if(job.equals("경찰") || job.equals("미치광이")) {
                // 경찰의 능력
                if(abilityResult.getFlagNight() == 1) {
                    //밤이라면
                    resultDto.setMessage("마피아 여부를 알고싶은 대상을 선택하세요.");
                    resultDto.setFlagTarget(true); //사람 고르기
                    resultDto.setStatus(true);
                } else {
                    //낮이라면
                    resultDto.setMessage("지금은 사용할 수 없습니다.");
                    resultDto.setFlagTarget(false);
                    resultDto.setStatus(false);
                }
            } else if (job.equals("훼방꾼")) {
                // 훼방꾼의 능력
                if(abilityResult.getFlagNight() == 1) {
                    //밤이라면
                    resultDto.setMessage("내일 시민들의 미션을 마피아 미션으로 만듭니다.");
                    resultDto.setFlagTarget(false);
                    resultDto.setStatus(true);
                } else {
                    //낮이라면
                    resultDto.setMessage("지금은 사용할 수 없습니다.");
                    resultDto.setFlagTarget(false);
                    resultDto.setStatus(false);
                }
            } else if (job.equals("건달")) {
                // 건달의 능력
                if(abilityResult.getFlagNight() == 1) {
                    //밤이라면
                    resultDto.setMessage("내일 투표권을 압수하고 싶은 사람을 고르세요.");
                    resultDto.setFlagTarget(true); //사람 고르기
                    resultDto.setStatus(true);
                } else {
                    //낮이라면
                    resultDto.setMessage("지금은 사용할 수 없습니다.");
                    resultDto.setFlagTarget(false);
                    resultDto.setStatus(false);
                }
            } else if (job.equals("판사")) {
                // 판사의 능력
                if(abilityResult.getFlagNight() == 1) {
                    //밤이라면
                    resultDto.setMessage("지금은 사용할 수 없습니다.");
                    resultDto.setFlagTarget(false);
                    resultDto.setStatus(false);
                } else {
                    //낮이라면
                    resultDto.setMessage("내가 투표한 사람이 마피아로 지목됩니다.");
                    resultDto.setFlagTarget(false); //사람 고르기
                    resultDto.setStatus(true);
                }
            } else if (job.equals("변장술사")) {
                // 변장술사의 능력
                if(abilityResult.getFlagNight() == 1) {
                    //밤이라면
                    resultDto.setMessage("지금은 사용할 수 없습니다.");
                    resultDto.setFlagTarget(false);
                    resultDto.setStatus(false);
                } else {
                    //낮이라면
                    resultDto.setMessage("내가 마피아로 지목되었을 때, 선택한 사람으로 살아갑니다.");
                    resultDto.setFlagTarget(true); //사람 고르기
                    resultDto.setStatus(true);
                }
            } else if (job.equals("스파이")) {
                // 스파이의 능력
                if(abilityResult.getFlagNight() == 1) {
                    //밤이라면
                    resultDto.setMessage("직업을 알고싶은 사람을 선택하세요.");
                    resultDto.setFlagTarget(true); //사람 고르기
                    resultDto.setStatus(true);
                } else {
                    //낮이라면
                    resultDto.setMessage("지금은 사용할 수 없습니다.");
                    resultDto.setFlagTarget(false);
                    resultDto.setStatus(false);
                }
            } else if (job.equals("정치인")) {
                // 정치인의 능력
                resultDto.setMessage("마피아로 지목되어도 한 번 되살아 납니다.");
                resultDto.setFlagTarget(false);
                resultDto.setStatus(false);
            } else if (job.equals("군인")) {
                // 군인의 능력
                resultDto.setMessage("마피아에게 지목되어도 한 번 되살아 납니다.");
                resultDto.setFlagTarget(false);
                resultDto.setStatus(false);
            } else {
                resultDto.setMessage("사용할 능력이 없습니다.");
                resultDto.setFlagTarget(false);
                resultDto.setStatus(false);
            }

        }

        return resultDto;
    }

    /**
     * userId와 roomId를 입력받아 id및 participant 생존여부 등을 체크하고 day를 구해 해당 participant의 mission에 완료 처리를 함
     * @param userId userId를 입력받음
     * @param roomId 게임방Id를 입력받음
     * @return int result 결과를 리턴함. 정상:1, 유저 사망 등:-1, 업데이트 실패: 0
     * @throws Exception
     */
    @Override
    public int completeMission(int userId, String roomId) throws Exception {
        //유저 검증
        ParticipantDto participant = gameMapper.getParticipant(new ParticipantReceiveDto(userId, roomId));

        if(participant == null){
            log.info("completeMission() can not find participant");
            return -1;
        }

        int participantId = participant.getId();
        Integer day = getMaxDay(participantId);
        
        //죽었는지 체크
        if(day == null) {
            log.info("completeMission() participant is dead");
            return -1;
        }

        int result = gameMapper.completeMission(participantId, day);
        log.info("completeMission() result : {}", result);

        
        return result;
    }

    /**
     * 이미지와 조건을 입력받아 해당 이미지가 조건에 부합하는지 gpt에게 요청을 보냅니다.
     * json내에 content의 내용을 체크하여 dto로 만들어 리턴합니다.
     * @param imageFile 검사할 사진
     * @param condition 조건
     * @return AiResultDto 합격 여부와 이유를 담아 리턴합니다.
     * @throws Exception
     */
    @Override
    public AiResultDto analyzeImage(MultipartFile imageFile, String condition) throws Exception {
        log.info("analyzeImage() condition : {}", condition);

        OpenAiApiResponseDto result = openAiUtil.analyzeImage(imageFile, condition);
        AiResultDto aiResult = new AiResultDto();

        if(result != null) {
            //응답이 비어있지 않다면

            String content = result.getChoices().get(0).getMessage().getContent();
            log.info("analyzeImage() result : {}", content);

            if(content.contains("true") || content.contains("True")) {
                //응답이 true일 때
                log.info("analyzeImage() true");
                aiResult.setResult(true);
            } else if(content.contains("false") || content.contains("False")) {
                //응답이 false일 때
                log.info("analyzeImage() false");
                aiResult.setResult(false);
            } else {
                //응답이 잘못 됨
                log.info("analyzeImage() ai 형식 오류");
                aiResult.setResult(false);
            }

            //content 내에 이유: 뒤 부터 자르기 위해 index를 가져온다.
            String keyString = "이유:";
            int keyIndex = content.indexOf(keyString);

            if (keyIndex != -1) {
                // 정상적으로 이유: 가 포함되어 있다면 뒷 부분을 잘라 dto에 담습니다.
                String reason = content.substring(keyIndex + keyString.length()).trim();
                log.info("analyzeImage() 이유 : {}", reason);

                aiResult.setReason(reason);
            } else {
                log.info("analyzeImage() 이유: 가 포함되지 않음");
                aiResult.setReason("오류");
            }
        }

        return aiResult;
    }

    /**
     * participant가 살아있다면 현재 누구를 지목 중인지 리턴합니다.
     * @param participantId participantId를 입력받음
     * @return 누구를 지목 중인지 nickname을 리턴합니다.
     * @throws Exception
     */
    @Override
    public String getMyVote(int participantId) throws Exception {
        Integer day = getMaxDay(participantId);

        if(day == null) { //죽었다면, maxday가 다르다면
            log.info("getMyVote() player is dead");
            return "participant is dead";
        }

        MyVoteDto dto = gameMapper.getMyVote(participantId, day);
        GetAbilityResultDto abilityResultDto = gameMapper.getAbilityAvailability(new GetAbilityParamDto(participantId, day));
        String job = abilityResultDto.getJobId();
        String faction = GameUtil.getRoleType(job);

        log.info("getMyVote() myVote : {}", dto);
        log.info("getMyVote() job : {}", job);
        log.info("getMyVote() faction : {}", faction);
        log.info("getMyVote() flagNight : {}", abilityResultDto.getFlagNight());

//        if (getTime(8, 20)) {
        if(abilityResultDto.getFlagNight() == 0) {
            //낮
            log.info("getMyVote() result : {}", dto.getNormalVote());
            return dto.getNormalVote();
        } else {
            if(faction.equals("마피아")) {
                log.info("getMyVote() result : {}", dto.getMafiaVote());
                return dto.getMafiaVote();
            } else if(job.equals("의사")) {
                log.info("getMyVote() result : {}", dto.getDoctorVote());
                return dto.getDoctorVote();
            } else if(job.equals("기자")) {
                log.info("getMyVote() result : {}", dto.getReporterVote());
                return dto.getReporterVote();
            } else {
                return dto.getNormalVote();
            }
        }
    }

    @Override
    public boolean isParticipantInRoom(EnterGroupRoomDto enterGroupRoomDto) throws Exception {
        return gameMapper.isParticipantInRoom(enterGroupRoomDto);
    }

    @Override
    public RoomDto createEnterGroupRoom(int userId) throws Exception {
        UserDto user = userMapper.findUserById(userId);

        String roomId = UUID.randomUUID().toString();

        RandomNameDto randomNameDto = getRandomRoomName();

        RoomDto roomDto = RoomDto.builder()
                .id(roomId)
                .dataType("그룹")
                .title(randomNameDto.getFirstName()+" "+randomNameDto.getLastName())
                .imageUrl(randomNameDto.getImgUrl())
                .build();

        chatMapper.createRoom(roomDto);

        ParticipantDto participant = ParticipantDto.builder()
                .roomId(roomId)
                .userId(userId)
                .imageUrl(user.getImageUrl())
                .nickname(user.getNickname())
                .build();

        chatMapper.enterRoom(participant);
        notificationService.sendRoomSubscribeRequest(userId, roomId);

        messageService.sendEnterNotice(participant);

        return chatMapper.getRoom(roomId);
    }

    @Override
    public RoomDto enterGroupRoom(EnterGroupRoomDto enterGroupRoomDto) throws Exception {

        String roomId = enterGroupRoomDto.getRoomId();
        RoomDto roomDto = chatMapper.getRoom(roomId);

        if(roomDto == null){
            throw new GroupRoomNotFoundException();
        }else {
            boolean isParticipantInRoom = gameMapper.isParticipantInRoom(enterGroupRoomDto);
            if (isParticipantInRoom){
                throw new GroupRoomDuplicateException();
            }else {
                List<ParticipantIdDto> participants = gameMapper.getParticipantsInRoom(roomId);
                if (participants.size() >= 7){
                    throw new GroupRoomFullException();
                }else {
                    UserDto user = userMapper.findUserById(enterGroupRoomDto.getUserId());

                    ParticipantDto participant = ParticipantDto.builder()
                            .roomId(roomId)
                            .userId(user.getId())
                            .imageUrl(user.getImageUrl())
                            .nickname(user.getNickname())
                            .build();

                    chatMapper.enterRoom(participant);
                    notificationService.sendRoomSubscribeRequest(user.getId(), roomId);

                    messageService.sendEnterNotice(participant);

                    if (participants.size() == 6){
                        if (getTime(8, 13)){
                            startGroupGame(roomId, roomDto.getTitle(), gameMapper.getParticipantsInRoom(roomId));
                        }
                    }
                }
            }
        }
        return roomDto;
    }

    @Override
    public void inviteGroupRoom(int userId, EnterGroupRoomDto enterGroupRoomDto) throws Exception {

        CreateRoomDto createRoomDto = CreateRoomDto.builder()
                .userId1(userId)
                .userId2(enterGroupRoomDto.getUserId()).build();

        Map map = chatService.getPersonalRoom(createRoomDto);

        String roomId = ((RoomDto) map.get("roomDto")).getId();

        // roomId로 초대 코드 (enterGroupRoomDto.getRoomId()) 와 함께 메시지 전송
        messageService.sendInvitation(userId, roomId, enterGroupRoomDto.getRoomId());

    }


    @Override
    public List<String> getRandomNicknames(int limit) throws Exception {
        List<String> list = new ArrayList<>();

        List<RandomNameDto> result = gameMapper.getRandomNicknames(limit);

        for(RandomNameDto name : result) {
            if (name != null) {
                list.add(name.getFirstName() + " " + name.getLastName() + " " + name.getImgUrl());
            }
        }

        return list;
    }

    @Override
    public RandomNameDto getRandomRoomName() throws Exception{
        return gameMapper.getRandomRoomName();
    }

    /**
     * dto에 roomId와 userEmail을 담은 리스트를 담아
     * 해당 유저들을 room의 participants로 만들어 준다
     * 랜덤 이름과 아이콘을 부여하고 직업을 배정한다.
     * 인원은 6~7명을 기준으로 한다.
     * @param roomStartReceiveDto roomId와 userList를 담은 Dto
     * @return List partcipants를 리턴한다.
     */
    @Override
    public List<ParticipantDto> randomGameStart(RoomStartReceiveDto roomStartReceiveDto) throws Exception{
        int numOfPlayers = roomStartReceiveDto.getUserList().size();
        String roomId = roomStartReceiveDto.getRoomId();
        List<RandomNameDto> randomNicknames = null;
        List<ParticipantDto> result = new ArrayList<>();

        log.info("gameStart() -> numOfPlayers : {}", numOfPlayers);


        randomNicknames = gameMapper.getRandomNicknames(numOfPlayers);


        List<String> randomJobs = getJobs(numOfPlayers);

        for(int i = 0; i  < numOfPlayers; i++) {
            ParticipantDto participant = ParticipantDto.builder()
                    .roomId(roomId)
                    .userId(roomStartReceiveDto.getUserList().get(i))
                    .nickname(randomNicknames.get(i).getFirstName() + " " + randomNicknames.get(i).getLastName())
                    .jobId(randomJobs.get(i))
                    .imageUrl(randomNicknames.get(i).getImgUrl())
                    .build();

            result.add(participant);
        }

        int resultCnt = gameMapper.insertParticipants(result);

        for(int userId: roomStartReceiveDto.getUserList()){
            gameMapper.deleteParticipant(userId);
        }

        log.info("gameStart() resultCnt : {}", resultCnt);

        return result;
    }

    /**
     * 숫자 만큼 역할을 배정하여 랜덤으로 섞은 리스트를 리턴
     * 6~7명을 상정하여 마피아2 의사1 고정
     * @param num 역할의 수
     * @return 무작위 리스트
     */
    @Override
    public List<String> getJobs(int num) {
        List<String> result = new ArrayList<>();

        //랜덤 직업을 바로 배정
        result.addAll(GameUtil.getMafiaRoles(2));
        result.addAll(GameUtil.getCivilRoles(num - 2));

        Collections.shuffle(result);

//        result.add("판사");   //공정한 커닝햄
//        result.add("경찰");      //정의로운 젠킨스
//        result.add("훼방꾼");  //방해되는 드미트리
//        result.add("불침번");  //잠이 없는 니콜슨
//        result.add("의사");   //영리한 제임스
//        result.add("건달");   //건방진 게릭
//        result.add("미치광이");//광적인 켄달

        log.info("getJobs() result : {}", result);

        return result;
    }

    /**
     * user email을 입력받아 user의 rooms 중, type이 "랜덤", "그룹"인 게임방 리스틀 리턴
     * @param userId 유저 id
     * @return Rooms list를 반환
     */
    @Override
    public List<RoomResponseDto> getUserRooms(int userId) throws Exception{
        List<RoomResponseDto> userRooms = gameMapper.getUserRooms(userId);

        for (RoomResponseDto room: userRooms){
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

        return userRooms;
    }

    /**
     * roomId와 userEmail을 입력받아 해당 방의 유저가 지금 볼 수 있는 투표 현황을 리턴한다.
     * @param userId 유저의 id
     * @param roomId roomId
     * @return 모든 유저가 각 몇 표를 받았는지의 정보가 담긴 list return
     */
    @Override
    public GetRoomVoteResult getRoomVote(int userId, String roomId) throws Exception {
        ParticipantDto participantDto = gameMapper.getParticipant(new ParticipantReceiveDto(userId, roomId));

        if(participantDto.getFlagDie() == 1) {
            //죽었다면
            return new GetRoomVoteResult("당신은 죽었습니다.", null);
        }

        String job = participantDto.getJobId();
        List<VoteResultDto> list = gameMapper.getRoomVote(roomId);
        String voteTitle = "마피아로 의심되는 사람을 선택하세요.";

        log.info("getRoomVote() -> job : {}", job);
        log.info("getRoomVote() -> list : {}", list);
        log.info("getRoomVote() -> roomId : {}", roomId);

        int nightFlag = gameMapper.getFlagNight(roomId);
        log.info("getRoomVote() nightFlag : {}", nightFlag);

        List<VoteResponseDto> responseList = new ArrayList<>();

        for(VoteResultDto vote : list){
            VoteResponseDto voteResult = VoteResponseDto.builder()
                    .id(vote.getId())
                    .nickname(vote.getNickname())
                    .imageUrl(vote.getImageUrl())
                    .build();
//            if (getTime(8, 20)) { // 낮일 때
            if(nightFlag == 0) { // 낮일 때
                voteResult.setVoteCount(vote.getNormalVoteCount());
            } else { //밤일 때
                if (job.equals("의사")) { //의사일 때
                    voteTitle = "치료할 사람을 선택하세요.";
                    voteResult.setVoteCount(vote.getDoctorVoteCount());
                } else if (job.equals("기자")) { //기자일 때
                    voteTitle = "조사하고 싶은 사람을 선택하세요.";
                    voteResult.setVoteCount(vote.getReporterVoteCount());
                } else if (GameUtil.getRoleType(job).equals("마피아")) { //마피아일 때
                    voteTitle = "처형할 사람을 선택하세요.";
                    voteResult.setVoteCount(vote.getMafiaVoteCount());
                } else if (GameUtil.getRoleType(job).equals("시민")) {
                    log.info("getRoomVote() 시민에게 보여줄 투표가 없음");
                    voteTitle = "현재 투표가 없습니다.";

                    return new GetRoomVoteResult(voteTitle, responseList);
                }
            }

            responseList.add(voteResult);
        }


        log.info("getRoomVote() resultList : {}", responseList);
        log.info("getRoomVote() title : {}", voteTitle);

        return new GetRoomVoteResult(voteTitle, responseList);
    }

    /**
     * userEmail과 roomId를 입력받아 해당 유저의 participant를 리턴
     * @param userId 유저  id
     * @param roomId 룸 id
     * @return participant 리턴
     */
    @Override
    public ParticipantResponseDto getParticipant(int userId, String roomId) throws Exception {
        ParticipantDto participantDto = gameMapper.getParticipant(new ParticipantReceiveDto(userId, roomId));

        return new ParticipantResponseDto(participantDto);
    }

    /**
     * roomId를 입력받아 살아있는 participant 리스트를 리턴
     * @param roomId roomId
     * @return list participantList
     * @throws Exception
     */
    @Override
    public List<AliveParticipantDto> getAliveParticipant(String roomId) throws Exception {
        return gameMapper.getAliveParticipants(roomId);
    }

    /**
     * 유저의 가장 높은 day가 room의 가장 높은 day와 같다면 day를 리턴하고 아니라면 null을 리턴함
     * @param participantId
     * @return Integer day or null
     * @throws Exception
     */
    @Override
    public Integer getMaxDay(int participantId) throws Exception {
        if(gameMapper.isAlive(participantId) != 0) {
            log.info("getMaxDay() participant is dead");
            return null;
        } else {
            Integer day = gameMapper.getMaxDay(participantId);
            log.info("getMaxDay() day : {}", day);

            return day;
        }
    }

    /**
     * participantId를 입력받아 현재 진행 중인 미션을 리턴
     * @param participantId
     * @return MissionResultDto 현재 진행 중인 미션 (일반, 마피아)
     * @throws Exception
     */
    @Override
    public MissionResultDto getMyMission(int participantId) throws Exception {
        Integer day = getMaxDay(participantId);

        if(day == null) {
            log.info("getMyMission() participant is dead");
            return null;
        } else {
            //훼방꾼 체크
            Integer checkSaboteur = gameMapper.checkSaboteur(participantId);
            MissionResultDto mission = gameMapper.getMyMission(participantId, day);

            if(checkSaboteur == 1){
                //훼방꾼이 능력을 사용했다면
                mission.setNormalMission(mission.getMafiaMission());
                return mission;
            } else {
                //훼방꾼이 능력을 사용하지 않았다면
                return mission;
            }

        }
    }

    /**
     * 유저의 랜덤 게임 플레이 횟수를 가져옵니다.
     * 전체 플레이 횟수 및 승리 수 부터 마피아, 시민 게임 플레이 횟수 및 승리 수를 가져옵니다.
     * @param userId userId를 입력받는다.
     * @return 게임 횟수, 승리 수, 시민 게임 횟스, 승리 수, 마피아 게임 횟수, 승리수
     * @throws Exception
     */
    @Override
    public UserGameRateResultDto getUserRate(int userId) throws Exception {
        //유저 검증 필요?
        List<UserEndGameDto> gameList = gameMapper.getUserEndGame(userId);
        int totalGameCnt = gameList.size(); // 모든 게임 플레이 수

        // 게임 플레이 수가 0인 경우, 0만 담긴 DTO 반환
        if (totalGameCnt == 0) {
            log.info("getUserRate() no play log");
            return new UserGameRateResultDto(0, 0, 0, 0, 0, 0);
        }

        int totalWinCnt = 0;    //모든 게임 승리 수
        int civilGameCnt = 0;   //시민으로 게임한 횟수
        int civilWinCnt = 0;    //시민으로 승리한 횟수
        int mafiaGameCnt = 0;   //마피아로 게임한 횟수
        int mafiaWinCnt = 0;    //마피아로 승리한 횟수

        for(UserEndGameDto dto : gameList) {
            if(dto.getFlagWin() == 1){
                //이겼을 때
                totalWinCnt++;
                if(GameUtil.getRoleType(GameUtil.getRoleType(dto.getJobId())).equals("마피아")) {
                    //마피아로 승리했을 때
                    mafiaGameCnt++;
                    mafiaWinCnt++;
                } else {
                    //시민, 의사로 승리했을 때
                    civilGameCnt++;
                    civilWinCnt++;
                }

            } else if(dto.getFlagWin() == 0) {
                //졌을 때
                if(GameUtil.getRoleType(GameUtil.getRoleType(dto.getJobId())).equals("마피아")) {
                    //마피아 패배했을 때
                    mafiaGameCnt++;
                } else {
                    //시민, 의사로 패배했을 때
                    civilGameCnt++;
                }
            }
        }

        log.info("getUserRate() - Total Games: {}, Total Wins: {}, Mafia Games: {}, Mafia Wins: {}, Civilian Games: {}, Civilian Wins: {}",
                totalGameCnt, totalWinCnt, mafiaGameCnt, mafiaWinCnt, civilGameCnt, civilWinCnt);

        return UserGameRateResultDto.builder()
                .totalGameCnt(totalGameCnt)
                .totalWinCnt(totalWinCnt)
                .mafiaGameCnt(mafiaGameCnt)
                .mafiaWinCnt(mafiaWinCnt)
                .civilGameCnt(civilGameCnt)
                .civilWinCnt(civilWinCnt)
                .build();
    }

    /**
     * 직업과 participantID, targetId를 입력받아 시간에 따른 투표를 반영합니다.
     * @param voteReceiveDto 직업과 participantID, targetId를 입력받아
     * @throws Exception
     */
    @Override
    public String vote(VoteReceiveDto voteReceiveDto) throws Exception {
        Integer day = getMaxDay(voteReceiveDto.getParticipantId());

        if(day == null) {
            log.info("vote() participant is dead");
            return "participant is dead";
        }

        GetAbilityResultDto abilityDto = gameMapper.getAbilityAvailability(new GetAbilityParamDto(voteReceiveDto.getParticipantId(), day));

        if(abilityDto.getFlagSuccess() == 0){
            log.info("vote() mission uncompleted");
            return "mission uncompleted";
        }

//        if(getTime(8, 20)){
        if(abilityDto.getFlagNight() == 0) {
            //낮 08:00 - 20:00

            Integer gangsterVote = schedulerMapper.getGangsterVoteResult(voteReceiveDto.getParticipantId());
            if(gangsterVote != null && gangsterVote == voteReceiveDto.getParticipantId()){
                //건달에게 표를 빼앗겼을 때
                log.info("vote() 건달에게 표를 빼앗김");
                return "you lost your vote to a gangster.";
            }

            //시민 투표
            log.info("vote() -> normalVote : {}", voteReceiveDto.getTargetId());
            gameMapper.normalVote(voteReceiveDto.getParticipantId(), voteReceiveDto.getTargetId(), day);
        } else {
            //밤
            if (GameUtil.getRoleType(voteReceiveDto.getJobId()).equals("마피아")){
                //마피아 투표
                log.info("vote() -> mafiaVote : {}", voteReceiveDto.getTargetId());
                gameMapper.mafiaVote(voteReceiveDto.getParticipantId(), voteReceiveDto.getTargetId(), day);
            } else if(voteReceiveDto.getJobId().equals("의사")){
                //의사 투표
                log.info("vote() -> doctorVote : {}", voteReceiveDto.getTargetId());
                gameMapper.doctorVote(voteReceiveDto.getParticipantId(), voteReceiveDto.getTargetId(), day);
            } else if (voteReceiveDto.getJobId().equals("기자")) {
                //기자 투표
                log.info("vote() -> reporterVote : {}", voteReceiveDto.getTargetId());
                gameMapper.reporterVote(voteReceiveDto.getParticipantId(), voteReceiveDto.getTargetId(), day);
            }
        }

        return "vote completed";
    }

    /**
     * 현재 시간으로 start:00 - end:00 사이라면 true, 아니라면 false를 리턴하는 함수
     * @return start:00 - end:00 true, else false
     */
    @Override
    public boolean getTime(int start, int end) {
        // 서울 표준시 기준으로 현재 시간을 가져옵니다.
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        log.info("getTime() -> now : {}", now);
        // 시작 시간과 종료 시간을 설정합니다.
        LocalTime startTime = LocalTime.of(start, 0);
        LocalTime endTime = LocalTime.of(end, 0);

        // 현재 시간이 시작 시간과 종료 시간 사이인지 확인합니다.
        if (now.toLocalTime().isAfter(startTime) && now.toLocalTime().isBefore(endTime)) {
            log.info("getRoomTime() 현재 시간은 {}:00과 {}:00 사이입니다.", start, end);
            return true;
        } else {
            log.info("getRoomTime() 현재 시간은 {}:00과 {}:00 사이입니다.", start, end);
            return false;
        }
    }

    /**
     * roomId와 day를 입력받아 해당 room에서 수행한적 없는 미션을 랜덤으로 골라
     * 모든 participant에게 새로운 daily미션을 입력받은 day로 만든다.
     * 모든 dailyMission은 생성될 때 기본적으로 각 보트가 본인을 찍음
     * 마피아나 의사, 기자가 아니라면 해당 vote는 null로 처리
     * @param roomId 새로운 미션을 분배할 roomId 입력
     * @param day   새로 만들 미션의 날짜
     * @throws Exception
     */
    @Override
    public void createNewMission(String roomId, int day) throws Exception {
        log.info("createNewMission() -> roomId : {}", roomId);
        log.info("createNewMission() -> day : {}", day);

        //입력받은 게임방에서 나온적 없는 미션 중 하나를 랜덤으로 고릅니다.
        int randMission = gameMapper.getRandomMission(roomId);
        log.info("createNewMission() randMissing : {}", randMission);

        //선택된 랜덤 게임을 입력받은 roomId의 모든 participant에게 보내준다.
        NewMissionDto newMissionDto = NewMissionDto.builder().roomId(roomId).missionId(randMission).day(day).build();
        gameMapper.createNewMission(newMissionDto);

        log.info("createNewMission() -> new mission created");
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
            RandomNameDto randomRoomName = getRandomRoomName();

            String roomId = UUID.randomUUID().toString();
            RoomDto roomDto = RoomDto.builder()
                    .id(roomId)
                    .dataType("랜덤")
                    .title(randomRoomName.getFirstName() + " " + randomRoomName.getLastName())
                    .imageUrl(randomRoomName.getImgUrl())
                    .build();
            schedulerMapper.createRoom(roomDto);

            RoomStartReceiveDto roomStartReceiveDto = new RoomStartReceiveDto(roomId, roomDto.getTitle(), waitingUsersId.subList(i*7, i*7+7));

            List<ParticipantDto> participantDtos = randomGameStart(roomStartReceiveDto);

            for(ParticipantDto participantDto: participantDtos){
                notificationService.sendRoomSubscribeRequest(participantDto.getUserId(), roomStartReceiveDto.getRoomId());
                notificationService.sendGameStartNotice(participantDto.getUserId(), roomStartReceiveDto.getRoomTitle(), "랜덤");
            }
            messageService.sendStartNotice(roomId);

            newDayMission(roomId);

        }
    }

    @Override
    public void startGroupGame(String roomId, String roomTitle, List<ParticipantIdDto> participantList) throws Exception{
        int numOfPlayers = participantList.size();
        List<RandomNameDto> randomNicknames = null;

        log.info("startGroupGame() -> roomId : {}", roomId);

        // 각 participant 랜덤 닉네임, 랜덤 직업, 랜덤 이미지로 update
        randomNicknames = gameMapper.getRandomNicknames(numOfPlayers);



//        randomNicknames.add(new RandomNameDto("공정한", "커닝햄", "https://team109testbucket.s3.ap-northeast-2.amazonaws.com/player19.png"));
//        randomNicknames.add(new RandomNameDto("정의로운", "드미트리", "https://team109testbucket.s3.ap-northeast-2.amazonaws.com/player1.png"));
//        randomNicknames.add(new RandomNameDto("방해되는", "젠킨스", "https://team109testbucket.s3.ap-northeast-2.amazonaws.com/player2.png"));
//        randomNicknames.add(new RandomNameDto("잠이 없는", "니콜슨", "https://team109testbucket.s3.ap-northeast-2.amazonaws.com/player3.png"));
//        randomNicknames.add(new RandomNameDto("영리한", "제임스", "https://team109testbucket.s3.ap-northeast-2.amazonaws.com/player10.png"));
//        randomNicknames.add(new RandomNameDto("건방진", "게릭", "https://team109testbucket.s3.ap-northeast-2.amazonaws.com/player15.png"));
//        randomNicknames.add(new RandomNameDto("광적인", "켄달", "https://team109testbucket.s3.ap-northeast-2.amazonaws.com/player20.png"));

        List<String> randomJobs = getJobs(numOfPlayers);

        for(int i = 0; i  < numOfPlayers; i++) {
            UpdateParticipantDto updateParticipantDto = UpdateParticipantDto.builder()
                    .id(participantList.get(i).getId())
                    .nickname(randomNicknames.get(i).getFirstName() + " " + randomNicknames.get(i).getLastName())
                    .jobId(randomJobs.get(i))
                    .imageUrl(randomNicknames.get(i).getImgUrl())
                    .build();

            gameMapper.updateParticipant(updateParticipantDto);
        }

        gameMapper.updateStartTime(roomId);

        for(int i = 0; i < numOfPlayers; i++){
            notificationService.sendGameStartNotice(participantList.get(i).getUserId(), roomTitle, "그룹");
        }
        messageService.sendStartNotice(roomId);
        newDayMission(roomId);
    }

    /**
     * 아침 알림 + 새로운 미션 배정
     * @param roomId
     * @throws Exception
     */
    @Override
    public void newDayMission(String roomId) throws Exception {
        // [알림] n일차 아침이 되었습니다.
        // AM 시민 투표 알림
        messageService.sendDayNotice("아침", roomId);
        messageService.sendGameNotice(roomId, "마피아 의심자를 지목해주세요.");

        int day = messageMapper.calDay(roomId) + 1;
        createNewMission(roomId, day);
    }

    @Override
    public void createMsgArticle(CreateMsgArticleDto createMsgArticleDto) throws Exception {

    }
}
