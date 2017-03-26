


import com.esotericsoftware.minlog.Log;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import org.apache.spark.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;

import java.io.*;
import java.util.*;

public class Exp {

    public static String K="";
    public static ConversionModule conversionModule = new ConversionModule();
    public static CompressModule compressModule = new CompressModule();
    /** Compress a string to a list of output symbols. */
    public String readTextFile(String filename)
    {String returnValue="";
        FileReader file=null;
        String line="";
        try{
            file=new FileReader(filename);
            BufferedReader reader=new BufferedReader(file);


            while((line=reader.readLine())!=null){

                returnValue+=line;
            }
        }catch(FileNotFoundException e){
            throw new RuntimeException("File not found");
        }catch(IOException e){
            throw new RuntimeException("IO Error occured");

        }finally{
            if(file!=null){
                try{
                    file.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }return returnValue;
    }


    public  static  String getString(List<Integer>Compress){


        for (int i = 0 ; i < Compress.size() ; i++){

            K = K + Compress.get(i).toString();


        }

        return K ;

    }
    public static void writeTo(String s,String fileName)
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







    public static ArrayList subValue(ArrayList<Integer> data){
        int p,q,k ;
        int base = 200;
        ArrayList<Integer> preprocessList = new ArrayList<Integer>();
        preprocessList.add(data.get(0)-base);
          for (int i =0 ; i< data.size()-1 ;i++) {
              p = data.get(i);
              q = data.get(i+1);
              k = q - p;

              preprocessList.add(k);
              Log.error("k",String.valueOf(preprocessList.get(i)));
          }
          return preprocessList;


    }



    public static void main(String[] args) throws IOException,InterruptedException{


/*

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-5f1b7c8e-fbee-11e3-aa40-02ee2ddab7fe");
        pnConfiguration.setPublishKey("demo");
        pnConfiguration.setSecure(false);

        PubNub pubnub = new PubNub(pnConfiguration);*/
        StringBuffer sensorData = new StringBuffer();
        Scanner scannerF = null;
        FileInputStream inputStreamF = null;
        try{

     //       inputStream = new FileInputStream("/home/steve02/StreamingCps/PubNub.txt");
            inputStreamF = new FileInputStream("/home/yiwei/IdeaProjects/FPro/PubNub.txt");

            scannerF = new Scanner(inputStreamF, "UTF-8");


            while (scannerF.hasNextLine()) {

                String line = scannerF.nextLine();
                sensorData.append(line+"\r\n");


            }

        }
            finally {
        if (inputStreamF!=null){

            inputStreamF.close();

        }
        if (scannerF!=null){

            scannerF.close();

        }
    }
  /*      pubnub.addListener(new SubscribeCallback() {
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

                sensorData.append(String.valueOf(message.getMessage().get("radiation_level"))+"\r\n");
                writeTo(String.valueOf(sensorData),"PubNub.txt");

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });


        pubnub.subscribe().channels(Arrays.asList("pubnub-sensor-network")).execute();*/

        /*SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("StreamCompress");
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf,Durations.seconds(1));
*/

        FileInputStream inputStream = null;
        Scanner scanner = null;
        ArrayList<Integer> DiffList = new ArrayList<Integer>();
        ArrayList<Integer> radiationList = new ArrayList<Integer>();
        try {
            inputStream = new FileInputStream("/home/yiwei/IdeaProjects/FPro/PubNub.txt");
         //   inputStream = new FileInputStream("/home/steve02/StreamingCps/RealTimeData1");
            scanner = new Scanner(inputStream, "UTF-8");

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine().substring(1,4);
                int k = Integer.valueOf(line);
                radiationList.add(k);
                System.out.println(k);


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
        DiffList = subValue(radiationList);
        List<Integer>compressList = new ArrayList<>();
        compressList = compressModule.compress(conversionModule.conversionTable(DiffList));
        String encodingText = getString(compressList);
        System.out.println(encodingText.);
        String decodingText = compressModule.decompress(compressList);
        compressModule.reConstruct(decodingText);
        System.out.println(conversionModule.conversionTable(DiffList));
        System.out.println(decodingText);
     /*   String testText = "BAbABbAbAbAB";//BAbABbAbAbABacCZA
        List<Integer> compressed = compressModule.compress(testText);
        System.out.println(compressed);
        String decompressed = compressModule.decompress(compressed);
        System.out.println(decompressed);
        Log.error("is before compress the same as after compress ?",String.valueOf(testText.equals(decompressed)));*/

       Log.error("is before compress the same as after compress ?", String.valueOf(compressModule.reConstruct(decodingText).equals(radiationList)));

   /*     javaStreamingContext.start();
        javaStreamingContext.awaitTermination();*/

    }}

