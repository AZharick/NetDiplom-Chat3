import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UtilsTests {

   @Test
   public void getPortFromSettingsFileTest() throws IOException {
      int expected = 8080;
      int result = Utils.getPortFromSettingsFile();
      Assertions.assertEquals(expected, result);
   }

   @Test
   public void logTest() throws IOException {
      String wordToLog = "Test logging";
      Utils.log(wordToLog);
      File file = new File(Utils.logFileName);
      StringBuilder fileContents = new StringBuilder();
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
         int c;
         while ((c = reader.read()) != -1) {
            fileContents.append((char)c);
         }
         System.out.println(fileContents);
         Assertions.assertTrue(fileContents.toString().contains(wordToLog));
      }
   }

}