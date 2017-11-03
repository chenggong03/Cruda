package view.commandline;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import controller.CrudaController;
import model.pojo.EntityInterface;

/**
 * Displays the View to client on command line.
 * 
 * @author gongcheng
 */
public class CrudaCommandlineView {

  // TODO use Spring framework to manage the bean objects.
  private static CrudaController controller = new CrudaController();

  // TODO Improve the options by getting the string from a config file
  public static final String WELCOME = "Welcom to Cruda v1.0, your CRUD entity"
      + " management system, built by"
      + " Cheng Gong, 2017.\n";
  public static final String USAGE = "Options:\n"
      + "1. CREATE an entity (i.e. Vehicle) of the type with the specified"
      + " fields"
      + "\n2. CREATE a number of entities of the type with the specific"
      + " fields"
      + "\n3. READ the entity of the type with id"
      + "\n4. READ all entities of the type"
      + "\n5. UPDATE an entity of the type with the specific fields"
      + "\n6. UPDATE an entity of the type with the specified id, with"
      + " the specific fields"
      + "\n7. DELETE an entity of the type with the specified id"
      + "\nq. type 'q' to quit this Entity Storage.\n";

  public static final String PROMPT_ENTITY = "What Entity Type are you working"
      + "on today? (q to quit)";

  public static final String THANK_YOU = "Thank you for using Cruda!";

  public static final String PROMPT_ENTITY_SIGN = "> ";
  public static final String PROMPT_OPTION_SIGN = ">> ";

  // Given a list of possible entity type, prompts the client to select
  // which entity type they will be using.
  // TODO Link the listOfKeys to the actual Pojo config.
  // TODO use a HashMap to correspond the fieldValue with its type,
  // so that no need for a just String[].
  private static HashMap<String, List<String>> listOfKeys = new HashMap<>();
  private static List<String> fieldKeys;

