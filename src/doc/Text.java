package doc;

import java.util.Arrays;

public class Text {
	private String content;
	public Text() {
		// TODO Auto-generated constructor stub
		setContent("");
		//�ԡ�#����Ϊ�ı���������
	}
	public Text(String content){
		this.setContent(content);
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSaveString(){
		return content+"#";
	}
	public void setContentTrans(String content){
		char[] ary= content.toCharArray();
		int i = 0;
		for (; i <ary.length ; i++) {
			if(ary[i]=='#'){
				break;
			}
		}
		if(i==ary.length){
			System.err.println("�ַ���¼��ʧ��");
			return;
		}
		ary=Arrays.copyOf(ary, i);
		this.content= new String(ary);
	}
}
