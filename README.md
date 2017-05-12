#mvn clean install
#java -jar target/demo-0.0.1-SNAPSHOT.jar


Use RabbitTemplate.sendAndReceive() (or convertSendAndReceive()) with a reply listener container; the template will take care of the correlation for you.

http://stackoverflow.com/questions/23469486/spring-integration-with-rabbit-amqp-for-client-sends-message-server-receives