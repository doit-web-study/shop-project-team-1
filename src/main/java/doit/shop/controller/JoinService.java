//package doit.shop.controller;
//
//import doit.shop.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class JoinService{
//
//    private final UserRepository userRepository;
//    private final BCyrptPasswordEncoder bCyrptPassowrdEncoder;
//    public void joinService(JoinDto joinDto){
//        UserEntity userEntity= new UserEntity;
//        userEntity.setName(joinDto.getName());
//        userEntity.setPassword(bCyrptPasswordEncoder(joinDto.getPassword()));
//
//        if(userRepository.findByUserName(joinDto.getName())){
//            reutrn;
//        }
//        userRepository.save(userEntity)
//    }
//}