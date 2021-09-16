package se.nackademin.java20.pgw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import se.nackademin.java20.pgw.application.PaymentService;
import se.nackademin.java20.pgw.domain.PaymentNotificationService;
import se.nackademin.java20.pgw.domain.PaymentRepository;
import se.nackademin.java20.pgw.persistance.PaymentRepositoryHibernate;
import se.nackademin.java20.pgw.persistance.RabbitNotificationService;

import javax.persistence.EntityManager;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {
    final String fanoutExchangeName = "payments-exchange";


    static final String queueName = "payments";

    @Bean
    public Binding declareBindingGeneric() {
        return new Binding("payments", Binding.DestinationType.QUEUE, "payments-exchange", "", null);
    }

    @Bean
    Queue queue() {
        return new Queue(queueName);
    }


    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory, final Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public PaymentNotificationService paymentNotificationService(RabbitTemplate template, ObjectMapper objectMapper) {
        return new RabbitNotificationService(template, objectMapper);
    }

    //...............................................................................................

    @Bean
    public PaymentRepository paymentRepository(EntityManager em) {
        return new PaymentRepositoryHibernate(em);
    }

    @Bean
    public PaymentService personalFinanceService(PaymentRepository paymentRepository, PaymentNotificationService paymentNotificationService) {
        return new PaymentService(paymentRepository, paymentNotificationService);
    }









}
