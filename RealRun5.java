import java.awt.Color;
import java.util.ArrayList;
import java.math.*;

public class RealRun5 {
    int[] sPos=new int[2];
    
   
    ArrayList path=new ArrayList();
    ArrayList path2=new ArrayList();
    
    ArrayList align;
   
    int length=20;
    int width=15;
    
    double coverage_limit;
    long time_limit;
    
    ArrayList sensor1=new ArrayList();
    ArrayList sensor2=new ArrayList();
    ArrayList sensor3=new ArrayList();
    ArrayList sensor4=new ArrayList();
    ArrayList sensor5=new ArrayList();
    ArrayList sensor6=new ArrayList();
    
    ArrayList direc=new ArrayList();
    
    
    ArrayList visited;

    
    int[][] curMap=new int[length][width];
    int[] curPos=new int[2];
    
    int count;
    int found_goal;
    int found_start;
    boolean out;
    boolean out2;
   
    int cycle_counter;
 
    
    int[][] occupancy=new int[length][width];
    
    int[] d; //current direction
    int[] stopP;
    
    
    Map m;
    
    ExtendObstacleMap eOM;
    ExtendObstacleMap eOM2;
    
    boolean wall;
    boolean backToStart;
    boolean senseFrontObstacle;
    boolean sensorAlignFront;
    
    long t1,t2;
    
    Client client;
    
    MapDescriptor md1=new MapDescriptor();
    int[][] tCM1; //=new int[20][15];
    ArrayList<String> ss=new ArrayList<String>(); //Map Descriptor Strings 
    
   // 0 for unexplored; 1 for visited; 2 for empty space but not visited ; 3 for obstacle; 5 for goal
    
     public  RealRun5(int[] strP,double coverage_limit,int[][] occupancy,long time_limit){
        
        this.client=new Client();
        client.setUp("192.168.22.1",6000);  //host, port
        
        
        this.coverage_limit=coverage_limit;
        this.time_limit=time_limit;
        
        sPos[0]=strP[0];
        sPos[1]=strP[1];
        this.curPos=sPos;
       
        
        d=new int[]{0,1};
        
       
        
       
       this.align=new ArrayList(); 
       this.path=new ArrayList(); 
       this.path2=new ArrayList(); 
        //this.visited=new ArrayList(); 
        
        
        this.occupancy=occupancy;
        
        
        direc.add(new int[]{0,1});   //E
        direc.add(new int[]{-1,0});  //S
        direc.add(new int[]{0,-1});  //W
        direc.add(new int[]{1,0});   //N
         
                                     
       
       
        
        this.count=18;  //count explored
      
        this.cycle_counter=0;
        this.found_goal=0;
        this.found_start=0;
        out=false;
        out2=false;
        wall=false;
        senseFrontObstacle=false;
         sensorAlignFront=false;
         
        this.m=new Map(occupancy,strP);
        this.eOM=new ExtendObstacleMap(); 
       
        
        //initialize current map
        for(int i=0;i<length;i++)
            for(int j=0;j<width;j++)
                curMap[i][j]=0;
        
        
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++)
                curMap[19-i][14-j]=2;   //5 for Goal
        }
        
         for(int i=0;i<3;i++){
            for(int j=0;j<3;j++)
                curMap[i][j]=2;   //5 for Goal
        }
             
     
        
       
    }
 
  public void start_Exploration(boolean mustFindGoal){
         
         t1 = System.currentTimeMillis();
         
         // MapDescriptor md1=new MapDescriptor();
         // int[][] tCM1=new int[20][15];
         // ArrayList<String> ss=new ArrayList<String>(); //Map Descriptor Strings
         
         
         dfs(curPos);
         
         
         System.out.println("dfs done");
         System.out.println("count "+count);
         
        
         
         //printMap(curMap);
         for(int i=0;i<20;i++){
                    for(int j=0;j<15;j++){
                        if(curMap[i][j]==1){
                            curMap[i][j]=2;
                        }
                    }
                
          }
         //printMap(curMap);
        
         
         int circle=0;

         this.stopP=new int[]{curPos[0],curPos[1]}; 
         
         adjustSensor();
         exploreOccupiedSpace(this.curPos);
         updateMap(this.curPos);
         turnRight();
         adjustSensor();
         exploreOccupiedSpace(this.curPos);
         updateMap(this.curPos);
   
        
        
       while(curPos[0]!=1||curPos[1]!=1||found_goal==0||found_start==0){
        
             
              
               t2=System.currentTimeMillis();
             
               if(t2-t1>=time_limit){break;}
             
               System.out.println("*******current pos: "+curPos[0]+" "+curPos[1]);
               System.out.println("*******current direc: "+d[0]+" "+d[1]);
              
               
               if(curPos[0]==18&&curPos[1]==13){
                  found_goal++; 
                
               }
               
               if(curPos[0]==1&&curPos[1]==1){
                   
                   found_start++; 
                    System.out.println("#########Total count= "+count);
                   
                   System.out.println("#########found_start= "+found_start);
                    tCM1=md1.transCurMap(this.curMap);
                    ss=md1.twoDtoStrings(tCM1);
                    System.out.println(ss.get(0));
                    System.out.println(ss.get(1));
                    
                    //ShortestPath mySP6 = new ShortestPath(new int[]{1,1},new int[]{18,13} ,curMap);
                    //ArrayList sp6=mySP6.searchShortestPath();
                    
                    if(found_goal!=0) break;   
                   
               }
               
               if(found_start>=5) break;
               
            //*****************Alignment********************************************
               if((sensorAlignFront||canAlignFront())&&canAlignLeft()){
                   
                     turnLeft();
                         adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                         
                      doAlignment();
//                       
                      turnRight();
                          adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                      
                       cycle_counter=0;
                       
                       
                       doAlignment();
                      align.add(new int[]{curPos[0],curPos[1]});
                      m.g.cells2[19-curPos[0]][curPos[1]].setBackground(Color.ORANGE);
               
               }
               
               
               
                 if((sensorAlignFront||canAlignFront())&&!canAlignLeft()){
                     doAlignment();
                         
                       //cycle_counter=0;
                       
                      align.add(new int[]{curPos[0],curPos[1]});
                      m.g.cells2[19-curPos[0]][curPos[1]].setBackground(Color.ORANGE);
                     
                 }
                 
             
                 if(!sensorAlignFront&&!canAlignFront()&&canAlignLeft()&&cycle_counter>=3){
                      turnLeft();
                         adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                         
                      doAlignment();
//                       
                      turnRight();
                          adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                      
                       cycle_counter=0;
                       
                      align.add(new int[]{curPos[0],curPos[1]});
                      m.g.cells2[19-curPos[0]][curPos[1]].setBackground(Color.ORANGE);
                     
                 }
             
       
            
             
             
             
          //*************************************************************
                    
            //no obstacle in left, turn left and move forward 
            if(isLeftEmpty(curPos)){
                
                
                    turnLeft();
                    adjustSensor();
                    exploreOccupiedSpace(this.curPos);
                    updateMap(this.curPos);
                    //if(unknownWithinSensor()) updateMap(this.curPos);
                    
                 
                     int ox=curPos[0];
                     int oy=curPos[1];
                     moveForward();
                     cycle_counter++;
                     
                    adjustSensor();
                    exploreOccupiedSpace(this.curPos);
                    updateMap(this.curPos);
                    
                     m.updateRobPos2(ox, oy, curPos[0], curPos[1],d[0],d[1]);
                      
                    //System. out.println("@@@@@@@@@@@@@@no obstacle on front and no obstacle in left, turn left and move forward");
                    continue;
            
            } 
            
  
             
            System.out.println("Sense front obstacle: "+senseFrontObstacle);          
           //if left not empty and front not empty, turn right 
            if(senseFrontObstacle||!isFrontEmpty(curPos)){  
            //if(){      
              
                    turnRight();
                    
                    adjustSensor();
                    exploreOccupiedSpace(this.curPos);
                    updateMap(this.curPos);
                    //if(unknownWithinSensor()) updateMap(this.curPos);
                    
                    
                    //System. out.println("@@@@@@@@@@@@@@wall in front, turn right");
                   
                    continue;
             }
            
            
            
            //if front empty && left not empty
            
            //if(isFrontEmpty(curPos)&&!isLeftEmpty(curPos)){
            if(!senseFrontObstacle&&!isLeftEmpty(curPos)){
                 
                     int ox=curPos[0];
                     int oy=curPos[1];
                     moveForward();
                     cycle_counter++;
                     
                    adjustSensor();
                    exploreOccupiedSpace(this.curPos);
                    updateMap(this.curPos);
                     
                     m.updateRobPos2(ox, oy, curPos[0], curPos[1],d[0],d[1]);

                    
                

                 
                 //System. out.println("@@@@@@@@@@@@@front empty && left not empty, move forward");
                 continue;
            } 
            
   
             
              
             if(curPos[0]==18&&curPos[1]==13){found_goal++; System.out.println("******Goal found");}
             if(curPos[0]==1&&curPos[1]==1){found_start++; System.out.println("******Start found");}
             
         
         }//end of while loop
         
         
         
         System.out.println("Total count= "+count);
        
         
         
         String sensorData1;
         
//         if(curPos[0]!=1||curPos[1]!=1){ 
//            
//             client.write("X|");
//         
//             dfsToStart(curPos);
//            sensorData1=client.read();
//            System.out.println("$$$$$$$$$$received from sensor: "+sensorData1);
//         
//             for(int m=0;m<20;m++){
//                                        for(int n=0;n<15;n++){
//                                            if(curMap[m][n]==1){
//                                                curMap[m][n]=2;
//                                            }
//                                        }
//
//             }
//         }
         
         tCM1=md1.transCurMap(this.curMap);
                    ss=md1.twoDtoStrings(tCM1);
                    System.out.println(ss.get(0));
                    System.out.println(ss.get(1));
         
         while(!canAlignLeft()||!canAlignFront()){
             turnRight();
             sensorData1=client.read();
             System.out.println("$$$$$$$$$$received from sensor: "+sensorData1);
            
         }
         
         turnLeft();
         sensorData1=client.read();
         System.out.println("$$$$$$$$$$received from sensor: "+sensorData1);
         
        
         
         doAlignment();
         
         turnRight();
         sensorData1=client.read();
         System.out.println("$$$$$$$$$$received from sensor: "+sensorData1);
         
         
         
         doAlignment();
         
         goToGoal();
     }
