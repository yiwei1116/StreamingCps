


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
    public static void writetoCache(String s)
    {



        try {

            BufferedWriter out = new BufferedWriter(new FileWriter("cacheTable.txt"));
            out.write(s);
            out.close();
            System.out.println("Generated cacheTable File::cacheTable.txt");
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
         //   inputStream = new FileInputStream("/home/steve02/StreamingCps/RealTimeData1");
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
/*        DiffList = subValue(radiationList);
        List<Integer>compressList = new ArrayList<>();
        compressList = compressModule.compress(conversionModule.conversionTable(DiffList));
        String encodingText = getString(compressList);
        System.out.println(encodingText);
        String decodingText = compressModule.decompress(compressList);
        compressModule.reConstruct(decodingText);*/
        //System.out.println(decodingText);
        String testText = "fifisdjfisdjsssdowkkowda";//fifisdjfisdjsss
        List<Integer> compressed = compressModule.compress(testText);
        System.out.println(compressed);
        String decompressed = compressModule.decompress(compressed);
        System.out.println(decompressed);
        Log.error("is before compress the same as after compress ?",String.valueOf(testText.equals(decompressed)));

    }}

