package backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by quibinni on 2015-11-30.
 * Represents an admin for the app. It has special privileges such as uploading flight or client
 * info, and editing and viewing clients' info. Moreover, an admin can view and book itineraries
 * for a specific client.
 */
public class Admin extends User {

  /**
   * Constructor class for Admin.
   *
   * @param lastName         String for last name of this aadmin.
   * @param firstName        String for first name of this admin.
   * @param email            String for email address of this admin.
   * @param address          String for address of this admin.
   * @param creditCardNumber String for credit card number of this admin.
   * @param expiryDate       String for expiry date of credit card of this admin.
   */
  public Admin(String lastName, String firstName, String email, String address,
               String creditCardNumber, String expiryDate) {
    super(lastName, firstName, email, address, creditCardNumber, expiryDate, true);
  }

  /**
   * Adds all clients in a .csv file to the user database of format
   * LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate where every
   * line is a client. Returns a string rep of the uploaded clients.
   *
   * @param file file to upload.
   * @return a string rep of the uploaded clients.
   */
  public static String uploadClients(File file) {
    try {
      // init the str to return
      String uploaded = "Uploaded the following Clients:\n\n";
      Scanner sc = new Scanner(file);
      // the string of the client's info
      String client;
      // strings of the individual client info
      String[] clientInfo;
      // read every line of the .csv file until there is none to read
      while (sc.hasNext()) {
        // get the client's info
        client = sc.nextLine();
        // extract the info by splitting it
        clientInfo = client.split(",");
        // create the new client
        Client newClient = new Client(clientInfo[0], clientInfo[1], clientInfo[2],
                clientInfo[3], clientInfo[4], clientInfo[5]);
        // add the new client to this database and save
        Admin.userDatabase.addItem(newClient);
        Admin.userDatabase.save();
        // add the string rep of the client to the recent upload var
        uploaded += newClient.toString() + "\n";
      }
      // close the scanner
      sc.close();
      // return the uploaded str
      return uploaded;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * Adds all admins in a .csv file to the user database of format
   * LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate where every
   * line is an admin. Returns a string rep of the uploaded admins.
   *
   * @param file file to upload.
   * @return a string rep of the uploaded admins.
   */
  public static String uploadAdmins(File file) {
    try {
      // init the str to return
      String uploaded = "Uploaded the following Admins:\n\n";
      Scanner sc = new Scanner(file);
      // the string of the admin's info
      String admin;
      // strings of the individual admin info
      String[] adminInfo;
      // read every line of the .csv file until there is none to read
      while (sc.hasNext()) {
        // get the admin's info
        admin = sc.nextLine();
        // extract the info by splitting it
        adminInfo = admin.split(",");
        // create the new admin
        Admin newAdmin = new Admin(adminInfo[0], adminInfo[1], adminInfo[2],
                adminInfo[3], adminInfo[4], adminInfo[5]);
        // add the new admin to this database and save
        Admin.userDatabase.addItem(newAdmin);
        Admin.userDatabase.save();
        uploaded += newAdmin.toString() + "\n";
      }
      // close the scanner
      sc.close();
      // return the string rep of the uploaded admins.
      return uploaded;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * Adds all flights in a .csv file to the flight database of format
   * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,Price
   * Dates and times stored as YYYY-MM-DD HH:MM and every line is a flight.
   * Returns a string rep of the flights uploaded.
   *
   * @param file file to upload.
   * @return a string rep of the uploaded flights.
   */
  public static String uploadFlights(File file) {
    try {
      // init the str to return
      String uploaded = "Uploaded the following Flights:\n\n";
      Scanner sc = new Scanner(file);
      // the string of the flight's info
      String flight;
      // strings of each individual flight info
      String[] flightInfo;
      // read every line of the .csv file until there is none to read
      while (sc.hasNext()) {
        // get the flight's info
        flight = sc.nextLine();
        // extract the info by splitting it
        flightInfo = flight.split(",");
        // create the new flight (the last element in the array is the price
        // info, therefore convert it to a double
        Flight newFlight = new Flight(flightInfo[0], flightInfo[1], flightInfo[2],
                flightInfo[3], flightInfo[4],
                flightInfo[5], Double.parseDouble(flightInfo[6]),
                Integer.parseInt(flightInfo[7]));
        // add it to the database and save it
        Admin.flightDatabase.addItem(newFlight);
        Admin.flightDatabase.save();
        // concat the new flight string rep to the recent upload var
        uploaded += newFlight.toString();
      }
      // close the scanner
      sc.close();
      // return the str rep of the uploaded admins
      return uploaded;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * Removes the client from the client database.
   *
   * @param client the client to remove.
   */
  public static void removeClient(Client client) {
    Admin.userDatabase.removeItem(client);
    Admin.userDatabase.save();
  }

  /**
   * Removes the admin from the admin database.
   *
   * @param admin the admin to remove.
   */
  public static void removeAdmin(Admin admin) {
    Admin.userDatabase.removeItem(admin);
    Admin.userDatabase.save();
  }

  /**
   * Returns the client with the given email.
   *
   * @param email the email address of the client.
   * @return the client with the given email.
   */
  public static Client getClient(String email) {
    return (Client) Admin.userDatabase.getUser(email);
  }

  /**
   * Returns the admin with the given email.
   *
   * @param email the email address of the admin.
   * @return the admin with the given email.
   */
  public static Admin getAdmin(String email) {
    return (Admin) Admin.userDatabase.getUser(email);

  }
}
