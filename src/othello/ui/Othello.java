package othello.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

public class Othello extends Frame implements Runnable,ActionListener{

	//private BufferedImage plainImage,blackImage,whiteImage,overImage;
	private BufferedImage imgs[];
	static final int BLACK=1,WHITE=2,PLAIN=0;
	//Resource res;
	MenuBar mb;
	int board[][]=new int[8][8];
	int flipboard[][]=new int[8][8];
	int turn;
	Thread me;
	int x_h,y_h;
	Color hc = new Color(255,255,255,150);
	Color exp = new Color(0,65,65);
	int g_w,g_b;
	boolean gameover=false;
	
	protected void processEvent(AWTEvent e) {
		switch(e.getID()){
		case WindowEvent.WINDOW_CLOSING:
			System.exit(0);
			break;
		case MouseEvent.MOUSE_CLICKED:
			mouseClicked((MouseEvent)e);
			break;
		case MouseEvent.MOUSE_MOVED:
			mouseMoved((MouseEvent)e);
			break;
		}
	}
	
	public Othello() {
		
		enableEvents(WindowEvent.WINDOW_CLOSING|MouseEvent.MOUSE_CLICKED|MouseEvent.MOUSE_MOVED);
		
		setSize(350, 330);
		setVisible(true);
		drawMenu();
		resetBoard();
		
		createBufferStrategy(2);
		
		//images
		imgs = new BufferedImage[4];
		imgs[PLAIN] = new BufferedImage(30,30,1);
		imgs[BLACK] = new BufferedImage(30,30,1);
		imgs[WHITE] = new BufferedImage(30,30,1);
		Graphics2D g=(Graphics2D)imgs[PLAIN].getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,30,30);
		g.setColor(Color.GRAY);
		g.fillRect(1,1,28,28);
		g=(Graphics2D)imgs[BLACK].getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,30,30);
		g.setColor(Color.GRAY);
		g.fillRect(1,1,28,28);
		g.setColor(Color.BLACK);
		g.fillOval(2,2,26,26);
		g=(Graphics2D)imgs[WHITE].getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,30,30);
		g.setColor(Color.GRAY);
		g.fillRect(1,1,28,28);
		g.setColor(Color.BLACK);
		g.fillOval(2,2,26,26);
		g.setColor(Color.WHITE);
		g.fillOval(3,3,24,24);
		
		me = new Thread(this);
		me.start();
	}
	private void resetBoard(){
		for(int i=0;i<board.length;i++)
			Arrays.fill(board[i], PLAIN);
		board[3][3]=BLACK;
		board[3][4]=WHITE;
		board[4][3]=WHITE;
		board[4][4]=BLACK;
		

		for(int i=0;i<board.length;i++)
			Arrays.fill(flipboard[i], PLAIN);
		//flipboard[3][3]=BLACK;
		//flipboard[3][4]=WHITE;
		//flipboard[4][3]=WHITE;
		//flipboard[4][4]=BLACK;
		gameover=false;
	}
	
	private void drawMenu(){
		
		MenuItem reset=new MenuItem();
		reset.setLabel("Reset");
		reset.addActionListener(this);
		
		MenuItem exit=new MenuItem();
		exit.setLabel("Exit");
		exit.addActionListener(this);
		
		Menu gameMenu=new Menu();
		gameMenu.setLabel("Game");
		gameMenu.setEnabled(true);
		gameMenu.add(reset);
		gameMenu.add(exit);
		
		mb=new MenuBar();
		mb.add(gameMenu);
		setMenuBar(mb);
	}
	
	private boolean stillFlipping = false;
	private void draw(Graphics2D g){
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(exp);
		g.fillRect(28, 60, 20, 240);
		for(int i=0;i<board.length ;i++){
			g.setColor(Color.WHITE);
			g.drawString(""+(i)+"",35,(i+1)*30+52 );
		}
		g.setColor(exp);
		g.fillRect(50, 302, 240, 20);
		for(int i=0;i<board.length ;i++){
			g.setColor(Color.WHITE);
			g.drawString(""+(i)+"",(i+1)*30+30,316 );
		}
		
		//show score
		g.setColor(Color.BLACK);
		g.drawString("W="+g_w, 300, 100);
		g.drawString("B="+g_b, 300, 120);
		
		int frameseq = 10;
		stillFlipping=false;
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++){
				int ii = getDrawX(i);
				int jj = getDrawY(j);
				if(board[i][j]==0){
					g.drawImage(imgs[PLAIN], ii, jj, this);
				}
				//if(board[i][j]>10 && board[i][j]<20){
				if(board[i][j]==BLACK){
					g.drawImage(imgs[PLAIN], ii, jj, this);
					//g.drawImage(blackImage, ii, jj, this);
					if(flipboard[i][j]>0){
						int tmp=0;
						if(flipboard[i][j]>15){
							tmp=30-flipboard[i][j];
							g.drawImage(imgs[WHITE], ii+tmp, jj, ii+imgs[WHITE].getWidth(this)-tmp, jj+imgs[WHITE].getHeight(this), 0, 0, 30, 30, this);
						}
						else {
							tmp=flipboard[i][j] ;
							g.drawImage(imgs[BLACK], ii+tmp, jj, ii+imgs[BLACK].getWidth(this)-tmp, jj+imgs[BLACK].getHeight(this), 0, 0, 30, 30, this);
						}
						
						flipboard[i][j]--;
						stillFlipping=true;
					}
					else{
						g.drawImage(imgs[BLACK], ii, jj, this);
					}
				}
				if(board[i][j]==WHITE){
					//g.drawImage(whiteImage, ii, jj, this);
					g.drawImage(imgs[PLAIN], ii, jj, this);
					if(flipboard[i][j]>0){
						int tmp=0;
						if(flipboard[i][j]>15){
							tmp=30-flipboard[i][j];
							g.drawImage(imgs[BLACK], ii+tmp, jj, ii+imgs[BLACK].getWidth(this)-tmp, jj+imgs[BLACK].getHeight(this), 0, 0, 30, 30, this);
						}
						else {
							tmp=flipboard[i][j] ;
							g.drawImage(imgs[WHITE], ii+tmp, jj, ii+imgs[WHITE].getWidth(this)-tmp, jj+imgs[WHITE].getHeight(this), 0, 0, 30, 30, this);
						}
						
						flipboard[i][j]--;
						stillFlipping=true;
					}
					else{
						g.drawImage(imgs[WHITE], ii, jj, this);
					}
				}
//				if(board[i][j]==3){
//					g.drawImage(overImage, i*50+50,j*50+60, this);
//				}
				//draw grid muhahahahaha
				//g.drawLine(0, j*50+50, getWidth(), j*50+50);
				//g.drawLine(i*50+50, 0, i*50+50, getHeight());
			}
		//draw highligh
