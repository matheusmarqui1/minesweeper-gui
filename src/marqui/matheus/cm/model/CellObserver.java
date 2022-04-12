package marqui.matheus.cm.model;

@FunctionalInterface
public interface CellObserver {
	public abstract void eventOccurred(Cell cell, CellEvent event);
}
