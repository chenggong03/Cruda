package view.commandline;

import java.util.HashMap;
import java.util.Scanner;

import controller.CrudaController;

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

  //Given a list of possible entity type, prompts the client to select
  // which entity type they will be using.
  // TODO Link the listOfKeys to the actual Pojo config.
  // TODO use a HashMap to correspond the fieldValue with its type,
  // so that no need for a just String[]. 
  private static HashMap<String, String[]> listOfKeys = new HashMap<>();
  private static String[] fieldKeys;

  /**
   * Parses the command line and interacts with the user.
   * 
   * @param args
   */
  public static void main(String[] args) {
    
    System.out.print(WELCOME);

    // Construct a Scanner that produces values scanned from standard input.
    Scanner input = new Scanner(System.in);
    
    generateListOfKeys();
        
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
      
      fieldKeys = listOfKeys.get(entityType);
      while (fieldKeys == null) {
        System.out.println("None Entity Type. Try Again.");
        System.out.print("> ");
        entityType = input.next();
        fieldKeys = listOfKeys.get(entityType);
      }
      
      System.out.print(USAGE + ">> ");

      // Prompts the client for the option.
      boolean toQuit = false;
      boolean toPrintError = false;
      while (input.hasNext()) {
        
        // Collects client's input fields.
        HashMap<String, Object> fields = new HashMap<>();
        
        // Reads option input.
        String option = input.next();
        switch (option) {
        
        case "1":
          System.out.println("1 put");
          
          // Requires valid integer input for id.
          // TODO move these code to Controller.
          int idInput = -1;
          boolean idOkay = true;
          do {
            if (!idOkay) {
              System.out.println("id not valid integer");
            }
            System.out.print("id: ");
            try {
              idInput = Integer.parseInt(input.next());
              idOkay = true;
            } catch (NumberFormatException e) {
              idOkay = false;
            }
          } while (!idOkay);
          fields.put("id", idInput);

          // Asks for input for each field key.
          for (String fieldKey : fieldKeys) {
            Object field;

            do {
              System.out.print(fieldKey + ": ");
              
              // TODO use a HashMap to correspond the fieldValue with its type.
              if (fieldKey.equals("year")) {
                field = Integer.parseInt(input.next());
                
              // put make and model in fields
              } else {
                field = input.next();
              }

            // Lets controller decide field's validity.
            } while (!controller.judge(fieldKey, field));
            fields.put(fieldKey, field);
          }
          
          // Lets controller process these fields.
          controller.process(entityType, "1", fields);
          
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
          toPrintError = true;
          break;
        }
        
        // Deals with the quit option.
        if (toQuit) {
          System.out.println("Exiting Storage for Entity Type: " + entityType);
          System.out.println("What Entity Type are you working on today? (q to quit)");
          System.out.print("> ");
          break;
        }
        
        // Checks if an invalid option is entered.
        if (toPrintError) {
          System.out.println("Invalid input: " + option);
          System.out.println(USAGE);
          toPrintError = false;
        } else {
          System.out.println("Option " + option + " processed;");
        }
        System.out.print(">> ");
      }
    }
    
    System.out.println("Thank you for using Cruda!");
    input.close();
  }

  /**
   * Helps generate list of default keys at the start of the program.
   */
  private static void generateListOfKeys() {
    String[] vehicleKeys = {"year", "make", "model"};
    listOfKeys.put("Vehicle", vehicleKeys);
  }

}