//************************************************************************************    
     public boolean isLeftWall(int[] pos){
         if(d[0]==0&&d[1]==1&&pos[0]==18) return true;
         else if(d[0]==-1&&d[1]==0&&pos[1]==13) return true;
         else if(d[0]==0&&d[1]==-1&&pos[0]==1) return true;
         else if(d[0]==1&&d[1]==0&&pos[1]==1) return true;
         return false;
     }
    
 //****************************Shortest Path**********************************************  
     public void goToStart(){
            
         ShortestPath mySP = new ShortestPath(curPos,new int[]{1,1} ,curMap);
         ArrayList sp=mySP.searchShortestPath();
         if(sp.size()>1){
             int[] t2=(int[])sp.get(1);
             int[] correctDirec=new int[]{t2[0]-curPos[0],t2[1]-curPos[1]};
             while(d[0]!=correctDirec[0]||d[1]!=correctDirec[1]) {turnRight();}
         }
         System.out.println("command sent to robot: "+mySP.finalPath);
         //client.write(mySP.finalSteps);
         
          try{Thread.sleep(100);}catch(Exception e){}
         client.write("R|"+mySP.finalPath);
         //m.paintSPath2(sp);
        
         goBack(sp);
     }
     
      public void goToGoal(){
             
         ShortestPath mySP = new ShortestPath(new int[]{1,1},new int[]{18,13} ,curMap);
         ArrayList sp2=mySP.searchShortestPath();
         if(sp2.size()>1){
             int[] t2=(int[])sp2.get(1);
             int[] correctDirec=new int[]{t2[0]-curPos[0],t2[1]-curPos[1]};
             while(d[0]!=correctDirec[0]||d[1]!=correctDirec[1]) {
                turnRight();
                adjustSensor();
                exploreOccupiedSpace(this.curPos);
                updateMap(this.curPos);
             }
             
             tCM1=md1.transCurMap(this.curMap);
             ss=md1.twoDtoStrings(tCM1);
             System.out.println(ss.get(0));
             System.out.println(ss.get(1));
             
                 //printShortestPath(sp2);
              System.out.println("command sent to robot: "+mySP.finalPath);
             // client.write(mySP.finalSteps);
              try{Thread.sleep(100);}catch(Exception e){}
              client.write("P");
              try{Thread.sleep(100);}catch(Exception e){}
             client.write(mySP.finalPath);
             m.paintSPath2(sp2);
             goBack(sp2);
         }
         
         else if(curPos[0]!=18||curPos[1]!=13){
             
                 //clear curMap and eOM
                //initialize current map
            for(int i=0;i<length;i++)
                for(int j=0;j<width;j++)
                    curMap[i][j]=0;


            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++)
                    curMap[19-i][14-j]=2;   //5 for Goal
            }


            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++)
                    curMap[i][j]=2;   //5 for Goal
            }

            this.eOM=new ExtendObstacleMap(); 
        
             
             
             
             adjustSensor();
             exploreOccupiedSpace(curPos);
            
             
             
             while(d[0]!=1||d[1]!=0) {
                 turnLeft();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
      
             
             
             }
             
       
             //open SENSOR
             try{Thread.sleep(100);}catch(Exception e){}
               client.write("P");
            
               try{Thread.sleep(100);}catch(Exception e){}
               client.write("X|");
             adjustSensor();
             exploreOccupiedSpace(curPos);
             updateMap(this.curPos);
             
             
             newExplorationToGoal();
         }
         
         else return;
         
     }
      
   public void goBack(ArrayList a){
            
            int[] oP=new int[2];
            int[] nP=new int[2];
            
            for(int i=0;i<a.size()-1;i++){
                oP=(int[])a.get(i);
                nP=(int[])a.get(i+1);
                
                d[0]=nP[0]-oP[0];
                d[1]=nP[1]-oP[1];
                m.updateRobPos2(oP[0],oP[1],nP[0],nP[1],d[0],d[1]);
                curPos[0]=nP[0];
                curPos[1]=nP[1];
               
                
            }
           
    } 
   
  //*********************************************************************************   
