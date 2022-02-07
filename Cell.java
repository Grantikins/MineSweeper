
public class Cell {

	private enum State {COVERED, UNCOVERED};
	private State state;
	private int xCoord, yCoord, mineNumber;
	private boolean hasMine;
	private boolean hasFlag;
	
	public Cell(int x, int y) {
		state = State.COVERED;
		xCoord = x;
		yCoord = y;
		mineNumber = 0;
		hasMine = false;
		hasFlag = false;
	}
	
	public void setMineNumber(int number) {
		this.mineNumber = number;
	}
	
	public boolean hasMine() {
		return this.hasMine;
	}
	
	public void uncover() {
		this.state = State.UNCOVERED;
	}
	
	public int getMineNumber() {
		return this.mineNumber;
	}
	
	public State getState() {
		return this.state;
	}
	
	public int getXCoord() {
		return this.xCoord;
	}
	
	public int getYCoord() {
		return this.yCoord;
	}
	
	public boolean isCovered() {
		if(this.state == State.COVERED)
			return true;
		else 
			return false;
	}
	
	public void giveMine() {
		this.hasMine = true;
	}
	
	public boolean hasFlag() {
		return hasFlag;
	}
	
	public void giveFlag() {
		this.hasFlag = true;
	}
	
	public void removeFlag() {
		this.hasFlag = false;
	}
	
	
}
