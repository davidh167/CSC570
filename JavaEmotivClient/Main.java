import java.net.URI;

/**
 * Main class to run the Emotiv WebSocket client.
 *
 *  @author javiersgs
 *  @version 0.1
 */
public class Main {

    public static void main(String[] args) throws Exception {
        EnhancedDelegate delegate = new EnhancedDelegate();
        URI uri = new URI("wss://localhost:6868");
        PADSocket ws = new PADSocket(uri, delegate);
        ws.connect();
    }
}
