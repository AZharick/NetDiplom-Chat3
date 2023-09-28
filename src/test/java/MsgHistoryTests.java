import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class MsgHistoryTests {
   MsgHistory ms = new MsgHistory();

   @Test
   public void addToHistoryTest() {
      ms.addToHistory("addition test");
      Assertions.assertTrue(ms.getStory().contains("addition test"));
   }

   @Test
   public void printPrevMsgsTest() throws IOException {
      StringWriter str = new StringWriter();
      BufferedWriter writer = new BufferedWriter(str);
      ms.addToHistory("TestWord");
      ms.printPrevMsgs(writer);
      String expected = "Previous messages:\nTestWord\n/....\n";
      String factResult = str.toString();
      Assertions.assertEquals(expected, factResult);
      writer.close();
   }

}