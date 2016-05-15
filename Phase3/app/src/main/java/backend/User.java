package backend;

import databases.FlightDatabase;
import databases.UserDatabase;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User class.
 *
 * @author Edgar Lai
 */
public class User implements Serializable {

  // Instance variables
  // to store saved Itineraries
  private ArrayList<Itinerary> bookedItineraries;
  // a temporary list to show all possible Itineraries to User
  private ArrayList<Itinerary> itineraryList;
  // the databases
  protected static UserDatabase userDatabase = new UserDatabase();
  protected static FlightDatabase flightDatabase = new FlightDatabase();
  // variables for User constructor containing all info
  private String lastName;
  private String firstName;
  private String email;
  private String address;
  private String creditCardNumber;
  private String expiryDate;
  private boolean isAdmin;

  /**
   * Constructor class for User.
   *
   * @param lastName         String for last name of User.
   * @param firstName        String for first name of User.
   * @param email            String for email address of User.
   * @param address          String for address of User.
   * @param creditCardNumber String for credit card number of User.
   * @param expiryDate       String for expiry date of credit card of User.
   */
  public User(String lastName, String firstName, String email, String address,
              String creditCardNumber, String expiryDate, boolean isAdmin) {
    // making user with parameters
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.address = address;
    this.creditCardNumber = creditCardNumber;
    this.expiryDate = expiryDate;
    // making lists of itineries for user to book and get list
    this.bookedItineraries = new ArrayList<Itinerary>();
    this.itineraryList = new ArrayList<Itinerary>();
    this.isAdmin = isAdmin;
    User.userDatabase.addItem(this);
    User.userDatabase.save();
  }


  // start of getters and setters for all billing and personal info.

  /**
   * Returns the last name of this user.
   *
   * @return the last name of this user.
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of this user.
   *
   * @param lastName the last name to set.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Returns the first name of this user.
   *
   * @return the first name of this user.
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name of this user.
   *
   * @param firstName the first name to set.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Returns the email of this user.
   *
   * @return the email of this user.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email of this user.
   *
   * @param email the email to set.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the address of this user.
   *
   * @return the address of this user.
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address of this user.
   *
   * @param address the address to set.
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Returns the credit card number of this user.
   *
   * @return the credit card number to set.
   */
  public String getCreditCardNumber() {
    return creditCardNumber;
  }

  /**
   * Sets the credit card number of this user.
   *
   * @param creditCardNumber the credit card number to set.
   */
  public void setCreditCardNumber(String creditCardNumber) {
    this.creditCardNumber = creditCardNumber;
  }

  /**
   * Returns the expiry date of this user's credit card.
   *
   * @return the expiry date of this user's credit card.
   */
  public String getExpiryDate() {
    return expiryDate;
  }

  /**
   * Sets the expiry date of this user's credit card.
   *
   * @param expiryDate the expiry date to set.
   */
  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * Checks if this user is an admin.
   *
   * @return true if the user is an admin, otherwise false.
   */
  public boolean isAdmin() {
    return this.isAdmin;
  }

  /**
   * Returns the flight with a given flight number since all flight numbers are unique.
   *
   * @param flightNum the flight number of the flight
   * @return the flight with give flight number
   */
  public static Flight getFlight(String flightNum) {
    return User.flightDatabase.getFlight(flightNum);
  }

  /**
   * Returns an array list of all flights that depart from origin and
   * arrive at destination on the given date.
   *
   * @param origin        a flight origin.
   * @param destination   a flight destination.
   * @param departureDate a departure date (in the format YYYY-MM-DD).
   * @return an array list of all flights that depart from origin and
   arrive at destination on the given date.
   */
  public static ArrayList<Flight> searchFlights(String origin, String destination,
                                                String departureDate) {
    return User.flightDatabase.getFlights(origin, destination, departureDate);
  }

  /**
   * Clears the itineary list for this user.
   */
  public void clearItineraries() {
    this.itineraryList.clear();
  }

  /**
   * Clears booked itineraries for this user
   */
  public void clearBookedItineraries() {
    this.bookedItineraries.clear();
  }

  /**
   * Return an arraylist of itineraries.
   *
   * @return an arraylist of itineraries.
   */
  public ArrayList<Itinerary> getItineraryListAsList() {
    return itineraryList;
  }

  /**
   * Return the String representation of the list of itineraries.
   *
   * @return the String representation of the list of itineraries.
   */
  public String getItineraryList() {
    String message = "";
    for (int i = 0; i < itineraryList.size(); i++) {
      message += itineraryList.get(i).toString();
      if (i != itineraryList.size() - 1) {
        message += "\n";
      }
    }
    return message;
  }

