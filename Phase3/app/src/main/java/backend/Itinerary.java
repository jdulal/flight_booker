package backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * An Itinerary that consists of one or more flights.
 *
 * @author Vinoshan Tharmalenkam
 */
public class Itinerary implements Serializable {

  private ArrayList<Flight> flights;

  /**
   * Creates a new Itinerary.
   */
  public Itinerary() {
    this.flights = new ArrayList<Flight>();
  }

  /**
   * Creates a new Itinerary given a list of flights.
   *
   * @param flights the list of flights.
   */
  public Itinerary(ArrayList<Flight> flights) {
    this.flights = new ArrayList<Flight>();
    this.flights.addAll(flights);
  }

  /**
   * Adds a flight to the itinerary.
   *
   * @param flight the flight being added to the itinerary.
   */
  public void addFlight(Flight flight) {
    this.flights.add(flight);
  }

  /**
   * Returns the number of flights in the itinerary.
   *
   * @return the number of flights in the itinerary.
   */
  public int numFlights() {
    return flights.size();
  }

  /**
   * Returns the total cost of the itinerary.
   *
   * @return the total cost of the itinerary.
   */
  public double getTotalCost() {
    double costs = 0;
    for (Flight flight : flights) {
      costs += flight.getCost();
    }
    return costs;
  }

  /**
   * Returns the total travel time of all flights in the itinerary in hours.
   *
   * @return the total travel time of all flights in the itinerary in hours.
   */
  public double totalTravelTime() {
    Calendar date1 = createNewCalendar(getFirstDepartureDateTime());
    Calendar date2 = createNewCalendar(getLastArrivalDateTime());
    // get the difference in time in milliseconds
    double timeDifference = date2.getTimeInMillis() - date1.getTimeInMillis();
    // convert to hours
    return timeDifference / (1000 * 60 * 60);

  }

  /**
   * Returns the starting origin of the itinerary.
   *
   * @return the starting origin of the itinerary.
   */
  public String getStartOrigin() {
    Flight firstFlight = flights.get(0);
    return firstFlight.getOrigin();
  }

  /**
   * Returns the final destination of the itinerary.
   *
   * @return the final destination of the itinerary.
   */
  public String getFinalDestination() {
    Flight lastFlight = flights.get(flights.size() - 1);
    return lastFlight.getDestination();
  }

  /**
   * Returns the first departure date and time in the itinerary.
   *
   * @return the first departure date and time in the itinerary.
   */
  public String getFirstDepartureDateTime() {
    Flight firstFlight = flights.get(0);
    return firstFlight.getDepartureDateTime();
  }

  /**
   * Returns the last arrival date and time in the itinerary.
   *
   * @return the last arrival date and time in the itinerary.
   */
  public String getLastArrivalDateTime() {
    Flight lastFlight = flights.get(flights.size() - 1);
    return lastFlight.getArrivalDateTime();
  }

  /**
   * Returns the first departure date in the itinerary.
   *
   * @return the first departure date in the itinerary.
   */
  public String getFirstDepartureDate() {
    Flight firstFlight = flights.get(0);
    return firstFlight.getDepartureDateTime().split(" ")[0];
  }

  /**
   * Returns the last arrival date in the itinerary.
   *
   * @return the last arrival date in the itinerary.
   */
  public String getLastArrivalDate() {
    Flight lastFlight = flights.get(flights.size() - 1);
    return lastFlight.getArrivalDateTime().split(" ")[0];
  }

  /**
   * Returns true if the itinerary is empty.
   *
   * @return true if the itinerary is empty.
   */
  public boolean is_empty() {
    return (this.flights.size() == 0);
  }

  /**
   * Returns the flights in this itinerary.
   *
   * @return the flights in this itinerary.
   */
  public ArrayList<Flight> getFlights() {
    return this.flights;
  }

