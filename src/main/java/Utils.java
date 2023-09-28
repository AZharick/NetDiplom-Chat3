import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
   static final String settingsFileName = "Settings.ini";
   static final String logFileName = "File.log";

   public static int getPortFromSettingsFile() throws IOException {
      File file = new File(settingsFileName);
      StringBuilder stringBuilder = new StringBuilder();
      try (FileReader fileReader = new FileReader(file)) {
         int c;
         while ((c=fileReader.read()) !=-1) {
            stringBuilder.append((char)c);
         }
      }
      return Integer.parseInt(String.valueOf(stringBuilder));
   }

   public static String getTime() {
      String datePattern = "[HH:mm:ss]";
      DateFormat d = new SimpleDateFormat(datePattern);
      Date today = Calendar.getInstance().getTime();
      String str = d.format(today);
      return str+" ";
   }

   public static void log(String messageToLog) throws IOException {
      File file = new File(logFileName);
      FileWriter fileWriter = new FileWriter(file, true);
      fileWriter.write(messageToLog+"\n");
      fileWriter.close();
   }
}
