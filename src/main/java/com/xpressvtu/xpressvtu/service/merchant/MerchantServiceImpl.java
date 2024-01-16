package com.xpressvtu.xpressvtu.service.merchant;

import com.xpressvtu.xpressvtu.config.JwtUtil;
import com.xpressvtu.xpressvtu.exceptions.IncorrectPasswordException;
import com.xpressvtu.xpressvtu.exceptions.MerchantNotFoundException;
import com.xpressvtu.xpressvtu.exceptions.MerchantServiceException;
import com.xpressvtu.xpressvtu.model.Merchant;
import com.xpressvtu.xpressvtu.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

    private final MerchantRepository merchantRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", email);
        Optional<Merchant> merchantOptional = merchantRepository.findByEmail(email);
        if (merchantOptional.isEmpty()) {
            throw new UsernameNotFoundException("Merchant not found with email: " + email);
        }
        Merchant merchant = merchantOptional.get();
        logger.info("Loaded user: {}", merchant.getEmail());
        return new User(merchant.getEmail(), merchant.getPassword(), Collections.emptyList());
    }

    @Override
    public Merchant registerMerchant(Merchant merchant) {
        try {
            logger.info("Registering new merchant: {}", merchant.getEmail());
            merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));
            Merchant savedMerchant = merchantRepository.save(merchant);
            logger.info("Merchant registered successfully: {}", savedMerchant.getEmail());
            return savedMerchant;
        } catch (Exception e) {
            logger.error("Error registering merchant: {}", e.getMessage(), e);
            throw new MerchantServiceException("Error registering merchant", e);
        }
    }

    @Override
    public String loginMerchant(String email, String password) {
        try {
            logger.info("Attempting to log in merchant: {}", email);
            UserDetails userDetails = loadUserByUsername(email);
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                String token = jwtUtil.generateToken(userDetails);
                logger.info("Merchant logged in successfully: {}", email);
                return token;
            } else {
                logger.warn("Incorrect password for merchant: {}", email);
                throw new IncorrectPasswordException("Incorrect password");
            }
        } catch (UsernameNotFoundException e) {
            logger.warn("Merchant not found: {}", email);
            throw new MerchantNotFoundException("Merchant not found");
        } catch (IncorrectPasswordException | MerchantNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error logging in merchant: {}", e.getMessage(), e);
            throw new MerchantServiceException("Error logging in merchant", e);
        }
    }
}
