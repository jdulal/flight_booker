package b07.cs.phase3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import backend.Admin;
import backend.User;
import databases.FlightDatabase;
import databases.UserDatabase;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * The login activity is launched first. This activity authenticates the user of this app. The user
 * must provide their username (which is their email) and their password to access the rest of the
 * app.
 *
 * @author NikQ - quibinni
 */
public class LoginActivity extends AppCompatActivity {

  // store the databases
  private FlightDatabase flightDatabase;
  private UserDatabase userDatabase;
  // the line number to match the usernames.txt and passwords.txt
  private int lineNum = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // load the databases
    this.flightDatabase = new FlightDatabase(this);
    this.userDatabase = new UserDatabase(this);

    // add admins on a fresh install
    this.addAdmins();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_login, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Checks if the email provided by the user is in this app's records. Returns true if it is,
   * otherwise false.
   *
   * @param email the email to check.
   * @return true if the email is in this app's records, otherwise false.
   */
  private boolean checkEmail(String email) {
    boolean result = false;

    // determine if the email is in the records.
    try {
      File userData = this.getApplicationContext().getDir("records_data", MODE_PRIVATE);
      File usernames = new File(userData, "usernames.txt");

      Scanner sc = new Scanner(usernames);

      while (sc.hasNext()) {
        if (email.equals(sc.nextLine())) {
          result = true;
          break;
        } else {
          // increment the line number
          this.lineNum += 1;
        }
      }

      sc.close();
      return result;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Checks if the password provided by the user is in this app's records. Returns true if it is,
   * otherwise false.
   *
   * @param password the password to check.
   * @return true if the password is in this app's records, otherwise false.
   */
  private boolean checkPassword(String password) {
    boolean result = false;

    // determine if the password is in the records
    try {
      File userData = this.getApplicationContext().getDir("records_data", MODE_PRIVATE);
      File passwords = new File(userData, "passwords.txt");

      Scanner sc = new Scanner(passwords);

      // current line to match the line number
      int currLineNum = 1;

      while (sc.hasNext()) {
        if (password.equals(sc.nextLine())) {
          result = true;
          break;
        } else if (currLineNum > this.lineNum) {
          // must mean that the user is not valid
          result = false;
          break;
        } else {
          currLineNum += 1;
        }
      }

      sc.close();
      return result;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Authenticates the user's credential by checking if the user is in this app's records.
   * If the user is valid, then it will move onto the MainMenuActivity.
   *
   * @param view the view of this activity.
   */
  public void authenticate(View view) {
    // get the views and its contents
    EditText userText = (EditText) findViewById(R.id.username);
    EditText passText = (EditText) findViewById(R.id.password);
    String email = userText.getText().toString();
    String password = passText.getText().toString();

    if (checkEmail(email) && checkPassword(password)) {
      // create an intent
      Intent intent = new Intent(this, MainMenuActivity.class);
      // get the user from the database
      User user = this.userDatabase.getUser(email);
      //user.clearBookedItineraries();
      //this.userDatabase.save();
      // start the new activity
      intent.putExtra("userKey", user);
      startActivity(intent);
    } else {
      // provide the user with an error message
      TextView resText = (TextView) findViewById(R.id.login_result);
      resText.setText("Username or password does not exist!");
    }
  }

  /**
   * Add admins to the user database. This method is called to check if it is a fresh install.
   * @return true if admins were added, otherwise false.
   */
  private boolean addAdmins() {
    boolean result = false;
    // check that the user database is empty
    if (this.userDatabase.isEmpty()) {
      // get the path to the internal storage
      File userData = this.getApplicationContext().getDir("uploads", MODE_PRIVATE);
      File adminsFile = new File(userData, "admins.txt");
      // add the admins to the user database
      Admin.uploadAdmins(adminsFile);
      // load the database to reflect the changes and save it
      this.userDatabase.load();
      this.userDatabase.save();
      result = true;
    } else {
      result = false;
    }
    return result;
  }
}
