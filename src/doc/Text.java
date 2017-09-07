package doc;

import java.util.Arrays;

public class Text {
	private String content;
	public Text() {
		// TODO Auto-generated constructor stub
		setContent("");
		//以‘#’作为文本结束符号
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
			System.err.println("字符串录入失败");
			return;
		}
		ary=Arrays.copyOf(ary, i);
		this.content= new String(ary);
	}
}
