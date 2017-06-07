package Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiwei on 2017/4/17.
 */
public class PreprocessModule {

    public  static ArrayList subValue(ArrayList<Integer> data){
        int p,q,k ;
        int base = 200;
        ArrayList<Integer> preprocessList = new ArrayList<Integer>();
        preprocessList.add(data.get(0)-base);
        for (int i =0 ; i< data.size()-1 ;i++) {
            p = data.get(i);
            q = data.get(i+1);
            k = q - p;

            preprocessList.add(k);
            //Log.error("k",String.valueOf(preprocessList.get(i)));
        }
        return preprocessList;


    }
    /**
     *
     * @param number
     * @param groupSize  number lengrh
     * @return 12bit
     */
    public static String intToString(int number, int groupSize) {
        StringBuilder result = new StringBuilder();

        for(int i = groupSize-1; i >= 0 ; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "1" : "0");

            if (i % groupSize == 0)
                result.append(" ");
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }
    /**
     *
     * @param Compress
     * @return    encode number bits
     */
    public  static  int getEncodeLength(List<Integer> Compress, int indexNum){

        int  encodeLength=0;
        for (int i = 0 ; i < Compress.size() ; i++){

            encodeLength +=intToString(Compress.get(i),indexNum).length();


        }

        return encodeLength ;

    }
}
