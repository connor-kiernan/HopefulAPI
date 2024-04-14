package uk.co.withingtonhopecf.hopefulapi.model;

import uk.co.withingtonhopecf.hopefulapi.model.enums.Position;

public record Player(
	String sub,
	String username,
	String firstName,
	String lastName,
	Integer kitNumber,
	Position position,
	String imageUrl) {

	public Player(String username, String firstName, String lastName, int kitNumber, Position position) {
		this(null, username, firstName, lastName, kitNumber, position, null);
	}
}
