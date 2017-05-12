package demo_rabbitmq.rabbit_demo.configuration;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo_rabbitmq.rabbit_demo.receiver.RabbitMqReceiver;
import demo_rabbitmq.rabbit_demo.sender.RabbitMqSender;

@Configuration
public class RabbitMqReceiveConfiguration {
	
	{
		System.out.println("Creating receiving configuration.");
	}
	
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = 
				new CachingConnectionFactory("localhost");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		return connectionFactory;
	}
			
	@Bean
	public Queue greeting() {
		return new Queue("request.queue");
	}
	
	@Bean
	public Queue replies() {
		return new Queue("reply.queue");
	}
	
    MessageListener receiver() {
        return new MessageListenerAdapter(new RabbitMqReceiver(), "onMessage");
    }
    
    @Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Queue replies) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setExchange("dorado");
		template.setRoutingKey("request.queue");		
		template.setReplyAddress("dorado"+"/"+replies.getName());
		template.setReplyTimeout(60000);
		return template;
	}
	
	@Bean
	public SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory, 
			RabbitTemplate rabbitTemplate, Queue replies) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageListener(rabbitTemplate);
		container.setQueues(replies);
		container.setMaxConcurrentConsumers(20);
		return container;
	}
	
	@Bean
	public SimpleMessageListenerContainer serviceListenerContainer(
			ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(greeting());
		container.setMessageListener(receiver());
		container.setMaxConcurrentConsumers(20);
		return container;
	}
	
	@Bean
	public RabbitMqSender sender() {
		final RabbitMqSender sender = new RabbitMqSender();
		return sender;
	}
}
