package marqui.matheus.cm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Board implements CellObserver{
	final private int lines;
	final private int columns;
	private int minesQty;
	
	final private List<Cell> cells =  new ArrayList<Cell>();
	final private List<Consumer<Boolean>> observers = new ArrayList<>();

	public Board(int lines, int columns, int minesQty) {
		this.lines = lines;
		this.columns = columns;
		this.minesQty = minesQty;
		
		generateCells();
		associateNeighbors();
		putRandomMines();
	}
	
	public void forEachCell(Consumer<Cell> consumer) {
		cells.forEach(consumer);
	}
	
	public void registerObserver(Consumer<Boolean> observer) {
		observers.add(observer);
	}
	
	public void notifyObservers(boolean gameResult) {
		observers.parallelStream().forEach(o -> o.accept(gameResult));
	}
	
	public int getLines() {
		return this.lines;
	}
	
	public int getColumns() {
		return this.columns;
	}
	
	public void open(int line, int column) {
		cells.stream()
			.filter(c -> c.getLine() == line && c.getColumn() == column)
			.findFirst()
			.ifPresent(c -> c.open());
	}
	
	private void showMines() {
		cells.stream().filter(c -> c.isMined()).forEach(c -> c.setOpened(true));
		
	}
	
	public void toggleMark(int line, int column) {
		cells.parallelStream()
			.filter(c -> c.getLine() == line && c.getColumn() == column)
			.findFirst()
			.ifPresent(c -> c.toggleMark());
	}

	private void generateCells() {
		for (int i = 0; i < lines; i++) {
			for (int j = 0; j < columns; j++) {
				Cell cell = new Cell(i, j);
				cell.resgisterObserver(this);
				cells.add(cell);
			}
		}
	}
	
	private void associateNeighbors() {
		for (Cell cell1 : cells) {
			for (Cell cell2 : cells) {
				cell1.addNeighbor(cell2);
			}
		}
	}
	
	private void putRandomMines() {
		long armedMines = 0;
		
		do {
			int aleatoryIndex = (int) (Math.random() * cells.size());
			cells.get(aleatoryIndex).mine();
			armedMines = cells.stream().filter((c) -> c.isMined()).count();
		} while(armedMines < minesQty);
	}
	
	public boolean goalReached() {
		return cells.stream().allMatch(c -> c.goalReached());
	}
	
	public void restart() {
		cells.stream().forEach(c -> c.restart());
		putRandomMines();
	}
	
	@Override
	public void eventOccurred(Cell cell, CellEvent event) {
		if(event == CellEvent.EXPLODE) {
			this.showMines();
			notifyObservers(false);
		}else if(goalReached()) {
			notifyObservers(true);
		}
	}
}
