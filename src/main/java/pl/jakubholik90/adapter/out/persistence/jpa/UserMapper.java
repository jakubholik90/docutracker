package pl.jakubholik90.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.UserEntity;
import pl.jakubholik90.domain.model.User;

public class UserMapper {
    public static User mapToUser(UserEntity userEntity) {

        User returnUser = User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .build();

        return returnUser;
    }

    public static UserEntity mapToUserEntity(User user) {
        UserEntity returnEntity = new UserEntity();

        returnEntity.setId(user.getId());
        returnEntity.setEmail(user.getEmail());
        returnEntity.setName(user.getName());

        return returnEntity;
    }
}
