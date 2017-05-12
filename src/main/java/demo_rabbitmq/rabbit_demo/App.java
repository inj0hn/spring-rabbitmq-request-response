package demo_rabbitmq.rabbit_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import demo_rabbitmq.rabbit_demo.sender.RabbitMqSender;

/**
 * Demo of rabbitmq
 */
@SpringBootApplication
public class App 
{	
    public static void main(String[] args) throws Exception {
        final ConfigurableApplicationContext configAppContext = SpringApplication.run(App.class, args);
        final RabbitMqSender sender = configAppContext.getBean(RabbitMqSender.class);
        sender.send();
        System.out.println("***************************\nSending without correlationId\n***************************");
        sender.sendMany(1010);
        System.out.println("***************************\nSending with correlationId\n***************************");
        sender.sendManyWithCorrelationId(1009);
    }
}
