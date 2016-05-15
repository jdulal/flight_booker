package databases;

/**
 * Defines a database.
 *
 * @author Nikki Quibin - quibinni
 */
public interface Database<T> {

  /**
   * Adds the item to this database.
   *
   * @param item the item to add.
   */
  public void addItem(T item);

  /**
   * Removes the item from this database. Returns true if the item was removed,
   * otherwise false.
   *
   * @param item the item to remove.
   * @return true if the item was removed, otherwise false.
   */
  public boolean removeItem(T item);

  /**
   * Returns the item from this database at the given index.
   *
   * @param index the index of the item.
   * @return the item from this database at the given index.
   */
  public T getItem(int index);

  /**
   * Checks if the item is in this database. Returns true if it is, otherwise
   * false.
   *
   * @param identifier a unique string identifier for the item.
   * @return true if the item is in this database, otherwise false.
   */
  public boolean contains(String identifier);

  /**
   * Returns the number of items in this database.
   *
   * @return the number of items in this database.
   */
  public int size();

  /**
   * Saves the current state of this database (i.e. serialize the contents of
   * this database).
   */
  public void save();

  /**
   * Loads the previously saved state of this database (i.e. deserialize the
   * contents of this database).
   */
  public void load();
}
