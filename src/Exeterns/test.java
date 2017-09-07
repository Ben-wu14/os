package Exeterns;
import java.io.*;




public class test {

	public static void main(String[] args) {
		
		Exetrans tempExetrans=new Exetrans();
		try {
			//txt到byte数组
			byte[] comand=tempExetrans.txtToByte("可执行文件8.txt");
			for (byte b : comand) {
				System.out.println(b);
			}
			//byte数组到指令数组
			String[] result = tempExetrans.byteToString(comand);
			for (String string : result) {
				System.out.println(string);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
