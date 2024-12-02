package doit.shop.repository;

import doit.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserLoginId(String userLoginId);
    Optional<User> findByUserPhoneNumber(String userPhoneNumber);
    boolean existsByUserPhoneNumber(String phoneNumber);
}
