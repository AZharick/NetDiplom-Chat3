import java.net.*;
import java.io.*;

class ClientLogic {
   private Socket socket;
   private BufferedReader in;
   private BufferedWriter out;
   private BufferedReader userInput;
   private String userName;

   public ClientLogic(String addr, int port) {
      try {
         this.socket = new Socket(addr, port);
      } catch (IOException e) {
         System.err.println("Socket creation failed");
      }
      try {
         userInput = new BufferedReader(new InputStreamReader(System.in));
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
         this.requestUserName();
         new ReadMsg().start();
         new WriteMsg().start();
      } catch (IOException e) {
         ClientLogic.this.closeSocket();
      }
   }

   void requestUserName() {
      System.out.print("Enter your name: ");
      try {
         userName = userInput.readLine();
         out.write("Hello, " + userName + "\n");
         Utils.log("Client: " + Utils.getTime() + " " + userName + " enters chat");
         out.flush();
      } catch (IOException ignored) {
      }
   }

   void closeSocket() {
      try {
         if (!socket.isClosed()) {
            Utils.log("Client: " + Utils.getTime() + " " + userName + " exits chat");
            socket.close();
            in.close();
            out.close();
         }
      } catch (IOException ignored) {
      }
   }

   private class ReadMsg extends Thread {
      @Override
      public void run() {
         String str;
         try {
            while (true) {
               str = in.readLine();
               if (str.equals("/exit")) {
                  ClientLogic.this.closeSocket();
                  break;
               }
               System.out.println(str);
            }
         } catch (IOException e) {
            ClientLogic.this.closeSocket();
         }
      }
   }

   public class WriteMsg extends Thread {
      @Override
      public void run() {
         while (true) {
            String userWord;
            try {
               userWord = userInput.readLine();
               if (userWord.equals("/exit")) {
                  out.write("/exit" + "\n");
                  ClientLogic.this.closeSocket();
                  break;
               } else {
                  out.write(Utils.getTime() + " " + userName + ": " + userWord + "\n");
                  Utils.log("Client: " + Utils.getTime() + userName + ": " + userWord);
               }
               out.flush();
            } catch (IOException e) {
               ClientLogic.this.closeSocket();
            }

         }
      }
   }

   public ClientLogic() throws IOException {
      int port = Utils.getPortFromSettingsFile();
      try {
         this.socket = new Socket("localhost", port);
      } catch (IOException e) {
         System.err.println("Socket creation failed");
      }
      try {
         userInput = new BufferedReader(new InputStreamReader(System.in));
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
         new ReadMsg().start();
         new WriteMsg().start();
      } catch (IOException e) {
         ClientLogic.this.closeSocket();
      }
   } // for tests

   public Socket getSocket() { //for test
      return socket;
   } // for tests
}//ClientLogic

public class Client {
   public static String ip = "localhost";
   public static int PORT;

   public static void main(String[] args) throws IOException {
      PORT = Utils.getPortFromSettingsFile();
      new ClientLogic(ip, PORT);
   }
}