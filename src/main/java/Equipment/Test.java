package Equipment;

import java.io.IOException;
import java.util.List;

/**
 * Created by steve02 on 2017/6/14.
 */

public class Test {
    public static void main(String[] args) throws IOException,InterruptedException{
        int index = 12;
        double compressRatio=0 , spaceSaving=0, readTextBit=0,compressBit=0;
        String readText = LZW.readTextFile("/home/steve02/StreamingCps/radiation_level/16K.txt");
        String testText = "12321";//BAbABbAbAbABacCZA
        List<Integer> compressed = LZW.compress(readText);
        System.out.println(readText.length()*8);
        System.out.println(compressed.size()*index);
        readTextBit = readText.length()*8;
        compressBit =compressed.size()*index;
        compressRatio = (readTextBit/compressBit);

        System.out.println("Compress Ratio: "+Math.round(compressRatio*100.0)/100.0);//只取小數點後兩位
        spaceSaving = (1 - (1/compressRatio));
        System.out.println("Space Saving: "+Math.round(spaceSaving*100.0)/100.0);
        String decompressed = LZW.decompress(compressed);
        System.out.println(readText.equals(decompressed));
        //System.out.println(decompressed);





    }
}

