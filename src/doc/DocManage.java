package doc;

import java.io.IOException;
import java.util.ArrayList;


public class DocManage {
	public static Directory rootDir;
	private static OpenedFile[] openedFile;
	public static void initial() {
		// TODO Auto-generated constructor stub
		openedFile=new OpenedFile[5];
		DiskManage.inital();
		rootTrans();
		System.out.println("DocManage Initial finished");
	}
	//���ļ��еĸ�Ŀ¼¼��DocManage
	private static void rootTrans() {
		byte[] data;
		try {
			data = DiskManage.readData(2, 1);
			DirEntry rootEntry=new DirEntry("��Ŀ¼",2);

			rootDir=new Directory(rootEntry);
			rootEntry.setDirectory(rootDir);
			rootDir.setEntries(DataTrans.loadEntry(data,rootDir));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	//�����ļ���
	public static DirEntry createDir(String name,Directory root) {
		//�ж��Ƿ����ظ�����
		if(findEntry(name, root)){
			System.err.println("����ͬ�����ļ�");
			return null;
		}
		//����Ŀ¼����뵽�ļ�����------------------
		DirEntry dirEntry= new DirEntry(root,name,DiskManage.allocateBlock(1));
		root.getEntries().add(dirEntry);
		try {
			saveData(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dirEntry;
	}
	public static DirEntry createDir(Directory root){
		int i=1;
		while(findEntry("ND"+i, root)){
			i++;
		}
		return createDir("ND"+i, root);
	}
	//�����ı��ļ�
	public static DocEntry createDoc(String name,Directory root){
		//�ж��Ƿ����ظ�����
		if(findEntry(name, root)){
			System.err.println("����ͬ�����ļ�");
			return null;
		}
		//�����ļ�����뵽�ļ�����------------------
		DocEntry docEntry= new DocEntry(root,name,DiskManage.allocateBlock(1),'t');
		root.getEntries().add(docEntry);
		try {
			saveData(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docEntry;
	}
	public static DocEntry createDoc(Directory root){
		int i=1;
		while(findEntry("NT"+i, root)){
			i++;
		}
		return createDoc("NT"+i, root);
	}
	
	//ɾ���ļ������ļ���
	public static boolean deleteFile(Entry entry,Directory root){
		//------------------
		//TODO delete file
		//�ж��Ƿ�Ϊ�ļ���
		if(entry.isDir()){
			DirEntry dirEntry=(DirEntry)entry;
			Directory directory = openDirectory(dirEntry);
			//�ж��ļ����Ƿ�Ϊ��
			if (!directory.isEmpty()) {
				System.err.println("��׼��ɾ���ǿ��ļ���");
				return false;
			}
		}
		//disk free�ռ�
		try {
			DiskManage.freeSpace(entry.getStartBlock(), entry.getNumOfBlock());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//------------------

		//ɾ����Ӧentry
		boolean isSucess=root.getEntries().remove(entry);
		//���ɾ���ɹ��ͱ����ļ�������
		if(isSucess){
			try {
				saveData(root);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isSucess;
	}
	//��д��ʽɾ��
	public static boolean removeFile(Entry entry,Directory root){
		//------------------
		//TODO delete file
		//�ж��Ƿ�Ϊ�ļ���
		if(entry.isDir()){
			DirEntry dirEntry=(DirEntry)entry;
			Directory directory = openDirectory(dirEntry);
			//�ж��ļ����Ƿ�Ϊ��
			if (!directory.isEmpty()) {
				System.err.println("��׼��ɾ���ǿ��ļ���");
				return false;
			}
		}
				
		//ɾ����Ӧentry
		boolean isSucess=root.getEntries().remove(entry);
		//���ɾ���ɹ��ͱ����ļ�������
		if(isSucess){
			try {
				saveData(root);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isSucess;
	}
	
	//�����ı��ļ�
	public static DocEntry copyText(DocEntry source,Directory destination){
		//���������ռ�
		int numOfBlocks= source.getNumOfBlock();
		int startBlock=DiskManage.allocateBlock(numOfBlocks);
		//�����¶���
		DocEntry docEntry = createDoc( source.getName(), destination);
		docEntry.setStartBlock(startBlock);
		docEntry.setNumOfBlock(numOfBlocks);
		//����ļ����ݣ�д�����ļ���
		byte[] data;
		try {
			data = DiskManage.readData(source.getStartBlock(), numOfBlocks);
			DiskManage.writeData(docEntry.getStartBlock(), data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docEntry;
	}
	//�����ı��ļ�
	public static void cutText(DocEntry source,Directory destination){
		//�Ѽ��е��ļ������µ��ļ����б�
		destination.getEntries().add(source);
		//�Ƴ�ԭ���ļ���������ļ���
		boolean sucess =source.getRoot().getEntries().remove(source);
		if(sucess){
			try {
				//����ɹ����򱣴������ļ�������
				source.setRoot(destination);
				saveData(source.getRoot());
				saveData(destination);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//���ʧ�ܣ������ʾ
			System.err.println("���й��̵��Ƴ�ʧ��");
		}
	}
	//���ļ���
	public static Directory openDirectory(DirEntry dirEntry){
		//�����ļ��ж���
		Directory directory = new Directory(dirEntry);
		//��ȡ�ļ����ڵ�����
		byte[] data;
		try {
			data = DiskManage.readData(dirEntry.getStartBlock(), dirEntry.getNumOfBlock());
			directory.setEntries(DataTrans.loadEntry(data,directory));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//������ת��Ϊ����Ҵ����ļ��ж�����
		
		return directory;
	}
	//���ļ�
	public static OpenedFile openFile(DocEntry file) throws IOException{
		if(hasFileSpace()){
			if(!isOpened(file)){
				Text text = new Text();
				//��ȡ����
				byte[] data=DiskManage.readData(file.getStartBlock(), file.getNumOfBlock());
				//������ת���string����text
				if(!DiskManage.isEmpty(data)){
					String content=new String(data);
					text.setContentTrans(content);
				}
				OpenedFile oFile = new OpenedFile(text, file);
				openedFile[findFileSpace()]=oFile;
				System.out.println("���ڴ��ļ�: "+file.getName());
				return oFile;
			}else{
				System.out.println("�ļ��Ѿ���");
			}
			
		}else{
			System.out.println("û�пռ���ļ����ﵽͬʱ��5���ļ�������");
		}
		 return null;
	}
	//�ر��ļ�
	public static boolean closeFile(DocEntry file) throws IOException{
		int index=findOpenedFile(file);
		if(index!=-1){
			//���Ѿ���
			//saveData(openedFile[index]);
			openedFile[index]=null;
			System.out.println("�ѹر��ļ�: "+file.getName());
			return true;
		}
		System.out.println("�ļ����������Ѵ��б�");
		return false;
	}
	public static boolean closeFile(OpenedFile file) throws IOException{
		for (int i = 0; i < openedFile.length; i++) {
			if(openedFile[i]==file){
				openedFile[i]=null;
				//saveData(file);
				System.out.println("�ļ�"+file.getEntry().getName()+"�Ѿ��ر�");
				return true;
			}
		}
		return false;
	}
	//��������
	public static void saveData(OpenedFile file) throws IOException{
		//���������
		byte[] content = file.getFile().getSaveString().getBytes();
		//�����������̿���Ŀ
		int numOfblocks=DiskManage.calNumOfBlocks(content);
		//����ļ���ʼ���̿�
		int begin=file.getEntry().getStartBlock();
		//�ж�������̿���Ŀ��������Ŀ�Ƿ�ƥ��
		if(numOfblocks!=file.getEntry().getNumOfBlock()){
			//��ƥ����������·���
			DiskManage.reAllocateBlock(begin, numOfblocks);
			//���´��̿���Ŀ
			file.getEntry().setNumOfBlock(numOfblocks);
		}
		//д���ı�����
		DiskManage.writeData(begin, content);
		//�����ı������ļ�������
		saveData(file.getEntry().getRoot());
	}
	public static void saveData(Directory directory) throws IOException{
		byte[] data = DataTrans.entryTrans(directory.getEntries());
		DiskManage.writeData(directory.getEntry().getStartBlock(), data);
	}
	//����·��Ѱ���ļ�����Ŀ¼
	public static Directory findFileDir(String path){
		//����ַ�ֲ�
		String[] dir = path.split("\\\\");
		//�ж��Ƿ����ڸ�Ŀ¼����
		if(dir[0].compareTo("root:")!=0){
			System.out.println("Not in root disk");
			return null;
		}else{
			//�Ĵ����ս��
			Directory result=rootDir;
			//�Ĵ�Ŀǰ�����ļ��а�������
			ArrayList<Entry> entries = result.getEntries();
			//����ÿһ��·����
			for (int i = 1; i < dir.length; i++) {
				//�뵱ǰ�ļ��е�������Ա�
				for (int j = 0; j < entries.size(); j++) {
					//�ݴ����������
					Entry temp = entries.get(j);
					//����ҵ���Ӧ��ͬ���Ƶ���
					if(temp.getName().compareTo(dir[i])==0){
						//Ѱ�ҵ����Ƿ�ΪĿ¼
						if (temp.isDir()) {
							//���������Լ���������
							result = openDirectory((DirEntry)temp);
							entries= result.getEntries();
						}else{
							//����������������
							return result;
						}
						//���һ����Ŀ¼��������ʼ��һ��
						break;
					}else{
						//������һ����Ҳ�Ҳ���ƥ������
						if(j==entries.size()-1){
							System.out.println("·���������ļ��������ڴ�����");
							return result;
						}
					}
				}
			}
			return result;
		}
		
	}
	//����·������ļ�����
	public static DocEntry findFile(String path){
		Directory root = findFileDir(path);
		String[]strings = path.split("\\\\");
		String docName= strings[strings.length-1];
		ArrayList<Entry> entries = root.getEntries();
		for (Entry entry : entries) {
			if(entry.getName().equals(docName)){
				return (DocEntry)entry;
			}
		}
		return null;
	}
	//�Ƿ��пռ�������ļ�
	private static boolean hasFileSpace(){
		return findFileSpace()!=-1;
	}
	//Ѱ�������ļ��еĿ���λ��
	private static int findFileSpace(){
		for (int i = 0; i < openedFile.length; i++) {
			if(openedFile[i]==null){
				return i;
			}
		}
		return -1;
	}
	//�ж��Ƿ��Ѿ���
	private static boolean isOpened(DocEntry file){
		return findOpenedFile(file)!=-1;
	}
	//���openedFile�ĳ���
	private static int getOpenedFileLength(){
		int length=0;
		for (int i = 0; i < openedFile.length; i++) {
			if(openedFile[i]!=null){
				length++;
			}
		}
		return length;
	}
	//��openedfile��Ѱ���ļ�
	private static int findOpenedFile(DocEntry file){
		for (int i = 0; i < getOpenedFileLength(); i++) {
			if(openedFile[i].getEntry().equals(file)){
				return i;
			}
		}
		return -1;
	}
	//�����������ж��Ƿ�������
	private static boolean findEntry(String name,Directory root){
		ArrayList<Entry> list = root.getEntries();
		for (Entry entry : list) {
			if(entry.getName().compareTo(name)==0){
				return true;
			}
		}
		return false;
	}
}