  /**
   * Return an arraylist of the booked itineraries.
   *
   * @return an arraylist of the booked itineraries.
   */
  public ArrayList<Itinerary> getBookedItineraryListAsList() {
    return bookedItineraries;
  }

  /**
   * Return an ArrayList of ArrayList of flights which contain all the possible sequences from
   * the origin to the destination.
   *
   * @param origin      the location which the user wants to depart from.
   * @param destination the location which the user wants to travel to.
   * @return an ArrayList of ArrayList of flights.
   */
  private ArrayList<ArrayList<Flight>> search(String origin, String destination,
                                              ArrayList<String> locations) {
    // get all the flights that depart from the given origin
    ArrayList<Flight> allOrigins = User.flightDatabase.getFlightsOfOrigin(origin);
    // if none were found then return an ArrayList with an empty ArrayList
    if (allOrigins.size() == 0) {
      return new ArrayList<ArrayList<Flight>>();
    }
    // initiate new list to store all possible flight combinations
    ArrayList<ArrayList<Flight>> allFlights = new ArrayList<ArrayList<Flight>>();
    // loop through each of these flights (starting points)
    for (Flight flight : allOrigins) {
      // keep track of the locations where we have been
      locations.add(origin);
      // if the flight goes directly to the destination then add it to the main list
      if (flight.getDestination().equals(destination)) {
        ArrayList<Flight> newFlight = new ArrayList<Flight>();
        newFlight.add(flight);
        allFlights.add(newFlight);
        // otherwise check if we have already visited the destination
      } else if ((!locations.contains(flight.getDestination()))) {
        // get all flight possibilities from the current flight's destination to
        // the actual destination
        ArrayList<ArrayList<Flight>> listofsubFlights = search(flight.getDestination(),
                destination, locations);
        // add the current flight to each of the subflights found
        for (ArrayList<Flight> subFlights : listofsubFlights) {
          subFlights.add(0, flight);
        }
        // add these new flights to the main list
        allFlights.addAll(listofsubFlights);
      }
      // remove the location that was traversed through
      locations.remove(locations.size() - 1);
    }
    return allFlights;
  }

  /**
   * Given the date, origin and destination, create possible itineraries
   *
   * @param date        the date which the user would like to depart on.
   * @param origin      the location which the user would like to depart from.
   * @param destination the location which the user wants to travel to.
   */
  private void createItineraries(String date, String origin, String destination) {
    // get all the flight combinations that go from the given origin to the given destination
    ArrayList<ArrayList<Flight>> allflights = search(origin, destination, new ArrayList<String>());
    // loop through every combination (each combination is a possible itinerary)
    for (ArrayList<Flight> flights : allflights) {
      // create new itinerary with each combination of flights
      Itinerary itinerary = new Itinerary(flights);
      // if this new itinerary has the wanted departure date and is not empty,
      // and has valid wait times add it to the main list of itineraries
      if (!itinerary.is_empty() && itinerary.getFirstDepartureDate().equals(date)
              && itinerary.hasValidWaitTimes() && itinerary.flightsHaveRoom()) {
        this.itineraryList.add(itinerary);
      }
    }

  }

  /**
   * Given the date, origin and destination, return a string of valid itineraries.
   *
   * @param date        the date which the user would like to depart on.
   * @param origin      the location which the user would like to depart from.
   * @param destination the location which the user wants to travel to.
   */
  public String getItineraries(String date, String origin, String destination) {
    createItineraries(date, origin, destination);
    return getItineraryList();
  }

  /**
   * Given a list of itineraries create a new list of itineraries that is sorted in non-decreasing
   * order based on travel time and return it.
   *
   * @param itineraryList the list of itineraries that needs to be sorted.
   * @return a new sorted list of itineraries.
   */
  private ArrayList<Itinerary> sortByTravelTime(ArrayList<Itinerary> itineraryList) {
    // make a new itinerary list
    ArrayList<Itinerary> newItin = new ArrayList<Itinerary>();
    // loop through original list
    for (Itinerary itin : itineraryList) {
      // if new list is empty then just add itinerary to list
      if (newItin.isEmpty()) {
        newItin.add(itin);
        // else loop through all elements of new itinerary list
      } else {
        // keep track whether the itinerary was added to the new list or not
        boolean added = false;
        for (Itinerary tempItin : newItin) {
          // if new itinerary travel time is less than the current itinerary travel time
          if (itin.totalTravelTime() < tempItin.totalTravelTime()) {
            // add the temporary itinerary to the new list
            int index = newItin.indexOf(tempItin);
            newItin.add(index, itin);
            added = true;
            // stop the loop
            break;
          }
        }
        // if its wasn't added then the itinerary has the largest cost, so add it to the end
        if (!added) {
          newItin.add(itin);
        }
      }
    }
    return newItin;
  }

