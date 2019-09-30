package ch.ww.electronics.exception;

/**
 * This class is thrown when item(s) should be removed from an inventory, but
 * the inventory doesn't contain enough of the item.
 */
public class NoSuchItemInInventoryException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7923168647307340274L;

	public NoSuchItemInInventoryException() {
	}

	public NoSuchItemInInventoryException(String message) {
		super(message);
	}

	public NoSuchItemInInventoryException(Throwable cause) {
		super(cause);
	}

	public NoSuchItemInInventoryException(String message, Throwable cause) {
		super(message, cause);
	}
}