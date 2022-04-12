package marqui.matheus.cm.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import marqui.matheus.cm.model.Cell;
import marqui.matheus.cm.model.CellEvent;
import marqui.matheus.cm.model.CellObserver;

public class CellButton extends JButton 
implements CellObserver, MouseListener {
	
	private static final long serialVersionUID = -884555660456668428L;
	
	private final Color DEFAULT_BG = new Color(184, 184, 184);
	private final Color MARKED_BG = new Color(88, 179, 247);
	private final Color EXPLODE_BG = new Color(189, 66, 68);
	private final Color GREEN_TEXT = new Color(0, 100, 0);
	
	private Cell cell;

	public CellButton(Cell cell) {
		this.cell = cell;
		setBackground(DEFAULT_BG);
		setBorder(BorderFactory.createBevelBorder(0));
		
		addMouseListener(this);
		cell.resgisterObserver(this);
	}

	@Override
	public void eventOccurred(Cell cell, CellEvent event) {
		switch (event) {
		case OPEN: {
			applyStylesOpen();
			break;
		}
		case MARK: {
			applyStylesMark();
			break;
		}
		case EXPLODE: {
			applyStylesExplode();
			break;
		}
		default:
			applyStylesDefault();
		}
	}

	private void applyStylesDefault() {
		setBackground(DEFAULT_BG);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}

	private void applyStylesExplode() {
		setBackground(EXPLODE_BG);
		setForeground(Color.WHITE);
		setText("💣");
		
	}

	private void applyStylesMark() {
		setBackground(MARKED_BG);
		setForeground(Color.BLACK);
		setText("🏁");
	}

	private void applyStylesOpen() {
		
		if(cell.isMined() && !cell.isMarked()) {
			applyStylesExplode();
			return;
		}
		
		this.setBackground(DEFAULT_BG);
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		switch ((int) cell.minesInNeighborhood()) {
		case 1: 
			setForeground(GREEN_TEXT);
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4: case 5: case 6:
			setForeground(Color.YELLOW);
			break;
		default:
			setForeground(Color.PINK);
		}
		
		String value = !cell.secureNeighborhood() ?
				Long.toString(cell.minesInNeighborhood()) :
				"";
		this.setText(value);
	}
	
	// Mouse events interface methods
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) {
			cell.open();
		}else {
			cell.toggleMark();
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
