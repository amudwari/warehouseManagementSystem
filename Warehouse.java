

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//Class by Kevin Gruwell
public class Warehouse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Warehouse warehouse;
	
	private ProductList productList;
	private SupplierList supplierList;
	private ClientList clientList;
	
	private OrderList orderList;
	
	private Warehouse() {
		productList = ProductList.instance();
		supplierList = SupplierList.instance();
		clientList = ClientList.instance();
		
		orderList = OrderList.instance();
	}
	
	public static Warehouse instance() {
		if(warehouse == null) {
			ProductIdServer.instance();
			SupplierIdServer.instance();
			ClientIdServer.instance();
			OrderIdServer.instance();
			warehouse = new Warehouse();
		}
		return warehouse;
	}
	
	public Product addProduct(String name, float salesPrice, int stock) {
		Product prod = new Product(name, salesPrice, stock);
		if(productList.insertProduct(prod)) return prod;
		
		return null;
	}
	
	public Supplier addSupplier(String name, String address) {
		Supplier supp = new Supplier(name, address);
		if(supplierList.insertSupplier(supp)) return supp;
		
		return null;
	}
	
	public Client addClient(String name, float balance) {
		Client client = new Client(name, balance);
		if(clientList.insertClient(client)) return client;
		
		return null;
	}
	
	public Order addOrder(String clientId) {
		Order order = new Order(clientId, this.getClient(clientId).getCart());
		this.getClient(clientId).clearCart();
		if(orderList.insertOrder(order)) return order;
		
		return null;
	}
	
	public Product getProduct(String id) { return productList.getProduct(id); }
	public Iterator<Product> getProducts() { return productList.getProducts(); }
	public boolean containsProduct(String id) { return productList.contains(id); }
	
	public Supplier getSupplier(String id) { return supplierList.getSupplier(id); }
	public Iterator<Supplier> getSuppliers() { return supplierList.getSuppliers(); }
	public boolean containsSupplier(String id) { return supplierList.contains(id); }
	
	public void pairProductSupplier(String prodId, String suppId, float purchasePrice) { getSupplier(suppId).addSoldProduct(prodId, purchasePrice); }
	
	public Client getClient(String id) { return clientList.getClient(id); }
	public Iterator<Client> getClients() { return clientList.getClients(); }
	public boolean containsClient(String id) { return clientList.contains(id); }
	
	public void creditAccount(String id, float amnt) { clientList.getClient(id).changeBalance(amnt); }
	
	public boolean isCartEmpty(String clientId) { return clientList.getClient(clientId).isCartEmpty(); }
	public void addToClientCart(String clientId, String productId, int quantity) { clientList.getClient(clientId).addToCart(productId, quantity); }
	
	public Iterator<Transaction> getTransactions(String clientId) { return getClient(clientId).getTransactions(); }
	
	public void processOrders() {
		Iterator<Order> it = orderList.getOrders();
		
		while(it.hasNext()) {
			Order ord = it.next();
			HashMap<String, Integer> pq = ord.getProdQuantities();
			
			for(Map.Entry<String, Integer> entry : pq.entrySet()) {
				Product p = getProduct(entry.getKey());
				int quantity = entry.getValue();
				
				if(p.getStock() >= quantity) p.changeStock(quantity * -1); // Enough stock exists to cover
				else { // Not enough stock exists to cover
					quantity -= p.getStock();
					p.changeStock(p.getStock() * -1);
					p.addToWaitlist(ord.getClientId(), quantity);
				}
			}
			
			System.out.println(ord.getClientId() + ", " + ord.getTotalPrice());
			Transaction t = new Transaction("Processed Order", ord.getTotalPrice());
			getClient(ord.getClientId()).changeBalance(ord.getTotalPrice() * -1);
			getClient(ord.getClientId()).addTransaction(t);
			it.remove();
		}
	}
	public void listProductSuppliers(String prodId) {
		Iterator<Supplier> it = getSuppliers();
		
		while(it.hasNext()) {
			Supplier s = it.next();
			if(s.sellsProduct(prodId)) System.out.println("Supplier " + s.getId() + " sells " + prodId + " for $" + s.getBuyPrice(prodId));
		}
	}
	
	public boolean save() {
		try {
			FileOutputStream file = new FileOutputStream("WarehouseData");
			ObjectOutputStream output = new ObjectOutputStream(file);
			output.writeObject(warehouse);
			
			file.close();
			output.close();
			return true;
		}catch(Exception e) { e.printStackTrace(); return false; }
	}
	
	public static Warehouse retrieve() {
		try {
			FileInputStream file = new FileInputStream("WarehouseData");
			ObjectInputStream input = new ObjectInputStream(file);
			input.readObject();
			
			file.close();
			input.close();
			return warehouse;
		} catch (Exception e) { e.printStackTrace(); return null; }
	}
	
	private void writeObject(ObjectOutputStream output) {
		try {
			output.defaultWriteObject();
			output.writeObject(warehouse);
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	private void readObject(ObjectInputStream input) {
		try {
			input.defaultReadObject();
			if(warehouse == null) warehouse = (Warehouse) input.readObject();
			else input.readObject();
		}catch(IOException e) { e.printStackTrace(); }
		 catch(ClassNotFoundException e) { e.printStackTrace(); }
	}
	
	@Override
	public String toString() {
		return productList + "\n" + supplierList + "\n" + clientList;
	}

}
