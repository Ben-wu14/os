package process;

import java.util.ArrayList;
import java.util.BitSet;

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
			//����������ݣ�����ָ��
			
			//pc+1
			pc++;
			//ִ��ָ��
		}
		
		//else �����ж�
		//�ж��ж�����
			//�������
				//1.��ʾx
				//2.��������
				//3.���̵���
			//ʱ��Ƭ����
				//1.����cpu�ֳ�
				//2.���̵���
			//I/O����
				//1.�������I/O�Ľ���
				//2.�������������е�һ������
		//ִ���ж�

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