//		if( board[x_h][y_h]==0 ){
			g.setColor(hc);
			g.fillRect(getDrawX(x_h), getDrawY(y_h), 30, 30);
//		}
		
		if(gameover){
			g.setColor(Color.RED);
			g.drawString("Game Over ", getWidth()/2-50, getHeight()/2-20);
			if(g_b>g_w)
				g.drawString("You Win", getWidth()/2-70, getHeight()/2);
			else if(g_b==g_w)
				g.drawString("TIE - you are smart as the dumb computer", getWidth()/2-100, getHeight()/2);
			else
				g.drawString("I Wins - muhahahha", getWidth()/2-100, getHeight()/2);
		}		
	}
	
	private int getDrawX(int v)
	{
		int width = imgs[PLAIN].getWidth(this);
		return (v*width+50);
	}
	
	private int getDrawY(int v)
	{
		int height = imgs[PLAIN].getHeight(this);
		return (v*height+60);
	}
	
	private int getArrX(int v)
	{
		int width = imgs[PLAIN].getWidth(this);
		return (v-50)/width;
	}
	
	private int getArrY(int v)
	{
		int height = imgs[PLAIN].getHeight(this);
		return (v-60)/height;
	}
	
	public void run() {
		
		BufferStrategy bs = getBufferStrategy();
		while(true)
		{
			if(bs == null) bs = getBufferStrategy();
			Graphics2D g = (Graphics2D)bs.getDrawGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			draw(g);
			bs.show();
			try{Thread.sleep(50);}catch(Exception e){}
		}
	}
	
	public static void main(String[] args) {
		Othello display = new Othello();
		display.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Reset")){
			resetBoard();
		}
		else if(cmd.equals("Exit")){
			System.exit(0);
		}
	}
	public void mouseClicked(MouseEvent e) {
		while(stillFlipping) sleep(50);
		stillFlipping=true;
		if(e.getX() < 51 || e.getX() > 289 || 
			e.getY() < 61 || e.getY() > 298	) 
			return;
		System.out.print("\n("+e.getX()+","+e.getY()+") = ");
		int x = getArrX(e.getX());
		int y = getArrY(e.getY());
		System.out.print("("+x+","+y+")");
		if(board[x][y]==PLAIN){
			board[x][y]=BLACK;
			int totcount = countFlips(x, y,true,board);
			if(totcount==0) board[x][y]=PLAIN;
			else{ //computer turn
				//System.out.println("wait..");
				while(stillFlipping) sleep(50);
				//System.out.println("....oooo OK");
				sleep(500);
				//computerTurn();
				//supercomputer(DEPTH);
				playsim(DEPTH, WHITE, board);
				//check if the game is over
				while(!isPossibleToFlip(BLACK)){
					if(isPossibleToFlip(WHITE)){
						//computerTurn();
						//supercomputer(DEPTH);
						playsim(DEPTH, WHITE, board);
					}
					else{//game over
						gameover=true;
						System.out.println("Game Over!!!");
						break;
					}
				}
				
			}
		}
		calculate();
	}
