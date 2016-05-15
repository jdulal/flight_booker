package b07.cs.phase3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import backend.Flight;
import backend.User;
import databases.FlightDatabase;

/**
 * This activity allows the admin users to edit flights. It can be accessed from the
 * EditInfo Activity. It contains the ability for admins to edit individual flights
 * Activated by the edit flight activity. Starts the edit flight info activity.
 * @author varS - sharm559
 */
public class EditFlightActivity extends AppCompatActivity {

  // create private instances for flight and flightDatabase and user
  private Flight flight;
  private FlightDatabase flightDatabase;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_flight);

    // get the intent that launched me
    Intent intent = getIntent();
    // set the flight database with this content
    this.flightDatabase = new FlightDatabase(this);
    // get the extras
    this.flight = (Flight) intent.getSerializableExtra("flightKey");
    this.user = (User) intent.getSerializableExtra("userKey");
    // get the flight reference from the database
    this.flight = this.flightDatabase.getFlight(this.flight.getFlightnum());

    // get the edittext and change it to the user's info
    EditText newText = (EditText) findViewById(R.id.edit_flight_number);
    newText.setText(this.flight.getFlightnum());
    // This will place the flight's number from our Flight class into our text field for editing.
    newText = (EditText) findViewById(R.id.edit_departure_date_time);
    newText.setText(this.flight.getDepartureDateTime());
    // This will place the flight's departure date and time from our Flight class into our text
    // field for editing.
    newText = (EditText) findViewById(R.id.edit_arrival_date_time);
    newText.setText(this.flight.getArrivalDateTime());
    // This will place the flight's arrival time and date from our Flight class into our text
    // field for editing.
    newText = (EditText) findViewById(R.id.edit_airline_name);
    newText.setText(this.flight.getAirline());
    // This will place the flight's arline name from our Flight class into our text field for
    // editing.
    newText = (EditText) findViewById(R.id.edit_origin);
    newText.setText(this.flight.getOrigin());
    // This will place the flight's origin from our Flight class into our text field for
    // editing.
    newText = (EditText) findViewById(R.id.edit_destination);
    newText.setText(this.flight.getDestination());
    // This will place the flight's destination from our Flight class into our text field for
    // editing.
    newText = (EditText) findViewById(R.id.edit_cost);
    newText.setText(String.valueOf((this.flight.getCost())));
    // This will place the flight's cost from our Flight class into our text field for editing.
    newText = (EditText) findViewById(R.id.edit_travel_time);
    newText.setText(String.valueOf(this.flight.getTraveltime()));
    // This will place the flight's travel time from our Flight class into our text field for
    // editing.
    newText = (EditText) findViewById(R.id.edit_seats_available);
    newText.setText(String.valueOf(this.flight.getNumSeats()));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_staple, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.menu_logout) {
      // create an intent and start the login activity
      Intent intent = new Intent(this, LoginActivity.class);
      startActivity(intent);
    } else if (id == R.id.menu_home) {
      // create an intent and start the main menu activity
      Intent intent = new Intent(this, MainMenuActivity.class);
      // add the original user to the intent
      intent.putExtra("userKey", this.user);
      startActivity(intent);
    }

    return super.onOptionsItemSelected(item);
  }
  /**
   * Activated by the change flight activity button. Edits flight info activity.
   *
   *    We replace an empty field with our new flight information.
   *    If there exists information we replace is with our new flight information.
   *    @param view the view of the section
   */
  public void changeFlightInfo(View view) {
    // we create a EditText to individually edit the flight number field of EditFlightActivity
    EditText editInfo = (EditText) findViewById(R.id.edit_flight_number);
    // we convert our editInfo into a string
    String newInfo = editInfo.getText().toString();
    // if the field is empty we automatically replace it with our new flight number
    if (!newInfo.isEmpty()) {
      this.flight.setFlightnum(newInfo);
    }
    // we create a EditText to individually edit the departure date and time field of
    // EditFlightActivity
    editInfo = (EditText) findViewById(R.id.edit_departure_date_time);
    newInfo = editInfo.getText().toString();
    // if the field is empty we automatically replace it with our new departure time
    if (!newInfo.isEmpty()) {
      this.flight.setDepartureDateTime(newInfo);
    }
    // we create a EditText to individually edit the arrival date and time field of
    // EditFlightActivity
    editInfo = (EditText) findViewById(R.id.edit_arrival_date_time);
    newInfo = editInfo.getText().toString();
    // if the field is empty we automatically replace it with our new arrival date time
    if (!newInfo.isEmpty()) {
      this.flight.setArrivalDateTime(newInfo);
    }
    editInfo = (EditText) findViewById(R.id.edit_airline_name);
    newInfo = editInfo.getText().toString();
    // if the field is empty we automatically replace it with our new airline name
    if (!newInfo.isEmpty()) {
      this.flight.setAirline(newInfo);
    }
    // we create a EditText to individually edit the origin field of EditFlightActivity
    editInfo = (EditText) findViewById(R.id.edit_origin);
    newInfo = editInfo.getText().toString();
    // if the field is empty we automatically replace it with our new origin
    if (!newInfo.isEmpty()) {
      this.flight.setOrigin(newInfo);
    }
    // we create a EditText to individually edit the destination field of EditFlightActivity
    editInfo = (EditText) findViewById(R.id.edit_destination);
    newInfo = editInfo.getText().toString();
    // if the field is empty we automatically replace it with our new destination
    if (!newInfo.isEmpty()) {
      this.flight.setDestination(newInfo);
    }
    // we create a EditText to individually edit the cost field of EditFlightActivity
    editInfo = (EditText) findViewById(R.id.edit_cost);
    newInfo = editInfo.getText().toString();
    // if the field is empty we automatically replace it with our new cost
    if (!newInfo.isEmpty()) {
      this.flight.setCost(Double.valueOf(newInfo));
    }
    // we create a EditText to individually edit the travel time field of EditFlightActivity
    editInfo = (EditText) findViewById(R.id.edit_travel_time);
    newInfo = editInfo.getText().toString();
    // if the field is empty we automatically replace it with our new travel time
    if (!newInfo.isEmpty()) {
      this.flight.setTraveltime(Double.parseDouble(newInfo));
    }
    // we create a EditText to individually edit the number of seats available field of
    // EditFlightActivity
    editInfo = (EditText) findViewById(R.id.edit_seats_available);
    newInfo = editInfo.getText().toString();
    // if the field is empty we automatically replace it with our new number of seats
    if (!newInfo.isEmpty()) {
      this.flight.setNumSeats(Integer.parseInt(newInfo));
    }

    this.flightDatabase.save();

    Intent newIntent = new Intent(this, EditInfoActivity.class);
    newIntent.putExtra("userKey", this.user);
    startActivity(newIntent);
  }
}
