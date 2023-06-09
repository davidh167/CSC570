import javax.net.ssl.SSLContext;
import java.net.URI;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URI;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

//package com.mkyong.io.csv.opencsv;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PADSocket extends EmotivSocket{

    private int messageCount = 0;
    List<String[]> temp  = new ArrayList<>();
    String fileSuffix = String.valueOf(LocalDateTime.now()).replaceAll(":", "-").replaceAll("\\.", "_");


    public PADSocket(URI serverURI, EnhancedDelegate delegate) throws Exception {
        super(serverURI, delegate);
    }


    @Override
    public void onMessage(String message) {

        System.out.println("Received message from Emotiv server.");
        if (!delegate.isSubscribed()) {
            JSONObject response = new JSONObject(message);
            int id = response.getInt("id");
            System.out.println(response);
            Object result = response.get("result");
            delegate.handle (id, result, this);
        } else {
            BigDecimal time = new JSONObject(message).getBigDecimal("time");
            JSONObject object = new JSONObject(message);
//            System.out.println(object);
//            System.out.println(time);
            JSONArray array = null;
            if ((object.keySet()).contains("fac")) {
                array = object.getJSONArray("fac");
            } else if ((object.keySet()).contains("dev")) {
                array = object.getJSONArray("dev");
            } else if ((object.keySet()).contains("met")) {
                array = object.getJSONArray("met");
            }
            // if fac refers to facial expression, and met refers to mental state, what does dev refer to?
            System.out.println(time + " :: " + array);

            ArrayList<String> temp2 = new ArrayList<>();
            time = time.setScale(8);
            temp2.add(time.toString());
            for(int i=0;i< array.length() ;i++){
//                System.out.println(array.get(i));
                if(array.get(i) instanceof Boolean) {
                    if ((Boolean) array.get(i)) {
                        temp2.add("true");
                    } else {
                        temp2.add("false");
                    }
                }else if(array.get(i) instanceof BigDecimal){
                    BigDecimal bd = (BigDecimal) array.get(i);
                    temp2.add(bd.toString());
                }else {

                    temp2.add((String) array.get(i));
                }
            }

            // Formatted Vector ^

            // Calculate PAD value in separate class
            System.out.println(temp2);
            PadVector vector = new PadVector(temp2);
            System.out.println(vector);
            LinkedVectors.pushVector(vector);


            // CSV writer code
//            messageCount++;
//            if(messageCount == 7){
                String[] list = new String[temp2.size()];
                for(int i = 0; i < temp2.size(); i++){
                    list[i] = temp2.get(i);
                }

//                temp.add(list);

                try{
                    File file = new File("./data/subject_" + fileSuffix + ".csv");
                    FileWriter outputfile = new FileWriter(file, true);
                    CSVWriter writer = new CSVWriter(outputfile);
//                    writer.writeAll(temp);
                    writer.writeNext(list);
                    writer.close();
                    System.out.println("Wrote a line");

                }catch (Exception e){
                    e.printStackTrace();
                }
                // Write to file
//            }else if(messageCount < 7) {
//                // simply keep adding to array
//                String[] list = new String[temp2.size()];
//                for (int i = 0; i < temp2.size(); i++) {
//                    list[i] = temp2.get(i);
//                }
//                temp.add(list);
//            }
            // End csv writer code






        }
    }




}
