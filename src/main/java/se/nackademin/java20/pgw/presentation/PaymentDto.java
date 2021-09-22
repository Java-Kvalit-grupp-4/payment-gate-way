package se.nackademin.java20.pgw.presentation;

/**
 * the incoming objet from Hakims backend
 */
public class PaymentDto {
    private String reference;
    private long amount;

    public PaymentDto() {
    }

    public String getReference() {
        return reference;
    }

    public long getAmount() {
        return amount;
    }
}
