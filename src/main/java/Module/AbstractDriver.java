package Module;

/**
 * Created by yiwei on 2017/5/31.
 */

import  java.io.BufferedReader;
import  java.io.File;
import  java.io.IOException;
import  java.io.InputStreamReader;
import java.util.ArrayList;
import  java.util.Enumeration;
import java.util.List;
import  java.util.zip.ZipEntry;
import  java.util.zip.ZipFile;

import com.esotericsoftware.minlog.Log;
import  org.apache.log4j.LogManager;
import  org.apache.log4j.Logger;

import static Module.PreprocessModule.intToString;
import static ncku.streamCp.Exp.conversionModule;

public abstract class AbstractDriver {
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
            File dirPath = new File(path);
            if (dirPath.isDirectory()) {
                File[] files = new File(path).listFiles();
                for (File f : files) {
                    LOG.info(String.format("Feeding zipped file %s", f.getName()));
                    ZipFile zFile = null ;
                    try {
                        zFile = new ZipFile(f);
                        Enumeration<? extends ZipEntry> zEntries = zFile.entries();
                        while (zEntries.hasMoreElements()) {
                            ZipEntry zEntry = zEntries.nextElement();
                            LOG.info(String.format("Feeding file %s", zEntry.
                                    getName()));
                            try (BufferedReader br = new BufferedReader(
                                    new InputStreamReader(zFile.
                                            getInputStream(zEntry)))) {// skip header
                                br.readLine();
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


                                 //   sendRecord(line);
                                }
                            }
                        }
                    } catch (IOException e) {
                        LOG.error(e.getMessage());
                    } finally {
                        if (zFile != null ) {
                            try {
                                zFile.close();
                            } catch (IOException e) {
                                LOG.error(e.getMessage());
                            }
                        }
                    }
                }
            } else {
                LOG.error(String.format("Path %s is not a directory", path));
            }
        } finally {
            close();
        }
    }
    }