public void newExplorationToGoal(){
    
    
    
    
                
    
    
    while(curPos[0]!=18||curPos[1]!=13){
        
              
            //*****************Alignment********************************************
               if((sensorAlignFront||canAlignFront())&&canAlignLeft()){
                   
                     turnLeft();
                         adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                         
                      doAlignment();
                     
                      turnRight();
                          adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                      
                       cycle_counter=0;
                       
                       
                       doAlignment();
                      align.add(new int[]{curPos[0],curPos[1]});
                      m.g.cells2[19-curPos[0]][curPos[1]].setBackground(Color.ORANGE);
               
               }
               
               
               
                 if((sensorAlignFront||canAlignFront())&&!canAlignLeft()){
                     doAlignment();
                         
                       //cycle_counter=0;
                       
                      align.add(new int[]{curPos[0],curPos[1]});
                      m.g.cells2[19-curPos[0]][curPos[1]].setBackground(Color.ORANGE);
                     
                 }
                 
             
                 if(!sensorAlignFront&&!canAlignFront()&&canAlignLeft()&&cycle_counter>=3){
                      turnLeft();
                         adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                         
                      doAlignment();
                       
                      turnRight();
                          adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                      
                       cycle_counter=0;
                       
                      align.add(new int[]{curPos[0],curPos[1]});
                      m.g.cells2[19-curPos[0]][curPos[1]].setBackground(Color.ORANGE);
                     
                 }
             

             
             
          //*************************************************************
                    
            //no obstacle in left, turn left and move forward 
            if(isLeftEmpty(curPos)){
                
                
                    turnLeft();
                    adjustSensor();
                    exploreOccupiedSpace(this.curPos);
                    updateMap(this.curPos);
                    //if(unknownWithinSensor()) updateMap(this.curPos);
                    
                 
                     int ox=curPos[0];
                     int oy=curPos[1];
                     moveForward();
                     cycle_counter++;
                     
                    adjustSensor();
                    exploreOccupiedSpace(this.curPos);
                    updateMap(this.curPos);
                    
                     m.updateRobPos2(ox, oy, curPos[0], curPos[1],d[0],d[1]);
                      
                    //System. out.println("@@@@@@@@@@@@@@no obstacle on front and no obstacle in left, turn left and move forward");
                    continue;
            
            } 
            
  
             
            System.out.println("Sense front obstacle: "+senseFrontObstacle);          
           //if left not empty and front not empty, turn right 
            if(senseFrontObstacle||!isFrontEmpty(curPos)){  
            //if(){      
              
                    turnRight();
                    
                    adjustSensor();
                    exploreOccupiedSpace(this.curPos);
                    updateMap(this.curPos);
                    //if(unknownWithinSensor()) updateMap(this.curPos);
                    
                    
                    //System. out.println("@@@@@@@@@@@@@@wall in front, turn right");
                   
                    continue;
             }
            
            
            
            //if front empty && left not empty
            
            //if(isFrontEmpty(curPos)&&!isLeftEmpty(curPos)){
            if(!senseFrontObstacle&&!isLeftEmpty(curPos)){
                 
                     int ox=curPos[0];
                     int oy=curPos[1];
                     moveForward();
                     cycle_counter++;
                     
                    adjustSensor();
                    exploreOccupiedSpace(this.curPos);
                    updateMap(this.curPos);
                     
                     m.updateRobPos2(ox, oy, curPos[0], curPos[1],d[0],d[1]);

                    
                

                 
                 //System. out.println("@@@@@@@@@@@@@front empty && left not empty, move forward");
                 continue;
            } 
            
   
            
         
         }//end of while loop
         

}   
     
 
 //****************************Check if surrounding is empty/movable********************************************** 
     
     
     public boolean isLeftEmpty(int[] pos){
         if(d[0]==1&&d[1]==0){
             if(pos[1]<=1) return false;
             else if(curMap[pos[0]+1][pos[1]-2]!=2||curMap[pos[0]][pos[1]-2]!=2||curMap[pos[0]-1][pos[1]-2]!=2) return false;
             else return true;       
         }
         
         
         else if(d[0]==0&&d[1]==1){
             if(pos[0]>=18) return false;
             else if(curMap[pos[0]+2][pos[1]-1]!=2||curMap[pos[0]+2][pos[1]]!=2||curMap[pos[0]+2][pos[1]+1]!=2) return false;
             else return true;       
          }
          
         else if(d[0]==-1&&d[1]==0){
             if(pos[1]>=13) return false;
             else if(curMap[pos[0]+1][pos[1]+2]!=2||curMap[pos[0]][pos[1]+2]!=2||curMap[pos[0]-1][pos[1]+2]!=2) return false;
             else return true;       
           }
           
         else{ //0 -1
             if(pos[0]<=1) return false;
             else if(curMap[pos[0]-2][pos[1]-1]!=2||curMap[pos[0]-2][pos[1]]!=2||curMap[pos[0]-2][pos[1]+1]!=2) return false;
             else return true;       
           }
         
     
     }
     
     
     public boolean isFrontEmpty(int[] pos){
         if(d[0]==1&&d[1]==0){
             if(pos[0]>=18) return false;
             else if(curMap[pos[0]+2][pos[1]-1]!=2||curMap[pos[0]+2][pos[1]]!=2||curMap[pos[0]+2][pos[1]+1]!=2) return false;
             else return true;       
         }
         
         
         else if(d[0]==0&&d[1]==1){
             if(pos[1]>=13) return false;
             else if(curMap[pos[0]-1][pos[1]+2]!=2||curMap[pos[0]][pos[1]+2]!=2||curMap[pos[0]+1][pos[1]+2]!=2) return false;
             else return true;       
          }
          
         else if(d[0]==-1&&d[1]==0){
             if(pos[0]<=1) return false;
             else if(curMap[pos[0]-2][pos[1]+1]!=2||curMap[pos[0]-2][pos[1]]!=2||curMap[pos[0]-2][pos[1]-1]!=2) return false;
             else return true;       
           }
           
         else{ //0 -1
             if(pos[1]<=1) return false;
             else if(curMap[pos[0]-1][pos[1]-2]!=2||curMap[pos[0]][pos[1]-2]!=2||curMap[pos[0]+1][pos[1]-2]!=2) return false;
             else return true;       
           }
         
     
     }
     
  
