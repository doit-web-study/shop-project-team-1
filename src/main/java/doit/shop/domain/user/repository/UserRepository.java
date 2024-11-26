package doit.shop.domain.user.repository;

import doit.shop.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    // id랑 password로 user 객체 찾기
    Users findByUserLoginIdAndUserPassword(String userLoginId, String userPassword);

    Optional<Users> findByUserLoginId(String loginId);
    boolean existsByUserLoginId(String loginId);

    Users findByUsername(String username);
}
