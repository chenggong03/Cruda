package model.pojo;

/**
 * EntityInterface ensures that the entity is extendable, not only limited to
 * the initial idea of vehicles. It describes every pojo entity with at least
 * an int id to hash to. id is not self generated and can be overwritten.
 * @author gongcheng
 */
public interface EntityInterface {
  public int getId();
}
