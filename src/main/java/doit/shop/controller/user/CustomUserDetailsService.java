package doit.shop.controller.user;

import doit.shop.controller.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByLoginId(username);
//        UserEntity userData = userRepository.findByLoginId(username);
//
//        if(userData!=null){
//            return new CustomUserDetails(userData);
//        }
//
//        return null;
    }
}
