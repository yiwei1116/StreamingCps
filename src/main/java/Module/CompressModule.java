package Module;

import com.esotericsoftware.minlog.Log;
import ncku.streamCp.Exp;
import ncku.streamCp.LRUCache;

import java.io.Serializable;
import java.util.*;

/**
 * Created by yiwei on 2017/3/14.
 */

public class CompressModule implements Serializable{
    private static Map<String,Integer> encodeDictionary = new HashMap<String,Integer>();
    private static Map<Integer,String> encodeDictionaryRe = new HashMap<Integer,String  >();
    private static Map<Integer,String> decodeDictionary = new HashMap<Integer,String>();
    private static int dictionaryMaxSize = 8192;
    private static int keySize = 0  ;
    private static int valueSize = 0  ;
    private  static String lruTable="";

    private static LRUCache<Integer, String> lruCache = new LRUCache<Integer, String>(dictionaryMaxSize);
    public  static List<Integer> compress(String uncompressed) {
        // Build the decodeDictionary.



        Exp exp = new Exp();
        int dictSize = 52;
        int count = 0;


       /* for (int i = 0; i < 256; i++) {
            encodeDictionary.put("" + (char) i, i);

        }*/
        /**
         *  A~Z 0~25
         */
        for (int i = 65; i < 91; i++) {
            encodeDictionary.put("" + (char) i, (i-65));

        }

/**
 *  a~z 26~51
 */
        for (int i = 97; i < 123; i++) {
            encodeDictionary.put( "" + (char) i,  (i-71));

        }
        String dict="";



        String w = "";
        List<Integer> result = new ArrayList<Integer>();
        for (char c : uncompressed.toCharArray()) {
            String wc = w + c;
            if (encodeDictionary.containsKey(wc)) {
                w = wc;
                count++;
                if(encodeDictionary.get(wc)>51)
                    lruCache.put(encodeDictionary.get(wc)  , wc);


            }
            /**
             * remove decodeDictionary least frequency used word from LRU
             *
             */
            else if (encodeDictionary.size() > dictionaryMaxSize ){
                result.add(encodeDictionary.get(w));
                Integer leastFrequenceIndex = lruCache.getHead().getKey();
                String leastFrequenceCode = lruCache.getHead().getValue();
                //       Log.error("index",String.valueOf(leastFrequenceIndex));
                //       Log.error("code",leastFrequenceCode);
                encodeDictionary.remove(leastFrequenceCode,leastFrequenceIndex);
                //      Log.error("wc",wc);
                encodeDictionary.put(wc,leastFrequenceIndex);
                //lruCache.remove(leastFrequenceIndex,leastFrequenceCode);
                lruCache.put(encodeDictionary.get(wc)  , wc);
                w = "" + c;

            }
            else {

                result.add(encodeDictionary.get(w));
                // Add wc to the decodeDictionary.
                encodeDictionary.put(wc, dictSize);
                if( encodeDictionary.get(wc)>51) {
                    lruCache.put(encodeDictionary.get(wc)  , wc);
                    //            Log.error("encodeDictionaryKey",String.valueOf(encodeDictionary.get(wc)));
                    //            Log.error("encodeDictionaryValue",wc);
                }

                dict=dict+"["+"Key:"+dictSize+" , "+"Code:"+wc+"]"+"\n";
                dictSize++;
                w = "" + c;

            }
        }
        //System.out.println("Dictionay Table:");

        for (Map.Entry<String,Integer>entry : encodeDictionary.entrySet()){
            String key =entry.getKey();

            keySize += key.length();

            int value = entry.getValue();
            valueSize += String.valueOf(value).length();
            encodeDictionaryRe.put(value,key);

          /*  if(value > 255)
            System.out.print("["+"Key:"+key+" , "+"Code:"+value+"]"+"\n");*/

        }
        //Log.error("Size",String.valueOf(valueSize+keySize));
        // Output the code for w.
        if (!w.equals(""))
            result.add(encodeDictionary.get(w));

/**
 * LRU key 同 value 不同 取代value且移至尾
 *         同        同
 */


        for (Map.Entry<Integer, String> e : lruCache.getAll())
            lruTable=lruTable+"["+"Key:"+e.getKey()+" , "+"Code:"+e.getValue()+"]"+"\n";
        /*    Log.error("r",String.valueOf(lruCache.getHead().getValue()));
        exp.writeTo(lruTable,"cacheTable.txt");*/
        //exp.writeTo(dict,"Dictionary.txt");

   //     System.out.println("DicIndex:"+encodeDictionary.size());
     //   System.out.println("DicSize:"+(keySize+valueSize));
        Log.error("count",String.valueOf(count));

        Log.error("valueSize",String.valueOf(valueSize));
        System.out.println("Represent /Single code:"+(valueSize/encodeDictionary.size()));
        System.out.println("encodeDictionary :"+encodeDictionary);
        return result;
    }

