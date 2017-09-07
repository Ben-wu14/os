package process;

import java.util.BitSet;

public class PCB {
    private int id;//进程编号
    private int ax;
    private int pc;
    private BitSet psw;
    private State state;//进程状态，有RUNNING,BLOCK,READY
    private BlockReason blockReason;//阻塞原因，有TIMEOUT,MEMFULL,DEVICEBUZY,NONE
    private int startPointer;//内存数据的起始指针
    private int dataSize;//数据的大小
    private static int idGenerate=0;//用于生成id的静态数据

    public PCB() {
		// TODO Auto-generated constructor stub
    	ax=0;
    	pc=0;
    	psw=new BitSet(3);
    	setID();
	}
    public void setID(){
    	//创建时生成ID
    	idGenerate++;
    	this.id=idGenerate;
    }
	public int getId() {
		return id;
	}
	public int getAx() {
		return ax;
	}
	public void setAx(int ax) {
		this.ax = ax;
	}
	public int getPc() {
		return pc;
	}
	public void setPc(int pc) {
		this.pc = pc;
	}
	public BitSet getPsw() {
		return psw;
	}
	public void setPsw(BitSet psw) {
		this.psw = psw;
	}
	public void setId(int id) {
		this.id = id;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public BlockReason getBlockReason() {
		return blockReason;
	}
	public void setBlockReason(BlockReason blockReason) {
		this.blockReason = blockReason;
	}
	public int getStartPointer() {
		return startPointer;
	}
	public void setStartPointer(int startPointer) {
		this.startPointer = startPointer;
	}
	public int getDataSize() {
		return dataSize;
	}
	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}
    
   
    
}
