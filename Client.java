import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CLIENT_STRING = "C";
    private String id;
    private String name;
    private float balance;
    
    private List<Transaction> transactions = new LinkedList<Transaction>();
    private HashMap<String, Integer> shoppingCart = new HashMap<String, Integer>();

    public Client(String name,float balance) {
        this.name = name;
        this.id = CLIENT_STRING + ClientIdServer.instance().getId();
        this.balance = balance;
        transactions = new LinkedList<Transaction>();
    }
    
    public String getName() {return name;}
    public String getId() {return id;}
    public float getBalance() {return balance;}
    
    public void addToCart(String productId, int amnt) {
    	if(shoppingCart.containsKey(productId)) {
    		int tempAmnt = shoppingCart.get(productId);
    		shoppingCart.replace(productId, tempAmnt + amnt);
    	}else shoppingCart.put(productId, amnt);
    }
    
    public HashMap<String, Integer> getCart() { return this.shoppingCart; }
    public void clearCart() { shoppingCart.clear(); }
    public boolean isCartEmpty() { return shoppingCart.isEmpty(); }
    
    public void addTransaction(Transaction t) {
    	if(transactions == null) transactions = new LinkedList<Transaction>();
    	transactions.add(t);
    }
    
    public Iterator<Transaction> getTransactions() { return transactions.iterator(); }

    public void setName(String newName) {name = newName;}
    public void changeBalance(float balanceMod) {balance += balanceMod;}

    public Boolean makePayment(float payment) {
        if(payment <= balance) {
            balance -= payment;
            return true;
        } else {
            return false;
        }
    }
    public String toString() {
        return "Name: " + name + ", ID: " + id + ", Balance: " + balance;
    }
}
