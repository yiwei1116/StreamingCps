import com.esotericsoftware.minlog.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yiwei on 2017/3/14.
 */
public class CompressModule {

    public static List<Integer> compress(String uncompressed) {
        // Build the dictionary.
        LRUCache lruCache = new LRUCache(4097);
        Exp exp = new Exp();
        int dictSize = 256;
        Map<String,Integer> dictionary = new HashMap<String,Integer>();
        for (int i = 0; i < 256; i++) {
            dictionary.put("" + (char) i, i);

        }
        String dict="";

 /*      for(int u=32;u<=32;u++)
            dict=dict+"["+"Key:"+Character.toString((char)u)+" , "+"Code:"+u+"]"+"\n";
        for(int o=46;o<=46;o++)
            dict=dict+"["+"Key:"+Character.toString((char)o)+" , "+"Code:"+o+"]"+"\n";*/
        for(int h=97;h<=122;h++)
            dict=dict+"["+"Key:"+h+" , "+"Code:"+Character.toString((char)h)+"]"+"\n";
        for(int s=65;s<=90;s++)
            dict=dict+"["+"Key:"+s+" , "+"Code:"+Character.toString((char)s)+"]"+"\n";

        String w = "";
        List<Integer> result = new ArrayList<Integer>();
        for (char c : uncompressed.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc))
                w = wc;
            else {
                result.add(dictionary.get(w));
                // Add wc to the dictionary.
                dictionary.put(wc, dictSize++);
                lruCache.set(dictSize,wc);

                dict=dict+"["+"Key:"+dictSize+" , "+"Code:"+wc+"]"+"\n";
                w = "" + c;
            }
        }

        // Output the code for w.
        if (!w.equals(""))
            result.add(dictionary.get(w));
        Log.error("LRU",lruCache.get(257));
        exp.writeto(dict);
        return result;
    }

    /** Decompress a list of output ks to a string. */
    public static String decompress(List<Integer> compressed) {
        // Build the dictionary.
        int dictSize = 256;
        Map<Integer,String> dictionary = new HashMap<Integer,String>();
        for (int i = 0; i < 256; i++)
            dictionary.put(i, "" + (char)i);

        String w = "" + (char)(int)compressed.remove(0);
        StringBuffer result = new StringBuffer(w);
        for (int k : compressed) {
            String entry;
            if (dictionary.containsKey(k))
                entry = dictionary.get(k);
            else if (k == dictSize)
                entry = w + w.charAt(0);
            else
                throw new IllegalArgumentException("Bad compressed k: " + k);

            result.append(entry);

            // Add w+entry[0] to the dictionary.
            dictionary.put(dictSize++, w + entry.charAt(0));

            w = entry;
        }

        return result.toString();
    }
}
