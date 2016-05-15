package b07.cs.phase3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import backend.User;
import databases.UserDatabase;

import java.util.ArrayList;

/**
 * This activity is for editing the user's or an admin editing another client's information.
 * @author NikQ - quibinni
 */
public class EditPersonalInfoActivity extends AppCompatActivity {

  // the user from the intent
  private User user;
  // client if it exists
  private User client;
  // load the user database
  private UserDatabase userDatabase;
  // an array list to store the extras (will only be users)
  private ArrayList<User> userArr = new ArrayList<>();
  // currently selected user
  private int selected = 0;
  // situaton
  private boolean thereIsClient = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_personal_info);

    // set the client and admin database with this context
    this.userDatabase = new UserDatabase(this);

    // get the intent that launched me
    Intent intent = getIntent();
    // get the user from the intent
    this.user = (User) intent.getSerializableExtra("userKey");
    // get the user from the database
    this.user = this.userDatabase.getUser(this.user.getEmail());

    // fill the array with the extras
    this.userArr.add(this.user);

    // check whether this situation is an admin editing a client's info
    if (intent.hasExtra("clientKey")) {
      this.thereIsClient = true;
      this.selected = 1;
      // get the client from the database and add it
      this.client = (User) intent.getSerializableExtra("clientKey");
      this.client = this.userDatabase.getUser(this.client.getEmail());
      this.userArr.add(this.client);
      // show a message that informs the admin that they are changing a client's info
      TextView resText = (TextView) findViewById(R.id.edit_personal_result);
      resText.setText(String.format("You are an Admin. You are about to edit %s %s personal info.",
              this.client.getFirstName(), this.client.getLastName()));
    }

    // get the edittext and change it to the currently selected user
    EditText newText = (EditText) findViewById(R.id.edit_first_name);
    newText.setText(this.userArr.get(selected).getFirstName());
    newText = (EditText) findViewById(R.id.edit_last_name);
    newText.setText(this.userArr.get(selected).getLastName());
    newText = (EditText) findViewById(R.id.edit_email);
    newText.setText(this.userArr.get(selected).getEmail());
    newText = (EditText) findViewById(R.id.edit_address);
    newText.setText(this.userArr.get(selected).getAddress());
    newText = (EditText) findViewById(R.id.edit_credit_card_number);
    newText.setText(this.userArr.get(selected).getCreditCardNumber());
    newText = (EditText) findViewById(R.id.edit_expiry_date);
    newText.setText(this.userArr.get(selected).getExpiryDate());
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
   * Changes the info of the user or client, depending on the current situation. The changes will
   * be reflected in the user database.
   *
   * @param view the view of this activity.
   */
  public void changeInfo(View view) {
    // edit and save the changes of the user's/client's info
    if (this.thereIsClient) {
      this.selected = 1;
    } else {
      this.selected = 0;
    }

    // edit and save
    EditText editInfo = (EditText) findViewById(R.id.edit_first_name);
    String newInfo = editInfo.getText().toString();
    if (!newInfo.isEmpty()) {
      this.userArr.get(selected).setFirstName(newInfo);
    }
    editInfo = (EditText) findViewById(R.id.edit_last_name);
    newInfo = editInfo.getText().toString();
    if (!newInfo.isEmpty()) {
      this.userArr.get(selected).setLastName(newInfo);
    }
    editInfo = (EditText) findViewById(R.id.edit_email);
    newInfo = editInfo.getText().toString();
    if (!newInfo.isEmpty()) {
      this.userArr.get(selected).setEmail(newInfo);
    }
    editInfo = (EditText) findViewById(R.id.edit_address);
    newInfo = editInfo.getText().toString();
    if (!newInfo.isEmpty()) {
      this.userArr.get(selected).setAddress(newInfo);
    }
    editInfo = (EditText) findViewById(R.id.edit_credit_card_number);
    newInfo = editInfo.getText().toString();
    if (!newInfo.isEmpty()) {
      this.userArr.get(selected).setCreditCardNumber(newInfo);
    }
    editInfo = (EditText) findViewById(R.id.edit_expiry_date);
    newInfo = editInfo.getText().toString();
    if (!newInfo.isEmpty()) {
      this.userArr.get(selected).setExpiryDate(newInfo);
    }

    this.userDatabase.save();

    if (this.thereIsClient) {
      // create a new intent and start the edit info activity if an admin
      Intent intent = new Intent(this, EditInfoActivity.class);
      intent.putExtra("clientKey", this.client);
      intent.putExtra("userKey", this.user);
      startActivity(intent);
    } else {
      // go back to the profile activity
      Intent intent = new Intent(this, ProfileActivity.class);
      intent.putExtra("userKey", this.user);
      startActivity(intent);
    }
  }
}
