package doc;

import java.io.IOException;

public class Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		diskGenerate();
	}
	private static void readTesting(){
		readBlock( 0, 2);
	}
	private static void readBlock(int begin,int num){
		try {
			byte[]data=DiskManage.readData(begin,num);
			for (int i = 0; i < num; i++) {
				//System.out.print("NO."+i+" block");
				for (int j = 0; j < 128; j++) {
					System.out.print(" "+data[i*128+j]);
				}
				System.out.println();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void writeTesting(){
		DiskManage.clearDisk();
		try {
			byte[] data=new byte[256];
			data[0]=1;
			data[5]=10;
			data[127]=1;
			data[128]=22;
			data[220]=88;
			data[255]=1;
			int begin =DiskManage.allocateBlock(2);
			System.out.println("Begin block"+begin);
			int num=DiskManage.writeData(begin, data);
			System.out.println("Before free space");
			readBlock( 0, 1);
			readBlock(1, 1);
			readBlock( 2, 1);
			readBlock(begin, num);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void freeSpaceTesting(){
		writeTesting();
		try {
			
			DiskManage.freeSpace(3,2);
			System.out.println("After free space");
			readBlock( 0, 1);
			readBlock( 1, 1);
			readBlock(2, 1);
			readBlock( 3, 1);
			readBlock( 4, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void allocateTesting() {
		DiskManage.clearDisk();
		System.out.println("Before allocate:");
		readBlock( 0, 1);
		DiskManage.allocateBlock(2);
		System.out.println("After allocate:");
		readBlock( 0, 1);
	}
	private static void createEntryTesting(){
		DiskManage.clearDisk();
		DirEntry dirEntry = new DirEntry("小目录", DiskManage.allocateBlock(2));
		dirEntry.setReadOnly(true);
		dirEntry.setNormalFile(true);
		dirEntry.setSystemFile(true);
		System.out.println(dirEntry);
		DocEntry docEntry = new DocEntry(new Directory(dirEntry), "文字小文件", DiskManage.allocateBlock(1),'t');
		System.out.println(docEntry);
	}
	private static void reAllocateTesting(){
		DiskManage.clearDisk();
		int begin=DiskManage.allocateBlock(2);
		System.out.println("分配磁盘块，磁盘起始位置为"+begin);
		DiskManage.reAllocateBlock(begin, 3);
		System.out.println("重分配3个磁盘块");
		readBlock(0, 1);
		DiskManage.reAllocateBlock(begin, 2);
		System.out.println("重分配2个磁盘块");
		readBlock(0, 1);
	}
	private static void diskGenerate(){
		DiskManage.clearDisk();
		DiskManage.printFAT();
		DocManage.initial();
		DirEntry dirEntry=DocManage.createDir("d1",DocManage.rootDir );
		DirEntry dirEntry2=DocManage.createDir("d2",DocManage.rootDir );
		DirEntry dirEntry3=DocManage.createDir("d3",DocManage.rootDir );
		DocManage.createDoc("t6", DocManage.rootDir );
		DiskManage.printFAT();
		Directory directory=DocManage.openDirectory(dirEntry);
		Directory directory2=DocManage.openDirectory(dirEntry2);
		Directory directory3=DocManage.openDirectory(dirEntry3);
		DocEntry docEntry=DocManage.createDoc("t1", directory);
		DocEntry docEntry2=DocManage.createDoc("t2", directory);
		DocManage.createDoc("t3", directory2);
		DocManage.createDoc("t4", directory2);
		DocManage.createDoc("t5", directory2);
		DocManage.createDir("d4", directory3);
		System.out.println(dirEntry);
		System.out.println(docEntry);
		DiskManage.printFAT();
	}
	private static void findPath(){
		DocManage.initial();
		String path = "root:\\d2";
		System.out.println(path);
		Directory result = DocManage.findFileDir(path);
		System.out.println(result);
	}
	private static void notepadTest(){
		DocManage.initial();
		DocEntry docEntry = DocManage.findFile("root:\\d1\\t2");
		try {
			OpenedFile file =DocManage.openFile(docEntry);
			Text text =file.getFile();
			System.out.println("原本t2文件内容： "+text.getContent());
			text.setContent("helo23");
			DocManage.saveData(file);
			System.out.println("文件保存完毕");
			DocManage.closeFile(file.getEntry());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void createDiffNameFileTest(){
		DiskManage.clearDisk();
		DocManage.initial();
		Directory root = DocManage.rootDir;
		DirEntry dir1Entry=DocManage.createDir(root);
		DocManage.createDoc(root);
		DirEntry dir2=DocManage.createDir(root);
		DocManage.createDir(root);
		Directory dir1= DocManage.openDirectory(dir1Entry);
		DocManage.createDoc(dir1);
		DocManage.createDoc(dir1);
		DocManage.createDoc(dir1);
		DocManage.deleteFile(dir1Entry, root);
		System.out.println(root);
		System.out.println(dir1);
	}
}
