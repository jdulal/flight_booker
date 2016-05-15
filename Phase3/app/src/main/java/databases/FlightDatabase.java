package databases;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import backend.Flight;

/**
 * A database to store all flights. The flights are stored in an array list. The
 * state of the database is saved in a json file for serializing and
 * deserializing. The GSON library is used for this process. No duplicates are
 * allowed in the array list. If a flight to be added matches an existing
 * flight, it will not be added.
 *
 * @author Nikki Quibin - quibinni
 */
public class FlightDatabase implements Database<Flight> {

  // a list to store all flights
  private ArrayList<Flight> flights;
  // a re-usuable gson object for serialization/deserialization
  private Gson gson = new Gson();
  // the parameterized type for object serialization/deserialization
  // with generics. Uses an anonymous class to get the tpye
  private Type flightType = new TypeToken<ArrayList<Flight>>() {}.getType();
  // store the context
  private static Context context;

  /**
   * Get the file of this database from the internal storage.
   *
   * @return the file of this database from the internal storage.
   */
  private File getFlightsFile() {
    File userData = FlightDatabase.context.getApplicationContext().getDir("databases",
            Context.MODE_PRIVATE);
    return new File(userData, "flights.json");
  }

  /**
   * Creates an flight database object without giving the context of the activity. This should be
   * called when there is an already existing context.
   */
  public FlightDatabase() {
    this(FlightDatabase.context);
  }

  /**
   * Initializes the flight database. Creates an empty database and json file for
   * saving the state of the database if it doesn't exist. If there is an
   * existing json file, then load the previously saved state of the database.
   *
   * @param context the context of the activity
   */
  public FlightDatabase(Context context) {
    FlightDatabase.context = context;

    File flightsFile = this.getFlightsFile();

    if (!flightsFile.exists()) {
      this.flights = new ArrayList<>();
      this.save();
    } else {
      this.load();
    }
  }

  /**
   * Finds the index of the flight in the list. It returns -1 if the flight
   * cannot be found.
   *
   * @param flight the flight to find the index of.
   * @return the index of the flight (-1 if not found).
   */
  private int findFlightIndex(Flight flight) {
    // the index to return
    int index = 0;

    // check if the list is empty, if it is, index = -1. If not
    // then try finding the flight in the list
    if (this.flights.isEmpty()) {
      index = -1;
    } else {
      for (Flight currFlight : this.flights) {
        // each flight has a unique flight number, so if they match,
        // we found the flight
        if (flight.getFlightnum().equals(currFlight.getFlightnum())) {
          // we found the index, so break
          break;
        } else {
          // increment the index
          index += 1;
        }
      }
    }

    // if the index is equal to the size of the list, then it's not in
    // the list
    if (index >= this.flights.size()) {
      index = -1;
      return index;
    } else {
      return index;
    }
  }

  /**
   * Add the flight to this database. If the given flight is already in this
   * database, then it will be overwritten.
   *
   * @param flight the flight to add.
   */
  @Override
  public void addItem(Flight flight) {
    // check if the flight is already in the list, if it is, overwrite
    // the existing flight
    int index = findFlightIndex(flight);
    if (index < 0) {
      this.flights.add(flight);
    } else {
      // check that the index is valid. Replace the flight if it is
      if (index < this.flights.size()) {
        this.flights.set(index, flight);
      }
    }
  }

  /**
   * Removes the flight from this database.
   *
   * @param flight the flight to remove.
   * @return true if the flight was removed, otherwise, false.
   */
  @Override
  public boolean removeItem(Flight flight) {
    // the index of the flight
    int index = findFlightIndex(flight);

    // if the item was removed, return true, otherwise false
    if (index < 0) {
      return false;
    } else {
      this.flights.remove(index);
      return true;
    }
  }

  /**
   * Returns the flight object at the given index if it is valid. It will return
   * a null value if the index is invalid.
   *
   * @param index the index of the desired flight.
   * @return the flight object at the given index, or null if index is invalid.
   */
  @Override
  public Flight getItem(int index) {
    // check if the index is valid, if it is return the flight object,
    // otherwise return a null value
    if (index < 0 || index >= this.flights.size()) {
      return null;
    } else {
      return this.flights.get(index);
    }
  }

