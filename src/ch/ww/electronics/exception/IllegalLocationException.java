package ch.ww.electronics.exception;

import ch.ww.electronics.game.gameobject.GameObject;

public class IllegalLocationException extends RuntimeException {
	private static final long serialVersionUID = 2670412969716682683L;
	/** The {@link GameObject} with the illegal location. */
	private final GameObject illegalObject;
	
	public IllegalLocationException(String message, Throwable cause, GameObject illegalObject) {
		super(message, cause);
		this.illegalObject = illegalObject;
	}
	
	public GameObject getIllegalObject() {
		return illegalObject;
	}
}