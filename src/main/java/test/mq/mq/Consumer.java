package test.mq.mq;

import com.rabbitmq.client.*;
import org.apache.commons.configuration.AbstractConfiguration;
import test.mq.sms.YunPianClient;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ Publisher
 */
public class Consumer {


    // rabbitmq
    protected Connection conn;
    protected Channel channel;

    // sms
    protected String SMS_VERIFY_TOPIC = "mithqtt.wechat";

    public void init(AbstractConfiguration config) {
        try {
            System.out.println("Initializing RabbitMQ consumer resources ...");
            ConnectionFactory cf = new ConnectionFactory();
            cf.setUsername(config.getString("rabbitmq.userName", ConnectionFactory.DEFAULT_USER));
            cf.setPassword(config.getString("rabbitmq.password", ConnectionFactory.DEFAULT_PASS));
            cf.setVirtualHost(config.getString("rabbitmq.virtualHost", ConnectionFactory.DEFAULT_VHOST));
            cf.setAutomaticRecoveryEnabled(true);
            cf.setExceptionHandler(new RabbitMQExceptionHandler());
            System.out.println(config.getString("rabbitmq.addresses"));
            this.conn = cf.newConnection(Address.parseAddresses(config.getString("rabbitmq.addresses")));
            this.channel = conn.createChannel();
            this.channel.exchangeDeclare(SMS_VERIFY_TOPIC, "topic");
            this.channel.queueDeclare("agentinfo", true, false, true, null);
            this.channel.queueBind("agentinfo", SMS_VERIFY_TOPIC, "#");
            this.channel.basicConsume("agentinfo", true, new ConsumerHandler(this.channel,new YunPianClient()));
        } catch (Exception e) {
            System.out.println("Failed to connect to RabbitMQ servers"+e);
            throw new IllegalStateException("Init RabbitMQ communicator failed");
        }
    }

    public void destroy() {
        try {
            if (this.conn != null) this.conn.close();
        } catch (IOException e) {
            System.out.println("Communicator error: Exception closing the RabbitMQ connection, exiting uncleanly"+ e);
        }
    }
}
