package doc;

import java.util.Arrays;


public class Entry {
	private Directory root;
	private String name;
	private boolean isDir;
	private String type;
	private boolean readOnly;
	private boolean normalFile;
	private boolean systemFile;
	private int startBlock;
	private int numOfBlock;
	public Entry(Directory root,String name) {
		// TODO Auto-generated constructor stub
		this.setRoot(root);
		setName(name);
		
	}
	public Entry(String name) {
		setName(name);
	}
	public boolean isNormalFile() {
		return normalFile;
	}
	public void setNormalFile(boolean normalFile) {
		this.normalFile = normalFile;
	}
	public boolean isSystemFile() {
		return systemFile;
	}
	public void setSystemFile(boolean systemFile) {
		this.systemFile = systemFile;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		char[] ary=name.toCharArray();
		int i = ary.length-1;
		for (; i > 0; i--) {
			if(ary[i]!=0){
				break;
			}
		}
		this.name = new String(Arrays.copyOf(ary, i+1));
	}
	public boolean isDir() {
		return isDir;
	}
	public void setDir(boolean isDir) {
		this.isDir = isDir;
		if(isDir){
			setType("�ļ���");
		}
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public int getStartBlock() {
		return startBlock;
	}
	public void setStartBlock(int startBlock) {
		this.startBlock = startBlock;
	}
	public int getNumOfBlock() {
		return numOfBlock;
	}
	public void setNumOfBlock(int numOfBlock) {
		this.numOfBlock = numOfBlock;
	}
	public Directory getRoot() {
		return root;
	}
	public void setRoot(Directory root) {
		this.root = root;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String state ="";
		if(root!=null)
		state+="��Ŀ¼��"+root.getName()+"\n";
		if(isDir)
			state+="Ŀ¼����: "+name+"\n";
		else
			state+="�ļ�����: "+name+"\n";
		if(readOnly)
			state+="ֻ���ļ�"+"\n";
		if(normalFile)
			state+="��ͨ�ļ�"+"\n";
		if(systemFile)
			state+="ϵͳ�ļ�"+"\n";
		state+="��ʼ���̿�"+startBlock+"\n";
		state+="���̿����"+numOfBlock+"\n";
		
		//------------------
		return name;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Entry entry = (Entry)obj;
		boolean result=true;
		result &= entry.getName().equals(name);
		result &= entry.getNumOfBlock()==numOfBlock;
		result &= entry.getStartBlock()==startBlock;
		result &= !(entry.isDir^isDir);
		return result;
	}
}
