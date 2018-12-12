package dataStuff;

import java.security.SecureRandom;
import java.util.Random;
import cassandra.Connector;
import text.Text;

public class Creator {

  private Connector cassandra;
  private Text text;

  private SecureRandom seedGen; // used for seeding rand to minimize its pseudorandomness effect on
                                // findings
  private Random rand; // provides random functionalities including gaussians
  private int keyType = 0;
  private int totalIterations = 0;
  private int readsRemaining = 10000;

  // Constructor and set up
  // ****************************

  public Creator() { // for testing only
    this.setUp("normal");

    this.run(0);

    this.terminate();
  }

  public Creator(String keyType, String iterations) {
    try {
      this.totalIterations = Integer.parseInt(iterations);
    } catch (NumberFormatException e) {
      System.err.println("error: number of entries must be a parsable intger value");
      System.err.println("aborting execution");
      System.exit(2);
    }

    this.setUp(keyType);

    this.run(this.totalIterations);

    this.terminate();
    System.exit(0);
  }

  private void setUp(String keyType) {
    this.cassandra = new Connector();
    this.text = new Text();

    this.seedGen = new SecureRandom();
    this.rand = new Random(this.seedGen.nextLong());

    this.cassandra.tableSetUp();
    this.cassandra.clearData();

    this.text.createKeyFile();

    this.randSetUp(keyType);
  }

  private void randSetUp(String type) {
    switch (type) {
      case "ideal":
        this.keyType = 1;
        break;
      case "normal":
        this.keyType = 2;
        break;
      case "worst":
        this.keyType = 3;
        break;
      default:
        System.err.println("no valid keyType selected, please use 'IDEAL', 'NORMAL' or 'WORST'");
        System.err.println("aborting execution");
        System.exit(2);
        break;
    }
  }

  // running
  // ******************************************************************************

  private void run(int iterations) {
    System.out.println("creating and writing Dataset");
    long time = 0;
    int hashID = 0;
    int cID = 0;
    String name = "test";
    String address = "testing";

    for (int i = 0; i < iterations; i++) {
      hashID = this.makeHashID(i);
      cID = i;

      time = this.cassandra.newCustomer(hashID, cID, name, address); // could remember writing time

      if (this.readThisKey(i)) {
        this.text
            .key(String.format("%s %s %s", Integer.toString(hashID), Integer.toString(cID), time));
      }

      if (i % 10 == 0) {
        this.reseed();
        if (i % 1000 == 0) {
          System.out.println(String.format("- now at key number %s, %s iterations remaining", i,
              this.totalIterations - i));
        }
      }

    }
  }

  private int makeHashID(int i) {
    int hash = 0;
    double temporary = 0.0;
    switch (this.keyType) {
      case 1:
        hash = i / 10;
        break;
      case 2:
        temporary = rand.nextGaussian() * 10;
        hash = Math.abs((int) temporary);
        break;
      case 3:
        hash = 0;
        break;
      default:
        System.err.println("error when selecting hash fucntion, aborting execution");
        System.exit(2);
        break;
    }
    return hash;
  }

  private boolean readThisKey(int i) {
    if (this.readsRemaining == 0) {
      return false;
    } else if (this.totalIterations - i <= this.readsRemaining) {
      this.readsRemaining--;
      return true;
    } else {
      int helper = this.totalIterations / 100;
      int otherHelper = this.rand.nextInt(this.totalIterations);
      if (otherHelper < helper) {
        this.readsRemaining--;
        return true;
      } else {
        return false;
      }
    }
  }

  private void reseed() {
    this.rand.setSeed(this.seedGen.nextLong());
  }

  // termination
  // **********************************************************************************************************************

  public void terminate() {
    this.cassandra.terminate();
    this.text.terminate();
  }

  // main
  // *************************************************************************************************************************************************************************

  public static void main(String[] args) {
    System.err.println("--- testing data creator ---");
    Creator myCreator = new Creator();

    myCreator.terminate();
    System.out.println("end of main");
    System.exit(0);
  }

}
