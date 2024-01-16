package com.xpressvtu.xpressvtu.controller;

import com.xpressvtu.xpressvtu.model.airtime.AirtimeRequest;
import com.xpressvtu.xpressvtu.model.airtime.AirtimeResponse;
import com.xpressvtu.xpressvtu.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/makePayment")
    public ResponseEntity<AirtimeResponse> makePayment(@RequestBody AirtimeRequest airtimeRequest) {
        try {
            AirtimeResponse response = paymentService.fulfillAirtime(airtimeRequest);
            // Do something with the response if needed
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Log the exception if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
