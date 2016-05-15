package b07.cs.phase3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import backend.Admin;
import backend.User;
import databases.FlightDatabase;
import databases.UserDatabase;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * This activity uploads clients/flights information into the databases. It accepts files that
 * have the extension .csv or .txt. A preview of what was uploaded will be shown.
 * @author NikQ - quibinni
 */
public class UploadActivity extends AppCompatActivity {

  // the original user
  private User user;
  // the databases
  private UserDatabase userDatabase;
  private FlightDatabase flightDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_upload);

    // get the intent
    Intent intent = getIntent();

    // load the databases

    this.userDatabase = new UserDatabase(this);
    // get the user and its reference
    this.user = (User) intent.getSerializableExtra("userKey");
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
   * Checks whether the file contents can be added to the client database. Only checks the first
   * line. If the first line checks out to be valid, it assumes that the rest of the other lines
   * are valid.
   *
   * @param file the file to check.
   * @return true if the file contents can be added to the client database.
   */
  private boolean isValidClientFile(File file) {
    try {
      // get the first line in the file
      Scanner sc = new Scanner(file);
      String line = sc.nextLine();
      // split the string
      String[] info = line.split(",");
      // if there are six elements in the array then the file contents are valid
      return info.length == 6;

    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Checks whether the file contents can be added to the flight database. Only checks the first
   * line. If the first line checks out to be valid, it assumes that the rest of the other lines
   * are valid.
   *
   * @param file the file to check.
   * @return true if the file contents can be added to the flight database.
   */
  private boolean isValidFlightFile(File file) {
    try {
      // get the first line in the file
      Scanner sc = new Scanner(file);
      String line = sc.nextLine();
      // split the string
      String[] info = line.split(",");
      // if there are eight elements in the array then the file contents are valid
      return info.length == 8;

    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Uploads the file to the database depending on which radio button was clicked.
   * A result is shown depending if the file was uploaded successfully or an error occured.
   *
   * @param view the view of the current activity.
   */
  public void upload(View view) {
    // get the radio buttons
    RadioButton radClients = (RadioButton) findViewById(R.id.upload_clients);
    RadioButton radFlights = (RadioButton) findViewById(R.id.upload_flights);
    // get the text view results
    TextView resText = (TextView) findViewById(R.id.upload_result);
    // get text from the edit text
    EditText nameEdit = (EditText) findViewById(R.id.file_name);
    String fileName = nameEdit.getText().toString();
    // get the file path to the internal storage
    File userData = this.getApplicationContext().getDir("uploads", Context.MODE_PRIVATE);
    File uploadFile = new File(userData, fileName);

    // check where to upload the file by which rad button was checked and whether the file has
    // valid contents for adding to the database
    if (radClients.isChecked()) {
      if (uploadFile.exists()) {
        // check whether the file contents are valid
        if (!isValidClientFile(uploadFile)) {
          resText.setText("The file contents are not in a valid format.");
        } else {
          // add the new clients to the user database and show the recently uploaded
          // clients
          resText.setText(Admin.uploadClients(uploadFile));
        }
      } else {
        resText.setText("The file was not found.");
      }
    } else if (radFlights.isChecked()) {
      if (uploadFile.exists()) {
        // check whether the file contents are valid
        if (!isValidFlightFile(uploadFile)) {
          resText.setText("The file contents are not in a valid format.");
        } else {
          // add the new flights to the flight database and show the recently uploaded
          // flights
          resText.setText(Admin.uploadFlights(uploadFile));
        }
      } else {
        resText.setText("The file was not found.");
      }
    } else {
      resText.setText("Please pick an upload option.");
    }
  }
}
