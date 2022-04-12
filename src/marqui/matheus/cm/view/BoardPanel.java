package marqui.matheus.cm.view;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import marqui.matheus.cm.model.Board;

public class BoardPanel extends JPanel {

	private static final long serialVersionUID = 2970892857666096458L;
	private Board board;
	
	public BoardPanel(Board board) {
		this.board = board;
		this.initLayout();
		this.board.registerObserver(e -> {
			SwingUtilities.invokeLater(() -> {
				if(e.booleanValue()) {
					JOptionPane.showMessageDialog(this, "Well done, you won!");
				}else {
					JOptionPane.showMessageDialog(this, "You lose, yep it can happen.");
				}
				board.restart();
			});
		});
	}

	private void initLayout() {
		int lines = board.getLines();
		int columns = board.getColumns();
		this.setLayout(new GridLayout(lines, columns));
		
		this.board.forEachCell(c -> add(new CellButton(c)));
		
	}
}
