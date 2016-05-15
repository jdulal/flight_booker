package b07.cs.phase3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import backend.Itinerary;
import backend.User;

/**
 * This activity is Used to view the currently booked itineraries of a user
 * If it is just the user(admin or client) then it shows only their own booked itineraries
 * If it is an admin editing a client then it shows the client's book itineraries
 * If any of the booked itineraries are clicked then the screen is changed to a more detailed
 * description of that itinerary
 * @author EdgarL -laiedgar
 */
public class ViewBookedItineraryActivity extends AppCompatActivity {

  private Intent intent;
  private User user;
  private User clientUser;
  private boolean isAdmin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content_view_booked_itinerary);

    // get the intent that launched me
    this.intent = getIntent();
    // setting the TextView text to User info
    // get the user
    this.user = (User) this.intent.getSerializableExtra("userKey");
    // check if this is an admin editing a clients info
    // if so then retrieve client that is being edited
    isAdmin = this.intent.hasExtra("clientKey");
    if(isAdmin) {
      this.clientUser = (User) this.intent.getSerializableExtra("clientKey");
    }
    // variable for string details of booked itineraries
    ArrayList<Itinerary> bookedItins = new ArrayList<>();
    // if admin is editing a client
    if(isAdmin) {
      // loop to get all the strings in booked itineraries in client user
      for(Itinerary strBookedItin : this.clientUser.getBookedItineraryListAsList()) {
        bookedItins.add(strBookedItin);
      }
    } else { // otherwise this is not an admin editing a client
      // loop to get all the strings in booked itineraries in current user
      for (Itinerary strBookedItin : this.user.getBookedItineraryListAsList()) {
        bookedItins.add(strBookedItin);
      }
    }

    // use adapter to convert to list view
    ListAdapter bookAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookedItins);
    // reference list view
    ListView bookItin = (ListView) findViewById(R.id.view_booked_itinerary);
    bookItin.setAdapter(bookAdapter);

    // Goes to new activity when something on list is clicked
    bookItin.setOnItemClickListener(onListClick);
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
   * Private instance of an AdapterView named onListClick with it's own method onItemClick.
   * This private instance is used to tell what item in the list view has been clicked and sends
   * that item with user(s) to detailedItineraryActivity.
   */
  private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
    private Itinerary currBookedItinerary;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      System.out.println(parent.getItemAtPosition(position));
      this.currBookedItinerary = (Itinerary)parent.getItemAtPosition(position);
      // Specifies the next Activity to move to: DetailedItinerary.
      Intent newIntent = new Intent(ViewBookedItineraryActivity.this,
              DetailedItineraryActivity.class);
      // Passes the user and booked Itinerary data to DetailedItineraryActivity.
      newIntent.putExtra("userKey", user);
      newIntent.putExtra("bookedItinKey", currBookedItinerary);
      // if current user is an admin editing a client then pass the client in the intent
      if(isAdmin) {
        newIntent.putExtra("clientKey", clientUser);
      }
      startActivity(newIntent); // start DetailedItineraryActivity
    }
  };
}
