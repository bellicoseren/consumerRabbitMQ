package bellicose.com.receiveRabbitMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * Hello world!
 *
 */
public class ConsumerClient
{
	private final static String QUEUE_NAME = "ratita";
	
    public static void main( String[] args ) throws IOException, TimeoutException
    {
    	ConnectionFactory factory = new ConnectionFactory();
    	factory.setHost("localhost");
    	Connection connection = factory.newConnection();
    	Channel channel = connection.createChannel();
    	
    	try {
    		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    		System.out.println( " [*] Esperando por mensajes. Para salir presiona CTRL + C" );
    		
    		Consumer consumer = new DefaultConsumer(channel) {
    			@Override
    			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
    				String message = new String(body, "UTF-8");
    				System.out.println(String.format("Recibido: %s", message));
    			}
    		};
    		
    		channel.basicConsume(QUEUE_NAME, true, consumer);
    		
    		Thread.sleep(5000);
    	} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
    		channel.close();
    		connection.close();
    	}
    }
}
