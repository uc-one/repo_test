////////////////////////////////////////////////////////////
/// Class:     SudokuValidate	
///	Author:    Andrew Upton
///	           Copyright 2011 Upton Consulting gmbh
////////////////////////////////////////////////////////////

package com.uc.sudoku.validate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Main class for validating Sudoku solutions.<p>
 * Validates comma-separated-files that represent Sudoku solutions and reports
 * 		any physical (file), structural (data) and/or logical (sudoku rule-based) validation violations.
 *  
 * @author Andrew Upton, © 2011 Upton Consulting gmbh 
 */
public class SudokuValidate {
	
	private static Logger LOGGER = Logger.getLogger(SudokuValidate.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DOMConfigurator.configure("src/main/resources/config/log4j.xml");
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t\t\t########################################");
		sb.append("\n\t\t\t###          Sudoku Validation       ###");
		sb.append("\n\t\t\t###  by Andrew Upton                 ###");
		sb.append("\n\t\t\t###  © 2011 Upton Consulting gmbh    ###");
		sb.append("\n\t\t\t########################################");
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(sb.toString());
		}
		
		ERR_CODE err = validateInput(args); 
		if (err != null){
			LOGGER.error(err.toString());
			return;
		}
		
		try{
			SudokuValidate validator = new SudokuValidate();
			validator.sudokuValidate(args);
		} finally {
			sb = new StringBuilder();
			sb.append("\n\t\t\t########################################");
			sb.append("\n\t\t\t##### Sudoku Validation completed  #####");
			sb.append("\n\t\t\t########################################");

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(sb.toString());
			}
		}
	}

	/**
	 * Validates the structure and logical content of the file<p>
	 * (method made protected for testing purposes)
	 * @param args
	 */
	protected void sudokuValidate(String[] args) {
		SudokuProposedSolution grid = null;
		try{
			FileReader rd = new FileReader(args[0]);
			
			// validate the proferred Sudoku solution file
			grid = SudokuProposedSolution.validate(rd);
			
		} catch (FileNotFoundException e) {
			grid = new SudokuProposedSolution();
			grid.addErr(new ValidationError(ERR_CODE.MISSING_SUDOKU_FILE, e));
		} catch (IOException e) {
			grid = new SudokuProposedSolution();
			grid.addErr(new ValidationError(ERR_CODE.UNABLE_TO_READ_SUDOKU_FILE, e));
		}
		
		printAnalysis(grid, args[0]);
	}

	protected static ERR_CODE validateInput(String[] args) {
		
		if (ArrayUtils.isEmpty(args)){			
			return ERR_CODE.MISSING_SUDOKU_FILE;
		}
		
		if (StringUtils.isBlank(args[0])){
			return ERR_CODE.MISSING_SUDOKU_FILE;
		}
		
		if (!StringUtils.endsWith(args[0], ".txt")){
			return ERR_CODE.WRONG_SUDOKU_FILENAME;
		}
		
		return null;
	}

    private void printAnalysis(SudokuProposedSolution grid, String filename) {
        // Print the grid with the givens
    	if (LOGGER.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("\n\n\t >>> filename: ").append(filename).append(" <<<\n");
			sb.append("\n\t\t\t\tPuzzle Analysis:");
			sb.append(grid).append("\n");
	        if (grid.isInError()){
	        	for (ValidationError error : grid.getErrs()){
	        		sb.append("\n\t !!! INVALID Grid !!! ").append(error.getError()).append("\n");
	        	}
	        } else {
	        	sb.append("\n\t\t\t VALID. Status Code: 0\n");
	        }
			LOGGER.info(sb.toString());
		}
    }
    
}
