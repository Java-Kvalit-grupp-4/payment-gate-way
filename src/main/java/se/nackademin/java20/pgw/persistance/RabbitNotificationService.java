package se.nackademin.java20.pgw.persistance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import se.nackademin.java20.pgw.domain.Payment;
import se.nackademin.java20.pgw.domain.PaymentNotificationService;

public class RabbitNotificationService implements PaymentNotificationService {
    private final static Logger LOG = LoggerFactory.getLogger(RabbitNotificationService.class);


    private final ObjectMapper objectMapper;
    private final RabbitTemplate template;

    public RabbitNotificationService(RabbitTemplate template, ObjectMapper objectMapper) {

        this.template = template;
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a message to Hakims backend whit the information when a order is created and paid
     * @param payment
     */
    @Override
    public void notifyPaid(Payment payment) {
        try {
            //String object = objectMapper.writeValueAsString(new PaymentMessageDto(payment.getReference(), "" + payment.getId(), payment.getStatus()));
            String object = objectMapper.writeValueAsString((payment.getReference() + "," + payment.getId() +","+ payment.getStatus()));
            LOG.info("Sending {}", object);
            template.convertAndSend("payments-exchange", "", object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
