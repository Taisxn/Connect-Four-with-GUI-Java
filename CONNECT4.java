import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//Creating class CONNECT4 which extends JPanel which is what the game will run on.
//Implements MouseMotionListener for mouse movements, MouseListener for mouse clicks, and Runnable to run the game. 
public class CONNECT4 extends JPanel implements MouseMotionListener, MouseListener, Runnable {
	// Creating object JFrame to create the window for the game application.
	// Creating object Thread for the speed at which the token shows up.
	JFrame frame = new JFrame();
	Thread thread;
	int fWidth = 1024;
	int fHeight = 768;
	int nW = 7;
	int nH = 6;

	public static void main(String[] args) {
		// Creating new Connect4 object.
		new CONNECT4();

	}

	// Creating CONNECT4 object of class CONNECT4.
	public CONNECT4() {
		// Setting the title of the application, making the window of the
		// application visible, setting the size of window
		// Setting the close operation of the game, adding the MouseListener and
		// MouseMotionListener to the frame as well as adding the frame to this
		// method.
		// Adding and starting the thread to this method.
		frame.setTitle("Connect Four");
		frame.setVisible(true);
		frame.setSize(fWidth, fHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addMouseListener(this);
		addMouseMotionListener(this);
		frame.add(this);

		thread = new Thread(this);
		thread.start();

	}

	// Creating 2D arrays to represent the connect four board with nW rows and
	// nH columns.
	// Also creating an array called grid to make ensure each token is placed
	// starting at the bottom of the game-board up.
	// Integers width and height to make the dimensions of each rectangle and
	// oval to create the game-board.
	int[][] num = new int[nW][nH];
	int grid[] = new int[nW];
	int width = fWidth / (nW + 1);
	int height = fHeight / (nH + 1);

	// Creating a method to make the color and shape of each token.
	public void paintComponent(Graphics g2) {
		// Made object Graphics2D to create GUI.
		Graphics2D g = (Graphics2D) g2;
		// Setting the "Stroke" changes the thickness of each line on the board
		// Double for-loop to check if each slot of the board is filled and
		// creates the board with ovals and rectangles.
		// 0 = dark gray = empty slot
		// 1 = pink = Player 1
		// 2 = green = Player 2
		g.clearRect(0, 0, fWidth, fHeight);
		g.setStroke(new BasicStroke(4));
		for (int i = 0; i < nW; i++) {
			for (int j = 0; j < nH; j++) {
				if (num[i][j] != 0) {
					if (num[i][j] == 1)
						g.setColor(Color.PINK);
					else if (num[i][j] == 2)
						g.setColor(Color.GREEN);
					g.fillOval(width * i, height * j, width, height);
				}
				g.setColor(Color.DARK_GRAY);
				g.drawOval(width * i, height * j, width, height);
				g.drawRect(width * i, height * j, width, height);
			}
		}
	}

	// Making the first player's turn 0 from the start of the game.
	// Creating a counter to check if the top of the board is filled meaning the
	// game ends in a draw.
	int turn = 0;
	int draw = 0;

	// 0 = player 1 = pink
	// 1 = player 2 = green
	// Creating method for when the mouse is clicked, @Override is needed to
	// Update the code each time the mouse is clicked.
	@Override
	public void mouseClicked(MouseEvent e) {
		// mgX = Mouse's x position which is the column the mouse is on.
		// mgY = Mouse's y position which is the row the mouse is on.
		// Making conditions to check if the position where the mouse was
		// clicked is within the board.
		// If the condition of the mouse clicked is within the board, then it
		// will proceed to place a token of the designated player into the
		// board.
		if (mgX != -1 && mgY != -1) {
			if (grid[mgX] >= 0) {
				if (turn == 0) {
					num[mgX][grid[mgX]] = 1;
					getWinner(1, mgX, grid[mgX]);
					grid[mgX]--;

				} else {
					num[mgX][grid[mgX]] = 2;
					getWinner(2, mgX, grid[mgX]);
					grid[mgX]--;
				}
				// This for loop checks if the game ends with a Draw
				// If there all of the spaces in the top row are filled then the
				// game ends in a draw
				for (int i = 0; i < nW - 1; i++) {
					if (num[i][0] != 0) {
						draw++;
					} else {
						draw = 0;
						break;
					}
					if (draw == 7) {
						// If the top row is full then a message window will pop
						// up saying the game ends in a draw
						JOptionPane.showMessageDialog(null, "DRAW");
						break;
					}

				}
				// After placing the token it will switch to the second player's
				// turn.
				if (turn == 0)
					turn = 1;
				else
					turn = 0;
			}
		}
	}

	// Created getWinner method to check if there are four tokens in a row;
	// Vertically, horizontally, or diagonally
	public void getWinner(int player, int X, int Y) {
		// If any of these methods return true then it will display the winner
		if (getAcross(X, Y, player) || getUp(X, Y, player) || getTopL(X, Y, player) || getTopR(X, Y, player)) {
			// This pops up a message window saying who the winner is
			JOptionPane.showMessageDialog(null, "Player " + player + " Wins!");
		}

	}

	// Method to check if there are four tokens in a row horizontally
	public boolean getAcross(int x, int y, int pl) {
		while (x - 1 >= 0) {
			if (num[x - 1][y] == pl)
				x--;
			else
				break;
		}
		int counter = 0;
		while (true) {
			if (num[x][y] == pl) {
				counter++;
				x++;
			} else
				break;
			if (counter == 4)
				return true;
			if (x >= nW)
				return false;
		}
		return false;
	}

	// Method to check if there are four tokens in a row vertically
	public boolean getUp(int x, int y, int pl) {
		while (y + 1 < nH) {
			if (num[x][y + 1] == pl)
				y++;
			else
				break;
		}
		int counter = 0;
		while (true) {
			if (num[x][y] == pl) {
				counter++;
				y--;
			} else
				break;
			if (counter == 4)
				return true;
			if (y < 0)
				return false;
		}
		return false;
	}

	// Method to check if there are four tokens in a row diagonally to the
	// top-left
	public boolean getTopL(int x, int y, int pl) {
		while (x + 1 < nW && y + 1 < nH) {
			if (num[x + 1][y + 1] == pl) {
				x++;
				y++;
			} else
				break;
		}
		int counter = 0;
		while (true) {
			if (num[x][y] == pl) {
				counter++;
				y--;
				x--;
			} else
				break;
			if (counter == 4) {
				return true;
			}
			if (y > nH || y < 0) {
				return false;
			} else if (x > nW || x < 0) {
				return false;
			}
		}
		return false;
	}

	// Method to check if there are four tokens in a row diagonally to the
	// top-right
	public boolean getTopR(int x, int y, int pl) {
		while (x - 1 >= 0 && y + 1 < nH) {
			if (num[x - 1][y + 1] == pl) {
				x--;
				y++;
			} else
				break;
		}
		int counter = 0;
		while (true) {
			if (num[x][y] == pl) {
				counter++;
				y--;
				x++;
			} else
				break;
			if (counter == 4) {
				return true;
			}
			if (y < 0) {
				return false;
			} else if (x >= nW) {
				return false;
			}
		}
		return false;
	}

	// Without these methods the MouseListener class will not work as intended
	// and the result any mouse interactions will not work
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	// Creating the run method which runs and updates the game board after each
	// turn.
	@Override
	public void run() {
		// For loop to set the grid array to the correct row values to place the
		// token in the correct row at the bottom of the board; since (0,0) is
		// the top left corner of the board

		for (int i = 0; i < nW; i++) {
			grid[i] = nH - 1;
		}
		// While loop to consistently update the game, set the thread updater to
		// 10 milliseconds and set fWidth and fHeight to the width and height of
		// the frame
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Setting width to the width of each rectangle in the board and
			// setting
			// the height of each rectangle in the board.
			// repaint is used to "repaint" the game board with the tokens
			// placed.
			fWidth = frame.getWidth();
			fHeight = frame.getHeight();
			width = fWidth / (nW + 1);
			height = fHeight / (nH + 1);
			repaint();
		}
	}

