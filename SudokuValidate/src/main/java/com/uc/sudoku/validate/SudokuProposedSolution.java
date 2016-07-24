////////////////////////////////////////////////////////////
/// Class:     SudokuProposedSolution	
///	Author:    Andrew Upton
///	           Copyright 2011 Upton Consulting gmbh
////////////////////////////////////////////////////////////

package com.uc.sudoku.validate;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
/**
 * Am object that holds the currently known values of the Sudoku puzzle.<br>
 * The Sudoku solution must contain 81 cells of 9 rows by 9 columns 
 * 		that hold integer values between 0 and 9.  Columns are labelled from 0 to 8,
 * 		rows are labelled from 0 to row 8.<br>
 * The Sudoku solution also consists of 9 times 3x3 subgrids, each referenced from 0 to 8.
 * Within the Sudoku grid each cell is uniquely referenced from 0 to 80.
 * 
 * @author Andrew Upton, © 2011 Upton Consulting gmbh 
 */

public class SudokuProposedSolution {
	
	private static Logger LOGGER = Logger.getLogger(SudokuProposedSolution.class);
	
	private int[] cells = new int[81];

	private int[] colsSet = new int[9];

	private int[] rowsSet = new int[9];

	private int[] subgridSet = new int[9];

	// Any validation errors determined during file processing
	private List<ValidationError> errCodes = new ArrayList<ValidationError>();
	
	/**
	 * This method validates the contents of a given file.<br>
	 * Validation fails if:
	 * <ul>
	 * <li>1. the file does not contain a CSV format<li>
	 * <li>2. the file does not contain the correct 9 x 9 grid
	 * <li>3. the grid contains invalid characters. That is, cells must contain digits between 1 an 9</li>
	 * <li>4. empty grid cells are not permitted</li>
	 * <li>5. a digit between 1 and 9 appears more than once per line</li>
	 * </ul>
	 * <p>
	 * Characters are read sequentially from the input stream and placed
	 * into the grid in left-to-right and top-down order.
	 * <p> 
	 * Any validation errors that are discovered are stored in the errors List in this instance.
	 *  
	 * @param     rd  the FileReader 
	 * @return    grid SudokuGrid which may contain errors.
	 */
	public static SudokuProposedSolution validate(Reader rd) throws IOException {
		SudokuProposedSolution grid = new SudokuProposedSolution();

		int row = 0, col = 0, loc = 0;
		boolean lastCharWasComma = true;
		
		// Read to the end of the file
		while (true){

			// sanity check on rows
			if (row > 8) {
				ValidationError err = new ValidationError(ERR_CODE.TOO_MANY_ROWS_IN_GRID, row);
				grid.addErr(err);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(err.getError());
				}
				break;
			}
			
			// Read a character
			int cellVal = rd.read();

			// -1 is returned if the input stream has reached EOF
			if (cellVal < 0) {
				break;
			}

			if (cellVal == ','){
				// comma ... skip to next cell
				
				// sanity check to make sure last char was not also a comma
				if (lastCharWasComma){
					ValidationError err = new ValidationError(ERR_CODE.MISSING_DIGITS_IN_ROW, row, col); 
					grid.addErr(err);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(err.getError());
					}
					return grid;
				}
				
				lastCharWasComma = true;
				continue;
			} else if (cellVal == '\n') {
				// newline ... skip to next row

				//sanity check on columns
				if (col < 9 ){
					ValidationError err = new ValidationError(ERR_CODE.MISSING_DIGITS_IN_ROW, row, col); 
					grid.addErr(err);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(err.getError());
					}
					return grid;
				}
				
				//sanity check on columns
				if (col > 9 ){
					ValidationError err = new ValidationError(ERR_CODE.TOO_MANY_DIGITS_IN_ROW, row, col); 
					grid.addErr(err);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(err.getError());
					}				
					return grid;
				}

				row ++; 
				col = 0;
				continue;
			} else if (cellVal == '\r'){
				// carriage return ... skip to next row				
				continue;
			} else if (cellVal >= '1' && cellVal <= '9') {
				
				// ensure the file is comma-separated
				if (!lastCharWasComma && col > 0){
					ValidationError err = new ValidationError(ERR_CODE.INVALID_SUDOKU_FILEFORMAT, row); 
					grid.addErr(err);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(err.getError());
					}				
					return grid;
				}
				
				// valid number
				if (!grid.set(loc, cellVal-'0')){
					int badVal = cellVal-'0';
					ValidationError err = new ValidationError(ERR_CODE.NUMBER_ALREADY_USED, row, col, String.valueOf(badVal)); 
					grid.addErr(err);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(err.getError());
					}
				}
			} else {
				ValidationError err = new ValidationError(ERR_CODE.INVALID_CHARACTERS_IN_ROW, row, col, Character.toString((char)cellVal));
				grid.addErr(err);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(err.getError());
				}
			}
			
			loc ++;
			col ++;
			lastCharWasComma = false;	
		}
		
		// sanity check on rows
		if (row < 8) {
			ValidationError err = new ValidationError(ERR_CODE.NOT_ENOUGH_ROWS_IN_GRID, row);
			grid.addErr(err);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(err.getError());
			}
		}

		return grid;
	}


	/**
	 * <b>Many thanks to Patrick Chan for this algorithm</b>
	 * 
	 * Sets a number in a cell. This method checks to see if
	 * 		the cell is allowed to contain the specified number in the given row, 
	 * 		column or subgrid.<br> 
	 * E.g. if the passed number is 5 but the row already has a 5, the cell will
	 *      not be set and false is returned.
	 *      
	 * @param loc  the location of the target cell.
	 *             Values must be in the range [0, 80].
	 * @param num  the number to set in the cell.
	 *             Values must be in the range [1, 9].
	 * @return     true if the set was successful.
	 */
	public boolean set(int loc, int num) {
		// Compute row and column
		int r = loc/9;
		int c = loc%9;
		int blockLoc = (r/3)*3+c/3;

		boolean canSet = cells[loc] == 0
		&& (colsSet[c] & (1<<num)) == 0
		&& (rowsSet[r] & (1<<num)) == 0
		&& (subgridSet[blockLoc] & (1<<num)) == 0;
		if (!canSet) {
			return false;
		}

		cells[loc] = num;
		colsSet[c] |= (1<<num);
		rowsSet[r] |= (1<<num);
		subgridSet[blockLoc] |= (1<<num);
		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int r = 0; r < 9; r++) {
			if (r%3 == 0) {
				sb.append("\n\t\t\t\t-------------------------\n\t\t\t\t");
			} else {
				sb.append("\n\t\t\t\t");
			}
			for (int c=0; c<9; c++) {
				if (c%3 == 0) {
					sb.append("| ");
				}
				int num = cells[r*9+c];
				if (num == 0) {
					sb.append("X ");
				} else {
					sb.append(num+" ");
				}
			}
			sb.append("|");
		}
		sb.append("\n\t\t\t\t-------------------------\n");
		return sb.toString();
	}

	
	// JavaBean accessors 
	public void addErr(ValidationError err) {
		this.errCodes.add(err);
	}
	
	public List<ValidationError> getErrs() {
		return errCodes;
	}
	
	public boolean isInError(){
		return getErrs().size() > 0 ? true : false;
	}
}
