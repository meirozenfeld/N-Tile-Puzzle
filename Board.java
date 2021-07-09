
public class Board {
	int row;
	int col;
	int [][] board;
	int hamming_distance; // var to know The Hamming distance betweeen a board and the goal board is the number of tiles in the wrong position
	int blank_i,blank_j,blank_i2,blank_j2; // index of blank 1 and 2
	String path; 
	int cost; 
	boolean out; // for DFBnB algo
	int euristic_value;
	public static int order; // order for comparator (if the euristic_value equal)
	
	// init constractor with row and col
	public Board(int r, int c)
	{
		row=r;
		col=c;
		board=new int [row][col];
		hamming_distance=0;
		blank_i=0;
		blank_j=0;
		blank_i2=0;
		blank_j2=0;
		path="";
		cost=0;
		out=false;
		euristic_value=0;
		order =0;


	}
	public Board(Board b) // constractor with board
	{
		this.row=b.row;
		this.col=b.col;
		this.board=new int[row][col];
		hamming_distance=0;
		blank_i=b.blank_i;
		blank_j=b.blank_j;
		blank_i2=b.blank_i2;
		blank_j2=b.blank_j2;
		path=b.path;
		cost=b.cost;
		out=false;
		euristic_value=0;	
		order+=1;

		for(int r = 0; r < board.length; r++) 
		{
			for(int c = 0; c < board[r].length; c++) 
			{
				board[r][c]=b.board[r][c];
			}
		}
	}
	public String toString() {
		String aString = "";
		for(int r = 0; r < board.length; r++) {
			for(int c = 0; c < board[r].length; c++) {
				if(board[r][c]==-1)
				{
					aString +="_ ";
				}
				else
				{
					aString +=board[r][c]+" ";
				}

			}
			aString +="\n";
		}
		return aString;
	}
	
	//This function called when we fill the init and goal boards in Game Class
	public int fill_board(String board_num)
	{
		int sum_blanks=0;
		String[] parts = board_num.split(",");
		int[] num = new int[parts.length];
		for(int n = 0; n < parts.length; n++) {
			if(parts[n].equals("_"))
			{
				num[n]=-1;

			}
			else {
				num[n] = Integer.parseInt(parts[n]);
			}
		}
		int k=0;
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				if(num[k]==-1)
				{
					sum_blanks++;
					if(sum_blanks==1)
					{
						blank_i=i;
						blank_j=j;
					}
					else
					{
						blank_i2=i;
						blank_j2=j;
					}


				}
				board[i][j]=num[k];
				k++;
			}
		}
		return sum_blanks;	
	}	

/*	Method to calculate the Hamming distance betweeen a board and the goal board is
	   the number of tiles in the wrong position
 */
	public int hamming(Board goal)
	{
		int hamm=0;
		for(int r = 0; r < row; r++) 
		{
			for(int c = 0; c < col; c++) 
			{
				if(board[r][c]!=goal.board[r][c])hamm++;
			}
		}
		return hamm;
	}

	/*	Method to calculate  The Manhattan distance between a board and the goal board is the sum of the Manhattan 
	  distances (sum of the vertical and horizontal distance) from the tiles to their goal positions.
*/
	public int manhattan(Board goal) 
	{
		int dis=0;
		for(int r = 0; r < row; r++) 
		{
			for(int c = 0; c < col; c++) 
			{
				boolean flag=false;
				if(board[r][c]!=goal.board[r][c])
				{
					for(int r1 = 0; r1 < goal.row; r1++) {

						for(int c1 = 0; c1 < goal.col; c1++) 
						{
							if(board[r][c]==goal.board[r1][c1]&&board[r][c]!=-1)
							{
								flag=true;
								dis+=Math.max(r, r1)-Math.min(r, r1)+Math.max(c, c1)-Math.min(c, c1);
								break;
							}
						}
						if(flag)break;
					}

				}
			}
		}
		return dis;
	}

// the method return boolean to check if board is the goal
	public boolean isGoal(Board goal)
	{
		if(this.hamming(goal)==0)
		{
			return true;
		}
		else {
			return false;
		}
	}

