import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yiwei on 2017/3/13.
 */
public class ConversionModule {

    public  String conversionTable(ArrayList<Integer> diffList){
        String tfNum="";
        Exp exp =new Exp();
        HashMap coverseT = new HashMap<Integer,String>();
        coverseT.put(1,"A");
        coverseT.put(2,"B");
        coverseT.put(3,"C");
        coverseT.put(4,"D");
        coverseT.put(5,"E");
        coverseT.put(6,"F");
        coverseT.put(7,"G");
        coverseT.put(8,"H");
        coverseT.put(9,"I");
        coverseT.put(10,"J");
        coverseT.put(11,"K");
        coverseT.put(12,"L");
        coverseT.put(13,"M");
        coverseT.put(14,"N");
        coverseT.put(15,"O");
        coverseT.put(16,"P");
        coverseT.put(17,"Q");
        coverseT.put(18,"R");
        coverseT.put(19,"S");
        coverseT.put(20,"T");
        coverseT.put(21,"U");
        coverseT.put(22,"V");
        coverseT.put(23,"W");
        coverseT.put(24,"X");
        coverseT.put(25,"Y");
        coverseT.put(0,"Z");

        coverseT.put(-1,"a");
        coverseT.put(-2,"b");
        coverseT.put(-3,"c");
        coverseT.put(-4,"d");
        coverseT.put(-5,"e");
        coverseT.put(-6,"f");
        coverseT.put(-7,"g");
        coverseT.put(-8,"h");
        coverseT.put(-9,"i");
        coverseT.put(-10,"j");
        coverseT.put(-11,"k");
        coverseT.put(-12,"l");
        coverseT.put(-13,"m");
        coverseT.put(-14,"n");
        coverseT.put(-15,"o");
        coverseT.put(-16,"p");
        coverseT.put(-17,"q");
        coverseT.put(-18,"r");
        coverseT.put(-19,"s");
        coverseT.put(-20,"t");
        coverseT.put(-21,"u");
        coverseT.put(-22,"v");
        coverseT.put(-23,"w");
        coverseT.put(-24,"x");
        coverseT.put(-25,"y");


        for(int i = 0 ; i < diffList.size() ; i++){

            tfNum += coverseT.get( diffList.get(i));


        }
        exp.writeTransfer(tfNum);





            return tfNum;

    }




}