//***************************Check whether can align***********************************************  
     public boolean canAlignFront(){
         
         if(d[0]==0&&d[1]==1) {
              if(curPos[1]==13) return true;
//             if(curPos[1]<=11&&curMap[curPos[0]+1][curPos[1]+2]==2&&curMap[curPos[0]-1][curPos[1]+2]==2&&curMap[curPos[0]+1][curPos[1]+3]==3&&curMap[curPos[0]-1][curPos[1]+3]==3)
//                 return true;
             if(curPos[1]<=12&&curMap[curPos[0]+1][curPos[1]+2]==3&&curMap[curPos[0]-1][curPos[1]+2]==3)
                 return true;
//            
//             else return (curPos[1]==12&&curMap[curPos[0]+1][curPos[1]+2]==2&&curMap[curPos[0]-1][curPos[1]+2]==2);
              else return false;
         }
         
         else if(d[0]==1&&d[1]==0){
             if(curPos[0]==18) return true;
//             if(curPos[0]<=16&&curMap[curPos[0]+2][curPos[1]-1]==2&&curMap[curPos[0]+2][curPos[1]+1]==2&&curMap[curPos[0]+3][curPos[1]-1]==3&&curMap[curPos[0]+3][curPos[1]+1]==3)
//                 return true;
             if(curPos[0]<=17&&curMap[curPos[0]+2][curPos[1]-1]==3&&curMap[curPos[0]+2][curPos[1]+1]==3)
                 return true;
//             
//             else return (curPos[0]==17&&curMap[curPos[0]+2][curPos[1]-1]==2&&curMap[curPos[0]+2][curPos[1]+1]==2);
             else return false;
         }
         else if(d[0]==-1&&d[1]==0){
             if(curPos[0]==1) return true;
//         
//             if(curPos[0]>=3&&curMap[curPos[0]-2][curPos[1]+1]==2&&curMap[curPos[0]-2][curPos[1]-1]==2&&curMap[curPos[0]-3][curPos[1]+1]==3&&curMap[curPos[0]-3][curPos[1]-1]==3)
//                     return true;
//             
             if(curPos[0]>=2&&curMap[curPos[0]-2][curPos[1]+1]==3&&curMap[curPos[0]-2][curPos[1]-1]==3)
                     return true;
//             
//             else return (curPos[0]==2&&curMap[curPos[0]-2][curPos[1]+1]==2&&curMap[curPos[0]-2][curPos[1]-1]==2);
             else return false;
         }

         else{  //d[0]==0&&d[1]==-1
            if(curPos[1]==1) return true;
//            if(curPos[1]>=3&&curMap[curPos[0]+1][curPos[1]-2]==2&&curMap[curPos[0]-1][curPos[1]-2]==2&&curMap[curPos[0]+1][curPos[1]-3]==3&&curMap[curPos[0]-1][curPos[1]-3]==3)
//                 return true;
            if(curPos[1]>=2&&curMap[curPos[0]+1][curPos[1]-2]==3&&curMap[curPos[0]-1][curPos[1]-2]==3)
                 return true;
//            
//             else return (curPos[1]==2&&curMap[curPos[0]+1][curPos[1]-2]==2&&curMap[curPos[0]-1][curPos[1]-2]==2);
            else return false;
         }
         
         
         
         
     
     
     }
     
     
    public boolean canAlignLeft(){
         
         
         if(d[0]==0&&d[1]==1) {
             if(curPos[0]==18) return true;
             
//             if(curPos[0]<=16&&curMap[curPos[0]+2][curPos[1]+1]==2&&curMap[curPos[0]+2][curPos[1]-1]==2&&curMap[curPos[0]+3][curPos[1]+1]==3&&curMap[curPos[0]+3][curPos[1]-1]==3)
//                 return true;
              if(curPos[0]<=17&&curMap[curPos[0]+2][curPos[1]+1]==3&&curMap[curPos[0]+2][curPos[1]-1]==3)
                return true;
//              
//             else return (curPos[0]==17&&curMap[curPos[0]+2][curPos[1]+1]==2&&curMap[curPos[0]+2][curPos[1]-1]==2);
             else return false;
         }
         
         else if(d[0]==1&&d[1]==0){
             if(curPos[1]==1) return true;
//             if(curPos[1]>=3&&curMap[curPos[0]+1][curPos[1]-2]==2&&curMap[curPos[0]-1][curPos[1]-2]==2&&curMap[curPos[0]+1][curPos[1]-3]==3&&curMap[curPos[0]-1][curPos[1]-3]==3)
//                 return true;
            if(curPos[1]>=2&&curMap[curPos[0]+1][curPos[1]-2]==3&&curMap[curPos[0]-1][curPos[1]-2]==3)
                 return true;
//             
//             else return (curPos[1]==2&&curMap[curPos[0]+1][curPos[1]-2]==2&&curMap[curPos[0]-1][curPos[1]-2]==2);
             else return false;
         }
         else if(d[0]==-1&&d[1]==0){
              if(curPos[1]==13) return true;
         
//             if(curPos[1]<=11&&curMap[curPos[0]+1][curPos[1]+2]==2&&curMap[curPos[0]-1][curPos[1]+2]==2&&curMap[curPos[0]+1][curPos[1]+3]==3&&curMap[curPos[0]-1][curPos[1]+3]==3)
//                     return true;
//             
             if(curPos[1]<=12&&curMap[curPos[0]+1][curPos[1]+2]==3&&curMap[curPos[0]-1][curPos[1]+2]==3)
                     return true;
//            
//             
//                 else return (curPos[1]==12&&curMap[curPos[0]+1][curPos[1]+2]==2&&curMap[curPos[0]-1][curPos[1]+2]==2);
              else return false;
         }

         else{  //d[0]==0&&d[1]==-1
             if(curPos[0]==1) return true;
//            if(curPos[0]>=3&&curMap[curPos[0]-2][curPos[1]+1]==2&&curMap[curPos[0]-2][curPos[1]-1]==2&&curMap[curPos[0]-3][curPos[1]+1]==3&&curMap[curPos[0]-3][curPos[1]-1]==3)
//                 return true;
            if(curPos[0]>=2&&curMap[curPos[0]-2][curPos[1]+1]==3&&curMap[curPos[0]-2][curPos[1]-1]==3)
                 return true;
//            
//            
//             else return (curPos[0]==2&&curMap[curPos[0]-2][curPos[1]+1]==2&&curMap[curPos[0]-2][curPos[1]-1]==2);
             else return false;
         }
         
         
          
     
     
     }
     
    
   //**********************Robot Command****************************************************  
     
     public void moveForward(){
         this.curPos[0]+=d[0];
         this.curPos[1]+=d[1];
         
         //robot command
         client.write("W|");
     }
     
    
     
     public void turnLeft(){
         
         if(d[0]==0&&d[1]==-1){d[0]=-1;d[1]=0;}
         else if(d[0]==-1&&d[1]==0){d[0]=0;d[1]=1;}
         else if(d[0]==0&&d[1]==1){d[0]=1;d[1]=0;}
         else {d[0]=0;d[1]=-1;}
         
         m.updateRobPos2(curPos[0],curPos[1],curPos[0],curPos[1],d[0],d[1]);
         //robot command
         client.write("A|");
     
     }
     
     public void turnRight(){
         if(d[0]==0&&d[1]==-1){d[0]=1;d[1]=0;}
         else if(d[0]==1&&d[1]==0){d[0]=0;d[1]=1;}
         else if(d[0]==0&&d[1]==1){d[0]=-1;d[1]=0;}
         else {d[0]=0;d[1]=-1;}
         
         m.updateRobPos2(curPos[0],curPos[1],curPos[0],curPos[1],d[0],d[1]);
         //robot command
        client.write("D|");
     }
     
     
     public void doAlignment(){
     
         //robot command
         client.write("Q|");
     
     }
     
