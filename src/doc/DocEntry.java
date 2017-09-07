package doc;

public class DocEntry extends Entry{
	private char extension;
	public DocEntry(Directory root,String name,int startBlock,char fileType) {
		// TODO Auto-generated constructor stub
		super(root,name);
		this.setDir(false);
		this.setNumOfBlock(1);
		this.setReadOnly(false);
		this.setStartBlock(startBlock);
		setFileType(fileType);
		//*********���������һ���մ��̿�***********
	}
	
	public char getFileType() {
		return extension;
	}
	public void setFileType(char fileType) {
		this.extension = fileType;
		if(fileType=='t'){
			setType("�ı��ļ�");
		}else{
			setType("��ִ���ļ�");
		}
	}
}
