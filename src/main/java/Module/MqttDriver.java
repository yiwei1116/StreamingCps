package Module;

/**
 * Created by yiwei on 2017/5/31.
 */
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
    public class MqttDriver extends AbstractDriver implements Serializable {
    private static final Logger LOG = LogManager.getLogger(MqttDriver.class );
    private final String brokerUrl;
    private  final String topic;
    private MqttClient client;
    private MqttTopic mqttTopic;

    public MqttDriver(String path, String brokerUrl, String topic) {
        super(path);
        this.brokerUrl = brokerUrl;
        this.topic = topic;
    }
    @Override
    public void init() throws Exception {
        client = new MqttClient(brokerUrl, MqttClient.generateClientId(), new
                MemoryPersistence());
        LOG.info(String.format("Attempting to connect to broker %s", brokerUrl));
        MqttConnectOptions connOpt = new MqttConnectOptions();

        client.connect();

        mqttTopic = client.getTopic(topic);
        LOG.info(String.format("Connected to broker %s", brokerUrl));
    }
    @Override
    public void close() throws Exception {
        if (client != null ) {
            client.disconnect();
        }
    }
    @Override
    public void sendRecord(String record) throws Exception {
        try {
        mqttTopic.publish( new MqttMessage(record.getBytes(StandardCharsets.UTF_8)));
            Thread.sleep(100);
    } catch (MqttException e){
            if (e.getReasonCode()== MqttException.REASON_CODE_MAX_INFLIGHT){
            Thread.sleep(10);
    }
        }
            }

    public static void main(String[] args) {
        BasicConfigurator.configure();
    if (args.length != 3) {
        System.err.println("Usage:MqttDriver <path_to_input_folder> <broker_url> <topic>");
                System.exit(-1);
    }
        String path = args[0];
        String brokerUrl = args[1];
        String topic = args[2];
        MqttDriver driver = new MqttDriver(path, brokerUrl,topic);
        try {
        driver.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                driver.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            }
                 }
