import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerLogicTests {
   static final int PORT;

   static {
      try {
         PORT = Utils.getPortFromSettingsFile();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   static ServerLogic serverLogic;
   static Socket socket;

   @BeforeAll
   public static void createResources() throws IOException {
      ServerSocket serverSocket = new ServerSocket(PORT);
      ClientLogic clientLogic = new ClientLogic();
      socket = serverSocket.accept();
      serverLogic = new ServerLogic();
   }

   @Test
   public void downServiceTest() {
      serverLogic.downService();
      Assertions.assertTrue(serverLogic.getSocket().isClosed());
   }

   @Test
   public void sendTest() {
      String expected = "Java\n";
      serverLogic.send("Java");
      String factResult = serverLogic.getStringWriter().toString();
      System.out.println(factResult);

      Assertions.assertEquals(expected, factResult);
   }

}