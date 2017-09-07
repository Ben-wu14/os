package process;

import java.util.ArrayList;
import java.util.BitSet;

import Exeterns.Exetrans;

public class ProcessManage {
	private static int ax;
	private static BitSet psw;
	private static int pc;
	private static byte ir;
	private static int timeSlice;
	private static PCB runningTask;
	private static ArrayList<PCB> readyTask;
	private static ArrayList<PCB> waitMem;
	private static ArrayList<PCB> waitDeviceA;
	private static ArrayList<PCB> waitDeviceB;
	private static ArrayList<PCB> waitDeviceC;
	public static void inital(){
		ax=0;
		psw=new BitSet(3);
		pc=0;
		timeSlice=6;
		readyTask=new ArrayList<>();
		waitMem=new ArrayList<>();
		waitDeviceA=new ArrayList<>();
		waitDeviceB=new ArrayList<>();
		waitDeviceC=new ArrayList<>();
	}
	//CPUӲ��ģ��
	public static void CPU(){
		//�ж��ж�
		if(psw.isEmpty()){
			//if ���ж�
			int startPointer=runningTask.getStartPointer();
			int dataSize = runningTask.getDataSize();
			//�����������
			ir=MemoryManage.readData(startPointer, dataSize, pc);
			//����ָ��
			String instruction=Exetrans.byteToString(ir);
			int i;
			//pc+1
			pc++;
			//ִ��ָ��
			if(instruction.indexOf("x=")!=-1){
				  i=Integer.parseInt(instruction.substring(2));
				//  System.out.println(i);
				  String s1=Integer.toBinaryString(i);
				  if(s1.length()==1){
					  s1="000"+s1;
				  }
				  else if(s1.length()==2){
					  s1="00"+s1;
				  }
				  else if(s1.length()==3){
					  s1="0"+s1;
				  }
				  instruction="0000"+s1;  
		     }
			//x++  ����������Ϊ0001 0000
			 if(instruction.indexOf("x++")!=-1){
				instruction="00010000";
			}
			
		    //x--  ����������Ϊ0010 0000
		 if(instruction.indexOf("x--")!=-1){
				instruction="00100000";	
			}			
		  //!?? ʹ��ĳ�豸��ʱ��
			 if(instruction.indexOf("!")!=-1){
				if(instruction.indexOf("A")!=-1){
					i=Integer.parseInt(instruction.substring(2));
					  String s2=Integer.toBinaryString(i);
					  if(s2.length()<2){
						  s2="0"+s2;
					  }
					  instruction="001100"+s2;
				}
				else if(instruction.indexOf("B")!=-1){
					i=Integer.parseInt(instruction.substring(2));
					  String s2=Integer.toBinaryString(i);
					  if(s2.length()<2){
						  s2="0"+s2;
					  }
					  instruction="001101"+s2;
				}
				else{ 
					i=Integer.parseInt(instruction.substring(2));
					  String s2=Integer.toBinaryString(i);
					  if(s2.length()<2){
						  s2="0"+s2;
					  }
					  instruction="001110"+s2;
				}	 
			}
		  //end�����ʾ��ִ���ļ�����������������Ϊ010000000
		  if(instruction.equals("end")==true){
				  instruction="01000000"; 
			}
			//---------------------------------------
		}else{
			//else �����ж�
			//�ж��ж�����
			if(psw.get(0)){
				//�������
					//1.��ʾx
					//2.��������
					//3.���̵���
			}else if(psw.get(1)){
				//ʱ��Ƭ����
					//1.����cpu�ֳ�
					//2.���̵���
			}else{
				//I/O����
					//1.�������I/O�Ľ���
					//2.�������������е�һ������
			}
		}
		
		

	}
	//�Զ�������ɽ���
	public static void generateTask(){
		//ʹ�ö��߳�
		//�������ʱ�䳤��
		//�������ʱ�䣬���ѡ��exe�ļ������ɽ���
	}
	//ʱ��Ƭ��ʱ���Ƚ���
	public static void dispatchTask(boolean isCanceled){
		//if ����δ����
		//�����ֳ���PCB���ѽ��̷Żؾ�������
		//else ���豣��
		
		//if �������в�Ϊ��
		//�Ӿ�������ȡ��һ������
		//else �����й����
		//�ָ�CPU�ֳ�
	}
	//�ĸ�ԭ��
	//�������̣����Ҵ����Ӧ����
	public static void create(byte[] exeFile){
		//����հ�PCB
		//�����ڴ�
		//if �ɹ�
			//�����ڴ�
			//��ʼ��PCB
			//�����������
		//else
			//��ʼ��
			//�����ڴ���������
	}
	//��������
	public static void destroy(PCB task){
		//�����ڴ�
		//����PCB
	}
	//��������
	public static void block(PCB pcb,BlockReason reason){
		//����CPU�ֳ�
		//�޸Ľ���״̬
		//�ж�����ԭ��
		//PCB������������
	}
	//���ѽ���
	public static void awake(PCB pcb){
		//�ж�����ԭ��Ѱ�Ҷ�Ӧ��������
		//�޸Ľ���״̬
		//�ѽ��̴�����������������������
	}
}
