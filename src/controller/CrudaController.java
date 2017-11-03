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
   * TODO implement.
   */
  public boolean judge(String elementKey, Object element) {
    return true;
  }
  
  public boolean process(String entityName, String option, HashMap<String, Object> elements) {
    switch (option) {
    
    case "1":
      return dao.create(entityName, elements);
      
    case "2":
      break;
    case "3":
      break;
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