/********************************************************************************/

	public void sleep(long v)
	{
		try{Thread.sleep(v);}catch(Exception e){}
	}
	
	public int countFlips(int x,int y,boolean flip,int board[][])
	{
		int dx,dy;
		int totcount=0,count=0;
/*//			right
		dx=1;dy=0;
		count=counter(x,y,dx,dy);
		totcount+=count;
//		left
		dx=-1;dy=0;
		count=counter(x,y,dx,dy);
		totcount+=count;
//		up
		dx=0;dy=-1;
		count=counter(x,y,dx,dy);
		totcount+=count;
//		down
		dx=0;dy=1;
		count=counter(x,y,dx,dy);
		totcount+=count;
//		upleft
		dx=-1;dy=-1;
		count=counter(x,y,dx,dy);
		totcount+=count;
//		upright
		dx=1;dy=-1;
		count=counter(x,y,dx,dy);
		totcount+=count;
//		dwnleft
		dx=-1;dy=1;
		count=counter(x,y,dx,dy);
		totcount+=count;
//		dwnright
		dx=1;dy=1;
		count=counter(x,y,dx,dy);
		totcount+=count;*/
		for(dx=-1;dx<2;dx++)
			for(dy=-1;dy<2;dy++)
			{
				if(dx==0 && dy==0) continue;
				count=counter(x,y,dx,dy,board);
				totcount+=count;
				if(flip && count>0)
					doFlips(x, y, dx, dy, count,board);
			}
		//System.out.println("totcount="+totcount);
		return totcount;
	}
	
	public int counter(int x,int y,int dx,int dy,int board[][])
	{
		int pcolor = board[x][y];
		int opp = (pcolor == BLACK ? WHITE : BLACK);
		
		int count=0;
		x+=dx;y+=dy;
		while(x>=0 && x<8 && y>=0 && y<8){
			if(board[x][y]==opp) count++;
			else if(board[x][y]==PLAIN) {
				count=0;
				break;
			}
			else {
				break;
			}
			x+=dx;y+=dy;
		}
		if(x<0 || x>7 || y<0 || y>7) count=0;
		//System.out.println(dx+","+dy+" count="+count);
		return count;
	}
	
	public void doFlips(int x,int y,int dx,int dy,int count,int lboard[][])
	{
		int pcolor = lboard[x][y];
		for(;count>0;count--){
			x+=dx;y+=dy;
			lboard[x][y]=pcolor;
			if(lboard == board)
				flipboard[x][y]=30;
		}
	}
	
	/*public void computerTurn()
	{
		int maxcount=0,fx=0,fy=0;
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++){
				if(board[i][j]==PLAIN){
					board[i][j]=WHITE;
					int tmpcount = countFlips(i, j, false,board);
					if(tmpcount>maxcount){
						maxcount = tmpcount;
						fx=i;fy=j;
					}
					board[i][j]=PLAIN;
				}
			}
		if(maxcount > 0){
			System.out.println("comp:"+fx+","+fy);
			board[fx][fy]=WHITE;
			countFlips(fx, fy, true,board);
		}
	}*/
	
	private void calculate()
	{
		int w=0, b=0;
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++){
				if(board[i][j]==BLACK) b++;
				if(board[i][j]==WHITE) w++;
			}
		g_w = w;
		g_b = b;
	}
	
	public boolean isPossibleToFlip(int pcolor)
	{
		
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++){
				if(board[i][j]==PLAIN){
					board[i][j]=pcolor;
					int tmpcount = countFlips(i, j, false,board);
					board[i][j]=PLAIN;
					if(tmpcount > 0) return true;
				}
			}
		return false;
	}
