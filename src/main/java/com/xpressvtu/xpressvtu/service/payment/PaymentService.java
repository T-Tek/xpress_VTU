package com.xpressvtu.xpressvtu.service.payment;


import com.xpressvtu.xpressvtu.model.airtime.AirtimeRequest;
import com.xpressvtu.xpressvtu.model.airtime.AirtimeResponse;
public interface PaymentService {
    AirtimeResponse fulfillAirtime(AirtimeRequest airtimeRequest);
}
