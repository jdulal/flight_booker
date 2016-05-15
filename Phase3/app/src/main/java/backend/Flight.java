package backend;

import java.io.Serializable;
import java.util.Calendar;

/**
 * A Flight that contains flight information.
 *
 * @author Vinoshan Tharmalenkam
 */
public class Flight implements Serializable {

  private String flightnum;
  private String departureDateTime;
  private String arrivalDateTime;
  private String airline;
  private String origin;
  private String destination;
  private double traveltime;
  private double cost;
  private int numSeats;
  private int bookedSeats;

  /**
   * Creates a new flight given the flight number, departure date and time, arrival date and time,
   * airline, origin, destination and cost.
   *
   * @param flightnum         the unique flight number.
   * @param departureDateTime the date and time when the flight departures from origin.
   * @param arrivalDateTime   the date and time when the flight arrives at destination.
   * @param airline           the airline of the flight.
   * @param origin            the location where the flight departures from.
   * @param destination       the location where the flight arrives to.
   * @param cost              the cost of the flight.
   * @param numSeats          the number of seats left on the flight.
   */
  public Flight(String flightnum, String departureDateTime, String arrivalDateTime, String airline,
                String origin, String destination, double cost, int numSeats) {
    this.flightnum = flightnum;
    this.departureDateTime = departureDateTime;
    this.arrivalDateTime = arrivalDateTime;
    this.airline = airline;
    this.origin = origin;
    this.destination = destination;
    this.cost = cost;
    this.numSeats = numSeats;
    this.bookedSeats = 0;

    Calendar date1 = createNewCalendar(departureDateTime);
    Calendar date2 = createNewCalendar(arrivalDateTime);
    // get the difference in time in milliseconds
    double timeDifference = date2.getTimeInMillis() - date1.getTimeInMillis();
    // convert to hours
    this.traveltime = timeDifference / (1000 * 60 * 60);

  }

  /**
   * @return flightnum the flight number of the flight.
   */
  public String getFlightnum() {
    return flightnum;
  }

  /**
   * @param flightnum the flightnum to set.
   */
  public void setFlightnum(String flightnum) {
    this.flightnum = flightnum;
  }

  /**
   * @return departureDateTime the departure date and time of the flight.
   */
  public String getDepartureDateTime() {
    return departureDateTime;
  }

  /**
   * @param departureDateTime the departureDateTime to set.
   */
  public void setDepartureDateTime(String departureDateTime) {
    this.departureDateTime = departureDateTime;
  }

  /**
   * @return arrivalDateTime the arrival date and time of the flight.
   */
  public String getArrivalDateTime() {
    return arrivalDateTime;
  }

  /**
   * @param arrivalDateTime the arrivalDateTime to set.
   */
  public void setArrivalDateTime(String arrivalDateTime) {
    this.arrivalDateTime = arrivalDateTime;
  }

  /**
   * @return airline the airline of the flight.
   */
  public String getAirline() {
    return airline;
  }

  /**
   * @param airline the airline to set.
   */
  public void setAirline(String airline) {
    this.airline = airline;
  }

  /**
   * @return origin the origin of the flight.
   */
  public String getOrigin() {
    return origin;
  }

  /**
   * @param origin the origin to set.
   */
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  /**
   * @return destination the destination of the flight.
   */
  public String getDestination() {
    return destination;
  }

  /**
   * @param destination the destination to set.
   */
  public void setDestination(String destination) {
    this.destination = destination;
  }

  /**
   * @return traveltime the travel time of the flight.
   */
  public double getTraveltime() {
    return traveltime;
  }

  /**
   * @param traveltime the traveltime to set.
   */
  public void setTraveltime(double traveltime) {
    this.traveltime = traveltime;
  }

  /**
   * @return cost the cost of the flight.
   */
  public double getCost() {
    return cost;
  }

  /**
   * @param cost the cost to set.
   */
  public void setCost(double cost) {
    this.cost = cost;
  }

  /**
   * Returns the number of seats available of this flight.
   *
   * @return the number of seats available of this flight.
   */
  public int getNumSeats() {
    return numSeats;
  }

  /**
   * Sets the number of available seats on this flight.
   *
   * @param numSeats number of available seats.
   */
  public void setNumSeats(int numSeats) {
    this.numSeats = numSeats;
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
   * Returns the wait time between this flight and the given flight.
   *
   * @param flight2 the second flight
   * @return the wait time between this flight and the given flight.
   */
  public double getWaitTime(Flight flight2) {
    Calendar date1 = createNewCalendar(arrivalDateTime);
    Calendar date2 = createNewCalendar(flight2.getDepartureDateTime());
    // get the difference in time in milliseconds
    double timeDifference = date2.getTimeInMillis() - date1.getTimeInMillis();
    // convert to hours
    return timeDifference / (1000 * 60 * 60);
  }

  /**
   * Returns true if every flight in the itinerary has room.
   *
   * @return true if every flight in the itinerary has room.
   */
  protected boolean isfull() {
    return this.numSeats == this.bookedSeats;
  }

  /**
   * Book a seat in the flight.
   */
  public boolean bookSeat() {
    // check if the flight is full
    if (isfull()) {
      return false;
    } else {
      // otherwise increase the number of booked seats
      this.bookedSeats += 1;
      return true;
    }
  }

  /**
   * Cancel a seat in the flight.
   */
  public void cancelSeat() {
    this.bookedSeats = this.bookedSeats - 1;
  }

  /**
   * Returns the string representation of this flight.
   *
   * @return the string representation of this flight.
   */
  @Override
  public String toString() {
    String message = "";
    message += "Flight Number: " + this.getFlightnum() + "\n";
    message += "Departure Date and Time: " + this.getDepartureDateTime() + "\n";
    message += "Arrival Date and Time: " + this.getArrivalDateTime() + "\n";
    message += "Airline: " + this.getAirline() + "\n";
    message += "Origin: " + this.getOrigin() + "\n";
    message += "Destination: " + this.getDestination() + "\n";
    message += "Price: " + String.valueOf(this.getCost()) + "\n";
    message += "Number of Seats Available: " + String.valueOf(this.getNumSeats()) + "\n\n";
    return message;
  }

  /**
   * Checks whether this flight is equal to another flight by comparing their flight number.
   * @param flight the flight to compare.
   * @return true if the flights are the same.
   */
  public boolean equals(Flight flight) {
    return this.getFlightnum().equals(flight.getFlightnum());
  }
}
