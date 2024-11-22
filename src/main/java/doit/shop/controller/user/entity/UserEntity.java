package doit.shop.controller.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@Entity
@Getter
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String name;
//    @Size(min = 5, max = 30) -> dto에서 처리해주면 굳이 해줄 필요가 없겠지?
    private String loginId;
    private String password;
    private String nickname;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public UserEntity(Long userId, String name, String loginId, String password, String nickname, String phoneNumber, LocalDateTime createdAt) {
        this.userId = userId;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    public void modifyInfo(String nickname, String phoneNumber){
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.modifiedAt = LocalDateTime.now();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }
}
