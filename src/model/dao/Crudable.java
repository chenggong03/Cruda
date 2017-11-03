package model.dao;

import java.util.HashMap;
import java.util.List;

import model.pojo.EntityInterface;

/**
 * Defines the behavior of all the needed CRUD methods, according to
 * the need of the clients.
 * @author gongcheng
 */
public interface Crudable {

  /**
   * CREATE an entity (i.e. Vehicle) of the type with the specific fields.
   */
  public abstract boolean create(String entityName,
      HashMap<String, Object> fields);

  /**
   * CREATE a list of entities of the type with the list of specific fields.
   */
  public boolean create(String entityName,
      List<HashMap<String, Object>> fields);

  /**
   * READ the entity of the type with id.
   */
  public abstract EntityInterface read(String entityName, int id);

  /**
   * READ all entities of the type.
   */
  public abstract HashMap<Integer, EntityInterface> read(String entityName);

  /**
   * READ all entities of the type with some filters (String is the field name,
   * Object is the filter value).
   */
  public abstract HashMap<Integer, EntityInterface> read(String entityName,
      HashMap<String, Object> filters);

  /**
   * UPDATE an entity of the type with the specific id, with the fields.
   */
  public abstract boolean update(String entityName, int id,
      HashMap<String, Object> fields);

  /**
   * DELETE an entity of the type with the specific id.
   */
  public abstract boolean delete(String entityName, int id);
}
