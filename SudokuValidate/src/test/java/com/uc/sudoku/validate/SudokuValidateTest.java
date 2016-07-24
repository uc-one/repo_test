////////////////////////////////////////////////////////////
/// Class:     SudokuValidateTest	
///	Author:    Andrew Upton
///	           Copyright 2011 Upton Consulting gmbh
////////////////////////////////////////////////////////////
package com.uc.sudoku.validate;
import java.io.FileReader;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Test;


public class SudokuValidateTest extends TestCase{

	private static Logger LOGGER = Logger.getLogger(SudokuValidateTest.class);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DOMConfigurator.configure("src/test/resources/config/log4j.xml");
	}
	
	@Test
	public void testMissingInput() throws Exception {
		ERR_CODE err = SudokuValidate.validateInput(null);
		Assert.assertEquals(ERR_CODE.MISSING_SUDOKU_FILE.getStatusCode(), err.getStatusCode());
	}
	
	@Test
	public void testMissingFilename() throws Exception {
		String [] args = {""};
		ERR_CODE err =  SudokuValidate.validateInput( args );
		Assert.assertEquals(ERR_CODE.MISSING_SUDOKU_FILE.getStatusCode(), err.getStatusCode());
	}

	@Test
	public void testWrongFilenameExtn() throws Exception {
		String [] args = {"puzzle.log"};
		ERR_CODE err =  SudokuValidate.validateInput( args );
		Assert.assertEquals(ERR_CODE.WRONG_SUDOKU_FILENAME.getStatusCode(), err.getStatusCode());
	}

	@Test
	public void testWrongFileFormat() throws Exception {
		String [] args = {"src/test/java/com/uc/sudoku/validate/badPuzzleFormat.txt"};

		try{
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Starting test: testWrongFileFormat() with file: ").append(args[0]);
				LOGGER.info(sb.toString());
			}
			
			FileReader fr = new FileReader(args[0]);
			SudokuProposedSolution grid = SudokuProposedSolution.validate(fr);
			Assert.assertNotNull(grid);
			Assert.assertTrue(grid.isInError());
			Assert.assertEquals(1, grid.getErrs().size());
			Assert.assertEquals(ERR_CODE.INVALID_SUDOKU_FILEFORMAT.getStatusCode(), grid.getErrs().get(0).getErrCode().getStatusCode());
			
		} finally {
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Completed test: testWrongFileFormat()");
				LOGGER.info(sb.toString());
			}
		}	}	
	
	@Test
	public void testPuzzleWithTooFewRows() throws Exception {

		String [] args = {"src/test/java/com/uc/sudoku/validate/brokenPuzzleTooFewRows.txt"};

		try{
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Starting test: testPuzzleWithTooFewRows() with file: ").append(args[0]);
				LOGGER.info(sb.toString());
			}
			
			FileReader fr = new FileReader(args[0]);
			SudokuProposedSolution grid = SudokuProposedSolution.validate(fr);
			Assert.assertNotNull(grid);
			Assert.assertTrue(grid.isInError());
			Assert.assertEquals(1, grid.getErrs().size());
			Assert.assertEquals(ERR_CODE.NOT_ENOUGH_ROWS_IN_GRID.getStatusCode(), grid.getErrs().get(0).getErrCode().getStatusCode());
			
		} finally {
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Completed test: testPuzzleWithTooFewRows()");
				LOGGER.info(sb.toString());
			}
		}
	}

	@Test
	public void testPuzzleWithTooManyRows() throws Exception {

		String [] args = {"src/test/java/com/uc/sudoku/validate/brokenPuzzleTooManyRows.txt"};

		try{
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Starting test: testPuzzleWithTooManyRows() with file: ").append(args[0]);
				LOGGER.info(sb.toString());
			}
			
			FileReader fr = new FileReader(args[0]);
			SudokuProposedSolution grid = SudokuProposedSolution.validate(fr);
			Assert.assertNotNull(grid);
			Assert.assertTrue(grid.isInError());
			Assert.assertEquals(1, grid.getErrs().size());
			Assert.assertEquals(ERR_CODE.TOO_MANY_ROWS_IN_GRID.getStatusCode(), grid.getErrs().get(0).getErrCode().getStatusCode());
			
		} finally {
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Completed test: testPuzzleWithTooManyRows()");
				LOGGER.info(sb.toString());
			}
		}
	}

	@Test
	public void testPuzzleWithTooFewCols() throws Exception {

		String [] args = {"src/test/java/com/uc/sudoku/validate/brokenPuzzleTooFewCols.txt"};

		try{
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Starting test: testPuzzleWithTooFewCols() with file: ").append(args[0]);
				LOGGER.info(sb.toString());
			}
			
			FileReader fr = new FileReader(args[0]);
			SudokuProposedSolution grid = SudokuProposedSolution.validate(fr);
			Assert.assertNotNull(grid);
			Assert.assertTrue(grid.isInError());
			Assert.assertEquals(1, grid.getErrs().size());
			Assert.assertEquals(ERR_CODE.MISSING_DIGITS_IN_ROW.getStatusCode(), grid.getErrs().get(0).getErrCode().getStatusCode());
			
		} finally {
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Completed test: testPuzzleWithTooFewCols()");
				LOGGER.info(sb.toString());
			}
		}
	}

	@Test
	public void testPuzzleWithTooManyCols() throws Exception {

		String [] args = {"src/test/java/com/uc/sudoku/validate/brokenPuzzleTooManyCols.txt"};

		try{
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Starting test: testPuzzleWithTooManyCols() with file: ").append(args[0]);
				LOGGER.info(sb.toString());
			}
			
			FileReader fr = new FileReader(args[0]);
			SudokuProposedSolution grid = SudokuProposedSolution.validate(fr);
			Assert.assertNotNull(grid);
			Assert.assertTrue(grid.isInError());
			Assert.assertEquals(2, grid.getErrs().size());
			Assert.assertEquals(ERR_CODE.NUMBER_ALREADY_USED.getStatusCode(), grid.getErrs().get(0).getErrCode().getStatusCode());
			Assert.assertEquals(ERR_CODE.TOO_MANY_DIGITS_IN_ROW.getStatusCode(), grid.getErrs().get(1).getErrCode().getStatusCode());
			
		} finally {
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Completed test: testPuzzleWithTooManyCols()");
				LOGGER.info(sb.toString());
			}
		}
	}
	
	@Test
	public void testPuzzleWithBadCharacter() throws Exception {

		String [] args = {"src/test/java/com/uc/sudoku/validate/brokenPuzzleBadCharacter.txt"};

		try{
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Starting test: testPuzzleWithBadCharacter() with file: ").append(args[0]);
				LOGGER.info(sb.toString());
			}
			
			FileReader fr = new FileReader(args[0]);
			SudokuProposedSolution grid = SudokuProposedSolution.validate(fr);
			Assert.assertNotNull(grid);
			Assert.assertTrue(grid.isInError());
			Assert.assertEquals(1, grid.getErrs().size());
			Assert.assertEquals(ERR_CODE.INVALID_CHARACTERS_IN_ROW.getStatusCode(), grid.getErrs().get(0).getErrCode().getStatusCode());

			
		} finally {
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Completed test: testPuzzleWithBadCharacter()");
				LOGGER.info(sb.toString());
			}
		}
	}

	@Test
	public void testPuzzleWithMissingCharacter() throws Exception {

		String [] args = {"src/test/java/com/uc/sudoku/validate/brokenPuzzleMissingCharacter.txt"};

		try{
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Starting test: testPuzzleWithMissingCharacter() with file: ").append(args[0]);
				LOGGER.info(sb.toString());
			}
			
			FileReader fr = new FileReader(args[0]);
			SudokuProposedSolution grid = SudokuProposedSolution.validate(fr);
			Assert.assertNotNull(grid);
			Assert.assertTrue(grid.isInError());
			Assert.assertEquals(1, grid.getErrs().size());
			Assert.assertEquals(ERR_CODE.MISSING_DIGITS_IN_ROW.getStatusCode(), grid.getErrs().get(0).getErrCode().getStatusCode());

			
		} finally {
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Completed test: testPuzzleWithMissingCharacter()");
				LOGGER.info(sb.toString());
			}
		}
	}

	/**
	 * Tests a puzzle with two faulty rows
	 * @throws Exception
	 */
	@Test
	public void testBadPuzzle() throws Exception {

		String [] args = {"src/test/java/com/uc/sudoku/validate/badPuzzle.txt"};

		try{
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Starting test: testBadPuzzle() with file: ").append(args[0]);
				LOGGER.info(sb.toString());
			}
			
			FileReader fr = new FileReader(args[0]);
			SudokuProposedSolution grid = SudokuProposedSolution.validate(fr);
			Assert.assertNotNull(grid);
			Assert.assertTrue(grid.isInError());
			Assert.assertEquals(2, grid.getErrs().size());
			Assert.assertEquals(ERR_CODE.NUMBER_ALREADY_USED.getStatusCode(), grid.getErrs().get(0).getErrCode().getStatusCode());
			Assert.assertEquals(ERR_CODE.NUMBER_ALREADY_USED.getStatusCode(), grid.getErrs().get(1).getErrCode().getStatusCode());
			
		} finally {
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Completed test: testBadPuzzle()");
				LOGGER.info(sb.toString());
			}
		}
	}

	@Test
	public void testOKPuzzle() throws Exception{
		String [] args = {"src/test/java/com/uc/sudoku/validate/okPuzzle.txt"};
		
		try{
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Starting test: testOKPuzzle() with file: ").append(args[0]);
				LOGGER.info(sb.toString());
			}
			
			FileReader fr = new FileReader(args[0]);
			SudokuProposedSolution grid = SudokuProposedSolution.validate(fr);
			Assert.assertNotNull(grid);
			Assert.assertTrue(!grid.isInError());
			
		} finally {
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Completed test: testOKPuzzle()");
				LOGGER.info(sb.toString());
			}
		}
	}
	
	/**
	 * Tests yet another correct solution to ensure no bias in the validation algorithm
	 * @throws Exception
	 */
	@Test
	public void testOKPuzzle_2() throws Exception{
		String [] args = {"src/test/java/com/uc/sudoku/validate/okPuzzle_2.txt"};
		
		try{
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Starting test: testOKPuzzle_2() with file: ").append(args[0]);
				LOGGER.info(sb.toString());
			}
			
			FileReader fr = new FileReader(args[0]);
			SudokuProposedSolution grid = SudokuProposedSolution.validate(fr);
			Assert.assertNotNull(grid);
			Assert.assertTrue(!grid.isInError());
			
		} finally {
			if (LOGGER.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("Completed test: testOKPuzzle_2()");
				LOGGER.info(sb.toString());
			}
		}
	}
}
