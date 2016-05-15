package databases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import backend.User;

/**
 * A database to store all users. The users are stored in an array list. The
 * state of the database is saved in a json file for serializing and
 * deserializing. The GSON library is used for this process. No duplicates are
 * allowed in the array list. If a user to be added matches an existing user, it
 * will not be added.
 *
 * @author Nikki Quibin - quibinni
 */
public class UserDatabase implements Database<User>, Serializable {

  // a list to store all users
  private ArrayList<User> users;
  // a re-usable gson object for serialization/deserialization
  private Gson gson = new Gson();
  // the parameterized type for object serialization/deserialization
  // with generics. Uses an anonymous class to get the type
  private Type userType = new TypeToken<ArrayList<User>>() {}.getType();
  // store the context
  private static Context context;

  private File getUsersFile() {
    File userData = this.context.getApplicationContext().getDir("databases", Context.MODE_PRIVATE);
    File usersFile = new File(userData, "users.json");
    return usersFile;
  }

  public UserDatabase() {
    this(context);
  }

  /**
   * Initializes the user database. Creates an empty database and json file for
   * saving the state of the database if it doesn't exist. If there is an
   * existing json file, then load the previously saved state of the database.
   *
   * @param context the context of the activity
   */
  public UserDatabase(Context context) {
    UserDatabase.context = context;

    File usersFile = this.getUsersFile();

    if (!usersFile.exists()) {
      this.users = new ArrayList<>();
      this.save();
    } else {
      this.load();
    }
  }

  /**
   * Finds the index of the user in the list. It returns -1 if the user cannot
   * be found.
   *
   * @param user the user to find the index of.
   * @return the index of the user (-1 if not found).
   */
  private int findUserIndex(User user) {
    // the index to return
    int index = 0;

    // check if the list is empty, if it is, index = -1. If not,
    // then check if the user is in the list
    if (this.users.isEmpty()) {
      index = -1;
    } else {
      for (User currUser : this.users) {
        // each user must have a unique email, so if they match
        // we found the user
        if (user.getEmail().equals(currUser.getEmail())) {
          // we found the index
          break;
        } else {
          // increment the index
          index += 1;
        }
      }
    }

    // if the index is equal to the size of the list, then it's not in
    // the list
    if (index >= this.users.size()) {
      index = -1;
      return index;
    } else {
      return index;
    }
  }

  /**
   * Adds the user to this database. If the given user is already in this
   * database, then it will be overwritten.
   *
   * @param user the user to be added.
   */
  @Override
  public void addItem(User user) {
    // add the user to the database if it's not already there, otherwise
    // overwrite the existing user
    int index = findUserIndex(user);
    if (index < 0) {
      this.users.add(user);
    } else {
      // check that the index is valid. Replace the user if it is
      if (index < this.users.size()) {
        this.users.set(index, user);
      }
    }
  }

  /**
   * Removes the user from this database.
   *
   * @param user the user to be removed.
   * @return true if the user was removed, otherwise false.
   */
  @Override
  public boolean removeItem(User user) {
    // the index of the user
    int index = findUserIndex(user);

    // return true if the user was removed, otherwise false
    if (index < 0) {
      return false;
    } else {
      this.users.remove(index);
      return true;
    }
  }

  /**
   * Returns the user obect at the given index. It will return a null value if
   * the index is invalid.
   *
   * @param index the index of the desired user.
   * @return the user object at the given index, or null if the index is
   invalid.
   */
  @Override
  public User getItem(int index) {
    // check if the index is valid, if it is return the user object,
    // otherwise,
    // return a null value
    if (index < 0 || index >= this.users.size()) {
      return null;
    } else {
      return this.users.get(index);
    }
  }

  /**
   * Returns the user in this database with the given email.
   *
   * @param email the email of the user
   * @return the user in this database with the given email.
   */
  public User getUser(String email) {
    // the user to return
    User user = null;
    // find the user with the corresponding email if it exists
    for (User currUser : this.users) {
      if (email.equals(currUser.getEmail())) {
        user = currUser;
        return user;
      }
    }

    return null;
  }

  /**
   * Returns an array list of all the clients in this database.
   *
   * @return an array list of all the clients in this database.
   */
  public ArrayList<User> getAllClients() {
    // list to return
    ArrayList<User> clients = new ArrayList<>();
    // loop through the user database and add it to the list to return if it's a client
    for (User user : this.users) {
      if (!user.isAdmin()) {
        clients.add(user);
      }
    }
    return clients;
  }

  /**
   * Returns an array list of all the admins in this database.
   *
   * @return an array list of all the admins in this database.
   */
  public ArrayList<User> getAllAdmins() {
    // list to return
    ArrayList<User> admins = new ArrayList<>();
    // loop through the user database and add it to the list to return if it's an admin
    for (User user : this.users) {
      if (user.isAdmin()) {
        admins.add(user);
      }
    }
    return admins;
  }

  /**
   * Returns true if the user is in this database, otherwise false.
   *
   * @param email the email to check for the user.
   * @return true if the user is in this database, otherwise false.
   */
  @Override
  public boolean contains(String email) {
    boolean result = false;
    // loop through the users list and return true if the user's email matches
    for (User user : this.users) {
      if (email.equals(user.getEmail())) {
        result = true;
        break;
      }
    }
    return result;
  }

  /**
   * Returns the size of this database.
   *
   * @return the size of this database.
   */
  @Override
  public int size() {
    return this.users.size();
  }

  /**
   * Saves the current state of this database by serializing it into a json
   * file. This method should be called after handling users in this database.
   */
  @Override
  public void save() {
    try {
      File usersFile = this.getUsersFile();

      FileWriter writer = new FileWriter(usersFile);
      gson.toJson(this.users, this.userType, writer);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the previously saved state of this database by deserializing from a
   * json file.
   */
  @Override
  public void load() {
    try {
      File usersFile = this.getUsersFile();
      // use a buffered reader to read the json file
      BufferedReader reader = new BufferedReader(new FileReader(usersFile));
      // restore this user database (deserialization)
      this.users = gson.fromJson(reader, userType);
      // close the reader
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks if this database is empty.
   * @return true if this database is empty, otherwise false.
   */
  public boolean isEmpty() {
    return this.users.isEmpty();
  }

  /**
   * Returns the string representation of this database.
   *
   * @return the string representation of this database.
   */
  @Override
  public String toString() {
    String message = "[User Database]\n";
    for (User user : this.users) {
      message += user.toString() + "\n";
    }
    return message;
  }
}
