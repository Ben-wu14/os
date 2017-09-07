package process;

public class PCB {
    private int id;//进程编号
    private int[] registers;//寄存器的列表，顺序为ax,psw,pc
    private State state;//进程状态，有RUNNING,BLOCK,READY
    private BlockReason blockReason;//阻塞原因，有TIMEOUT,MEMFULL,DEVICEBUZY,NONE
    private int startPointer;//内存数据的起始指针
    private int dataSize;//数据的大小
    private static int idGenerate=0;//用于生成id的静态数据

    public PCB() {
		// TODO Auto-generated constructor stub
    	setRegisters(new int[3]);
    	setID();
	}
    public void setID(){
    	//创建时生成ID
    	idGenerate++;
    	this.id=idGenerate;
    }
    
    public int[] getRegisters(){
    	return registers;
    }
    public void setRegisters(int[] registers){
    	this.registers=registers;
    }
    
    public State getState(){
    	return state;
    }
    public void setState(State state){ 	
    	this.state=state;
    }
    
    public BlockReason getBlockReason(){
    	return blockReason;
    }
    public void setBlockReason(BlockReason blockReason){
    	this.blockReason=blockReason;
    }
    
    public int getStartPointer(){
    	return startPointer;
    }
    public void setStartPointer(){
    	this.startPointer=startPointer;
    }
    
    public int getDataSize(){
    	return dataSize;
    }
    public void setDataSize(int dataSize){
    	this.dataSize=dataSize;
    }
    
}
