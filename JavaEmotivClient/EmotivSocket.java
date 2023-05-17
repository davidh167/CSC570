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



/**
 * EmotivSocket is a WebSocket client that connects to the Emotiv server.
 * It is used to send requests to the Emotiv server and receive responses.
 *
 *  Forked from CSC 570 git repo by javiersgs
 *  @author javiersgs
 *  @author dh435
 *  @version 0.1
 */

public class EmotivSocket extends WebSocketClient {

//    private int messageCount = 0;
//    List<String[]> temp  = new ArrayList<>();
EnhancedDelegate delegate;

    private static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
    };

    public EmotivSocket(URI serverURI, EnhancedDelegate delegate) throws Exception {
        super(serverURI);
        this.delegate = delegate;
        // Disable SSL certificate validation to allow self-signed certificates
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        // Connect to Emotiv server using secure WebSocket protocol
        setSocket(sc.getSocketFactory().createSocket());
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to Emotiv server: " + getURI());
        delegate.handle (0, null, this);
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



//            ArrayList<String> temp2 = new ArrayList<>();
//            time = time.setScale(8);
//            temp2.add(time.toString());
//            for(int i=0;i< array.length() ;i++){
////                System.out.println(array.get(i));
//                if(array.get(i) instanceof Boolean) {
//                    if ((Boolean) array.get(i)) {
//                        temp2.add("true");
//                    } else {
//                        temp2.add("false");
//                    }
//                }else if(array.get(i) instanceof BigDecimal){
//                    BigDecimal bd = (BigDecimal) array.get(i);
//                    temp2.add(bd.toString());
//                }else {
//
//                    temp2.add((String) array.get(i));
//                }
//            }



//            // CSV writer code
//            messageCount++;
//            if(messageCount == 7){
//                String[] list = new String[temp2.size()];
//                for(int i = 0; i < temp2.size(); i++){
//                    list[i] = temp2.get(i);
//                }
//
//                temp.add(list);
//
//                try{
//                    File file = new File("./eeg.csv");
//                    FileWriter outputfile = new FileWriter(file);
//                    CSVWriter writer = new CSVWriter(outputfile);
//                    writer.writeAll(temp);
//                    writer.close();
//                    System.out.println("WRITTEN");
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                // Write to file
//            }else if(messageCount < 7) {
//                // simply keep adding to array
//                String[] list = new String[temp2.size()];
//                for (int i = 0; i < temp2.size(); i++) {
//                    list[i] = temp2.get(i);
//                }
//                temp.add(list);
//            }
//            // End csv writer code


        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed with code " + code + " and reason " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error: " + ex);
    }

}
