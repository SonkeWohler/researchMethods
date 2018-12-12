package dataStuff;

import java.io.IOException;
import text.Text;

public class Transformer {

  private Text text;

  public Transformer() {

    this.text = new Text();

    this.text.openKeyFile();
    this.text.createWritingFile();

    this.run();

    this.terminate();
    System.exit(0);
  }

  private void run() {
    String line = "";
    String[] keyString = new String[2];

    for (int i = 0; i < 1000000; i++) {
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
      keyString = line.split(" ");
      line = String.format("%s,%s,%s,%s", keyString);
      this.text.writing(line);
    }
  }

  private void terminate() {
    this.text.terminate();
  }

}
