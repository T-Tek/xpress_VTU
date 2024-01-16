package com.xpressvtu.xpressvtu.service.merchant;

import com.xpressvtu.xpressvtu.model.Merchant;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface MerchantService {
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    Merchant registerMerchant(Merchant merchant);
    String loginMerchant(String email, String password);


}
