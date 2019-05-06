import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ClientDetails {
    String MAC_id;
    HashMap<String, ArrayList<String>> sentMessages, receivedMessages;

    ClientDetails(String mac) {
        MAC_id = mac;
        sentMessages = new HashMap<>();
        receivedMessages = new HashMap<>();
    }

    void sendMessage(String toMAC, String time, String message) {
        message = time + "\n" + message;
        sentMessages.putIfAbsent(toMAC, new ArrayList<>());
        sentMessages.get(toMAC).add(message);
    }

    void receiveMessage(String fromMAC, String time, String message) {
        message = time + "\n" + message;
        receivedMessages.putIfAbsent(fromMAC, new ArrayList<>());
        receivedMessages.get(fromMAC).add(message);
    }

    ArrayList<String> getSentMessages(String toMAC) {
        return sentMessages.getOrDefault(toMAC, new ArrayList<>());
    }

    ArrayList<String> getReceivedMessages(String fromMAC) {
        return receivedMessages.getOrDefault(fromMAC, new ArrayList<>());
    }

    HashSet<String> getAllMAC_IDs() {
        HashSet<String> allMACs = new HashSet<>();
        allMACs.addAll(receivedMessages.keySet());
        allMACs.addAll(sentMessages.keySet());
        return allMACs;
    }
}