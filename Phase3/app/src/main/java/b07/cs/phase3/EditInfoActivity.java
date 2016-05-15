package b07.cs.phase3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import backend.Flight;
import backend.User;
import databases.FlightDatabase;
import databases.UserDatabase;

import java.util.ArrayList;

/**
 * This activity allows an admin to view and edit clients'/flights' info.
 * @author NikQ - quibinni
 */
public class EditInfoActivity extends AppCompatActivity {

  // the original user of this app
  private User user;
  // the databases
  private UserDatabase userDatabase;
  private FlightDatabase flightDatabase;
  // the clients and flights
  private ArrayList<User> clients;
  private ArrayList<Flight> flights;
  // group of radio buttons to select for edit
  private RadioButton[] radBtns;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_info);

    // load the databases
    this.userDatabase = new UserDatabase(this);
    this.flightDatabase = new FlightDatabase(this);
    // get the clients and flights
    this.clients = userDatabase.getAllClients();
    this.flights = flightDatabase.getAllFlights();
    // get the original user of this app
    Intent intent = getIntent();
    this.user = (User) intent.getSerializableExtra("userKey");
    // get user reference from the user database
    this.user = this.userDatabase.getUser(this.user.getEmail());
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
   * View all the clients that can be edited. Each client shown is a radio button which can be
   * selected for editing.
   *
   * @param view the view of this activity.
   */
  public void viewClients(View view) {
    // get the result text view for messages
    TextView resText = (TextView) findViewById(R.id.edit_info_result);
    // if there are no flights, show a message showing thus
    if (this.clients.isEmpty()) {
      resText.setText("No clients are available for editing.");
    } else {
      // get the radio group
      RadioGroup radGrp = (RadioGroup) findViewById(R.id.view_all);
      // remove present radio buttons (no multiple stacks of the same info)
      radGrp.removeAllViews();
      // create an array of radio buttons according to the number of clients
      this.radBtns = new RadioButton[this.clients.size()];
      // create and add a radio button to the radio group per client
      for (int i = 0; i < this.clients.size(); i++) {
        // create a btn
        this.radBtns[i] = new RadioButton(this);
        // set the id
        this.radBtns[i].setId(i);
        // set the text
        this.radBtns[i].setText(this.clients.get(i).toString());
        // add it to the radio group
        radGrp.addView(this.radBtns[i]);
      }
      // clear the default checked (sometimes selection is there but visually it doesn't show,
      // so we clear it first)
      radGrp.clearCheck();
      // select the first rad btn as the default
      radGrp.check(this.radBtns[0].getId());
      // make the edit button available now
      Button editBtn = (Button) findViewById(R.id.edit_info);
      editBtn.setEnabled(true);
    }
  }

  /**
   * View all the flights that can be edited. Each flight shown is a radio button which can be
   * selected for editing.
   *
   * @param view the view of this activity.
   */
  public void viewFlights(View view) {
    // get the result text view for messages
    TextView resText = (TextView) findViewById(R.id.edit_info_result);
    // if there are no flights, show a message showing thus
    if (this.flights.isEmpty()) {
      resText.setText("No flights are available for editing.");
    } else {
      // get the radio group
      RadioGroup radGrp = (RadioGroup) findViewById(R.id.view_all);
      // remove present radio buttons (no multiple stacks of the same info)
      radGrp.removeAllViews();
      // create an array of radio buttons according to the number of flights
      this.radBtns = new RadioButton[this.flights.size()];
      // create and add a radio button to the radio group per flight
      for (int i = 0; i < this.flights.size(); i++) {
        // create a btn
        this.radBtns[i] = new RadioButton(this);
        // set the id
        this.radBtns[i].setId(i);
        // set the text
        this.radBtns[i].setText(this.flights.get(i).toString());
        // add it to the radio group
        radGrp.addView(this.radBtns[i]);
      }
      // clear the default checked (sometimes selection is there but visually it doesn't show,
      // so we clear it first)
      radGrp.clearCheck();
      // select the first rad btn as the default
      radGrp.check(this.radBtns[0].getId());
      // make the edit button available now
      Button editBtn = (Button) findViewById(R.id.edit_info);
      editBtn.setEnabled(true);
    }
  }

  /**
   * Edit the selected item, either a client or flight. Starts a new activity depending on the
   * premise. If the clients radio button is selected, it launches the EditPersonalInfoActivity,
   * if not then it launches the EditFlightActivity.
   *
   * @param view the view of this activity.
   */
  public void editInfo(View view) {
    // get the radio group of the dynamically added rad btn
    RadioGroup radGrp = (RadioGroup) findViewById(R.id.view_all);
    // get any rad btn in the first group box
    RadioButton radClients = (RadioButton) findViewById(R.id.view_clients);
    // move to a new activity given the selected item
    if (radClients.isChecked()) {
      // create a new intent
      Intent newIntent = new Intent(this, ProfileActivity.class);
      // get the id of the selected btn
      int selected = radGrp.getCheckedRadioButtonId();
      // get the client based on the id
      User client = this.clients.get(selected);
      // put it into the intent and start the new activity
      newIntent.putExtra("clientKey", client);
      newIntent.putExtra("userKey", this.user);
      startActivity(newIntent);
    } else {
      // create a new intent
      Intent newIntent = new Intent(this, EditFlightActivity.class);
      // get the id of the selected btn
      int selected = radGrp.getCheckedRadioButtonId();
      // get the flight based on the id
      Flight flight = this.flights.get(selected);
      // put it into the intent and start the new activity
      newIntent.putExtra("flightKey", flight);
      newIntent.putExtra("userKey", this.user);
      startActivity(newIntent);
    }
  }
}
