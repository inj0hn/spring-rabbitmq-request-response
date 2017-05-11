package demo_rabbitmq.rabbit_demo.sender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Tomas Kloucek
 */
public class RabbitMqSender {

	@Autowired
	private RabbitTemplate template;
	
	public void send() {
		Message message = MessageBuilder.withBody("inj0hn request".getBytes())
				.setContentType("text/plain")
				.build();

		// greeting
		Message reply = this.template.sendAndReceive(message);
		System.out.println("Reply from server is: "+new String(reply.getBody()));
	}
	
	public void sendMany(int size) throws Exception{
		List<Callable<String>> tasks = new ArrayList<>();
		for(int i=0 ; i<size ; i++) {
			String threadName = "Thread:" + String.format("%04d", new Integer(i+1));
			Message rabbitMessage = MessageBuilder.withBody(threadName.getBytes())
					.setContentType("text/plain")
					.build();
			tasks.add(
					() -> {
						Message reply = template.sendAndReceive(rabbitMessage); 
						return new String(reply.getBody());
			});
		}
		ExecutorService executor = Executors.newFixedThreadPool(size);
		List<Future<String>> futures = executor.invokeAll(tasks);
		assert futures.size() == size;
		Set<String> threadNames = new TreeSet<>();
		for(Future<String> f : futures) {
			threadNames.add(f.get());
		}
		assert threadNames.size() == size;
		threadNames.stream().forEach(System.out::println);
	}
}

