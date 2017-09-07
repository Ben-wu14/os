package process;

import java.util.ArrayList;
import java.util.BitSet;

import oracle.jrockit.jfr.events.Bits;

public class MemoryManage {
	public static BitSet bitList=new BitSet(512);
	public static ArrayList<PCB> pcbList;
	public static byte[] memory=new byte[512];
	
	private static int allocateSpace(int dataSize){
		return 0;
	}
	private static void freeSpace(int startPointer,int dataSize){
		
	}
	public static boolean writeData(byte[] data){
		return false;
	}
	public static byte readData(int startPointer,int dataSize,int offset){
		return 0;
	}
	public static PCB createPCB(byte[] data){
		return null;
	}
	public static void removePCB(PCB pcb){
		
	}
	public static boolean isFull(){
		return pcbList.size()<=10;
	}
	public static BitSet getBitList(){
		return bitList;
	}
}
