package doc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class DiskManage {
	private static byte[] fat;
	private static ArrayList<Integer> emptySpace;
	private static RandomAccessFile disk;
	public static void inital() {
		// TODO Auto-generated constructor stub
		try {
			disk = new RandomAccessFile("disk.dat", "rw");
			//读取fat表
			disk.seek(0);
			fat=new byte[256];
			for (int i = 0; i < fat.length; i++) {
				fat[i]=disk.readByte();
			}
			//录入空闲块列表
			emptySpace=new ArrayList<>();
			for(int i=0;i<fat.length;i++){
				if(fat[i]==0){
					emptySpace.add(i);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public static byte[] readData(int begin,int numOfBlocks) throws IOException {
		
		
			int[] blocks=iterate(begin);
			if(numOfBlocks!=blocks.length){
				System.out.println("Blocks 实际个数："+blocks.length+" 不等于记录个数："+numOfBlocks);
				return null;
			}
			byte[] data = new byte[blocks.length * 128];
			int t = 0;
			for (int i = 0; i < blocks.length; i++) {
				disk.seek(blocks[i] * 128);
				for (int j = 0; j < 128; j++) {
					data[t] = disk.readByte();
					t++;
				}
			}
			return data;
	}
	public static void freeSpace(int begin,int numOfBlocks) throws IOException {
		
		if ((begin == 0) || (begin) == 1 || (begin) == 2 ) {
			System.out.println("Sorry ,your parameters are error !");
			return;
		} else {
			
			int[] blocks=iterate(begin);
			if(numOfBlocks<blocks.length){
				System.out.println("Blocks 实际个数："+blocks.length+" 多于格式化个数："+numOfBlocks);
				return ;
			}
			byte[] data = new byte[128];
			for (int j = 0; j < 128; j++) {
				data[j] = 0;
			}
			
			for (int i = 0; i < blocks.length; i++) {
				disk.seek(blocks[i] * 128);
				disk.write(data);
				//fat表对应值更新为0
				fat[blocks[i]]=0;
				//加入未用空间列表
				emptySpace.add(blocks[i]);
			}
			//排序
			emptySpace.sort(null);
			//写入fat表
			storeFat();
		}
	}
	public static int writeData(int begin,byte[] data) throws IOException {
	
		if ((begin == 0) || (begin) == 1) {
			System.out.println("Incorrect position to write");
			return 0;
		} else {
			int numOfBlock = (int) Math.ceil(data.length/128.);
			int[] blocks=iterate(begin);
			int t = 0;
			for (int i = 0; i < numOfBlock-1; i++) {
				disk.seek(blocks[i] * 128);
				for (int j = 0; j < 128; j++) {
					disk.writeByte(data[t]);
					t++;
				}
			}
			//写入不足一个磁盘块的数据
			disk.seek(blocks[numOfBlock-1]*128);
			for(int i=0;i<data.length-(numOfBlock-1)*128;i++){
				disk.writeByte(data[t]);
				t++;
			}
			return numOfBlock;
		}
	}
	public static void reAllocateBlock(int begin,int numOfBlock){
		if(numOfBlock<=0){
			System.out.println("Reallocate ERROR");
			return;
		}
		int[] blocks=iterate(begin);
		int oldLength= blocks.length;
		int diff=oldLength-numOfBlock;
		if(diff>0){
			try {
				//清除后面几块的数据
				freeSpace(begin+blocks.length-diff, diff);
				//最后一个磁盘块设为结尾磁盘块
				fat[begin+blocks.length-diff-1]=1;
				storeFat();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			int lastIndex=blocks.length-1;
			int newBegin=allocateBlock(-diff);
			fat[blocks[lastIndex]]=(byte)newBegin;
			storeFat();
		}
	}
	public static int allocateBlock(int numOfBlock){
		int top=0;
		if (numOfBlock >= 253) {
			System.out.println("Sorry , your parameters are error!");
			return 0;
		} else {
			
			if (emptySpace.size() <= numOfBlock) {
				System.out.println("Sorry , I don't have any more space to you!");
				return -1;//如果出错返回-1
			} else{
				//更新fat表
				top=emptySpace.get(0);
				for (int i = 0; i < numOfBlock; i++) {
					fat[emptySpace.get(i)]=emptySpace.get(i+1).byteValue();
				}
				//最后一个磁盘块赋予结束值1
				fat[emptySpace.get(numOfBlock-1).intValue()]=1;
				//更新空余磁盘列表
				for (int i = 0; i < numOfBlock; i++) {
					emptySpace.remove(0);
				}
				//printFAT();
				storeFat();
				return top;
			}
				
		}
	}
	//disk格式化
	public static void clearDisk(){
		try {
			disk = new RandomAccessFile("disk.dat", "rw");
			disk.setLength(0);
		
		for (int i = 0; i < 256; i++) {
			byte[] data = new byte[128];
			for (int j = 0; j < 128; j++) {
				data[j] = 0;
			}
			disk.write(data);
		}

		
		fat = new byte[256];
		for (int i = 0; i < 3; i++) {
			fat[i] = 1;//以1表示文件已经结束
		}
		
		for (int i = 3; i < 256; i++) {
			fat[i] = 0;//以0表示磁盘块为空
		}

		
		disk.seek(0);
		disk.write(fat);
		} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
		emptySpace=new ArrayList<>();
		for(int i=3;i<256;i++){
			emptySpace.add(i);
		}
	}
	public static void close() {
		try {
			disk.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//遍历磁盘块，返回连续的磁盘块队列
	private static int[] iterate(int begin){
		int firstBlock = begin;
		ArrayList<Integer> blocks=new ArrayList<>();
		blocks.add(firstBlock);
		if((int) fat[firstBlock]==0){
			System.out.println("磁盘为空");
		}else{
			int tran = firstBlock;
			while ((int) fat[tran]!=1) {
				blocks.add((int) fat[tran]);
				tran = fat[tran] & 0xff;//byte 转换为  integer,保留低位，高位置零
			}
		}
		
		int[] result= new int[blocks.size()];
		for (int i = 0; i < result.length; i++) {
			result[i]=blocks.get(i);
		}
		/*System.out.println("磁盘序列");
		for (int i : result) {
			System.out.print(" "+i);
		}
		System.out.println();*/
		return result;
	}
	//存储fat表
	private static void storeFat(){
		try {
			disk.seek(0);
			disk.write(fat);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static boolean isEmpty(byte[] data){
		for (int i = 0; i < data.length; i++) {
			if(data[i]!=0){
				return false;
			}
		}
		return true;
	}
	public static void printFAT(){
		System.out.println("Fat:");
		for (byte data : fat) {
			System.out.print(" "+data);
		}
		System.out.println();
	}
	public static byte[] getFat() {
		return fat;
	}
	//计算data[]所用的磁盘个数
	public static int calNumOfBlocks(byte[] data){
		return (int) Math.ceil(data.length/128.);
	}
}