/****************************************************************************/
	int DEPTH=4;
/*	public int supercomputer(int depth)
	{
		if(depth==0) return 0;
//System.out.println("depth="+depth);			
		int mincount=999999,fx=-1,fy=-1;
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++){
				if(board[i][j]==PLAIN){
					board[i][j]=WHITE;
					int mycount = countFlips(i, j, false,board);
					if(mycount>0){//if comp can flip atleat one
						int tmpcount = simulatePlayer(depth);
						if(tmpcount<mincount){//find the min flip count of opp
							mincount=tmpcount;
							fx=i;fy=j;
						}
					}
					board[i][j]=PLAIN;
				}
			}
		if(depth==DEPTH){
			if(fx!=-1){
				board[fx][fy]=WHITE;
				countFlips(fx, fy, true,board);
				System.out.println("comp="+fx+","+fy);
			}
			else{
				System.out.println(" comp: no move");
			}
		}
		return mincount;
	}
	public int simulatePlayer(int depth)
	{
		int maxcount=0;
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++){
				if(board[i][j]==PLAIN){
					board[i][j]=BLACK;
					int tmpcount = countFlips(i, j, false,board);
					if(tmpcount>0){
						int nextcount = supercomputer(depth-1);
						tmpcount+=nextcount;
						if(tmpcount>maxcount){
							maxcount=tmpcount;
						}
					}
					board[i][j]=PLAIN;
				}
			}
		return maxcount;
	}
	
/****************************************************************************/
	//base on count
