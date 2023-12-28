package weteam.backend.domain.hashtag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import weteam.backend.application.ExceptionMessage;
import weteam.backend.application.handler.exception.DuplicateKeyException;
import weteam.backend.domain.hashtag.domain.Hashtag;
import weteam.backend.domain.hashtag.dto.AddHashtagDto;
import weteam.backend.domain.hashtag.dto.HashtagDto;
import weteam.backend.domain.hashtag.repository.HashtagRepository;
import weteam.backend.domain.hashtag.repository.HashtagRepositorySupport;
import weteam.backend.domain.member.MemberService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final HashtagRepositorySupport hashtagRepositorySupport;
    private final MemberService memberService;

    public void save(final AddHashtagDto hashtagDto, final Long memberId) {
        Optional<Hashtag> data = hashtagRepository.findByName(hashtagDto.name());
        if (data.isPresent()) {
            throw new DuplicateKeyException(ExceptionMessage.DUPLICATE);
        }
        Hashtag hashtag = Hashtag.from(hashtagDto, memberId);
        hashtagRepository.save(hashtag);
    }

    public List<HashtagDto> findByMemberIdWithType(final Long memberId, final String type) {
        List<Hashtag> hashtagList = hashtagRepositorySupport.findByMemberIdWithType(memberId, type);
        return HashtagDto.from(hashtagList);
    }

    public void updateUse(final Long memberHashtagId, final Long memberId) {
        Hashtag hashtag = checkHashtag(memberHashtagId, memberId);
        hashtag.setUse(!hashtag.isUse());
        hashtagRepository.save(hashtag);
    }

    public void delete(final Long memberHashtagId, final Long memberId) {
        Hashtag hashtag = checkHashtag(memberHashtagId, memberId);
        hashtagRepository.delete(hashtag);
    }

    public void deleteAll(final Long memberId) {
        if (hashtagRepository.findByMemberId(memberId).isEmpty()) {
            throw new NotFoundException(ExceptionMessage.NOT_FOUND.getMessage());
        }
        hashtagRepository.deleteAllByMemberId(memberId);
    }

    public Hashtag checkHashtag(final Long memberHashtagId, final Long memberId) {
        Hashtag hashtag = hashtagRepository.findById(memberHashtagId).orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND.getMessage()));
        return hashtag;
    }
}
