import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ManagerMenuState extends WarehouseState {
	
	private static final int EXIT = 0;
	private static final int ADD_PRODUCT = 1;
	private static final int ADD_SUPPLIER = 2;
	private static final int LIST_SUPPLIERS = 3;
	private static final int LIST_SUPPLIERS_OF_PRODUCT = 4;
	private static final int LIST_PRODUCTS_OF_SUPPLIER = 5;
	private static final int ADD_SUPPLIER_FOR_PRODUCT = 6;
	private static final int MODIFY_PURCHASE_PRICE = 7;
	private static final int BECOME_CLERK = 8;
	private static final int HELP = 9;
	
	private static ManagerMenuState instance;
	private ManagerMenuState() { super(); }
	
	public static ManagerMenuState instance() {
		if(instance == null) instance = new ManagerMenuState();
		return instance;
	}

	public int getCommand() {
		do {
			try {
				int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help."));
				if (value >= EXIT && value <= HELP) return value;
			} catch (NumberFormatException e) { System.out.println("Enter a number."); }
		} while(true);
	}
	
	public void logout() { Context.instance().changeState(0); }
	
	public void addProducts() {
		Product result;
		do {
			String name = getToken("Enter name");
			String salesPrice = getToken("Enter sales price");
			String stock = getToken("Enter stock");
			result = Warehouse.instance().addProduct(name, Float.parseFloat(salesPrice), Integer.parseInt(stock));
			
			if(result != null) System.out.println(result);
			else System.out.println("Product could not be added.");
			
			if(!yesOrNo("Add more products?")) break;
		}while(true);	
	}
	
	public void addSuppliers() {
		Supplier result;
		do {
			String name = getToken("Enter name");
			String address = getToken("Enter address");
			result = Warehouse.instance().addSupplier(name, address);
			
			if(result != null) System.out.println(result);
			else System.out.println("Supplier could not be added.");
			
			if(!yesOrNo("Add more suppliers?")) break;
		}while(true);	
	}
	
	public void listSuppliers() {
		Iterator<Supplier> suppliers = Warehouse.instance().getSuppliers();
		while(suppliers.hasNext()) System.out.println(suppliers.next().toString());
	}
	
	public void listSuppliersOfProduct() {
		do {
			String productId = getToken("Enter product ID");
			if(Warehouse.instance().containsProduct(productId)) {
				Warehouse.instance().listProductSuppliers(productId);
			}else System.out.println("Product with id " + productId + " does not exist.");
			
			if(!yesOrNo("List suppliers for a different product?")) break;
		}while(true);
	}
	
	public void listProductsOfSupplier() {
		do {
			String id = getToken("Enter supplier ID");
			if(Warehouse.instance().containsSupplier(id)) {
				HashMap<String, Float> hm = Warehouse.instance().getSupplier(id).getSoldProducts();
				
				DecimalFormat df = new DecimalFormat("0.00");
				for(Map.Entry<String, Float> entry : hm.entrySet()) 
					System.out.println("ID: " + entry.getKey() + ", Price: $" + df.format(entry.getValue()));
			}else System.out.println("Supplier with id " + id + " does not exist.");
			
			if(!yesOrNo("List products of a different supplier?")) break;
		}while(true);
	}
	
	public void pairProdSupp() {
		do {
			String prodId = getToken("Enter product ID");
			if(Warehouse.instance().containsProduct(prodId)) {
				String suppId = getToken("Enter supplier ID");
				if(Warehouse.instance().containsSupplier(suppId)) {
					float purchasePrice = Float.parseFloat(getToken("Enter purchase price"));
					
					Warehouse.instance().pairProductSupplier(prodId, suppId, purchasePrice);
				}else System.out.println("Supplier with id " + suppId + " does not exist.");
			}else System.out.println("Product with id " + prodId + " does not exist.");
			
		if(!yesOrNo("Pair a different product and supplier?")) break;
		}while(true);
	}
	
	public void changeProductSalesprice() {
		do {
			String id = getToken("Enter product ID");
			Product p = Warehouse.instance().getProduct(id);
			if(p == null) System.out.println("Product with id " + id + " not found.");
			else {
				float newPrice = Float.parseFloat(getToken("Enter new sales price"));
				p.setSalesPrice(newPrice);
				System.out.println("New sales price has been set.");
			}
			
			if(!yesOrNo("Change a different sales price?")) break;
		}while(true);
	}
	
	public void becomeClerk() { Context.instance().changeState(1); }
	
	public void help() {
		System.out.println("Enter a number between " + EXIT + " and " + HELP + " as explained below:");
		System.out.println(EXIT + " to log out.");
		System.out.println(ADD_PRODUCT + " to add a product.");
		System.out.println(ADD_SUPPLIER + " to add a supplier.");
		System.out.println(LIST_SUPPLIERS + " to show a list of suppliers.");
		System.out.println(LIST_SUPPLIERS_OF_PRODUCT + " to list all suppliers of a given product.");
		System.out.println(LIST_PRODUCTS_OF_SUPPLIER + " to list all products supplied by a supplier.");
		System.out.println(ADD_SUPPLIER_FOR_PRODUCT + " to pair a product and a supplier.");
		System.out.println(MODIFY_PURCHASE_PRICE + " to modify the sales price of a product.");
		System.out.println(BECOME_CLERK + " to become a clerk.");
		System.out.println(HELP + " to display this message.");
	}
	
	public void process() {
		int command;
		help();
		while ((command = getCommand()) != EXIT) {
			switch (command) {
			case EXIT: logout(); break;
			case ADD_PRODUCT: addProducts(); break;
			case ADD_SUPPLIER: addSuppliers(); break;
			case LIST_SUPPLIERS: listSuppliers(); break;
			case LIST_SUPPLIERS_OF_PRODUCT: listSuppliersOfProduct(); break;
			case LIST_PRODUCTS_OF_SUPPLIER: listProductsOfSupplier(); break;
			case ADD_SUPPLIER_FOR_PRODUCT: pairProdSupp(); break;
			case MODIFY_PURCHASE_PRICE: changeProductSalesprice(); break;
			case BECOME_CLERK: becomeClerk(); break;
			case HELP: help(); break;
			}
		}
		
		logout();
	}

	@Override
	public void run() {
		process();
	}

}
