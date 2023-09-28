import java.io.*;
import java.net.*;
import java.util.LinkedList;

class ServerLogic extends Thread {
   private Socket socket;
   private BufferedReader in;
   private BufferedWriter out;
   private StringWriter stringWriter;

   public ServerLogic(Socket socket) throws IOException {
      this.socket = socket;
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      Server.history.printPrevMsgs(out);
      start();
   }

   @Override
   public void run() {
      String word;
      try {
         word = in.readLine(); //userName here
         try {
            out.write(word + "\n");
            out.flush();
         } catch (IOException ignored) {
         }

         try {
            while (true) {
               word = in.readLine();
               if (word.equals("/exit")) {
                  this.downService();
                  break;
               }
               System.out.println("Echoing: " + word);
               Server.history.addToHistory(word);
               for (ServerLogic vr : Server.serverList) {
                  vr.send(word);
               }
            }
         } catch (NullPointerException ignored) {
         }
      } catch (IOException e) {
         this.downService();
      }
   }

   void send(String msg) {
      try {
         out.write(msg + "\n");
         out.flush();
      } catch (IOException ignored) {
      }
   }

   void downService() {
      try {
         if (!socket.isClosed()) {
            socket.close();
            in.close();
            out.close();
            for (ServerLogic vr : Server.serverList) {
               if (vr.equals(this)) vr.interrupt();
               Server.serverList.remove(this);
            }
         }
      } catch (IOException ignored) {
      }
   }

   public Socket getSocket() { // for tests
      return socket;
   }
   public ServerLogic() throws IOException { // for tests
      this.socket = new Socket("localhost", Utils.getPortFromSettingsFile());
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      stringWriter = new StringWriter();
      out = new BufferedWriter(stringWriter);
      start();
   }
   public StringWriter getStringWriter() { // for tests
      return stringWriter;
   } // for tests
}

class MsgHistory {
   private LinkedList<String> history = new LinkedList<>();

   public void addToHistory(String str) {
      if (history.size() >= 10) {
         history.removeFirst();
         history.add(str);
      } else {
         history.add(str);
      }
   }

   public void printPrevMsgs(BufferedWriter writer) {
      if (history.size() > 0) {
         try {
            writer.write("Previous messages:" + "\n");
            for (String vr : history) {
               writer.write(vr + "\n");
            }
            writer.write("/...." + "\n");
            writer.flush();
         } catch (IOException ignored) {
         }
      }
   }

   public LinkedList<String> getStory() { // for tests
      return history;
   }
}//MsgHistory

public class Server {
   public static int PORT;
   public static LinkedList<ServerLogic> serverList = new LinkedList<>();
   public static MsgHistory history;

   public static void main(String[] args) throws IOException {
      PORT = Utils.getPortFromSettingsFile();
      history = new MsgHistory();

      ServerSocket server = new ServerSocket(PORT);
      System.out.println(Utils.getTime() + "Server started");
      Utils.log("\nServer: " + Utils.getTime() + "Server started!");
      try {
         while (true) {
            Socket socket = server.accept();
            Utils.log("Server: " + Utils.getTime() + socket.getInetAddress().getHostAddress() + " connected!");
            try {
               serverList.add(new ServerLogic(socket));
            } catch (IOException e) {
               socket.close();
            }
         }
      } finally {
         server.close();
      }
   }
}