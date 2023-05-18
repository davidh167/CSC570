import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class PadVector implements Runnable{

    public ArrayList<Double> pad;

    public PadVector(ArrayList<String> array) {
//        System.out.println("PAD Vector Init: " + array);

        // Summation array:
        // eng: 1, exc: 3, str: 6, rel: 8, int: 10, foc: 12

//        System.out.println(array);

        ArrayList<ArrayList<Double>> attrs = new ArrayList<>();
        Double emot = Double.parseDouble(array.get(2));
        attrs.add( new ArrayList<>( Arrays.asList(emot, emot, -1.0 * emot)));
        emot = Double.parseDouble(array.get(4));
        attrs.add(new ArrayList<>( Arrays.asList(emot, emot, emot)));
        emot = Double.parseDouble(array.get(7));
        attrs.add(new ArrayList<>(Arrays.asList(emot, -1.0 * emot, emot)));
        emot = Double.parseDouble(array.get(9));
        attrs.add(new ArrayList<>(Arrays.asList(-1.0 * emot, emot, -1.0 *emot)));
        emot = Double.parseDouble(array.get(11));
        attrs.add(new ArrayList<>(Arrays.asList(emot, -1.0 * emot, emot)));
        emot = Double.parseDouble(array.get(13));
        attrs.add(new ArrayList<>(Arrays.asList(emot, emot, -1.0 * emot)));


        System.out.println(attrs);


        ArrayList<Double> summation = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0));
        for (int i = 0; i < attrs.size(); i++){
            summation.set(0, summation.get(0) + attrs.get(i).get(0));
            summation.set(1, summation.get(1) + attrs.get(i).get(1));
            summation.set(2, summation.get(2) + attrs.get(i).get(2));
        }



//        this.pad = normalize(summation);
        this.pad = normalize(summation);
    }



    @Override
    public String toString(){
        return pad.toString();
    }

    public static ArrayList<Double> normalize(ArrayList<Double> vector){
        double stdDev = stdDev(vector);
//        System.out.println(stdDev);
        double mean = mean(vector);
//        System.out.println(mean);
        double norm = 0.0;

        for(int i = 0; i < vector.size(); i++){
            norm = (vector.get(i) - mean) / stdDev;
            vector.set(i, norm);
        }

        return vector;

    }

    public static double stdDev(ArrayList<Double> numArray){
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();

        for(Double num : numArray) {
            sum += num;
        }

        double mean = sum/length;
        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

    public static double mean(ArrayList<Double> numArray){
        double sum = 0.0;

        for(Double d : numArray){
            sum += d;
        }

        return sum/(1.0 * numArray.size());
    }


    @Override
    public void run() {

    }



}
