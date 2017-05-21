package ai;

public class search {
	//utility value Draw:1 Win:1001 Lose:-999
	int[][] value = new int[4][4]; //record the utility value of every move
	int size; //how many grids have been used
	int number_node = 0;   // total number of nodes generated
	int max_prune = 0, min_prune = 0; 
	boolean cutoff = false;  //wether the cutoff occured
	int depth; 
	
	search(int board_size, int d){
		this.size = board_size;
		this.depth = d;
	}
	
	int[] a_b_search(int[][] board){
		int[] ret = new int[2];
		int[][] temp_board = new int[4][4];
		//copy the state of the game and use for computing
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				temp_board[i][j] = board[i][j];
			}
		}
		//get max v value for computer
		int v = max_value(temp_board, -999, 1001, size);
		
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(value[i][j] == v && board[i][j] == 0){  //find the position of max value(the next move)
					ret[0] = i;
					ret[1] = j;
					System.out.println("whether cutoff occurred:"+cutoff);
					System.out.println("maximum depth reached:"+depth);
					System.out.println("total number of nodes generated:"+number_node);
					System.out.println("number of times pruning occurred within the MAX-VALUE:"+max_prune);
					System.out.println("number of times pruning occurred within the MIN-VALUE:"+min_prune);
					System.out.println();
					return ret;
				}
			}
		}
		return ret; //return the position of next move
	}
	
	int max_value(int[][] board, int alpha, int beta, int sizes){
		int test_score = test(board, sizes); //terminal state test
		if(test_score == 1) return 1;        //draw
		else if(test_score == 2) return 1001;//win(computer)
		else if(test_score == 3) return -999;//lose(computer)
		if((sizes - size) > depth){ //cutoff test
			int eval = eval(board);
			cutoff = true;
			return eval;
		}
		int v = Integer.MIN_VALUE; //assign the min value to v
		//go through all actions
		for(int i=0; i<4; i++){    
			for(int j=0; j<4; j++){
				if(board[i][j] == 0) {	//this grid is empty
					this.number_node++;
					board[i][j] = 1;
					int temp = min_value(board, alpha, beta, sizes+1);
					if(size == sizes) value[i][j] = temp;
					v = Math.max(v, temp);
					board[i][j] = 0;
					if(v >= beta) {
						this.max_prune++;// here the max prune occure
						return v;
					}
					alpha = Math.max(v, alpha);					
				}
				else continue; // this grid has been used
			}
		}
		return v;
	}
	
	int min_value(int[][] board, int alpha, int beta, int sizes){//human play
		int test_score = test(board, sizes); //terminal state test
		if(test_score == 1) return 1;        //draw
		else if(test_score == 2) return 1001;//win(computer)
		else if(test_score == 3) return -999;//lose(computer)
		if((sizes - size) > depth){  //cutoff test
			int eval = eval(board);
			cutoff = true;
			return eval;
		}
		int v = Integer.MAX_VALUE;
		//go through all actions
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(board[i][j] == 0) {//this grid is empty
					this.number_node++;
					board[i][j] = -1;
					v = Math.min(v, max_value(board, alpha, beta, sizes+1));
					board[i][j] = 0;
					if(v <= alpha) {
						this.min_prune++;
						return v;
					}
					beta = Math.min(v, beta);					
				}
				else continue; // this grid has been used
			}
		}
		return v;
	}
	
	int test(int[][] board, int sizes){ // check final state draw:1 (computer)win:2 (computer)lose:3 other:0
		for(int i=0; i<4; i++){ //row check
			if(board[i][0] == 1 && board[i][1] == 1 && board[i][2] == 1 && board[i][3] == 1) return 2;//win
			if(board[i][0] == -1 && board[i][1] == -1 && board[i][2] == -1 && board[i][3] == -1) return 3;//lose
		}
		for(int j=0; j<4; j++){ //column check
			if(board[0][j] == 1 && board[1][j] == 1 && board[2][j] == 1 && board[3][j] == 1) return 2;//win
			if(board[0][j] == -1 && board[1][j] == -1 && board[2][j] == -1 && board[3][j] == -1) return 3;//lose
		}
		// diagonal check
		if((board[0][0] == 1 && board[1][1] == 1 && board[2][2] == 1 && board[3][3] == 1) ||
				(board[0][3] == 1 && board[1][2] == 1 && board[2][1] == 1 && board[3][0] == 1)) return 2;//win
		if((board[0][0] == -1 && board[1][1] == -1 && board[2][2] == -1 && board[3][3] == -1) ||
				(board[0][3] == -1 && board[1][2] == -1 && board[2][1] == -1 && board[3][0] == -1)) return 3;//lose
		if(sizes == 16){//draw
			depth = Math.abs(size - sizes) - 1;
			return 1;
		}
		return 0;
	}
	//evaluation function
	int eval(int[][] board){
		int ret = 0, threex = 0, twox = 0, onex = 0, threeo = 0, twoo = 0, oneo = 0;
		
		for(int i=0; i<4; i++){//compute how many x's and o's in rows
			if(one(board[i], 1)) onex++;
			else if(two(board[i], 1)) twox++;
			else if(three(board[i], 1)) threex++;
			if(one(board[i], -1)) oneo++;
			else if(two(board[i], -1)) twoo++;
			else if(three(board[i], -1)) threeo++;
		}
		
		for(int j=0; j<4; j++){//compute how many x's and o's in columns
			int[] temp = new int[4];
			for(int i=0; i<4; i++){
				temp[i] = board[i][j];
			}
			if(one(temp, 1)) onex++;
			else if(two(temp, 1)) twox++;
			else if(three(temp, 1)) threex++;
			if(one(temp, -1)) oneo++;
			else if(two(temp, -1)) twoo++;
			else if(three(temp, -1)) threeo++;
		}
		
		//compute how many x's and o's in diagonal
		int[] temp = {board[0][0],board[1][1],board[2][2],board[3][3]};
		if(one(temp, 1)) onex++;
		else if(two(temp, 1)) twox++;
		else if(three(temp, 1)) threex++;
		if(one(temp, -1)) oneo++;
		else if(two(temp, -1)) twoo++;
		else if(three(temp, -1)) threeo++;
		
		int[] temp2 = {board[0][3],board[1][2],board[2][1],board[3][0]};
		if(one(temp2, 1)) onex++;
		else if(two(temp2, 1)) twox++;
		else if(three(temp2, 1)) threex++;
		if(one(temp2, -1)) oneo++;
		else if(two(temp2, -1)) twoo++;
		else if(three(temp2, -1)) threeo++;
		
		ret = (depth-6)*threex + depth/4*twox + onex - ((depth-6)*threeo + depth/4*twoo + oneo);
		return ret;
	}
	
	//check: wether there is one X's or O's
	boolean one(int[] temp, int x_or_o){
		boolean isone = false;
		if(temp[0] == x_or_o && temp[1] == 0 && temp[2] == 0 && temp[3] == 0) isone = true;
		else if(temp[0] == 0 && temp[1] == x_or_o && temp[2] == 0 && temp[3] == 0) isone = true;
		else if(temp[0] == 0 && temp[1] == 0 && temp[2] == x_or_o && temp[3] == 0) isone = true;
		else if(temp[0] == 0 && temp[1] == 0 && temp[2] == 0 && temp[3] == x_or_o) isone = true;
		return isone;
	}
	
	//check: wether there is two X's or O's
	boolean two(int[] temp, int x_or_o){
		boolean istwo = false;
		if(temp[0] == x_or_o && temp[1] == x_or_o && temp[2] == 0 && temp[3] == 0) istwo = true;
		else if(temp[0] == x_or_o && temp[1] == 0 && temp[2] == x_or_o && temp[3] == 0) istwo = true;
		else if(temp[0] == x_or_o && temp[1] == 0 && temp[2] == 0 && temp[3] == x_or_o) istwo = true;
		else if(temp[0] == 0 && temp[1] == x_or_o && temp[2] == x_or_o && temp[3] == 0) istwo = true;
		else if(temp[0] == 0 && temp[1] == x_or_o && temp[2] == 0 && temp[3] == x_or_o) istwo = true;
		else if(temp[0] == 0 && temp[1] == 0 && temp[2] == x_or_o && temp[3] == x_or_o) istwo = true;
		return istwo;
	}
	
	//check: wether there is three X's or O's
	boolean three(int[] temp, int x_or_o){
		boolean isthree = false;
		if(temp[0] == x_or_o && temp[1] == x_or_o && temp[2] == x_or_o && temp[3] == 0) isthree = true;
		else if(temp[0] == 0 && temp[1] == x_or_o && temp[2] == x_or_o && temp[3] == x_or_o) isthree = true;
		else if(temp[0] == x_or_o && temp[1] == 0 && temp[2] == x_or_o && temp[3] == x_or_o) isthree = true;
		else if(temp[0] == x_or_o && temp[1] == x_or_o && temp[2] == 0 && temp[3] == x_or_o) isthree = true;
		return isthree;
	}
	
}
