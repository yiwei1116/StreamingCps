package ncku.streamCp;


import Module.PreprocessModule;
import com.esotericsoftware.minlog.Log;

import Module.CompressModule;


import Module.ConversionModule;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.io.*;
import java.util.*;

import static Module.PreprocessModule.getEncodeLength;
import static Module.PreprocessModule.intToString;
import static Module.PreprocessModule.subValue;

public class Exp {

    public static String K="";
    public static ConversionModule conversionModule = new ConversionModule();
    public static CompressModule compressModule = new CompressModule();
    private  static StringBuffer sensorData = new StringBuffer();

    public  static  String getString(List<Integer>Compress){


        for (int i = 0 ; i < Compress.size() ; i++){

            K = K + Compress.get(i).toString();


        }

        return K ;

    }


    public  static  String toBinary12(List<Integer>Compress){

        String encodeBinary="";
        for (int i = 0 ; i < Compress.size() ; i++){

            encodeBinary+=intToString(Compress.get(i),12);


        }

        return encodeBinary ;

    }
    public static void  writeTo(String s,String fileName)
    {



        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(s);
            out.close();

        }
        catch (IOException e)
        {
            System.out.println("Exception ");
        }







    }





    public static void oldDataSave(){




        Scanner scannerF = null;
        FileInputStream inputStreamF = null;
        try{


            try {
                inputStreamF = new FileInputStream("/home/steve02/StreamingCps/10M.txt");
              //  inputStreamF = new FileInputStream("/home/yiwei/IdeaProjects/FPro/PubNub.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            scannerF = new Scanner(inputStreamF, "UTF-8");


            while (scannerF.hasNextLine()) {

                String line = scannerF.nextLine();
                sensorData.append(line+"\r\n");


            }

        }
        finally {
            if (inputStreamF!=null){

                try {
                    inputStreamF.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (scannerF!=null){

                scannerF.close();

            }
        }


    }

    public static void main(String[] args) throws IOException,InterruptedException{
        /*PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-5f1b7c8e-fbee-11e3-aa40-02ee2ddab7fe");
        pnConfiguration.setPublishKey("demo");
        pnConfiguration.setSecure(false);

        PubNub pubnub = new PubNub(pnConfiguration);*/
        /*SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("StreamCompress");
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(5));
        //JavaDStream<String> stream = javaStreamingContext.textFileStream("/home/yiwei/IdeaProjects/FPro/PubNub.txt").cache();
        JavaReceiverInputDStream<String> lines = javaStreamingContext.receiverStream(new MyReceiver("localhost", 9999));*/
        double compressRatio ;
        double spaceSaving;
        //oldDataSave();
    /*    pubnub.addListener(new SubscribeCallback() {
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

                Log.error("pubnub", String.valueOf(message.getMessage().get("radiation_level")));

                sensorData.append(String.valueOf(message.getMessage().get("radiation_level")).substring(1,4)+"\r\n");
                writeTo(String.valueOf(sensorData),"256K.txt");
                if(sensorData.length()>256000){


                    pubnub.destroy();
                }

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });


       pubnub.subscribe().channels(Arrays.asList("pubnub-sensor-network")).execute();*/



        FileInputStream inputStream = null;
        Scanner scanner = null;
        ArrayList<Integer> DiffList = new ArrayList<Integer>();
        ArrayList<Integer> radiationList = new ArrayList<Integer>();
        double originSensorSize = 0 ;


        try {
          //  inputStream = new FileInputStream("/home/yiwei/IdeaProjects/FPro/100K.txt");
            inputStream = new FileInputStream("/home/steve02/StreamingCps/radiation_level/100K.txt");
            scanner = new Scanner(inputStream, "UTF-8");

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();

                int k = Integer.valueOf(line);
                originSensorSize += line.length();

                radiationList.add(k);

            }

            if (scanner.ioException() != null) {

                throw scanner.ioException();

            }

        }finally {
            if (inputStream!=null){

                inputStream.close();

            }
            if (scanner!=null){

                scanner.close();

            }
        }
     /*   DiffList = subValue(radiationList);
        List<Integer>compressList = new ArrayList<>();
        compressList = compressModule.compress(conversionModule.conversionTable(DiffList));
        double  encodingTextLength = getEncodeLength(compressList,13);
       // Log.error("num",intToString(2048,13));
        String  encodeBinary = toBinary12(compressList);
        //writeTo(encodeBinary,"Binary12");
        System.out.println("Origin bit: "+originSensorSize *8);// byte to bit
        System.out.println("Compress bit: "+encodingTextLength);
        compressRatio = (originSensorSize *8 / encodingTextLength);
        System.out.println("Compress Ratio: "+Math.round(compressRatio*100.0)/100.0);//只取小數點後兩位
        spaceSaving = (1 - (1/compressRatio));
        System.out.println("Space Saving: "+Math.round(spaceSaving*100.0)/100.0);
        String decodingText = compressModule.decompress(compressList);
        compressModule.reConstruct(decodingText);*/
        /*System.out.println(conversionModule.conversionTable(DiffList));
        System.out.println(decodingText);*/
        String testText = "12321";//BAbABbAbAbABacCZA
        List<Integer> compressed = compressModule.compress(testText);
        System.out.println(compressed);
        String decompressed = compressModule.decompress(compressed);
        System.out.println(testText);
        System.out.println(decompressed);
     //   Log.error("is before compress the same as after compress ?",String.valueOf(testText.equals(decompressed)));
      //Log.error("is before compress the same as after compress ?", String.valueOf(compressModule.reConstruct(decodingText).equals(radiationList)));



    }}

