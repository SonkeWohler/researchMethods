package launch;

import dataStuff.Creator;
import dataStuff.Reader;

public class Launcher {

  public static void main(String[] args) {
    System.out.println("CS4040 Project 2018 - Sonke Wohler 51552212");
    System.out.println("");
    for (int i = 0; i < args.length; i++) {
      args[i] = args[i].toLowerCase();
    }
    switch (args[0]) {
      case "":
        System.err.println("No arguments given, please provide arguments.");
        System.err.println(
            "Use 'WRITE' to set up DB with Data, followed by 'IDEAL', 'NORMAL' or 'WORST' and the desired number of entries.");
        System.err.println("Use 'READ' to test the Data for reading times.");
        System.exit(2);
        break;
      case "write":
        System.out.println(String.format(
            "Initiating Program for DB set up for %s Case with %s entries", args[1], args[2]));
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        new Creator(args[1], args[2]);
        break;
      case "read":
        System.out.println("Initiating Program for assessing DB reading times.");
        new Reader();
        break;
    }

    System.out.println("end of main");

  }

}
