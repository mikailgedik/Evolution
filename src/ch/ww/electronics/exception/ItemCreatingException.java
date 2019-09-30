package ch.ww.electronics.exception;

/**
 * Thrown when an item couldn't be created, i. e. because the required items
 * weren't in the inventory
 */
public class ItemCreatingException extends RuntimeException {
	private static final long serialVersionUID = -4021017959106462297L;

	public ItemCreatingException() {
	}

	public ItemCreatingException(String message) {
		super(message);
	}
}