  /**
   * Parses the command line and interacts with the user.
   * 
   * @param args
   *          is the command-line arguments, not used
   */
  public static void main(String[] args) {

    System.out.print(WELCOME);

    // Construct a Scanner that produces values scanned from standard input.
    Scanner input = new Scanner(System.in);

    generateListOfKeys();

    // Prompts the client for Entity Type.
    System.out.println(PROMPT_ENTITY);
    System.out.print(PROMPT_ENTITY_SIGN);
    while (input.hasNext()) {

      // Selects the Entity Type and the keys.
      // TODO need to use input.hasNext method and deal with the situation
      // where we reach the EOF.
      String entityType = input.next();
      if (entityType.equals("q")) {
        break;
      }

      // Gets the fieldKeys according to the entityType.
      fieldKeys = listOfKeys.get(entityType);
      while (fieldKeys == null) {
        System.out.println("None Entity Type. Try Again.");
        System.out.print(PROMPT_ENTITY_SIGN);
        entityType = input.next();
        fieldKeys = listOfKeys.get(entityType);
      }
      fieldKeys.add(0, "id");

      System.out.print(USAGE + PROMPT_OPTION_SIGN);

      // Prompts the client for the option.
      boolean toQuit = false;
      boolean toPrintError = false;
      boolean processSuccess = true;
      while (input.hasNext()) {

        // Collects client's input fields.
        HashMap<String, Object> fields = new HashMap<>();

        // Reads option input.
        // TODO use a HashMap/external file for the options.
        String option = input.next();
        switch (option) {

        case "1": {
          System.out.println("1 put");

          // Asks for input for each field key.
          for (String fieldKey : fieldKeys) {

            // Lets controller decide field's validity.
            String someField = processFieldInput(fieldKey, input);
            fields.put(fieldKey, someField);
          }

          // Lets controller process these fields.
          processSuccess = (boolean) controller.process(entityType, option,
              fields);
          break;
        }

        case "2": {
          System.out.println("2 put");

          // TODO input check.
          System.out.print("Number of Entities to create: ");
          int iterations = input.nextInt();
          for (int i = 0; i < iterations; i++) {

            // Asks for input for each field key.
            for (String fieldKey : fieldKeys) {

              // Lets controller decide field's validity.
              String someField = processFieldInput(fieldKey, input);
              fields.put(fieldKey, someField);
            }

            // Lets controller process these fields.
            processSuccess = (boolean) controller.process(entityType, option,
                fields);

            // If one of the creation fails, terminates option immediately.
            if (!processSuccess) {
              System.out.println("First " + i + " entities stored, and the " + i + "th"
                  + "one failed.");
              break;
            }
          }
          break;
        }

        case "3": {
          System.out.println("3 put");
          String idField = processFieldInput("id", input);
          fields.put("id", idField);
          EntityInterface entity = (EntityInterface) controller.process(
              entityType, option, fields);
          if (entity == null) {
            System.out.println("no " + entityType + " with the id of "
                + idField);
            break;
          }

          processSuccess = displayEntity(entity);
          break;
        }

        case "4": {

          @SuppressWarnings("unchecked")
          HashMap<Integer, EntityInterface> entityStorage
          = (HashMap<Integer, EntityInterface>) controller
              .process(entityType, option, null);
          if (entityStorage == null) {
            System.out.println("no " + entityType);
            break;
          }

          // Iterates and displays.
          for (EntityInterface entity : entityStorage.values()) {
            processSuccess = displayEntity(entity);

            // If one of the display fails, terminates option immediately.
            if (!processSuccess) {
              System.out.println("An entity failed to display; break.");
              break;
            }
          }
          break;
        }

        case "5": {
          
          System.out.print("Number of Entities to update: ");
          int iterations = input.nextInt();
          for (int i = 0; i < iterations; i++) {
            
            // Asks for input for each field key.
            for (String fieldKey : fieldKeys) {

              // Lets controller decide field's validity.
              String someField = processFieldInput(fieldKey, input);
              fields.put(fieldKey, someField);
            }
            processSuccess = (boolean) controller.process(entityType, option,
                null);

            // If one of the creation fails, terminates option immediately.
            if (!processSuccess) {
              System.out.println("First " + i + " entities stored, and the " + i + "th"
                  + "one failed.");
              break;
            }
          }
          break;
        }

        case "6": {

          // Asks for input for each field key.
          for (String fieldKey : fieldKeys) {

            // Lets controller decide field's validity.
            String someField = processFieldInput(fieldKey, input);
            fields.put(fieldKey, someField);
          }

          // Lets controller process these fields.
          // TODO tell client if the entered id doesn't exist.
          processSuccess = (boolean) controller.process(entityType, option,
              fields);
          break;
        }

        case "7": {
          System.out.println("7 put");

          String idField = processFieldInput("id", input);
          fields.put("id", idField);
          processSuccess = (boolean) controller.process(entityType, option,
              fields);
          break;
        }

        case "q": {
          toQuit = true;
          break;
        }

        default: {
          toPrintError = true;
          break;
        }
        } // End of switch statement on options.

        // Deals with the quit option.
        if (toQuit) {
          System.out.println("Exiting Storage for Entity Type: " + entityType);
          System.out.println(PROMPT_ENTITY);
          System.out.print(PROMPT_ENTITY_SIGN);
          break;
        }

        // Checks if an invalid option is entered.
        if (toPrintError) {
          System.out.println("Invalid input: " + option);
          System.out.println(USAGE);
          toPrintError = false;
        } else {
          if (processSuccess) {
            System.out.println("Option " + option + " processed;");
          } else {
            System.out.println("Option " + option + " failed to process;");
            processSuccess = true;
          }
        }
        System.out.println("----------");
        System.out.print(PROMPT_OPTION_SIGN);
      }
    }

    System.out.println(THANK_YOU);
    input.close();
  }

  /**
   * Helps generate list of default keys at the start of the program.
   */
  private static void generateListOfKeys() {
    ArrayList<String> vehicleKeys = new ArrayList<>(
        Arrays.asList("year", "make", "model"));
    listOfKeys.put("Vehicle", vehicleKeys);
  }

  /**
   * Updates the field and returns about the decision message. If the decision
   * message is null, then field should be used.
   * 
   * @param field
   * @param fieldKey
   * @param input
   * @return an array of decision message and valid input field
   */
  private static String processFieldInput(String fieldKey, Scanner input) {
    String decisionMessage;
    System.out.print(fieldKey + ": ");

    String field = input.next();

    decisionMessage = controller.judge(fieldKey, field);

    while (decisionMessage != null) {
      System.out.println(field + ": " + decisionMessage);
      System.out.print(fieldKey + ": ");

      field = input.next();

      decisionMessage = controller.judge(fieldKey, field);
    }
    return field;
  }

  private static boolean displayEntity(EntityInterface entity) {
    Class<? extends EntityInterface> clazz = entity.getClass();
    Method[] methods = clazz.getDeclaredMethods();

    for (Method method : methods) {
      String methodName = method.getName();

      // Prints the result of the getter methods.
      if ("get".equals(methodName.substring(0, 3))) {
        try {
          System.out.println(methodName.substring(3) + ": "
              + method.invoke(entity));
        } catch (IllegalAccessException | IllegalArgumentException
            | InvocationTargetException e) {
          return false;
        }
      }
    }
    return true;
  }

}
