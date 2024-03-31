package uk.co.withingtonhopecf.hopefulapi.model.enums;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT;
import static uk.co.withingtonhopecf.hopefulapi.model.enums.PositionGroup.ATTACKER;
import static uk.co.withingtonhopecf.hopefulapi.model.enums.PositionGroup.DEFENDER;
import static uk.co.withingtonhopecf.hopefulapi.model.enums.PositionGroup.GOALKEEPER;
import static uk.co.withingtonhopecf.hopefulapi.model.enums.PositionGroup.MIDFIELDER;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = OBJECT)
public enum Position {

	GK("Goalkeeper", GOALKEEPER),
	RB("Right Back", DEFENDER),
	CB("Centre Back", DEFENDER),
	LB("Left Back", DEFENDER),
	RWB("Right Wing Back", DEFENDER),
	LWB("Left Wing Back", DEFENDER),
	DM("Defensive Midfielder", MIDFIELDER),
	RM("Right Midfielder", MIDFIELDER),
	CM("Central Midfielder", MIDFIELDER),
	LM("Left Midfielder", MIDFIELDER),
	AM("Attacking Midfielder", MIDFIELDER),
	RW("Right Winger", ATTACKER),
	SS("Second Striker", ATTACKER),
	LW("Left Winger", ATTACKER),
	CF("Centre Forward", ATTACKER);

	private final String qualifiedPosition;
	private final PositionGroup positionGroup;

	Position(String qualifiedPosition, PositionGroup positionGroup) {
		this.qualifiedPosition = qualifiedPosition;
		this.positionGroup = positionGroup;
	}

}
