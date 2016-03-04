package it.octograve.jmetarparser;

/**
 * Exception thrown in case of errors during parsing.
 * 
 * @author Alessandro Rossi
 */
public class MetarParsingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	MetarParsingException() {
		super();
	}

	MetarParsingException(String message) {
		super(message);
	}

}
