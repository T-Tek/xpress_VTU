package com.xpressvtu.xpressvtu.controller;

import com.xpressvtu.xpressvtu.model.Merchant;
import com.xpressvtu.xpressvtu.request.LoginRequest;
import com.xpressvtu.xpressvtu.response.JwtResponse;
import com.xpressvtu.xpressvtu.service.merchant.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping("/register")
    public ResponseEntity<?> registerMerchant(@RequestBody Merchant merchant) {
        Merchant registeredMerchant = merchantService.registerMerchant(merchant);
        return ResponseEntity.ok(registeredMerchant);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMerchant(@RequestBody LoginRequest loginRequest) {
        String token = merchantService.loginMerchant(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
