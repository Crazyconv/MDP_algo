import java.io.*;
import java.util.*;

public class ShortestPath {

	private int [][] maze;
	private int iX;
	private ExtendObstacleMap exMap;
	private int iY;
        public String finalPath="";
        public String finalSteps="";
	private int fX;
	private int fY;
	int pos[]=new int[2];
        private int countSteps=0;
	
	private ArrayList shortestPath=new ArrayList();
	
	public ShortestPath(int startP[], int endP[],int [][] map)
	{
		iY = startP[0];
		iX = startP [1];
		fY = endP[0];
		fX = endP[1];

        maze = new int[map.length][map[0].length];
        for(int i= 0; i < maze.length; i++){
	for(int j=0; j< maze[0].length; j++)
	{      if(map[i][j]==0) maze[i][j]=3;
                         else maze[i][j]=map[i][j];
	}
}
        
        
        
exMap = new ExtendObstacleMap();
exMap.transMap(maze);
		
		maze[fY][fX]=5;
		maze[iY][iX]=4;
		/*for(int i=0; i<maze.length; i++){
			for(int j=0; j < maze[0].length; j++)
				System.out.print(maze[i][j]+" ");
			System.out.println();
		}*/
	}
	public ArrayList searchShortestPath()
	{
		int x = maze[0].length;
		int y = maze.length;
		int error = 0;
		int xtemp = 0;
		int ytemp = 0;

	    int xP = iX;        // xP = 'x position'. iX = 'initial x'
	    int yP = iY;

	    int flood[][]= new int[y][x];

	    // Fill 'Flood' Map with -1s.
	    for(ytemp = 0;ytemp < y; ytemp++){
	        for(xtemp = 0;xtemp < x; xtemp++){
	            flood[ytemp][xtemp] = -1;
	        }
	    }
	        
	        flood[iY][iX] = 0;
	        int found = 0;
	        int moves = 0;
	        int itt = 1;        // 'Itteration' increments as the bot 'floods' outwards
	        int moremoves = 1;
	      //This is the main loop that 'floods' the maze
	       while(found != 1 && moves < 150 && error == 0 && moremoves == 1){
	            moremoves = 0;      // Assume no more moves. If more moves, set to 1

	            // Loop through every grid space individually
	            for(ytemp = 0;ytemp < y; ytemp++){
	                for(xtemp = 0;xtemp < x; xtemp++){
	                    xP = xtemp;
	                    yP = ytemp;

	                    // If the boundary of the "flooding" is found...
	                    if(flood[ytemp][xtemp] == (itt - 1)){
	                        
	                        // Move the 'flooding' up if the move is valid and not
	                        // hitting a wall.
	                        if(
	                        	yP >= 2  &&
	                            (exMap.isMovable(yP-1, xP)|| 
	                            maze[yP-1][xP] == 4 ||
	                            maze[yP-1][xP] == 5)
	                            && flood[yP-1][xP] == -1
	                            ){
	                            flood[yP-1][xP] = itt;  // Make this the new boundary
	                            moremoves = 1;

	                            // If target is found, let the program can stop looping.
	                            // Since the location of the is already known from
	                            // initial scan, the flooding job is done.
	                            if(maze[yP-1][xP] == 5){
	                                flood[yP-1][xP] = itt;
	                                found = 1;
	                            }
	                        }

	                        //Same as above, but checking flooding Down
	                        if(
	                        	yP<y-2 &&
	                            (exMap.isMovable(yP+1, xP) || 
	                            maze[yP+1][xP] == 4 ||
	                            maze[yP+1][xP] == 5)
	                            && flood[yP+1][xP] == -1
	                        ){
	                            flood[yP+1][xP] = itt;
	                            moremoves = 1;
	                            if(maze[yP+1][xP] == 5){
	                                flood[yP+1][xP] = itt;
	                                found = 1;
	                            }
	                        }

	                        //Same as above, but checking flooding Left
	                        if(
	                            xP >= 2 && (exMap.isMovable(yP, xP-1) || 
	                            maze[yP][xP-1] == 4 ||
	                            maze[yP][xP-1] == 5)
	                            && flood[yP][xP - 1] == -1
	                        ){
	                            flood[yP][xP - 1] = itt;
	                            moremoves = 1;
	                            if(maze[yP][xP-1] == 5){
	                                flood[yP][xP-1] = itt;
	                                found = 1;
	                            }
	                        }

	                        //Same as above, but checking flooding Right
	                        if(
	                            xP<x-2 && (exMap.isMovable(yP, xP+1) || 
	                            maze[yP][xP+1] == 4 ||
	                            maze[yP][xP+1] == 5)
	                            && flood[yP][xP + 1] == -1
	                        ){
	                            flood[yP][xP + 1] = itt;
	                            moremoves = 1;
	                            if(maze[yP][xP+1] == 5){
	                                flood[yP][xP+1] = itt;
	                                found = 1;
	                            }
	                        }
	                    }


	                }// End Inner For Loop
	            }//End Outer For Loop

	            itt++;
	            moves++;


	        }//End Main While Loop

	        /*
	         * THE MAP IS FLOODED! IF NOT FOUND END HERE.
	         * IF FOUND, WE CAN NOW FIND THE DIRECTION
	         */




	        if(found == 0 && error == 0){
	            System.out.println("no path\n");
	        }else if(error==0){
	        	path(flood);
	        	//pathToString(shortestPath);

	        }//End If

	        
	        return shortestPath;


	    }
	
	
	public void path(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            shortestPath.add(new int[]{fY,fX});

	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		shortestPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	shortestPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	shortestPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	shortestPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }

	            }//End While
	            pathToString(directions, 0, i);
	            Collections.reverse(shortestPath);
	}


	public void pathToString (int directions[], int error, int i)
	{
	       
	        // Print output in reverse order
		finalPath = "W0";
	       if(error == 0){

	            for(;i>0;i--){
	            	
	            char ch = finalPath.charAt(finalPath.length()-1);
	       

	
	            if(directions[i]==1 && directions[i-1] ==2 || directions[i]==3 && directions[i-1]==4 || directions[i]==2 && directions[i-1] == 3 || directions[i]==4 && directions[i-1]==1)
	            {
	            		finalPath = finalPath + "|A|W1";
	            		countSteps+=2;
	            }
	            else if(directions[i]==1 && directions[i-1] ==4 || directions[i]==3 && directions[i-1]==2 || directions[i]==2 && directions[i-1] == 1 || directions[i]==4 && directions[i-1]==3)
	            		{
	            		 finalPath = finalPath + "|D|W1";
	            		 countSteps+=2;
	            		}
	            		
	            else if(Character.isDigit(ch))
	            {
	            	if(ch=='9')
	            		finalPath = finalPath.substring(0, finalPath.length()-1)+"10";
	            	else
	            		finalPath = finalPath.substring(0, finalPath.length()-1)+(char)(ch+1);
	            	countSteps++;
	            }
	            }
	            

	              /* if(directions[i]==1){
                            
	                    System.out.println("North\n");
	                }else if(directions[i]==2){
	                    System.out.println("West\n");
	                }else if(directions[i]==3){
	                    System.out.println("South\n");
                            
	                }else if(directions[i]==4){
	                    System.out.println("East\n");
	                }*/
	            
	        }
	       finalPath = finalPath + "|";
	       finalSteps = "S"+countSteps;
	       System.out.println(finalPath);
	       System.out.println(finalSteps);
	}
}
