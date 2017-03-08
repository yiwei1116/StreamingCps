import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static String K="";
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
    public static List<Integer> compress(String uncompressed) {
        // Build the dictionary.
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
        Test.writeto(dict);
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
            System.out.println("Exception ");
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


    public static void main(String[] args) throws IOException{




        List<Integer> compressed = compress("av");
        System.out.println(compressed);
        System.out.println(getString(compressed).length());
        String decompressed = decompress(compressed);
        System.out.println(decompressed.length());
        System.out.println(decompressed);
    }
}
