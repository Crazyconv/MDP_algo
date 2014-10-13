import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.ArrayList;
 
 
public class Map

{  
       
    int length;
    int width;
    int[][] occupancy;
    WinGrid g;
    
    Map(int[][] occupancy,int[] strP){
        
        this.length=occupancy.length;
        this.width=occupancy[0].length;
        this.occupancy=occupancy;
        
        //initialize current map
        int [][] curMap=new int[length][width];
        for(int l=0;l<length;l++){
            for (int k=0;k<width;k++){
                curMap[l][k]=0;
            }
        }
        
        
        
        g= new WinGrid (curMap,strP, occupancy);
        g.updateCell(curMap, strP[0], strP[1], 1);  //start point
        g.updateCell(curMap, strP[0]+1, strP[1], 2);
        g.updateCell(curMap, strP[0], strP[1]+1, 2);
        g.updateCell(curMap, strP[0]+1, strP[1]+1, 2);
        
        //Goal Area
        for(int i=17;i<20;i++){
            for(int j=12;j<15;j++){
                g.updateCell(curMap,i,j,5);
            }
        }
        
        
       //try{Thread.sleep(1000);}catch(Exception e){}
       //g.updateCell(occupancy,a,b,c);
    
    }
   
    
 //#################################################################################     
    public void updateCell(int[][] curMap, int a, int b, int c){
        this.g.updateCell(curMap,a,b,c);
        
    }  
    
 //#################################################################################    
    public void updateRobPos(int ox, int oy, int nx, int ny){
       
        this.g.updateRobPos(ox,oy,nx,ny);
    }
    
  //#################################################################################     
   public void paintSPath1(ArrayList sp){
        this.g.paintSPath1(sp);
       
    
   }
   
   //#################################################################################     
   public void paintSPath2(ArrayList sp){
        this.g.paintSPath2(sp);
       
    
   }
   
   //#################################################################################     
   public void printAlign(ArrayList a){
        this.g.printAlign(a);
       
    
   }
   
    
   
    
//################################################################################# 
    public void printMap(int[][] map){
        
        for(int i=0;i<map.length;i++){
            System.out.print("{");
            for(int j=0;j<map[0].length;j++){
                System.out.print(map[i][j]+",");
            }
        
             System.out.println("},");
        }
       
    
    }
       

}







//################################################################################# 
// 0 for unexplored; 1 for visited; 2 for empty space but not visited ;
//3 for obstacle;4 for robot; 5 for goal
class WinGrid extends JFrame
{
    private static final long serialVersionUID = 1L;
    GridLayout grid;
    JPanel chessboard1;
    JPanel chessboard2;
    Label[][] cells1;
    Label[][] cells2;
    int length;
    int width;
    
