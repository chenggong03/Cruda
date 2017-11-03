package model.dao;

import java.util.HashMap;
import java.util.List;

import model.pojo.EntityInterface;

/**
 * Defines the behavior of all the needed CRUD methods.
 * 
 * @author gongcheng
 *
 */
public interface Crudable {

  /**
   * GET all entities of the type.
   */
  public abstract HashMap<Integer, EntityInterface> get(String entityName);

  /**
   * GET all entities of the type with some filters (String is the field name,
   * Object is the filter value).
   */
  public abstract HashMap<Integer, EntityInterface> get(String entityName,
      HashMap<String, Object> filters);

  /**
   * GET all entities of the type with id.
   */
  public abstract EntityInterface get(String entityName, int id);

  /**
   * CREATE an entity of the type with the specified fields.
   */
  public abstract boolean create(String entityName,
      HashMap<String, Object> fields);

  /**
   * CREATE a list of entities of the type with the list of specified fields.
   */
  public boolean create(String entityName,
      List<HashMap<String, Object>> fields);

  /**
   * UPDATE an entity of the type with the specified id, with the fields.
   */
  public abstract boolean update(String entityName, int id,
      HashMap<String, Object> fields);

  /**
   * DELETE an entity of the type with the specified id.
   */
  public abstract boolean delete(String entityName, int id);
}
