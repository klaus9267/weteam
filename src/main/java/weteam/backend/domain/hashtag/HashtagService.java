package weteam.backend.domain.hashtag;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import weteam.backend.application.handler.exception.BadRequestException;
import weteam.backend.application.message.ExceptionMessage;
import weteam.backend.domain.hashtag.domain.MemberHashtag;
import weteam.backend.domain.hashtag.repository.MemberHashtagRepository;
import weteam.backend.domain.hashtag.repository.MemberHashtagRepositorySupport;
import weteam.backend.domain.member.MemberService;
import weteam.backend.domain.member.entity.Member;
import weteam.backend.domain.hashtag.domain.Hashtag;
import weteam.backend.domain.hashtag.dto.AddHashtagDto;
import weteam.backend.domain.hashtag.dto.HashtagDto;
import weteam.backend.domain.hashtag.repository.HashtagRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashTagRepository;
    private final MemberHashtagRepository memberHashtagRepository;
    private final MemberHashtagRepositorySupport memberHashtagRepositorySupport;
    private final MemberService memberService;

    public HashtagDto create(final AddHashtagDto hashtagDto, final Long memberId) {
        Optional<Hashtag> data = hashTagRepository.findByName(hashtagDto.getName());
        Member member = memberService.findById(memberId);

        if (data.isEmpty()) {
            MemberHashtag memberHashtag = HashtagMapper.toMemberHashtag(hashtagDto, member);
            return HashtagDto.from(memberHashtagRepository.save(memberHashtag));
        } else {
            Hashtag hashtag = data.get();
            if (memberHashtagRepository.findByHashtag(hashtag).isEmpty()) {
                MemberHashtag memberHashtag = HashtagMapper.toMemberHashtag(hashtag, member, hashtagDto.getColor());
                return HashtagDto.from(memberHashtagRepository.save(memberHashtag));
            } else {
                throw new DuplicateKeyException(ExceptionMessage.DUPLICATE.getMessage());
            }
        }
    }

    public List<HashtagDto> findByMemberIdWithType(final Long memberId, final String type) {
        List<MemberHashtag> memberHashtagList = memberHashtagRepositorySupport.findByMemberIdWithType(memberId, type);
        return HashtagDto.from(memberHashtagList);
    }

    public void updateUse(final Long memberHashtagId, final Long memberId) {
        MemberHashtag memberHashtag = checkHashtag(memberHashtagId, memberId);
        memberHashtag.setUse(!memberHashtag.isUse());
        memberHashtagRepository.save(memberHashtag);
    }

    public void delete(final Long memberHashtagId, final Long memberId) {
        MemberHashtag memberHashtag = checkHashtag(memberHashtagId, memberId);
        memberHashtagRepository.delete(memberHashtag);
    }

    public void deleteAllByMemberId(final Long memberId) {
        if (memberHashtagRepository.findByMemberId(memberId).isEmpty()) {
            throw new NotFoundException(ExceptionMessage.NOT_FOUND.getMessage());
        }
        memberHashtagRepository.deleteAllByMemberId(memberId);
    }

    public MemberHashtag checkHashtag(final Long memberHashtagId, final Long memberId) {
        MemberHashtag memberHashtag = memberHashtagRepository.findById(memberHashtagId).orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND.getMessage()));
        return memberHashtag;
    }
}
