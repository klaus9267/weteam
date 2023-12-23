//package weteam.backend.application.oauth;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@AllArgsConstructor
//@Service
//public class OAuth2UserService extends DefaultOAuth2UserService {
//    private final UserRepository userRepository;
//
//
//    @Override
//    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        final Map<String, Object> attributes = oAuth2User.getAttributes();
//
//        final OAuth2ProviderFactory providerFactory = new OAuth2ProviderFactory();
//        final OAuth2Provider oAuth2Provider = providerFactory.getOAuth2Provider(userRequest.getClientRegistration().getRegistrationId(), attributes);
//
//        User user = userRepository.findOneByProviderAndProviderId(oAuth2Provider.getProvider(), oAuth2Provider.getProviderId()).orElseGet(() -> userRepository.save(
//                User.builder()
//                    .username(oAuth2Provider.getUsername())
//                    .name(oAuth2Provider.getName())
//                    .email(oAuth2Provider.getEmail())
//                    .role(UserRole.USER)
//                    .provider(oAuth2Provider.getProvider())
//                    .providerId(oAuth2Provider.getProviderId())
//                    .build())
//        );
//
//        return new PrincipalDetails(UserDto.toUserDto(user), attributes);
//    }
//}
