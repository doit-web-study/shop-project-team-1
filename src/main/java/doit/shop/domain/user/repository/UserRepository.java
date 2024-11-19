package doit.shop.domain.user.repository;

import doit.shop.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    // id랑 password로 user 객체 찾기
    Users findByUserLoginIdAndUserPassword(String userLoginId, String userPassword);

    Users findByUserLoginId(String userLoginid);
}
