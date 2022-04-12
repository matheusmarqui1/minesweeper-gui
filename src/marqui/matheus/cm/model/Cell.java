package marqui.matheus.cm.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Cell {
	
	private final int line;
	private final int column;
	
	private boolean opened = false;
	private boolean mined = false;
	private boolean marked = false;
	
	private List<Cell> neighbors = new ArrayList<Cell>();
	private Set<CellObserver> observers =  new HashSet<CellObserver>();
	
	Cell(int line, int column){
		this.column = column;
		this.line = line;
	}
	
	public void resgisterObserver(CellObserver observer) {
		observers.add(observer);
	}
	
	private void notifyObservers(CellEvent event) {
		observers.parallelStream().forEach(o -> o.eventOccurred(this, event));
	}
	
	boolean addNeighbor(Cell neighbor) {
		if(neighbor == null || !(neighbor instanceof Cell))
			throw new IllegalArgumentException("The field 'neighbor' needs to be a cell.");
		
		boolean diagonal = neighbor.line != this.line && neighbor.column != this.column;
			
		int distanceBetweenColumns = Math.abs(neighbor.column - this.column);
		int distanceBetweenLines = Math.abs(neighbor.line - this.line);
	
		int distanceBetweenCells = distanceBetweenLines + distanceBetweenColumns;
			
		if(!diagonal && distanceBetweenCells == 1) {
			neighbors.add(neighbor);
			return true;
		}else if (diagonal && distanceBetweenCells == 2) {
			neighbors.add(neighbor);
			return true;
		}else return false;
	}
	
	public void toggleMark() {
		if(!this.opened) {
			this.marked = !this.marked;
			
			if(marked) notifyObservers(CellEvent.MARK);
			else notifyObservers(CellEvent.UNMARK);
		}
	}
	
	public boolean isMarked() {
		return this.marked;
	}
	
	void setOpened(boolean opened) {
		this.opened = opened;
		
		if(this.opened) notifyObservers(CellEvent.OPEN);
	}
	
	public boolean open() {
		if(!opened && !marked) {
			if(this.mined) {
				notifyObservers(CellEvent.EXPLODE);
				return true;
			}
			setOpened(true);
			if(this.secureNeighborhood()) {
				neighbors.forEach((n) -> n.open());
			}
			return true;
		}
		return false;
	}
	
	public boolean isOpened() {
		return this.opened;
	}
	
	void mine() {
		this.mined = true;
	}
	
	public boolean isMined() {
		return mined;
	}

	public boolean secureNeighborhood() {
		Predicate<Cell> isNeighborSecure = (n) -> !n.mined;
		return neighbors.stream().allMatch(isNeighborSecure);
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
	
	boolean goalReached() {
		boolean isDiscoved = !this.mined && this.opened;
		boolean isProtected = this.mined && this.marked;
		
		return isDiscoved ^ isProtected;
	}
	
	public long minesInNeighborhood() {
		return neighbors.stream().filter((n) -> n.mined).count();
	}
	
	void restart() {
		this.opened = false;
		this.mined = false;
		this.marked = false;
		notifyObservers(CellEvent.RESTART);
	}
	
	
}
