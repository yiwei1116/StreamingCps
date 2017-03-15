


import com.esotericsoftware.minlog.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
    /*
    public static List<Integer> compress(String uncompressed) {
        // Build the dictionary.
        int dictSize = 256;
        Map<String,Integer> dictionary = new HashMap<String,Integer>();
        for (int i = 0; i < 256; i++) {
            dictionary.put("" + (char) i, i);

        }
        String dict="";

       for(int u=32;u<=32;u++)
            dict=dict+"["+"Key:"+Character.toString((char)u)+" , "+"Code:"+u+"]"+"\n";
        for(int o=46;o<=46;o++)
            dict=dict+"["+"Key:"+Character.toString((char)o)+" , "+"Code:"+o+"]"+"\n";
       for(int h=97;h<=122;h++)
            dict=dict+"["+"Key:"+Character.toString((char)h)+" , "+"Code:"+h+"]"+"\n";
        for(int s=65;s<=90;s++)
            dict=dict+"["+"Key:"+Character.toString((char)s)+" , "+"Code:"+s+"]"+"\n";

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
                dict=dict+"["+"Key:"+wc+" , "+"Code:"+dictSize+"]"+"\n";
                w = "" + c;
            }
        }

        // Output the code for w.
        if (!w.equals(""))
            result.add(dictionary.get(w));
        Exp.writeto(dict);
        return result;
    }*/

    /** Decompress a list of output ks to a string. */

    /*public static String decompress(List<Integer> compressed) {
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
    }*/
    public  static  String getString(List<Integer>Compress){


        for (int i = 0 ; i < Compress.size() ; i++){

            K = K + Compress.get(i).toString();


        }

        return K ;

    }
    public static void write(String s)
    {



        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("Compressed.txt"));
            out.write(s);
            out.close();
            System.out.println("Generated Compressed File::Compressed.txt");
        }
        catch (IOException e)
        {
            System.out.println("Exception ");
        }







    }
    public static void writeTransfer(String s)
    {



        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("Transfer.txt"));
            out.write(s);
            out.close();
            System.out.println("Generated Compressed File::Transfer.txt");
        }
        catch (IOException e)
        {
            System.out.println("Exception ");
        }







    }
    public static void writeto(String s)
    {



        try {

            BufferedWriter out = new BufferedWriter(new FileWriter("Dictionary.txt"));
            out.write(s);
            out.close();
            System.out.println("Generated Dictionary File::Dictionary.txt");
        }
        catch (IOException e)
        {

            System.out.println("Exception : "  + e.getMessage());
        }

    }
    public static void writein(String s)
    {



        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("Decompressed.txt"));
            out.write(s);
            out.close();
            System.out.println("Generated Decompressed File::Decompressed.txt");
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


    public static void main(String[] args) throws IOException{

        FileInputStream inputStream = null;
        Scanner scanner = null;
        ArrayList<Integer> DiffList = new ArrayList<Integer>();
        ArrayList<Integer> radiationList = new ArrayList<Integer>();
        try {
            inputStream = new FileInputStream("/home/yiwei/IdeaProjects/FPro/RealTimeData1");
           // inputStream = new FileInputStream("/home/steve02/StreamingCps/RealTimeData");
            scanner = new Scanner(inputStream, "UTF-8");
            int i = 0;

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine().substring(20,23);
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
        System.out.println(encodingText);
        String decodingText = compressModule.decompress(compressList);
        compressModule.reConstruct(decodingText);
        //System.out.println(decodingText);

        /*List<Integer> compressed = compress("av");
        System.out.println(compressed);
        System.out.println(getString(compressed).length());
        String decompressed = decompress(compressed);
        System.out.println(decompressed.length());
        System.out.println(decompressed);*/
    }}

