package model.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dao.Crudable;
import model.pojo.EntityInterface;

/**
 * Implements the CRUD methods of DAO and store entities in memory.
 * 
 * @author gongcheng
 *
 */
public class InMemoryDaoImpl implements Crudable {

  // TODO use one HashMap for each EntityInterface Type, instead of one
  // storage for
  // all type of Entities
  // Stores each HashMap of Entities into a HashMap. For example, we could
  // have a HashMap for Vehicles and a HashMap for Clients
  HashMap<String, HashMap<Integer, EntityInterface>> storage
      = new HashMap<String, HashMap<Integer, EntityInterface>>();

  /**
   * GET all entities of the type. The returned HashMap could be empty.
   */
  @Override
  public HashMap<Integer, EntityInterface> get(String entityName) {
    return storage.get(entityName);
  }

  /**
   * GET all entities of the type with some filters (String is the field nam,
   * Object is the filter value). The returned HashMap could be empty.
   */
  @Override
  public HashMap<Integer, EntityInterface> get(String entityName,
      HashMap<String, Object> filters) {

    HashMap<Integer, EntityInterface> entityStorage = storage.get(entityName);
    HashMap<Integer, EntityInterface> filteredEntityStorage = new HashMap<>();

    // Iterates through every entity to filter, and put entities that passed
    // the filters into filteredEntityStorage to return
    for (Map.Entry<Integer, EntityInterface> entry : entityStorage.entrySet()) {
      Integer id = entry.getKey();
      EntityInterface entity = entry.getValue();

      try {
        @SuppressWarnings("unchecked")
        Class<? super EntityInterface> clazz = (Class<? super EntityInterface>) Class
            .forName("model.pojo." + entityName);

        // Checks for each filter field using getters on this Entity.
        boolean filterMatched = true;
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
          String filterKey = filter.getKey();
          Object filterValue = filter.getValue();

          // Converts the filtering key to the method name, i.e. id -> getId.
          filterKey = "get" + filterKey.substring(0, 1).toUpperCase()
              + filterKey.substring(1);
          Method method = clazz.getMethod(filterKey);

          Object entityValue = method.invoke(entity);

          // If the two values differ, Marks object invalid and stops checking
          // the rest of the filters.
          if ((entityValue != null || filterValue != null)
              && (entityValue == null || !entityValue.equals(filterValue))) {
            filterMatched = false;
            break;
          }
        }
        if (filterMatched) {
          filteredEntityStorage.put(id, entity);
        }

      } catch (ClassNotFoundException e) {
        return null;
      } catch (NoSuchMethodException e) {
        return null;
      } catch (IllegalAccessException e) {
        return null;
      } catch (IllegalArgumentException e) {
        return null;
      } catch (InvocationTargetException e) {
        return null;
      }

    }
    return filteredEntityStorage;
  }

  /**
   * GET all entities of the type with id. Returns null if none.
   */
  @Override
  public EntityInterface get(String entityName, int id) {
    return storage.get(entityName).get(id);
  }

  /**
   * CREATE an entity of the type with the specified fields.
   */
  @Override
  public boolean create(String entityName, HashMap<String, Object> fields) {
    HashMap<Integer, EntityInterface> entityStorage = storage.get(entityName);

    try {
      @SuppressWarnings("unchecked")
      Class<? super EntityInterface> clazz = (Class<? super EntityInterface>) Class
          .forName("model.pojo." + entityName);

      // Creates a new entity object and fills in the fields.
      EntityInterface entity = (EntityInterface) clazz.newInstance();
      for (Map.Entry<String, Object> field : fields.entrySet()) {
        String filterKey = field.getKey();
        Object filterValue = field.getValue();

        // Converts the filtering key to the method name, i.e. id -> getId.
        filterKey = "set" + filterKey.substring(0, 1).toUpperCase()
            + filterKey.substring(1);
        Method method = clazz.getMethod(filterKey);

        // Calls the setter method on the field.
        method.invoke(entity, filterValue);
      }

      // This Integer cast will be covered in the exceptions related to
      // method.invoke method call above.
      Object id = fields.get("id");
      if (id == null) {
        return false;
      }

      // Stores the new entity back in memory.
      entityStorage.put((Integer) id, entity);

    } catch (ClassNotFoundException | NoSuchMethodException
        | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException | InstantiationException e) {
      return false;
    }

    return true;
  }

  /**
   * CREATE a list of entities of the type with the list of specified fields.
   * @return false if one of the entity creation fails.
   */
  @Override
  public boolean create(String entityName,
      List<HashMap<String, Object>> fieldList) {
    
    // Iterates through all the entities to create.
    for (HashMap<String, Object> fields : fieldList) {
      if (create(entityName, fields) == false) {
        return false;
      }
    }
    return true;
  }

  /**
   * UPDATE an entity of the type with the specified id, with the fields.
   * TODO This method should not update the id, need to do a check.
   */
  @Override
  public boolean update(String entityName, int id,
      HashMap<String, Object> fields) {
    EntityInterface entity = get(entityName, id);

    try {
      @SuppressWarnings("unchecked")
      Class<? super EntityInterface> clazz = (Class<? super EntityInterface>) Class
          .forName("model.pojo." + entityName);

      // Updates the fields to the entity.
      for (Map.Entry<String, Object> field : fields.entrySet()) {
        String filterKey = field.getKey();
        Object filterValue = field.getValue();

        // Converts the filtering key to the method name, i.e. id -> getId.
        filterKey = "set" + filterKey.substring(0, 1).toUpperCase()
            + filterKey.substring(1);
        Method method = clazz.getMethod(filterKey);

        // Calls the setter method on the field.
        method.invoke(entity, filterValue);
      }

    } catch (ClassNotFoundException | NoSuchMethodException
        | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      return false;
    }
    return true;

  }

  /**
   * DELETE an entity of the type with the specified id.
   * 
   * @return true if an non-null entity is deleted
   */
  @Override
  public boolean delete(String entityName, int id) {
    return storage.get(entityName).remove(id) != null;
  }

}
