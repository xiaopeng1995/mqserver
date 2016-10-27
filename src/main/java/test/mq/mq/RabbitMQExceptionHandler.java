package test.mq.mq;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.DefaultExceptionHandler;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

/**
 * Exception Handler for RabbitMQ
 */
public class RabbitMQExceptionHandler extends DefaultExceptionHandler {


    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    public void handleConnectionRecoveryException(Connection conn, Throwable exception) {
        // ignore java.net.ConnectException as those are
        // expected during recovery and will only produce noisy
        // traces
        if (exception instanceof ConnectException) {
            // do nothing
        } else {
            System.out.println("error: Caught an exception during connection recovery");
        }
    }

    @Override
    public void handleChannelRecoveryException(Channel ch, Throwable exception) {
        System.out.println("error:Caught an exception when recovering channel {}"+ch+exception);
    }

    @Override
    public void handleTopologyRecoveryException(Connection conn, Channel ch, TopologyRecoveryException exception) {
        System.out.println("Caught an exception when recovering topology"+exception);
    }

    @Override
    protected void handleChannelKiller(Channel channel, Throwable exception, String what) {
        System.out.println("{}  threw an exception for channel {}"+ what+ channel+exception);
        try {
            channel.close(AMQP.REPLY_SUCCESS, "Closed due to exception from " + what);
        }catch (Exception ioe) {
            System.out.println("Failure during close of channel {} after exception"+channel+ioe);
            channel.getConnection().abort(AMQP.INTERNAL_ERROR, "Internal error closing channel for " + what);
        }
    }

    @Override
    protected void handleConnectionKiller(Connection connection, Throwable exception, String what) {
        System.out.println("{}  threw an exception for connection {}"+what+connection+ exception);
        try {
            connection.close(AMQP.REPLY_SUCCESS, "Closed due to exception from " + what);
        } catch (AlreadyClosedException ace) {
            // do nothing
        } catch (IOException ioe) {
            System.out.println("Failure during close of connection {} after exception"+connection+ioe);
            connection.abort(AMQP.INTERNAL_ERROR, "Internal error closing connection for " + what);
        }
    }
}
