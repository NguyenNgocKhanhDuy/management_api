package com.nnkd.managementbe.service;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.dto.request.UserCreationRequest;
import com.nnkd.managementbe.dto.request.UserUpdateRequest;
import com.nnkd.managementbe.dto.request.VerifyCodeRequest;
import com.nnkd.managementbe.dto.response.VerifyCodeResponse;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User save(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email has already been used");
        }
        User user = new User().builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(hashPassword(request.getPassword()))
                .build();

        return userRepository.save(user);
    }

    public User register(UserCreationRequest request, String code) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email has already been used");
        }
        User user = new User().builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(hashPassword(request.getPassword()))
                .code(code)
                .date(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public ApiResponse<VerifyCodeResponse> verifyCode(VerifyCodeRequest request) {
        User user = userRepository.findUserByEmail(request.getEmail()).get();
        if (user == null)
            throw new NoSuchElementException("No user found: "+request.getEmail());
        boolean valid = user.getCode().equals(request.getCode()) && user.getDate().isAfter(LocalDateTime.now());
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(VerifyCodeResponse.builder().valid(valid).build());
        return apiResponse;
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }


    public User updateUser(String userId, UserUpdateRequest request){
        User user = getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found: "+userId);
        }
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found: "+userId);
        }
    }

    public String hashPassword(String password){
        try {
            MessageDigest sha256 = null;
            sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256.digest(password.getBytes());
            BigInteger number = new BigInteger(1, hash);
            return number.toString(16);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


    public String randomCodeVerify() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += (random.nextInt(9) + 1);
        }
        return result;
    }


}