	// This method is needed to be able to use the implemented
	// MouseMotionListener class, it constantly updates the mouse's movement.
	@Override
	public void mouseDragged(MouseEvent e) {

	}

	// Setting the x and y position of the mouse to -1
	// Variables mgY and mgX are needed to check which row and column the mouse
	// is on.
	int mgY = -1;
	int mgX = -1;

	// This method consistently updates and stores the x and y position of the
	// mouse
	// As to ensure that the mouse is not clicked off of the board.
	@Override
	public void mouseMoved(MouseEvent e) {
		// Instance variables x and y store the x and y positions of the mouse
		int x = e.getX();
		int y = e.getY();
		// Try and catch used to set variables mgX and mgY to the mouse's
		// position with respect to the rows and columns on the board.
		// For clarification:
		// mgX = (initial mouse x position) - (the modulus of the initial mouse
		// x position with the width of a rectangle on the board) / the width of
		// a rectangle on the board.
		// Conditions to check if the row or column the mouse is on is on the
		// board, if not it will make the value of mgX and mgY equal to -1.
		try {
			mgX = (x - (x % width)) / width;
			mgY = (y - (y % height)) / height;
			if (mgX >= nW)
				mgX = -1;
			if (mgY >= nH)
				mgY = -1;
		} catch (Exception z) {
			mgX = -1;
			mgY = -1;
		}
	}

}