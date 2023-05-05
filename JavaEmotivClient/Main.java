import java.net.URI;

public class Main {

    public static void main(String[] args) throws Exception {
        EmotivDelegate delegate = new EmotivDelegate();
        URI uri = new URI("wss://localhost:6868");
        EmotivSocket ws = new EmotivSocket(uri, delegate);
        ws.connect();
    }
}
