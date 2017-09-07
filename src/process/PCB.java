package process;

public class PCB {
    private int id;//���̱��
    private int[] registers;//�Ĵ������б�˳��Ϊax,psw,pc
    private State state;//����״̬����RUNNING,BLOCK,READY
    private BlockReason blockReason;//����ԭ����TIMEOUT,MEMFULL,DEVICEBUZY,NONE
    private int startPointer;//�ڴ����ݵ���ʼָ��
    private int dataSize;//���ݵĴ�С
    private static int idGenerate=0;//��������id�ľ�̬����

    public PCB() {
		// TODO Auto-generated constructor stub
    	setRegisters(new int[3]);
    	setID();
	}
    public void setID(){
    	//����ʱ����ID
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
