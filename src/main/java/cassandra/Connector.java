package cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class Connector {

  // Connection Variables for Cassandra
  Cluster cluster;
  Session session;
  // Bound Statements
  PreparedStatement write;
  PreparedStatement read;


  // constructor
  // *********************

  public Connector() {
    this.setUp("127.0.0.1");
  }

  private void setUp(String connectionAddress) {
    System.out.println("Java connecting to Cassandra on " + connectionAddress);
    cluster = Cluster.builder().addContactPoints(connectionAddress).build();
    try {
      session = cluster.connect();
    } catch (NoHostAvailableException e) {
      System.err.println("no Cassandra available, please start Cassandra Node");
      System.err.println("aborting execution");
      System.exit(3);
    }
    System.out.println("Java connection to Cassandra established");
  }

  // Database Set Up
  // ***********************************

  public void clearData() {
    System.out.println("clearing Data");
    String query = "TRUNCATE cs4040.customer;";
    this.session.execute(query);
  }

  public void tableSetUp() {
    String query;


    System.out.println("Cassandra: Creating Keyspace");

    query =
        "CREATE KEYSPACE IF NOT EXISTS cs4040 WITH REPLICATION={'class':'SimpleStrategy', 'replication_factor':'1'};";
    this.session.execute(query);
    query = "USE cs4040;";
    this.session.execute(query);

    System.out.println("Cassandra: Setting up Tables");

    query =
        "CREATE TABLE IF NOT EXISTS customer(hashID int, cID int, name text, address text, PRIMARY KEY((hashID), cID));";
    this.session.execute(query);


    System.out.println("Cassandra: Setting up Bound Statements");

    query = "INSERT INTO customer (hashID, cID, name, address) VALUES (?,?,?,?);";
    this.write = this.session.prepare(query);


    query = "SELECT name FROM cs4040.customer WHERE hashID=? AND cID=?;";
    this.read = this.session.prepare(query);
  }

  // Writing to DB
  // *********************************************************

  public double newCustomer(int hashID, int cID, String name, String address) {
    double time = 0.0;
    BoundStatement statement = this.write.bind(hashID, cID, name, address);
    // TODO System time
    this.session.execute(statement);
    // TODO System time
    return time;
  }

  // Reading from DB
  // ***********************************************************************************

  public double getCustomer(int hashID, int cID) {
    double time = 0.0;
    String query =
        String.format("SELECT name FROM cs4040.customer WHERE hashID=%s AND cID=%s;", hashID, cID);
    // TODO System time
    this.session.execute(query);
    // TODO System time
    return time;
  }

  // termination
  // **************************************************************************************************************************************************

  public void terminate() {
    System.out.println("Terminating Java Connection to Cassandra");
    this.session.close();
  }

  // main
  // **********************************************************************************************************************************************************************************

  public static void main(String[] args) {
    System.err.println("--- testing Cassandra ---");
    Connector myConnector = new Connector();
    myConnector.tableSetUp();
    myConnector.clearData();
    myConnector.newCustomer(0, 0, "test", "testing");
    System.out.println(myConnector.getCustomer(0, 0));
    myConnector.terminate();
    System.out.println("end of main");
    System.exit(0);
  }

}
