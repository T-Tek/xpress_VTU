package com.xpressvtu.xpressvtu.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class JwtResponse {

    private final String token;

//    public JwtResponse(String token) {
//        this.token = token;
//    }

}