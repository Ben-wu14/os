package doc;

public class DirEntry extends Entry{
	private Directory directory;
	public DirEntry(Directory root,String name,int StartBlock) {
		// TODO Auto-generated constructor stub
		super(root,name);
		this.setDir(true);
		this.setNumOfBlock(1);
		this.setReadOnly(false);
		this.setStartBlock(StartBlock);
		
	}
	
	public DirEntry(String name,int StartBlock) {
		// TODO Auto-generated constructor stub
		super(name);
		this.setDir(true);
		this.setNumOfBlock(1);
		this.setReadOnly(false);
		this.setStartBlock(StartBlock);
	}
	public Directory getDirectory() {
		return directory;
	}
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
