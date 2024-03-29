package com.ssafy.msg.game.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class GameUtil {
    private static final Map<String, String> roles = new HashMap<>();
    private static final List<String> civilRoles = new ArrayList<>();
    private static final List<String> mafiaRoles = new ArrayList<>();


    static {
        //직업의 세력
        //시민 직업
        roles.put("경찰", "시민");
        roles.put("정치인", "시민");
        roles.put("군인", "시민");
        roles.put("판사", "시민");
        roles.put("미치광이", "시민");
        roles.put("불침번", "시민");
//        roles.put("청소부", "시민");
        roles.put("기자", "시민");
        roles.put("의사", "시민");
        roles.put("자경단", "시민");
//        roles.put("사냥꾼", "시민");
//        roles.put("본인이 죽으면 마피아 공개시키기", "시민");

        //마피아 직업
        roles.put("훼방꾼", "마피아");
//        roles.put("변장술사", "마피아");
        roles.put("스파이", "마피아");
        roles.put("건달", "마피아");
        roles.put("마피아", "마피아");

        //랜덤 출력을 위한 list
        //마피아
        mafiaRoles.add("훼방꾼");
//        mafiaRoles.add("변장술사");
        mafiaRoles.add("스파이");
        mafiaRoles.add("건달");
        mafiaRoles.add("마피아");

        //시민
        civilRoles.add("경찰");
        civilRoles.add("정치인");
        civilRoles.add("군인");
        civilRoles.add("판사");
        civilRoles.add("미치광이");
        civilRoles.add("불침번");
//        civilRoles.add("청소부");
        civilRoles.add("기자");
        civilRoles.add("의사");
        civilRoles.add("자경단");
    }

    /**
     * 직업을 입력하면 해당 직업의 세력(마피아, 시민)을 리턴
     * @param roleName 세력을 알고싶은 직업을 입력합니다.
     * @return 입력받은 직업의 세력을 리턴
     */
    public static String getRoleType(String roleName) {

        return roles.getOrDefault(roleName, "알 수 없는 직업");
    }

    /**
     * 입력된 숫자 만큼 랜덤한 시민의 직업을 리턴
     * @param num 몇 개의 직업을 리턴할지 입력
     * @return 랜덤으로 num 만큼의 랜덤 직업 출력
     */
    public static List<String> getCivilRoles(int num){
        Collections.shuffle(civilRoles);
        List<String> result = new ArrayList<>();

        while(num-->0) {
            result.add(civilRoles.get(num));
        }

        return result;
    }

    /**
     * 입력된 숫자 만큼 랜덤한 마피아의 직업을 리턴
     * @param num 몇 개의 직업을 리턴할지 입력
     * @return 랜덤으로 num 만큼의 랜덤 직업 출력
     */
    public static List<String> getMafiaRoles(int num){
        Collections.shuffle(mafiaRoles);
        List<String> result = new ArrayList<>();

        while(num-->0) {
            result.add(mafiaRoles.get(num));
        }

        return result;
    }

    // 다음날 게임 시작 시 피드 알림 메세지

    /**
     * 시민투표로 인해 사망한 유저에 대한 결과 정보 전달.
     * @param name 시민투표에서 최고득점자 닉네임을 입력
     * @return "name님이 시민투표로 인해 사망했습니다." 리턴
     */
    public static String getCivilKill(String name) {
        return name + "님이 시민투표로 인해 사망했습니다.";
    }

    /**
     * 마피아 지목으로 인해 사망한 시민 결과 정보 전달.
     * @param name 마피아에게 지목당한 사람의 닉네임을 입력
     * @return "name 님이 마피아에게 당했습니다." 리턴
     */
    public static String getMafialKill(String name) {
        return name + "님이 마피아에게 당했습니다 :(";
    }

    /**
     * 의사의 지목으로 죽을 뻔 한 사람이 살게 된 정보를 전달.
     * @param name 의사에게 지목받아 살게 된 사람의 닉네임을 입력
     * @return "name님이 의사에게 치료받아 살았습니다." 리턴
     */
    public static String getDoctorHeal(String name) {
        return name + "님이 마피아에게 지목받았지만, 의사에게 치료받아 살았습니다.";
    }

    /**
     * 시민직업 중 기자가 지목한 사람의 직업 정보를 전달.
     * @param name 기자가 지목한 사람의 닉네임을 입력
     * @param job 기자에게 지목당한 사람의 직업 입력
     * @return "기자 조사 결과 name님의 직업은 job 입니다" 리턴
     */
    public static String getReporterInfo(String name, String job) {
        return "기자 조사 결과 " + name + "님의 직업은 " + job + "입니다.";
    }

    /**
     * 마피아 직업 중 변장술사의 부활 정보를 전달.
     * @param name 시민투표에 의해 죽임을 당한 변장술사의 닉네임을 입력
     * @param nextName 내가 전날 시민투표 때 지목한 사람의 닉네임 입력
     * @return "name님은 변장술사 능력을 활용해 nextName님으로 부활 했습니다. 기존 nextName님은 사망하셨습니다." 리턴
     */
    public static String getChangeInfo(String name, String nextName) {
        return name + "님은 변장술사 능력을 활용해" + nextName + "님으로 부활 했습니다. "
                + "기존 " + nextName + "님은 사망했습니다.";
    }

    public static String getJudgeInfo(String name, String nextName) {
        return name + "님이 판사 능력을 활용하여 기존 투표를 무시하고" + nextName + "님이 사망했습니다. ";
    }

}
