package weteam.backend.domain.hashtag.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import weteam.backend.domain.hashtag.dto.AddHashtagDto;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private HashtagType type;

    public Hashtag(AddHashtagDto hashtagDto) {
        this.name = hashtagDto.getName();
        this.type = HashtagType.valueOf(hashtagDto.getType());
    }
}
