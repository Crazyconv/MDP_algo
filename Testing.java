import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class Testing {
    public static void main ( String[] args ){
    
        WallSticking5 ws5;
        WallSticking4 ws4;
       
       
        RealRun rr;
        
        
        
        int[] strP;
        int[] endP;
         
        int[][] tCM;  //transformed current map
        ArrayList<String> s=new ArrayList<String>(); //Map Descriptor Strings
        
        
        MapDescriptor md=new  MapDescriptor();
        
        double coverage_limit;
        long time_limit;
        boolean mustFindGoal;
        int speed;
        
       
         int[][] occupancy=new int[20][15];
         int[][] occupancy1=new int[20][15];
         int[][] occupancy2=new int[20][15];
         int[][] occupancy3=new int[20][15];
         int[][] occupancy4=new int[20][15];
         int[][] occupancyRR=new int[20][15];
         
         int[][]occupancy7=new int[20][15]; //for real run
          int[][]occupancy8=new int[20][15];
          
          int[][]sample1=new int[20][15];
          int[][]sample2=new int[20][15];
          int[][]sample3=new int[20][15];
          int[][]sample4=new int[20][15];
          int[][]sample5=new int[20][15];
          
         
         
          
         /*
         String hostName = "192.168.22.1";
		int portNumber = 5000;
		Socket socket = null;
		try{
			socket = new Socket(hostName, portNumber);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}
*/
         
         //Testing map
         //Long block
         occupancy=new int[][]{
           //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //0
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //1
            {2,2,2,2,2,2,2,3,2,2,2,2,2,2,2},   //2
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //3
            {2,2,2,2,2,2,2,2,2,2,2,3,3,3,2},   //4
            {2,3,2,2,2,2,2,2,2,2,2,2,2,2,2},   //5
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //6
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //7
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //8
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //9
            {2,2,2,2,2,2,2,2,2,2,3,2,2,2,2},   //10
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //11
            {2,2,2,2,3,3,3,2,2,2,2,2,2,2,2},   //12
            {3,2,2,2,2,2,2,3,3,3,3,3,3,3,3},   //13
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //14
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,3},   //15
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //16
            {2,2,2,2,2,2,2,3,2,2,2,2,2,2,2},   //17
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //18
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //19
           
        };
         
          occupancy1=new int[][]{
                    {2,2,2,2,2,3,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,3,2},   //1
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //2
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},   //3
                    {2,2,2,3,3,3,2,2,2,2,2,2,2,2,2},   //4
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //5
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //6
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //7
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //8
                    {2,2,2,3,2,2,2,2,2,2,2,3,2,2,2},   //9
                    {2,2,2,3,2,2,2,2,2,2,2,3,2,2,2},   //10
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //11
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //12
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //13
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //14
                    {2,2,2,2,2,2,2,2,2,3,3,3,2,2,2},   //15
                    {2,2,2,2,2,3,2,2,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //17
                    {2,3,2,2,2,2,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},     //19
         
         
          };
          
          //L
          occupancy2=new int[][]{
                    {2,2,2,2,2,3,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,3,2},   //1
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //2
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},   //3
                    {2,2,2,3,3,3,2,2,2,2,2,2,2,2,2},   //4
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //5
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //6
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //7
                    {2,2,2,2,3,3,3,3,2,2,2,3,2,2,2},   //8
                    {2,2,2,3,3,2,2,2,2,2,2,3,2,2,2},   //9
                    {2,2,2,3,2,2,2,2,2,2,2,3,2,2,2},   //10
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //11
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //12
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //13
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //14
                    {2,2,2,2,2,2,2,2,2,3,3,3,2,2,2},   //15
                    {2,2,2,2,2,3,2,2,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //17
                    {2,3,2,2,2,2,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},     //19
         
         
          };
          
          //empty
           occupancy4=new int[][]{
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //1
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //2
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //3
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //4
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //5
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //6
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //7
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //8
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //9
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //10
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //11
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //12
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //13
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //14
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //15
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //17
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},     //19
         
         
          };
           
           
            occupancy3=new int[][]{
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //1
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //2
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //3
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //4
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,3,3},   //5
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //6
                    {2,2,2,2,2,2,2,3,2,2,2,2,2,2,2},   //7
                    {3,3,2,2,2,2,2,2,2,2,2,2,2,2,2},   //8
                    {3,3,2,2,2,2,2,2,2,2,2,2,2,2,2},   //9
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //10
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //11
                    {2,2,2,2,2,2,2,3,2,2,2,2,2,2,2},   //12
                    {2,2,2,2,2,2,2,3,2,2,2,2,2,2,2},   //13
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //14
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //15
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //17
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},     //19
         
         
          };
            
          //For real run   
          occupancyRR=new int[][]{  
              
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //1
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //2
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //3
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //4
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //5
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //6
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //7
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //8
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //9
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //10
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //11
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //12
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //13
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //14
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //15
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //17
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //19
         
          };  
          
          
          occupancy8=new int[][]{  
                   //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //1
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //2
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //3
                    {2,2,3,2,2,2,2,2,2,2,2,2,2,2,2},   //4
                    {2,2,3,2,2,2,2,2,2,2,2,3,2,2,2},   //5
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //6
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //7
                    {2,2,3,2,2,2,2,2,2,2,2,2,2,2,2},   //8
                    {2,2,3,2,2,2,2,2,2,2,2,2,2,2,2},   //9
                    {2,2,3,2,2,2,2,2,2,2,2,2,2,2,2},   //10
                    {2,2,3,2,2,2,2,2,2,2,2,2,2,2,2},   //11
                    {2,2,3,2,2,2,2,2,2,2,2,2,2,2,2},   //12
                    {2,3,2,2,2,2,2,2,2,2,3,2,2,2,2},   //13
                    {2,3,2,2,2,2,2,2,2,2,3,2,2,2,2},   //14
                    {2,3,3,3,3,2,2,2,2,2,2,2,2,2,2},   //15
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //17
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //19
         
         
          };  
          
          sample1=new int[][]{  
                   //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //1
                    {2,2,2,2,2,2,2,3,2,2,2,2,2,2,2},   //2
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //3
                    {2,2,2,2,2,2,2,2,2,2,2,3,3,3,2},   //4
                    {2,3,2,2,2,2,2,2,2,2,2,2,2,2,2},   //5
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //6
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //7
                    {2,2,2,2,2,3,3,3,2,2,2,2,2,2,2},   //8
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //9
                    {2,2,2,2,2,2,2,2,2,2,3,2,2,2,2},   //10
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //11
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //12
                    {3,3,3,3,3,3,2,2,2,2,2,2,2,2,2},   //13
                    {2,2,2,2,2,2,2,2,2,2,2,3,3,3,2},   //14
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //15
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,2,2,3,2,2,2,2,2,2,2},   //17
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //19
         
         
          }; 
          
         sample2=new int[][]{  
                   //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //1
                    {2,2,2,2,2,2,2,2,2,2,3,2,2,2,2},   //2
                    {2,2,2,2,2,2,2,2,2,2,3,2,2,2,2},   //3
                    {2,2,2,2,2,3,2,2,2,2,3,2,2,2,2},   //4
                    {2,2,2,3,3,3,2,2,2,2,3,2,2,2,2},   //5
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //6
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //7
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //8
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //9
                    {2,3,2,2,2,2,3,3,2,2,2,2,2,2,2},   //10
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //11
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //12
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //13
                    {2,2,2,2,3,2,2,2,2,3,3,3,3,3,3},   //14
                    {2,2,2,2,3,2,2,2,2,2,2,2,2,2,2},   //15
                    {2,2,2,2,3,2,2,2,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //17
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,2,2,2,3,2,2,2,2,2,2},   //19
         
         
          }; 
          
          sample3=new int[][]{  
                   //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //1
                    {2,2,2,2,2,2,2,2,2,2,2,2,3,2,2},   //2
                    {2,2,2,2,2,2,2,2,3,2,2,2,2,2,2},   //3
                    {2,2,2,2,2,2,2,2,3,2,2,2,2,2,2},   //4
                    {2,2,2,2,2,2,2,2,3,2,2,2,2,2,2},   //5
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //6
                    {3,3,3,2,2,2,2,2,2,2,2,2,2,2,2},   //7
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //8
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //9
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //10
                    {2,2,2,2,3,3,3,3,3,3,2,2,2,2,2},   //11
                    {2,2,2,2,3,3,3,3,3,3,2,2,2,2,2},   //12
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //13
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,3,2},   //14
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //15
                    {2,2,2,2,2,2,2,3,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //17
                    {2,2,2,2,3,2,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //19
         
         
          };

          sample4=new int[][]{  
                   //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},   //1
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},   //2
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //3
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //4
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //5
                    {3,3,3,3,3,3,2,2,2,2,2,3,2,2,2},   //6
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //7
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //8
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //9
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},   //10
                    {2,2,2,2,2,2,2,2,3,2,2,2,2,2,2},   //11
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //12
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //13
                    {2,2,2,2,2,2,2,2,2,2,2,2,3,3,3},   //14
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //15
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,3,2,2,2,2,2,2,2,2,2},   //17
                    {2,2,2,2,2,3,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,3,2,2,2,2,2,2,2,2,2},   //19
         
         
          };

          sample5=new int[][]{  
                   //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4
                    {2,2,2,2,2,3,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,3,2},   //1
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //2
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},   //3
                    {2,2,2,3,3,3,2,2,2,2,2,2,2,2,2},   //4
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //5
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //6
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //7
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //8
                    {2,2,2,3,2,2,2,2,2,2,2,3,2,2,2},   //9
                    {2,2,2,3,2,2,2,2,2,2,2,3,2,2,2},   //10
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //11
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //12
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //13
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //14
                    {2,2,2,2,2,2,2,2,2,3,3,3,2,2,2},   //15
                    {2,2,2,2,2,3,2,2,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //17
                    {2,3,2,2,2,2,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},   //19
         
         
          };
                  
         
          
          
                  
         
        //instructions
        System.out.println("Chose function: ");
        System.out.println("1: Complete exploration on simulator");  
        System.out.println("2: Fatest Path Computation ");  
        System.out.println("3: Generate Map Descriptor "); 
        System.out.println("4: Coverage limited exploration ");  
        System.out.println("5: Time limited exploration "); 
        System.out.println("6: Real run ");
        System.out.println("7: Simulate Real run ");
        System.out.println("9: Simulate Real run dfs to start: ");
        System.out.println("10: Real run dfs to start ");
        System.out.println("14: New Algo:SWNE ");
        System.out.println("15: New Algo:ESWN ");


        
        
        
        Scanner in=new Scanner(System.in);
        int a=in.nextInt();
        
        switch(a){
        
            case(1):  //Complete exploration on simulator
               
              strP=new int[]{10,7};     //9,7 for sample3,  1 grid impossible to explore for sample3
  
               coverage_limit=1;
               time_limit=1000000000;
               mustFindGoal=true;
               speed=10;
               
               //no path problem for occupancy 1
               ws4=new WallSticking4(strP,coverage_limit,sample5,time_limit,speed);
                
                ws4.start_Exploration(mustFindGoal);
                System.out.println("Exploration complete");
                
                
               // ws1.m.printAlign(ws.align);
                ws4.printMap(ws4.curMap);
             
                
                //map descriptor
                tCM=md.transCurMap(ws4.curMap);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
                int[][] testMap=md.transMap(s.get(0), s.get(1));
                md.printMap(testMap);
                 
                
                
                break;
                
            case(2): // Fatest Path Computation
                
                
                 strP=new int[]{1,1};
                 endP=new int[]{18,13};
                
                 int[][] expMap =new int[][]{   //explored map
                    
                    
                   //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4
                    {2,2,2,2,2,3,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,3,2},   //1
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //2
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},   //3
                    {2,2,2,3,3,3,2,2,2,2,2,2,2,2,2},   //4
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //5
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //6
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //7
                    {2,2,2,2,2,2,2,2,2,2,2,3,2,2,2},   //8
                    {2,2,2,3,2,2,2,2,2,2,2,3,2,2,2},   //9
                    {2,2,2,3,2,2,2,2,2,2,2,3,2,2,2},   //10
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //11
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //12
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //13
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //14
                    {2,2,2,2,2,2,2,2,2,3,3,3,2,2,2},   //15
                    {2,2,2,2,2,3,2,2,2,2,2,2,2,2,2},   //16
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //17
                    {2,3,2,2,2,2,2,2,2,2,2,2,2,2,2},   //18
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},     //19
                    
                 }; 
                
                ShortestPath sh;
                sh = new ShortestPath(strP, endP,occupancy1);
                ArrayList shp =sh.searchShortestPath();
                Map maps=new Map(occupancy1,strP);
                maps.paintSPath2(shp);
                
               break;
                 
                
            
            case(3): //Generate Map Descriptor
                //change anyMap
                int[][] anyMap =new int[][]{   //explored map
                    
                    
                   //0 1 2 3 4 5 6 7 8 9 0 1 2 3 4
                    {2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},   //0
                    {2,2,2,2,2,2,2,2,2,3,2,2,2,2,2},   //1
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //2
                    {2,2,2,2,2,2,3,2,2,2,2,2,2,2,3},   //3
                    {2,2,2,2,2,2,3,3,2,2,2,2,2,2,3},   //4
                    {2,2,2,2,2,2,3,3,2,2,2,2,2,2,3},   //5
                    {2,2,2,2,2,2,2,3,2,2,2,2,2,3,2},   //6
                    {2,2,2,2,2,2,2,2,2,2,2,3,3,3,2},   //7
                    {3,3,3,2,2,2,2,2,2,2,2,3,3,3,2},   //8
                    {2,3,3,3,2,2,2,2,2,2,2,2,2,2,2},   //9
                    {2,2,2,2,2,2,3,2,2,2,2,2,2,2,2},   //10
                    {2,2,2,2,2,2,2,3,3,3,3,2,2,2,2},   //11
                    {2,2,2,2,2,2,2,3,3,3,3,2,2,2,2},   //12
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},   //13
                    {2,2,2,2,2,2,2,2,2,2,2,2,2,2,3},   //14
                    {2,3,3,3,2,2,2,2,2,2,2,2,2,2,3},   //15
                    {2,0,0,3,2,2,2,2,2,2,2,2,2,2,2},   //16
                    {0,0,0,0,3,2,2,3,2,2,2,2,2,2,2},   //17
                    {0,0,0,0,0,0,2,3,2,2,2,2,2,2,2},   //18
                    {0,0,0,0,0,0,0,3,2,2,2,2,2,2,2},     //19
                    
                 }; 
                tCM=md.transCurMap(anyMap);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
                
                
                break;
                   
            case(4):  //Coverage limited exploration
               
                strP=new int[]{10,7};
                time_limit=1000000000;
                mustFindGoal=true;
                
                System.out.println("Enter coverage limit:");
                double cl=in.nextDouble();
                coverage_limit=cl;
                speed=5;
                
                
                ws4=new WallSticking4(strP,coverage_limit,occupancy1,time_limit,speed);
                ws4.start_Exploration(mustFindGoal);
                //System.out.println("case 4");
                //ws4.printMap(ws4.curMap);
                //System.out.println();
                // System.out.println();
               
                //map descriptor
                tCM=md.transCurMap(ws4.curMap);
                //e.m.printMap(tCM);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
                break;
         
            
            case 5:
            	//Time Limited Exploration
                strP=new int[]{10,7};
                coverage_limit=1;
                mustFindGoal=false;
                
                System.out.println("Enter time limit (seconds):");
                int tl=in.nextInt();
                time_limit=1000*tl;
                
                System.out.println("Enter robot speed (steps per second):");
                speed=in.nextInt();
             
                
                ws4=new WallSticking4(strP,coverage_limit,occupancy,time_limit,speed);
                ws4.start_Exploration(mustFindGoal);
                
                
                //map descriptor
                tCM=md.transCurMap(ws4.curMap);
                //e.m.printMap(tCM);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
                
                
               break;
                
                
            case 6:  //real run
                
                strP=new int[]{9,7};
                
                endP=new int[]{18,13};
                
                
               coverage_limit=1;
               time_limit=1000000000;
               mustFindGoal=true;
               
                rr=new RealRun(strP,coverage_limit,occupancy7,time_limit);
                rr.start_Exploration(mustFindGoal);
                System.out.println("Exploration complete");
                
                
               
                //map descriptor
                tCM=md.transCurMap(rr.curMap);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
               
                break;
                
                
                
             case 7:  //simulate real run
                
                strP=new int[]{10,7};
                
                endP=new int[]{18,13};
                
                
               coverage_limit=1;
               time_limit=1000000000;
               mustFindGoal=true;
               
                SimulateRealRun srr=new SimulateRealRun(strP,coverage_limit,sample1,time_limit);
                srr.start_Exploration(mustFindGoal);
                System.out.println("Exploration complete");
                
  
               
                //map descriptor
                tCM=md.transCurMap(srr.curMap);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
               
                break;
                
                
             case 8:
                 
                 Client client=new Client();
                client.setUp("192.168.22.1",5000);
                client.write("T");
                try{Thread.sleep(500);}catch(Exception e){}
                client.write("W1|W1|");
                 try{Thread.sleep(500);}catch(Exception e){}
                client.write("D|");
                 try{Thread.sleep(500);}catch(Exception e){}
                 client.write("S");
                  try{Thread.sleep(500);}catch(Exception e){}
                 client.write("W1|W1|");
                 
                 //client.write("W10|A|W6|D|W2|A|W11|");
                break;
                 
                 
            case(9):  //Complete exploration on simulator+dfs to start
               
              strP=new int[]{9,7};     //9,7 for sample3,  1 grid impossible to explore for sample3
  
               coverage_limit=1;
               time_limit=1000000000;
               mustFindGoal=true;
               speed=4;
               
               
               ws5=new WallSticking5(strP,coverage_limit,sample4,time_limit,speed);
                
                ws5.start_Exploration(mustFindGoal);
                System.out.println("Exploration complete");
                
                
               // ws1.m.printAlign(ws.align);
                ws5.printMap(ws5.curMap);
             
                
                //map descriptor
                tCM=md.transCurMap(ws5.curMap);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
                int[][] testMap2=md.transMap(s.get(0), s.get(1));
                md.printMap(testMap2);
                 
                
                
                break;
            
             case 10:  //real run DFS+WALL+DFS
                
                strP=new int[]{9,7};
                
                endP=new int[]{18,13};
                
                
               coverage_limit=1;
               time_limit=1000000000;
               mustFindGoal=true;
               
               RealRun2 rr2=new RealRun2(strP,coverage_limit,occupancy7,time_limit);
                rr2.start_Exploration(mustFindGoal);
                System.out.println("Exploration complete");
                
                
               
                //map descriptor
                tCM=md.transCurMap(rr2.curMap);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
               
                break;
                 
                 
            case 11:  //real run DFS+WALL+DFS+full exploration
                
                strP=new int[]{9,7};
                
                endP=new int[]{18,13};
                
                
               coverage_limit=1;
               time_limit=1000000000;
               mustFindGoal=true;
               
               RealRun3 rr3=new RealRun3(strP,coverage_limit,occupancy7,time_limit);
                rr3.start_Exploration(mustFindGoal);
                System.out.println("Exploration complete");
                
                
               
                //map descriptor
                tCM=md.transCurMap(rr3.curMap);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
               
                break; 
                
             case(12):  //dfs to south wall
               
              strP=new int[]{9,7};     //9,7 for sample3,  1 grid impossible to explore for sample3
  
               coverage_limit=1;
               time_limit=1000000000;
               mustFindGoal=true;
               speed=20;
               
               
               WallSticking6 ws6=new WallSticking6(strP,coverage_limit,sample3,time_limit,speed);
                
                ws6.start_Exploration(mustFindGoal);
                System.out.println("Exploration complete");
                
                
               // ws1.m.printAlign(ws.align);
                ws6.printMap(ws6.curMap);
             
                
                //map descriptor
                tCM=md.transCurMap(ws6.curMap);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
                int[][] testMap3=md.transMap(s.get(0), s.get(1));
                md.printMap(testMap3);
                 
                
                
                break;    
                 
            
                
             case 14:  //SWNE
                
                strP=new int[]{9,7};
                
                endP=new int[]{18,13};
                
                
               coverage_limit=1;
               time_limit=1000000000;
               mustFindGoal=true;
               
               RealRun4 rr4=new RealRun4(strP,coverage_limit,occupancy7,time_limit);
                rr4.start_Exploration(mustFindGoal);
                System.out.println("Exploration complete");
                
                
               
                //map descriptor
                tCM=md.transCurMap(rr4.curMap);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
               
                break;     
                 
                 
                 
                   
             case 15:  //ESWN
                
                strP=new int[]{9,7};
                
                endP=new int[]{18,13};
                
                
               coverage_limit=1;
               time_limit=1000000000;
               mustFindGoal=true;
               
               RealRun5 rr5=new RealRun5(strP,coverage_limit,occupancy7,time_limit);
                rr5.start_Exploration(mustFindGoal);
                System.out.println("Exploration complete");
                
                
               
                //map descriptor
                tCM=md.transCurMap(rr5.curMap);
                s=md.twoDtoStrings(tCM);
                System.out.println(s.get(0));
                System.out.println(s.get(1));
               
                break;       
                
            default:
                System.out.println("Invalid input");
        
        
        
        
        }
    }
}
