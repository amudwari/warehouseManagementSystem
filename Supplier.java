
import java.io.*;
import java.util.HashMap;

public class Supplier implements Serializable {
  
  private static final long serialVersionUID = 1L;
  private static final String SUPPLIER_STRING = "S";
  private String name;
  private String address;
  private String id;
  
  private HashMap<String, Float> soldProducts = new HashMap<String, Float>();
  
  public Supplier (String name, String address) {
    this.name = name;
    this.address = address;
    this.id = SUPPLIER_STRING + SupplierIdServer.instance().getId();
  }
  
  public String getName() { return name; }
  public String getAddress(){ return address; }
  public String getId() { return id; }

  public void addSoldProduct(String prodId, float buyPrice) {
	  soldProducts.put(prodId, buyPrice);
  }
  
  public boolean sellsProduct(String prodId) { return soldProducts.containsKey(prodId); }
  public float getBuyPrice(String prodId) { return soldProducts.get(prodId); }
  
  public HashMap<String, Float> getSoldProducts() { return this.soldProducts; }
  
  public void setName(String newName) {
    name = newName;
  }
  public void setAddress(String newAddress) {
    address = newAddress;
  }

  public String toString() {
    String string = " Name:" + name + " address:" + address + "  ID:" + id +"  ";
    return string;
  }
}
