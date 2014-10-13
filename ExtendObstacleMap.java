/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author axaxfbi
 */
public class ExtendObstacleMap {
    int length=20;
    int width=15;
    int[][] map=new int[length][width];
    
    public ExtendObstacleMap(){
        //initiate map
        for(int i=0;i<length;i++){
            for (int j=0;j<width;j++){
                map[i][j]=0;
            }
        }
        
       
    
    }
    
    public void printEOM(){
         for(int i=0;i<map.length;i++){
            System.out.print("{");
            for(int j=0;j<map[0].length;j++){
                System.out.print(map[i][j]+",");
            }
        
             System.out.println("},");
        }
    }
    
    
    public void transMap(int[][] expMap){   //1,2,5=>0, 3=>3, neibor=>6
        for(int i=0;i<length;i++){
            for(int j=0;j<width;j++){
            
            if(expMap[i][j]==3){
               extendObstacle(i,j);
            
            
            }
            
            }
        }
    
    }
    
    
     public void extendObstacle(int x,int y){
         
        
         this.map[x][y]=3;  //real obstacle
         
         
         //extend obstacle boundary
         if(x-1>=0){ 
             if(this.map[x-1][y]!=3){
                this.map[x-1][y]=6;
             }
             if(y-1>=0){
                 if(this.map[x-1][y-1]!=3){
                    this.map[x-1][y-1]=6;
                 }
             }
             
             if(y+1<=14){
                 if(this.map[x-1][y+1]!=3){
                    this.map[x-1][y+1]=6;
                 }
            }
         }
            
    
         if(x+1<=19){ 
             if(this.map[x+1][y]!=3){
                this.map[x+1][y]=6;
             }
             if(y-1>=0){
                 if(this.map[x+1][y-1]!=3){
                    this.map[x+1][y-1]=6;
                 }
             }
             
             if(y+1<=14){
                 if(this.map[x+1][y+1]!=3){
                    this.map[x+1][y+1]=6;
                 }
            }
         }
         
         if(y+1<=14){
             if(this.map[x][y+1]!=3){
                this.map[x][y+1]=6;
             }
         }
         
         if(y-1>=0){
             if(this.map[x][y-1]!=3){
                this.map[x][y-1]=6;
             }
         }
         
         
         
         
         
     }
     
     
     
     
     
     
     public boolean isMovable(int x,int y){
        // System.out.println("isMovable");
         //System.out.println(x);
         //System.out.println(y);
         return (map[x][y]==0);
     }
     
   
     
     
     
     
     
     
     
}
    
    
    
    
    
    
    
    
   
