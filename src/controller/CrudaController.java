package controller;

import java.util.HashMap;

import model.dao.impl.InMemoryDaoImpl;

/**
 * Processes data between the view and the model.
 * @author gongcheng
 */
public class CrudaController {

  // TODO use Spring framework to manage the bean objects.
  private static InMemoryDaoImpl dao = new InMemoryDaoImpl();

  // TODO Initialize a HashMap from a config file, so the switch cases
  // and the actions are not hard coded here.

  /**
   * Decides whether the user input to the key are valid.
   * @param fieldKey is the key that serve as the criteria for the field
   * @param field is the word to be judged
   * @return an error message if the field is not valid for the fieldKey, return
   *         null otherwise TODO use a HashMap/external file for the keys.
   */
  public String judge(String fieldKey, String field) {
    if ("id".equals(fieldKey) || "year".equals(fieldKey)) {
      try {
        Integer.parseInt((String) field);
      } catch (NumberFormatException e) {
        return "For " + fieldKey + ", " + field + "is not an valid integer.";
      }
    }

    return null;
  }

  /**
   * Processes the client request to the specified entity type, given the
   * option signifying the action
   * @param entityName is the entity type to be acted upon
   * @param option indicates the action to process
   * @param fields contains the user specified info
   * @return the outcome of the process
   */
  public Object process(String entityName, String option,
      HashMap<String, Object> fields) {

    // case 5 will be null.
    int id = -1;
    if (fields != null) {
      id = Integer.parseInt((String) fields.get("id"));
    }

    // TODO use a HashMap/external file for the options.
    switch (option) {

    case "1":
      return dao.create(entityName, fields);

    case "2":
      return dao.create(entityName, fields);

    case "3":
      return dao.read(entityName, id);

    case "4":
      return dao.read(entityName);

    case "5":
      return false;

    case "6":

      // TODO reduce the redundancy of id in fields.
      // No need to check for parsing exceptions since handled in
      // processFieldInput in View
      return dao.update(entityName, id,
          fields);

    case "7":
      return dao.delete(entityName, id);

    default:
      return false;
    }
  }
}
