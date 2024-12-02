package doit.shop.port.jwt;

import doit.shop.domain.User;
import doit.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean isExistsClient = userService.isExistUserByPhoneNumber(username);
        if (isExistsClient) {
            User user = userService.findUserByPhoneNumber(username);
            return new CustomUserDetails(user.getUserLoginId(), user.getUserPhoneNumber(), SecurityRole.USER);
        }

        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}
