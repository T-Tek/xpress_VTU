package com.xpressvtu.xpressvtu.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpressvtu.xpressvtu.exceptions.PaymentServiceException;
import com.xpressvtu.xpressvtu.model.airtime.AirtimeRequest;
import com.xpressvtu.xpressvtu.model.airtime.AirtimeResponse;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.management.openmbean.InvalidKeyException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Value("${BASE_URL}")
    private String baseUrl;

    @Value("${PUBLIC_KEY}")
    private String publicKey;

    @Value("${PRIVATE_KEY}")
    private String privateKey;
    private final RestTemplate restTemplate;

    public PaymentServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public AirtimeResponse fulfillAirtime(AirtimeRequest airtimeRequest) {
        String endpoint = baseUrl + "/fulfil";
        String paymentHash = generatePaymentHash(airtimeRequest, privateKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + publicKey);
        headers.set("PaymentHash", paymentHash);

        HttpEntity<AirtimeRequest> requestEntity = new HttpEntity<>(airtimeRequest, headers);

        try {
            return restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<AirtimeResponse>() {}).getBody();
        } catch (HttpClientErrorException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            logger.error("Error fulfilling airtime request. Server responded with HTTP {}: {}", ex.getRawStatusCode(), responseBody);
            throw new PaymentServiceException("Airtime request failed: " + responseBody, ex);
        } catch (RestClientException ex) {
            logger.error("Error fulfilling airtime request: {}", ex.getMessage());
            throw new PaymentServiceException(ex.getMessage(), ex);
        } catch (Exception e) {
            logger.error("Unexpected error fulfilling airtime request: {}", e.getMessage(), e);
            throw new PaymentServiceException(e.getMessage(), e);
        }
    }

    private String generatePaymentHash(AirtimeRequest airtimeRequest, String privateKey) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String toHash = objectMapper.writeValueAsString(airtimeRequest);
            return calculateHMAC512(toHash, privateKey);
        } catch (JsonProcessingException e) {
            logger.error("Error converting AirtimeRequest to JSON: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String calculateHMAC512(String data, String key) {
        String HMAC_SHA512 = "HmacSHA512";
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA512);

        try {
            Mac mac = Mac.getInstance(HMAC_SHA512);
            mac.init(secretKeySpec);
            return Hex.encodeHexString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | InvalidKeyException | java.security.InvalidKeyException e) {
            logger.error("Error calculating HMAC512: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
