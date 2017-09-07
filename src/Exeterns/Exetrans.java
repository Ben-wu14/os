package Exeterns;
import java.io.*;

public class Exetrans {
    public Exetrans(){
    }
    
    public static byte[] txtToByte(String path) throws Exception{  
    	//����txt���������תΪbyte
    	int i = 0;
    	int length=0; //byte[]�ĳ���
    	int t=0;      //������
		File file=new File(path);
		BufferedReader bk = new BufferedReader(new FileReader(file));
		String s = null;
		while((s=bk.readLine())!=null){
			length++;
		}
		bk.close();
		byte[] b=new byte[length];
		BufferedReader br = new BufferedReader(new FileReader(file));
		while((s = br.readLine())!=null){
			//x=? ��ֵ������������Ϊ0000 xxxx
			 if(s.indexOf("x=")!=-1){
				  i=Integer.parseInt(s.substring(2));
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
				  s="0000"+s1;  
		     }
			//x++  ����������Ϊ0001 0000
			 if(s.indexOf("x++")!=-1){
				s="00010000";
			}
			
		    //x--  ����������Ϊ0010 0000
		 if(s.indexOf("x--")!=-1){
				s="00100000";	
			}			
		  //!?? ʹ��ĳ�豸��ʱ��
			 if(s.indexOf("!")!=-1){
				if(s.indexOf("A")!=-1){
					i=Integer.parseInt(s.substring(2));
					  String s2=Integer.toBinaryString(i);
					  if(s2.length()<2){
						  s2="0"+s2;
					  }
					  s="001100"+s2;
				}
				else if(s.indexOf("B")!=-1){
					i=Integer.parseInt(s.substring(2));
					  String s2=Integer.toBinaryString(i);
					  if(s2.length()<2){
						  s2="0"+s2;
					  }
					  s="001101"+s2;
				}
				else{ 
					i=Integer.parseInt(s.substring(2));
					  String s2=Integer.toBinaryString(i);
					  if(s2.length()<2){
						  s2="0"+s2;
					  }
					  s="001110"+s2;
				}	 
			}
		  //end�����ʾ��ִ���ļ�����������������Ϊ010000000
		  if(s.equals("end")==true){
				  s="01000000"; 
			}
		  b[t]=BitToByte(s);
		  t++;
		}
		br.close();
		return b;
}
    public static byte BitToByte(String s) { 
    	//bitתbyte
	    int a;
	        if (s.charAt(0) == '0') {
	            a=Integer.parseInt(s, 2);  
	        } else { 
	            a=Integer.parseInt(s, 2)-256;  
	        }  
	    return (byte)a;  
	}  

	public static String byteToString(byte b){
		//��������������תΪString
		String s=Integer.toBinaryString((int)b);//ʮ������תΪ������
		String s2;
		if(s.length()==8){
			s2=s;
		}
		else if(s.length()==7){
			s2="0"+s;
		}
		else if(s.length()==6){
			s2="00"+s;
		}
		else if(s.length()==5){
			s2="000"+s;
		}
		else if(s.length()==4){
			s2="0000"+s;
		}
		else if(s.length()==3){
			s2="00000"+s;
		}
		else if(s.length()==2){
			s2="000000"+s;
		}
		else{
			s2="0000000"+s;
		}
    	  
    	  // System.out.println(s2);
    	   if(s2.startsWith("0000")){
    			String s1= Integer.valueOf(s2.substring(4,8),2).toString();
    			  s2="x="+s1;
    			 // System.out.println(s2);
    			 return s2;
    	     }
    		 
    		// ����������Ϊ0001 0000  ת��Ϊx++����
    		if(s2.equals("00010000")==true){  
    			s2="x++";
    			// System.out.println(s2);
    			return s2;
    		}
    		
    	    //����������Ϊ0010 0000 ת��Ϊx--����
    		if(s2.equals("00100000")==true){  
    			s2="x--";
    			// System.out.println(s2);
    			 return s2;
    		}
    	  //!?? ʹ��ĳ�豸��ʱ��
    		//001100xx  ʹ��A�豸��ʱ�� ת��Ϊ����!Ax
    		if(s2.startsWith("001100")){
    			String s1= Integer.valueOf(s2.substring(6,8),2).toString();
    			s2="!A"+s1;
    			// System.out.println(s2);
    			return s2;
    		}
    		//001101xx  ʹ��B�豸��ʱ�� ת��Ϊ����!Bx
    		if(s2.startsWith("001101")){
    			String s1= Integer.valueOf(s2.substring(6,8),2).toString();
    			s2="!B"+s1;
    			// System.out.println(s2);
    			return s2;
    		}
    		//001110xx  ʹ��C�豸��ʱ�� ת��Ϊ����!Cx
    		if(s2.startsWith("001110")){
    			String s1= Integer.valueOf(s2.substring(6,8),2).toString();
    			s2="!C"+s1;
    			// System.out.println(s2);
    			return s2;
    		}		
    	  //����������Ϊ010000000,ת��Ϊend�����ʾ��ִ���ļ�����
    	  if(s2.equals("01000000")==true){
    			  s2="end";
    		//System.out.println(s2);
    			 return s2 ;
    		}
    	  s2="�Ҳ���������";
    	  return s2;	  
    	}
    	
	public static String[] byteToString(byte[] list) {
		int size = list.length;
		String[] result = new String[size];
		for (int i = 0; i < result.length; i++) {
			try {
				result[i]= byteToString(list[i]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
    
     