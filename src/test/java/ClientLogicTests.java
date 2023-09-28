import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;

public class ClientLogicTests {
   static ClientLogic clientLogic;
   static final int PORT;

   static {
      try {
         PORT = Utils.getPortFromSettingsFile();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   @BeforeAll
   public static void createResources() throws IOException {
      ServerSocket serverSocket = new ServerSocket(PORT);
      clientLogic = new ClientLogic();
      serverSocket.accept();
   }

   @Test
   public void closeSocketTest() {
      clientLogic.closeSocket();
      Assertions.assertTrue(clientLogic.getSocket().isClosed());
   }

}