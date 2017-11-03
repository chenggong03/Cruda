/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import controller.CrudaController;
import model.pojo.EntityInterface;
import model.pojo.Phone;
import model.pojo.Vehicle;

/**
 * Test mainly for the process method in the Control side.
 * The process method is important because it gets called by the View
 * and modifies all the data in the Model.
 * 
 * @author gongcheng
 */
public class CrudaTester {

  CrudaController controller;
  
  String entityType1;
  String entityType2;
  String entityTypeNone;
  
  HashMap<String, Object> fields1;
  HashMap<String, Object> fields2;
  HashMap<String, Object> fields3;
  HashMap<String, Object> fieldsId2;

  /**
   * Set up variables, entityType strings, and fields
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    controller = new CrudaController();
    entityType1 = "Vehicle";
    entityType2 = "Phone";
    entityTypeNone = "Car Collision";

    fields1 = new HashMap<>();
    fields1.put("id", 1);
    fields1.put("year", 2016);
    fields1.put("make", "Audi");
    fields1.put("model", "A6");
    
    fields2 = new HashMap<>();
    fields2.put("id", 2);
    fields2.put("color", "Sky Blue");
    
    fields3 = new HashMap<>();
    fields3.put("id", 2);
    fields3.put("year", 965);
    fields3.put("make", "Range Rover");
    fields3.put("model", "Evoque");
    
    fieldsId2 = new HashMap<>();
    fieldsId2.put("id", 2);
  }

  /**
   * Test method for
   * {@link controller.CrudaController#judge(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testProcess() {
    
    // Reads before any creation should return null.
    assertNull(controller.process(entityType1, "3", fields1));
    
    // Creates.
    assertTrue((boolean) controller.process(entityType1, "1", fields1));
    assertTrue((boolean) controller.process(entityType2, "1", fields2));
    assertTrue((boolean) controller.process(entityType1, "1", fields3));
    
    // Creates again, and overwrite the same id into HashMap.
    assertTrue((boolean) controller.process(entityType1, "2", fields3));

    // Tests invalid option.
    assertFalse((boolean) controller.process(entityType1, "q", fields1));

    // Reads entity with id.
    Phone blueBird = (Phone) controller.process(entityType2, "3", fieldsId2);
    assertEquals(2, blueBird.getId());
    assertEquals("Sky Blue", blueBird.getColor());
    
    // Reads entities
    @SuppressWarnings("unchecked")
    HashMap<Integer, EntityInterface> vehicles
        = (HashMap<Integer, EntityInterface>) controller.process(entityType1,
            "4", fields1);
    assertEquals(2, vehicles.size());
    Vehicle aud = (Vehicle) vehicles.get(1);
    assertEquals(1, aud.getId());
    assertEquals(2016, aud.getYear());
    assertEquals("Audi", aud.getMake());
    assertEquals("A6", aud.getModel());
    Vehicle evoq = (Vehicle) vehicles.get(2);
    assertEquals(2, evoq.getId());
    assertEquals(965, evoq.getYear());
    assertEquals("Range Rover", evoq.getMake());
    assertEquals("Evoque", evoq.getModel());
    
    @SuppressWarnings("unchecked")
    HashMap<Integer, EntityInterface> nonExisting
    = (HashMap<Integer, EntityInterface>) controller.process(entityTypeNone,
        "4", fields1);
    assertNull(nonExisting);
    
    // Filters to be implemented.
    assertFalse((boolean) controller.process(entityTypeNone, "5", fields1));

    // Updates and then check the updates with read.
    fields1.put("model", "A6L");
    fields1.put("year", 2020);
    assertTrue( (boolean) controller.process(entityType1, "6", fields1));
    assertEquals("A6L", ( (Vehicle) controller.process(entityType1, "3",
        fields1) ).getModel());
    assertEquals(2020, ( (Vehicle) controller.process(entityType1, "3",
        fields1) ).getYear());
    
    // Deletes and then check the delete with read.
    assertTrue( (boolean) controller.process(entityType1, "7", fields1));
    assertNull(controller.process(entityType1, "3", fields1));
  }

}
