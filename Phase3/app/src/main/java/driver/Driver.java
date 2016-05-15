package driver;

import java.io.File;
import java.util.ArrayList;

import backend.Admin;
import backend.Flight;
import backend.User;

/**
 * A Driver used for autotesting the project backend.
 */
public class Driver {

  /**
   * Uploads client information to the application from the file at the
   * given path.
   *
   * @param path the path to an input csv file of client information with
   *             lines in the format:
   *             LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
   *             (the ExpiryDate is stored in the format YYYY-MM-DD)
   */
  public static void uploadClientInfo(String path) {
    // create a file with the path and pass it into the upload clients method
    File file = new File(path);
    Admin.uploadClients(file);
  }

  /**
   * Uploads flight information to the application from the file at the
   * given path.
   *
   * @param path the path to an input csv file of flight information with
   *             lines in the format:
   *             Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,Price,NumSeats
   *             (the dates are in the format YYYY-MM-DD HH:MM; the price has exactly two
   *             decimal places; the number of seats is a non-negative integer)
   */
  public static void uploadFlightInfo(String path) {
    // create a file with the path and pass it into the upload flights method
    File file = new File(path);
    Admin.uploadFlights(file);
  }

  /**
   * Returns the information stored for the client with the given email.
   *
   * @param email the email address of a client
   * @return the information stored for the client with the given email
   * in this format:
   * LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
   * (the ExpiryDate is stored in the format YYYY-MM-DD)
   */
  public static String getClient(String email) {
    // get the client
    User client = Admin.getClient(email);
    // format the message
    String message = String.format("%s,%s,%s,%s,%s,%s", client.getLastName(),
            client.getFirstName(), client.getEmail(), client.getAddress(),
            client.getCreditCardNumber(), client.getExpiryDate());
    return message;
  }

  /**
   * Returns all flights that depart from origin and arrive at destination on
   * the given date.
   *
   * @param date        a departure date (in the format YYYY-MM-DD)
   * @param origin      a flight origin
   * @param destination a flight destination
   * @return the flights that depart from origin and arrive at destination
   * on the given date formatted with one flight per line in exactly this
   * format:
   * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,Price
   * (the departure and arrival date and time are in the format
   * YYYY-MM-DD HH:MM; the price has exactly two decimal places)
   */
  public static String getFlights(String date, String origin, String destination) {
    // get the flights that matches the requirement
    ArrayList<Flight> flights = User.searchFlights(origin, destination, date);
    String message = "";

    // loop through the array list and format the message
    for (Flight flight : flights) {
      message += String.format("%s,%s,%s,%s,%s,%s,%.2f\n", flight.getFlightnum(),
              flight.getDepartureDateTime(), flight.getArrivalDateTime(), flight.getAirline(),
              flight.getOrigin(), flight.getDestination(), flight.getCost());
    }
    return message;
  }

  /**
   * Returns all itineraries that depart from origin and arrive at
   * destination on the given date. See handout for detailed description
   * of a valid itinerary.
   * <p/>
   * Every flight in an itinerary must have at least one seat
   * available for sale. That is, the itinerary must be bookable.
   *
   * @param date        a departure date (in the format YYYY-MM-DD)
   * @param origin      a flight original
   * @param destination a flight destination
   * @return itineraries that satisfy the requirements in the handout.
   * Each itinerary in the output should contain one line per flight,
   * in the format:
   * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
   * followed by total price (on its own line, exactly two decimal places)
   * followed by total duration (on its own line, in format HH:MM).
   */
  public static String getItineraries(String date, String origin, String destination) {
    // create a temporary admin
    Admin temp = new Admin(null, null, "qwertyqwerty", null, null, null);
    // use temporary admin to get itineraries
    String itineraries = temp.getItineraries(date, origin, destination);
    // remove the temp admin from the user database
    Admin.removeAdmin(temp);
    return itineraries;
  }

  /**
   * Returns the same itineraries as getItineraries produces, but sorted according
   * to total itinerary cost, in non-decreasing order.
   *
   * @param date        a departure date (in the format YYYY-MM-DD)
   * @param origin      a flight original
   * @param destination a flight destination
   * @return itineraries that satisfy the requirements in the handout.
   * Each itinerary in the output should contain one line per flight,
   * in the format:
   * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
   * followed by total price (on its own line, exactly two decimal places)
   * followed by total duration (on its own line, in format HH:MM).
   */
  public static String getItinerariesSortedByCost(String date, String origin, String destination) {
    // create a temporary admin to search
    Admin temp = new Admin(null, null, "qwertyqwerty", null, null, null);
    // use temporary admin to get itineraries
    String itineraries = temp.getItinerariesSortedByCost(date, origin, destination);
    // remove the temp admin from the user database
    Admin.removeAdmin(temp);
    return itineraries;
  }

  /**
   * Returns the same itineraries as getItineraries produces, but sorted according
   * to total itinerary travel time, in non-decreasing order.
   *
   * @param date        a departure date (in the format YYYY-MM-DD)
   * @param origin      a flight original
   * @param destination a flight destination
   * @return itineraries that satisfy the requirements in the handout.
   * Each itinerary in the output should contain one line per flight,
   * in the format:
   * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
   * followed by total price (on its own line, exactly two decimal places),
   * followed by total duration (on its own line, in format HH:MM).
   */
  public static String getItinerariesSortedByTime(String date, String origin, String destination) {
    // create a temporary admin to search
    Admin temp = new Admin(null, null, "qwertyqwerty", null, null, null);
    // use temporary admin to get itineraries
    String itineraries = temp.getItinerariesSortedByTime(date, origin, destination);
    // remove the temp admin from the user database
    Admin.removeAdmin(temp);
    return itineraries;
  }
}
