//package com.price.discount.api.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.annotation.EnableRabbit;
//import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
//import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
//import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.retry.interceptor.RetryOperationsInterceptor;
//import org.springframework.util.ErrorHandler;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@EnableRabbit
//@Configuration
//public class RabbitMqConfiguration {
//
//    @Value("${rabbitmq.queue}")
//    private String queueName;
//    @Value("${rabbitmq.exchange}")
//    private String exchange;
//    @Value("${rabbitmq.routingkey}")
//    private String routingkey;
//    @Value("${rabbitmq.username}")
//    private String username;
//    @Value("${rabbitmq.password}")
//    private String password;
//    @Value("${rabbitmq.host}")
//    private String host;
//    @Value("${rabbitmq.virtualhost}")
//    private String virtualHost;
//    @Value("${rabbitmq.reply.timeout}")
//    private Integer replyTimeout;
//    @Value("${rabbitmq.concurrent.consumers}")
//    private Integer concurrentConsumers;
//    @Value("${rabbitmq.max.concurrent.consumers}")
//    private Integer maxConcurrentConsumers;
//
//    @Bean
//    @Qualifier("rabbitmq.demoQueue")
//    public Queue queue() {
//        Map<String, Object> deadLetter = new HashMap<>();
//        deadLetter.put("x-dead-letter-exchange", "rabbitmq.demoDLExchange");
//        deadLetter.put("x-max-length", 1000);
//
//        return new Queue(queueName, false, false, false, deadLetter);
//    }
//
//    @Bean
//    @Qualifier("rabbitmq.demoExchange")
//    public DirectExchange exchange() {
//        return new DirectExchange(exchange);
//    }
//    @Bean
//    public Binding binding(@Qualifier("rabbitmq.demoQueue") Queue queue, @Qualifier("rabbitmq.demoExchange") DirectExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
//    }
//
//    @Bean
//    @Qualifier("rabbitmq.demoDLQueue")
//    public Queue dlQueue() {
//        return new Queue("rabbitmq.demoDLQueue", false);
//    }
//
//    @Bean
//    @Qualifier("rabbitmq.demoDLExchange")
//    public DirectExchange dlExchange() {
//        return new DirectExchange("rabbitmq.demoDLExchange");
//    }
//
//    @Bean
//    public Binding dlBinding(@Qualifier("rabbitmq.demoDLQueue") Queue queue, @Qualifier("rabbitmq.demoDLExchange") DirectExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("rabbitmq.routingkey");
//    }
//
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return new Jackson2JsonMessageConverter(objectMapper);
//    }
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setVirtualHost(virtualHost);
//        connectionFactory.setHost(host);
//        connectionFactory.setUsername(username);
//        connectionFactory.setPassword(password);
//
//        return connectionFactory;
//    }
//    @Bean
//    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setDefaultReceiveQueue(queueName);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        rabbitTemplate.setReplyAddress(queue().getName());
//        rabbitTemplate.setReplyTimeout(replyTimeout);
//        rabbitTemplate.setUseDirectReplyToContainer(false);
//
//        return rabbitTemplate;
//    }
//    @Bean
//    public AmqpAdmin amqpAdmin() {
//        return new RabbitAdmin(connectionFactory());
//    }
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
//        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory());
//        factory.setMessageConverter(jsonMessageConverter());
//        factory.setConcurrentConsumers(concurrentConsumers);
//        factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
//        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
//        factory.setAdviceChain(setRetries());
//
//        return factory;
//    }
//    @Bean
//    public ErrorHandler errorHandler() {
//        return new ConditionalRejectingErrorHandler(new MyFatalExceptionStrategy());
//    }
//    public static class MyFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
//        @Override
//        public boolean isFatal(Throwable t) {
//            if (t instanceof ListenerExecutionFailedException) {
//                ListenerExecutionFailedException lefe = (ListenerExecutionFailedException) t;
//                log.error("Failed to process inbound message from queue "
//                        + lefe.getFailedMessage().getMessageProperties().getConsumerQueue()
//                        + "; failed message: " + lefe.getFailedMessage(), t);
//            }
//            return super.isFatal(t);
//        }
//    }
//
//    @Bean("operationsInterceptor")
//    public RetryOperationsInterceptor setRetries() {
//        return RetryInterceptorBuilder.stateless()
//                .maxAttempts(4)
//                .backOffOptions(1000,
//                        2,
//                        10000)
//                .recoverer(new RejectAndDontRequeueRecoverer())
//                .build();
//    }
//}

/*
import lombok.extern.slf4j.Slf4j;
import net.aalkhodiry.api.configurations.queues.CustomDLQueueDLQueue;
import net.aalkhodiry.api.utils.CustomJsonMessageConverter;
import org.aopalliance.intercept.Interceptor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;


 // For RabbitMq

@Configuration
@EnableRabbit
@Slf4j
public class RabbitmqConfiguration {

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;


     // To configure @RabbitListener

      //@return

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConcurrentTaskScheduler taskScheduler) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setTaskExecutor(taskScheduler);
        factory.setConnectionFactory(cachingConnectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(6);
        factory.setTransactionManager(rabbitTransactionManager());

        // use a non-transactional template for the DLQ
        RabbitTemplate dlqTemplate = new RabbitTemplate(this.cachingConnectionFactory);
        // When retries reach the maximum number that massage is dead-lettered to jz-exceptions-exchange
        Interceptor retryInterceptor = RetryInterceptorBuilder.stateless()
                .maxAttempts(5)
                .recoverer(new RepublishMessageRecoverer(dlqTemplate, CustomDLQueue.EXCEPTION_EXCHANGE))
                .build();

        factory.setAdviceChain(retryInterceptor);
        return factory;
    }

    public RabbitTransactionManager rabbitTransactionManager() {
        return new RabbitTransactionManager(cachingConnectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }


     // Required for executing administration functions against an AMQP Broker

    @Bean
    public AmqpAdmin rabbitAdmin() {
        return new RabbitAdmin(cachingConnectionFactory);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        final CustomJsonMessageConverter customJsonMessageConverter = new CustomJsonMessageConverter();
        return customJsonMessageConverter;
    }
}
*/