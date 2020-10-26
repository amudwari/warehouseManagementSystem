import java.io.*;

public class Waitlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private String prodname;
    private String clientId;
    private int quantity;

    public Waitlist(String prodname, String c, int q) {
        this.prodname = prodname;
    	this.clientId = c;
        this.quantity = q;
    }
    
    public String getProductName() {
    	return prodname;
    }

    public String getClientId() {
        return clientId;
    }

    public int getQuantity() {
        return quantity;
    }
    
    @Override
	public String toString() {
		return prodname + ": " + quantity + " for client " + clientId;
	}

}