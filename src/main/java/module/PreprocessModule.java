package module;

import java.util.ArrayList;

/**
 * Created by steve02 on 2017/4/17.
 */
public class PreprocessModule {

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
            //Log.error("k",String.valueOf(preprocessList.get(i)));
        }
        return preprocessList;


    }
}
