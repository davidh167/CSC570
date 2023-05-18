import org.json.JSONArray;
import org.json.JSONObject;

public static class LinkedVectors implements Runnable{
    private LinkedList<PadVector> head;

    public LinkedVectors(){
        head = new LinkedList<>();
    }

    public void pushVector(PadVector vector){
        head.push(vector);
    }

    public void popVector(){
        head.pop();
    }

    @Override
    public String toString(){
        return head.toString();
    }

    @Override
    public void run() {
    }
}