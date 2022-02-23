package fr.insee.pogues.exceptions;

import lombok.Getter;
import lombok.Setter;

public class PoguesClientException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6576322059017435882L;

	@Getter
	private final int status;
	
	@Getter
	private final String details;

	/**
	 *
	 * @param status
	 * @param message
	 * @param details
	 */
	public PoguesClientException(int status, String message, String details) {
		super(message);
		this.status = status;
		this.details = details;
	}

}