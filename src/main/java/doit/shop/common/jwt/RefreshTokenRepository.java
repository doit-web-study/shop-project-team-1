package doit.shop.common.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    RefreshToken findByLoginId(String loginId);
    RefreshToken findByRefreshToken(String token);
}
