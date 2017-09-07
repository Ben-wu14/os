package doc;

public class OpenedFile {
	private Text file;
	private DocEntry entry;
	public OpenedFile(Text file,DocEntry entry) {
		// TODO Auto-generated constructor stub
		this.setFile(file);
		this.setEntry(entry);
	}
	public Text getFile() {
		return file;
	}
	public void setFile(Text file) {
		this.file = file;
	}
	public DocEntry getEntry() {
		return entry;
	}
	public void setEntry(DocEntry entry) {
		this.entry = entry;
	}
	
}
