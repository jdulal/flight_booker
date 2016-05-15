package b07.cs.phase3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import backend.Itinerary;
import backend.User;
import databases.FlightDatabase;
import databases.UserDatabase;

/**
 * This activity is Used to view a booked itinerary of a User in more detail
 * The bottom of the Itinerary will have a button to unbook this itinerary.
 * When clicked the Itinerary will be unbooked and will go back to ViewBookedItineraryActiviy
 * @author EdgarL -laiedgar
 */
public class DetailedItineraryActivity extends AppCompatActivity {

  private Intent intent;
  private User user;
  private User clientUser;
  private UserDatabase userDatabase;
  private FlightDatabase flightDatabase;
  private Itinerary currBookedItin;
  private boolean isAdmin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content_detailed_itinerary);

    // get the intent that launched me
    this.intent = getIntent();
    // setting the TextView text to User info
    // get the user
    this.user = (User) this.intent.getSerializableExtra("userKey");
    // load the flight database
    this.flightDatabase = new FlightDatabase(this);
    // make user database
    this.userDatabase = new UserDatabase(this);
    this.user = this.userDatabase.getUser(this.user.getEmail());
    // check if this is an admin editing a clients info
    // if so then retrieve client that is being edited
    isAdmin = this.intent.hasExtra("clientKey");
    if (isAdmin) {
      this.clientUser = (User) this.intent.getSerializableExtra("clientKey");
      this.clientUser = this.userDatabase.getUser(this.clientUser.getEmail());
    }
    // get the current booked itinerary
    this.currBookedItin = (Itinerary) this.intent.getSerializableExtra("bookedItinKey");

    // text to display origin of itinerary
    TextView originText = (TextView) findViewById(R.id.origin);
    originText.setText(this.currBookedItin.getStartOrigin());
    // text to display destination of itinerary
    TextView destinationText = (TextView) findViewById(R.id.destination);
    destinationText.setText(this.currBookedItin.getFinalDestination());
    // text to display total_time of itinerary
    TextView totalTimeText = (TextView) findViewById(R.id.total_time);
    totalTimeText.setText(String.valueOf(this.currBookedItin.totalTravelTime()));
    // text to display departure_date of itinerary
    TextView departureDateText = (TextView) findViewById(R.id.departure_date);
    departureDateText.setText(this.currBookedItin.getFirstDepartureDateTime());
    // text to display arrival_date of itinerary
    TextView arrivalDateText = (TextView) findViewById(R.id.arrival_date);
    arrivalDateText.setText(this.currBookedItin.getLastArrivalDateTime());
    // text to display total_cost of itinerary
    TextView totalCostText = (TextView) findViewById(R.id.total_cost);
    totalCostText.setText(String.valueOf(this.currBookedItin.getTotalCost()));
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
   * Method to remove the Itinerary from Users bookedItineraries.
   *
   * @param view - view of this activity
   */
  public void removeItinerary(View view) {
    // Specifies the next Activity to move to: ViewBookedItineraryActivity.
    Intent newIntent = new Intent(this, ViewBookedItineraryActivity.class);
    // Passes user to ViewBookedItineraryActivity.
    newIntent.putExtra("userKey", this.user);
    // if user is an admin that is editing then also pass the client that is being edited
    if (isAdmin) {
      // cancels Itinerary in Client
      this.clientUser.cancelItinerary(currBookedItin);
      // save to database so that itinerary has been removed from users booked itineraries
      this.userDatabase.save();
      newIntent.putExtra("clientKey", this.clientUser);
    } else { // otherwise
      // cancels Itinerary in current user
      this.user.cancelItinerary(currBookedItin);
      // save to database so that itinerary has been removed from users booked itineraries
      this.userDatabase.save();
      this.flightDatabase.save();
    }
    startActivity(newIntent); // Starts ViewBookedItineraryActivity.
  }
}
