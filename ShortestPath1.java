import java.io.*;
import java.util.*;

public class ShortestPath1 {

	private int [][] maze;
	private int iX;
	private ExtendObstacleMap exMap;
	private int iY;
    public String finalPath="";
	private int fX;
	private int fY;
	private int countSteps;
	private int noTurns=0;
	int pos[]=new int[2];
	
	private ArrayList shortestPath=new ArrayList();
	private ArrayList tempPath;
	
	
	public ShortestPath1(int startP[], int endP[],int [][] map)
	{
		iY = startP[0];
		iX = startP [1];
		fY = endP[0];
		fX = endP[1];
		exMap = new ExtendObstacleMap();
		exMap.transMap(map);
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
	            System.exit(0);
	        }else{
	        	int leastTurn = 100;
	        	pathOne(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathTwo(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
	        	pathThree(flood);
	        	Collections.reverse(tempPath);
	        	finalPath = pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathFour(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
	        	pathFive(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathSix(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
	        	pathSeven(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathEight(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
	        	pathNine(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathTen(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
	        	pathEleven(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathTwelve(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
	        	pathThirteen(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathFourteen(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
	        	pathFifteen(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathSixteen(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
	        	pathSeventeen(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathEighteen(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
	        	pathNineteen(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathTwenty(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
	        	pathTwentyOne(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathTwentyTwo(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
	        	pathTwentyThree(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
	        ////////////////////////////////////////////
	        	pathTwentyFour(flood);
	        	Collections.reverse(tempPath);
	        	pathToString(tempPath);
	        	if(noTurns<leastTurn)
	        	{
	        		leastTurn=noTurns;
	        		shortestPath = tempPath;
	        	}
	        	System.out.println(tempPath);
		        System.out.println(noTurns);
		        ////////////////////////////////////////
		        
	        }//End If

	        return shortestPath;
	    }
	
	

	public String pathToString(ArrayList path)
	{	
		
		
		 noTurns = 0;
		 finalPath = "W0";
		 for(int i=1; i<path.size()-1;i++)
	        {
	    	    int pBef[] = (int[])path.get(i-1);
	        	int p[] = (int[])path.get(i);
	        	int pAft[] = (int[])path.get(i+1);
	        	if(pBef[0]==p[0] && p[0]!=pAft[0]){
	        		char ch = finalPath.charAt(finalPath.length()-1);
	        		if(Character.isDigit(ch))
	        		{
	        			if(ch=='9')
	        				finalPath = finalPath.substring(0,finalPath.length()-1)+"10";
	        			else
	        				finalPath = finalPath.substring(0, finalPath.length()-1)+(char)(ch+1);
	        		}
	        		else
	        			finalPath = finalPath + "|W1";
	        		if(p[0]<pAft[0])
	        		{	

	        			finalPath = finalPath + "|A";
	        			noTurns++;
	        		}
	        		else
	        		{
	        			finalPath = finalPath + "|D";
	        			noTurns++;
	        		}
	        		
	        	}
	        	else if((pBef[1]==p[1] && p[1]!=pAft[1]))
	        	{
	        		if(p[1]<pAft[1])
	        		{
	        			char ch = finalPath.charAt(finalPath.length()-1);
	        			if(Character.isDigit(ch))
	        			{
	        				if(ch=='9')
	        					finalPath = finalPath.substring(0,finalPath.length()-1)+"10";
	        				else
	        					finalPath = finalPath.substring(0, finalPath.length()-1)+(char)(ch+1);
	        			}
	        				else
	        				finalPath = finalPath + "|W1";
	        				

	        			finalPath = finalPath + "|D";
	        			noTurns++;
	        		}
	        		else
	        		{char ch = finalPath.charAt(finalPath.length()-1);
	        			if(Character.isDigit(ch))
	        			{
	        				if(ch=='9')
	        					finalPath = finalPath.substring(0, finalPath.length()-1)+"10";
	        				else
	        					finalPath = finalPath.substring(0, finalPath.length()-1)+(char)(ch+1);
	        			}
	        			else
	        				finalPath = finalPath + "|W1";
	        				

	        			finalPath = finalPath + "|A";
	        			noTurns++;
	        		}
	        		
	        			
	        	}
	        	else
	        	{
	        		char ch = finalPath.charAt(finalPath.length()-1);
	        		if(Character.isDigit(ch))
	        		{
	        				if(ch=='9')
	        					finalPath = finalPath.substring(0, finalPath.length()-1)+"10";
	        				else
	        					finalPath = finalPath.substring(0, finalPath.length()-1)+(char)(ch+1);
	        		}
	        		else
	        			finalPath = finalPath + "|W1";
	        				
	        	}
	        	
	        		
	        }
	       	        
	       char ch = finalPath.charAt(finalPath.length()-1);
	       
	       if(Character.isDigit(ch))
	       {
	    	   if(ch=='9')
	    		   finalPath = finalPath.substring(0, finalPath.length()-1)+"10";
	    	   else
	    		   finalPath = finalPath.substring(0, finalPath.length()-1)+(char)(ch+1);
	       }
	        

	        				


	       finalPath = finalPath + "|";
	       System.out.println(finalPath);
	       return finalPath;
	}
	
	public ArrayList pathOne(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		//System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	//System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	//System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	//System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }

	            }//End While
	            return tempPath;
	            
	}
		
	
	public ArrayList pathTwo(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 1;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		//System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        if(i>0 && directions[i-1]!=directions[i])
	                        i++;	
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	//System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;
	                        if(i>0 && directions[i-1]!=directions[i])
	                        	countSteps++;
	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
		                    ){
		                	tempPath.add(new int[]{yP,xP+1});
		                	//System.out.println(yP + "," +(xP+1));
		                        xP++;
		                        directions[i] = 4;  // 4 means west
		                        countSteps++;
		                        if(i>0 && directions[i-1]!=directions[i])
		                        	countSteps++;
		                        i++;
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }

	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	//System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;
	                        if(i>0 && directions[i-1]!=directions[i])
	                        	countSteps++;
	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }

	            }//End While
	            return tempPath;
	            
	}
	public ArrayList pathThree(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 1;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        if(i>0 && directions[i-1]!=directions[i])
	                        i++;	
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }

	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;
	                        if(i>0 && directions[i-1]!=directions[i])
	                        	countSteps++;
	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }

	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;
	                        if(i>0 && directions[i-1]!=directions[i])
	                        	countSteps++;
	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
		                    ){
		                	tempPath.add(new int[]{yP,xP+1});
		                	System.out.println(yP + "," +(xP+1));
		                        xP++;
		                        directions[i] = 4;  // 4 means west
		                        countSteps++;
		                        if(i>0 && directions[i-1]!=directions[i])
		                        	countSteps++;
		                        i++;
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }

	            }//End While
	            return tempPath;
	            
	}
	public ArrayList pathFour(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 1;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        if(i>0 && directions[i-1]!=directions[i])
	                        i++;	
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }

	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;
	                        if(i>0 && directions[i-1]!=directions[i])
	                        	countSteps++;
	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }


	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
		                    ){
		                	tempPath.add(new int[]{yP,xP+1});
		                	System.out.println(yP + "," +(xP+1));
		                        xP++;
		                        directions[i] = 4;  // 4 means west
		                        countSteps++;
		                        if(i>0 && directions[i-1]!=directions[i])
		                        	countSteps++;
		                        i++;
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
		                    ){
		                	tempPath.add(new int[]{yP+1,xP});
		                	System.out.println(yP+1 + "," +xP);
		                        yP++;
		                        directions[i] = 1;  // 1 means north
		                        countSteps++;
		                        if(i>0 && directions[i-1]!=directions[i])
		                        	countSteps++;
		                        i++;
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }

	            }//End While
	            return tempPath;
	            
	}
	public ArrayList pathFive(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 1;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	            	if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
		                    ){
		                		tempPath.add(new int[]{yP-1,xP});
		                		System.out.println(yP-1 + "," +xP);
		                        yP--;
		                        directions[i] = 3;  // 3 means south
		                        if(i>0 && directions[i-1]!=directions[i])
		                        i++;	
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }

	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
		                    ){
		                	tempPath.add(new int[]{yP,xP+1});
		                	System.out.println(yP + "," +(xP+1));
		                        xP++;
		                        directions[i] = 4;  // 4 means west
		                        countSteps++;
		                        if(i>0 && directions[i-1]!=directions[i])
		                        	countSteps++;
		                        i++;
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }

	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
		                    ){
		                	tempPath.add(new int[]{yP+1,xP});
		                	System.out.println(yP+1 + "," +xP);
		                        yP++;
		                        directions[i] = 1;  // 1 means north
		                        countSteps++;
		                        if(i>0 && directions[i-1]!=directions[i])
		                        	countSteps++;
		                        i++;
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }
	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;
	                        if(i>0 && directions[i-1]!=directions[i])
	                        	countSteps++;
	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }


	            }//End While
	            return tempPath;
	            
	}
	public ArrayList pathSix(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
		                    ){
		                	tempPath.add(new int[]{yP,xP+1});
		                	System.out.println(yP + "," +(xP+1));
		                        xP++;
		                        directions[i] = 4;  // 4 means west
		                        countSteps++;

		                        i++;
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }
	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
		                    ){
		                	tempPath.add(new int[]{yP,xP-1});
		                	System.out.println(yP + "," +(xP-1));
		                        xP--;
		                        directions[i] = 2;  // 2 means east
		                        countSteps++;

		                        i++;
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }



	            }//End While
	            return tempPath;
	            
	}
	public ArrayList pathSeven(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	               if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
		                    ){
		                	tempPath.add(new int[]{yP+1,xP});
		                	System.out.println(yP+1 + "," +xP);
		                        yP++;
		                        directions[i] = 1;  // 1 means north
		                        countSteps++;

		                        i++;
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }
	               else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }

	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }

	            }//End While
	            return tempPath;
	            
	}
	public ArrayList pathEight(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	               if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
		                    ){
		                	tempPath.add(new int[]{yP+1,xP});
		                	System.out.println(yP+1 + "," +xP);
		                        yP++;
		                        directions[i] = 1;  // 1 means north
		                        countSteps++;

		                        i++;
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }
	               else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
		                    ){
		                	tempPath.add(new int[]{yP,xP+1});
		                	System.out.println(yP + "," +(xP+1));
		                        xP++;
		                        directions[i] = 4;  // 4 means west
		                        countSteps++;

		                        i++;
		                        if(flood[yP][xP] == 0){
		                            complete = 1;
		                        }
		                }


	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }

	            }//End While
	            return tempPath;
	            
	}
			
	
	
	

	public ArrayList pathNine(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
					if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
					else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                
	                
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }

	            }//End While
	            return tempPath;
	            
	}
	
		public ArrayList pathTen(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
					if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
					else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
					else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                
	                
	                

	            }//End While
	            return tempPath;
	            
	}
	
	
	public ArrayList pathEleven(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
					if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                
					
					else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }

	            }//End While
	            return tempPath;
	            
	}
	
	
	public ArrayList pathTwelve(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
					if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
					else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
					else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                
	                
	                

	            }//End While
	            return tempPath;
	            
	}
	
	
	public ArrayList pathThirteen(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
					
					if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }

	            }//End While
	            return tempPath;
	            
	}
	
	public ArrayList pathFourteen(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
					
					if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
					else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                
	                

	            }//End While
	            return tempPath;
	            
	}
	
	public ArrayList pathFifteen(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
					
					if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
					else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
					else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                
	                
	                

	            }//End While
	            return tempPath;
	            
	}
	
	public ArrayList pathSixteen(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
					
					if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
					else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                
					else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                
	                

	            }//End While
	            return tempPath;
	            
	}
	
	public ArrayList pathSeventeen(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                

	            }//End While
	            return tempPath;
	            
	}

	public ArrayList pathEighteen(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                

	            }//End While
	            return tempPath;
	            
	}
	public ArrayList pathNineteen(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                
	                
	                

	            }//End While
	            return tempPath;
	            
	}
	public ArrayList pathTwenty(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                
	                
	                

	            }//End While
	            return tempPath;
	            
	}
	public ArrayList pathTwentyOne(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                
	                
	                

	            }//End While
	            return tempPath;
	            
	}
	public ArrayList pathTwentyTwo(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                
	                
	                

	            }//End While
	            return tempPath;
	            
	}
	public ArrayList pathTwentyThree(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                
	                
	                

	            }//End While
	            return tempPath;
	            
	}
		public ArrayList pathTwentyFour(int [][] flood)
	{	 	
				int complete = 0;
				int directions[] = new int [100];
				int i = 0;
				int xP = fX;
	            int yP = fY;
	            int y = flood.length;
	            int x = flood[0].length;
	            tempPath = new ArrayList();
	            tempPath.add(new int[]{fY,fX});
	            countSteps = 0;
	            while(complete == 0){

	                // Since the numbers increase spanning out from zero, we can check
	                // on all sides FROM the target, and count down to work back to the
	                // start. When the loop gets back to the end, it is all finished
	                if(flood[yP][xP+1] == flood[yP][xP] - 1 && (xP + 2) < x
	                    ){
	                	tempPath.add(new int[]{yP,xP+1});
	                	System.out.println(yP + "," +(xP+1));
	                        xP++;
	                        directions[i] = 4;  // 4 means west
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(flood[yP][xP-1] == flood[yP][xP] - 1 && (xP - 2) >= 0
	                    ){
	                	tempPath.add(new int[]{yP,xP-1});
	                	System.out.println(yP + "," +(xP-1));
	                        xP--;
	                        directions[i] = 2;  // 2 means east
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP<(y-2) && flood[yP+1][xP] == flood[yP][xP] - 1
	                    ){
	                	tempPath.add(new int[]{yP+1,xP});
	                	System.out.println(yP+1 + "," +xP);
	                        yP++;
	                        directions[i] = 1;  // 1 means north
	                        countSteps++;

	                        i++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                        }
	                }
	                else if(yP >= 2 && flood[yP-1][xP] == (flood[yP][xP] - 1)
	                    ){
	                		tempPath.add(new int[]{yP-1,xP});
	                		System.out.println(yP-1 + "," +xP);
	                        yP--;
	                        directions[i] = 3;  // 3 means south
	                        i++;	
	                        countSteps++;
	                        if(flood[yP][xP] == 0){
	                            complete = 1;
	                          
	                        }
	                }
	                
	                
	                

	            }//End While
	            return tempPath;
	            
	}

	}
	       
	        // Print output in reverse order
	      /* if(error == 0){

	            for(;i>=0;i--){
	                if(directions[i]==1){
                            
	                    System.out.println("North\n");
	                }else if(directions[i]==2){
	                    finalPath = finalPath + "West\n";
	                }else if(directions[i]==3){
	                    finalPath = finalPath + "South\n";
                            
	                }else if(directions[i]==4){
	                    finalPath = finalPath + "East\n";
	                }
	            }
	        }*/
