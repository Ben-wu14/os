package Exeterns;
import java.io.*;




public class test {

	public static void main(String[] args) {
		
		Exetrans tempExetrans=new Exetrans();
		try {
			//txt��byte����
			byte[] comand=tempExetrans.txtToByte("��ִ���ļ�8.txt");
			for (byte b : comand) {
				System.out.println(b);
			}
			//byte���鵽ָ������
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
