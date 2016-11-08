package edu.usc.parknpay.database;

/**
 * Created by yunalee on 11/7/16.
 */

public class PaymentMethod {

    private String paymentMethodType;
    private String paymentInformation;

    public PaymentMethod(String paymentMethodType, String paymentInformation) {
        this.paymentMethodType = paymentMethodType;
        this.paymentInformation = paymentInformation;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public String getPaymentInformation() {
        return paymentInformation;
    }

}
