public class Context extends TextProcessor {
	
	private int currentUser;
	private int currentState;
	private int[][] nextState;
	
	public static final int IS_CLIENT = 0;
	public static final int IS_CLERK = 1;
	public static final int IS_MANAGER = 2;
	
	private String userID;
	
	private static Warehouse warehouse;
	private static Context context;
	
	private WarehouseState[] states;

	private void retrieve() {
		try {
			Warehouse tempWarehouse = Warehouse.retrieve();
			if(tempWarehouse != null) {
				System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n" );
				warehouse = tempWarehouse;
			} else{
				System.out.println("File doesnt exist; creating new warehouse" );
				warehouse = Warehouse.instance();
			}
		} catch(Exception e) { e.printStackTrace(); }
	}

	public void setLoginLevel(int user) { currentUser = user; }
	public void setUser(String id) { userID = id;}
	public int getLoginLevel() { return currentUser; }
	public String getUser() { return userID; }
	
	public void process(){ states[currentState].run(); }

	private Context() {
		if (yesOrNo("Look for saved data and use it?")) retrieve();
		else warehouse = Warehouse.instance(); 
		
		// set up the FSM and transition table;
		states = new WarehouseState[4];
		
		states[0] = OpeningState.instance();
		states[1] = ClientMenuState.instance();
		states[2] = ClerkMenuState.instance();
		states[3] = ManagerMenuState.instance();
		nextState = new int[4][4];
		
		// LOGIN STATE
		//   to Client           to Clerk              to Manager             Exit
		nextState[0][0] = 1; nextState[0][1] = 2; nextState[0][2] = 3; nextState[0][3] = -1;
		
		// CLIENT STATE
		// back to Clerk       back to Login          Error State           Error State
		nextState[1][0] = 2; nextState[1][1] = 0; nextState[1][2] = -2; nextState[1][3] = -2;
		
		// CLERK STATE
		//back to Manager       back to Login          to Client             Error State
		nextState[2][0] = 3; nextState[2][1] = 0; nextState[2][2] = 1; nextState[2][3] = -2;
		
		// MANAGER STATE
		// back to Login         to Clerk            Error State          Error State
		nextState[3][0] = 0; nextState[3][1] = 2; nextState[3][2] = -2; nextState[3][3] = -2;
		
		currentState = 0;
	}

	public void changeState(int transition) {
		currentState = nextState[currentState][transition];
		
		if (currentState == -2) {System.out.println("Error has occurred"); terminate();}
		if (currentState == -1) terminate();
		
		states[currentState].run();
	}

	private void terminate() {
		if(yesOrNo("Save data?")) {
			if (warehouse.save()) System.out.println("The warehouse has been successfully saved in the file WarehouseData.");
			else System.out.println("There has been an error in saving.");
		}
		System.out.println("Goodbye!"); System.exit(0);
	}

	public static Context instance() {
		if (context == null) context = new Context();
		
		return context;
	}

	public static void main(String[] args) { Context.instance().process(); }

}
