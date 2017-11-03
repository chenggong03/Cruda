package controller;

import java.util.HashMap;

import model.dao.impl.InMemoryDaoImpl;

/**
 * Processes data between the view and the model.
 * 
 * @author gongcheng
 *
 */
public class CrudaController {

  // TODO use Spring framework to manage the bean objects.
  private static InMemoryDaoImpl dao = new InMemoryDaoImpl();
  
  // TODO Initialize a HashMap from a config file, so the switch cases
  // and the actions are not hard coded here.
  
  /**
   * Decides whether the user input to the key are valid.
   * TODO use a HashMap/external file for the keys.
   */
  public String judge(String fieldKey, String field) {
    if ("id".equals(fieldKey)) {
      try {
        Integer.parseInt((String) field);
      } catch (NumberFormatException e) {
        return field + "id not valid integer.";
      }
    } else if ("year".equals(fieldKey)) {
      try {
        Integer.parseInt((String) field);
      } catch (NumberFormatException e) {
        return field + "year not valid integer.";
      }
    }
    
    return null;
  }
  
  // TODO use a HashMap/external file for the options
  public Object process(String entityName, String option, HashMap<String, Object> fields) {
    switch (option) {
    
    case "1":
      return dao.create(entityName, fields);
      
    case "2":
      break;
    case "3":
      return dao.read(entityName, Integer.parseInt((String) fields.get("id")));

    case "4":
      break;
    case "5":
      break;
    case "6":
      break;
    case "7":
      break;
    default:
      return false;
    }
    
    return true;
  }
}
