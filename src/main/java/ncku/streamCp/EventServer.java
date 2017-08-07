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
import Equipment.CompressModule;
import Module.ConversionModule;
import Module.PreprocessModule;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import static Module.PreprocessModule.intToString;

public class EventServer {
    private static final Executor SERVER_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final int PORT = 9999;
    private static final String DELIMITER = ":";
    private static final long EVENT_PERIOD_SECONDS = 1;
    private static final Random random = new Random();

    public static void main(String[] args) throws IOException, InterruptedException,FileNotFoundException {

        ConversionModule conversionModule = new ConversionModule();

        ArrayList<String> radiationList = new ArrayList<String>();

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

                try {
                    InputStream in = null;
                    try {
                        in = new FileInputStream(new File("/home/steve02/StreamingCps/radiation_level/256K.txt"));
                        //in = new FileInputStream(new File("/home/yiwei/IdeaProjects/FPro/100K.txt"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));


                   // StringBuilder out = new StringBuilder();
                    String line;
                    int pre = 200;
                    int cur;
                    int diff;
                    String unCompress="";
                    String compress ="";
                    List<Integer>compressList = new ArrayList<>();
                    try {
                        while ((line = reader.readLine()) != null) {
                            cur = Integer.valueOf(line);
                            diff = cur - pre ;
                            pre = cur ;

                            unCompress += conversionModule.conversionT(diff);
                            Log.error(String.valueOf(unCompress.length()));
                            if(unCompress.length()==100){
                              //  compressList = CompressModule.compress(unCompress);
                                System.out.println("compressList"+ compressList);
                                for(int i =0 ; i < compressList.size();i++)
                                {

                                    compress += (String.valueOf(intToString(compressList.get(i),10)))+" ";

                                }
                                eventQueue.put(compress);
                                unCompress = "";
                                compress = "";
                            }


                            //eventQueue.put(conversionModule.conversionT(diff));
                           Thread.sleep(50);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
/*
                    radiationList.add((String.valueOf(message.getMessage().get("radiation_level"))).substring(1,4));
                    ArrayList<Integer> radiationListInt = getIntegerArray(radiationList);
                    ArrayList<Integer> DiffList = preprocessModule.subValue(radiationListInt);
                    List<Integer> compressList = compressModule.compress(conversionModule.conversionTable(DiffList));
                    String binary12 = exp.toBinary12(compressList);
                    System.out.println();
                    eventQueue.put(intStream(compressList).toString());*/

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


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


}