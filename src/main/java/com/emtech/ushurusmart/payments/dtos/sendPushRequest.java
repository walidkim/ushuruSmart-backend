package com.emtech.ushurusmart.payments.dtos;

public class sendPushRequest {
    private String phoneNo;
    private String amount;
    private String eslipNo;

    public sendPushRequest(String phoneNo, String amount, String eslipNo) {
        this.phoneNo = phoneNo;
        this.amount = amount;
        this.eslipNo = eslipNo;
    }

    public String getPhoneNo() {
        return this.phoneNo;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getEslipNo() {
        return this.eslipNo;
    }

    public void setPhoneNo(final String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    public void setEslipNo(final String eslipNo) {
        this.eslipNo = eslipNo;
    }
}
