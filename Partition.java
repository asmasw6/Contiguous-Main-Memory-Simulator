
package mainmemory;


public class Partition {
    private int FirstAddress;
    private int SizeOfPartition;
    private String nameProces = "hole"; //is free
    
    //to make as Big memory
    public Partition( int SizeOfPartition){
        this.FirstAddress = 0;
        this.nameProces = nameProces;
        this.SizeOfPartition = SizeOfPartition;
    }
    //to make as new remaining partition - hole
     public Partition(int FirstAddress, int SizeOfPartition  ){
        this.FirstAddress = FirstAddress;
        this.SizeOfPartition = SizeOfPartition;
    }
    
    //to make partition with size - name process
    public Partition(int SizeOfPartition, String nameProces){
        this.FirstAddress = -1;
        this.nameProces = nameProces;
        this.SizeOfPartition = SizeOfPartition;
    }

    public int getFirstAddress() {
        return FirstAddress;
    }

    public int getSizeOfPartition() {
        return SizeOfPartition;
    }

    public String getNameProces() {
        return nameProces;
    }

    public void setFirstAddress(int FirstAddress) {
        this.FirstAddress = FirstAddress;
    }

    public void setSizeOfPartition(int SizeOfPartition) {
        this.SizeOfPartition = SizeOfPartition;
    }
    
    
     public void setNameProces(String nameProces) {
        this.nameProces = nameProces;
    }
   
       
}
