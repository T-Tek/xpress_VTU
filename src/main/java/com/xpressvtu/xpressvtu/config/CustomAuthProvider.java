package com.xpressvtu.xpressvtu.config;

import com.xpressvtu.xpressvtu.model.Merchant;
import com.xpressvtu.xpressvtu.repository.MerchantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthProvider.class);

    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthProvider(MerchantRepository merchantRepository, PasswordEncoder passwordEncoder) {
        this.merchantRepository = merchantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<Merchant> merchantOptional = merchantRepository.findByEmail(username);

        if (merchantOptional.isPresent()) {
            Merchant merchant = merchantOptional.get();

            if (passwordEncoder.matches(password, merchant.getPassword())) {
                logger.info("Authentication successful for user: {}", username);

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(merchant.getRole().name()));


                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            } else {
                logger.warn("Invalid password for user: {}", username);
                throw new BadCredentialsException("Invalid password!");
            }
        } else {
            logger.warn("No user registered with email: {}", username);
            throw new BadCredentialsException("No user registered with this email!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
