import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

// Used to allow common text processing functionality to occur over new classes
public abstract class TextProcessor {

	protected static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	protected String getToken(String prompt) {
		do {
			try {
				System.out.println(prompt);
				String line = reader.readLine();
				StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
				if (tokenizer.hasMoreTokens()) return tokenizer.nextToken();
			} catch (IOException e) { System.exit(0); }
		} while(true);
	}

	public int getNumber(String prompt) {
		do {
			try {
				String item = getToken(prompt);
				Integer num = Integer.valueOf(item);
				return num.intValue();
			} catch (NumberFormatException e) { System.out.println("Please input a number."); }
		} while(true);
	}

	public Calendar getDate(String prompt) {
		do {
			try {
				Calendar date = new GregorianCalendar();
				String item = getToken(prompt);
				DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
				date.setTime(df.parse(item));
				return date;
			}catch(Exception e) { System.out.println("Please input a date as mm/dd/yy."); }
		} while(true);
	}

	protected boolean yesOrNo(String prompt) {
		String more = getToken(prompt + " (Y|y)[es] or anything else for no");
		if(more.charAt(0) != 'y' && more.charAt(0) != 'Y') return false;
		return true;
	}

}
