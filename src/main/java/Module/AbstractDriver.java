package Module; /**
 * Created by yiwei on 2017/5/31.
 */

import com.esotericsoftware.minlog.Log;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static Module.PreprocessModule.intToString;
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
                                String unCompress="";
                                String compress ="";
                                List<Integer> compressList = new ArrayList<>();
                                while ((line = br.readLine()) != null ) {
                                    cur = Integer.valueOf(line);
                                    diff = cur - pre ;
                                    pre = cur ;

                                    unCompress += conversionModule.conversionT(diff);
                                    Log.error(String.valueOf(unCompress.length()));
                                    if(unCompress.length()==100){
                                        compressList = CompressModule.compress(unCompress);
                                        System.out.println("compressList"+ compressList);
                                        for(int i =0 ; i < compressList.size();i++)
                                        {

                                            compress += (String.valueOf(intToString(compressList.get(i),10)))+" ";

                                        }
                                        sendRecord(compress);
                                        unCompress = "";
                                        compress = "";
                                    }
                               /*     Log.error(String.valueOf(cur));
                                    sendRecord(String.valueOf(cur));*/
                                    Thread.sleep(50);

                                }






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
