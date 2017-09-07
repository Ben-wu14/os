package doc;

import java.util.ArrayList;

public class Directory {
	private Directory root;
	private ArrayList<Entry> entries;
	private String name;
	private DirEntry entry;
	public Directory(DirEntry entry) {
		// TODO Auto-generated constructor stub
		this.setEntry(entry);
		setName(entry.getName());
		setRoot(entry.getRoot());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		entry.setName(name);
	}
	public DirEntry getEntry() {
		return entry;
	}
	public void setEntry(DirEntry entry) {
		this.entry = entry;
		name=entry.getName();
	}
	public ArrayList<Entry> getEntries() {
		return entries;
	}
	public void setEntries(ArrayList<Entry> entries) {
		this.entries = entries;
	}
	public Directory getRoot() {
		return root;
	}
	public void setRoot(Directory root) {
		this.root = root;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		/*
		String state="目录名： "+name;
		state+="\n包含内容：\n";
		for (Entry entry : entries) {
			state+=entry.getName()+"\n";
		}
		*/
		return name;
	}
	public boolean contentEquals(Directory root2) {
		// TODO Auto-generated method stub
		return root2.equals(this);
	}
	public boolean isEmpty(){
		return entries==null||entries.size()==0;
	}
}
