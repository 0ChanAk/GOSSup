import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server extends Thread {
    static final HashMap<String, ClientDetails> DATABASE = new HashMap<>();

    Socket clientSocket;
    String MAC_id;
    ClientDetails clientDetails;

    DataInputStream inputStream;
    DataOutputStream outputStream;

    Server(Socket client) throws Exception {
        clientSocket = client;
        MAC_id = getMAC_id(clientSocket.getRemoteSocketAddress().toString());

        DATABASE.putIfAbsent(MAC_id, new ClientDetails(MAC_id));
        clientDetails = DATABASE.get(MAC_id);

        inputStream = new DataInputStream(client.getInputStream());
        outputStream = new DataOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (clientSocket.isConnected()) {
                int choice = Integer.parseInt(inputStream.readUTF().trim());

                switch (choice) {
                    case 1: sendMessage(); break;
                    case 2: refreshAllChatHistory(); break;
                    case 3: refreshPersonalChatHistory(); break;
                    //case 4: obtainAllConnectedIPs(); break;
                    //case 5: obtainAllRegisteredIPs(); break;
                    //case 6: getMAC_id(); break;
                    //case 7: getIP(); break;
                    //case 8: broadcastMessage(); break;
                    default: System.out.println("Wrong Choice !!!");
                }
            }
            System.out.println("Device with MAC " + MAC_id + " has disconnected.");

            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (Exception e) {
        }
    }

    void sendMessage() throws Exception {
        String destMAC = getMAC_id(inputStream.readUTF());
        String timeStamp = inputStream.readUTF();
        String message = inputStream.readUTF();

        clientDetails.sendMessage(destMAC, timeStamp, message);

        DATABASE.putIfAbsent(destMAC, new ClientDetails(destMAC));
        DATABASE.get(destMAC).receiveMessage(MAC_id, timeStamp, message);
    }

    void refreshPersonalChatHistory() throws Exception {
        String MAC = getMAC_id(inputStream.readUTF());

        ArrayList<String> receivedMsgs = clientDetails.getReceivedMessages(MAC);
        outputStream.writeUTF(receivedMsgs.size() + "");
        for (String itr : receivedMsgs) outputStream.writeUTF(itr);

        ArrayList<String> sentMsgs = clientDetails.getSentMessages(MAC);
        outputStream.writeUTF(sentMsgs.size() + "");
        for (String itr : sentMsgs) outputStream.writeUTF(itr);
    }

    void refreshAllChatHistory() throws Exception {
        HashSet<String> allMACs = clientDetails.getAllMAC_IDs();
        outputStream.writeUTF(allMACs.size() + "");

        for (String mac : allMACs) {
            outputStream.writeUTF(mac);

            ArrayList<String> receivedMsgs = clientDetails.getReceivedMessages(mac);
            outputStream.writeUTF(receivedMsgs.size() + "");
            for (String itr : receivedMsgs) outputStream.writeUTF(itr);

            ArrayList<String> sentMsgs = clientDetails.getSentMessages(mac);
            outputStream.writeUTF(sentMsgs.size() + "");
            for (String itr : sentMsgs) outputStream.writeUTF(itr);
        }
    }


    final static String getIP(String mac_id) throws Exception {
        return mac_id;
    }

    final static String getMAC_id(String ip) throws Exception {
        return ip;
    }
}