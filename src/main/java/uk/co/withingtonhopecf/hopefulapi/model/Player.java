package uk.co.withingtonhopecf.hopefulapi.model;

import uk.co.withingtonhopecf.hopefulapi.model.enums.Position;

public record Player(String firstName,
					 String lastName,
					 int kitNumber,
					 Position position) {}
