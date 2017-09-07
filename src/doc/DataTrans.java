package doc;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class DataTrans {

	//把所有二进制数据转换为entry的列表
	protected static ArrayList<Entry> loadEntry(byte[] data,Directory root) {
		ArrayList<Entry> list = new ArrayList<>();
		int length=data.length;
		for (int i = 0; i < length; i+=8) {
			byte[] input=new byte[8];
			input=Arrays.copyOfRange(data, i, i+8);
			if(isTheEnd(input)){
				break;
			}else
			list.add(dataToEntry(input,root));
		}
		return list;
	}
	//把一条8byte的数据转化为entry
	protected static Entry dataToEntry(byte[] data,Directory root) {
		String name= new String(Arrays.copyOfRange(data, 0, 3));
		int startBlock = data[5];
		if(data.length!=8){
			System.out.println("字节截取出错");
			return null;
		}
		
		else{
			Entry entry ;
			if(data[3]==0){
				entry = new DirEntry(root,name,startBlock);
			}else{
				entry = new DocEntry(root,name,startBlock,'t');
				DocEntry docEntry=(DocEntry) entry;
				docEntry.setFileType((char)data[3]);
			}
			
			
			byte variable=data[4];
			entry.setReadOnly(isSet(variable, 0));
			entry.setSystemFile(isSet(variable, 1));
			entry.setNormalFile(isSet(variable, 2));
			entry.setDir(isSet(variable, 3));
			entry.setNumOfBlock(ByteBuffer.wrap(Arrays.copyOfRange(data, 6, 8)).getShort());
			return entry;
		}
	}
	//把entry列表转化成字节数组
	protected static byte[] entryTrans(ArrayList<Entry> entries){
		byte[] data=new byte[128];
		for (int i = 0; i < entries.size(); i++) {
			setByte(data, entryToByte(entries.get(i)), i*8);
		}
		
		return data;
	}
	//把一个entry转化为字节数组
	protected static byte[] entryToByte(Entry entry) {
		byte[] data=new byte[8];
		setByte(data, Arrays.copyOfRange(entry.getName().getBytes(),0,3), 0);
		if(entry instanceof DocEntry){
			DocEntry docEntry= (DocEntry) entry;
			data[3]= (byte) docEntry.getFileType();
		}
		byte variable=0;
		if(entry.isReadOnly()) variable=set(variable, 0);
		if(entry.isSystemFile()) variable=set(variable, 1);
		if(entry.isNormalFile()) variable=set(variable, 2);
		if(entry.isDir()) variable=set(variable, 3);
		data[4]=variable;
		
		data[5]=(byte) entry.getStartBlock();
		setByte(data,ByteBuffer.allocate(2).putShort((short) entry.getNumOfBlock()).array(), 6);
		return data;
	}
	private static byte[] setByte(byte[] destination,byte[] source,int begin) {
		for (int i = 0; i < source.length; i++) {
			destination[i+begin]=source[i];
		}
		return destination;
	}
	//判断字节串是否为全0
	private static boolean isTheEnd(byte[] data){
		for (int i = 0; i < data.length; i++) {
			if(data[i]!=0){
				return false;
			}
		}
		return true;
	}
	// tests if bit is set in value
	private static boolean isSet(byte value, int bit){
	   return (value&(1<<bit))!=0;
	} 

	// returns a byte with the required bit set
	private static byte set(byte value, int bit){
	   return (byte) (value|(1<<bit));
	}
}
