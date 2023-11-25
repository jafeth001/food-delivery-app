package com.backend.foodproject.service;

import com.backend.foodproject.config.JwtService;
import com.backend.foodproject.entity.User;
import com.backend.foodproject.error.ConflictException;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.model.UpdatePassword;
import com.backend.foodproject.reposiory.UserRepository;
import com.backend.foodproject.utils.EmailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
    private final JwtService jwtService;

    public User updateUser(User user, String email) {
        var updateUser = userRepository.findByEmail(email);
        if (Objects.nonNull(user.getFirstname()) && !"".equalsIgnoreCase(user.getFirstname())) {
            updateUser.setFirstname(user.getFirstname());
        }
        if (Objects.nonNull(user.getLastname()) && !"".equalsIgnoreCase(user.getLastname())) {
            updateUser.setLastname(user.getLastname());
        }
        return userRepository.save(updateUser);
    }

    public String updateUserPassword
            (UpdatePassword request, String email) throws GlobalException, ConflictException {
        var updateUsers = userRepository.findByEmail(email);
        if (!passwordEncoder.matches(request.getOldPassword(), updateUsers.getPassword())) {
            throw new GlobalException("enter current password to change the password");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ConflictException("password did not match... try again");
        }
        updateUsers.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(updateUsers);
        return "password updated successfully...";
    }

    public String userDelete(User user) throws GlobalException {
        var deletUser = userRepository.findByEmail(user.getEmail());
        if (deletUser != null) {
            userRepository.delete(user);
            return "account successfully deleted";
        }
        throw new GlobalException("account email " + user.getEmail() + " not found");
    }

    public String userForgotPassword(String email) {
        var user = userRepository.findByEmail(email);
        var token = jwtService.generateToken(user);
        try {
            emailUtil.sendSellerForgetPassword(email, token);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "check link in your email " + email + " to reset password";

    }

    public String userResetPassword(UpdatePassword request, String email, String token) throws ConflictException {
        var userPass = userRepository.findByEmail(email);
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ConflictException("password did not match... try again");
        }
        userPass.setPassword(passwordEncoder.encode(request.getConfirmPassword()));
        userRepository.save(userPass);
        return "password reset is sucessfully...";
    }
}

