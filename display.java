package ai;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class display extends JFrame{
	
	private static final int start_x = 50;
	private static final int start_y = 50;
	private static final int length = 75; // the length of grid
	private static final int rw = 300;
	
	private int[][] map = new int[4][4];
	private int size = 0;
	int depth = 0;
	int level;
	int first_second = 0;
	static int start = 0;
	
	private Graphics board;
	
	public display(){
		
		Container p = getContentPane();
		setBounds(100, 100, 500, 500);
		setVisible(true);
		p.setBackground(new Color(0xF4A460));
		setLayout(null);
		setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		try {
			Thread.sleep(50);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		
		board =  this.getGraphics();
		//display the game board
		paintComponents(board);
		
		this.addMouseListener(new MouseAdapter() {

			 @Override
			 public void mouseClicked(MouseEvent e) {   
				search sear;
				int check = 0;
			 	//get x，y of mouseclick
			 	int y = e.getX(), x = e.getY();
			 	//check wether the game is started, does it need some parameters
			 	if(start < 2){
			 		//choose the order and level
			 		//1 : go first, 2 : go after
			 		if(y > 390 && x > 50 && y < 435 && x < 90) first_second = 1;
			 		else if(y > 435 && x > 50 && y < 480 && x < 90) {
			 			first_second = 2;
			 			Xplay(1, 1);
			 		}
			 		//1 : easy, 2 : intermediate, 3 : difficult
			 		if(y > 400 && x > 110 && y < 470 && x < 160) {
			 			level = 1;
			 			depth = 0;
			 		}
			 		else if(y > 400 && x > 210 && y < 470 && x < 260) {
			 			level = 2;
			 			depth = 6;
			 		}
			 		else if(y > 400 && x > 310 && y < 470 && x < 360) {
			 			level = 3;
			 			depth = 12;
			 		}
			 		start++;
			 		//System.out.println(level);
			 		paintComponents(board);
			 		return;
			 	}
			 	//calculate the index of grid
			 	if(x < 50 || y < 50 || x > 350 || y > 350) return;
			 	int cx = (x - start_x) / length, cy = (y - start_y) / length; 
			 	if(map[cx][cy] != 0) return;
			 	Oplay(cx, cy);			 	
			 	sear = new search(size, depth);
			 	//check terminal state
				if((check = sear.test(map, size)) != 0) {
					if(check == 1) board.drawString("Draw!", 90, 450);
					else if(check == 2) board.drawString("You Lose.", 30, 450);
					else board.drawString("You Win!", 50, 450);
				}
				int[] next_move = sear.a_b_search(map);
				if(map[next_move[0]][next_move[1]] == 0) Xplay(next_move[0], next_move[1]);
				//check terminal state
				if((check = sear.test(map, size)) != 0) {
					if(check == 1) board.drawString("Draw!", 90, 450);
					else if(check == 2) board.drawString("You Lose.", 30, 450);
					else board.drawString("You Win!", 50, 450);
				}
			 	paintComponents(board);
			 	
			 }
		});
	}
	
	public void paintComponents(Graphics g) { //draw the board and chessmen
		try{
			g.setFont(new Font("Arial", 0, 15));
			if(this.start < 1)  g.drawString("Please select the level of difficulty", 20, 363);
			else if(this.start < 2)  g.drawString("Please select the order", 20, 375);
			
			setTitle("Tic-Tac-Toe");  
			g.setColor(Color.black);
			g.setFont(new Font("Arial", 0, 10));
			
			//draw choose : who go first
			if(first_second == 1) g.setColor(Color.blue);
			g.drawRect(390, 50, 44, 40);
			g.drawString("First", 400, 75);
			g.setColor(Color.black);
			if(first_second == 2) g.setColor(Color.blue);
			g.drawRect(435, 50, 45, 40);
			g.drawString("Second", 440, 75);
			
			//draw levels of difficulty
			g.setColor(Color.black);
			g.setFont(new Font("Arial", 0, 30));
			if(level == 1) g.setColor(Color.blue);
			g.drawRect(400, 110, 70, 50);
			g.drawString("Easy", 402, 143);
			g.setColor(Color.black);
			if(level == 2) g.setColor(Color.blue);
			g.drawRect(400, 210, 70, 50);
			g.drawString("inter", 405, 243);
			g.setColor(Color.black);
			if(level == 3) g.setColor(Color.blue);
			g.drawRect(400, 310, 70, 50);
			g.drawString("diffi", 410, 343);
			
			//draw the chess board
			g.setColor(Color.black);
			g.drawRect(start_x, start_y, rw, rw);
			for(int i = 0; i < 4; i ++) {
				 g.drawLine(start_x + ((i+1) * length), start_y, start_x + ((i+1) * length), start_y + rw); // columns
				 g.drawLine(start_x, start_y + ((i+1) * length), start_x + rw, start_y + ((i+1) * length)); // rows
				 for(int j = 0; j < 4; j++) {
					 drawString(g, i, j);                    
				 }
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void drawString(Graphics g, int i, int j) {
		if(map[i][j] != 0) {
			g.setFont(new Font("Arial", 0, 80));// size of symbol
			if(map[i][j] == 1) g.drawString("X" + "", start_x + (j  * length) + 5, start_y + ((i + 1) * length) - 5);// write X
			else g.drawString("O" + "", start_x + (j  * length) + 5, start_y + ((i + 1) * length) - 5);// write O
		}
	}
	
	void Xplay(int i, int j){  //computer: i is row, j is column
		this.map[i][j] = 1;
		size++;
	}
	
	void Oplay(int i, int j){  //human: i is row, j is column
		this.map[i][j] = -1;
		size++;
	}
	
	int show_size(){
		return this.size;
	}
	
	int[][] return_board(){
		return this.map;
	}
	
	
}
