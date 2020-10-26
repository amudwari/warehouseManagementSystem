

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class OrderList implements Serializable
{
    private static final long serialVersionUID = 1L;
    public LinkedList<Order> orders= new LinkedList<>();
    private static OrderList orderList;

    private OrderList(){ }

    public static OrderList instance() {
        if(orderList == null) orderList = new OrderList();
        return orderList;
    }

    public boolean insertOrder (Order order){
        return orders.add(order);
    }

    public Order getOrder(String id) {
        Iterator<Order> it = orders.iterator();

        while(it.hasNext()) {
        	Order order = it.next();

            if(order.getOrderId().equals(id)) return order;
        }

        return null;
    }

    public Iterator<Order> getOrders() {
        return orders.iterator();
    }

    public boolean contains(String id)
    {
        Iterator<Order> orders = orderList.getOrders();
        while (orders.hasNext())
        {
        	Order newOrder = (Order)(orders.next());
            String checkId = newOrder.getOrderId();
            if (checkId == id) {
                return true;
            }
        }
        return false;
    }
    
    private void writeObject(ObjectOutputStream output) {
		try {
			output.defaultWriteObject();
			output.writeObject(orderList);
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	private void readObject(ObjectInputStream input) {
		try {
			if(orderList != null) return;
			else {
				input.defaultReadObject();
				if(orderList == null) orderList = (OrderList) input.readObject();
				else input.readObject();
			}
		}catch(IOException e) { e.printStackTrace(); }
		 catch(ClassNotFoundException e) { e.printStackTrace(); }
	}

    public String toString() {
        return orders.toString();
    }


}