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
	//把文件中的根目录录入DocManage
	private static void rootTrans() {
		byte[] data;
		try {
			data = DiskManage.readData(2, 1);
			DirEntry rootEntry=new DirEntry("根目录",2);

			rootDir=new Directory(rootEntry);
			rootEntry.setDirectory(rootDir);
			rootDir.setEntries(DataTrans.loadEntry(data,rootDir));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	//创建文件夹
	public static DirEntry createDir(String name,Directory root) {
		//判断是否有重复名称
		if(findEntry(name, root)){
			System.err.println("出现同名的文件");
			return null;
		}
		//创建目录项，加入到文件夹中------------------
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
	//创建文本文件
	public static DocEntry createDoc(String name,Directory root){
		//判断是否有重复名称
		if(findEntry(name, root)){
			System.err.println("出现同名的文件");
			return null;
		}
		//创建文件项，加入到文件夹中------------------
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
	
	//删除文件或者文件夹
	public static boolean deleteFile(Entry entry,Directory root){
		//------------------
		//TODO delete file
		//判断是否为文件夹
		if(entry.isDir()){
			DirEntry dirEntry=(DirEntry)entry;
			Directory directory = openDirectory(dirEntry);
			//判断文件夹是否为空
			if (!directory.isEmpty()) {
				System.err.println("正准备删除非空文件夹");
				return false;
			}
		}
		//disk free空间
		try {
			DiskManage.freeSpace(entry.getStartBlock(), entry.getNumOfBlock());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//------------------

		//删除对应entry
		boolean isSucess=root.getEntries().remove(entry);
		//如果删除成功就保存文件夹数据
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
	//非写入式删除
	public static boolean removeFile(Entry entry,Directory root){
		//------------------
		//TODO delete file
		//判断是否为文件夹
		if(entry.isDir()){
			DirEntry dirEntry=(DirEntry)entry;
			Directory directory = openDirectory(dirEntry);
			//判断文件夹是否为空
			if (!directory.isEmpty()) {
				System.err.println("正准备删除非空文件夹");
				return false;
			}
		}
				
		//删除对应entry
		boolean isSucess=root.getEntries().remove(entry);
		//如果删除成功就保存文件夹数据
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
	
	//复制文本文件
	public static DocEntry copyText(DocEntry source,Directory destination){
		//向磁盘请求空间
		int numOfBlocks= source.getNumOfBlock();
		int startBlock=DiskManage.allocateBlock(numOfBlocks);
		//创建新对象
		DocEntry docEntry = createDoc( source.getName(), destination);
		docEntry.setStartBlock(startBlock);
		docEntry.setNumOfBlock(numOfBlocks);
		//获得文件数据，写入新文件中
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
	//剪切文本文件
	public static void cutText(DocEntry source,Directory destination){
		//把剪切的文件放入新的文件项列表
		destination.getEntries().add(source);
		//移除原本文件夹里面的文件项
		boolean sucess =source.getRoot().getEntries().remove(source);
		if(sucess){
			try {
				//如果成功，则保存两个文件夹数据
				source.setRoot(destination);
				saveData(source.getRoot());
				saveData(destination);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//如果失败，输出提示
			System.err.println("剪切过程的移除失败");
		}
	}
	//打开文件夹
	public static Directory openDirectory(DirEntry dirEntry){
		//创建文件夹对象
		Directory directory = new Directory(dirEntry);
		//读取文件夹内的数据
		byte[] data;
		try {
			data = DiskManage.readData(dirEntry.getStartBlock(), dirEntry.getNumOfBlock());
			directory.setEntries(DataTrans.loadEntry(data,directory));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//把数据转译为项，并且存入文件夹对象中
		
		return directory;
	}
	//打开文件
	public static OpenedFile openFile(DocEntry file) throws IOException{
		if(hasFileSpace()){
			if(!isOpened(file)){
				Text text = new Text();
				//读取数据
				byte[] data=DiskManage.readData(file.getStartBlock(), file.getNumOfBlock());
				//把数据转译成string存入text
				if(!DiskManage.isEmpty(data)){
					String content=new String(data);
					text.setContentTrans(content);
				}
				OpenedFile oFile = new OpenedFile(text, file);
				openedFile[findFileSpace()]=oFile;
				System.out.println("正在打开文件: "+file.getName());
				return oFile;
			}else{
				System.out.println("文件已经打开");
			}
			
		}else{
			System.out.println("没有空间打开文件，达到同时打开5个文件的上限");
		}
		 return null;
	}
	//关闭文件
	public static boolean closeFile(DocEntry file) throws IOException{
		int index=findOpenedFile(file);
		if(index!=-1){
			//若已经打开
			//saveData(openedFile[index]);
			openedFile[index]=null;
			System.out.println("已关闭文件: "+file.getName());
			return true;
		}
		System.out.println("文件不存在于已打开列表");
		return false;
	}
	public static boolean closeFile(OpenedFile file) throws IOException{
		for (int i = 0; i < openedFile.length; i++) {
			if(openedFile[i]==file){
				openedFile[i]=null;
				//saveData(file);
				System.out.println("文件"+file.getEntry().getName()+"已经关闭");
				return true;
			}
		}
		return false;
	}
	//保存数据
	public static void saveData(OpenedFile file) throws IOException{
		//获得数据流
		byte[] content = file.getFile().getSaveString().getBytes();
		//计算出所需磁盘块数目
		int numOfblocks=DiskManage.calNumOfBlocks(content);
		//获得文件开始磁盘块
		int begin=file.getEntry().getStartBlock();
		//判断所需磁盘块数目与已有数目是否匹配
		if(numOfblocks!=file.getEntry().getNumOfBlock()){
			//不匹配则进行重新分配
			DiskManage.reAllocateBlock(begin, numOfblocks);
			//更新磁盘块数目
			file.getEntry().setNumOfBlock(numOfblocks);
		}
		//写入文本数据
		DiskManage.writeData(begin, content);
		//更新文本所在文件夹数据
		saveData(file.getEntry().getRoot());
	}
	public static void saveData(Directory directory) throws IOException{
		byte[] data = DataTrans.entryTrans(directory.getEntries());
		DiskManage.writeData(directory.getEntry().getStartBlock(), data);
	}
	//根据路径寻找文件所在目录
	public static Directory findFileDir(String path){
		//将地址分拆
		String[] dir = path.split("\\\\");
		//判断是否属于根目录内容
		if(dir[0].compareTo("root:")!=0){
			System.out.println("Not in root disk");
			return null;
		}else{
			//寄存最终结果
			Directory result=rootDir;
			//寄存目前所打开文件夹包含的项
			ArrayList<Entry> entries = result.getEntries();
			//对于每一个路径项
			for (int i = 1; i < dir.length; i++) {
				//与当前文件夹的所有项对比
				for (int j = 0; j < entries.size(); j++) {
					//暂存遍历到的项
					Entry temp = entries.get(j);
					//如果找到对应相同名称的项
					if(temp.getName().compareTo(dir[i])==0){
						//寻找到的是否为目录
						if (temp.isDir()) {
							//如果是则可以继续打开往下
							result = openDirectory((DirEntry)temp);
							entries= result.getEntries();
						}else{
							//如果不是则结束搜索
							return result;
						}
						//完成一级的目录搜索，开始下一级
						break;
					}else{
						//如果最后一个项也找不到匹配名称
						if(j==entries.size()-1){
							System.out.println("路径出错，此文件不存在于磁盘中");
							return result;
						}
					}
				}
			}
			return result;
		}
		
	}
	//根据路径获得文件的项
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
	//是否有空间继续打开文件
	private static boolean hasFileSpace(){
		return findFileSpace()!=-1;
	}
	//寻找正打开文件中的空余位置
	private static int findFileSpace(){
		for (int i = 0; i < openedFile.length; i++) {
			if(openedFile[i]==null){
				return i;
			}
		}
		return -1;
	}
	//判断是否已经打开
	private static boolean isOpened(DocEntry file){
		return findOpenedFile(file)!=-1;
	}
	//获得openedFile的长度
	private static int getOpenedFileLength(){
		int length=0;
		for (int i = 0; i < openedFile.length; i++) {
			if(openedFile[i]!=null){
				length++;
			}
		}
		return length;
	}
	//从openedfile中寻找文件
	private static int findOpenedFile(DocEntry file){
		for (int i = 0; i < getOpenedFileLength(); i++) {
			if(openedFile[i].getEntry().equals(file)){
				return i;
			}
		}
		return -1;
	}
	//搜索所有项判断是否有重名
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
