import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

public class Transaction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Date date;
	private String description;
	private float amount;
	
	public Transaction(String description, float amount) {
		date = Date.from(Instant.now());
		
		this.description = description;
		this.amount = amount;
	}
	
	public Date getDate() { return date; }
	public String getDescription() { return description; }
	public float getAmount() { return amount; }
	
	public String toString() {
        return "At " + getDate() + ": " + getDescription() + " for $" + getAmount();
    }

}