//**************************************************************************     
     
     
//************************************************************************** 
     
     
     public boolean isWall(int x, int y){
         return (x<=0||x>=19||y<=0||y>=14);
     
     }
     
     public boolean withinBoundary(int x, int y){
         return (x>=0&&x<=19&&y>=0&&y<=14);
     }
     
       public boolean nextToWall(int x, int y){
         return (x==1||x==18||y==1||y==13);
     
     }
    
//************************************************************************** 
  
     
     
    public void adjustSensor(){
          
        this.sensor1=new ArrayList();
        this.sensor2=new ArrayList();
        this.sensor3=new ArrayList();
        this.sensor4=new ArrayList();
        this.sensor5=new ArrayList();
        this.sensor6=new ArrayList();
        
        if(d[0]==1&&d[1]==0){  //N
        
                sensor1.add(new int[]{1,-2}); 
                sensor1.add(new int[]{1,-3});
                //sensor1.add(new int[]{1,-4});  

                sensor2.add(new int[]{2,-1});
                sensor2.add(new int[]{3,-1});
                //sensor2.add(new int[]{4,-1});

                sensor3.add(new int[]{2,0});
               // sensor3.add(new int[]{3,0});

                sensor4.add(new int[]{2,1});
                sensor4.add(new int[]{3,1});
                //sensor4.add(new int[]{4,1});

                sensor6.add(new int[]{-1,2});
                sensor6.add(new int[]{-1,3});
                sensor5.add(new int[]{-1,4});
                sensor5.add(new int[]{-1,5});
                sensor5.add(new int[]{-1,6});
               // sensor5.add(new int[]{-1,7});
                //sensor5.add(new int[]{-1,8});
        
        
        }
        
        
        else if(d[0]==0&&d[1]==-1){ //W
                
                sensor1.add(new int[]{-2,-1}); 
                sensor1.add(new int[]{-3,-1});
                //sensor1.add(new int[]{-4,-1});  

                sensor2.add(new int[]{-1,-2});
                sensor2.add(new int[]{-1,-3});
                //sensor2.add(new int[]{-1,-4});

                sensor3.add(new int[]{0,-2});
                //sensor3.add(new int[]{0,-3});

                sensor4.add(new int[]{1,-2});
                sensor4.add(new int[]{1,-3});
                //sensor4.add(new int[]{1,-4});

                sensor6.add(new int[]{2,1});
                sensor6.add(new int[]{3,1});
                sensor5.add(new int[]{4,1});
                sensor5.add(new int[]{5,1});
                sensor5.add(new int[]{6,1});
               // sensor5.add(new int[]{7,1});
                //sensor5.add(new int[]{8,1});
        
        }
        
        
        else if(d[0]==-1&&d[1]==0){  //S
                sensor1.add(new int[]{-1,2}); 
                sensor1.add(new int[]{-1,3});
                //sensor1.add(new int[]{-1,4});  

                sensor2.add(new int[]{-2,1});
                sensor2.add(new int[]{-3,1});
                //sensor2.add(new int[]{-4,1});

                sensor3.add(new int[]{-2,0});
                //sensor3.add(new int[]{-3,0});

                sensor4.add(new int[]{-2,-1});
                sensor4.add(new int[]{-3,-1});
                //sensor4.add(new int[]{-4,-1});

                sensor6.add(new int[]{1,-2});
                sensor6.add(new int[]{1,-3});
                sensor5.add(new int[]{1,-4});
                sensor5.add(new int[]{1,-5});
                sensor5.add(new int[]{1,-6});
               // sensor5.add(new int[]{1,-7});
                //sensor5.add(new int[]{1,-8});
        }
        
        else{   //E
                sensor1.add(new int[]{2,1}); 
                sensor1.add(new int[]{3,1});
                //sensor1.add(new int[]{4,1});  

                sensor2.add(new int[]{1,2});
                sensor2.add(new int[]{1,3});
                //sensor2.add(new int[]{1,4});

                sensor3.add(new int[]{0,2});
                //sensor3.add(new int[]{0,3});

                sensor4.add(new int[]{-1,2});
                sensor4.add(new int[]{-1,3});
                //sensor4.add(new int[]{-1,4});

                sensor6.add(new int[]{-2,-1});
                sensor6.add(new int[]{-3,-1});
                sensor5.add(new int[]{-4,-1});
                sensor5.add(new int[]{-5,-1});
                sensor5.add(new int[]{-6,-1});
                //sensor5.add(new int[]{-7,-1});
                //sensor5.add(new int[]{-8,-1});
        
        
        }
     }
    

  //**************************************************************************      
          
      public void updateMap(int[] pos){
         ////////////add statements to receive info from arduino
         //System.out.println("SENSING........");
         String sensorData=client.read();
         System.out.println("$$$$$$$$$$received from sensor: "+sensorData);
         
         if (sensorData==null) {System.out.println("sensor Data null"); return;}
         
         interpretSensor(sensorData);
         boolean inSR=false;
         
            //sensor1
         for(int i=0;i<sensor1.size();i++){
             
             int[] t=(int[]) sensor1.get(i);
             if(withinBoundary(pos[0]+t[0],pos[1]+t[1])){
                 
                   if(curMap[pos[0]+t[0]][pos[1]+t[1]]==3) { 
                       break;}
                   
                   if(curMap[pos[0]+t[0]][pos[1]+t[1]]==0){ 
                       int temp=occupancy[pos[0]+t[0]][pos[1]+t[1]];
	               if(temp==0) temp=2;
                       curMap[pos[0]+t[0]][pos[1]+t[1]]=temp; 
                      
                       m.updateCell(curMap,pos[0]+t[0],pos[1]+t[1],temp);
                       count++;
                     
                       if(temp==3) { eOM.extendObstacle(pos[0]+t[0], pos[1]+t[1]);  break;};
                    }
             }
                   
         }
         
         //sensor2
          for(int i=0;i<sensor2.size();i++){
               
             int[] t=(int[]) sensor2.get(i);
            if(withinBoundary(pos[0]+t[0],pos[1]+t[1])){
                //if(curMap[pos[0]+t[0]][pos[1]+t[1]]==3) {  break;}
                   if(curMap[pos[0]+t[0]][pos[1]+t[1]]==0) count++;
                   
                       int temp=occupancy[pos[0]+t[0]][pos[1]+t[1]];
                       if(temp==0) temp=2;
                       
                   if(curMap[pos[0]+t[0]][pos[1]+t[1]]!=1){
                       curMap[pos[0]+t[0]][pos[1]+t[1]]=temp; 
                       m.updateCell(curMap,pos[0]+t[0],pos[1]+t[1],temp);
                       
                       if(temp==3) { eOM.extendObstacle(pos[0]+t[0], pos[1]+t[1]); break;};
                   } 
             }
                   
         }
          
          
          //sensor3
          for(int i=0;i<sensor3.size();i++){
             
             int[] t=(int[]) sensor3.get(i);
             if(withinBoundary(pos[0]+t[0],pos[1]+t[1])){
                //if(curMap[pos[0]+t[0]][pos[1]+t[1]]==3) {  break;}
                   if(curMap[pos[0]+t[0]][pos[1]+t[1]]==0) count++;
                   
                       int temp=occupancy[pos[0]+t[0]][pos[1]+t[1]];
                       if(temp==0) temp=2;
                       
                   if(curMap[pos[0]+t[0]][pos[1]+t[1]]!=1){
                       curMap[pos[0]+t[0]][pos[1]+t[1]]=temp; 
                       m.updateCell(curMap,pos[0]+t[0],pos[1]+t[1],temp);
                       
                       if(temp==3) { eOM.extendObstacle(pos[0]+t[0], pos[1]+t[1]); break;};
                   } 
             }
                   
         }
          
          //sensor4
          for(int i=0;i<sensor4.size();i++){
            
             int[] t=(int[]) sensor4.get(i);
            if(withinBoundary(pos[0]+t[0],pos[1]+t[1])){
                //if(curMap[pos[0]+t[0]][pos[1]+t[1]]==3) {  break;}
                   if(curMap[pos[0]+t[0]][pos[1]+t[1]]==0) count++;
                   
                       int temp=occupancy[pos[0]+t[0]][pos[1]+t[1]];
                       if(temp==0) temp=2;
                       
                   if(curMap[pos[0]+t[0]][pos[1]+t[1]]!=1){
                       curMap[pos[0]+t[0]][pos[1]+t[1]]=temp; 
                       m.updateCell(curMap,pos[0]+t[0],pos[1]+t[1],temp);
                       
                       if(temp==3) { eOM.extendObstacle(pos[0]+t[0], pos[1]+t[1]); break;};
                   } 
             }
                   
         }
          
          //sensor6 right SR 
          for(int i=0;i<sensor6.size();i++){ 
              
             int[] t=(int[]) sensor6.get(i);
             if(withinBoundary(pos[0]+t[0],pos[1]+t[1])){
                  if(curMap[pos[0]+t[0]][pos[1]+t[1]]==3) {inSR=true;break;}
                   if(curMap[pos[0]+t[0]][pos[1]+t[1]]==0){ 
                       int temp=occupancy[pos[0]+t[0]][pos[1]+t[1]];
                       if(temp==0) temp=2;
                       curMap[pos[0]+t[0]][pos[1]+t[1]]=temp; 
                       m.updateCell(curMap,pos[0]+t[0],pos[1]+t[1],temp);
                       count++;
                       if(temp==3) {inSR=true;eOM.extendObstacle(pos[0]+t[0], pos[1]+t[1]); break;};
                    }
             }
                   
         }
          
           for(int i=0;i<3;i++){
              for(int j=0;j<3;j++){
                  if(curMap[i][j]!=1)
                    m.updateCell(curMap,i,j,2);
              }
          }
          
          for(int i=17;i<20;i++){
              for(int j=12;j<15;j++){
                  if(curMap[i][j]!=1)
                    m.updateCell(curMap,i,j,2);
              }
          }
          
          
          if(inSR==true) return;
          
          
          // sensor 5 right LR
          for(int i=0;i<sensor5.size();i++){  
              
             int[] t=(int[]) sensor5.get(i);
             if(withinBoundary(pos[0]+t[0],pos[1]+t[1])){
                  if(curMap[pos[0]+t[0]][pos[1]+t[1]]==3) break;
                   if(curMap[pos[0]+t[0]][pos[1]+t[1]]==0){ 
                       int temp=occupancy[pos[0]+t[0]][pos[1]+t[1]];
                        if(temp==0) temp=2;
                       curMap[pos[0]+t[0]][pos[1]+t[1]]=temp; 
                       m.updateCell(curMap,pos[0]+t[0],pos[1]+t[1],temp);
                       count++;
                       if(temp==3) {eOM.extendObstacle(pos[0]+t[0], pos[1]+t[1]); break;};
                    }
             }
                   
         }
      
        
          
             for(int i=0;i<3;i++){
              for(int j=0;j<3;j++){
                  if(curMap[i][j]!=1)
                    m.updateCell(curMap,i,j,2);
              }
          }
          
          for(int i=17;i<20;i++){
              for(int j=12;j<15;j++){
                  if(curMap[i][j]!=1)
                    m.updateCell(curMap,i,j,2);
              }
          }
      
         
     
     }
     
     
        public void interpretSensor(String s){
         //interprete string
         //update occupancy!! map
         s=s.substring(0, s.length()-1);
         
         double r1=0;
         double r2=0;
         double r3=0;
         double r4=0;
         double r5=0;
         double r6=0;
         
         int i;
         int[] t;
         
         boolean isSR=false;
         this.senseFrontObstacle=false;
         this.sensorAlignFront=false;
         
         String[] as=new String[6];
         as=s.split(",");
         
         
         r1=Double.parseDouble(as[0]);
         r2=Double.parseDouble(as[1]);
         r3=Double.parseDouble(as[2]);
         r4=Double.parseDouble(as[3]);
         r5=Double.parseDouble(as[4]);
         r6=Double.parseDouble(as[5]);
         
         r2-=3;
         r4-=3;
         
         r5+=4;
         r6-=0.5;
         
         if(r2<15||r3==1||r4<15) senseFrontObstacle=true;
         if(r2<15&&r4<15) sensorAlignFront=true;
         
         if(r1<26&&r6>=0) {
             i=(int)(r1+4-10)/10;
             if(r1<10) i=0;
             t=(int[])sensor1.get(i);
             
             if(withinBoundary(curPos[0]+t[0],curPos[1]+t[1])){
                 occupancy[curPos[0]+t[0]][curPos[1]+t[1]]=3;
                 System.out.println("r1 value"+r1+ "  "+(curPos[0]+t[0])+","+(curPos[1]+t[1]));
             }
             
          }
         
         
         if(r2<26&&r6>=0) {
             i=(int)(r2+4-10)/10;
             if(r2<10) i=0;
             t=(int[])sensor2.get(i);
             
             if(withinBoundary(curPos[0]+t[0],curPos[1]+t[1])){
                 occupancy[curPos[0]+t[0]][curPos[1]+t[1]]=3;
                 System.out.println("r2 value"+r2+ "  "+(curPos[0]+t[0])+","+(curPos[1]+t[1]));
             }
             
          }
         
         if(r3==1) {   //ultrasonic
             i=0;
             t=(int[])sensor3.get(i);
             
             if(withinBoundary(curPos[0]+t[0],curPos[1]+t[1])){
                 occupancy[curPos[0]+t[0]][curPos[1]+t[1]]=3;
                 System.out.println("r3 value"+r3+ "  "+(curPos[0]+t[0])+","+(curPos[1]+t[1]));
             }
             
          }
         
        
         if(r4<26&&r6>=0) {
             i=(int)(r4+4-10)/10;
             if(r4<10) i=0;
             t=(int[])sensor4.get(i);
             
             if(withinBoundary(curPos[0]+t[0],curPos[1]+t[1])){
                 occupancy[curPos[0]+t[0]][curPos[1]+t[1]]=3;
                 System.out.println("r4 value"+r4+ "  "+(curPos[0]+t[0])+","+(curPos[1]+t[1]));
             }
             
          }
         
         
         //SR
        
         if(r6<26&&r6>=0){
             i=(int)(r6+4-10)/10;
             if(r6<10) i=0;
             t=(int[])sensor6.get(i);
             
             if(withinBoundary(curPos[0]+t[0],curPos[1]+t[1])){
                 occupancy[curPos[0]+t[0]][curPos[1]+t[1]]=3;
                 System.out.println("r6 SR value"+r6+ "  "+(curPos[0]+t[0])+","+(curPos[1]+t[1]));
             }
             isSR=true;
         
         }
         
         
         
         
         if(isSR==true) return;
         
        
         if(r5>27&&r5<55) {
             i=(int)(r5+4-30)/10;
             t=(int[])sensor5.get(i);
             
             if(withinBoundary(curPos[0]+t[0],curPos[1]+t[1])){
                 occupancy[curPos[0]+t[0]][curPos[1]+t[1]]=3;
                 System.out.println("r5 value"+r5+ "  "+(curPos[0]+t[0])+","+(curPos[1]+t[1]));
             }
             
          }
         
         
     
     }
    //**************************************************************************   
      
     public void exploreOccupiedSpaceDFS(int pos[]){
         for(int i=-1;i<2;i++){
             for(int j=-1;j<2;j++){
                 if(withinBoundary(pos[0]+i,pos[1]+j)){
                   if(curMap[pos[0]+i][pos[1]+j]==0){  
                       curMap[pos[0]+i][pos[1]+j]=2; 
                       m.updateCell(curMap,pos[0]+i,pos[1]+j,2);
                       count++;
                   }
                       
                 }
              }
         }
         curMap[pos[0]][pos[1]]=1;
     
     }
     
     
      public void exploreOccupiedSpace(int pos[]){
         for(int i=-1;i<2;i++){
             for(int j=-1;j<2;j++){
                 if(withinBoundary(pos[0]+i,pos[1]+j)){
                   if(curMap[pos[0]+i][pos[1]+j]==0){  
                       curMap[pos[0]+i][pos[1]+j]=2; 
                       m.updateCell(curMap,pos[0]+i,pos[1]+j,2);
                       count++;
                   }
                       
                 }
              }
         }
         
     
     }
     
  
  //***************************Testing purpose***********************************************     
     public void printAlign(){
        this.m.printAlign(this.align);
     
     }
     
     
     public void printMap(int[][] map){
        
        for(int i=0;i<map.length;i++){
            System.out.print("{");
            for(int j=0;j<map[0].length;j++){
                System.out.print(map[i][j]+",");
            }
        
             System.out.println("},");
        }
       
    
    }
     
        public void printShortestPath(ArrayList sp){
		System.out.println("Shortest Path:");
                for(int i=0;i<sp.size();i++){
                 int[] temp=(int[]) sp.get(i);
                 System.out.println("("+temp[0]+","+temp[1]+") ");
                
                }
	}
        