    WinGrid (int[][] curMap, int[] strP, int[][] occupancy)
    {   
        length=curMap.length;
        width=curMap[0].length;
        
        chessboard1 = new JPanel ();
        chessboard1.setPreferredSize(new Dimension(600, 800));
        
        chessboard2 = new JPanel ();
        chessboard2.setPreferredSize(new Dimension(600, 800));
        
        
        grid = new GridLayout (length,width,1,1);
        chessboard1.setLayout (grid);
        chessboard2.setLayout (grid);
        cells1 = new Label[length][width];
        cells2 = new Label[length][width];
        
        //PAINT PANEL1--known to robot
        for ( int i = 0; i < cells1.length; i++ )
        {
            for ( int j = 0; j < cells1[i].length; j++ )
            {
                cells1[i][j] = new Label ();
                //cells[i][j].addActionListener(this);
                
                switch(curMap[length-1-i][j]){
                    case 0: //unexplored
                        cells1[i][j].setBackground (Color.red);
                        break;
                        
                    case 1: //empty
                        cells1[i][j].setBackground (Color.yellow);
                        break;
                           
                        
                     case 2:  //empty and visited
                        cells1[i][j].setBackground (Color.orange);
                        break;    
                      
                    
                        
                    case 3: //obstacle
                        cells1[i][j].setBackground (Color.black);
                        break;
                         
                         
                    case 5: //goal
                        cells1[i][j].setBackground (Color.green);
                        break;
                    default:
                        System.out.println("Error1 "+curMap[length-1-i][j]);
                
                }
                //cells[i][j].setBorderPainted(false);
                
                chessboard1.add (cells1[i][j]);
            }
        }
        
        for(int i=-1;i<2;i++){
            for(int j=-1;j<2;j++){
            cells1[length-1-strP[0]-i][strP[1]+j].setBackground (Color.magenta);
            }

         }
       
       
        
        //PAINT PANEL2--fully known occupancy map
        for ( int i = 0; i < cells2.length; i++ )
        {
            for ( int j = 0; j < cells2[i].length; j++ )
            {
                cells2[i][j] = new Label ();
                //cells[i][j].addActionListener(this);
                
                switch(occupancy[length-1-i][j]){
                    
                    case 0: //unexplored
                        cells2[i][j].setBackground (Color.red);
                        break;
                    
                    case 1:  //empty and visited
                        cells2[i][j].setBackground (Color.orange);
                        break;
                        
                    case 2: //empty
                        cells2[i][j].setBackground (Color.yellow);
                        break;
                  
                        
                    case 3: //obstacle
                        cells2[i][j].setBackground (Color.black);
                        break;
                        
                    case 5: //goal
                        cells2[i][j].setBackground (Color.green);    
                        break;
                        
                    default:
                        System.out.println("Error2 "+occupancy[length-1-i][j]);
                
                }
                //cells[i][j].setBorderPainted(false);
                
                chessboard2.add (cells2[i][j]);
            }
        }
        
        
        add (chessboard1, BorderLayout.WEST);
        add (chessboard2, BorderLayout.EAST);
        setBounds (10, 10, 1250, 800 );
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setVisible (true);
    }
    
    
  
 // 0 for unexplored; 1 for visited; 2 for empty space but not visited ;
//3 for obstacle;4 for robot; 5 for goal
    public void updateCell(int[][] curMap, int a, int b, int c){
        
        
            curMap[a][b]=c;
        
            switch(c){
                        case 0: //unexplored
                            cells1[length-1-a][b].setBackground (Color.red);
                            break;

                        case 1: //visited(empty)
                            cells1[length-1-a][b].setBackground (Color.orange);
                            break;

                        case 2: //empty
                            cells1[length-1-a][b].setBackground (Color.yellow);
                            break;

                        case 3: //obstacle
                            cells1[length-1-a][b].setBackground (Color.black);
                            break;
                        
                        case 5://goal
                            cells1[length-1-a][b].setBackground (Color.green);
                            break;
                        default:
                            System.out.println("Error3 "+c);

             }
       }
        
          
      
                
            
        
    
    
    public void updateRobPos(int ox, int oy,int nx, int ny){
        //draw a dot to represent robot
        //Graphics gp=chessboard.getGraphics();
        for(int i=-1;i<2;i++){
            for(int j=-1;j<2;j++){
            cells1[length-1-ox-i][oy+j].setBackground (Color.yellow);
            }

        }
        
         for(int i=-1;i<2;i++){
            for(int j=-1;j<2;j++){
            cells1[length-1-nx-i][ny+j].setBackground (Color.MAGENTA);
            }

         }
       
        
    }
    
     public void paintSPath1(ArrayList sp){
         
        for(int i=0;i<sp.size();i++){
             try{Thread.sleep(50);}catch(Exception e){}
                int[] w=(int[]) sp.get(i);
                //System.out.print(" ("+w[0]+","+w[1]+") ");
                cells1[length-1-w[0]][w[1]].setBackground (Color.blue);
        } 
        
       
    
    }
     
     
     
    public void paintSPath2(ArrayList sp){
         
        for(int i=0;i<sp.size();i++){
            try{Thread.sleep(50);}catch(Exception e){}
                int[] w=(int[]) sp.get(i);
                //System.out.print(" ("+w[0]+","+w[1]+") ");
                cells2[length-1-w[0]][w[1]].setBackground (Color.blue);
        } 
        
       
    
    }
    
    
    public void printAlign(ArrayList a){
         
        for(int i=0;i<a.size();i++){
             try{Thread.sleep(50);}catch(Exception e){}
                int[] w=(int[]) a.get(i);
                //System.out.print(" ("+w[0]+","+w[1]+") ");
                cells1[length-1-w[0]][w[1]].setBackground (Color.orange);
        } 
        
       
    
    }
    
        
    
}
    
    


