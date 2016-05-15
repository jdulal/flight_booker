package b07.cs.phase3;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.ArrayList;

import backend.Flight;
import backend.Itinerary;
import backend.User;
import databases.FlightDatabase;
import databases.UserDatabase;

/**
 * This activity displays the different itineraries that are created from SearchActivity. It
 * allows you to view each itinerary individually. Activated by the Search Acitvity.
 * @author vinoT - tharma55
 */
public class ViewItinerariesActivity extends AppCompatActivity {

  private Intent intent;
  private User user;
  private User client;
  private boolean isAdmin = false;
  private UserDatabase userDatabase;
  private FlightDatabase flightDatabase;
  private ArrayList<Itinerary> itineraries;
  private ArrayList<CheckBox> cboxes;
  private RelativeLayout myLayout;
  private Intent newIntent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.flightDatabase = new FlightDatabase(this);
    this.userDatabase = new UserDatabase(this);
    newIntent = new Intent(this, SearchActivity.class);
    // get the intent that launched me
    this.intent = getIntent();
    // get the user from the intent
    this.user = (User) this.intent.getSerializableExtra("userKey");
    this.user = this.userDatabase.getUser(this.user.getEmail());
    // check if there is a client
    if (intent.hasExtra("clientKey")) {
      this.isAdmin = true;
      this.client = (User) this.intent.getSerializableExtra("clientKey");
      this.client = this.userDatabase.getUser(this.client.getEmail());
      this.itineraries = this.client.getItineraryListAsList();
    } else {
      //get the itineraries searched by the user
      itineraries = user.getItineraryListAsList();
    }

    //create a new scroll view
    final ScrollView sv = new ScrollView(this);

    myLayout = new RelativeLayout(this);
    // add the background
    myLayout.setBackgroundResource(R.drawable.activitybg);
    sv.addView(myLayout);

    //create a spinner for the drop down menu
    Spinner spinner = new Spinner(this);
    spinner.setId(0);
    ArrayList<String> spinnerArray = new ArrayList<String>();
    spinnerArray.add("Sort");
    spinnerArray.add("by Cost");
    spinnerArray.add("by Time");
    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(spinnerArrayAdapter);
    RelativeLayout.LayoutParams spinnerParams =
            new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
    spinnerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
    // add spinner to the new layout
    myLayout.addView(spinner, spinnerParams);

    // keep track of all the checkboxes by keeping them in a list
    cboxes = new ArrayList<>();
    // for each itinerary, create a new check box
    for (int i = 0; i < itineraries.size(); i++) {
      CheckBox cbox = new CheckBox(this);
      cbox.setText(itineraries.get(i).toString());
      cbox.setId(i + 1);
      cboxes.add(cbox);
      RelativeLayout.LayoutParams buttonParams =
              new RelativeLayout.LayoutParams(
                      RelativeLayout.LayoutParams.WRAP_CONTENT,
                      RelativeLayout.LayoutParams.WRAP_CONTENT);
      //set the layout so each check box is below the previous one
      if (i == 0) {
        buttonParams.addRule(RelativeLayout.BELOW, spinner.getId());
        buttonParams.setMargins(0, 50, 0, 0);
      }
      if (i != 0) {
        buttonParams.addRule(RelativeLayout.BELOW, cboxes.get(i - 1).getId());
        buttonParams.setMargins(0, 50, 0, 0);
      }
      myLayout.addView(cbox, buttonParams);

    }

    // create a new button
    Button button = new Button(this);
    button.setText("Book Itineraries");
    button.setId(cboxes.size() + 1);
    RelativeLayout.LayoutParams buttonParams =
            new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
    buttonParams.addRule(RelativeLayout.BELOW, cboxes.get(cboxes.size() - 1).getId());
    buttonParams.setMargins(0, 50, 0, 0);
    myLayout.addView(button, buttonParams);

    setContentView(sv);

    // create a new listener for the spinner object for user input
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                 long myID) {
        String item = parentView.getItemAtPosition(position).toString();
        if (item == "by Cost") {
          if (isAdmin) {
            client.sortByCost();
            itineraries = client.getItineraryListAsList();
          } else {
            user.sortByCost();
            itineraries = user.getItineraryListAsList();
          }

          updateDisplay(itineraries);
        } else if (item == "by Time") {
          if (isAdmin) {
            client.sortByTime();
            itineraries = client.getItineraryListAsList();
          } else {
            user.sortByTime();
            itineraries = user.getItineraryListAsList();
          }

          updateDisplay(itineraries);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parentView) {
      }
    });

    // create a new listener for the button object for user input
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int count = 0;
        // check which check boxes were checked
        for (CheckBox cbox : cboxes) {
          // if checked make the user book the corresponding itinerary
          if (cbox.isChecked()) {
            if (isAdmin) {
              client.bookItinerary(itineraries.get(count));
            } else {
              user.bookItinerary(itineraries.get(count));
            }
          }
          count += 1;
        }
        flightDatabase.save();
        userDatabase.save();
        // go back to the search activity
        newIntent.putExtra("userKey", user);
        if (isAdmin) {
          newIntent.putExtra("clientKey", client);
        }
        startActivity(newIntent);
      }
    });
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
   * This activity displays the different itineraries that are created from SearchActivity. It
   * allows you to view each itinerary individually. Activated by the Search Acitvity.
   * @param itineraries all itineraries for which the entered trip is possible.
   */
  public void updateDisplay(ArrayList<Itinerary> itineraries) {
    int count = 0;
    // for each checkbox, change text so it follows order of the itineraries in the list
    for (CheckBox cbox : cboxes) {
      // remove the check box to change the text
      myLayout.removeView(cbox);
      cbox.setText(itineraries.get(count).toString());
      // add the check box back
      myLayout.addView(cbox);
      count += 1;
    }
  }
}
