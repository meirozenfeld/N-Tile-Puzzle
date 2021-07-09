import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Stack;

public class Game {
	public static String algo; // var for the alghorithm BFS ,DFID* ,A* ,IDA , DFBnB
	public static int run_time; // var for with time=0 or no time=1
	public static int open; // var for with open list=0 or no open list=1
	public static String board_size; // var for NXM board size
	public static String board_num; // var for NXM-1 or NXM-2 numbers
	public static String board_goal_num;// var for NXM-1 or NXM-2 goal board numbers
	public static Board init_board_game; // init board game
	public static Board goal_board_game; // goal board game
	public enum DIRECTION {RR,DD,LL,UU,R1,D1,L1,U1,R2,D2,L2,U2};// init enum of direction move by the order (opposites direction)
	public static int num_vertex;  // var for numbers of vertex that  developed
	static long startTime; 	// time vars
	static long stopTime;
	static double finalTime;
	static String open_str=""; // string of open list print
	static boolean goal_found; // if goal found or not
	static boolean two_blanks; // if game with two blanks or not

	public Game() throws IOException // constractor for Game
	{
		//init vars
		num_vertex=0; 
		startTime=0;
		stopTime=0;
		finalTime=0;
		goal_found=false;
		two_blanks=false;
		read_input(); // read input text file
		build_boards(); // build the init and goal boards
		int blanks=init_board_game.fill_board(board_num); // fill init board and get the bumbers of blanks 
		if(blanks==2)two_blanks=true;
		goal_board_game.fill_board(board_goal_num);// fill goal board
	}

