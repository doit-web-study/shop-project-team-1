package doit.shop.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import doit.shop.global.auth.Authority;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static doit.shop.global.auth.Authority.ROLE_USER;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String userLoginId;
    private String userPassword;
    private String userNickname;
    private String userPhone;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    private Users(String userName, String userPassword, String userNickname, String userPhone, String userLoginId, Authority authority) {
        this.username = userName;
        this.userLoginId = userLoginId;
        this.userPassword = userPassword;
        this.userNickname = userNickname;
        this.userPhone = userPhone;
        this.authority = authority;
    }

}
