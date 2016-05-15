package b07.cs.phase3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import backend.Admin;
import backend.Client;
import backend.User;
import databases.UserDatabase;

/**
 * This activity is Used to view all of the personal information of a user
 * The top shall have all of the personal information displayed
 * Underneath that will be three buttons where the third is hidden unless it is an admin is editing
 * a clients information.
 * The first will be to view the users booked itinerary
 * The second will be to edit the users personal information
 * The third is a search button to search for itineraries to book for a client
 * @author EdgarL -laiedgar
 */
public class ProfileActivity extends AppCompatActivity {

  private User user;
  private User clientUser;
  private boolean isAdmin;
  private UserDatabase userDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content_profile);

    // load the database
    this.userDatabase = new UserDatabase(this);

    // get the intent that launched me
    Intent intent = getIntent();
    // setting the TextView text to User info
    // get the user
    this.user = (User) intent.getSerializableExtra("userKey");
    this.user = this.userDatabase.getUser(this.user.getEmail());
    // check if this is an admin editing a clients info
    // if so then retrieve client that is being edited
    this.isAdmin = intent.hasExtra("clientKey");
    if (isAdmin) {
      this.clientUser = (User) intent.getSerializableExtra("clientKey");
      this.clientUser = this.userDatabase.getUser(this.clientUser.getEmail());

      // set the view as the client's
      TextView userInfo = (TextView) findViewById(R.id.user_info);
      userInfo.setText(this.clientUser.toString());

      // making search button only shown to admins mainly to add to clients itinerary
      Button searchButton = (Button) findViewById(R.id.search_button);
      searchButton.setVisibility(View.VISIBLE);

      // remind the admin that they are viewing a client's profile
      TextView resText = (TextView) findViewById(R.id.profile_result);
      resText.setText("You are an admin viewing a client's profile.");
    } else {
      // set the text view to the user's info
      TextView userInfo = (TextView) findViewById(R.id.user_info);
      userInfo.setText(this.user.toString());
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
   * Method to go to ViewBookedItineraryActivity if the view booked itinerary button is pressed.
   *
   * @param view - view of this activity
   */
  public void viewBookedItinerary(View view) {
    // Specifies the next Activity to move to: ViewBookedItineraryActivity.
    Intent newIntent = new Intent(this, ViewBookedItineraryActivity.class);
    // Passes the Client and Admin data to ViewBookedItineraryActivity.
    newIntent.putExtra("userKey", this.user);
    // if current user is an admin editing a client then pass the client in the intent
    if (isAdmin) {
      newIntent.putExtra("clientKey", this.clientUser);
    }
    startActivity(newIntent); // Starts ViewBookedItineraryActivity.
  }

  /**
   * Method to go to the EditPersonalInfoActivity if the edit personal info button is pressed.
   *
   * @param view - view of this activity
   */
  public void editPersonalInfo(View view) {
    // Specifies the next Activity to move to: EditPersonalInfoActivity.
    Intent newIntent = new Intent(this, EditPersonalInfoActivity.class);
    // Passes the String data to EditPersonalInfoActivity.
    newIntent.putExtra("userKey", this.user);
    // if current user is an admin editing a client then pass the client in the intent
    if (isAdmin) {
      newIntent.putExtra("clientKey", this.clientUser);
    }
    startActivity(newIntent); // Starts EditPersonalInfoActivity.
  }

  /**
   * Method to go to the SearchActivity if the search button is pressed.
   *
   * @param view - view of this activity
   */
  public void search(View view) {
    // Specifies the next Activity to move to: SearchActivity.
    Intent newIntent = new Intent(this, SearchActivity.class);
    // Passes the Client and Admin data to SearchActivity.
    newIntent.putExtra("userKey", this.user);
    newIntent.putExtra("clientKey", this.clientUser);
    startActivity(newIntent); // Starts SearchActivity.
  }
}
