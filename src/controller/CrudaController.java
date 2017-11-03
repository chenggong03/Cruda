package controller;

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
  public void delegate(String num) {
    
  }
}
