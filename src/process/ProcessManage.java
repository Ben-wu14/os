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
	//CPU硬件模拟
	public static void CPU(){
		//判断中断
		if(psw.isEmpty()){
			//if 无中断
			int startPointer=runningTask.getStartPointer();
			int dataSize = runningTask.getDataSize();
			//获得命令数据
			ir=MemoryManage.readData(startPointer, dataSize, pc);
			//生成指令
			String instruction=Exetrans.byteToString(ir);
			int i;
			//pc+1
			pc++;
			//执行指令
			if(instruction.indexOf("x=")!=-1){
				  i=Integer.parseInt(instruction.substring(2));
				  ax=i;
		     }
			//x++  二进制命令为0001 0000
			 if(instruction.indexOf("x++")!=-1){
				ax++;
			}
		    //x--  二进制命令为0010 0000
			 if(instruction.indexOf("x--")!=-1){
				ax--;
			}			
		  //!?? 使用某设备的时间
			 if(instruction.indexOf("!")!=-1){
				 boolean suc;
				if(instruction.indexOf("A")!=-1){
					i=Integer.parseInt(instruction.substring(2));
					suc=DeviceManage.request(DeviceType.A, runningTask, i);
					if(!suc){
						//如果调配设备失败
						psw.set(2);
						block(runningTask, BlockReason.DEVICE_BUZY);
					}
				}
				else if(instruction.indexOf("B")!=-1){
					i=Integer.parseInt(instruction.substring(2));
					suc=DeviceManage.request(DeviceType.B, runningTask, i);
				}
				else{ 
					i=Integer.parseInt(instruction.substring(2));
					suc=DeviceManage.request(DeviceType.C, runningTask, i);
				}	 
				if(!suc){
					//如果调配设备失败
					psw.set(2);
				}
			}
		  //end命令表示可执行文件结束
		  if(instruction.equals("end")==true){
			  	//显示结果------------------
			  	//清除内存
			  	MemoryManage.removePCB(runningTask);
			  	//设置psw
			  	psw.set(0);
			}
			//---------------------------------------
		}else{
			//else 处理中断
			//判断中断类型
			if(psw.get(0)){
				//程序结束
					//1.显示x-------------------
					//2.撤销进程
					destroy(runningTask);
					//3.进程调度
					dispatchTask(true);
			}else if(psw.get(1)){
				//时间片结束
					//1.保存cpu现场
					
					//2.进程调度
			}else{
				//I/O结束
					//1.唤醒完成I/O的进程
					//2.唤醒阻塞队列中的一个进程
			}
		}
		
		

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
	//时钟
	public static void timer(){
		
	}
	//保存当前状态
	public static void  savePCB() {
		runningTask.setAx(ax);
		runningTask.setPc(pc);
		runningTask.setPsw(psw);
		//未完成
	}
}
