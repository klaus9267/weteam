package weteam.backend.hashtag;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.config.message.ExceptionMessage;
import weteam.backend.hashtag.domain.Hashtag;
import weteam.backend.hashtag.domain.MemberHashtag;
import weteam.backend.hashtag.dto.HashtagDto;
import weteam.backend.hashtag.repository.HashtagRepository;
import weteam.backend.hashtag.repository.MemberHashtagCustomRepository;
import weteam.backend.hashtag.repository.MemberHashtagRepository;
import weteam.backend.member.MemberService;
import weteam.backend.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashTagRepository;
    private final MemberHashtagRepository memberHashtagRepository;
    private final MemberHashtagCustomRepository memberHashtagCustomRepository;
    private final MemberService memberService;

    public HashtagDto.Res create(HashtagDto hashtagDto, Long memberId) {
        Optional<Hashtag> data = hashTagRepository.findByName(hashtagDto.getName());
        Member member = memberService.findById(memberId);

        if (data.isEmpty()) {
            Hashtag hashtag = HashtagMapper.instance.toEntity(hashtagDto);
            MemberHashtag memberHashtag = MemberHashtag.builder().hashtag(hashtag).member(member).build();
            hashTagRepository.save(hashtag);
            return HashtagMapper.instance.toRes(memberHashtagRepository.save(memberHashtag));
        } else {
            Hashtag hashtag = data.get();
            if (memberHashtagRepository.findByHashtag(hashtag).isEmpty()) {
                MemberHashtag memberHashtag = MemberHashtag.builder().hashtag(hashtag).member(member).build();
                return HashtagMapper.instance.toRes(memberHashtagRepository.save(memberHashtag));
            } else {
                throw new DuplicateKeyException(ExceptionMessage.DUPLICATE.getMessage());
            }
        }
    }

    public List<MemberHashtag> findByMemberIdWithType(Long memberId, int type) {
        return memberHashtagCustomRepository.findByMemberIdWithType(memberId, type);
    }

    public void updateUse(Long memberHashtagId, Long memberId) {
        MemberHashtag memberHashtag = checkHashtag(memberHashtagId, memberId);
        memberHashtag.setUse(!memberHashtag.isUse());
        HashtagMapper.instance.toRes(memberHashtagRepository.save(memberHashtag));
    }

    public void delete(Long memberHashtagId, Long memberId) {
        MemberHashtag memberHashtag = checkHashtag(memberHashtagId, memberId);
        memberHashtagRepository.delete(memberHashtag);
    }

    public void deleteAllByMemberId(Long memberId) {
        memberHashtagRepository.deleteAllByMemberId(memberId);
    }

    public MemberHashtag checkHashtag(Long memberHashtagId, Long memberId) {
        MemberHashtag memberHashtag = memberHashtagRepository.findById(memberHashtagId).orElseThrow(() -> new RuntimeException("없는 해시태그입니다."));
        if (!memberHashtag.getMember().getId().equals(memberId)) {
            throw new RuntimeException("다른 사람의 해시태크입니다.");
        }
        return memberHashtag;
    }
}