    /** Decompress a list of output ks to a string. */
    public  String decompress(List<Integer> compressed) {
        // Build the decodeDictionary.

        LRUCache<Integer, String> lruCache = new LRUCache<Integer, String>(dictionaryMaxSize);
        String delruTable="";
        int dictSize = 52;

        /*for (int i = 0; i < 256; i++)
            decodeDictionary.put(i, "" + (char)i);*/
/**
 *  A~Z 0~25
 */
        for (int i = 65; i < 91; i++) {
            decodeDictionary.put((i-65), "" + (char) i);

        }

/**
 *  a~z 26~51
 */
        for (int i = 97; i < 123; i++) {
            decodeDictionary.put( (i-71), "" + (char) i);

        }
        String w = "" + decodeDictionary.get(compressed.remove(0));
        StringBuffer result = new StringBuffer(w);
        for (int k : compressed) {

            String entry;

            if (decodeDictionary.containsKey(k)) {
                /**
                 * entry = output
                 */
                entry = decodeDictionary.get(k);

          /*      Log.error("k",String.valueOf(k));
                Log.error("entrychar",String.valueOf(entry.charAt(0)));*/

            }
            else if (k == dictSize) {
                entry = w + w.charAt(0);
            }
            else
                throw new IllegalArgumentException("Bad compressed k: " + k);

            if(decodeDictionary.size()>dictionaryMaxSize){
                Integer leastFrequenceIndex = lruCache.getHead().getKey();
                String leastFrequenceCode = lruCache.getHead().getValue();
                //       Log.error("leastFrequenceIndex",String.valueOf(leastFrequenceIndex));
                //      Log.error("leastFrequenceCode",leastFrequenceCode);
                // Log.error("w",w);

                if(k==leastFrequenceIndex){
                    //  Log.error("replace",w+w.charAt(0));
                    lruCache.put(leastFrequenceIndex,w+w.charAt(0));
                    decodeDictionary.remove(leastFrequenceIndex,leastFrequenceCode);
                    decodeDictionary.put(leastFrequenceIndex,w+w.charAt(0));
                   /* for (Map.Entry<Integer, String> e : lruCache.getAll()){
                        if(decodeDictionary.get(k).contains(e.getValue()))
                            lruCache.put(e.getKey(),e.getValue());


                    }*/
                  /*  if( k > 255 )
                        lruCache.put(k, decodeDictionary.get(k));
*/

                    //     Log.error("w+w.charAt",w+w.charAt(0));
                    entry = decodeDictionary.get(k);
                }
                else {
                    //   Log.error("replace",w+entry.charAt(0));
                    lruCache.put(leastFrequenceIndex, w + entry.charAt(0));
                    decodeDictionary.remove(leastFrequenceIndex, leastFrequenceCode);
                    decodeDictionary.put(leastFrequenceIndex, w + entry.charAt(0));
                    /**
                     * hashmap 長度調整短字串先
                     */
                    Map<Integer,String> hashMap = new HashMap<>();

                    for (Map.Entry<Integer, String> e : lruCache.getAll()){
                        if(decodeDictionary.get(k).startsWith(e.getValue())) {
                            hashMap.put(e.getKey(),e.getValue());

                        /*    Log.error("kk", String.valueOf(e.getKey()));
                            Log.error("dd", String.valueOf(decodeDictionary.get(k)));
                            Log.error("ee", String.valueOf(e.getValue()));*/
                            //            Log.error("value",String.valueOf(e.getValue()));
                        }
                    }
                    List<Map.Entry> entryList = new ArrayList<>(hashMap.entrySet());
                    Comparator< Map.Entry> sortByValue = (e1,e2)->{
                        return ((String)e1.getValue()).compareTo( (String)e2.getValue());
                    };
                    Collections.sort(entryList, sortByValue );
                    for(Map.Entry e : entryList){

                        lruCache.put((Integer) e.getKey(), (String)e.getValue());


                    }

                    /*if( k > 255 )
                        lruCache.put(k, decodeDictionary.get(k));*/

                    //        Log.error("w+entry.charAt",w+entry.charAt(0));
                    entry = decodeDictionary.get(k);
                }

            }

            else{

                lruCache.put(dictSize,w +entry.charAt(0));
                //       Log.error("dictSize",String.valueOf(dictSize));
                //       Log.error("w +entry.charAt(0)",String.valueOf(w +entry.charAt(0)));
                decodeDictionary.put(dictSize, w + entry.charAt(0));
                Map<Integer,String> hashMap = new HashMap<>();
                for (Map.Entry<Integer, String> e : lruCache.getAll()){
                    if(decodeDictionary.get(k).startsWith(e.getValue())) {
                        hashMap.put(e.getKey(),e.getValue());

                 /*   Log.error("key",String.valueOf(e.getKey()));
                    Log.error("value",String.valueOf(e.getValue()));*/
                    }
                }
                List<Map.Entry> entryList = new ArrayList<>(hashMap.entrySet());
                Comparator< Map.Entry> sortByValue = (e1,e2)->{
                    return ((String)e1.getValue()).compareTo( (String)e2.getValue());
                };
                Collections.sort(entryList, sortByValue );
                for(Map.Entry e : entryList){

                    lruCache.put((Integer) e.getKey(), (String)e.getValue());


                }
         /* if( k > 255 )
                lruCache.put(k, decodeDictionary.get(k));*/

            }
            result.append(entry);

            dictSize++;

            w = entry;
        }
        for (Map.Entry<Integer, String> e : lruCache.getAll())
            delruTable=delruTable+"["+"Key:"+e.getKey()+" , "+"Code:"+e.getValue()+"]"+"\n";

        System.out.println("Decode Cache Table:");
        //      System.out.print(delruTable);


        System.out.println("********************************************************");
        System.out.println("Decode decodeDictionary Table:");
        for (Map.Entry<Integer,String>dic : decodeDictionary.entrySet()){
            Integer key =dic.getKey();
            String value = dic.getValue();

           /* if(key > 255)
                System.out.print("["+"Key:"+key+" , "+"Code:"+value+"]"+"\n");*/

        }

        Log.error("if encode cache equal to decode cache ?",String.valueOf(delruTable.equals(lruTable)));
      //  Log.error("if encode dictionary equal to decode dictionary ?",String.valueOf(isDictionarySame(encodeDictionaryRe,decodeDictionary)));
        return result.toString();
    }
    /** reconstruct to origin. */
    public  List<Integer> reConstruct(String decode){
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
    public static boolean isDictionarySame(Map<Integer,String>en,Map<Integer, String> de){

        return en.equals(de);



    }
    public static boolean getFlag(boolean flag){


        return flag;
    }
  //  public  static   LRUCache<Integer, String>

}