  /**
   * Create a new calendar object given the date and time and return it.
   *
   * @param dateAndTime the date and time in the format YYYY-MM-DD hh:mm.
   */
  private Calendar createNewCalendar(String dateAndTime) {
    // initiate a calendar
    Calendar calendar = Calendar.getInstance();
    // take the given dateandTime string and split it into the different values
    String date = dateAndTime.split(" ")[0];
    String time = dateAndTime.split(" ")[1];
    int year = Integer.valueOf(date.split("-")[0]);
    int month = Integer.valueOf(date.split("-")[1]) - 1;
    int day = Integer.valueOf(date.split("-")[2]);
    int hour = Integer.valueOf(time.split(":")[0]);
    int min = Integer.valueOf(time.split(":")[1]);
    // set the calendar to the wanted values
    calendar.set(year, month, day, hour, min);
    return calendar;
  }

  /**
   * Returns true if consecutive flights in the itinerary has wait times between
   * 0 and 6 hours.
   *
   * @return true if consecutive flights in the itinerary has wait times between
   * 0 and 6 hours.
   */
  protected boolean hasValidWaitTimes() {
    // loop through all flights in the itinerary
    for (int i = 0; i < this.flights.size() - 1; i++) {
      // get the next 2 flights
      Flight flight1 = this.flights.get(i);
      Flight flight2 = this.flights.get(i + 1);
      // get the wait time between these 2 flights
      double waittime = flight1.getWaitTime(flight2);
      // if wait time is more than 6 (too long) or less than 0 (second flight already departed)
      // then the flights are invalid making the itinerary is invalid
      if ((waittime > 6) || (waittime < 0.5)) {
        return false;
      }
    }
    // otherwise all the flights are valid
    return true;
  }

  /**
   * Returns true if every flight in the itinerary has room.
   *
   * @return true if every flight in the itinerary has room.
   */
  protected boolean flightsHaveRoom() {
    // loop through all flights in the itinerary
    for (Flight flight : this.flights) {
      if (flight.isfull()) {
        // if even one flight is full then false
        return false;
      }
    }
    // otherwise all the flights have room
    return true;
  }

  /**
   * Book a seat in each flight of the itinerary.
   */
  public void bookFlights() {
    // loop through all flights in the itinerary
    for (Flight flight : this.flights) {
      flight.bookSeat();
    }
  }

  /**
   * Unbook a seat in each flight of the itinerary.
   */
  public void cancelFlights() {
    // loop through all flights in the itinerary
    for (Flight flight : this.flights) {
      flight.cancelSeat();
    }
  }

  /**
   * Returns the string representation of this itinerary.
   *
   * @return the string representation of this itinerary.
   */
  public String toString() {
    // format the first 6 elements of the itinerary
    String message = "";
    for (Flight flight : this.flights) {
      message += String.format("%s,%s,%s,%s,%s,%s\n", flight.getFlightnum(),
              flight.getDepartureDateTime(), flight.getArrivalDateTime(), flight.getAirline(),
              flight.getOrigin(), flight.getDestination());
    }
    //get the total time in format HH:MM
    Calendar date1 = createNewCalendar(getFirstDepartureDateTime());
    Calendar date2 = createNewCalendar(getLastArrivalDateTime());
    // get the difference in time in milliseconds
    long timeDifference = date2.getTimeInMillis() - date1.getTimeInMillis();
    // get the minutes
    long diffMinutes = timeDifference / (60 * 1000) % 60;
    // get the hours
    long diffHours = timeDifference / (60 * 60 * 1000);

    String hours = String.valueOf(diffHours);
    String minutes = String.valueOf(diffMinutes);
    if (hours.length() == 1) {
      hours = "0" + hours;
    }
    if (minutes.length() == 1) {
      minutes = "0" + minutes;
    }
    String time = hours + ":" + minutes;
    message += String.format("%.2f\n%s", this.getTotalCost(), time);

    return message;
  }

  /**
   * Determines if this itinerary is equal to the given itinerary.
   * @param itinerary the itinerary to compare.
   * @return true if both itineraries are equal.
   */
  public boolean equals(Itinerary itinerary) {
    if (this.flights.size() != itinerary.getFlights().size()) {
      return false;
    } else {
      for (int i = 0; i < this.flights.size(); i++) {
        if (!this.flights.get(i).equals(itinerary.getFlights().get(i))) {
          return false;
        }
      }
      return true;
    }
  }
}
