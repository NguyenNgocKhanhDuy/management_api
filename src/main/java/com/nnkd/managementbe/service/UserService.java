package com.nnkd.managementbe.service;

import com.nnkd.managementbe.dto.request.*;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public User getUserById(String id) {
        try {
            User user = userRepository.findById(id).get();
            return user;
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No user found: "+id);
        }
    }

    public User register(UserCreationRequest request, String code) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email has already been used");
        User user = new User().builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(hashPassword(request.getPassword()))
                .avatar(generateAvatar(request.getUsername()))
                .code(code)
                .date(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public ApiResponse<VerifyCodeResponse> verifyCode(VerifyCodeRequest request) {
        try {
            User user = userRepository.findUserByEmail(request.getEmail()).get();
            boolean valid = user.getCode().equals(request.getCode()) && LocalDateTime.now().isBefore(user.getDate().plus(1, ChronoUnit.HOURS));
            ApiResponse apiResponse = new ApiResponse<>();
            apiResponse.setResult(VerifyCodeResponse.builder().valid(valid).build());
            return apiResponse;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No user found: " + request.getEmail());
        }
    }

    public User sendCodeToUser(String email, String code) {
        try {
            User user = userRepository.findUserByEmail(email).get();
            user.setCode(code);
            user.setDate(LocalDateTime.now());
            return userRepository.save(user);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No user found: " + email);
        }
    }

    public List<User> searchUsersByEmail(String partialEmail) {
        return userRepository.findUserByEmailContaining(partialEmail);
    }


    public User updateUserPassword(UserUpdateRequest request) {
        try {
            User user = userRepository.findUserByEmail(request.getEmail()).get();
            user.setPassword(hashPassword(request.getPassword()));
            return userRepository.save(user);
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No user found: " + request.getEmail());
        }
    }

    public User getUser(String email) {
        return userRepository.findUserByEmail(email).get();
    }

    public User updateUserAvatar(UserUpdateRequest request) {
        try {
            User user = userRepository.findUserByEmail(request.getEmail()).get();
            user.setAvatar(request.getAvatar());
            return userRepository.save(user);
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No user found: " + request.getEmail());
        }
    }

    public User updateUserUsername(UserUpdateRequest request) {
        try {
            User user = userRepository.findUserByEmail(request.getEmail()).get();
            user.setUsername(request.getUsername());
            return userRepository.save(user);
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No user found: " + request.getEmail());
        }
    }


    public String hashPassword(String password) {
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

    public String generateAvatar(String username) {
        String firstLetter = username.substring(0, 1);
        return "https://ui-avatars.com/api/?name="+firstLetter+"&background=random";
    }


}
