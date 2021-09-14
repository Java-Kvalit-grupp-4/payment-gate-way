package se.nackademin.java20.pgw.persistance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentMessageDto {
    @JsonProperty("reference")
    private final String reference;
    @JsonProperty("paymentId")
    private final String paymentId;
    @JsonProperty("status")
    private final String status;

    @JsonCreator
    public PaymentMessageDto(@JsonProperty("reference") String reference, @JsonProperty("paymentId") String paymentId, @JsonProperty("status") String status) {
        this.reference = reference;
        this.paymentId = paymentId;
        this.status = status;
    }

    @Override
    public String toString() {
        return "PaymentMessageDto{" +
                "reference='" + reference + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
