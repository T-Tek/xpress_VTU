package com.xpressvtu.xpressvtu.model.airtime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirtimeResponse {
    private String referenceId;
    private String requestId;
    private String responseCode;
    private String responseMessage;
    private Object data;
}
