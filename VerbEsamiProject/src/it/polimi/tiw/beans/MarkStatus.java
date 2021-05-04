package it.polimi.tiw.beans;

//Enum: https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
public enum MarkStatus {
	NONINSERITO(0),
	INSERITO(1),
	PUBBLICATO(2),
	RIFIUTATO(3),
	VERBALIZZATO(4);
	

	private final int value;

	MarkStatus(int value) {
		this.value = value;
	}

	public static MarkStatus getMarkStatusFromInt(int value) {
		switch (value) {
		case 0:
			return MarkStatus.NONINSERITO;
		case 1:
			return MarkStatus.INSERITO;
		case 2:
			return MarkStatus.PUBBLICATO;
		case 3:
			return MarkStatus.RIFIUTATO;
		case 4:
			return MarkStatus.VERBALIZZATO;
		}
		return null;
	}

	public int getValue() {
		return value;
	}
}

