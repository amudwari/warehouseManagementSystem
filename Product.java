import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// Class by Kevin Gruwell
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String PRODUCT_STRING = "P";
	
	private String name;
	private String id;
	private float salesPrice;
	private int stock;
	
	private List<Waitlist> waitlists = new LinkedList<Waitlist>();
	
	public Product(String name, float salesPrice, int stock) {
		this.name = name;
		this.id = PRODUCT_STRING + ProductIdServer.instance().getId();
		this.salesPrice = salesPrice;
		this.stock += stock;
	}
	
	public String getId() { return id; }
	public String getName() { return name; }
	public float getSalesPrice() { return salesPrice; }
	public int getStock() { return stock; }
	
	public void setSalesPrice(float salesPrice) { this.salesPrice = salesPrice; }
	
	public void changeStock(int stockMod) {
		stock += stockMod;
	}
	
	public void addToWaitlist(String clientId, int quantity) { waitlists.add(new Waitlist(name, clientId, quantity)); }
	
	public int getStockOfWaitlistedOrders() { 
		int tot = 0;
		for(Waitlist w : waitlists) tot += w.getQuantity();
		return tot;
	}
	
	public Waitlist getWaitlistedOrderForClient(String clientId) {
		for(Waitlist w: waitlists) if(w.getClientId() == clientId) return w;
		
		return null;
	}
	
	public Iterator<Waitlist> getWaitlistIterator() { return waitlists.iterator(); }
	
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		return name + 
			   " | ID: " + id + 
			   " | Sales Price: $" + df.format(salesPrice) + 
			   " | Stock: " + stock;
	}

}