	/* This void function read the input text file into variables */
	private static void read_input() {
		try {
			File myObj = new File("input.txt");

			Scanner myReader = new Scanner(myObj);
			Queue<String> data = new LinkedList<>();
			while (myReader.hasNextLine()) {
				String line_data = myReader.nextLine();
				data.add(line_data);
			}
			algo=data.poll();
			if (data.peek().equals("with time"))
			{
				run_time=0;
			}
			else {
				run_time=1;
			}
			data.poll();
			if (data.peek().equals("with open"))
			{
				open=0;
			}
			else {
				open=1;
			}
			data.poll();
			board_size=data.poll();

			board_num=data.poll();
			while(!data.peek().equals("Goal state:"))
			{
				board_num+=","+data.poll();
			}
			data.poll(); // remove "Goal state:"
			board_goal_num=data.poll();
			while(!data.isEmpty())
			{
				board_goal_num+=","+data.poll();
			}
			data.clear();
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	/* This void function build the init start board and the goal board
	 with all variables that we read in read_input() function  */
	private static void build_boards() {
		int N=0; // row
		int M=0; // col
		String str="";
		int i=0;
		while(board_size.charAt(i)!='x') // read row size
		{
			str+=board_size.charAt(i);
			i++;
		}
		N=Integer.parseInt(str);
		i++;
		str="";
		while (i<board_size.length())// read col size
		{
			str+=board_size.charAt(i);
			i++;
		}
		M=Integer.parseInt(str);
		//        	System.out.println(N+" "+M);
		init_board_game=new Board(N,M);
		goal_board_game=new Board(N,M);


	/* This void function starting the game with wanted alghorithm */
	}
	public  void lets_play() throws IOException {
		switch(algo) {// the alghorithms BFS ,DFID* ,A* ,IDA , DFBnB
		case "BFS":
			BFS(); 
			break;
		case "DFID":
			DFID();
			break;
		case "A*":
			A();
			break;
		case "IDA*":
			IDA();
			break;
		case "DFBnB":
			DFBnB();
			break;
		default:
			System.out.println("wrong alghorithm");
			break;
		}

	}

	//  *** Alghorithms functions ***
	private static boolean IDA() throws IOException {
		Stack<Board> frontiers = new Stack<Board>();
		Hashtable<String, Board> H = new Hashtable<>();
		init_board_game.euristic(goal_board_game,algo);
		int tresh=init_board_game.euristic_value;
		while(tresh!=Double.POSITIVE_INFINITY)
		{
			int minF=Integer.MAX_VALUE;
			frontiers.add(init_board_game);
			H.put(init_board_game.toString() ,init_board_game);
			while(!frontiers.isEmpty())
			{
				Board b=frontiers.pop();

				if(b.out)H.remove(b.toString());
				else
				{
					b.out=true;
					frontiers.add(b);
					num_vertex++;
					open_str+=num_vertex+"\n"+b.path+"\n"+b.toString()+"\n";

					for (DIRECTION dir : DIRECTION.values()) {// loop of all directions with enem DIRECTION
						if(b.canMove(dir,goal_board_game,two_blanks))// Check if it is possible to move in that direction
						{
							Board newBoard=new Board(b);// create new son board
							newBoard.move(dir);// making the board move
							newBoard.euristic(goal_board_game,algo);
							if(newBoard.euristic_value>tresh)
							{
								minF=Math.min(minF, newBoard.euristic_value);
								//								newBoard=null;
								continue;
							}
							if(H.containsKey(newBoard.toString())&&H.get(newBoard.toString()).out)
							{
								continue;
							}
							if(H.containsKey(newBoard.toString())&&!H.get(newBoard.toString()).out)
							{
								if(H.get(newBoard.toString()).euristic_value>newBoard.euristic_value)
								{
									frontiers.remove(H.get(newBoard.toString()));
									H.remove(newBoard.toString());
								}
								else
								{
									continue;
								}
							}
							if(newBoard.isGoal(goal_board_game))
							{
								goal_found=true;
								Output o=new Output(newBoard.path,num_vertex,newBoard.cost,finalTime,run_time);
								o.writeFile();
								open_str+=num_vertex+1+" Goal Board:\n"+newBoard.path+"\n"+newBoard.toString()+"\n";
								if(open==0)System.out.println(open_str);
								return true;
							}
							frontiers.add(newBoard);
							H.put(newBoard.toString(), newBoard);
						}
					}
				}
			}
			tresh=minF;
			init_board_game.out=false;
		}
		if(!goal_found)
		{
			Output o=new Output("no path!",num_vertex);
			o.writeFile();
		}
		return false;
	}

	/* comparator for Priority Queue in ucs alghorithms */
	static Comparator<Board> comparator1 = new Comparator<Board>() {
		@Override
		public int compare(Board a, Board b) {
			if(a.euristic_value != b.euristic_value)
			{
				return (a.euristic_value - b.euristic_value);
			}
			else { // if even euristic value return the oldest board
				return (a.order-b.order);
			}
		}
	};

	private static int A() throws IOException {
		startTime=System.nanoTime();
		PriorityQueue<Board> frontiers = new PriorityQueue<Board>(comparator1);
		Hashtable<String, Board> open_list = new Hashtable<>();
		Hashtable<String, Board> close_list = new Hashtable<>();
		init_board_game.euristic(goal_board_game,algo);
		frontiers.add(init_board_game);
		open_list.put(init_board_game.toString(), init_board_game);
		while(!frontiers.isEmpty())
		{
			Board b=frontiers.poll();	
			open_list.remove(b.toString());
			if(b.isGoal(goal_board_game))//goal found
			{
				stopTime=System.nanoTime();
				finalTime = ((double)(stopTime - startTime))/ 1_000_000_000;
				goal_found=true;
				num_vertex++;
				open_str+=num_vertex+" Goal Board:\n"+b.path+"\n"+b.toString();
				Output o=new Output(b.path,num_vertex,b.cost,finalTime,run_time);
				o.writeFile();
				if(open==0)System.out.println(open_str);
				return 0;
			}
			close_list.put(b.toString(), b);
			num_vertex++;
			b.manhattan(goal_board_game);
			open_str+=num_vertex+"\n"+b.path+"\n"+b.toString()+"\n";
			for (DIRECTION dir : DIRECTION.values()) { // loop of all directions with enum DIRECTION
				if(b.canMove(dir,goal_board_game,two_blanks)) // Check if it is possible to move in that direction
				{
					Board newBoard=new Board(b); // create new son board
					newBoard.move(dir); // making the board move
					newBoard.euristic(goal_board_game,algo);
					if(!open_list.containsKey(newBoard.toString()) && !close_list.containsKey(newBoard.toString()))
					{
						frontiers.add(newBoard);
						open_list.put(newBoard.toString(), newBoard);
					}
					else {

						if(open_list.containsKey(newBoard.toString())&&(newBoard.euristic_value)<(open_list.get(newBoard.toString()).euristic_value))
						{
							frontiers.remove(open_list.get(newBoard.toString()));
							open_list.put(newBoard.toString(),newBoard);
							frontiers.add(newBoard);
						}
						else {
							newBoard=null;
						}

					}
				}
			}
		}
		if(!goal_found)
		{
			Output o=new Output("no path!",num_vertex);
			o.writeFile();
		}
		return 1;


	}

	private static void DFBnB() throws IOException {
		startTime=System.nanoTime();
		Stack<Board> frontiers = new Stack<Board>();
		Hashtable<String, Board> H = new Hashtable<>();
		frontiers.add(init_board_game);
		H.put(init_board_game.toString() ,init_board_game);
		String path_result="";
		int final_cost=0;
		int t=Integer.MAX_VALUE;
		PriorityQueue<Board> N = new PriorityQueue<Board>(comparator1);
		ArrayList<Board> NN = new ArrayList<Board>();
		while(!frontiers.isEmpty())
		{
			Board b=frontiers.pop();
			if(b.out)H.remove(b.toString());
			else
			{
				b.out=true;
				frontiers.add(b);
				num_vertex++;
				open_str+=num_vertex+"\n"+b.path+"\n"+b.toString()+"\n";
				for (DIRECTION dir : DIRECTION.values())// loop of all directions with enum DIRECTION 
				{
					if(b.canMove(dir,goal_board_game,two_blanks))// Check if it is possible to move in that direction
					{
						Board newBoard=new Board(b);// create new son board
						newBoard.move(dir); // making the board move
						newBoard.euristic(goal_board_game,algo);
						N.add(newBoard);
					}
				}
				while(!N.isEmpty())
				{
					NN.add(N.poll());
				}
				for (int i = 0; i < NN.size(); i++) 
				{
					if(NN.get(i).euristic_value>=t)
					{
						NN.subList(i, NN.size()).clear();
						break;
					}
					else if(H.containsKey(NN.get(i).toString())&&H.get(NN.get(i).toString()).out)
					{
						NN.remove(i);
						i--;
					}
					else if(H.containsKey(NN.get(i).toString())&&!H.get(NN.get(i).toString()).out)
					{
						if(H.get(NN.get(i).toString()).euristic_value<=NN.get(i).euristic_value)
						{
							NN.remove(i); 
							i--;
						}
						else
						{
							frontiers.remove(H.get(NN.get(i).toString()));
							H.remove(NN.get(i).toString());

						}
					}
					else if(NN.get(i).isGoal(goal_board_game))
					{
						goal_found=true;
						t=NN.get(i).euristic_value;
						path_result=NN.get(i).path;
						final_cost=NN.get(i).cost;
						NN.subList(i, NN.size()).clear();
					}
				}

				for (int j = NN.size()-1; j >=0; j--) 
				{
					frontiers.add(NN.get(j));
					H.put(NN.get(j).toString(), NN.get(j));
				}
				NN.clear();
				N.clear();
			}
		}
		stopTime=System.nanoTime();
		finalTime = ((double)(stopTime - startTime))/ 1_000_000_000;
		if(goal_found)
		{
			num_vertex++;
			Output o=new Output(path_result,num_vertex,final_cost,finalTime,run_time);
			o.writeFile();
			open_str+=num_vertex+" Goal Board:\n"+path_result+"\n"+goal_board_game.toString()+"\n";
			if(open==0)System.out.println(open_str);
		}
		else
		{
			o=new Output("no path",num_vertex);
			o.writeFile();
		}
	}



	static Output o=null; // create output object
	private static void DFID() throws IOException {
		String res="";
		startTime=System.nanoTime();
		for (int depth = 1; depth < Integer.MAX_VALUE; depth++) {
			Hashtable<String, Board> frontiers = new Hashtable<>();
			res=limited_dfs(init_board_game,depth,frontiers);
			if(!res.equals("cutoff"))
			{ 
				if(res.equals("done"))
				{
					o.writeFile();
					if(open==0)System.out.println(open_str);
					break;
				}
				else  if(res.equals("fail"))
				{
					o=new Output("no path",num_vertex);
					o.writeFile();
					break;
				}
			}
		}
	}





	private static String limited_dfs(Board b,int limit,Hashtable<String, Board> frontiers) throws IOException {

		if(b.isGoal(goal_board_game))//goal found
		{
			goal_board_game.path=b.path;
			stopTime=System.nanoTime();
			finalTime = ((double)(stopTime - startTime))/ 1_000_000_000;
			num_vertex++;
			if(open==0)open_str+=num_vertex+" Goal Board:\n"+b.path+"\n"+b.toString()+"\n";
			o=new Output(b.path,num_vertex,b.cost,finalTime,run_time);
			return "done";
		}
		else if(limit==0)return "cutoff";
		else {
			frontiers.put(b.toString(), b);
			boolean isCutoff=false;
			for (DIRECTION dir : DIRECTION.values()) // loop of all directions with enem DIRECTION
			{
				if(b.canMove(dir,goal_board_game,two_blanks))// Check if it is possible to move in that direction
				{
					Board newBoard=new Board(b);// create new son board
					newBoard.move(dir);// making the board move
					if(frontiers.containsKey(newBoard.toString()))
					{
						newBoard=null;
						continue;
					}
					String res=limited_dfs(newBoard,limit-1,frontiers);
					if(res.equals("cutoff"))isCutoff=true;
					else if(!res.equals("fail"))return res;
				}
			}
			num_vertex++;
			if(open==0)open_str+=num_vertex+"\n"+b.path+"\n"+b.toString()+"\n";
			frontiers.remove(b.toString());
			if(isCutoff)return "cutoff";
			else {
				return "fail";
			}
		}
	}
	private static int BFS() throws IOException {
		startTime=System.nanoTime();
		Queue<Board> frontiers = new LinkedList<>();
		Hashtable<String, Board> open_list = new Hashtable<>();
		Hashtable<String, Board> close_list = new Hashtable<>();
		frontiers.add(init_board_game);
		open_list.put(init_board_game.path, init_board_game);
		num_vertex++;
		open_str+=num_vertex+"\n"+"Init Board:"+"\n"+init_board_game.toString()+"\n";
		while(!frontiers.isEmpty())
		{
			Board b=frontiers.poll();
			open_list.remove(b.toString());
			close_list.put(b.toString(), b);
			for (DIRECTION dir : DIRECTION.values()) {// loop of all directions with enem DIRECTION
				if(b.canMove(dir,goal_board_game,two_blanks))// Check if it is possible to move in that direction
				{
					Board newBoard=new Board(b);// create new son board
					newBoard.move(dir); // making the board move
					if(!open_list.containsKey(newBoard.toString()) && !close_list.containsKey(newBoard.toString()))
					{
						if(newBoard.isGoal(goal_board_game))//goal found
						{
							stopTime=System.nanoTime();
							finalTime = ((double)(stopTime - startTime))/ 1_000_000_000;
							goal_found=true;
							num_vertex++;
							Output o=new Output(newBoard.path,num_vertex,newBoard.cost,finalTime,run_time);
							o.writeFile();
							open_str+=num_vertex+" Goal Board:\n"+newBoard.path+"\n"+newBoard.toString()+"\n";
							if(open==0)System.out.println(open_str);
							return 0;
						}
						num_vertex++;
						open_str+=num_vertex+"\n"+newBoard.path+"\n"+newBoard.toString()+"\n";
						frontiers.add(newBoard);
						open_list.put(newBoard.toString(), newBoard);
					}
					else {
						newBoard=null;
					}
				}
			}
		}
		if(!goal_found)
		{
			Output o=new Output("no path!",num_vertex);
			o.writeFile();
		}
		return 1;
	}
}
