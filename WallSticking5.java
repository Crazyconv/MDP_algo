import java.awt.Color;
import java.util.ArrayList;
import java.math.*;
 //if oocuoancy2 is L, circling
public class WallSticking5 {
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
    ArrayList direc2=new ArrayList();
    
    
    
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
    
    long t1,t2;
    
 
    int delay;
    
    
    //Client client;
    
   // 0 for unexplored; 1 for visited; 2 for empty space but not visited ; 3 for obstacle; 5 for goal
    
     public  WallSticking5(int[] strP,double coverage_limit,int[][] occupancy,long time_limit, int speed){
        
     
        
        this.coverage_limit=coverage_limit;
        this.time_limit=time_limit;
   
        this.delay=(int)1000/speed;
        
        sPos[0]=strP[0];
        sPos[1]=strP[1];
        this.curPos=sPos;
       
        
        d=new int[]{0,-1};
        
       
        
       
       this.align=new ArrayList(); 
       this.path=new ArrayList(); 
       this.path2=new ArrayList(); 
        //this.visited=new ArrayList(); 
        
        
        this.occupancy=occupancy;
        
        direc.add(new int[]{0,-1});  //W
        direc.add(new int[]{1,0});   //N
        direc.add(new int[]{0,1});   //E
        direc.add(new int[]{-1,0});  //S
        
        
        direc2.add(new int[]{0,-1});  //W
        direc2.add(new int[]{-1,-0});   //S
        direc2.add(new int[]{1,0});   //N
        direc2.add(new int[]{0,1});  //E
                
        
       
        
        this.count=18;  //count explored
      
        this.cycle_counter=0;
        this.found_goal=0;
        this.found_start=0;
        out=false;
        out2=false;
        wall=false;
        backToStart=false;
         
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
         
       MapDescriptor md1=new MapDescriptor();
         int[][] tCM1=new int[20][15];
         ArrayList<String> ss=new ArrayList<String>(); //Map Descriptor Strings
         
         
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
   
        
        
       while(count<300*coverage_limit||found_goal==0||found_start==0){
        
              
             
             if(curPos[0]==stopP[0]&&curPos[1]==stopP[1]&&isLeftWall(curPos) ){
                 circle++;
                 if(circle==2) {
                     //System.out.println("e%%%%%%%%%%%%%%%%explore a circle%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                     break;}
                 
             }
              
               t2=System.currentTimeMillis();
             
               if(t2-t1>=time_limit){
                   //System.out.println("Exceeed time limit "+(t2-t1)+"ms");
                   break;}
             
               System.out.println("*******current pos: "+curPos[0]+" "+curPos[1]);
               System.out.println("*******current direc: "+d[0]+" "+d[1]);
               //System. out.println("******count="+count);
               //System. out.println("********found goal="+found_goal);
               
               if(curPos[0]==18&&curPos[1]==13){found_goal++; if(count>=300*coverage_limit&&found_start!=0) break;}
               
               if(curPos[0]==1&&curPos[1]==1){
                   
                    System.out.println("#########Total count= "+count);
                   found_start++; 
                    tCM1=md1.transCurMap(this.curMap);
                    ss=md1.twoDtoStrings(tCM1);
                    System.out.println(ss.get(0));
                    System.out.println(ss.get(1));
                    
                    ShortestPath mySP6 = new ShortestPath(new int[]{1,1},new int[]{18,13} ,curMap);
                    ArrayList sp6=mySP6.searchShortestPath();
                    
                    if((count>=300*coverage_limit)&&found_goal!=0) break;
                    
                    
                  
                   
               }
               
               if(found_start>=3) break;
              
               
               if(!mustFindGoal&&count>=300*coverage_limit){break;}
            //*****************Alignment********************************************
               if(canAlignFront()&&canAlignLeft()){
                   
                     turnLeft();
                    
                         adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                      
                       try{Thread.sleep(delay);}catch(Exception e){}   
                         
                      doAlignment();
                      
                      try{Thread.sleep(delay);}catch(Exception e){}
                      
                      turnRight();
                      
                          adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                      
                       cycle_counter=0;
                       
                        try{Thread.sleep(delay);}catch(Exception e){}
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
                 
             
                 if(!canAlignFront()&&canAlignLeft()&&cycle_counter>=3){
                      turnLeft();
                         adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                      
                       try{Thread.sleep(delay);}catch(Exception e){}
   
                         
                      doAlignment();
                      try{Thread.sleep(delay);}catch(Exception e){}
                       
                      turnRight();
                      
                          adjustSensor();
                         exploreOccupiedSpace(this.curPos);
                         updateMap(this.curPos);
                      try{Thread.sleep(delay);}catch(Exception e){}
                       cycle_counter=0;
                       
                      align.add(new int[]{curPos[0],curPos[1]});
                      m.g.cells2[19-curPos[0]][curPos[1]].setBackground(Color.ORANGE);
                     
                 }
             
             
         
                    
            //no obstacle on front and no obstacle in left, turn left and move forward 
            if(isLeftEmpty(curPos)){
                
                try{Thread.sleep(delay);}catch(Exception e){}
                    turnLeft();
                   
                    adjustSensor();
                    exploreOccupiedSpace(this.curPos);
                    updateMap(this.curPos);
                    //if(unknownWithinSensor()) updateMap(this.curPos);
                    
                 try{Thread.sleep(delay);}catch(Exception e){}
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
            
  
             
                      
           //if front not empty, turn right 
            if(!isFrontEmpty(curPos)){  
                   
                try{Thread.sleep(delay);}catch(Exception e){}
                    turnRight();
                    
                    adjustSensor();
                    exploreOccupiedSpace(this.curPos);
                    updateMap(this.curPos);
                    //if(unknownWithinSensor()) updateMap(this.curPos);
                    
                    
                    //System. out.println("@@@@@@@@@@@@@@wall in front, turn right");
                   
                    continue;
             }
            
            
            
            //if front empty && left not empty
            
            if(isFrontEmpty(curPos)&&!isLeftEmpty(curPos)){
                  try{Thread.sleep(delay);}catch(Exception e){}
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
         
         
         
         System.out.println("#############Total count= "+count);
        
         String sensorData1;
         
         if(curPos[0]!=1||curPos[1]!=1){ 
             //sensorData1=client.read();
             System.out.println("$$$$$$$$$$received from sensor: ");
         
              System.out.println("X|");
         
             dfsToStart(curPos);
            //sensorData1=client.read();
            System.out.println("$$$$$$$$$$received from sensor: ");
         
             for(int m=0;m<20;m++){
                                        for(int n=0;n<15;n++){
                                            if(curMap[m][n]==1){
                                                curMap[m][n]=2;
                                            }
                                        }

             }
         }
         
         while(!canAlignLeft()||!canAlignFront()){
             turnRight();
             //sensorData1=client.read();
             System.out.println("$$$$$$$$$$received from sensor: ");
            
         }
         
         turnLeft();
         //sensorData1=client.read();
         System.out.println("$$$$$$$$$$received from sensor: ");
         
        
         
         doAlignment();
         
         turnRight();
         //sensorData1=client.read();
         System.out.println("$$$$$$$$$$received from sensor: ");
         
         
         
         doAlignment();
         
         goToGoal();
     
     }
     
     
  //***********************************************************************************    
     public boolean isLeftWall(int[] pos){
         if(d[0]==0&&d[1]==1&&pos[0]==18) return true;
         else if(d[0]==-1&&d[1]==0&&pos[1]==13) return true;
         else if(d[0]==0&&d[1]==-1&&pos[0]==1) return true;
         else if(d[0]==1&&d[1]==0&&pos[1]==1) return true;
         return false;
     }
    
 //****************************Shortest Path**********************************************  
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
         System.out.println("R|"+mySP.finalPath);
         //m.paintSPath2(sp);
        
         goBack(sp);
     }
     
      public void goToGoal(){
             
         ShortestPath mySP = new ShortestPath(new int[]{1,1},new int[]{18,13} ,curMap);
         ArrayList sp2=mySP.searchShortestPath();
         if(sp2.size()>1){
             int[] t2=(int[])sp2.get(1);
             int[] correctDirec=new int[]{t2[0]-curPos[0],t2[1]-curPos[1]};
             while(d[0]!=correctDirec[0]||d[1]!=correctDirec[1]) {turnRight();}
             
                 //printShortestPath(sp2);
              System.out.println("command sent to robot: "+mySP.finalPath);
             // client.write(mySP.finalSteps);
              try{Thread.sleep(100);}catch(Exception e){}
              System.out.println("P");
              try{Thread.sleep(100);}catch(Exception e){}
             System.out.println(mySP.finalPath);
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
              System.out.println("P");
            
               try{Thread.sleep(100);}catch(Exception e){}
               System.out.println("X|");
             adjustSensor();
             exploreOccupiedSpace(curPos);
             updateMap(this.curPos);
             
             
             //newExplorationToGoal();
         }
         
         else return;
         
     }
      
     public void goBack(ArrayList a){
            
            int[] oP=new int[2];
            int[] nP=new int[2];
            
            for(int i=0;i<a.size()-1;i++){
                oP=(int[])a.get(i);
                nP=(int[])a.get(i+1);
              try{Thread.sleep(delay);}catch(Exception e){}
                d[0]=nP[0]-oP[0];
                d[1]=nP[1]-oP[1];
                m.updateRobPos2(oP[0],oP[1],nP[0],nP[1],d[0],d[1]);
                curPos[0]=nP[0];
                curPos[1]=nP[1];
                
                
            }
           
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
        // client.write("W|");
         
         System.out.println("W|");
     }
     
    
     
     public void turnLeft(){
         
         if(d[0]==0&&d[1]==-1){d[0]=-1;d[1]=0;}
         else if(d[0]==-1&&d[1]==0){d[0]=0;d[1]=1;}
         else if(d[0]==0&&d[1]==1){d[0]=1;d[1]=0;}
         else {d[0]=0;d[1]=-1;}
          m.updateRobPos2(curPos[0],curPos[1],curPos[0],curPos[1],d[0],d[1]);
         //robot command
         //client.write("A|");
         System.out.println("A|");
     
     }
     
     public void turnRight(){
         if(d[0]==0&&d[1]==-1){d[0]=1;d[1]=0;}
         else if(d[0]==1&&d[1]==0){d[0]=0;d[1]=1;}
         else if(d[0]==0&&d[1]==1){d[0]=-1;d[1]=0;}
         else {d[0]=0;d[1]=-1;}
          m.updateRobPos2(curPos[0],curPos[1],curPos[0],curPos[1],d[0],d[1]);
         //robot command
        //client.write("D|");
         System.out.println("D|");
     }
     
     
     public void doAlignment(){
     
         //robot command
         //client.write("Q|");
         System.out.println("Q|");
     
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

                sensor2.add(new int[]{2,-1});
                sensor2.add(new int[]{3,-1});

                sensor3.add(new int[]{2,0});
                //sensor3.add(new int[]{3,0});

                sensor4.add(new int[]{2,1});
                sensor4.add(new int[]{3,1});

                sensor6.add(new int[]{-1,2});
                sensor6.add(new int[]{-1,3});
                sensor5.add(new int[]{-1,4});
                sensor5.add(new int[]{-1,5});
                sensor5.add(new int[]{-1,6});
                //sensor5.add(new int[]{-1,7});
                //sensor5.add(new int[]{-1,8});
        
        
        }
        
        
        else if(d[0]==0&&d[1]==-1){ //W
                
                sensor1.add(new int[]{-2,-1}); 
                sensor1.add(new int[]{-3,-1}); 

                sensor2.add(new int[]{-1,-2});
                sensor2.add(new int[]{-1,-3});

                sensor3.add(new int[]{0,-2});
                //sensor3.add(new int[]{0,-3});

                sensor4.add(new int[]{1,-2});
                sensor4.add(new int[]{1,-3});

                sensor6.add(new int[]{2,1});
                sensor6.add(new int[]{3,1});
                sensor5.add(new int[]{4,1});
                sensor5.add(new int[]{5,1});
                sensor5.add(new int[]{6,1});
                //sensor5.add(new int[]{7,1});
                //sensor5.add(new int[]{8,1});
        
        }
        
        
        else if(d[0]==-1&&d[1]==0){  //S
                sensor1.add(new int[]{-1,2}); 
                sensor1.add(new int[]{-1,3}); 

                sensor2.add(new int[]{-2,1});
                sensor2.add(new int[]{-3,1});

                sensor3.add(new int[]{-2,0});
                //sensor3.add(new int[]{-3,0});

                sensor4.add(new int[]{-2,-1});
                sensor4.add(new int[]{-3,-1});

                sensor6.add(new int[]{1,-2});
                sensor6.add(new int[]{1,-3});
                sensor5.add(new int[]{1,-4});
                sensor5.add(new int[]{1,-5});
                sensor5.add(new int[]{1,-6});
                //sensor5.add(new int[]{1,-7});
                //sensor5.add(new int[]{1,-8});
        }
        
        else{   //E
                sensor1.add(new int[]{2,1}); 
                sensor1.add(new int[]{3,1}); 

                sensor2.add(new int[]{1,2});
                sensor2.add(new int[]{1,3});

                sensor3.add(new int[]{0,2});
                //sensor3.add(new int[]{0,3});

                sensor4.add(new int[]{-1,2});
                sensor4.add(new int[]{-1,3});

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
         System.out.println("SENSING........");
         //String sensorData=client.read();
         //System.out.println("$$$$$$$$$$received from sensor: "+sensorData);
         
        // interpretSensor(sensorData);
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
        



 
   //************************************************************************** 
  
 public void dfsToStart(int[] pos){
       // int[] cPos=new int[2];  //next position
      
        
        if(pos[0]==1&&pos[1]==1) backToStart=true;
          
        
        
        
        
        
        while(!backToStart){
            
             t2=System.currentTimeMillis(); 
             if(t2-t1>=time_limit){System.out.println("Exceeed time limit "+(t2-t1)+"ms");break;}
            
             adjustSensor();
             exploreOccupiedSpaceDFS(pos);
             updateMap(this.curPos);  
             
             
             
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

        //WSNE        
            for(int i=0;i<4;i++){
                int[] w=(int[]) direc2.get(i);

                if((pos[1]<=1&&i==0)||(pos[0]>=18&&i==2)||(pos[1]>=13&&i==3)||(pos[0]<=1&&i==1)) continue;   
                // don't go into wall direction
                if(curMap[pos[0]+w[0]][pos[1]+w[1]]==1||curMap[pos[0]+w[0]][pos[1]+w[1]]==0) continue;
                
                //if visited or unknown, don't go there
                if(this.eOM.isMovable(pos[0]+w[0],pos[1]+w[1])==false) continue;
                //If obstacle or obstacle boundary, don't go there
                
                path2.add(new int[]{pos[0],pos[1]});  
                int[] oldPos=new int[]{pos[0],pos[1]};  
                
                generateCommandDFS(d[0],d[1],w[0],w[1]);
             
                
            try{Thread.sleep(delay);}catch(Exception e){}
                
                
                m.updateRobPos2(oldPos[0],oldPos[1],pos[0],pos[1],d[0],d[1]);
     
      
                dfsToStart(pos);      
                if(out2==true) return;


            }//end of for loop
           
            if(out2==false){
                
                
                int[] temp=new int[2];
                temp[0]=pos[0];
                temp[1]=pos[1];
                        
             try{Thread.sleep(delay);}catch(Exception e){}
                if(path2.size()==0){System.out.println("Error"); System.exit(0);}
                int[] tpos=(int[])path2.remove(path2.size()-1);
                generateCommandDFS(d[0],d[1],tpos[0]-pos[0],tpos[1]-pos[1]);
            
                m.updateRobPos2(temp[0],temp[1],pos[0],pos[1],d[0],d[1]);
                        
            }
        }//end of while loop
 
        out2=true;
        curPos=pos;
        //System.out.println("count "+count);
   
   }
 
 
   //************************************************************************** 
 
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

        //WNES        
            for(int i=0;i<4;i++){
                int[] w=(int[]) direc.get(i);

                if((pos[1]<=1&&i==0)||(pos[0]>=18&&i==1)||(pos[1]>=13&&i==2)||(pos[0]<=1&&i==3)) continue;   
                // don't go into wall direction
                if(curMap[pos[0]+w[0]][pos[1]+w[1]]==1||curMap[pos[0]+w[0]][pos[1]+w[1]]==0) continue;
                
                //if visited or unknown, don't go there
                if(this.eOM.isMovable(pos[0]+w[0],pos[1]+w[1])==false) continue;
                //If obstacle or obstacle boundary, don't go there
                
                
                path.add(new int[]{pos[0],pos[1]});  
                int[] oldPos=new int[]{pos[0],pos[1]};
                
                //System.out.println("dx="+d[0]+" dy="+d[1]+"  wx="+w[0]+" wy="+w[1]);  
                generateCommandDFS(d[0],d[1],w[0],w[1]);
       
            try{Thread.sleep(delay);}catch(Exception e){}
                
              
               m.updateRobPos2(oldPos[0],oldPos[1],pos[0],pos[1],d[0],d[1]);
               
               
                dfs(pos);      
                if(out==true) return;


            }//end of for loop
           
            if(out==false){
                
                
                int[] temp=new int[2];
                temp[0]=pos[0];
                temp[1]=pos[1];
                        
             try{Thread.sleep(delay);}catch(Exception e){}
                if(path.size()==0){System.out.println("Error"); System.exit(0);}
                int[] tpos=(int[])path.remove(path.size()-1);
                generateCommandDFS(d[0],d[1],tpos[0]-pos[0],tpos[1]-pos[1]);
              
                m.updateRobPos2(temp[0],temp[1],pos[0],pos[1],d[0],d[1]);
                        
            }
        }//end of while loop
 
        out=true;
        curPos=pos;
        //System.out.println("count "+count);
   
   }
  //************************************************************************** 
   
      public void generateCommandDFS(int dx,int dy,int wx,int wy){

        if(dx==1&&dy==0){
            if(wx==1&&wy==0){ 
               // System.out.println("W|");
                    moveForward();
     
            }
            else if(wx==0&&wy==1)  {
               // System.out.println("D|");
                 turnRight();
                
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                // System.out.println("W|");
                 moveForward();
        
            }
            else if(wx==-1&&wy==0) { 
                
                //System.out.println("D");
                 turnRight();
                
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 
                //System.out.println("D|");
                 turnRight();
                 
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
            
                
                //System.out.println("W|");
                 moveForward();
        
            
            }
            else  {
                //System.out.println("A"); 
                 turnLeft();
                
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 //System.out.println("W|"); 
                 moveForward();
            
            
            }  //0 -1
        
        }
        
        else if(dx==0&&dy==1){
            if(wx==1&&wy==0) {
                
               // System.out.println("A|");
                 turnLeft();
                 
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 try{Thread.sleep(delay);}catch(Exception e){}
               //  System.out.println("W|");
                 moveForward();
            
            
            }
            else if(wx==0&&wy==1)  {
               // System.out.println("W|");
                moveForward();
          
            }
            else if(wx==-1&&wy==0)  {
               // System.out.println("D|");
                turnRight();
                
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 try{Thread.sleep(delay);}catch(Exception e){}
                // System.out.println("W|");
                 moveForward();
                
            }
            else  {
               // System.out.println("D|");
                turnRight();
               
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 try{Thread.sleep(delay);}catch(Exception e){}
                // System.out.println("D|");
                turnRight();
                
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
            
                 try{Thread.sleep(delay);}catch(Exception e){}
                 //System.out.println("W|");
                 moveForward();
            
            } //0 -1
        
        }
        
        else if(dx==-1&&dy==0){
            if(wx==1&&wy==0){ 
               // System.out.println("A|");
                turnLeft();
                
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 try{Thread.sleep(delay);}catch(Exception e){}
                 // System.out.println("A|");
                 turnLeft();
                
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 try{Thread.sleep(delay);}catch(Exception e){}
                 
                //  System.out.println("W|");
                 moveForward();
              
            }
            else if(wx==0&&wy==1)  {
                
                //System.out.println("A|");
                turnLeft();
                
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 try{Thread.sleep(delay);}catch(Exception e){}
                // System.out.println("W|");
                 moveForward();
        
            
            }
            else if(wx==-1&&wy==0)  {
                //System.out.println("W|");
                 moveForward();
          }
            else  {
               // System.out.println("D|");
                turnRight();
               
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 try{Thread.sleep(delay);}catch(Exception e){}
                // System.out.println("W|");
                moveForward();
           
            
            }  //0 -1
        
        }
        else{     //if(dx==0&&dy==-1)
            if(wx==1&&wy==0) {
               // System.out.println("D|");
                turnRight();
               
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 try{Thread.sleep(delay);}catch(Exception e){}
               //  System.out.println("W|");
                moveForward();
     
            }
            else if(wx==0&&wy==1)  {
                // System.out.println("D|");
                turnRight();
               
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 try{Thread.sleep(delay);}catch(Exception e){}
                // System.out.println("D|");
                turnRight();
                
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 try{Thread.sleep(delay);}catch(Exception e){}
            
                 //System.out.println("W|");
                 moveForward();

            
            }
            else if(wx==-1&&wy==0)  {
                //System.out.println("A|");
                 turnLeft();
                 
                 adjustSensor();
                 exploreOccupiedSpace(this.curPos);
                 updateMap(this.curPos);
                 
                 try{Thread.sleep(delay);}catch(Exception e){}
                 //System.out.println("W|");
                 moveForward();

            }
            else  {
                //System.out.println("W|");
                moveForward();

            } //0 -1
        
        }
   
   
      }
 
 
 
 
}


