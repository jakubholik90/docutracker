package pl.jakubholik90.adapter.in.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pl.jakubholik90.domain.model.User;
import pl.jakubholik90.domain.port.out.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // getting data from google
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //getting email and name
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        //checking if user exist
        if (userRepository.findByEmail(email).isEmpty()) {
            User newUserToSave = User.builder()
                    .email(email)
                    .name(name).build();
            userRepository.save(newUserToSave);
        }

        //return OAuth2User
        return oAuth2User;
    }
}
