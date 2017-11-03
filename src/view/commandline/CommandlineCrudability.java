package view.commandline;

import java.util.Scanner;

/**
 * Displays the View to client on command line.
 * 
 * @author gongcheng
 *
 */
public class CommandlineCrudability {

  /**
   * Parses the command line and interacts with the user.
   * 
   * @param args
   */
  public static void main(String[] args) {

    // TODO Improve the options by getting the string from a config file
    System.out.println("Welcom to Crudability v1.0, built by Cheng Gong\n"
        + "Options:\n"
        + "1. GET all entities of the type (i.e. Vehicle)"
        + "\n2. GET all entities of the type with some filters"
        + "\n3. GET all entities of the type with id"
        + "\n4. CREATE an entity of the type with the specified fields"
        + "\n5. CREATE a number of entities of the type with the specified"
        + " fields"
        + "\n6. UPDATE an entity of the type with the specified id, with"
        + " the fields"
        + "\n7. DELETE an entity of the type with the specified id"
        + "\nq. type 'q' to quit the system.\n");

    // Construct a Scanner that produces values scanned from standard input
    Scanner input = new Scanner(System.in);
    System.out.print("> ");

    // TODO Initialize a HashMap from a config file, so the switch cases
    // are not hard coded here.
    // while there is more input (user has not hit EOF)
    while (input.hasNext()) {
      // read next integer
      String num = input.next();
      switch (num) {
      case "1":
        System.out.println("1 put");
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
        System.out.println("1 put");
        break;
      case "q":
        return;
      default:
        System.out.println("Invalid input");
        break;
      }
      System.out.print("> ");
    }
    input.close();
  }

}
