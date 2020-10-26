import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orderId;
    
    private String clientId;
    
    private HashMap<String, Integer> prodQuantities = new HashMap<String, Integer>();
    
    private static final String ORDER_STRING = "O";

    public Order(String clientId, HashMap<String, Integer> cart) {
        orderId = ORDER_STRING + (OrderIdServer.instance()).getId();
        this.clientId = clientId;
        
        this.prodQuantities.putAll(cart);
    }

    public String getOrderId() {
        return orderId;
    }

    public String getClientId() {
        return clientId;
    }

    public float getTotalPrice() {
        float total = 0F;
        for(Map.Entry<String, Integer> entry : prodQuantities.entrySet())
        	total += Warehouse.instance().getProduct(entry.getKey()).getSalesPrice() * entry.getValue();
    	
    	return total;
    }
    
    public HashMap<String, Integer> getProdQuantities() { return prodQuantities; }

    public String toString() {
        return "\nOrder ID:" + getOrderId() + "\nClient ID: " + getClientId() + "\nTotal: $" + getTotalPrice();
    }
}
