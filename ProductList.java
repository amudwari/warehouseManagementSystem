import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//Class by Kevin Gruwell
public class ProductList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Product> products = new LinkedList<Product>();
	private static ProductList productList;
	
	private ProductList() {}
	
	public static ProductList instance() {
		if(productList == null) productList = new ProductList();
		
		return productList;
	}
	
	public boolean insertProduct(Product p) {
		return products.add(p);
	}
	
	public Product getProduct(String id) {
		Iterator<Product> it = products.iterator();
		
		while(it.hasNext()) {
			Product p = it.next();
			
			if(p.getId().equals(id)) return p;
		}
		
		return null;
	}
	
	public Iterator<Product> getProducts() {
		return products.iterator();
	}
	
	public boolean contains(String id) {
        Iterator<Product> products = productList.getProducts();
        while (products.hasNext()) {
            Product prod = products.next();
            if (prod.getId().equals(id)) return true;
        }
        return false;
    }
	
	private void writeObject(ObjectOutputStream output) {
		try {
			output.defaultWriteObject();
			output.writeObject(productList);
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	private void readObject(ObjectInputStream input) {
		try {
			if(productList != null) return;
			else {
				input.defaultReadObject();
				if(productList == null) productList = (ProductList) input.readObject();
				else input.readObject();
			}
		}catch(IOException e) { e.printStackTrace(); }
		 catch(ClassNotFoundException e) { e.printStackTrace(); }
	}
	
	public String toString() {
		return products.toString();
	}

}
