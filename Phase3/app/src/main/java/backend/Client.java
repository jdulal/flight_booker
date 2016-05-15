package backend;

/**
 * Created by quibinni on 2015-11-30.
 * Represents a client for this app. A client is able to view, sort, and book itineraries.
 */
public class Client extends User {

  /**
   * Constructor class for Client.
   *
   * @param lastName         String for last name of this client.
   * @param firstName        String for first name of this client.
   * @param email            String for email address of this client.
   * @param address          String for address of this client.
   * @param creditCardNumber String for credit card number of this client.
   * @param expiryDate       String for expiry date of credit card of this client.
   */
  public Client(String lastName, String firstName, String email, String address,
                String creditCardNumber, String expiryDate) {
    super(lastName, firstName, email, address, creditCardNumber, expiryDate, false);
  }
}
