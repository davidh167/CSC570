import java.util.LinkedList;

public class LinkedVectors implements Runnable{
    private static LinkedList<PadVector> stack;

    public LinkedVectors(){
    	stack = new LinkedList<>();
    }

    public static void pushVector(PadVector vector){
    	stack.push(vector);
    }

    public static PadVector popVector(){
        return stack.pop();
    }
    
    public static int size(){
        return stack.size();
    }

    @Override
    public String toString(){
        return stack.toString();
    }

    @Override
    public void run() {
    }
}