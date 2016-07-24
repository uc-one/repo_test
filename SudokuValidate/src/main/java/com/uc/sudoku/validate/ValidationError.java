////////////////////////////////////////////////////////////
/// Class:     ValidationError	
///	Author:    Andrew Upton
///	           Copyright 2011 Upton Consulting gmbh
////////////////////////////////////////////////////////////
package com.uc.sudoku.validate;

import org.apache.commons.lang.StringUtils;

/**
 * Convenience class holding the validation error enum and supporting data, if any
 * @author Andrew Upton, © 2011 Upton Consulting gmbh
 */
public class ValidationError {
	private ERR_CODE errCode;
	private int row = -1;
	private int col = -1;
	private String badValue ;
	private Exception e;

	public ValidationError(ERR_CODE errCode, Exception e) {
		this.errCode = errCode;
		this.e = e;
	}
	
	public ValidationError(ERR_CODE errCode, int row) {
		this.errCode = errCode;
		this.row = row;
		this.col = 0;
	}
	
	public ValidationError(ERR_CODE errCode, int row, int col) {
		this.errCode = errCode;
		this.row = row;
		this.col = col;
	}

	public ValidationError(ERR_CODE errCode, int row, int col, String badValue) {
		this(errCode, row, col);
		this.badValue = badValue;
	}
	
	public ERR_CODE getErrCode() {
		return errCode;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	public String getBadValue() {
		return badValue;
	}
	
	public String getError(){
		StringBuilder sb = new StringBuilder();
		sb.append(errCode.getErrMessage());
		if (StringUtils.isNotBlank(badValue)) sb.append(", incorrect value: ").append(badValue).append(" ");
		if (row > -1) sb.append("At row: ").append(row);
		if (col > -1) sb.append(", col: ").append(col);
		if (e != null) sb.append("\n").append(e.getStackTrace());
		return sb.toString();
	}
}
