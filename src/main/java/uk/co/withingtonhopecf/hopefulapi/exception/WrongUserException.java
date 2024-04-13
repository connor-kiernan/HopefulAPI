package uk.co.withingtonhopecf.hopefulapi.exception;

public class WrongUserException extends RuntimeException {

	public WrongUserException() {
		super("You are not authorized to perform this action for this user");
	}

}
