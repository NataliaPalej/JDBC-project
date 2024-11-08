package makeup_store;

@SuppressWarnings("serial")
public class NataliaException extends Exception {
	String message;
	
	public NataliaException(String e) {
		message = e;
	}
	
	public String getMessage() {
		return message;
	}
}
