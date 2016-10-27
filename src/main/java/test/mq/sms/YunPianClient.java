package test.mq.sms;

import java.io.IOException;

/**
 * YunPian SMS Client
 */
public class YunPianClient {


    public YunPianClient() {

    }

    public void sendTplSms(String mobile, String type, String code) throws IOException {

        System.out.println("mobile:"+mobile+"\ntype:"+type+"\ncode:"+code);
    }
}
