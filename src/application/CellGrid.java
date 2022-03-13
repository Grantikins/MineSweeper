package application;

import java.util.ArrayList;

public class CellGrid {
	
	private int numberOfCells, numberOfMines;
	private Cell[][] cells;
	
	//Makes a new grid; should use 81 for easy and 256 for hard; 81 has 10 mines, 256 has 40 mines
	//The number should be a perfect square
	public CellGrid(int numberOfCells) {
		this.numberOfCells = numberOfCells;
		if(numberOfCells == 256) this.numberOfMines = 40;	// I will just hard code this value in because I intend to use it
		cells = new Cell[(int)Math.sqrt(numberOfCells)][(int)Math.sqrt(numberOfCells)];
		for(int i = 0; i < cells.length; i++) {
			for(int j = 0; j < cells.length; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
	}
	
	
	public void unCoverCell(int x, int y) {
		if(!getCellAt(x, y).isCovered()) return; 	// returns if the cell is already uncovered
		
		Cell[] adjCells = getAdjacentCellsAt(x, y); // gets a list of the cells adjacent to the provided cell
		
		if(getCellAt(x, y).hasMine()) 		// if it has a mine, the game is over
			return;
		else {							// if there is no mine, then count the mines in adjacent cells and give that number to the input cell 
			getCellAt(x, y).setMineNumber(countSurroundingMines(x, y));
			getCellAt(x, y).uncover();
		}
		
		if(!(getCellAt(x, y).getMineNumber() > 0))  {		//if the number of mines is over zero, display that number on the input cell
			for(Cell c : adjCells) {						// if the number of mines is zero, uncover all the adjacent cells
				try {
					unCoverCell(c.getXCoord(), c.getYCoord()); // this will recursively happen until it reaches cells with mines adjacent (mineCount > 0) 
				} catch(NullPointerException n) {}
			}
		}
	}
	
		
	private int countSurroundingMines(int x, int y) {
		Cell[] adjCells = getAdjacentCellsAt(x, y);
		int mineCount =  0;
		
		for(Cell c: adjCells) {
			try {
				if(c.hasMine())
					mineCount++;
			} catch(NullPointerException e) {}
		}
		
		return mineCount;
	}
	
	
	public Cell[] getAdjacentCellsAt(int x, int y) {
		ArrayList<Cell> adjCells = new ArrayList<>(8);
		
			for(int i = -1; i < 2; i++) {
				for(int j = -1; j < 2; j++) {
					if(i == 0 && j == 0) 
						continue;
					try {
						adjCells.add(getCellAt(x + i, y + j));
					} catch(IndexOutOfBoundsException e) {}
				}
			}
		
		Cell[] adjCellsArray = new Cell[8];
		int i = 0;
		for(Cell c : adjCells) {
			adjCellsArray[i++] = (Cell) c;
		}
		
		return adjCellsArray;
	}
	
	
	public void setUpGameAfterFirstClick(int x, int y) {
		int numRequired = this.numberOfMines;
		
		for(int i = 0; i < this.cells.length; i++) {
			for(int j = 0; j < this.cells[0].length; j++) {
				if(i == x && j == y) continue;
				if(numRequired == 0) break;
				if(Math.random() <= (double)this.numberOfMines / (double)this.numberOfCells) {
					getCellAt(i, j).giveMine();
					numRequired--;
				}
			}
		}
		
		this.numberOfMines = this.numberOfMines - numRequired;
		
		unCoverCell(x, y);
	}
	
	
	public boolean checkForGameWin() {
		boolean res = false;
		ArrayList<Cell> mineCells = new ArrayList<>();
		
		for(int i = 0; i < cells.length; i++) {
			for(int j = 0; j < cells[0].length; j++) {
				if(getCellAt(i, j).hasMine()) {
					mineCells.add(getCellAt(i, j));
				}
			}
		}
		
		for(Cell c : mineCells) {
			if(mineCellisCleared(c.getXCoord(), c.getYCoord()))
				res = true;
			else {
				res = false;
				break;
			}
		}
		
		return res;				// true means the player has won; false does not mean the player lost, just that the player still needs to uncover mines to win.
	}
	
	
	private boolean mineCellisCleared(int x, int y) {
		Cell[] adjCells = getAdjacentCellsAt(x, y);
		boolean isCleared = false;
		
		for(Cell c : adjCells) {
			try {
				if(!c.isCovered() || c.hasMine())
					isCleared = true;
				else {
					isCleared = false;
					break;
				}
			} catch(NullPointerException e) {}
		}
		
		return isCleared;
	}
	
	
	public int getNumberOfMines() {
		return this.numberOfMines;
	}
	
	public Cell getCellAt(int x, int y) throws NullPointerException {
		return this.cells[x][y];
	}
	
	public int getNumberOfCells() {
		return this.numberOfCells;
	}
	
}
