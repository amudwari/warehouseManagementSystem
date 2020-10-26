import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map.Entry;

public class ClientMenuState extends WarehouseState {

	private static final int EXIT = 0;
	private static final int SHOW_CLIENT_DETAILS = 1;
	private static final int SHOW_PRODUCTS_AND_PRICES = 2;
	private static final int LIST_TRANSACTIONS = 3;
	private static final int ADD_TO_CART = 4;
	private static final int EDIT_CART = 5;
	private static final int DISPLAY_WAITLIST = 6;
	private static final int HELP = 7;
	
	private static ClientMenuState instance;
	private ClientMenuState() { super(); }
	
	public static ClientMenuState instance() {
		if(instance == null) instance = new ClientMenuState();
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
	
	public void logout() { 
		if(Context.instance().getLoginLevel() >= Context.IS_CLERK) 
			Context.instance().changeState(0);
		
		else if(Context.instance().getLoginLevel() == Context.IS_CLIENT)
			Context.instance().changeState(1);
		
		else Context.instance().changeState(2); // ERROR
	}
	
	public void showDetails() { System.out.println(Warehouse.instance().getClient(Context.instance().getUser())); }
	
	public void showProductsAndPrices() {
		Iterator<Product> it = Warehouse.instance().getProducts();
		
		DecimalFormat df = new DecimalFormat("0.00");
		while(it.hasNext()) {
			Product p = it.next();
			System.out.println( p.getName() + " | ID: " + p.getId() + " | Sales Price: $" + df.format(p.getSalesPrice()));
		}
	}
	
	public void listTransactions() {
		Iterator<Transaction> it = Warehouse.instance().getTransactions(Context.instance().getUser());
		while(it.hasNext()) System.out.println(it.next());
	}
	
	public void addToCart() {
		do {
			String productId = getToken("Enter product ID:");
			if(Warehouse.instance().containsProduct(productId)) {
				int quantity = Integer.parseInt(getToken("Enter quantity:"));

				Warehouse.instance().addToClientCart(Context.instance().getUser(), productId, quantity);
			}else System.out.println("Product with id " + productId + " does not exist.");

			if(!yesOrNo("Add another item?")) break;
		}while(true);
	}
	
	public void editCart() {
		String clientId = Context.instance().getUser();
		
		do {
			System.out.println("Cart Items:");
			for(Entry<String, Integer> entry : Warehouse.instance().getClient(clientId).getCart().entrySet()) 
				System.out.println(entry.getKey() + ": " + entry.getValue());

			String prodId = getToken("Enter product ID to modify");
			if(Warehouse.instance().getClient(clientId).getCart().containsKey(prodId)) {
				int amnt = Integer.parseInt(getToken("Enter amount"));
				int adjAmnt = Warehouse.instance().getClient(clientId).getCart().get(prodId) + amnt;
				if(adjAmnt <= 0) Warehouse.instance().getClient(clientId).getCart().remove(prodId);
				else Warehouse.instance().getClient(clientId).getCart().replace(prodId, adjAmnt);
			}else System.out.println("Product with ID " + prodId + " does not exist in cart.");

			if(!yesOrNo("Modify a different item?")) break;
		}while(true);
	}
	
	public void displayWaitlist() {
		Iterator<Product> products = Warehouse.instance().getProducts();
		
		while(products.hasNext()) {
			Waitlist w = products.next().getWaitlistedOrderForClient(Context.instance().getUser());
			if(w != null) System.out.println(w);
		}
	}
	
	public void help() {
		System.out.println("Enter a number between " + EXIT + " and " + HELP + " as explained below:");
		System.out.println(EXIT + " to log out.");
		System.out.println(SHOW_CLIENT_DETAILS + " to display your personal details.");
		System.out.println(SHOW_PRODUCTS_AND_PRICES + " to display all products and their prices.");
		System.out.println(LIST_TRANSACTIONS + " to list all transactions.");
		System.out.println(ADD_TO_CART + " to add products to your shopping cart.");
		System.out.println(EDIT_CART + " to edit your shopping cart.");
		System.out.println(DISPLAY_WAITLIST + " to display your waitlist.");
		System.out.println(HELP + " to display this message.");
	}
	
	public void process() {
		int command;
		help();
		while ((command = getCommand()) != EXIT) {
			switch (command) {
			case EXIT: logout(); break;
			case SHOW_CLIENT_DETAILS: showDetails(); break;
			case SHOW_PRODUCTS_AND_PRICES: showProductsAndPrices(); break;
			case LIST_TRANSACTIONS: listTransactions(); break;
			case ADD_TO_CART: addToCart(); break;
			case EDIT_CART: editCart(); break;
			case DISPLAY_WAITLIST: displayWaitlist(); break;
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