/*	This method get enum DIRECTION and check if we can move to direction, In addition
	the method get two_blanks var to know if we need to check double moves.
	the method return boolean
*/
	public boolean canMove(Game.DIRECTION dir, Board goal,boolean two_blanks)
	{
		switch(dir) {
		case LL:
			if (two_blanks&&(blank_j==blank_j2)&&(Math.abs(blank_i-blank_i2)==1)&&(blank_j != 0 )&&(blank_j2 != 0 )) {//&& board[blank_i][blank_j-1] != goal.board[blank_i][blank_j-1]
				return true;
			}

			break;
		case UU:
			if (two_blanks&&(blank_i != 0)&&(blank_i2 != 0)&&(blank_i==blank_i2)&&(Math.abs(blank_j-blank_j2)==1)) {//&& board[blank_i-1][blank_j] != goal.board[blank_i-1][blank_j]
				return true;
			}
			break;
		case RR:
			if (two_blanks&&(blank_j==blank_j2)&&(Math.abs(blank_i-blank_i2)==1)&&(blank_j != board[blank_i].length - 1 )&&(blank_j2 != board[blank_i2].length - 1 )) {//&& board[blank_i][blank_j+1] != goal.board[blank_i][blank_j+1]
				return true;
			}
			break;


		case DD:
			if (two_blanks&&(blank_i != board.length - 1) &&(blank_i2 != board.length - 1) &&(blank_i==blank_i2)&&(Math.abs(blank_j-blank_j2)==1)) { //&& board[blank_i+1][blank_j] != goal.board[blank_i+1][blank_j]
				return true;
			}
			break;
		case L1:
			if ((blank_j != 0 )&& board[blank_i][blank_j-1] != -1) {//&& board[blank_i][blank_j-1] != goal.board[blank_i][blank_j-1]
				return true;
			}

			break;
		case L2:
			if (two_blanks&&(blank_j2 != 0 )&& board[blank_i2][blank_j2-1] != -1) {//&& board[blank_i][blank_j-1] != goal.board[blank_i][blank_j-1]
				return true;
			}

			break;
		case U1:
			if ((blank_i != 0) && board[blank_i-1][blank_j] != -1) {//&& board[blank_i-1][blank_j] != goal.board[blank_i-1][blank_j]
				return true;
			}
			break;

		case U2:
			if (two_blanks&&(blank_i2 != 0 )&& board[blank_i2-1][blank_j2] != -1) {//&& board[blank_i-1][blank_j] != goal.board[blank_i-1][blank_j]
				return true;
			}
			break;
		case R1:
			if ((blank_j != board[blank_i].length - 1 )&& board[blank_i][blank_j+1] != -1) {//&& board[blank_i][blank_j+1] != goal.board[blank_i][blank_j+1]
				return true;
			}
			break;
		case R2:
			if (two_blanks&&(blank_j2 != board[blank_i2].length - 1 )&& board[blank_i2][blank_j2+1] != -1) {//&& board[blank_i][blank_j+1] != goal.board[blank_i][blank_j+1]
				return true;
			}
			break;

		case D1:
			if ((blank_i != board.length - 1)&& board[blank_i+1][blank_j] != -1) { //&& board[blank_i+1][blank_j] != goal.board[blank_i+1][blank_j]
				return true;
			}
			break;


		case D2:
			if (two_blanks&&(blank_i2 != board.length - 1)&& board[blank_i2+1][blank_j2] != -1 ) { //&& board[blank_i+1][blank_j] != goal.board[blank_i+1][blank_j]
				return true;
			}
			break;
		default:
		}


		return false;

	}
	
	/*	This method get enum DIRECTION and change the board accordingly
*/
	public void move(Game.DIRECTION dir)
	{
		switch(dir) {
		case LL:
			int tmpLL=board[blank_i][blank_j-1];
			board[blank_i][blank_j-1]=board[blank_i][blank_j];
			board[blank_i][blank_j]=tmpLL;
			blank_j--;

			int tmpLL2=board[blank_i2][blank_j2-1];
			board[blank_i2][blank_j2-1]=board[blank_i2][blank_j2];
			board[blank_i2][blank_j2]=tmpLL2;
			blank_j2--;

			if(tmpLL<tmpLL2)path+=String.valueOf(tmpLL)+"&"+String.valueOf(tmpLL2)+"R-";
			else if(tmpLL>tmpLL2)path+=String.valueOf(tmpLL2)+"&"+String.valueOf(tmpLL)+"R-";
			cost+=6;
			break;
		case UU:
			int tmpUU=board[blank_i-1][blank_j];
			board[blank_i-1][blank_j]=board[blank_i][blank_j];
			board[blank_i][blank_j]=tmpUU;
			blank_i--;

			int tmpUU2=board[blank_i2-1][blank_j2];
			board[blank_i2-1][blank_j2]=board[blank_i2][blank_j2];
			board[blank_i2][blank_j2]=tmpUU2;
			blank_i2--;

			if(tmpUU<tmpUU2)path+=String.valueOf(tmpUU)+"&"+String.valueOf(tmpUU2)+"D-";
			else if(tmpUU>tmpUU2)path+=String.valueOf(tmpUU2)+"&"+String.valueOf(tmpUU)+"D-";
			cost+=7;
			break;
		case RR:
			int tmpRR=board[blank_i][blank_j+1];

			board[blank_i][blank_j+1]=board[blank_i][blank_j];
			board[blank_i][blank_j]=tmpRR;
			blank_j++;

			int tmpRR2=board[blank_i2][blank_j2+1];
			board[blank_i2][blank_j2+1]=board[blank_i2][blank_j2];
			board[blank_i2][blank_j2]=tmpRR2;
			blank_j2++;

			if(tmpRR<tmpRR2)path+=String.valueOf(tmpRR)+"&"+String.valueOf(tmpRR2)+"L-";
			else if(tmpRR>tmpRR2)path+=String.valueOf(tmpRR2)+"&"+String.valueOf(tmpRR)+"L-";
			cost+=6;
			break;
		case DD:
			int tmpDD=board[blank_i+1][blank_j];
			board[blank_i+1][blank_j]=board[blank_i][blank_j];
			board[blank_i][blank_j]=tmpDD;
			blank_i++;

			int tmpDD2=board[blank_i2+1][blank_j2];
			board[blank_i2+1][blank_j2]=board[blank_i2][blank_j2];
			board[blank_i2][blank_j2]=tmpDD2;
			blank_i2++;

			if(tmpDD<tmpDD2)path+=String.valueOf(tmpDD)+"&"+String.valueOf(tmpDD2)+"U-";
			else if(tmpDD>tmpDD2)path+=String.valueOf(tmpDD2)+"&"+String.valueOf(tmpDD)+"U-";
			cost+=7;
			break;
		case L1:
			int tmpL=board[blank_i][blank_j-1];
			board[blank_i][blank_j-1]=board[blank_i][blank_j];
			board[blank_i][blank_j]=tmpL;
			blank_j--;
			path+=String.valueOf(tmpL)+"R-";
			cost+=5;
			break;
		case L2:
			int tmpL2=board[blank_i2][blank_j2-1];
			board[blank_i2][blank_j2-1]=board[blank_i2][blank_j2];
			board[blank_i2][blank_j2]=tmpL2;
			blank_j2--;
			path+=String.valueOf(tmpL2)+"R-";
			cost+=5;
			break;
		case U1:
			int tmpU=board[blank_i-1][blank_j];
			board[blank_i-1][blank_j]=board[blank_i][blank_j];
			board[blank_i][blank_j]=tmpU;
			blank_i--;
			path+=String.valueOf(tmpU)+"D-";
			cost+=5;
			break;
		case U2:
			int tmpU2=board[blank_i2-1][blank_j2];
			board[blank_i2-1][blank_j2]=board[blank_i2][blank_j2];
			board[blank_i2][blank_j2]=tmpU2;
			blank_i2--;
			path+=String.valueOf(tmpU2)+"D-";
			cost+=5;
			if(blank_i>blank_i2)swap_blanks();
			if(blank_i==blank_i2&&blank_j>blank_j2)swap_blanks();
			break;
		case R1:
			int tmpR=board[blank_i][blank_j+1];
			board[blank_i][blank_j+1]=board[blank_i][blank_j];
			board[blank_i][blank_j]=tmpR;
			blank_j++;
			path+=String.valueOf(tmpR)+"L-";
			cost+=5;
			break;
		case R2:
			int tmpR2=board[blank_i2][blank_j2+1];
			board[blank_i2][blank_j2+1]=board[blank_i2][blank_j2];
			board[blank_i2][blank_j2]=tmpR2;
			blank_j2++;
			path+=String.valueOf(tmpR2)+"L-";
			cost+=5;
			break;
		case D1:
			int tmpD=board[blank_i+1][blank_j];
			board[blank_i+1][blank_j]=board[blank_i][blank_j];
			board[blank_i][blank_j]=tmpD;
			blank_i++;
			path+=String.valueOf(tmpD)+"U-";
			cost+=5;
			if(blank_i>blank_i2)swap_blanks();
			if(blank_i==blank_i2&&blank_j>blank_j2)swap_blanks();
			break;
		case D2:
			int tmpD2=board[blank_i2+1][blank_j2];
			board[blank_i2+1][blank_j2]=board[blank_i2][blank_j2];
			board[blank_i2][blank_j2]=tmpD2;
			blank_i2++;
			path+=String.valueOf(tmpD2)+"U-";
			cost+=5;
			break;
		default:
		}
	}


	private void swap_blanks() {
		int tmp=blank_i;
		int tmp2=blank_j;
		blank_i=blank_i2;
		blank_j=blank_j2;
		blank_i2=tmp;
		blank_j2=tmp2;

	}
	// Euristic function to make the calculate and save it in euristic_value field
	public void euristic(Board goal,String algo)
	{
		switch(algo) {
		case "A*":
			euristic_value=(int)(this.cost*0.2+this.manhattan(goal)*0.8+this.hamming(goal)*0.3); //the best of A* = num=537 time=2.28 cost=76
			break;
		case "IDA*":
			euristic_value=(int)(this.cost*0.2+this.manhattan(goal)*0.8+this.hamming(goal)*0.1); // the best of ida num=5301 wrong path
			break;
		case "DFBnB":
			euristic_value=(int)(this.cost*0.2+this.manhattan(goal)*0.8+this.hamming(goal)*0.25); // the best of FDBnb = time=1.18 cost=76///A*=951/// best IDA 5728 wrong path
			break;
		default:
		}
	}
}
