package text;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

public class Text {

  private Path readingFile = Paths.get(Text.filePath, Text.readingFileName);
  private Path keyFile = Paths.get(Text.filePath, Text.keyFileName);
  private Path writingFile = Paths.get(Text.filePath, Text.writingFileName);
  private Scanner keyScanner;

  public static final String filePath = "D:/uni/CS4040/executionFiles";
  public static final String readingFileName = "CS4040_readingFile.txt";
  public static final String keyFileName = "CS4040_keyFile.txt";
  public static final String writingFileName = "CS4040_fullDetails.txt";

  private ArrayList<String> readingList = new ArrayList<String>();
  private ArrayList<String> keyList = new ArrayList<String>();
  private ArrayList<String> writingList = new ArrayList<String>();

  private Charset encoding = StandardCharsets.UTF_8;

  // Constructor and set up
  // *********************

  public Text() {

  }

  public void createReadingFile() {
    try {
      Files.deleteIfExists(this.readingFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      Files.createFile(this.readingFile);
      System.out
          .println(String.format("readingFile is located at: %s", this.readingFile.toString()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void createWritingFile() {
    try {
      Files.deleteIfExists(this.writingFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      Files.createFile(this.writingFile);
      System.out.println(
          String.format("full detail file is located at: %s", this.writingFile.toString()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void createKeyFile() {
    try {
      Files.deleteIfExists(this.keyFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      Files.createFile(this.keyFile);
      System.out.println(String.format("keyFile is located at: %s", this.keyFile.toString()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void openKeyFile() {
    if (Files.exists(keyFile) == false) {
      System.err.println("no keyFile found, aborting execution");
      System.exit(1);
    }
    try {
      this.keyScanner = new Scanner(this.keyFile, this.encoding.name());
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  // writing to file
  // *********************************

  public void reading(String reading) {
    this.readingList.add(reading);
    if (this.readingList.size() > 10) {
      this.reading();
    }
  }

  private void reading() {
    try {
      Files.write(this.readingFile, this.readingList, this.encoding, StandardOpenOption.APPEND);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.readingList.clear();
  }

  public void key(String line) {
    this.keyList.add(line);
    if (this.keyList.size() > 10) {
      this.key();
    }
  }

  private void key() {
    try {
      Files.write(this.keyFile, this.keyList, this.encoding, StandardOpenOption.APPEND);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.keyList.clear();
  }

  public void writing(String line) {
    this.writingList.add(line);
    if (this.writingList.size() > 10) {
      this.writing();
    }
  }

  private void writing() {
    try {
      Files.write(this.writingFile, this.writingList, this.encoding, StandardOpenOption.APPEND);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.writingList.clear();
  }

  // reading from file
  // ****************************************************

  public String readNextKey() throws IOException {
    String line = "";
    if (this.keyScanner.hasNextLine() == true) {
      line = this.keyScanner.nextLine();
    } else {
      throw new IOException("end of file"); // TODO use this somehow to end reading
    }
    return line;
  }

  // termination
  // *******************************************************************************

  public void terminate() {
    this.reading();
    this.key();
    this.writing();
    try {
      this.keyScanner.close();
    } catch (NullPointerException e) {
    }

    // TODO apparently no need to close resources?
  }

  // main
  // ********************************************************************************************************************************************

  public static void main(String[] args) {
    System.out.println("--- testing Text ---");
    Text myText = new Text();
    myText.createKeyFile();
    myText.createReadingFile();
    myText.reading("testing");
    myText.key("testing");
    myText.terminate();
    System.out.println("end of main");
    System.exit(0);
  }
}