  /**
   * Gets a flight from the flight database given the flight number if it exists
   * then it returns that flight otherwise it returns null.
   *
   * @param flightNum the flight number of desired flight
   * @return the flight given flight number or null if it does not belong to the
   database
   */
  public Flight getFlight(String flightNum) {
    // the flight to return
    Flight flight = null;
    // find the user with the corresponding email if it exists
    for (Flight currFlight : this.flights) {
      if (flightNum.equals(currFlight.getFlightnum())) {
        flight = currFlight;
        return flight;
      }
    }
    return null;
  }

  /**
   * /** Returns an array list of all flights that depart from origin and arrive
   * at destination on the given date.
   *
   * @param origin        a flight origin.
   * @param destination   a flight destination.
   * @param departureDate a departure date (in the format YYYY-MM-DD).
   * @return an array list of all flights that depart from origin and arrive at
   destination on the given date.
   */
  public ArrayList<Flight> getFlights(String origin, String destination, String departureDate) {
    // create an array list to return
    ArrayList<Flight> flights = new ArrayList<Flight>();

    for (Flight flight : this.flights) {
      // extract only the date from the departure date and time field
      String date = flight.getDepartureDateTime().substring(0, 10);
      // check if the origins, destinations, and departure dates and times matches. If they
      // do, add it
      if (origin.equals(flight.getOrigin()) && destination.equals(flight.getDestination())
              && departureDate.equals(date)) {
        flights.add(flight);
      }
    }
    return flights;
  }

  /**
   * Returns an array list of all the flights in this database with the given
   * origin.
   *
   * @param origin the origin of the flight.
   * @return an array list of all the flights with the given origin.
   */
  public ArrayList<Flight> getFlightsOfOrigin(String origin) {
    // the array list to return
    ArrayList<Flight> flights = new ArrayList<Flight>();

    // find the flights that have the same origin
    for (Flight flight : this.flights) {
      if (origin.equals(flight.getOrigin())) {
        // add it to the list
        flights.add(flight);
      }
    }
    return flights;
  }

  /**
   * Returns an array list of all the flights in this database.
   *
   * @return an array list of all the flights in this database.
   */
  public ArrayList<Flight> getAllFlights() {
    return this.flights;
  }

  /**
   * Returns true if the flight is in this database, otherwise false.
   *
   * @param flightNum the flight number to check for the flight.
   * @return true if the flight is in this database, otherwise false.
   */
  @Override
  public boolean contains(String flightNum) {
    boolean result = false;
    // loop through the flights list and check if the flight number matches. Return true if it is,
    // otherwise false
    for (Flight flight : this.flights) {
      if (flightNum.equals(flight.getFlightnum())) {
        result = true;
        break;
      }
    }
    return result;
  }

  /**
   * Returns true if this database is empty, otherwise false.
   * @return true if this database is empty, otherwise false.
   */
  public boolean isEmpty() { return this.flights.isEmpty(); }

  /**
   * Returns the size of this database.
   *
   * @return the size of this database.
   */
  @Override
  public int size() {
    return this.flights.size();
  }

  /**
   * Saves the current state of this database by serializing it into a json
   * file. This method should be called after handling users in this database.
   */
  @Override
  public void save() {
    try {
      // get the file in the internal storage
      File flightsFile = this.getFlightsFile();
      // save the current state of this database by using gson to serialize
      FileWriter writer = new FileWriter(flightsFile);
      gson.toJson(this.flights, this.flightType, writer);
      // close the writer
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the previously saved state of this database by deserializing from a
   * json file.
   */
  @Override
  public void load() {
    try {
      File flightsFile = this.getFlightsFile();
      // use a buffered reader to read the json file
      BufferedReader reader = new BufferedReader(new FileReader(flightsFile));
      // restore this user database (deserialization)
      this.flights = gson.fromJson(reader, this.flightType);
      // close the reader
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the string representation of this database.
   *
   * @return the string representation of this database.
   */
  @Override
  public String toString() {
    String message = "[Flight Database]\n";
    for (Flight flight : this.flights) {
      message += flight.toString() + "\n";
    }
    return message;
  }
}