  /**
   * Given the date, origin and destination, return a string of valid itineraries that is
   * sorted in non-decreasing order based on travel time and return it.
   *
   * @param date        the date which the user would like to depart on.
   * @param origin      the location which the user would like to depart from.
   * @param destination the location which the user wants to travel to.
   */
  public String getItinerariesSortedByTime(String date, String origin, String destination) {
    createItineraries(date, origin, destination);
    this.itineraryList = sortByTravelTime(itineraryList);
    return getItineraryList();
  }

  /**
   * Sort the already existing itinerary list by cost.
   *
   * @return the itinerary list sorted by time.
   */
  public ArrayList<Itinerary> sortByTime() {
    this.itineraryList = sortByTravelTime(itineraryList);
    return itineraryList;
  }

  /**
   * Given a list of itineraries create a new list of itineraries that is sorted in non-decreasing
   * order based on total cost and return it.
   *
   * @param itineraryList the list of itineraries that needs to be sorted.
   * @return a new sorted list of itineraries.
   */
  private ArrayList<Itinerary> sortByTotalCost(ArrayList<Itinerary> itineraryList) {
    // make a new itinerary list
    ArrayList<Itinerary> newItin = new ArrayList<Itinerary>();
    // loop through original list
    for (Itinerary itin : itineraryList) {
      // if  new list is empty then just add itinerary to list
      if (newItin.isEmpty()) {
        newItin.add(itin);
        // else loop through all elements of new itinerary list
      } else {
        boolean added = false;
        for (Itinerary tempItin : newItin) {
          // if temporary itinerary cost is less than the current itinerary cost
          if (itin.getTotalCost() < tempItin.getTotalCost()) {
            // add the temporary itinerary to the new list
            int index = newItin.indexOf(tempItin);
            newItin.add(index, itin);
            added = true;
            // stop the loop
            break;
          }
        }
        // if its wasn't added then the itinerary has the largest cost, so add it to the end
        if (!added) {
          newItin.add(itin);
        }
      }
    }
    return newItin;
  }

  /**
   * Given the date, origin and destination, return a string of valid itineraries that is
   * sorted in non-decreasing order based on total cost and return it.
   *
   * @param date        the date which the user would like to depart on.
   * @param origin      the location which the user would like to depart from.
   * @param destination the location which the user wants to travel to.
   */
  public String getItinerariesSortedByCost(String date, String origin, String destination) {
    createItineraries(date, origin, destination);
    this.itineraryList = sortByTotalCost(itineraryList);
    return getItineraryList();
  }

  /**
   * Sort the already existing itinerary list by cost.
   *
   * @return the itinerary list sorted by cost.
   */
  public ArrayList<Itinerary> sortByCost() {
    this.itineraryList = sortByTotalCost(itineraryList);
    return itineraryList;
  }

  /**
   * Return true if the itinerary is already booked and false otherwise
   */
  public boolean isBooked(Itinerary newItin) {
    // check each booked itinerary
    for (Itinerary itinerary : this.bookedItineraries) {
      // if one of those equal to the given itinerary then it was already booked
      if (newItin.equals(itinerary)) {
        return true;
      }
    }
    // otherwise it wasn't booked
    return false;
  }

  /**
   * Book an itinerary only if all the flights have available seats.
   * @param newItin the itinerary to book.
   * @return true if the itinerary was booked, otherwise false.
   */
  public boolean bookItinerary(Itinerary newItin) {
    // only add the new itinerary if it hasn't already been booked
    if (!(isBooked(newItin))) {
      // check if the flights in the itinerary even has room
      if (newItin.flightsHaveRoom()) {
        // then add it to the booked itinerary list
        this.bookedItineraries.add(newItin);
        // book seats in each flight
        newItin.bookFlights();
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * Unbook the given itinerary from the booked Itineraries.
   */
  public void cancelItinerary(Itinerary newItin) {
    // only unbook if it already exists
    for (Itinerary itinerary : this.bookedItineraries) {
      if(itinerary.equals(newItin)) {
        this.bookedItineraries.remove(itinerary);
        // unbook the seat in each flight of the itinerary
        itinerary.cancelFlights();
        break;
      }
    }
  }

  /**
   * method to allow user to view BookedItineraries.
   *
   * @return bookedItinerary ArrayList of booked itineraries
   */
  public ArrayList<Itinerary> viewBookedItineraries() {
    return this.bookedItineraries;
  }

  /**
   * Returns the string representation of this user.
   *
   * @return the string representation of this user.
   */
  @Override
  public String toString() {
    return String.format("%s, %s\n%s\n%s\n%s, %s\n", this.getLastName(),
            this.getFirstName(), this.getEmail(), this.getAddress(), this.getCreditCardNumber(),
            this.getExpiryDate());
  }
}
