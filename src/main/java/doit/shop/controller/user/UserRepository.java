package doit.shop.controller.user;

import doit.shop.controller.user.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByLoginId(String loginId);
    UserEntity findByNickname(String nickname);
    boolean existsByloginId(String loginId);
}
