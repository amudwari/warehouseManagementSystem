import java.util.Iterator;
import java.util.Map;

public class ClerkMenuState extends WarehouseState {

	private static final int EXIT = 0;
	private static final int ADD_CLIENT = 1;
	private static final int SHOW_PRODUCTS_FULL = 2;
	private static final int LIST_CLIENTS = 3;
	private static final int LIST_OUTSTANDING_BALANCES = 4;
	private static final int BECOME_CLIENT = 5;
	private static final int SHOW_PRODUCT_WAITLIST = 6;
	private static final int RECEIVE_SHIPMENT = 7;
	private static final int ACCEPT_CLIENT_PAYMENT = 8;
	private static final int HELP = 9;
	
	private static ClerkMenuState instance;
	private ClerkMenuState() { super(); }
	
	public static ClerkMenuState instance() {
		if(instance == null) instance = new ClerkMenuState();
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
		if(Context.instance().getLoginLevel() >= Context.IS_MANAGER) 
			Context.instance().changeState(0);
		
		else if(Context.instance().getLoginLevel() == Context.IS_CLERK)
			Context.instance().changeState(1);
		
		else Context.instance().changeState(3); // ERROR
	}
	
	public void addClients() {
		Client result;
		do {
			String name = getToken("Enter name");
			String balance = getToken("Enter balance");
			result = Warehouse.instance().addClient(name, Float.parseFloat(balance));
			
			if(result != null) System.out.println(result);
			else System.out.println("Client could not be added.");
			
			if(!yesOrNo("Add more clients?")) break;
		}while(true);	
	}
	
	public void showProductsFull() {
		Iterator<Product> it = Warehouse.instance().getProducts();
		while(it.hasNext()) System.out.println(it.next());
	}
	
	public void listClients() {
		Iterator<Client> clients = Warehouse.instance().getClients();
		while(clients.hasNext()) System.out.println(clients.next().toString());
	}
	
	public void listOutstandingBalances() {
		Iterator<Client> it = Warehouse.instance().getClients();
		
		while(it.hasNext()) {
			Client c = it.next();
			if(c.getBalance() < 0) System.out.println(c.toString());
		}
	}
	
	public void becomeClient() {
		String clientID = getToken("Please input the client id: ");
		if(Warehouse.instance().containsClient(clientID)) {
			Context.instance().setUser(clientID);
			Context.instance().changeState(2);
		}else System.out.println("Client with ID " + clientID + " does not exist in the Warehouse.instance().");
	}
	
	public void showProductWaitlists() {
		do {
			String id = getToken("Enter product ID");
			if(Warehouse.instance().containsProduct(id)) {
				Iterator<Waitlist> it = Warehouse.instance().getProduct(id).getWaitlistIterator();
				
				while(it.hasNext()) System.out.println(it.next());
			}else System.out.println("Product with ID " + id + " does not exist.");
			
			if(!yesOrNo("Show waitlists for a different product?")) break;
		}while(true);	
	}
	
	public void receiveShipment() {
		do {
			String suppId = getToken("Enter supplier ID");
			if(Warehouse.instance().containsSupplier(suppId)) {
				do {
					System.out.println("Supplied Products:");
					for(Map.Entry<String, Float> entry : Warehouse.instance().getSupplier(suppId).getSoldProducts().entrySet()) 
						System.out.println(entry.getKey());
					
					String prodId = getToken("Enter product ID to add");
					if(Warehouse.instance().getSupplier(suppId).getSoldProducts().containsKey(prodId)) {
						int amnt = Integer.parseInt(getToken("Enter amount"));
						Warehouse.instance().getProduct(prodId).changeStock(amnt);
					}else System.out.println("Supplier does not supply product with ID " + prodId + ".");
					
					if(!yesOrNo("Receive a different product?")) break;
				}while(true);
			}else System.out.println("Supplier with id " + suppId + " does not exist.");
			
			if(!yesOrNo("Receive shipment from a different supplier?")) break;
		}while(true);
	}
	
	public void acceptClientPayment() {
		do {
			String clientId = getToken("Enter client ID");
			if(Warehouse.instance().containsClient(clientId)) {
				float amnt = Float.parseFloat(getToken("Enter amount"));
				Warehouse.instance().creditAccount(clientId, amnt);
				System.out.println("New Balance: " + Warehouse.instance().getClient(clientId).getBalance());
			}else System.out.println("Client with id " + clientId + " does not exist.");
			
			if(!yesOrNo("Accept payment for a different client?")) break;
		}while(true);
	}
	
	public void help() {
		System.out.println("Enter a number between " + EXIT + " and " + HELP + " as explained below:");
		System.out.println(EXIT + " to log out.");
		System.out.println(ADD_CLIENT + " to add a client.");
		System.out.println(SHOW_PRODUCTS_FULL + " to show information on all products.");
		System.out.println(LIST_CLIENTS + " to show a list of all clients.");
		System.out.println(LIST_OUTSTANDING_BALANCES + " to list all clients with outstanding balances.");
		System.out.println(BECOME_CLIENT + " to become a client.");
		System.out.println(SHOW_PRODUCT_WAITLIST + " to show the waitlists associated with a specific product.");
		System.out.println(RECEIVE_SHIPMENT + " to receive a shipment from a supplier.");
		System.out.println(ACCEPT_CLIENT_PAYMENT + " to accept payment from a client.");
		System.out.println(HELP + " to display this message.");
	}
	
	public void process() {
		int command;
		help();
		while ((command = getCommand()) != EXIT) {
			switch (command) {
			case EXIT: logout(); break;
			case ADD_CLIENT: addClients(); break;
			case SHOW_PRODUCTS_FULL: showProductsFull(); break;
			case LIST_CLIENTS: listClients(); break;
			case LIST_OUTSTANDING_BALANCES: listOutstandingBalances(); break;
			case BECOME_CLIENT: becomeClient(); break;
			case SHOW_PRODUCT_WAITLIST: showProductWaitlists(); break;
			case RECEIVE_SHIPMENT: receiveShipment(); break;
			case ACCEPT_CLIENT_PAYMENT: acceptClientPayment(); break;
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
