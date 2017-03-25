import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yiwei on 2017/3/13.
 */
public class ConversionModule {

    public  String conversionTable(ArrayList<Integer> diffList){
        String tfNum="";
        Exp exp =new Exp();
        HashMap converseT = new HashMap<Integer,String>();
        converseT.put(1,"A");
        converseT.put(2,"B");
        converseT.put(3,"C");
        converseT.put(4,"D");
        converseT.put(5,"E");
        converseT.put(6,"F");
        converseT.put(7,"G");
        converseT.put(8,"H");
        converseT.put(9,"I");
        converseT.put(10,"J");
        converseT.put(11,"K");
        converseT.put(12,"L");
        converseT.put(13,"M");
        converseT.put(14,"N");
        converseT.put(15,"O");
        converseT.put(16,"P");
        converseT.put(17,"Q");
        converseT.put(18,"R");
        converseT.put(19,"S");
        converseT.put(20,"T");
        converseT.put(21,"U");
        converseT.put(22,"V");
        converseT.put(23,"W");
        converseT.put(24,"X");
        converseT.put(25,"Y");
        converseT.put(0,"Z");

        converseT.put(-1,"a");
        converseT.put(-2,"b");
        converseT.put(-3,"c");
        converseT.put(-4,"d");
        converseT.put(-5,"e");
        converseT.put(-6,"f");
        converseT.put(-7,"g");
        converseT.put(-8,"h");
        converseT.put(-9,"i");
        converseT.put(-10,"j");
        converseT.put(-11,"k");
        converseT.put(-12,"l");
        converseT.put(-13,"m");
        converseT.put(-14,"n");
        converseT.put(-15,"o");
        converseT.put(-16,"p");
        converseT.put(-17,"q");
        converseT.put(-18,"r");
        converseT.put(-19,"s");
        converseT.put(-20,"t");
        converseT.put(-21,"u");
        converseT.put(-22,"v");
        converseT.put(-23,"w");
        converseT.put(-24,"x");
        converseT.put(-25,"y");


        for(int i = 0 ; i < diffList.size() ; i++){

            tfNum += converseT.get( diffList.get(i));


        }
        exp.writeTo(tfNum,"Transfer.txt");





            return tfNum;

    }




}
