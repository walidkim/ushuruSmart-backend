package com.emtech.ushurusmart.payments.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.emtech.ushurusmart.config.PaymentConfig;
import com.emtech.ushurusmart.payments.Utils.HelperUtility;
import com.emtech.ushurusmart.payments.dtos.AccessTokenResponse;
import com.emtech.ushurusmart.payments.dtos.ApiResponse;
import com.emtech.ushurusmart.payments.dtos.okPushResponse;
import com.emtech.ushurusmart.payments.dtos.callback.CallbackMetadata;
import com.emtech.ushurusmart.payments.dtos.callback.StkCallBack;
import com.emtech.ushurusmart.payments.dtos.callback.StkCallbackRequest;
import com.emtech.ushurusmart.payments.entity.PaymentEntity;
import com.emtech.ushurusmart.payments.repository.PaymentRepository;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class PaymentImpl {
    private static final Logger log = LoggerFactory.getLogger(PaymentImpl.class);
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentRepository paymentRepo;
    private ApiResponse apiResponse;
    private final PaymentConfig paymentConfig;
    @Autowired
    private OwnerRepository ownerRepository;

    public PaymentImpl(PaymentRepository paymentRepo, PaymentConfig paymentConfig) {
        this.paymentRepo = paymentRepo;
        this.paymentConfig = paymentConfig;
    }

    @SuppressWarnings("resource")
    public String getAccessToken() {
        String authheader = "Basic " + HelperUtility.toBase64String(paymentConfig.keySecret);
        Request request = (new Request.Builder()).url(PaymentConfig.authURL).get()
                .addHeader("Authorization", authheader).addHeader("Content-Type", "application/json").build();

        try {
            Response response = this.okHttpClient.newCall(request).execute();

            assert response.body() != null;

            AccessTokenResponse accessTokenResponse = (AccessTokenResponse) this.objectMapper
                    .readValue(response.body().string(), AccessTokenResponse.class);
            return accessTokenResponse.getAccessToken();
        } catch (Exception e) {
            log.error(String.format("Could not get access token. -> %s", e));
            System.out.println("Could not get access token: " + String.valueOf(e));
            apiResponse = new ApiResponse(false, "Please try Later!", null);
            return apiResponse.toString();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(PaymentImpl.class);
    private static final Pattern phonePattern = Pattern.compile("^[0-9]{12}$");
    private static final Pattern amountPattern = Pattern.compile("^[0-9]{1,6}$");
    private static final Pattern eslipPattern = Pattern.compile("^[0-9]{6}$");

    public ResponseEntity<String> sendSTKPush(String phonenumber, String amount, String eslipNo)
            throws JsonProcessingException {
        try {
            String phoneNo = "254" + phonenumber.trim();
            String eslipNoTrimmed = eslipNo.trim();
            String amountTrimmed = amount.trim();

            if (!validateInput(phoneNo, amountTrimmed, eslipNoTrimmed)) {
                apiResponse = new ApiResponse(false, "Provide valid phone number and amount.", null);
                return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(apiResponse));
            }

            int parsedAmount = Integer.parseInt(amountTrimmed);
            if (parsedAmount <= 0 || parsedAmount > 150000) {
                apiResponse = new ApiResponse(false,
                        "Invalid Phone Number or Amount, Amount should be less than KES150,000", null);
                return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(apiResponse));
            }

            String authHeader = "Bearer " + getAccessToken();
            String password = generatePassword();
            String stkBody = buildStkBody(phoneNo, parsedAmount, eslipNoTrimmed, password);

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(stkBody, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(paymentConfig.stkPushURL)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", authHeader)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {

                    okPushResponse okPushResponse = objectMapper.readValue(responseBody, okPushResponse.class);
                    if ("0".equals(okPushResponse.getResponseCode())) {
                        PaymentEntity paymentEntity = initPayment(okPushResponse, parsedAmount, phoneNo,
                                eslipNoTrimmed);
                        paymentRepo.save(paymentEntity);
                        apiResponse = new ApiResponse(true, "STK Push successful", null);
                        return ResponseEntity.ok().body(objectMapper.writeValueAsString(apiResponse));
                    } else {
                        apiResponse = new ApiResponse(false, "Payment failed, please try again!", null);
                        return ResponseEntity.ok().body(objectMapper.writeValueAsString(apiResponse));
                    }

                } else {
                    // Check if response is JSON

                    // errorPushResponse errorPushResponse =
                    // objectMapper.readValue(responseBody,errorPushResponse.class);
                    apiResponse = new ApiResponse(false, "STK Push failed", null);
                    return ResponseEntity.ok().body(objectMapper.writeValueAsString(apiResponse));

                }
            }
        } catch (IOException e) {
            logger.error("Error occurred: ", e);
            apiResponse = new ApiResponse(false, "Error occurred, try again later", null);
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(apiResponse));
        }
    }

    private boolean validateInput(String phoneNo, String amount, String eslipNo) {
        return phonePattern.matcher(phoneNo).matches() &&
                amountPattern.matcher(amount).matches() &&
                eslipPattern.matcher(eslipNo).matches();
    }

    private String generatePassword() {
        String base64Password = HelperUtility
                .toBase64String(paymentConfig.shortCode + paymentConfig.passkey + HelperUtility.getTimeStamp());
        return base64Password;
    }

    private String buildStkBody(String phoneNo, int amount, String eslipNo, String password) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("BusinessShortCode", paymentConfig.shortCode);
        jsonObject.put("Password", password);
        jsonObject.put("Timestamp", HelperUtility.getTimeStamp());
        jsonObject.put("TransactionType", "CustomerPayBillOnline");
        jsonObject.put("Amount", amount);
        jsonObject.put("PartyA", phoneNo);
        jsonObject.put("PartyB", paymentConfig.shortCode);
        jsonObject.put("PhoneNumber", phoneNo);
        jsonObject.put("CallBackURL", paymentConfig.callBackURL);
        jsonObject.put("AccountReference", "ushuru Smart-Tax Collection");
        jsonObject.put("TransactionDesc", "For Eslip:" + eslipNo);

        return jsonObject.toString();
    }

    private PaymentEntity initPayment(okPushResponse okPushResponse, int amount, String phoneno, String eslipNo) {
        String OwnerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner = this.ownerRepository.findByEmail(OwnerEmail);
        String businesskra = owner.getBusinessKRAPin();
        // System.out.println(OwnerEmail + ":" + businesskra);
        PaymentEntity payEntity = new PaymentEntity();
        payEntity.setAmount(amount);
        payEntity.setPayerEmail(OwnerEmail);
        payEntity.setESlipNumber(Long.parseLong(eslipNo));
        payEntity.setIsPaid(false);
        payEntity.setPhoneNumber(phoneno);
        payEntity.setCheckoutRequestID(okPushResponse.getCheckoutRequestID());
        payEntity.setBusiness_krapin(businesskra);
        payEntity.setTransactionDate(LocalDateTime.now());
        payEntity.setOwner(owner);
        payEntity.setPaidBy(owner.getEmail());
        return payEntity;
    }

    public void callback(StkCallbackRequest stkCallbackRequest) throws Exception {
        try {
            String jsonResponse = stkCallbackRequest.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            StkCallbackRequest StkCallbackRequest = (StkCallbackRequest) objectMapper.readValue(jsonResponse,
                    StkCallbackRequest.class);
            StkCallBack stkCallBack = StkCallbackRequest.getBody().getStkCallback();
            CallbackMetadata callbackMetadata = stkCallBack.getCallbackMetadata();
            String CheckoutRequestID = stkCallBack.getCheckoutRequestId().toString();

            PaymentEntity Updatepay = paymentRepo.findByCheckoutRequestID(CheckoutRequestID);

            if (Updatepay != null) {
                String transactiondate = callbackMetadata.getItemValueByName("TransactionDate");
                Updatepay.setCheckoutRequestID(CheckoutRequestID);
                Updatepay.setMpesa_receipt(callbackMetadata.getItemValueByName("MpesaReceiptNumber"));
                Updatepay.setIsPaid(true);
                Updatepay.setTransactionDate(HelperUtility.getLocalDateTime(transactiondate));
                Updatepay.setPhoneNumber(callbackMetadata.getItemValueByName("PhoneNumber"));
                System.out.println(this.paymentRepo.toString());

                paymentRepo.save(Updatepay);
            } else {
                System.out.println("No payment to update for CHeckoutRequest : " + CheckoutRequestID);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

}