//*******************************************************************************
 
 
   
   public void dfs(int[] pos){
        //int[] cPos=new int[2];  //next position
                   
       
       
        if(pos[0]==1&&pos[1]==1) found_start++;
        if(pos[0]==18&&pos[1]==13) found_goal++;
        
      
        if(nextToWall(pos[0],pos[1]))//Goal found
            wall=true;
        
        
        
        
        
        while(!wall){
             //System.out.println("dfs:   count "+count);
   
            
             System.out.println("*******current pos: "+pos[0]+" "+pos[1]);
             System.out.println("*******current direc: "+d[0]+" "+d[1]);
             
             adjustSensor();
             exploreOccupiedSpaceDFS(pos);
             updateMap(pos);  
             
                //*****************Alignment********************************************
               if(canAlignFront()&&canAlignLeft()){
                   
                     turnLeft();
                         adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                         
                      doAlignment();
                     
                      turnRight();
                          adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                      
                      
                       
                       
                       doAlignment();
                      align.add(new int[]{curPos[0],curPos[1]});
                      m.g.cells2[19-curPos[0]][curPos[1]].setBackground(Color.ORANGE);
               
               }
               
               
               
                 if(canAlignFront()&&!canAlignLeft()){
                     doAlignment();
                         
                       //cycle_counter=0;
                       
                      align.add(new int[]{curPos[0],curPos[1]});
                      m.g.cells2[19-curPos[0]][curPos[1]].setBackground(Color.ORANGE);
                     
                 }
                 
             
                 if(!canAlignFront()&&canAlignLeft()){
                      turnLeft();
                         adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                         
                      doAlignment();
                       
                      turnRight();
                          adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                      
                      
                       
                      align.add(new int[]{curPos[0],curPos[1]});
                      m.g.cells2[19-curPos[0]][curPos[1]].setBackground(Color.ORANGE);
                     
                 }

        //ESWN        
            for(int i=0;i<4;i++){
                int[] w=(int[]) direc.get(i);

                if((pos[1]<=1&&i==2)||(pos[0]>=18&&i==3)||(pos[1]>=13&&i==0)||(pos[0]<=1&&i==1)) continue;   
                // don't go into wall direction
                if(curMap[pos[0]+w[0]][pos[1]+w[1]]==1||curMap[pos[0]+w[0]][pos[1]+w[1]]==0) continue;
                
                //if visited or unknown, don't go there
                if(this.eOM.isMovable(pos[0]+w[0],pos[1]+w[1])==false) continue;
                //If obstacle or obstacle boundary, don't go there
                
                
                path.add(new int[]{pos[0],pos[1]});  
                int[] oldPos=new int[]{pos[0],pos[1]};
                
                //System.out.println("dx="+d[0]+" dy="+d[1]+"  wx="+w[0]+" wy="+w[1]);  
                generateCommandDFS(d[0],d[1],w[0],w[1]);
              
                
           
                
                //m.updateRobPos(pos[0],pos[1],cPos[0],cPos[1]);
               m.updateRobPos2(oldPos[0],oldPos[1],pos[0],pos[1],d[0],d[1]);
              
                dfs(pos);      
                if(out==true) return;


            }//end of for loop
           
            if(out==false){
                
                
                int[] temp=new int[2];
                temp[0]=pos[0];
                temp[1]=pos[1];
                        
             
                if(path.size()==0){System.out.println("No way to go. Error"); System.exit(0);}
                if(path.size()!=0){
                    int[] tpos=(int[])path.remove(path.size()-1);
                    generateCommandDFS(d[0],d[1],tpos[0]-pos[0],tpos[1]-pos[1]);

                    m.updateRobPos2(temp[0],temp[1],pos[0],pos[1],d[0],d[1]);
                }
                else{System.out.println("No way to go");}
            }
        }//end of while loop
 
        out=true;
        curPos=pos;
        //System.out.println("count "+count);
   
   }
    
    
      public void generateCommandDFS(int dx,int dy,int wx,int wy){

        if(dx==1&&dy==0){
            if(wx==1&&wy==0){ 
            
                    moveForward();
         
            }
            else if(wx==0&&wy==1)  {
               
                 turnRight();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
               
                 moveForward();
           
            
            }
            else if(wx==-1&&wy==0) { 
                
           
                 turnRight();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 
          
                 turnRight();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
            
 
                 moveForward();

            
            
            
            
            }
            else  {
              
                 turnLeft();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                
                 moveForward();
           
            
            }  //0 -1
        
        }
        
        else if(dx==0&&dy==1){
            if(wx==1&&wy==0) {
             
                 turnLeft();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
            
                 moveForward();
        
            
            }
            else if(wx==0&&wy==1)  {
              
                moveForward();

           
            }
            else if(wx==-1&&wy==0)  {
            
                turnRight();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
              
                 moveForward();

            
            }
            else  {
             
                turnRight();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
             
                turnRight();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
            
                 moveForward();
            
            } //0 -1
        
        }
        
        else if(dx==-1&&dy==0){
            if(wx==1&&wy==0){ 
            
                turnLeft();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
            
                 turnLeft();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
               
                 moveForward();

            }
            else if(wx==0&&wy==1)  {
              
                turnLeft();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                
                 moveForward();
 
            
            }
            else if(wx==-1&&wy==0)  {
                
                 moveForward();

            }
            else  {
             
                turnRight();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
            
                moveForward();

            
            
            }  //0 -1
        
        }
        else{     //if(dx==0&&dy==-1)
            if(wx==1&&wy==0) {
               
                turnRight();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
               
                moveForward();

           
            }
            else if(wx==0&&wy==1)  {
              
                turnRight();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
               
                turnRight();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
            
                 moveForward();

            
            }
            else if(wx==-1&&wy==0)  {
               
                 turnLeft();
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                
                 moveForward();

            }
            else  {
                
                moveForward();
//                
            } //0 -1
        
        }
   
   
      }
 
 
 
}





