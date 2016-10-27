package test.mq;
import org.apache.commons.configuration.PropertiesConfiguration;
import test.mq.mq.Consumer;

/**
 * Created by xiaopeng on 2016/10/26.
 */
public class Application {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Power.io sms module ...");
        // load config
        System.out.println("Loading Power.io sms module config files ...");
        // PropertiesConfiguration smsConfig;
        PropertiesConfiguration rabbitConfig;

        if (args.length >= 1) {
            rabbitConfig = new PropertiesConfiguration(args[0]);
        } else {
            rabbitConfig = new PropertiesConfiguration("config/rabbit.properties");
        }

        // comsumer
        System.out.println("Initializing consumer ...");
        Consumer consumer = new Consumer();
        consumer.init(rabbitConfig);
        System.out.println("Power.io sms module is up and running.");
    }
}
