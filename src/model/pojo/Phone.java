package model.pojo;

/**
 * Class that implements EntityInterface just for testing
 * @author gongcheng
 *
 */
public class Phone implements EntityInterface {
  private int id;
  private String color;
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getColor() {
    return color;
  }
  
  public void setColor(String color) {
    this.color = color;
  }
}
