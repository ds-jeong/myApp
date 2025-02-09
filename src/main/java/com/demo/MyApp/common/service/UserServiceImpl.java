package com.demo.MyApp.common.service;

import com.demo.MyApp.common.dto.UserDto;
import com.demo.MyApp.common.entity.User;
import com.demo.MyApp.common.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor //생성자 주입코드없이 의존성주입
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 패스워드 암호화를 위한 빈

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserDto userDto) throws Exception {
        // 패스워드 암호화
        String encodedPassword = passwordEncoder.encode(userDto.getPw());
        userDto.setPw(encodedPassword);
        // 기타 필요한 로직 처리 (예: 역할 설정, 기본값 등)
        userDto.setRole("user"); // 기본적으로 일반 사용자 역할 할당

        // DTO를 Entity로 변환하여 save() 메소드에 담아 데이터 삽입
        User user = User.toEntity(userDto);
        userRepository.save(user);
    }

    @Override
    public boolean isUsernameExists(String userNm) throws Exception {
        return userRepository.findByUserNm(userNm) != null;
    }

    @Override
    public boolean isPhoneExists(String phone) throws Exception {
        return userRepository.findByPhone(phone) != null;
    }

    @Override
    public boolean isEmailExists(String email) throws Exception {
        return userRepository.findByEmail(email) != null;
    }

    @Transactional
    @Override
    public UserDto selectUserInfo(UserDto dto){
        User user = userRepository.findByUserId(dto.getUserId());

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .userNm(user.getUserNm())
                .pw(user.getPw())
                .address(user.getAddress())
                .phone(user.getPhone())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return userDto;
    }

    @Transactional
    @Override
    public UserDto selectUserInfoByEmail(String email){
        User user = userRepository.findByUserNm(email);

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .userNm(user.getUserNm())
                .phone(user.getPhone())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return userDto;
    }
}
