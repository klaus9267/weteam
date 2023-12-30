package weteam.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.application.BaseEntity;
import weteam.backend.application.oauth.provider.ProviderType;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username, nickname, organization, providerId;

    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public void updateOrganization(String organization) {
        this.organization = organization;
    }
}
