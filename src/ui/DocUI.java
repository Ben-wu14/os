package ui;

import java.io.IOException;
import java.util.ArrayList;

import doc.DirEntry;
import doc.Directory;
import doc.DiskManage;
import doc.DocEntry;
import doc.DocManage;
import doc.Entry;
import doc.OpenedFile;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DocUI extends Application{
	public static String path="root:\\";	//搜索框路径
	public static Directory currentDir=DocManage.rootDir;	//目前所在文件夹
	public static DocEntry copySpace=null;		//复制的文档
	public static ObservableList<Entry> data;	//列表的数据
	static TableView<Entry> tableView = new TableView<>();	//列表的对象
	private static Entry seleted;				//列表中被选中的项
	private static boolean isCopy=false;		//是否处于复制状态
	private static boolean isCut=false;			//是否处于剪切状态
	private static Directory cutRootDir=null;	//被剪切文件所在的文件夹
	private static TableView<readFAT> table = new TableView<readFAT>();	//显示磁盘状态的列表
	private static ObservableList<readFAT> dataFAT;	//显示磁盘状态的数据
	
	//根目录界面
		private static Node rootIcon=new ImageView(new Image(("/目录.png")));//根目录所用的图片
		
	    private static  TreeItem<Entry> rootNode;	//根目录节点
	    private static TreeView<Entry> treeView;	//树状图的对象
	    private static TreeItem<Entry> seletNote;	//被选中的树状图节点
	    //private static TreeItem<Entry> copyNote;
	    private static TreeItem<Entry> curDirNote;	//被选中的树状图节点所在的目录
	//同步组件
	private static TextField searchTextField = new TextField(path);//搜索框
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		DocManage.initial();	//初始化磁盘
		BorderPane borderPane = new BorderPane();
		HBox topPane =searchBlock();
		VBox rightPane = diskVisualRun();
		VBox leftPane = treeInterface();
		HBox centrePane =new HBox(showinterfaceRun(path));
		
		//设置居中
		topPane.setAlignment(Pos.CENTER);
		//设置边界
		topPane.setPadding(new Insets(20));
		
		HBox conectBox = new HBox();
		conectBox.getChildren().addAll(leftPane,centrePane,rightPane);
		conectBox.setAlignment(Pos.CENTER);
		//设置搜索框的长度
		searchTextField.setMinWidth(conectBox.getWidth());
		//放入组件
		borderPane.setTop(topPane);
		borderPane.setCenter(conectBox);
		Scene scene = new Scene(borderPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("文档管理");
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	//NotePad 记事本界面-------------------------------------------
	public static void notpadRun(DocEntry docEntry){
		Stage stage = new Stage();
		try {
			notepadopen(stage,docEntry);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void notpadRun(String path){
		Stage stage = new Stage();
		try {
			notepadopen(stage,DocManage.findFile(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void notepadopen(Stage primaryStage,DocEntry docEntry) throws IOException{
		OpenedFile doc = DocManage.openFile(docEntry);
		BorderPane borderPane = new BorderPane();
		
		TextArea textField = new TextArea(doc.getFile().getContent());
		MenuBar menuBar = new MenuBar();
		
        // --- Menu 保存
        Menu menuDoc = new Menu("文件");
        MenuItem menuSave=new MenuItem("保存");
        menuSave.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		doc.getFile().setContent(textField.getText());
    			try {
    				DocManage.saveData(doc);
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    			updateToShowDisk();
        	}
        });
        menuDoc.getItems().add(menuSave);
        // --- Menu style
        Menu menuStyle = new Menu("字体大小");
        
        MenuItem large = new MenuItem("大字");   //字体大小
        large.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		textField.setFont(Font.font(40));
        	}
        });
        
        MenuItem middle = new MenuItem("中号");
        middle.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		textField.setFont(Font.font(30));
        	}
        });
        		
        MenuItem small = new MenuItem("小字");
        small.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		textField.setFont(Font.font(20));
        	}
        });
        
        menuStyle.getItems().addAll(large , middle, small);
        
        menuBar.getMenus().addAll(menuDoc, menuStyle);
		borderPane.setTop(menuBar);
		borderPane.setCenter(textField);
		Scene scene = new Scene(borderPane);
		primaryStage.setTitle(docEntry.getName());
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e->{
			try {
				DocManage.closeFile(doc);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		primaryStage.show();
	}
	//-------------------------------------------------
	//右键菜单界面------------------------------------
	//列表空白处右键
	public static ContextMenu blankRightClick(){
		ContextMenu contextMenu = new ContextMenu();   //空白处右键菜单
		contextMenu.setStyle("-fx-background-color:rgb(240 , 240 , 240 , 0.95) ; -fx-border-color: lightgray ");
		MenuItem item1 = new MenuItem("新建文件夹" );              //新建文件夹
		item1.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				DirEntry dirEntry=DocManage.createDir(currentDir);//创建文件夹
				data =FXCollections.observableArrayList(currentDir.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				updateToShowDisk();
				curDirNote.getChildren().add(new TreeItem<Entry>(dirEntry));
			}
			
		});
		
		MenuItem item2 = new MenuItem("新建文本文件");    //新建文本文件
		item2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				//pane.getChildren().add(new Button("ok"));
				DocEntry docEntry=DocManage.createDoc(currentDir);
				data =FXCollections.observableArrayList(currentDir.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				updateToShowDisk();
				//新建文本文件
				curDirNote.getChildren().add(new TreeItem<Entry>(docEntry));
			}
			
		});
		
		MenuItem item4 = new MenuItem("粘贴");
		item4.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				//把剪贴空间的文本文件复制到当前文件夹
				DocManage.copyText(copySpace,currentDir);
				if(isCut){
					DocManage.deleteFile(copySpace,cutRootDir);
				}
				isCopy=false;
				isCut=false;
				//update
				data =FXCollections.observableArrayList(currentDir.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				
				updateToShowDisk();
				curDirNote.getChildren().add(new TreeItem<Entry>(copySpace));
			}
		});
		//是否有复制
		if(isCopy||isCut)	contextMenu.getItems().addAll(item1,item2,item4);
		else contextMenu.getItems().addAll(item1,item2);
		return contextMenu;
	}
	//在项里右键
	private static ContextMenu rightClickMenu(Entry entry){
	  		ContextMenu contextMenu = new ContextMenu();   //右键菜单
			contextMenu.setStyle("-fx-background-color:rgb(240 , 240 , 240 , 0.95) ; -fx-border-color: lightgray ");
			MenuItem item1 = new MenuItem("打开");
			item1.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent e){
					if(entry.isDir()){
						//打开被选中的文件夹
						Directory directory = DocManage.openDirectory((DirEntry)seleted);
						currentDir = directory;
						//更新搜索框数据
						path+=directory.getName()+"\\";
						searchTextField.setText(path);
						//更新列表数据
						data =FXCollections.observableArrayList(currentDir.getEntries());
						tableView.setItems(FXCollections.observableArrayList(data));
					}else{
						//如果是文本文件，则打开文件
						notpadRun((DocEntry)entry);
					}
					
				}
				
			});

			//复制
			MenuItem item2 = new MenuItem("复制"); 
			item2.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent e){
					//如果不是文件夹，把选中项存入copySpace
					if(!entry.isDir())
					copySpace=(DocEntry)entry;
					isCopy=true;
				}
			});
			
			//粘贴
			MenuItem item3 = new MenuItem("粘贴"); 
			item3.setOnAction(new EventHandler<ActionEvent>(){
						@Override
						public void handle(ActionEvent e){
							//寄存复制后新的entry
							Entry copyed=null;
							if(entry.isDir()){
								//如果是文件夹，粘贴到选中的文件夹中
								if(isCut)
									DocManage.cutText(copySpace, DocManage.openDirectory((DirEntry)entry));
								else
									copyed=DocManage.copyText(copySpace, DocManage.openDirectory((DirEntry)entry));
							}else{
								//如果是文件，粘贴到选中文件所在的文件夹
								if(isCut)
									DocManage.cutText(copySpace,currentDir);
								else
									copyed=DocManage.copyText(copySpace,currentDir);
							}
							//更新树状图界面
							if(isCut)
								seletNote.getParent().getChildren().add(new TreeItem<Entry>(copySpace,new ImageView(new Image(("txt-icon3.png")))));
							else 
								seletNote.getParent().getChildren().add(new TreeItem<Entry>(copyed,new ImageView(new Image(("txt-icon3.png")))));
							isCut=false;
							isCopy=false;
							//更新列表和磁盘情况的界面
							data =FXCollections.observableArrayList(currentDir.getEntries());
							tableView.setItems(FXCollections.observableArrayList(data));
							updateToShowDisk();
							
						}
					});

			//删除
			MenuItem item4 = new MenuItem("删除"); 
			item4.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent e){
					   //从列表中移除项
					   tableView.getItems().remove(entry);
					   //通过文件操作从磁盘彻底删除文件
					   System.out.println("删除"+entry+"是否成功"+DocManage.deleteFile(entry, currentDir));
					   //更新界面
					   data =FXCollections.observableArrayList(currentDir.getEntries());
					   tableView.setItems(FXCollections.observableArrayList(data));
					   updateToShowDisk();
					   seletNote.getParent().getChildren().remove(seletNote);
				}
			});

			//剪切
			MenuItem item5 = new MenuItem("剪切");
			item5.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent e){
					//保存所剪切的项和所在的文件夹
					copySpace=(DocEntry)entry;
					cutRootDir=currentDir;
					//移除文件但是不清除磁盘内容
					tableView.getItems().remove(entry);
					System.out.println("删除"+entry+"是否成功"+DocManage.removeFile(entry, currentDir));
					//更新界面
					data =FXCollections.observableArrayList(currentDir.getEntries());
					tableView.setItems(FXCollections.observableArrayList(data));
					updateToShowDisk();
					seletNote.getParent().getChildren().remove(seletNote);
					isCut=true;
					/*
					copySpace=(DocEntry)entry;
					System.out.println("cut: "+entry);
					cutRootDir=currentDir;
					ObservableList<Entry> observableList = tableView.getItems();
					boolean sucess=tableView.getItems().remove(entry);
					
					seletNote.getParent().getChildren().remove(seletNote);
					tableView.refresh();
					isCut=true;
					*/
				}
			});
			
			//重命名
			MenuItem item6 = new MenuItem("重命名");
			item6.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent e){
					renameStage(entry);
				}
			});
			//判断是否有复制或者剪切
			if(isCopy||isCut)
				contextMenu.getItems().addAll(item1 , item2 , item3 , item4 , item5,item6);
			else
				contextMenu.getItems().addAll(item1 , item2 , item4 , item5 ,item6);
			//contextMenu.getItems().addAll(item1 , item2 , item3 , item4 ,item6);
			return contextMenu;
	  	}
	//重命名方法
	public static void renameStage(Entry entry){
			TextField textField = new TextField(entry.getName());
			Stage stage = new Stage();
			Button save = new Button("保存");
			save.setOnAction(e->{
				String name=textField.getText();
				entry.setName(name);
				try {
					DocManage.saveData(currentDir);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				data.removeAll(data);
				data.addAll(FXCollections.observableArrayList(currentDir.getEntries()));
				tableView.setItems(FXCollections.observableArrayList(data));
				tableView.refresh();
				seletNote.getValue().setName(name);
				treeView.refresh();
				stage.close();
			});
			Button cancle = new Button("取消");
			cancle.setOnAction(e->stage.close());
			VBox vBox=new VBox(10);
			HBox hBox= new HBox(10);
			hBox.getChildren().addAll(save,cancle);
			vBox.getChildren().addAll(textField,hBox);
			Scene scene = new Scene(vBox);
			stage.setScene(scene);
			stage.show();
		}
	//文件夹操作
	private static ContextMenu dirClickMenu(DirEntry entry){
		ContextMenu contextMenu = new ContextMenu();   //右键菜单
		contextMenu.setStyle("-fx-background-color:rgb(240 , 240 , 240 , 0.95) ; -fx-border-color: lightgray ");
		MenuItem item1 = new MenuItem("新建文件夹" );              //新建文件夹
		item1.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Directory directory = DocManage.openDirectory((DirEntry)entry);
				DirEntry dirEntry=DocManage.createDir(directory);//创建文件夹
				data =FXCollections.observableArrayList(directory.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				updateToShowDisk();
				seletNote.getChildren().add(new TreeItem<Entry>(dirEntry,new ImageView(new Image(("icon-folder2.png")))));
			}
			
		});
		
		MenuItem item2 = new MenuItem("新建文本文件");    //新建文本文件
		item2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				//pane.getChildren().add(new Button("ok"));
				Directory directory = DocManage.openDirectory((DirEntry)entry);
				DocEntry docEntry=DocManage.createDoc(directory);
				data =FXCollections.observableArrayList(directory.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				updateToShowDisk();
				//新建文本文件
				seletNote.getChildren().add(new TreeItem<Entry>(docEntry,new ImageView(new Image(("txt-icon3.png")))));
			}
			
		});
		
		
		MenuItem item3 = new MenuItem("打开");
		item3.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
					Directory directory = DocManage.openDirectory((DirEntry)entry);
					currentDir = directory;
					//update data
					path+=directory.getName()+"\\";
					searchTextField.setText(path);
					data =FXCollections.observableArrayList(currentDir.getEntries());
					tableView.setItems(FXCollections.observableArrayList(data));
					seletNote.expandedProperty().set(true);
			}
			
		});

		//粘贴
		MenuItem item4 = new MenuItem("粘贴"); 
		item4.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent e){
						//进行复制，获得复制完后的新entry
						Entry copyed=DocManage.copyText(copySpace, DocManage.openDirectory((DirEntry)entry));
						if(isCut){
							DocManage.deleteFile(copySpace,cutRootDir);
						}
						isCut=false;
						isCopy=false;
						data =FXCollections.observableArrayList(currentDir.getEntries());
						tableView.setItems(FXCollections.observableArrayList(data));
						updateToShowDisk();
						//把新的entry存入树状图中
						seletNote.getChildren().add(new TreeItem<Entry>(copyed,new ImageView(new Image(("txt-icon3.png")))));
					}
				});

		//删除
		MenuItem item5 = new MenuItem("删除"); 
		item5.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				   Directory directory = DocManage.openDirectory(entry);
				   if(!directory.isEmpty()){
					   System.out.println("删除失败");
				   }else{
					   tableView.getItems().remove(entry);
					   System.out.println("删除"+entry+"是否成功"+DocManage.deleteFile(entry, currentDir));
					   data =FXCollections.observableArrayList(currentDir.getEntries());
					   tableView.setItems(FXCollections.observableArrayList(data));
					   updateToShowDisk();
					   seletNote.getParent().getChildren().remove(seletNote);
				   }
				   
			}
		});

		//重命名
		MenuItem item6 = new MenuItem("重命名");
		item6.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				renameStage(entry);
			}
		});
		//判断是否有复制或者剪切
		if(isCopy||isCut)
			contextMenu.getItems().addAll(item1 , item2 , item3 , item4 , item5,item6);
		else
			contextMenu.getItems().addAll(item1 , item2 , item3 , item5 ,item6);
		//contextMenu.getItems().addAll(item1 , item2 , item3 , item4 ,item6);
		return contextMenu;
	}
	//磁盘操作
	private static ContextMenu rootClickMenu(DirEntry entry){
		ContextMenu contextMenu = new ContextMenu();   //右键菜单
		contextMenu.setStyle("-fx-background-color:rgb(240 , 240 , 240 , 0.95) ; -fx-border-color: lightgray ");
		MenuItem item1 = new MenuItem("新建文件夹" );              //新建文件夹
		item1.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Directory directory = DocManage.openDirectory((DirEntry)entry);
				DirEntry dirEntry=DocManage.createDir(directory);//创建文件夹
				data =FXCollections.observableArrayList(directory.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				updateToShowDisk();
				seletNote.getChildren().add(new TreeItem<Entry>(dirEntry,new ImageView(new Image(("icon-folder2.png")))));
			}
			
		});
		
		MenuItem item2 = new MenuItem("新建文本文件");    //新建文本文件
		item2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				//pane.getChildren().add(new Button("ok"));
				Directory directory = DocManage.openDirectory((DirEntry)entry);
				DocEntry docEntry=DocManage.createDoc(directory);
				data =FXCollections.observableArrayList(directory.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				updateToShowDisk();
				//新建文本文件
				seletNote.getChildren().add(new TreeItem<Entry>(docEntry,new ImageView(new Image(("txt-icon3.png")))));
			}
			
		});
		
		//粘贴
		MenuItem item3 = new MenuItem("粘贴"); 
		item3.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent e){
						DocManage.copyText(copySpace, DocManage.openDirectory((DirEntry)entry));
						if(isCut){
							DocManage.deleteFile(copySpace,cutRootDir);
						}
						isCut=false;
						isCopy=false;
						data =FXCollections.observableArrayList(currentDir.getEntries());
						tableView.setItems(FXCollections.observableArrayList(data));
						updateToShowDisk();
						seletNote.getChildren().add(new TreeItem<Entry>(copySpace,new ImageView(new Image(("txt-icon3.png")))));
					}
				});

		//格式化
		MenuItem item4 = new MenuItem("格式化"); 
		item4.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent e){
						formatting();
					}
				});

		//判断是否有复制或者剪切
		if(isCopy||isCut)
			contextMenu.getItems().addAll(item1 , item2 , item3 , item4 );
		else
			contextMenu.getItems().addAll(item1 , item2 , item4 );
		return contextMenu;
	}
	//格式化界面
	public static void formatting(){
		Text text = new Text("是否确定进行格式化，此操作不可逆转，所以数据将消除");
		Stage stage = new Stage();
		Button yes = new Button("确定");
		yes.setOnAction(e->{
			DiskManage.clearDisk();
			DocManage.initial();
			currentDir = DocManage.rootDir;
			data.removeAll(data);
			tableView.setItems(FXCollections.observableArrayList(data));
			tableView.refresh();
			rootNode.getChildren().removeAll(rootNode.getChildren());
			treeView.refresh();
			updateToShowDisk();
			stage.close();
		});
		Button no = new Button("取消");
		no.setOnAction(e->stage.close());
		VBox vBox = new VBox(10);
		HBox hBox = new HBox(20);
		hBox.getChildren().addAll(yes,no);
		vBox.getChildren().addAll(text,hBox);
		Scene scene = new Scene(vBox);
		stage.setScene(scene);
		stage.show();
		
	}
	
	//-------------------------------------------------------
	//showinterface 列表界面------------------------------------
	public static TableView<Entry> showinterfaceRun(String path){
		Directory directory = DocManage.findFileDir(path);
		return showinterfaceRun(directory);
	}
	
	public static TableView<Entry> showinterfaceRun(Directory current){
		//设置当前文件夹
		currentDir=current;
		//tableView.setPadding(new Insets(15 , 10 , 10 , 25));
		//设置table view的两列
		TableColumn<Entry,String> first=new TableColumn<>("名称");
		first.setMinWidth(100);
		first.setEditable(true);
		first.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<Entry,String> second = new TableColumn<>("类型");
		second.setCellValueFactory(new PropertyValueFactory<>("type"));
		tableView.getColumns().addAll(first,second);
		//把数据载入table view
		data =FXCollections.observableArrayList(currentDir.getEntries());
		tableView.setItems(FXCollections.observableArrayList(data)); 
		//设置鼠标事件
		tableView.setOnMouseClicked(e->{
			if(e.getClickCount()!=0){
				seleted = tableView.getSelectionModel().getSelectedItem();
				if(seleted!=null){
					//如果是在项上的右键
					curDirNote=treeNodeFinder(path);
					seletNote =treeNodeFinder(seleted);
					if(seleted.isDir()){
						tableView.setContextMenu(dirClickMenu((DirEntry)seleted));
						
					}else{
						tableView.setContextMenu(rightClickMenu(seleted));
					}
					
					//System.out.println(seletNote);
				}else{
					tableView.setContextMenu(blankRightClick());
					curDirNote=treeNodeFinder(path);
					seletNote=null;
					//System.out.println(curDirNote);
				}
			} 
			if(e.getClickCount()==2){
				//System.out.println("Seleted Entry :"+seleted.getName());
				if(seleted.isDir()){
					Directory directory = DocManage.openDirectory((DirEntry)seleted);
					currentDir = directory;
					path+=directory.getName()+"\\";
					//update search block data
					searchTextField.setText(path);
					//update showinterface
					data =FXCollections.observableArrayList(currentDir.getEntries());
					tableView.setItems(FXCollections.observableArrayList(data));
					//expand tree graph
					curDirNote=treeNodeFinder(path);
					curDirNote.expandedProperty().set(true);
				}else{
					notpadRun((DocEntry)seleted);
				}
				
			}
		});
		tableView.setRowFactory(new Callback<TableView<Entry>, TableRow<Entry>>() {  
	        @Override  
	        public TableRow<Entry> call(TableView<Entry> tableView2) {  
	            final TableRow<Entry> row = new TableRow<>();  
	            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
	                @Override  
	                public void handle(MouseEvent event) {  
	                    final int index = row.getIndex();  
	                    if (index >= tableView.getItems().size()) {
	                        tableView.getSelectionModel().clearSelection();
	                        event.consume();  
	                    }
	                }  
	            });  
	            return row;  
	        }  
	    });  
		tableView.setFixedCellSize(25);
		tableView.setMinHeight(17*25+16);
		tableView.setMaxHeight(17*25+16);
		//tableView.setStyle("-fx-background-color:white");
		
		return tableView;
	}
	//--------------------------------------------------
	//diskVisual----------------------------------------

	/*
	 * 定义一个类来引用数据
	 */
	public static class readFAT {
	    private final SimpleStringProperty index;
	    private final SimpleStringProperty fat;
	     
	    private readFAT(String newIndex, String newFatValue) {
	        this.index = new SimpleStringProperty(newIndex);
	        this.fat = new SimpleStringProperty(newFatValue);
	    }
	 
	    public String getIndex() {
	        return index.get();
	    }
	    public void setIndex(String newIndex) {
	    	index.set(newIndex);
	    }
	        
	    public String getFat() {
	    	return fat.get();
	    }
	    public void setFat(String newFatValue) {
	    	fat.set(newFatValue);
	    }	    	
	}
	
	//引用数据模型来添加数据
	
	public static VBox diskVisualRun(){
		
		final Label label = new Label("磁盘可视化");
		label.setAlignment(Pos.CENTER);
		label.setFont(new Font("Arial", 20));
		
		TableColumn indexCol = new TableColumn("编号");
		indexCol.setMinWidth(100);
		indexCol.setCellValueFactory(
                new PropertyValueFactory<readFAT,String>("index")
                );//将readFAT的数据和表格列相关联
		
		TableColumn fatCol = new TableColumn("FAT表");
		fatCol.setMinWidth(100);
		fatCol.setCellValueFactory(
                new PropertyValueFactory<readFAT,String>("fat")
                );
		ArrayList<readFAT> list = new ArrayList<>();
		byte[] fat = DiskManage.getFat();//读取fat表
		String strIndex;
		String strFat;	
		for(int i = 0;i<256 ; i++){
			strIndex = String.valueOf(i);
			strFat = String.valueOf(fat[i]);	
			list.add(new readFAT(strIndex, strFat));	
		}
		//将list和data关联
		dataFAT =FXCollections.observableArrayList(list);
		table.setItems(dataFAT);//将数据添加到表格内
		table.getColumns().addAll(indexCol, fatCol);//把列添加到表格内
		table.setMinHeight(17*25+16);
		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.getChildren().addAll(table);
		
		//vbox.setPadding(new Insets(10, 0, 0, 10));
		return vbox;
    }
	//update data to show disk usage
	private static void updateToShowDisk(){
		ArrayList<readFAT> list = new ArrayList<>();
		byte[] fat = DiskManage.getFat();//读取fat表
		String strIndex;
		String strFat;	
		for(int i = 0;i<256 ; i++){
			strIndex = String.valueOf(i);
			strFat = String.valueOf(fat[i]);	
			list.add(new readFAT(strIndex, strFat));	
		}
		//将list和data关联
		dataFAT =FXCollections.observableArrayList(list);
		table.setItems(dataFAT);//将数据添加到表格内
		table.refresh();
	}
	//---------------------------------------------------
	//searchBlock----------------------------------------
	public static HBox searchBlock(){
		HBox hBox = new HBox();
		searchTextField.setOnKeyPressed(e->{
			if(e.getCode()==KeyCode.ENTER){
				path =searchTextField.getText();
				//update showinterface
				currentDir=DocManage.findFileDir(path);
				data =FXCollections.observableArrayList(currentDir.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				curDirNote =treeNodeFinder(path);
			}
		});
		Button button = new Button("ENTER");
		button.setOnAction(e->{
			path =searchTextField.getText();
			//update showinterface
			Directory directory = DocManage.rootDir;
			currentDir=DocManage.findFileDir(path);
			data =FXCollections.observableArrayList(currentDir.getEntries());
			tableView.setItems(FXCollections.observableArrayList(data));
			curDirNote = treeNodeFinder(path);
		});
		hBox.getChildren().addAll(searchTextField,button);
		return hBox;
	}

	//tree-----------------------------------------------
	public static VBox treeInterface(){
		  
			//生成树状图
			directoriesGenerate();
	        rootNode.setExpanded(true);
	        VBox box = new VBox();

	        treeView.setEditable(true);
	        
	       // treeView.setOnAction(e->openfile(stage));
	        treeView.setCellFactory((TreeView<Entry> p) ->new TextFieldTreeCellImpl());
	        //当树状图中的项被点击
	        treeView.getSelectionModel().selectedItemProperty().addListener(e->{
	        	//获得被点击的项
	        	Entry sEntry = treeView.getSelectionModel().getSelectedItem().getValue();
	        	if(sEntry!=null){
	        		//把被点击的项以及它所在文件夹存入静态变量
	        		seletNote= treeView.getSelectionModel().getSelectedItem();
	        		currentDir=DocManage.openDirectory(sEntry.getRoot().getEntry());
        			curDirNote=seletNote.getParent();
        			path=findNodePath(curDirNote);
        			searchTextField.setText(path);
	        		System.out.println("seleted node :"+seletNote.getValue().getName());
	        		System.out.println("Now list dir: "+currentDir+" , Now tree dir"+curDirNote.getValue());
	        		//ArrayList<Entry> list = new ArrayList<>();
	        		if(sEntry.isDir()){
	        			//如果是文件夹
	        			/*
	        			if(isCut)//当处于剪切状态时，把树状图的文件夹内容与列表对接
	        			for (int i = 0; i < seletNote.getChildren().size(); i++) {
							list.add(seletNote.getChildren().get(i).getValue());
						}*/
	        			//右键启动菜单
	        			treeView.setContextMenu(dirClickMenu((DirEntry)sEntry));
	        			if(sEntry.equals(DocManage.rootDir.getEntry())){
	        				//如果是对根目录右键，则修改菜单内容
	        				treeView.setContextMenu(rootClickMenu((DirEntry)sEntry));
	        			}
	        		}else{
	        			//树状节点被点击时
	        			treeView.setContextMenu(rightClickMenu(seletNote.getValue()));
	        			
	        			/*
	        			if(isCut)//当处于剪切状态时，把树状图的文件夹内容与列表对接
	        			for (int i = 0; i < seletNote.getParent().getChildren().size(); i++) {
							list.add(seletNote.getParent().getChildren().get(i).getValue());
						}*/
	        		}
	        		//进行界面
	        		//若不是在进行剪切
	        		if(!isCut){
	        			//如果不是剪切，直接读取文件内容
	        			///if(currentDir!=null)
	        			System.out.println("Normal");
	        			System.out.println(data+" ");
	        			data =FXCollections.observableArrayList(currentDir.getEntries());	        			
	        		}else{
	        			//如果是剪切，使用树状图的列表
	        			//data =FXCollections.observableArrayList(list);
	        		}
	        		//更新列表数据
	        		tableView.setItems(FXCollections.observableArrayList(data));
	        		tableView.refresh();
	        		
	        		 
	        	}
	        	
	        });
	        treeView.setOnMouseClicked(e->{
	        	if(e.getButton().equals(MouseButton.PRIMARY)){
	                if(e.getClickCount() == 2){
	                	//如果项被双击
	                	if(!seletNote.getValue().isDir()){
	                		//项是文本，就打开文件
	                		DocEntry opEntry = (DocEntry) seletNote.getValue();
	              	  		notpadRun((opEntry));
	                	}
	                	
	                }
	            }
	        });
	        treeView.setMinHeight(17*25+16);
	        Text text1=new Text("C.A.T.A.L.O.G");
	        text1.setFont(Font.font("Verdana",20));
	        text1.setFill(Color.BROWN);
	        box.getChildren().addAll(treeView);
	        return box;
	  }
	//生成列表
	private static void directoriesGenerate(){
	   Directory rootDir = DocManage.rootDir;
	   //设置根目录，和当前目录
	   rootNode=new TreeItem<>(DocManage.rootDir.getEntry(),rootIcon);
	   curDirNote=rootNode;
		treeView = new TreeView<>(rootNode);
		//递归生成树状图
	   iterateDir(rootDir,rootNode);
 }
	//生成树的递归函数
	private static  void iterateDir(Directory directory,TreeItem<Entry> root){
	   //获得当前目录的项列表
		ArrayList<Entry> list = directory.getEntries();
	   for (int i = 0; i < list.size(); i++) {
		   //遍历所有的项，暂存在temp中
		   Entry temp = list.get(i);
		   //用temp生成树节点
		   TreeItem<Entry> empLeaf = new TreeItem<>(temp);
		   //项加入文件夹和文本图标
		   if(temp.isDir())
			   empLeaf.setGraphic(new ImageView(new Image(("icon-folder2.png"))));
		   else
			   empLeaf.setGraphic(new ImageView(new Image(("txt-icon3.png"))));
		   //当前文件夹节点加入新节点
		   root.getChildren().add(empLeaf);
		   //如果项是文件夹
		if(temp.isDir()){
			//打开文件夹
			Directory dir = DocManage.openDirectory((DirEntry)list.get(i));
			//利用新打开文件夹继续递归生成树
			iterateDir(dir,empLeaf);
		}
	}
 }
	private static final class TextFieldTreeCellImpl extends TreeCell<Entry> {
		
      public TextFieldTreeCellImpl() {           
    	
      }
      
      
      @Override
      public void startEdit() {
          super.startEdit();
         /* if(getItem() instanceof DocEntry){
        	  DocEntry opEntry = (DocEntry) getItem();
        	  notpadRun((opEntry));
          }*/
      }

      @Override
      public void cancelEdit() {
          super.cancelEdit();
          setText( getItem().getName());
          setGraphic(getTreeItem().getGraphic());
      }

      @Override
      public void updateItem(Entry item, boolean empty) {
      	 super.updateItem(item, empty);
      	 if (empty) {
             setText(null);
             setGraphic(null);
             //
         } else {
             {
                 setText(getString());
                 setGraphic(getTreeItem().getGraphic());
                 if (
                     getTreeItem().getParent()!= null&&getItem().isDir()
                 ){
                	 //文件夹操作
                	// setContextMenu(blackMenu);
                 }else{
                	 //setContextMenu(rightClickMenu(getTreeItem().getValue()));
                 }
             }
         }
      }
      private String getString() {
          return getItem() == null ? "" : getItem().toString();
      }
  }
	//利用项来寻找对应树状图中的树节点
	private static TreeItem<Entry> treeNodeFinder(Entry entry){
		TreeItem<Entry> result = curDirNote;
		//获得当前目录的列表
		ObservableList<TreeItem<Entry>> treeItems =curDirNote.getChildren();
		for (int i = 0; i < treeItems.size(); i++) {
			TreeItem<Entry> temp =treeItems.get(i);
			//如果列表内元素与所寻找元素名称相同
			if(temp.getValue().getName().equals(entry.getName())){
				//文件属性相同
				if(temp.getValue().isDir()==entry.isDir())
					result =temp;
					break;
			}
		}
		
		return result;
	}
	//利用文本路径来寻找对应树状图中的树节点
	private static TreeItem<Entry> treeNodeFinder(String path){
		String[] liStrings = path.split("\\\\");
		ObservableList<TreeItem<Entry>> treeItems =rootNode.getChildren();
		TreeItem<Entry> result=rootNode;
		if(!liStrings[0].equals("root:")){
			System.err.println("路径出错");
		}
		for (int i = 1; i < liStrings.length; i++) {
			//for each path item in string
			for (int j = 0; j < treeItems.size(); j++) {
				Entry temp = treeItems.get(j).getValue();
				//if item name match 
				if(temp.getName().equals(liStrings[i])){
					//if it's a directory
					if(temp.isDir()){
						result = treeItems.get(j);
						treeItems = result.getChildren();
						break;
					}
					
				}else{
					//if the last item is not match
					if(j==treeItems.size()-1){
						return result;
					}
				}
			}
		}
		return result;
	}
	//利用树节点生成文件路径
	private static String findNodePath(TreeItem<Entry> node){
		String result="";
		if(node!=null)
		while(!node.getValue().equals(DocManage.rootDir.getEntry())){
			result=node.getValue().getName()+"\\"+result;
			node = node.getParent();
		}
		result = "root:\\"+result;
		return result;
	}
}
