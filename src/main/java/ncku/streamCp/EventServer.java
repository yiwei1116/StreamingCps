package ncku.streamCp;

import com.esotericsoftware.minlog.Log;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class EventServer {
    private static final Executor SERVER_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final int PORT = 9999;
    private static final String DELIMITER = ":";
    private static final long EVENT_PERIOD_SECONDS = 1;
    private static final Random random = new Random();

    public static void main(String[] args) throws IOException, InterruptedException {
        BlockingQueue<String> eventQueue = new ArrayBlockingQueue<>(100);

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-5f1b7c8e-fbee-11e3-aa40-02ee2ddab7fe");
        pnConfiguration.setPublishKey("demo");
        pnConfiguration.setSecure(false);

        PubNub pubnub = new PubNub(pnConfiguration);
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {


                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                }

                else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc

                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
                        pubnub.publish().channel("pubnub-sensor-network").message("radiation_level").async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                // Check whether request successfully completed or not.
                                if (!status.isError()) {

                                    // Message successfully published to specified channel.
                                }
                                // Request processing failed.
                                else {

                                    // Handle message publish error. Check 'category' property to find out possible issue
                                    // because of which request did fail.
                                    //
                                    // Request can be resent using: [status retry];
                                }
                            }
                        });
                    }
                }
                else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                }
                else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // Handle new message stored in message.message
                if (message.getChannel() != null) {
                    // Message has been received on channel group stored in
                    // message.getChannel()
                }
                else {
                    // Message has been received on channel stored in
                    // message.getSubscription()
                }

                //Log.error("pubnub", String.valueOf(message.getMessage().get("radiation_level")));

               /* try {
                    eventQueue.put((String.valueOf(message.getMessage().get("radiation_level"))).substring(1,4));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/


            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });


        pubnub.subscribe().channels(Arrays.asList("pubnub-sensor-network")).execute();
        SERVER_EXECUTOR.execute(new SteamingServer(eventQueue));
    /*    while (true) {
            eventQueue.put(generateEvent());
            Thread.sleep(TimeUnit.SECONDS.toMillis(EVENT_PERIOD_SECONDS));
        }*/
        eventQueue.put(readBinaryFile());

    }

    private static String generateEvent() {
        int userNumber = random.nextInt(10);
        String event = random.nextBoolean() ? "login" : "purchase";
        // In production use a real schema like JSON or protocol buffers
        return String.format("user-%s", userNumber) + DELIMITER + event;
    }

    private static class SteamingServer implements Runnable {
        private final BlockingQueue<String> eventQueue;

        public SteamingServer(BlockingQueue<String> eventQueue) {
            this.eventQueue = eventQueue;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(PORT);
                 Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ) {
                while (true) {
                    String event = eventQueue.take();
                    System.out.println(String.format("radiation_level: %s ", event));
                    out.println(event);
                }
            } catch (IOException|InterruptedException e) {
                throw new RuntimeException("Server error", e);
            }
        }
    }
    public static String readBinaryFile(){



        StringBuffer sensorData = new StringBuffer();
        Scanner scanner = null;
        FileInputStream inputStream = null;
        try{

            //     inputStreamF = new FileInputStream("/home/steve02/StreamingCps/PubNub.txt");
            try {
                inputStream = new FileInputStream("/home/yiwei/IdeaProjects/FPro/Binary12");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            scanner = new Scanner(inputStream, "UTF-8");


            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                sensorData.append(line);


            }

        }
        finally {
            if (inputStream!=null){

                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (scanner!=null){

                scanner.close();

            }
        }

            return sensorData.toString();
    }
}