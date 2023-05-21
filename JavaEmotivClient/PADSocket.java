import java.net.URI;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PADSocket extends EmotivSocket{


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
            PadVector vector = new PadVector(temp2);
            System.out.println(vector);
            LinkedVectors.pushVector(vector);






        }
    }




}
