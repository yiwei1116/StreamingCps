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

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                                String line;
                                String unCompress="";

                                int pre = 200;
                                int cur;
                                int diff;

                                long StartTime=0;
                                long EndTime=0;
                                long actualTime =0;
                                long durationTime =0;


                                double i =0 ;
                                double j =0 ;
                                int k = 1;

                                List<String> compressList = new ArrayList<>();
                                compressList.add(0,"");
                                compressList.add(1,"");
                                compressList.add(2,String.valueOf(51));
                                compressList.add(3,"");

                                while ((line = br.readLine()) != null ) {

                                    StartTime = System.currentTimeMillis();
                                    i++;
                                    cur = Integer.valueOf(line);
                                    diff = cur - pre ;
                                    pre = cur ;

                                    unCompress = conversionModule.conversionT(diff);

                                    compressList.set(0,unCompress);


                                    compressList = CompressModule.compress(compressList);


                                    if(!compressList.get(3).equals("")){
                                        k=1;
                                        sendRecord(compressList.get(3));
                                        Log.error("send",compressList.get(3));
                                        j++;//compress count
                                        compressList.set(3,"");



                                    }



                                    Thread.sleep(500);
                                    EndTime = System.currentTimeMillis();
                                    durationTime += ((EndTime-StartTime)*k);

                                    k++;
                                }

                                System.out.println("EndTime  : " +(EndTime));
                                System.out.println("StartTime  : " +(StartTime));
                                 actualTime = (durationTime-500)/1000;
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