//private int boarddup[][] = new int[8][8];
public int[][] cloneboard(int board[][])
{
	int boarddup[][] = new int[8][8];
	for(int i=0;i<board.length;i++)
		for(int j=0;j<board.length;j++){
			boarddup[i][j]=board[i][j];
		}
	return boarddup;
}
public int[][] copyboard(int boardsrc[][],int boarddest[][])
{
	for(int i=0;i<boardsrc.length;i++)
		for(int j=0;j<boardsrc.length;j++){
			boarddest[i][j]=boardsrc[i][j];
		}
	return boarddest;
}
public int playsim(int depth,int pcolor,int board[][])
{
	int fx=-1,fy=-1,mincount=99999,fcount=0,maxcount=0;
	int boarddup[][] = new int[8][8];
	int opp=(pcolor == WHITE ? BLACK : WHITE);
	if(depth==0) return countColor(pcolor,board,scoreMap);
	for(int i=0;i<board.length;i++)
		for(int j=0;j<board.length;j++){
			if(board[i][j]==PLAIN){
				copyboard(board, boarddup);
				boarddup[i][j]=pcolor;
				if(depth==1){
					int mycount = countFlips(i, j, true,boarddup);
					if(mycount>maxcount){
						maxcount=mycount;
						fx=i;fy=j;
						fcount=countColor(pcolor,boarddup,scoreMap);
					}
				}
				else{
					int mycount = countFlips(i, j, true,boarddup);
					if(mycount>0){//if atleast one can flip
						int oppcount = playsim(depth-1, opp,boarddup);
						if(oppcount<mincount){
							mincount=oppcount;
							fx=i;fy=j;
							fcount=countColor(pcolor,boarddup,scoreMap);
						}
					}
				}
			}
		}
	if(depth==DEPTH){
		if(fx!=-1){
			board[fx][fy]=pcolor;
			countFlips(fx, fy, true,board);
			//System.out.println("comp("+pcolor+")="+fx+","+fy);
		}
		else{
			System.out.println(" comp: no move");
		}
	}
	return fcount;
}
public int countColor(int pcolor,int boarddup[][],int scoreMap[][])
{
	int count=0;
	for(int i=0;i<boarddup.length;i++)
		for(int j=0;j<boarddup.length;j++){
			//if(boarddup[i][j]==pcolor) count++;
			if(boarddup[i][j]==pcolor) count+=1+scoreMap[i][j];
		}
	return count;
}
private int scoreMap[][]= new int[][]{
		{876, 716, 404, 880, 374, 909, 725, 911},
		{513, 434, 242, 96, 542, 222, 963, 293},
		{765, 597, 479, 109, 490, 861, 956, 112},
		{97, 295, 256, 837, 936, 308, 746, 858},
		{45, 40, 116, 350, 246, 308, 188, 886},
		{746, 299, 159, 395, 568, 421, 157, 941},
		{84, 907, 476, 67, 371, 357, 77, 680},
		{876, 716, 404, 880, 374, 909, 725, 911}
		//{551, 413, 623, 627, 996, 538, 817, 440}
		/*{373, 474, 392, 75, 249, 11, 198, 67},
		{301, 432, 65, 116, 117, 42, 87, 250},
		{314, 32, 392, 41, 186, 89, 17, 11},
		{163, 28, 231, 363, 366, 129, 249, 73},
		{414, 224, 59, 413, 369, 74, 76, 6},
		{448, 13, 134, 89, 7, 255, 9, 262},
		{177, 133, 293, 236, 189, 78, 221, 81},
		{348, 54, 483, 459, 428, 454, 362, 181}
		/*{391, 474, 56, 186, 448, 30, 220, 397},
		{297, 209, 65, 116, 117, 42, 136, 250},
		{332, 32, 19, 41, 50, 89, 17, 11},
		{259, 28, 130, 363, 366, 129, 22, 215},
		{446, 70, 161, 110, 369, 74, 391, 117},
		{448, 13, 499, 258, 7, 282, 399, 262},
		{196, 133, 426, 188, 206, 78, 221, 338},
		{247, 54, 483, 411, 99, 436, 92, 366}*/
		
};
/********************************************************************************/	
//	class MoveListener extends MouseMotionAdapter{
//		@Override
		public void mouseMoved(MouseEvent e) {
			if(e.getX() < 51 || e.getX() > 289 || 
					e.getY() < 61 || e.getY() > 298	)
					return;
			
			x_h = getArrX(e.getX());
			y_h = getArrY(e.getY());
			
//			System.out.println("x_h="+x_h+", X="+getDrawX(x_h));
//			System.out.println("y_h="+y_h+", Y="+getDrawX(y_h));
		}
