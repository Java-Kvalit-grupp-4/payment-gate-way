package se.nackademin.java20.pgw.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nackademin.java20.pgw.domain.Payment;
import se.nackademin.java20.pgw.domain.PaymentNotificationService;
import se.nackademin.java20.pgw.domain.PaymentRepository;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentService {
    private final static Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final PaymentNotificationService paymentNotificationService;

    public PaymentService(PaymentRepository paymentRepository, PaymentNotificationService paymentNotificationService) {
        this.paymentRepository = paymentRepository;
        this.paymentNotificationService = paymentNotificationService;
    }

    /**
     * Creates a new payment with the incoming order-number
     * @param reference
     * @return the created Payment
     */
    @Transactional
    public Payment createPayment(String reference) {
        LOG.info("Creating payment");
        final Payment created = paymentRepository.save(new Payment(reference, "CREATED"));
        paymentNotificationService.notifyPaid(created);
        return created;
    }

    /**
     * Every 60 sec this method thecks if there is any unpaid Payments in the db
     * if it is that payment will be handled in handlePayment
     */
    @Transactional
    public void performPayments() {
        LOG.info("Checking payments to be performed");
        Duration duration = Duration.ofSeconds(60);
        List<Payment> payments = paymentRepository.finalAllUnpaid()
                .stream()
                .filter(p -> p.getCreated().plus(duration).isBefore(Instant.now()))
                .collect(Collectors.toList());
        LOG.info("Found {} payments to be performed", payments.size());
        payments.forEach(this::handlePayment);
    }

    /**
     * Saves the incoming Payment to the db and sends a message to HakimLivs backend
     * @param payment
     */
    private void handlePayment(Payment payment) {
        LOG.info("Marking payment {} as paid", payment.getId());
        payment.markAsPaid();
        paymentRepository.save(payment);
        paymentNotificationService.notifyPaid(payment);//Skickar ett payment object till hakim --->

    }
}
