package b07.cs.phase3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import backend.Flight;
import backend.User;

/**
 * This activity allows all users to view flights. It can be accessed from the
 * search activity. Activated by the search activity. Starts the edit flight info activity.
 * @author varS - sharm559
 */
public class ViewFlightsActivity extends AppCompatActivity {
  // create private instances for intent, flights and user
  private Intent intent;
  private ArrayList<Flight> flights;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_flights);

    // get the intent that launched me
    this.intent = getIntent();
    // get the flights
    this.flights = (ArrayList<Flight>) this.intent.getSerializableExtra("flightsKey");
    // get the original user
    this.user = (User) this.intent.getSerializableExtra("userKey");
    // number each flight from 1 to n flight
    int i = 1;
    String results = "";
    for (Flight flight : this.flights) {
      results += i + ". " + flight;
      i += 1;
    }
    // set the text view to the user's info
    TextView flightInfo = (TextView) findViewById(R.id.flight_info);
    // display the flight information on the activity
    flightInfo.setText(results);
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

}
