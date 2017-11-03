package model.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dao.Crudable;
import model.pojo.EntityInterface;

/**
 * Implements the CRUD methods of DAO and stores entities in memory.
 * @author gongcheng
 */
public class InMemoryDaoImpl implements Crudable {

  // Stores each HashMap of Entities into a HashMap. For example, we could
  // have a HashMap for Vehicles and a HashMap for Clients
  HashMap<String, HashMap<Integer, EntityInterface>> storage
      = new HashMap<String, HashMap<Integer, EntityInterface>>();

  /**
   * CREATE an entity of the type with the specific fields.
   * Id is not self generated and can be overwritten.
   * @param entityName is the entity type to be acted upon
   * @param fields contains the user specified info
   * @return true if successfully created
   */
  @Override
  public boolean create(String entityName, HashMap<String, Object> fields) {
    HashMap<Integer, EntityInterface> entityStorage = storage.get(entityName);
    if (entityStorage == null) {
      entityStorage = new HashMap<>();
      storage.put(entityName, entityStorage);
    }
    
    return reflectiveSet(entityName, fields, null, entityStorage);
  }

  /**
   * CREATE a list of entities of the type with the list of specific fields.
   * Id is not self generated and can be overwritten.
   * @param entityName is the entity type to be acted upon
   * @param fieldList contains the user specified lists of fields
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
   * GET the entity of the type with id. Returns null if none.
   * @param entityName is the entity type to be acted upon
   * @param id is the id of the entity to be read
   * @return the read EntityInterface, null if none read
   */
  @Override
  public EntityInterface read(String entityName, int id) {
    HashMap<Integer, EntityInterface> entityStorage = storage.get(entityName);
    if (entityStorage == null) {
      return null;
    }
    return entityStorage.get(id);
  }

  /**
   * GET all entities of the type. The returned HashMap could be empty.
   * @param entityName is the entity type to be acted upon
   * @return a HashMap of EntityInterfaces of that entity type
   */
  @Override
  public HashMap<Integer, EntityInterface> read(String entityName) {
    return storage.get(entityName);
  }

  /**
   * GET all entities of the type with some filters (String is the field nam,
   * Object is the filter value). The returned HashMap could be empty.
   * @param entityName is the entity type to be acted upon
   * @param filters contains the user specified info to filter the entities
   * @return a HashMap of filtered EntityInterfaces of that entity type
   */
  @Override
  public HashMap<Integer, EntityInterface> read(String entityName,
      HashMap<String, Object> filters) {

    HashMap<Integer, EntityInterface> entityStorage = storage.get(entityName);
    HashMap<Integer, EntityInterface> filteredEntityStorage = new HashMap<>();
    if (entityStorage == null) {
      return filteredEntityStorage;
    }

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

      } catch (ClassNotFoundException | NoSuchMethodException
          | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException e) {
        return new HashMap<Integer, EntityInterface>();
      }

    }
    return filteredEntityStorage;
  }

  /**
   * UPDATE an entity of the type with the specific id, with the fields.
   * TODO This method should not update the id, need to do a check.
   * @param entityName is the entity type to be acted upon
   * @param id is the id of the entity to be read
   * @param fields contains the user specified info to update
   * @return true if modified, false otherwise
   */
  @Override
  public boolean update(String entityName, int id,
      HashMap<String, Object> fields) {
    EntityInterface entity = read(entityName, id);
    if (entity == null) {
      System.out.println("entity == null");
      return false;
    }

    return reflectiveSet(entityName, fields, entity, null);

  }
  
  
  /**
   * Helper method for only create and update, since they user similar setter
   * operations.
   * @param entityName is the entity type to be acted upon
   * @param fields contains the user specified info to update
   * @param entity is the entity to be acted upon. It should be null when using
   * create method
   * @param entityStorage is the entityStorage used to store entity in. It
   * should be null when using update method
   * @return true if modified, false otherwise
   */
  private boolean reflectiveSet(String entityName, HashMap<String, Object>
      fields, EntityInterface entity, HashMap<Integer, EntityInterface>
      entityStorage) {
    try {
      @SuppressWarnings("unchecked")
      Class<? super EntityInterface> clazz = (Class<? super EntityInterface>) Class
          .forName("model.pojo." + entityName);
      
      // entity is only null when create method is called.
      if (entity == null) {
        entity = (EntityInterface) clazz.newInstance();
      }
      
      // Updates the fields to the entity.
      for (Map.Entry<String, Object> field : fields.entrySet()) {
        String filterKey = field.getKey();
        Object filterValue = field.getValue();

        // TODO use a HashMap/external file for the keys
        Class<?> paramClazz;
        if ("id".equals(filterKey) || "year".equals(filterKey)) {
          paramClazz = int.class;
        } else {
          paramClazz = String.class;
        }
    
        // Converts the filtering key to the method name, i.e. id -> getId.
        // command should be "set" or "get" only.
        filterKey = "set" + filterKey.substring(0, 1).toUpperCase()
            + filterKey.substring(1);
        
        
        // Calls the setter method on the field.
        Method method = clazz.getMethod(filterKey, paramClazz);
        
        // TODO use a HashMap/external file for the keys
        if (paramClazz == int.class) {
          filterValue = Integer.parseInt((String) filterValue);
        }
        method.invoke(entity, filterValue);
      }
      
      // entityStorage is only used when create method is called.
      if (entityStorage != null) {
        entityStorage.put(Integer.parseInt((String)fields.get("id")), entity);
      }
      
    } catch (ClassNotFoundException | NoSuchMethodException
        | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException | InstantiationException e) {
      e.printStackTrace();
      return false;
    }
    return true;
    
  }

  /**
   * DELETE an entity of the type with the specific id.
   * @param entityName is the entity type to be acted upon
   * @param id is the id of the entity to be read
   * @return true if an non-null entity is deleted
   */
  @Override
  public boolean delete(String entityName, int id) {
    HashMap<Integer, EntityInterface> entityStorage = storage.get(entityName);
    if (entityStorage == null) {
      return false;
    }
    return entityStorage.remove(id) != null;
  }

}
