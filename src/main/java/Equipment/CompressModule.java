package Equipment;

import com.esotericsoftware.minlog.Log;
import ncku.streamCp.Exp;
import ncku.streamCp.LRUCache;

import java.io.*;
import java.util.*;

/**
 * Created by yiwei on 2017/3/14.
 */

public class CompressModule implements Serializable{
    private static HashMap<String,Integer> encodeDictionary = new HashMap<String,Integer>();
    private static HashMap<Integer,String> encodeDictionaryRe = new HashMap<Integer,String  >();
    private static HashMap<Integer,String> decodeDictionary = new HashMap<Integer,String>();
    private static int dictionaryMaxSize = 1024;
    private  static String lruTable="";
    private static LRUCache<Integer, String> lruCache = new LRUCache<Integer, String>(dictionaryMaxSize);


    /**
     *              index 0 -> uncompress string
     *                    1 -> w
     *                    2 -> dicIndex
     *                    3 -> compress : int

     * @return
     */
    public  static List<String> compress(List<String> result) {




        int dictSize = 52;


       if(encodeDictionary.size()==0){
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
       }

        String dict="";


            String w = result.get(1);
            String c = result.get(0);
            String wc = w + c;
            Log.error("s",wc);
            if (encodeDictionary.containsKey(wc)) {
                w = wc;
                if(encodeDictionary.get(wc)>51)
                    lruCache.put(encodeDictionary.get(wc)  , wc);


            }
            /**
             * remove decodeDictionary least frequency used word from LRU
             *
             */
            else if (encodeDictionary.size() > dictionaryMaxSize ){
                result.set(3,String.valueOf(encodeDictionary.get(w)));
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

                result.set(3,String.valueOf(encodeDictionary.get(w)));

                System.out.println((Integer.valueOf(result.get(2))));
                encodeDictionary.put(wc, (Integer.valueOf(result.get(2))));
                if( encodeDictionary.get(wc)>51) {
                    lruCache.put(encodeDictionary.get(wc)  , wc);
                }

                dict=dict+"["+"Key:"+dictSize+" , "+"Code:"+wc+"]"+"\n";
                dictSize++;

                w = "" + c;

            }


        /***
         *
         *  no input get dictionary key
         */
      /*  if (!w.equals(""))
            result.add(encodeDictionary.get(w));*/

/**
 * LRU key 同 value 不同 取代value且移至尾
 *         同        同
 */



        System.out.println("DicIndex:"+encodeDictionary.size());
       // System.out.println("DicIndex:"+(keySize+valueSize));
        //storeHashMap(encodeDictionary);

        result.set(1,w);
        result.set(2,String.valueOf(encodeDictionary.size()));

        return result;
    }


    /**
     * -----------------------------------------------------------------------------------------
     */




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


                if(k==leastFrequenceIndex){
                    lruCache.put(leastFrequenceIndex,w+w.charAt(0));
                    decodeDictionary.remove(leastFrequenceIndex,leastFrequenceCode);
                    decodeDictionary.put(leastFrequenceIndex,w+w.charAt(0));

                    entry = decodeDictionary.get(k);
                }
                else {
                    lruCache.put(leastFrequenceIndex, w + entry.charAt(0));
                    decodeDictionary.remove(leastFrequenceIndex, leastFrequenceCode);
                    decodeDictionary.put(leastFrequenceIndex, w + entry.charAt(0));

                    Map<Integer,String> hashMap = new HashMap<>();

                    for (Map.Entry<Integer, String> e : lruCache.getAll()){
                        if(decodeDictionary.get(k).startsWith(e.getValue())) {
                            hashMap.put(e.getKey(),e.getValue());


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


                    entry = decodeDictionary.get(k);
                }

            }

            else{

                lruCache.put(dictSize,w +entry.charAt(0));

                decodeDictionary.put(dictSize, w + entry.charAt(0));
                Map<Integer,String> hashMap = new HashMap<>();
                for (Map.Entry<Integer, String> e : lruCache.getAll()){
                    if(decodeDictionary.get(k).startsWith(e.getValue())) {
                        hashMap.put(e.getKey(),e.getValue());


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
        Log.error("if encode dictionary equal to decode dictionary ?",String.valueOf(isDictionarySame(encodeDictionaryRe,decodeDictionary)));
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
}

