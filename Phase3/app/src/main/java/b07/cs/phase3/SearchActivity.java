package b07.cs.phase3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import backend.Flight;
import backend.Itinerary;
import backend.User;
import databases.UserDatabase;

/**
 * This activity allows the admin users to edit flight activity. It can be accessed from the
 * EditInfo Activity. It contains the ability for admins to edit individual flights
 * Activated by the edit flight activity. Starts the edit flight info activity.
 *
 * @author vinoT - tharma55
 */
public class SearchActivity extends AppCompatActivity {
  // create private instances for intent, user, client, isAdmin and userDatabase
  private Intent intent;
  private User user;
  private User client;
  private boolean isAdmin = false;
  private UserDatabase userDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    // load the database
    this.userDatabase = new UserDatabase(this);
    // get the intent that launched me
    this.intent = getIntent();
    // get the user from the intent
    this.user = (User) this.intent.getSerializableExtra("userKey");
    this.user = this.userDatabase.getUser(this.user.getEmail());

    if (this.intent.hasExtra("clientKey")) {
      // we set isAdmin to true
      this.isAdmin = true;
      this.client = (User) intent.getSerializableExtra("clientKey");
      this.client = this.userDatabase.getUser(this.client.getEmail());
      // show the hidden button
      Button profile = (Button) findViewById(R.id.profile_search);
      profile.setVisibility(View.VISIBLE);
    }

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
   * This activity searches through our flight database and returns all possible flights that
   * match our origin to destination. It contains the ability to return individual flights which
   * are searched for. Activated by the MainMenuActivity.
   */
  public void searchFlights(View view) {
    // get the departure date that the user entered
    EditText input = (EditText) findViewById(R.id.edit_departure_date);
    String departure_date = input.getText().toString();
    // get the origin that the user entered
    input = (EditText) findViewById(R.id.edit_origin);
    String origin = input.getText().toString();
    // get the destination that the user entered
    input = (EditText) findViewById(R.id.edit_destination);
    String destination = input.getText().toString();

    TextView resText = (TextView) findViewById(R.id.invalid);
    if (this.isValidDate(departure_date) && !departure_date.isEmpty() && !origin.isEmpty()
            && !destination.isEmpty()) {
      ArrayList<Flight> flights = user.searchFlights(origin, destination, departure_date);
      if (flights.isEmpty()) {
        resText.setText("No flights were found according to your search preferences.");
      } else {
        Intent newIntent = new Intent(this, ViewFlightsActivity.class);
        newIntent.putExtra("flightsKey", flights);
        newIntent.putExtra("userKey", this.user);
        startActivity(newIntent);
      }
    } else {
      resText.setText("You must enter all the required information or date given is invalid.");
    }
  }

  /**
   * This activity searches through our flight database and returns all possible flights that
   * match our origin to destination. It also checks if there are any connecting flights if so
   * we add that to our itineraries. It contains the ability to search through itineraries
   * Activated by the MainMenuActivity. Differentiates Itinerary for either client or admin.
   *
   * @param view used to determine what the user searches for
   */
  public void searchItineraries(View view) {
    boolean result;
    // determine which user to search for
    Intent newIntent = new Intent(this, ViewItinerariesActivity.class);
    if (isAdmin) {
      result = this.searchItin(this.client);
      newIntent.putExtra("clientKey", this.client);
    } else {
      result = this.searchItin(this.user);
    }

    if(result) {
      newIntent.putExtra("userKey", this.user);
      startActivity(newIntent);
    }
  }

  /**
   * This activity searches through our flight database and returns all possible flights that
   * match our origin to destination dependent to client or admin.
   *
   * @param user a particular user whose we generate an itinerary for.
   * @return true if any itineraries were found.
   */
  private boolean searchItin(User user) {
    // get the departure date that the user entered
    EditText input = (EditText) findViewById(R.id.edit_departure_date);
    String departure_date = input.getText().toString();
    // get the origin that the user entered
    input = (EditText) findViewById(R.id.edit_origin);
    String origin = input.getText().toString();
    // get the destination that the user entered
    input = (EditText) findViewById(R.id.edit_destination);
    String destination = input.getText().toString();
    TextView resText = (TextView) findViewById(R.id.invalid);
    if (this.isValidDate(departure_date) && !departure_date.isEmpty() && !origin.isEmpty()
            && !destination.isEmpty()) {
      user.clearItineraries();
      user.getItineraries(departure_date, origin, destination);
      if (!user.getItineraryListAsList().isEmpty()) {
        this.userDatabase.save();
        return true;
      } else {
        resText.setText("No itineraries were found according to your search preferences.");
        return false;
      }
    } else {
      resText.setText("You must enter all the required information or date given is invalid.");
      return false;
    }
  }

  /**
   * Starts our SearchActivity.
   */
  public void profile(View view) {
    Intent newIntent = new Intent(this, ProfileActivity.class);
    newIntent.putExtra("userKey", this.user);
    newIntent.putExtra("clientKey", this.client);
    startActivity(newIntent);
  }

  /**
   * Check if the date format is correct.
   *
   * @param date the date to check.
   * @return true if the date is valid, otherwise false.
   */
  private boolean isValidDate(String date) {
    if (date.length() != 10) {
      return false;
    } else {
      // if date isn't in the format YYYY-MM-DD, return false
      String validChars = "1234567890";
      for (int i = 0; i < date.length(); i++) {
        if (i == 4 || i == 7) {
          if (date.charAt(i) != '-') {
            return false;
          }
        } else {
          if (!validChars.contains(Character.toString(date.charAt(i)))) {
            return false;
          }
        }
      }
    }
    // get the month and date in integer form
    int month = Integer.valueOf(date.split("-")[1]);
    int day = Integer.valueOf(date.split("-")[2]);

    // return false if month or day is invalid
    if (month > 12 || month <= 0) {
      return false;
    }
    if (day > 30 || day <= 0) {
      return false;
    }
    return true;
  }
}
