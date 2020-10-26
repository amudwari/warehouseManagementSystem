public class OpeningState extends WarehouseState {

	private static final int CLIENT_LOGIN = 0;
	private static final int CLERK_LOGIN = 1;
	private static final int MANAGER_LOGIN = 2;
	private static final int EXIT = 3;
	
	private static OpeningState instance;
	private OpeningState() { super(); }
	
	public static OpeningState instance() {
		if(instance == null) instance = new OpeningState();
		return instance;
	}
	
	public int getCommand() {
		do {
			try {
				int value = Integer.parseInt(getToken("Enter command:" ));
				if (value <= EXIT && value >= CLERK_LOGIN) return value;
			} catch (NumberFormatException e) { System.out.println("Enter a number."); }
		}while (true);
	}
	
	private void client() {
		String clientID = getToken("Please input the client id: ");
		if(Warehouse.instance().containsClient(clientID)) {
			Context.instance().setUser(clientID);
			Context.instance().setLoginLevel(Context.IS_CLIENT);
			Context.instance().changeState(0);
		}else System.out.println("Client with ID " + clientID + " does not exist in the Warehouse.instance().");
	}
	
	private void clerk() {
		Context.instance().setLoginLevel(Context.IS_CLERK);
		System.out.println("WTFg");
		Context.instance().changeState(1);
	}
	
	private void manager() {
		Context.instance().setLoginLevel(Context.IS_MANAGER);
		System.out.println("WTF");
		Context.instance().changeState(2);
	}
	
	public void process() {
		int command;
		System.out.println("Please input:");
		System.out.println(CLIENT_LOGIN + " to login as a client.");
		System.out.println(CLERK_LOGIN + " to login as a clerk.");
		System.out.println(MANAGER_LOGIN + " to login as a manager.");
		System.out.println(EXIT + " to exit.");
		
		while((command = getCommand()) != EXIT) {
			switch(command) {
			case CLIENT_LOGIN: client(); break;
			case CLERK_LOGIN: clerk(); break;
			case MANAGER_LOGIN: manager(); break;
			default: System.out.println("Invalid choice.");
			}
			
			System.out.println("Please input:");
			System.out.println(CLIENT_LOGIN + " to login as a client.");
			System.out.println(CLERK_LOGIN + " to login as a clerk.");
			System.out.println(MANAGER_LOGIN + " to login as a manager.");
			System.out.println(EXIT + " to exit.");
		}
		
		Context.instance().changeState(3);
	}
	
	@Override
	public void run() {
		process();
	}

}
