package demo_rabbitmq.rabbit_demo.receiver;

public class RabbitMqReceiver {

	public String onMessage(String personName) {
		return "Hello "+personName;
	}
}
