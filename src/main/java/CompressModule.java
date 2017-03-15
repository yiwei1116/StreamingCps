import com.esotericsoftware.minlog.Log;
import scala.tools.cmd.gen.AnyVals;

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
        LRUCache<Integer, String> lruCache = new LRUCache<Integer, String>(4096);
        Exp exp = new Exp();
        int dictSize = 256;
        int j = 0;
        Map<String,Integer> dictionary = new HashMap<String,Integer>();
        for (int i = 0; i < 256; i++) {
            dictionary.put("" + (char) i, i);

        }
        String dict="";
        String lruTable="";

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
            if (dictionary.containsKey(wc)) {
                w = wc;
            if(dictionary.get(wc)>255)
                lruCache.put(dictionary.get(wc)  , wc);


            }
            /**
             * remove dictionary least frequency used word from LRU
             *
             */
            /*else if (dictionary.size() > 280 ){

                Integer leastFrequenceIndex = lruCache.getHead().getKey();
                String leastFrequenceCode = lruCache.getHead().getValue();
                dictionary.remove(leastFrequenceCode,leastFrequenceIndex);
                dictionary.put(wc,leastFrequenceIndex);
                lruCache.remove(leastFrequenceIndex,leastFrequenceCode);


            }*/


            else {

                result.add(dictionary.get(w));
                // Add wc to the dictionary.
                dictionary.put(wc, dictSize);
                if( dictionary.get(wc)>=256) {
                    lruCache.put(dictionary.get(wc)  , wc);
                }

                dict=dict+"["+"Key:"+dictSize+" , "+"Code:"+wc+"]"+"\n";
                dictSize++;
                w = "" + c;

            }
        }

        for (Map.Entry<String,Integer>entry : dictionary.entrySet()){
            String key =entry.getKey();
            Integer value = entry.getValue();

            if(value > 255)
            System.out.print("["+"Key:"+key+" , "+"Code:"+value+"]"+"\n");

        }

        // Output the code for w.
        if (!w.equals(""))
            result.add(dictionary.get(w));

/**
 * LRU key 同 value 不同 取代value且移至尾
 *         同        同
 */

        for (Map.Entry<Integer, String> e : lruCache.getAll())
            lruTable=lruTable+"["+"Key:"+e.getKey()+" , "+"Code:"+e.getValue()+"]"+"\n";
            Log.error("r",String.valueOf(lruCache.getHead().getValue()));
       // lruTable=lruTable+"["+"Key:"++" , "+"Code:"+e.getValue()+"]"+"\n";
        exp.writetoCache(lruTable);
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
    public static List<Integer> reConstruct(String decode){
        int base = 200;
        int diff ;
        List<Integer> reNum = new ArrayList<Integer>();
        HashMap reConverse = new HashMap<String,Integer>();
        reConverse.put("A",1);
        reConverse.put("B",2);
        reConverse.put("C",3);
        reConverse.put("D",4);
        reConverse.put("E",5);
        reConverse.put("F",6);
        reConverse.put("G",7);
        reConverse.put("H",8);
        reConverse.put("I",9);
        reConverse.put("J",10);
        reConverse.put("K",11);
        reConverse.put("L",12);
        reConverse.put("M",13);
        reConverse.put("N",14);
        reConverse.put("O",15);
        reConverse.put("P",16);
        reConverse.put("Q",17);
        reConverse.put("R",18);
        reConverse.put("S",19);
        reConverse.put("T",20);
        reConverse.put("U",21);
        reConverse.put("V",22);
        reConverse.put("W",23);
        reConverse.put("X",24);
        reConverse.put("Y",25);
        reConverse.put("Z",0);



        reConverse.put("a",-1);
        reConverse.put("b",-2);
        reConverse.put("c",-3);
        reConverse.put("d",-4);
        reConverse.put("e",-5);
        reConverse.put("f",-6);
        reConverse.put("g",-7);
        reConverse.put("h",-8);
        reConverse.put("i",-9);
        reConverse.put("j",-10);
        reConverse.put("k",-11);
        reConverse.put("l",-12);
        reConverse.put("m",-13);
        reConverse.put("n",-14);
        reConverse.put("o",-15);
        reConverse.put("p",-16);
        reConverse.put("q",-17);
        reConverse.put("r",-18);
        reConverse.put("s",-19);
        reConverse.put("t",-20);
        reConverse.put("u",-21);
        reConverse.put("v",-22);
        reConverse.put("w",-23);
        reConverse.put("x",-24);
        reConverse.put("y",-25);
        for (int i = 0 ; i < decode.length(); i++){

            diff =  (Integer)(reConverse.get(String.valueOf(decode.charAt(i))));
            base += diff;
            reNum.add(base);
          //  Log.error("renum",String.valueOf(reNum.get(i)));
        }

        return reNum;
    }
}
