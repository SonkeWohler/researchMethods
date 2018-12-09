package dataStuff;

import java.io.IOException;
import cassandra.Connector;
import text.Text;

public class Reader {

  private Connector cassandra;
  private Text text;

  // constructor and set up
  // ********************************

  public Reader() {
    this.cassandra = new Connector();
    this.text = new Text();

    this.text.createReadingFile();
    this.text.openKeyFile();

    this.run();

    this.terminate();
    System.exit(0);
  }

  // running
  // *******************************************************

  public void run() {
    String line = "";
    String[] stringKeys = new String[2];
    int[] intKeys = new int[2];
    double time = 0.0;
    for (int i = 0; i < 1000000; i++) {
      // read the next key pair
      try {
        line = this.text.readNextKey();
        // catch end of file exception
      } catch (IOException e) {
        if (e.getMessage().equals("end of file")) {
          System.out.println("end of file reached, exiting application");
          break;
        } else {
          e.printStackTrace();
          System.exit(1);
        }
      }
      // split keys
      stringKeys = line.split(" ");
      if (stringKeys.length != 2) {
        System.err.println("read keys not of the correct length! aborting execution");
        System.err.println("key contents:");
        for (String key : stringKeys) {
          System.err.println(String.format("--- %s", key));
        }
        System.exit(1);
      }
      try {
        intKeys[0] = Integer.parseInt(stringKeys[0]);
        intKeys[1] = Integer.parseInt(stringKeys[1]);
      } catch (NumberFormatException e) {
        System.err.println("read keys are not in integer format! aborting execution");
        System.err.println("key contents:");
        System.err.println(String.format("--- %s %s", stringKeys));
        System.exit(1);
      }
      // read from DB and write time to file
      time = this.cassandra.getCustomer(intKeys[0], intKeys[1]);
      this.text.reading(Double.toString(time));


    }
  }

  // terminate
  // **************************************************************************************************************************************************

  public void terminate() {
    this.cassandra.terminate();
    this.text.terminate();
  }

}
