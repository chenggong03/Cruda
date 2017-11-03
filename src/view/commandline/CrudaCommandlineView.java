package view.commandline;

import java.util.HashMap;
import java.util.Scanner;

import controller.CrudaController;
import model.dao.impl.InMemoryDaoImpl;

/**
 * Displays the View to client on command line.
 * 
 * @author gongcheng
 *
 */
public class CrudaCommandlineView {

  // TODO use Spring framework to manage the bean objects.
  private static CrudaController controller = new CrudaController();

  // TODO Improve the options by getting the string from a config file
  public static final String WELCOME
      = "Welcom to Cruda v1.0, your CRUD entity management system, built by"
      + " Cheng Gong, 2017.\n";
    public static final String USAGE
      = "Options:\n"
      + "1. CREATE an entity (i.e. Vehicle) of the type with the specified"
      + " fields"
      + "\n2. CREATE a number of entities of the type with the specific"
      + " fields"
      + "\n3. READ the entity of the type with id"
      + "\n4. READ all entities of the type"
      + "\n5. READ all entities of the type with some filters"
      + "\n6. UPDATE an entity of the type with the specified id, with"
      + " the specific fields"
      + "\n7. DELETE an entity of the type with the specified id"
      + "\nq. type 'q' to quit this Entity Storage.\n";

  /**
   * Parses the command line and interacts with the user.
   * 
   * @param args
   */
  public static void main(String[] args) {
    
    System.out.print(WELCOME);

    // Construct a Scanner that produces values scanned from standard input.
    Scanner input = new Scanner(System.in);
    
    // Given a list of possible entity type, prompts the client to select
    // which entity type they will be using.
    // TODO Link the listOfKeys to the actual Pojo config.
    // TODO use a HashMap to correspond the elementValue with its type,
    // so that no need for a just String[]. 
    HashMap<String, String[]> listOfKeys = new HashMap<>();
    String[] vehicleKeys = {"year", "make", "model"};
    listOfKeys.put("Vehicle", vehicleKeys);
    
    String[] elementKeys;
    
    // Prompts the client for Entity Type.
    System.out.println("What Entity Type are you working on today? (q to quit)");
    System.out.print("> ");
    while (input.hasNext()) {
          
      // Select the Entity Type and the keys.
      // TODO need to use input.hasNext method and deal with the situation
      // where we reach the EOF.
      String entityType = input.next();
      if (entityType.equals("q")) {
        break;
      }
      
      elementKeys = listOfKeys.get(entityType);
      while (elementKeys == null) {
        System.out.println("None Entity Type. Try Again.");
        System.out.print("> ");
        entityType = input.next();
        elementKeys = listOfKeys.get(entityType);
      }
      
      System.out.print(USAGE + ">> ");

      // While there is more input (client has not hit EOF),
      // Prompts the client for the option.
      boolean toQuit = false;
      while (input.hasNext()) {
        
        // collect client's input elements
        HashMap<String, Object> elements = new HashMap<>();
        
        // Reads option input.
        String option = input.next();
        switch (option) {
        
        case "1":
          System.out.println("1 put");
          elements.put("entityType", "Vehicle");
          
          System.out.print("id: ");
          elements.put("id", Integer.parseInt(input.next()));
          
          for (String elementKey : elementKeys) {
            System.out.print(elementKey + ": ");
            
            // TODO use a HashMap to correspond the elementValue with its type.
            if (elementKey.equals("year")) {
              elements.put(elementKey, Integer.parseInt(input.next()));
            } else {
              elements.put(elementKey, input.next());
            }
          }
          
          
          // TODO
          // Lets CrudaController process these.
          break;
        case "2":
          System.out.println("2 put");
          break;
        case "3":
          System.out.println("3 put");
          break;
        case "4":
          System.out.println("4 put");
          break;
        case "5":
          System.out.println("5 put");
          break;
        case "6":
          System.out.println("6 put");
          break;
        case "7":
          System.out.println("7 put");
          break;
        case "q":
          toQuit = true;
          break;
        default:
          System.out.println("Invalid input: " + option);
          System.out.println(USAGE);
          break;
        }
        
        if (toQuit) {
          System.out.println("Exiting Storage for Entity Type: " + entityType);
          break;
        }
        
        // TODO
        controller.delegate("HI");
        System.out.print(">> ");
      }

      System.out.println("What Entity Type are you working on today? (q to quit)");
      System.out.print("> ");
    }
    
    System.out.println("Thank you for using Cruda!");
    input.close();
  }

}
