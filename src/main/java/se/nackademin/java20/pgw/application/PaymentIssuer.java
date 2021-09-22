package se.nackademin.java20.pgw.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentIssuer {
    private final static Logger LOG = LoggerFactory.getLogger(PaymentIssuer.class);

    private final PaymentService paymentService;

    @Autowired
    public PaymentIssuer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * every 5 sec does this preform payments
     */
    @Scheduled(fixedRate = 5000)
    public void scheduleFixedDelayTask() {
        LOG.info("Checking payment statuses..");
        paymentService.performPayments();

    }
}
