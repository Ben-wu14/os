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
	//CPU硬件模拟
	public static void CPU(){
		//判断中断
		if(psw.isEmpty()){
			//if 无中断
			//获得命令数据，生成指令
			
			//pc+1
			pc++;
			//执行指令
		}
		
		//else 处理中断
		//判断中断类型
			//程序结束
				//1.显示x
				//2.撤销进程
				//3.进程调度
			//时间片结束
				//1.保存cpu现场
				//2.进程调度
			//I/O结束
				//1.唤醒完成I/O的进程
				//2.唤醒阻塞队列中的一个进程
		//执行中断

	}
	//自动随机生成进程
	public static void generateTask(){
		//使用多线程
		//生成随机时间长度
		//经过随机时间，随机选择exe文件，生成进程
	}
	//时间片到时调度进程
	public static void dispatchTask(boolean isCanceled){
		//if 进程未撤销
		//保存现场到PCB，把进程放回就绪队列
		//else 无需保存
		
		//if 就绪队列不为空
		//从就绪队列取出一个进程
		//else 调用闲逛进程
		//恢复CPU现场
	}
	//四个原语
	//创建进程，并且存入对应队列
	public static void create(byte[] exeFile){
		//申请空白PCB
		//申请内存
		//if 成功
			//分配内存
			//初始化PCB
			//放入就绪队列
		//else
			//初始化
			//放入内存阻塞队列
	}
	//撤销进程
	public static void destroy(PCB task){
		//回收内存
		//回收PCB
	}
	//进程阻塞
	public static void block(PCB pcb,BlockReason reason){
		//保存CPU现场
		//修改进程状态
		//判断阻塞原因
		//PCB载入阻塞队列
	}
	//唤醒进程
	public static void awake(PCB pcb){
		//判断阻塞原因，寻找对应阻塞队列
		//修改进程状态
		//把进程从阻塞队列中移至就绪队列
	}
}
