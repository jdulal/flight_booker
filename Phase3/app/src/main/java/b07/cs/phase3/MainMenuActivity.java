package b07.cs.phase3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import backend.User;
import databases.UserDatabase;

/**
 * This activity is the hub of the app. It is launched by the home menu item and also by the login
 * activity. It contains the ability for clients to search flights and itineraries, and also to
 * check their profile. For admins, there are extra buttons such as uploading information and
 * editing clients'/flights' info.
 * @author NikQ - quibinni
 */
public class MainMenuActivity extends AppCompatActivity {

  // the original user of the app
  private User user;
  // the user database
  private UserDatabase userDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_menu);

    // get the intent
    Intent intent = getIntent();
    // load the database
    this.userDatabase = new UserDatabase(this);
    // get the user from the intent and load its reference from the database
    this.user = (User) intent.getSerializableExtra("userKey");
    this.user = this.userDatabase.getUser(this.user.getEmail());

    // create a status message for the user
    String status = "";
    // get the text view to show the message
    TextView statusText = (TextView) findViewById(R.id.user_status);
    status = String.format("%s, %s", this.user.getLastName(), this.user.getFirstName());
    // add an admin string if they are an admin
    if (this.user.isAdmin()) {
      status = "Admin: " + status;
    }
    statusText.setText(status);

    // show the admin usable buttons if they are an admin
    if (this.user.isAdmin()) {
      // get the texts and buttons
      TextView uploadText = (TextView) findViewById(R.id.upload_main_text);
      TextView editText = (TextView) findViewById(R.id.edit_main_text);
      Button uploadBtn = (Button) findViewById(R.id.upload_main);
      Button editBtn = (Button) findViewById(R.id.edit_main);
      // show their visibility
      uploadText.setVisibility(View.VISIBLE);
      editText.setVisibility(View.VISIBLE);
      uploadBtn.setVisibility(View.VISIBLE);
      editBtn.setVisibility(View.VISIBLE);
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
   * Activated by the search button. Starts the SearchActivity.
   *
   * @param view the view of this activity.
   */
  public void search(View view) {
    // create a new intent and start the search activity with the user as an extra
    Intent intent = new Intent(this, SearchActivity.class);
    intent.putExtra("userKey", this.user);
    startActivity(intent);
  }

  /**
   * Activated by the profile button. Starts the ProfileActivity.
   * @param view the view of this activity.
   */
  public void profile(View view) {
    // create a new intent and start the profile activity with the user as an extra
    Intent intent = new Intent(this, ProfileActivity.class);
    intent.putExtra("userKey", this.user);
    startActivity(intent);
  }

  /**
   * Activated by the upload button. Starts the UploadActivity.
   *
   * @param view the view of this activity.
   */
  public void upload(View view) {
    // create a new intent and start the upload activity with the user as an extra
    Intent intent = new Intent(this, UploadActivity.class);
    intent.putExtra("userKey", this.user);
    startActivity(intent);
  }

  /**
   * Activated by the edit info button. Starts the edit info activity.
   *
   * @param view the view of this activity.
   */
  public void editInfo(View view) {
    // create a new intent and start the edit info activity with the user as an extra.
    Intent intent = new Intent(this, EditInfoActivity.class);
    intent.putExtra("userKey", this.user);
    startActivity(intent);
  }
}
