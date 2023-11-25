package com.backend.foodproject.service;

import com.backend.foodproject.config.JwtService;
import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.error.ConflictException;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.model.UpdatePassword;
import com.backend.foodproject.reposiory.SellerRepository;
import com.backend.foodproject.utils.EmailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
    private final JwtService jwtService;
    public Seller updateSeller(Seller seller, String email) {
        var updateSeller = sellerRepository.findByEmail(email);
        if (Objects.nonNull(seller.getFirstname()) && !"".equalsIgnoreCase(seller.getFirstname())) {
            updateSeller.setFirstname(seller.getFirstname());
        }
        if (Objects.nonNull(seller.getLastname()) && !"".equalsIgnoreCase(seller.getLastname())) {
            updateSeller.setLastname(seller.getLastname());
        }
        return sellerRepository.save(updateSeller);
    }

    public String updateSellerPassword(String email, UpdatePassword request) throws ConflictException, GlobalException {
        var currentPass = sellerRepository.findByEmail(email);
        if (!passwordEncoder.matches(request.getOldPassword(), currentPass.getPassword())) {
            throw new GlobalException("enter current password to change the password");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ConflictException("password did not match... try again");
        }
        currentPass.setPassword(passwordEncoder.encode(request.getNewPassword()));
        sellerRepository.save(currentPass);
        return "password updated successfully...";
    }

    public String sellerDelete(Seller seller) throws GlobalException {
        var deletSeller = sellerRepository.findByEmail(seller.getEmail());
        if (deletSeller != null) {
            sellerRepository.delete(seller);
            return "account successfully deleted";
        }
        throw new GlobalException("account email " + seller.getEmail() + " not found");
    }

    public String sellerForgotPassword(String email) {
        var seller = sellerRepository.findByEmail(email);
        var token = jwtService.generateToken(seller);
        try {
            emailUtil.sendSellerForgetPassword(email,token);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "check link in your email "+email+" to reset password";
    }

    public String SellerResetPassword
            (UpdatePassword request, String email, String token) throws ConflictException {
        var sellerPass = sellerRepository.findByEmail(email);
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ConflictException("password did not match... try again");
        }
        sellerPass.setPassword(passwordEncoder.encode(request.getConfirmPassword()));
        sellerRepository.save(sellerPass);
        return "password reset is sucessfully...";
    }
}
