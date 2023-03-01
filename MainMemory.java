
package mainmemory;
import java.util.ArrayList;
import java.util.Scanner;
/*
 memory simulator by Asma
*/

public class MainMemory {
    public static int memorySize ;   
    static ArrayList <Partition> MainMem = new ArrayList<>();
    
    public static void main(String[] args) {
        
        //read memory size from the user
        Scanner input = new Scanner(System.in);
        System.out.print(".Enter memory size: ");
        memorySize = input.nextInt();
        //make large parttion as main memory 
        // from 0 to memorySize-1 and status is hole
        MainMem.add(new Partition(memorySize));
        
        
        //display menue
        menue();
        while(true){
        
        //raed command from user & split them
        Scanner in = new Scanner(System.in);
        System.out.print("Enter your command: ");
        String command = in.nextLine();
        String [] Comm = command.split(" ");
        
        if(Comm[0].equalsIgnoreCase("RQ")){//-----Request-----
                int indexFit=-1 ;
                String processName =  Comm[1];
                int ByteSize = Integer.parseInt(Comm[2]);
                String Strategy = Comm[3];
                //make new object 
                Partition newObj = new Partition(ByteSize, processName);
                //analysing fit
                if(Strategy.equalsIgnoreCase("B")){
                    //if Best Fit
                    indexFit = insertByBestFit( newObj);
                }else if(Strategy.equalsIgnoreCase("W")){
                    //if Worst Fit
                    indexFit =  insertByWorstFit(newObj);
                }else if(Strategy.equalsIgnoreCase("F")) {
                       //if First Fit
                      indexFit = insertByFirstFit(newObj);            
                 } 
                if(indexFit != -1 ){
                    //Allocate
                    System.out.println("Succeded allocation");
                    //get the aporprate Fit object, take size, firstAdd
                    Partition tempPart = MainMem.get(indexFit);
                    int remainSize = tempPart.getSizeOfPartition();
                    int firstAdd = tempPart.getFirstAddress();
                    //set First address
                    newObj.setFirstAddress(firstAdd);
                    MainMem.remove(indexFit);
                    MainMem.add(indexFit, newObj);
                    remainSize -=  newObj.getSizeOfPartition();
                    if(remainSize!=0){
                        int newFirstAdd = firstAdd + newObj.getSizeOfPartition();
                        //new partition as hole
                        MainMem.add( indexFit+1 ,new Partition(newFirstAdd, remainSize));
                   }
                }else{
                    //not-Allocate
                    System.out.println("Failed allocation");
                
                }
                
        }else if(Comm[0].equalsIgnoreCase("RL")){//-----Release-----
                String name = Comm[1];
                ReleasePro(name);
        }else if(Comm[0].equalsIgnoreCase("C")){//-----Compaction-----
                compaction();
                
        }else if(Comm[0].equalsIgnoreCase("STATE")){//-----State-----
                reportState();
                
        }else if(Comm[0].equalsIgnoreCase("EXIT")){//-----Exit-----
                System.exit(0); 
                
       }
            
   }
        
 }
    
