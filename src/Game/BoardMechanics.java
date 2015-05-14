package Game;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import GUI.ConnectFourListener;
import GUI.GamePanel;
import GUI.MainFrame;
import Game.ConnectFourGame;


public class BoardMechanics implements ActionListener, KeyListener{
	private ConnectFourGame c4Game;
	private MainFrame mainFrame;
	private GamePanel gamePanel;
	private ConnectFourListener listener;
	private AI ai;
	private int currentPlayer;
	private ArrayList<ArrayList<Cell>> board;
	private int turn;
	private int curr_row;
	private int difficulty;
	
	public BoardMechanics(ConnectFourGame connectFourGame, MainFrame mFrame, int diff) {
		//Initializing board
		initilize();
		this.mainFrame = mFrame;
		this.c4Game = connectFourGame;
		this.ai = new AI(diff);
		gamePanel = new GamePanel(mainFrame);
		listener = new ConnectFourListener(this, this.gamePanel);
		
	}
	
	private void initilize(){
		board = new ArrayList<ArrayList<Cell>>();
		
		//Populating the board with cells
		for(int row = 0; row < 6; row++){
			board.add(new ArrayList<Cell>());
			for(int col = 0; col < 7;col++){
				board.get(row).add(new Cell(row,col,0));
			}
		}
		//this.print();
		
		turn = 0;
		curr_row = 0;
	}
	
	/**
	 * Drops in a token into the board
	 * @param col 		column to insert token
	 * @param player	owner of token -> can be replaced by turn or whatever
	 */
	public int dropToken(int col) {
		int dRow = -1;
		if(checkMoveValid(col)){
			for(int i = 5; i >=0; i--){
				if(board.get(i).get(col).getValue() == 0){
					board.get(i).get(col).setValue(this.getCurrentPlayer());
					curr_row = i;
					break;
				}
			}
			turn++;
			return curr_row;
		}
		return dRow;
	}
	
	public int aiDropToken(){
		int row = this.ai.makeAIMove(this);
		return row;
	}
	
	/**
	 * Calculates the current player with turns
	 */
	public int getCurrentPlayer() {
		currentPlayer = (turn%2)+1;
		return currentPlayer;
	}
	
	public AI getAI(){
		return this.ai;
	}

	public void print() {
		for (int row = 0; row < 6; row++) {
		    for (int col = 0; col < 7; col++) {
		    	System.out.print(board.get(row).get(col).getValue() + " ");
		    }
		    System.out.println();
		}
		System.out.println();
	}
	
	public boolean isCPU(){
		boolean isCPU = false;
		if(this.difficulty != -1){
			isCPU = true;
		}
		return isCPU;
	}

	/**
	 * Checks if the current move attempt is valid 
	 * @param col      : The column that the player or computer is attempting to drop a token in
	 * @return valid   : If the move is valid or not
	 */
	public boolean checkMoveValid(int col){
		boolean valid = true;
		
		if(col > 6 || col < 0){
			valid = false;
		} else {
			if(board.get(0).get(col).getValue() != 0){
				System.out.println("Full"); //Need to put a label after adding side panels to indicate that the column is full
				valid = false;
			}
		}
		
		
		return valid;
	}
	
	/***
	 * Checks for win at each coin drop at column, col.
	 * @pre			turn is greater than 6.
	 * @param col	column number
	 * @return		0 if there is no win, player number (i.e. 1, 2...) if there is a win.
	 */
	public boolean checkForWin(){
		
		if (turn < 7) return false;		
		
		     
		// check for a horizontal win 
		    for (int row =0; row<6; row++) { 
		    	for (int column=0; column<4; column++) { 
		    		if (board.get(row).get(column).getValue()!= 0 && board.get(row).get(column).getValue() == board.get(row).get(column+1).getValue() && board.get(row).get(column).getValue() == board.get(row).get(column+2).getValue() && board.get(row).get(column).getValue() == board.get(row).get(column+3).getValue()) { 
		    			return true; 
		            }        
		    	}      
		    }
		 
		 
		  // check for a vertical win 
		    for (int row=0; row<3; row++) { 
		       for (int column=0; column<7; column++) { 
		          if (board.get(row).get(column).getValue() != 0 && 
		        	 board.get(row).get(column).getValue() == board.get(row+1).get(column).getValue() && 
		        	 board.get(row).get(column).getValue() == board.get(row+2).get(column).getValue() && 
		        	 board.get(row).get(column).getValue() == board.get(row+3).get(column).getValue()) { 
		        	 return true; 
		          }  
		       }       
		    }    
		     
		    // check for a diagonal win (positive slope) 
		    for (int row=0; row<3; row++) { 
		    	for (int column=0; column<4; column++) { 
		    		if (board.get(row).get(column).getValue() != 0 && 
	    				board.get(row).get(column).getValue() == board.get(row+1).get(column+1).getValue() && 
						board.get(row).get(column).getValue() == board.get(row+2).get(column+2).getValue() && 
						board.get(row).get(column).getValue() == board.get(row+3).get(column+3).getValue()) { 
			           return true; 
			        }        
		    	}      
			}    
		     
		    // check for a diagonal win (negative slope) 
		    for (int row=3; row<6; row++) { 
		      for (int column=0; column<4; column++) { 
		    	  if (board.get(row).get(column).getValue() != 0 && 
					  board.get(row).get(column).getValue() == board.get(row-1).get(column+1).getValue() && 
					  board.get(row).get(column).getValue() == board.get(row-2).get(column+2).getValue() && 
					  board.get(row).get(column).getValue() == board.get(row-3).get(column+3).getValue()) { 
				      return true; 
			      }        
		      }      
		    }    
		     
		    return false; 
	}  
		
	
	public void win(int player) {
		int playAgain = JOptionPane.showConfirmDialog(gamePanel,"Player " + player + " Won!!!\n" + "Would you like to play again?","Winner",JOptionPane.YES_NO_OPTION);
		if(playAgain == 0){		//yes
			restart();
		} else if(playAgain == 1){		//no
			c4Game.viewMenuPanel(mainFrame);
			gamePanel.setVisible(false);
		}
	}
	
	/**
	 * restarting the entire game
	 * 
	 */
	public void restart(){
		initilize();
		
		gamePanel.setVisible(true);
		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();
		gamePanel.restart(mainFrame);
		listener = new ConnectFourListener(this, gamePanel);
//		new BoardMechanics(c4Game, mainFrame);
	}
	
	
	/**
	 * TODO: Need to look over this
	 */
	public void keyPressed(KeyEvent e) {
		System.out.println("Press");
		int k = e.getKeyCode() - 48;
		boolean win = false;
		// numbers one to seven
		if (k >= 0 && k < 8 ) {
			if(checkMoveValid(k)){
				int row = dropToken(k);  
//			    print();
			    if(row != -1){
		        	gamePanel.set(k, row, getCurrentPlayer());
			    }
			    win = checkForWin();
//				System.out.println("Player " + win + " Wins!");
//			    win = true;
			    if(win == true){
			    	win(getCurrentPlayer());
			    }
			}
		}
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}










	
	
	
}
