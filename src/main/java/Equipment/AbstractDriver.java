package Equipment;

/**
 * Created by yiwei on 2017/5/31.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.minlog.Log;
import  org.apache.log4j.LogManager;
import  org.apache.log4j.Logger;

import static ncku.streamCp.Exp.conversionModule;

public abstract class  AbstractDriver implements Serializable{
            private static final Logger LOG = LogManager.getLogger(AbstractDriver. class );
            private String path;
    public AbstractDriver(String path) {
        this .path = path;
    }
    public abstract void init() throws Exception;
    public abstract void close() throws Exception;
    public abstract void sendRecord(String record) throws Exception;
    public void execute() throws Exception {
        try {
            init();
            try {
                InputStream in = null;
                try {
                    in = new FileInputStream(new File(path));
                  //  in = new FileInputStream(new File("/home/yiwei/IdeaProjects/FPro/100K.txt"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                                String line;
                                int pre = 200;
                                int cur;
                                int diff;
                                int dicIndex = 52;
                                long StartTime;
                                long EndTime;
                                String unCompress="";
                                String compress ="";
                                double i =0 ;
                double j =0 ;
                                List<String> compressList = new ArrayList<>();
                                compressList.add(0,"");
                                compressList.add(1,"");
                                compressList.add(2,String.valueOf(51));
                                compressList.add(3,"");
                                StartTime = System.currentTimeMillis();
                                while ((line = br.readLine()) != null ) {
                                    i++;
                                    cur = Integer.valueOf(line);
                                    diff = cur - pre ;
                                    pre = cur ;

                                    unCompress = conversionModule.conversionT(diff);

                                    compressList.set(0,unCompress);


                                    compressList = CompressModule.compress(compressList);

                                    if(!compressList.get(3).equals("")){

                                        sendRecord(compressList.get(3));
                                        Log.error("send",compressList.get(3));
                                        j++;//compress count
                                        compressList.set(3,"");
                                    //    unCompress = "";
                                   //     dicIndex++;


                                    }

                                /*    Log.error(String.valueOf(unCompress.length()));
                                    if(unCompress.length()==100){
                                        compressList = CompressModule.compress(unCompress);
                                        System.out.println("compressList"+ compressList);
                                        for(int i =0 ; i < compressList.size();i++)
                                        {

                                            compress += (String.valueOf(intToString(compressList.get(i),10)))+" ";

                                        }
                                        sendRecord(String.valueOf(cur));
                                        unCompress = "";
                                        compress = "";
                                    }*/
                                   /* Log.error(String.valueOf(cur));
                                    sendRecord(String.valueOf(cur));*/
                                    Thread.sleep(500);

                                }
                                EndTime = System.currentTimeMillis();
                                System.out.println("EndTime  : " +(EndTime));
                                System.out.println("StartTime  : " +(StartTime));
                                long actualTime = ((EndTime-StartTime))/1000;
                                System.out.println("During time : " +actualTime);
                                 System.out.println("avarge delay time : " +actualTime/i);
                                double compressbit = j*10;
                                double compressRatio = (3*i*8)/compressbit;

                                System.out.println("compress ratio : " +Math.round(compressRatio*100.0)/100.0);






        } finally {
            close();
        }
    }catch (IOException e) {
            e.printStackTrace();
        }
    }}
/**
 * https://github.com/apache/bahir/blob/master/streaming-mqtt/examples/src/main/scala/org/apache/spark/examples/streaming/mqtt/MQTTWordCount.scala
 **/