     public static void menue(){
        System.out.print("**------------------------------------------**\n");
        System.out.print("a. Request for a contiguous block of memory. --RQ--\n" +
                             "b. Release of a contiguous block of memory. --RL--\n" +
                             "c. Compact unused holes of memory into one single block. --C--\n" +
                             "d. Report the regions of free and allocated memory. --STATE--\n" +
                             "e. Exite from program. --EXIT--\n" );
        System.out.print("**------------------------------------------**\n");
     
    }
     public static int insertByFirstFit(Partition obj) {
        int temp =-1;
     
        //loop to find apporporate part to fit process
        for (int i = 0; i < MainMem.size(); i++) {
            if(MainMem.get(i).getNameProces().equalsIgnoreCase("hole") ){
                if(MainMem.get(i).getSizeOfPartition() >= obj.getSizeOfPartition()){
                    temp = i;
                }
            }
        }
            return temp;
       
    }
      public static int insertByBestFit(Partition obj) {
        int temp =-1, minSize = 1000000000;     
        //loop to find apporporate part to fit process
        for (int i = 0; i < MainMem.size(); i++) {
            if(MainMem.get(i).getNameProces().equalsIgnoreCase("hole") ){
                if(  minSize > MainMem.get(i).getSizeOfPartition() //check if the size of partition is smaller than minSize
                     && obj.getSizeOfPartition()<= MainMem.get(i).getSizeOfPartition() ){
                    //reassign the meinmum size-(bistFit)
                    minSize =MainMem.get(i).getSizeOfPartition();
                    temp = i;
                }
            }
        }
            return temp;
       
    }
       public static int insertByWorstFit(Partition obj) {
        int temp =-1, maxSize = 0;     
        //loop to find apporporate BLOCK to fit process
        for (int i = 0; i < MainMem.size(); i++) {
            if(MainMem.get(i).getNameProces().equalsIgnoreCase("hole") ){
                if(  maxSize < MainMem.get(i).getSizeOfPartition() //check if the size of partition is greater than maxSize
                     && obj.getSizeOfPartition()<= MainMem.get(i).getSizeOfPartition() ){
                    //reassign the maximum size-(worstFit)
                    maxSize =MainMem.get(i).getSizeOfPartition();
                    temp = i;
                }
            }
        }
            return temp;
       
    }
    
       public static void ReleasePro(String name) {
           boolean notFound = true;
           //find the process need to release and change name to hole 
           for (int i = 0; i < MainMem.size(); i++) {
                    if(MainMem.get(i).getNameProces().equalsIgnoreCase(name))
                     {MainMem.get(i).setNameProces("hole");
                        notFound=false;}
           }
           if(notFound){
               System.out.println("The cannot be release, isn't found.");
           }
           
           //compact adjacjent holes
           for (int i = 0; i < MainMem.size()-1 ; ++i) {
               if(MainMem.get(i).getNameProces().equals("hole") &&
                   MainMem.get(i+1).getNameProces().equals("hole") ){
                   //get sum of tow adjacen parttion holes
                   int sum = MainMem.get(i).getSizeOfPartition()+MainMem.get(i+1).getSizeOfPartition();
                   //set as size for the first adjacent object
                   MainMem.get(i).setSizeOfPartition(sum);
                   //remove the second adjacent object
                   MainMem.remove(i+1);
                   i--;
              }
          }
       
    }
     public static void compaction(){
         int sumSizeOfHoles = 0, temp = 0;
         boolean findHole = true;

         //-- sum all sizes hole | else add process to temp ArrayList--
         for (int i = 0; i < MainMem.size(); i++) {
             if(MainMem.get(i).getNameProces().equals("hole")){
                 sumSizeOfHoles += MainMem.get(i).getSizeOfPartition();
                 findHole=false;
             }
         }
         
         //Not Holes in Main Memory 
         if(findHole){
             System.out.println("There are no holes");
         }
         
         //delete hole object in main Memory
         for (int i = 0; i < MainMem.size(); i++) {
             if(MainMem.get(i).getNameProces().equals("hole"))
                 MainMem.remove(i);
        }
         
         //arrange the addresses
         for (int i = 0; i < MainMem.size(); i++) {
             if( !(temp == MainMem.get(i).getSizeOfPartition()) ){
                 MainMem.get(i).setFirstAddress(temp);
             }
             temp = MainMem.get(i).getFirstAddress() + MainMem.get(i).getSizeOfPartition();
         }
         //finaly set new partition as big hole in the end
         MainMem.add(new Partition(temp, sumSizeOfHoles));
       
     }
       

     public static void reportState() {
         for (int i = 0; i < MainMem.size(); i++) {
             int FirstAdd = MainMem.get(i).getFirstAddress();
             int LastAdd =  FirstAdd + MainMem.get(i).getSizeOfPartition()-1;
             String ProcessName = MainMem.get(i).getNameProces();
             if(ProcessName.equals("hole"))
                 System.out.println("Addresses[" + FirstAdd + ":" + LastAdd + "] " + " Unused");
             else
                System.out.println("Addresses[" + FirstAdd + ":" + LastAdd + "] " + " Process " + ProcessName);
      
         }
        
    }
     
     
}

