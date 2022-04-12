package marqui.matheus.cm.view;

import javax.swing.JFrame;

import marqui.matheus.cm.model.Board;

public class MainView extends JFrame{

	/**
	 * Using aleatory serialVersionId
	 */
	private static final long serialVersionUID = 7279503256469516967L;
	
	public MainView() {
		Board board = new Board(16, 30, 5);
		
		add(new BoardPanel(board));
		
		setTitle("Mine Sweeper");
		setSize(690, 438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainView();
	}
	
}
