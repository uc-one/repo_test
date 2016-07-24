////////////////////////////////////////////////////////////
/// Class:     ERR_CODE	
///	Author:    Andrew Upton
///	           Copyright 2011 Upton Consulting gmbh
////////////////////////////////////////////////////////////

package com.uc.sudoku.validate;

/**
 * Convenience enum articulating the three different types of validation error with corresponding error status codes.
 * @author Andrew Upton, © 2011 Upton Consulting gmbh
 */
public enum ERR_CODE {
	
	// File errors 1001..1999
	MISSING_SUDOKU_FILE("1001", "Missing Sudoku filename. " + SudokuValidateUtils.USAGE),
	UNABLE_TO_READ_SUDOKU_FILE("1002", "Failed to read file"),
	WRONG_SUDOKU_FILENAME("1003", "Invalid filename. Please use a file conforming to the extension '*.txt'. " + SudokuValidateUtils.USAGE),
	INVALID_SUDOKU_FILEFORMAT("1004", "Invalid file format. File must contain comma-separated values. "),
	
	// Structure errors 2001..2999
	MISSING_DIGITS_IN_ROW("2001", "Not enough digits in row. Exactly 9 digits allowed. "),
	TOO_MANY_DIGITS_IN_ROW("2002", "Too many digits in row.  Exactly 9 columns allowed. "),
	INVALID_CHARACTERS_IN_ROW("2003", "Invalid character in row. Only digits betweeen 1 and 9 are allowed. "),
	NOT_ENOUGH_ROWS_IN_GRID("2004", "Not enough rows in grid.  Exactly 9 rows allowed. "),
	TOO_MANY_ROWS_IN_GRID("2005", "Too many rows in grid.  Exactly 9 rows allowed. "),
	
	// Sudoku logic errors 3001..3999 
	NUMBER_ALREADY_USED("3001", "Number has already been used in this solution complex. Has been replaced by 'X'");
	
	private String statusCode;
	private String description;
	private ERR_CODE(String code, String desc){
		this.statusCode = code;
		this.description = desc;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getErrMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append("Status Code: ").append(statusCode).append(". ").append(description);
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return getErrMessage();
	}
}