//	}
	
	/***********************************************************************/
	/***********************************************************************/
	//GA
	/*class BoardMap{
		public int map[][]=new int[8][8];
		public int wins;
	}
	public void GA()
	{
		//gen boards 10
		int mapcount=10;
		int playcount=mapcount/2;
		BoardMap boardMap[] = new BoardMap[mapcount];
		for(int i=0;i<mapcount;i++){
			boardMap[i]=new BoardMap();
			boardMap[i].wins=0;
			genScoreMap(boardMap[i].map);
		}
		boardMap[0].map=scoreMap;
		//number of runs for the GA
		for(int rounds=0;rounds<200;rounds++){
			System.out.println("ROUND >>>>>> "+rounds);
			//loop them and do stuff to play each round
			for(int i=0;i<mapcount-1;i++){
				for(int k=i+1;k<mapcount;k++){
					resetBoard();
					//run game
					while(isPossibleToFlip(BLACK)||isPossibleToFlip(WHITE)){
						scoreMap = boardMap[i].map;//System.out.println("B");
						playsim(DEPTH, BLACK, board);
						//sleep(500);
						scoreMap = boardMap[k].map;//System.out.println("W");
						playsim(DEPTH, WHITE, board);
						calculate();
						//System.out.println(">> b="+g_b+" , w="+g_w);
						//sleep(500);
					}
					//game over
					if(g_b>g_w){
						boardMap[i].wins++;
					}
					else {
						boardMap[k].wins++;
					}
					System.out.println("b="+g_b+" , w="+g_w);
					System.out.println("win "+i+" = "+boardMap[i].wins+" , win"+(k)+" = "+boardMap[k].wins);
				}
			}
			//move winmaps to the top
			//move the winner to top 5 locations sort
			for(int i=0;i<mapcount;i++){
				boolean swap = false;
				for(int j=0;j<mapcount-1;j++){
					if(boardMap[j].wins<boardMap[j+1].wins){
						BoardMap tmp = boardMap[j];
						boardMap[j]=boardMap[j+1];
						boardMap[j+1]=tmp;
						swap=true;
					}
				}
				if(!swap) break;
			}
			//gen new boards
			for(int i=0;i<playcount;i++){
				int x = i;
				int y = (i+1)%playcount;
				int dx = random.nextInt(8);
				int dy = random.nextInt(8);
				mutcopy(boardMap, x, y, dx, dy);
			}
			
			//print best board after each round
			printMap(boardMap[0]);
			
			//make wins 0
			for(int i=0;i<mapcount;i++){
				boardMap[i].wins=0;
			}
		}
		System.out.println("GA done...");
		//print the best
		printMap(boardMap[0]);
		printMap(boardMap[1]);
		printMap(boardMap[2]);
	}
	private Random random = new Random();
	public void genScoreMap(int map[][])
	{
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++){
				map[i][j]=random.nextInt(1000);
			}
	}
	public void mutcopy(BoardMap[] bm,int x,int y,int dx,int dy)
	{
		int destmap = x + bm.length/2;
		for(int i=0;i<dx;i++)
			for(int j=0;j<dy;j++){
				bm[destmap].map[i][j]=mutate(bm[x].map[i][j]);
			}
		for(int i=dx;i<bm[destmap].map.length;i++)
			for(int j=dy;j<bm[destmap].map.length;j++){
				bm[destmap].map[i][j]=mutate(bm[y].map[i][j]);
			}
		bm[destmap].wins=0;
		System.out.println("destmap="+destmap);
	}
	public int mutate(int value){
		if(random.nextInt(100)<10){
			value = random.nextInt(1000);
		}
		return value;
	}
	public void printMap(BoardMap bm)
	{
		System.out.println("Total wins="+bm.wins);
		for(int i=0;i<bm.map.length;i++){
			System.out.println();
			for(int j=0;j<bm.map.length;j++){
				System.out.print(bm.map[i][j]+", ");
			}
		}
	}*/
}
