import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientSide {
    Socket serverSocket;

    DataInputStream inputStream;
    DataOutputStream outputStream;

    ClientSide(String ip, int port) throws Exception {
        serverSocket = new Socket(ip, port);
        inputStream = new DataInputStream(serverSocket.getInputStream());
        outputStream = new DataOutputStream(serverSocket.getOutputStream());
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    void startProccess() throws Exception {
        System.out.println("Successfully started the process");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\n\n1 to Send Message\n3 to refresh Chat\nE to Exit\n");

        while (true) {
            System.out.println();
            String choice = br.readLine();
            if (choice.equals("E")) break;

            switch (Integer.parseInt(choice)) {
                case 1:
                    outputStream.writeUTF("1");
                    System.out.print("IP: ");
                    outputStream.writeUTF(br.readLine());

                    Date currTime = new Date();
                    outputStream.writeUTF(dateFormat.format(currTime));

                    System.out.println("Message: ");
                    outputStream.writeUTF(br.readLine());

                    break;

                case 2:
                    outputStream.writeUTF("2");

                    for (int i = Integer.parseInt(inputStream.readUTF()); i > 0; --i) {
                        System.out.println("\n\nMAC ID: " + inputStream.readUTF());

                        System.out.println("\n\nReceived: ");
                        for (int j = Integer.parseInt(inputStream.readUTF()); j > 0; --j) {
                            System.out.println(inputStream.readUTF());
                        }

                        System.out.println("\n\nSent: ");
                        for (int j = Integer.parseInt(inputStream.readUTF()); j > 0; --j) {
                            System.out.println(inputStream.readUTF());
                        }
                    }

                    break;

                case 3:
                    outputStream.writeUTF("3");
                    System.out.print("IP: ");
                    outputStream.writeUTF(br.readLine());

                    System.out.println("\n\nReceived: ");
                    for (int i = Integer.parseInt(inputStream.readUTF()); i > 0; --i) {
                        System.out.println(inputStream.readUTF());
                    }

                    System.out.println("\n\nSent: ");
                    for (int i = Integer.parseInt(inputStream.readUTF()); i > 0; --i) {
                        System.out.println(inputStream.readUTF());
                    }

                    break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new ClientSide("192.168.43.225", 7777).startProccess();
    }
}