package test.mq.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import test.mq.sms.YunPianClient;

import java.io.IOException;


/**
 * Consumer Handler
 */
public class ConsumerHandler extends DefaultConsumer {


    private YunPianClient smsClient;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public ConsumerHandler(Channel channel, YunPianClient smsClient) {
        super(channel);
        this.smsClient = smsClient;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        try {
            String verifyType = envelope.getRoutingKey();
            String mag = new String(body);
            this.smsClient.sendTplSms(mag, verifyType, mag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
