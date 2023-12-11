package weteam.backend.hashtag.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HashtagType {
    JOB_OBJECTIVE("JOB_OBJECTIVE","희망업무"),
    MBTI("MBIT", "MBTI"),
    SPECIALTY("SPECIALTY", "특기"),
    PERSONALITY("PERSONALITY", "성격"),
    ETC("ETC", "기타");

    private final String key;
    private final String title;
}
