package doit.shop.repository;

import doit.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 이름으로 회원 조회
    User findByUsername(String username);

    // 이메일로 회원 조회
    User findByEmail(String email);

}