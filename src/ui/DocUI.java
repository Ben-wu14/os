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
	public static String path="root:\\";	//������·��
	public static Directory currentDir=DocManage.rootDir;	//Ŀǰ�����ļ���
	public static DocEntry copySpace=null;		//���Ƶ��ĵ�
	public static ObservableList<Entry> data;	//�б������
	static TableView<Entry> tableView = new TableView<>();	//�б�Ķ���
	private static Entry seleted;				//�б��б�ѡ�е���
	private static boolean isCopy=false;		//�Ƿ��ڸ���״̬
	private static boolean isCut=false;			//�Ƿ��ڼ���״̬
	private static Directory cutRootDir=null;	//�������ļ����ڵ��ļ���
	private static TableView<readFAT> table = new TableView<readFAT>();	//��ʾ����״̬���б�
	private static ObservableList<readFAT> dataFAT;	//��ʾ����״̬������
	
	//��Ŀ¼����
		private static Node rootIcon=new ImageView(new Image(("/Ŀ¼.png")));//��Ŀ¼���õ�ͼƬ
		
	    private static  TreeItem<Entry> rootNode;	//��Ŀ¼�ڵ�
	    private static TreeView<Entry> treeView;	//��״ͼ�Ķ���
	    private static TreeItem<Entry> seletNote;	//��ѡ�е���״ͼ�ڵ�
	    //private static TreeItem<Entry> copyNote;
	    private static TreeItem<Entry> curDirNote;	//��ѡ�е���״ͼ�ڵ����ڵ�Ŀ¼
	//ͬ�����
	private static TextField searchTextField = new TextField(path);//������
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		DocManage.initial();	//��ʼ������
		BorderPane borderPane = new BorderPane();
		HBox topPane =searchBlock();
		VBox rightPane = diskVisualRun();
		VBox leftPane = treeInterface();
		HBox centrePane =new HBox(showinterfaceRun(path));
		
		//���þ���
		topPane.setAlignment(Pos.CENTER);
		//���ñ߽�
		topPane.setPadding(new Insets(20));
		
		HBox conectBox = new HBox();
		conectBox.getChildren().addAll(leftPane,centrePane,rightPane);
		conectBox.setAlignment(Pos.CENTER);
		//����������ĳ���
		searchTextField.setMinWidth(conectBox.getWidth());
		//�������
		borderPane.setTop(topPane);
		borderPane.setCenter(conectBox);
		Scene scene = new Scene(borderPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("�ĵ�����");
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	//NotePad ���±�����-------------------------------------------
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
		
        // --- Menu ����
        Menu menuDoc = new Menu("�ļ�");
        MenuItem menuSave=new MenuItem("����");
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
        Menu menuStyle = new Menu("�����С");
        
        MenuItem large = new MenuItem("����");   //�����С
        large.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		textField.setFont(Font.font(40));
        	}
        });
        
        MenuItem middle = new MenuItem("�к�");
        middle.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		textField.setFont(Font.font(30));
        	}
        });
        		
        MenuItem small = new MenuItem("С��");
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
	//�Ҽ��˵�����------------------------------------
	//�б�հ״��Ҽ�
	public static ContextMenu blankRightClick(){
		ContextMenu contextMenu = new ContextMenu();   //�հ״��Ҽ��˵�
		contextMenu.setStyle("-fx-background-color:rgb(240 , 240 , 240 , 0.95) ; -fx-border-color: lightgray ");
		MenuItem item1 = new MenuItem("�½��ļ���" );              //�½��ļ���
		item1.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				DirEntry dirEntry=DocManage.createDir(currentDir);//�����ļ���
				data =FXCollections.observableArrayList(currentDir.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				updateToShowDisk();
				curDirNote.getChildren().add(new TreeItem<Entry>(dirEntry));
			}
			
		});
		
		MenuItem item2 = new MenuItem("�½��ı��ļ�");    //�½��ı��ļ�
		item2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				//pane.getChildren().add(new Button("ok"));
				DocEntry docEntry=DocManage.createDoc(currentDir);
				data =FXCollections.observableArrayList(currentDir.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				updateToShowDisk();
				//�½��ı��ļ�
				curDirNote.getChildren().add(new TreeItem<Entry>(docEntry));
			}
			
		});
		
		MenuItem item4 = new MenuItem("ճ��");
		item4.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				//�Ѽ����ռ���ı��ļ����Ƶ���ǰ�ļ���
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
		//�Ƿ��и���
		if(isCopy||isCut)	contextMenu.getItems().addAll(item1,item2,item4);
		else contextMenu.getItems().addAll(item1,item2);
		return contextMenu;
	}
	//�������Ҽ�
	private static ContextMenu rightClickMenu(Entry entry){
	  		ContextMenu contextMenu = new ContextMenu();   //�Ҽ��˵�
			contextMenu.setStyle("-fx-background-color:rgb(240 , 240 , 240 , 0.95) ; -fx-border-color: lightgray ");
			MenuItem item1 = new MenuItem("��");
			item1.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent e){
					if(entry.isDir()){
						//�򿪱�ѡ�е��ļ���
						Directory directory = DocManage.openDirectory((DirEntry)seleted);
						currentDir = directory;
						//��������������
						path+=directory.getName()+"\\";
						searchTextField.setText(path);
						//�����б�����
						data =FXCollections.observableArrayList(currentDir.getEntries());
						tableView.setItems(FXCollections.observableArrayList(data));
					}else{
						//������ı��ļ�������ļ�
						notpadRun((DocEntry)entry);
					}
					
				}
				
			});

			//����
			MenuItem item2 = new MenuItem("����"); 
			item2.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent e){
					//��������ļ��У���ѡ�������copySpace
					if(!entry.isDir())
					copySpace=(DocEntry)entry;
					isCopy=true;
				}
			});
			
			//ճ��
			MenuItem item3 = new MenuItem("ճ��"); 
			item3.setOnAction(new EventHandler<ActionEvent>(){
						@Override
						public void handle(ActionEvent e){
							//�Ĵ渴�ƺ��µ�entry
							Entry copyed=null;
							if(entry.isDir()){
								//������ļ��У�ճ����ѡ�е��ļ�����
								if(isCut)
									DocManage.cutText(copySpace, DocManage.openDirectory((DirEntry)entry));
								else
									copyed=DocManage.copyText(copySpace, DocManage.openDirectory((DirEntry)entry));
							}else{
								//������ļ���ճ����ѡ���ļ����ڵ��ļ���
								if(isCut)
									DocManage.cutText(copySpace,currentDir);
								else
									copyed=DocManage.copyText(copySpace,currentDir);
							}
							//������״ͼ����
							if(isCut)
								seletNote.getParent().getChildren().add(new TreeItem<Entry>(copySpace,new ImageView(new Image(("txt-icon3.png")))));
							else 
								seletNote.getParent().getChildren().add(new TreeItem<Entry>(copyed,new ImageView(new Image(("txt-icon3.png")))));
							isCut=false;
							isCopy=false;
							//�����б�ʹ�������Ľ���
							data =FXCollections.observableArrayList(currentDir.getEntries());
							tableView.setItems(FXCollections.observableArrayList(data));
							updateToShowDisk();
							
						}
					});

			//ɾ��
			MenuItem item4 = new MenuItem("ɾ��"); 
			item4.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent e){
					   //���б����Ƴ���
					   tableView.getItems().remove(entry);
					   //ͨ���ļ������Ӵ��̳���ɾ���ļ�
					   System.out.println("ɾ��"+entry+"�Ƿ�ɹ�"+DocManage.deleteFile(entry, currentDir));
					   //���½���
					   data =FXCollections.observableArrayList(currentDir.getEntries());
					   tableView.setItems(FXCollections.observableArrayList(data));
					   updateToShowDisk();
					   seletNote.getParent().getChildren().remove(seletNote);
				}
			});

			//����
			MenuItem item5 = new MenuItem("����");
			item5.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent e){
					//���������е�������ڵ��ļ���
					copySpace=(DocEntry)entry;
					cutRootDir=currentDir;
					//�Ƴ��ļ����ǲ������������
					tableView.getItems().remove(entry);
					System.out.println("ɾ��"+entry+"�Ƿ�ɹ�"+DocManage.removeFile(entry, currentDir));
					//���½���
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
			
			//������
			MenuItem item6 = new MenuItem("������");
			item6.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent e){
					renameStage(entry);
				}
			});
			//�ж��Ƿ��и��ƻ��߼���
			if(isCopy||isCut)
				contextMenu.getItems().addAll(item1 , item2 , item3 , item4 , item5,item6);
			else
				contextMenu.getItems().addAll(item1 , item2 , item4 , item5 ,item6);
			//contextMenu.getItems().addAll(item1 , item2 , item3 , item4 ,item6);
			return contextMenu;
	  	}
	//����������
	public static void renameStage(Entry entry){
			TextField textField = new TextField(entry.getName());
			Stage stage = new Stage();
			Button save = new Button("����");
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
			Button cancle = new Button("ȡ��");
			cancle.setOnAction(e->stage.close());
			VBox vBox=new VBox(10);
			HBox hBox= new HBox(10);
			hBox.getChildren().addAll(save,cancle);
			vBox.getChildren().addAll(textField,hBox);
			Scene scene = new Scene(vBox);
			stage.setScene(scene);
			stage.show();
		}
	//�ļ��в���
	private static ContextMenu dirClickMenu(DirEntry entry){
		ContextMenu contextMenu = new ContextMenu();   //�Ҽ��˵�
		contextMenu.setStyle("-fx-background-color:rgb(240 , 240 , 240 , 0.95) ; -fx-border-color: lightgray ");
		MenuItem item1 = new MenuItem("�½��ļ���" );              //�½��ļ���
		item1.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Directory directory = DocManage.openDirectory((DirEntry)entry);
				DirEntry dirEntry=DocManage.createDir(directory);//�����ļ���
				data =FXCollections.observableArrayList(directory.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				updateToShowDisk();
				seletNote.getChildren().add(new TreeItem<Entry>(dirEntry,new ImageView(new Image(("icon-folder2.png")))));
			}
			
		});
		
		MenuItem item2 = new MenuItem("�½��ı��ļ�");    //�½��ı��ļ�
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
				//�½��ı��ļ�
				seletNote.getChildren().add(new TreeItem<Entry>(docEntry,new ImageView(new Image(("txt-icon3.png")))));
			}
			
		});
		
		
		MenuItem item3 = new MenuItem("��");
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

		//ճ��
		MenuItem item4 = new MenuItem("ճ��"); 
		item4.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent e){
						//���и��ƣ���ø���������entry
						Entry copyed=DocManage.copyText(copySpace, DocManage.openDirectory((DirEntry)entry));
						if(isCut){
							DocManage.deleteFile(copySpace,cutRootDir);
						}
						isCut=false;
						isCopy=false;
						data =FXCollections.observableArrayList(currentDir.getEntries());
						tableView.setItems(FXCollections.observableArrayList(data));
						updateToShowDisk();
						//���µ�entry������״ͼ��
						seletNote.getChildren().add(new TreeItem<Entry>(copyed,new ImageView(new Image(("txt-icon3.png")))));
					}
				});

		//ɾ��
		MenuItem item5 = new MenuItem("ɾ��"); 
		item5.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				   Directory directory = DocManage.openDirectory(entry);
				   if(!directory.isEmpty()){
					   System.out.println("ɾ��ʧ��");
				   }else{
					   tableView.getItems().remove(entry);
					   System.out.println("ɾ��"+entry+"�Ƿ�ɹ�"+DocManage.deleteFile(entry, currentDir));
					   data =FXCollections.observableArrayList(currentDir.getEntries());
					   tableView.setItems(FXCollections.observableArrayList(data));
					   updateToShowDisk();
					   seletNote.getParent().getChildren().remove(seletNote);
				   }
				   
			}
		});

		//������
		MenuItem item6 = new MenuItem("������");
		item6.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				renameStage(entry);
			}
		});
		//�ж��Ƿ��и��ƻ��߼���
		if(isCopy||isCut)
			contextMenu.getItems().addAll(item1 , item2 , item3 , item4 , item5,item6);
		else
			contextMenu.getItems().addAll(item1 , item2 , item3 , item5 ,item6);
		//contextMenu.getItems().addAll(item1 , item2 , item3 , item4 ,item6);
		return contextMenu;
	}
	//���̲���
	private static ContextMenu rootClickMenu(DirEntry entry){
		ContextMenu contextMenu = new ContextMenu();   //�Ҽ��˵�
		contextMenu.setStyle("-fx-background-color:rgb(240 , 240 , 240 , 0.95) ; -fx-border-color: lightgray ");
		MenuItem item1 = new MenuItem("�½��ļ���" );              //�½��ļ���
		item1.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Directory directory = DocManage.openDirectory((DirEntry)entry);
				DirEntry dirEntry=DocManage.createDir(directory);//�����ļ���
				data =FXCollections.observableArrayList(directory.getEntries());
				tableView.setItems(FXCollections.observableArrayList(data));
				updateToShowDisk();
				seletNote.getChildren().add(new TreeItem<Entry>(dirEntry,new ImageView(new Image(("icon-folder2.png")))));
			}
			
		});
		
		MenuItem item2 = new MenuItem("�½��ı��ļ�");    //�½��ı��ļ�
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
				//�½��ı��ļ�
				seletNote.getChildren().add(new TreeItem<Entry>(docEntry,new ImageView(new Image(("txt-icon3.png")))));
			}
			
		});
		
		//ճ��
		MenuItem item3 = new MenuItem("ճ��"); 
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

		//��ʽ��
		MenuItem item4 = new MenuItem("��ʽ��"); 
		item4.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent e){
						formatting();
					}
				});

		//�ж��Ƿ��и��ƻ��߼���
		if(isCopy||isCut)
			contextMenu.getItems().addAll(item1 , item2 , item3 , item4 );
		else
			contextMenu.getItems().addAll(item1 , item2 , item4 );
		return contextMenu;
	}
	//��ʽ������
	public static void formatting(){
		Text text = new Text("�Ƿ�ȷ�����и�ʽ�����˲���������ת���������ݽ�����");
		Stage stage = new Stage();
		Button yes = new Button("ȷ��");
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
		Button no = new Button("ȡ��");
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
	//showinterface �б����------------------------------------
	public static TableView<Entry> showinterfaceRun(String path){
		Directory directory = DocManage.findFileDir(path);
		return showinterfaceRun(directory);
	}
	
	public static TableView<Entry> showinterfaceRun(Directory current){
		//���õ�ǰ�ļ���
		currentDir=current;
		//tableView.setPadding(new Insets(15 , 10 , 10 , 25));
		//����table view������
		TableColumn<Entry,String> first=new TableColumn<>("����");
		first.setMinWidth(100);
		first.setEditable(true);
		first.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<Entry,String> second = new TableColumn<>("����");
		second.setCellValueFactory(new PropertyValueFactory<>("type"));
		tableView.getColumns().addAll(first,second);
		//����������table view
		data =FXCollections.observableArrayList(currentDir.getEntries());
		tableView.setItems(FXCollections.observableArrayList(data)); 
		//��������¼�
		tableView.setOnMouseClicked(e->{
			if(e.getClickCount()!=0){
				seleted = tableView.getSelectionModel().getSelectedItem();
				if(seleted!=null){
					//����������ϵ��Ҽ�
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
	 * ����һ��������������
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
	
	//��������ģ�����������
	
	public static VBox diskVisualRun(){
		
		final Label label = new Label("���̿��ӻ�");
		label.setAlignment(Pos.CENTER);
		label.setFont(new Font("Arial", 20));
		
		TableColumn indexCol = new TableColumn("���");
		indexCol.setMinWidth(100);
		indexCol.setCellValueFactory(
                new PropertyValueFactory<readFAT,String>("index")
                );//��readFAT�����ݺͱ���������
		
		TableColumn fatCol = new TableColumn("FAT��");
		fatCol.setMinWidth(100);
		fatCol.setCellValueFactory(
                new PropertyValueFactory<readFAT,String>("fat")
                );
		ArrayList<readFAT> list = new ArrayList<>();
		byte[] fat = DiskManage.getFat();//��ȡfat��
		String strIndex;
		String strFat;	
		for(int i = 0;i<256 ; i++){
			strIndex = String.valueOf(i);
			strFat = String.valueOf(fat[i]);	
			list.add(new readFAT(strIndex, strFat));	
		}
		//��list��data����
		dataFAT =FXCollections.observableArrayList(list);
		table.setItems(dataFAT);//��������ӵ������
		table.getColumns().addAll(indexCol, fatCol);//������ӵ������
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
		byte[] fat = DiskManage.getFat();//��ȡfat��
		String strIndex;
		String strFat;	
		for(int i = 0;i<256 ; i++){
			strIndex = String.valueOf(i);
			strFat = String.valueOf(fat[i]);	
			list.add(new readFAT(strIndex, strFat));	
		}
		//��list��data����
		dataFAT =FXCollections.observableArrayList(list);
		table.setItems(dataFAT);//��������ӵ������
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
		  
			//������״ͼ
			directoriesGenerate();
	        rootNode.setExpanded(true);
	        VBox box = new VBox();

	        treeView.setEditable(true);
	        
	       // treeView.setOnAction(e->openfile(stage));
	        treeView.setCellFactory((TreeView<Entry> p) ->new TextFieldTreeCellImpl());
	        //����״ͼ�е�����
	        treeView.getSelectionModel().selectedItemProperty().addListener(e->{
	        	//��ñ��������
	        	Entry sEntry = treeView.getSelectionModel().getSelectedItem().getValue();
	        	if(sEntry!=null){
	        		//�ѱ���������Լ��������ļ��д��뾲̬����
	        		seletNote= treeView.getSelectionModel().getSelectedItem();
	        		currentDir=DocManage.openDirectory(sEntry.getRoot().getEntry());
        			curDirNote=seletNote.getParent();
        			path=findNodePath(curDirNote);
        			searchTextField.setText(path);
	        		System.out.println("seleted node :"+seletNote.getValue().getName());
	        		System.out.println("Now list dir: "+currentDir+" , Now tree dir"+curDirNote.getValue());
	        		//ArrayList<Entry> list = new ArrayList<>();
	        		if(sEntry.isDir()){
	        			//������ļ���
	        			/*
	        			if(isCut)//�����ڼ���״̬ʱ������״ͼ���ļ����������б�Խ�
	        			for (int i = 0; i < seletNote.getChildren().size(); i++) {
							list.add(seletNote.getChildren().get(i).getValue());
						}*/
	        			//�Ҽ������˵�
	        			treeView.setContextMenu(dirClickMenu((DirEntry)sEntry));
	        			if(sEntry.equals(DocManage.rootDir.getEntry())){
	        				//����ǶԸ�Ŀ¼�Ҽ������޸Ĳ˵�����
	        				treeView.setContextMenu(rootClickMenu((DirEntry)sEntry));
	        			}
	        		}else{
	        			//��״�ڵ㱻���ʱ
	        			treeView.setContextMenu(rightClickMenu(seletNote.getValue()));
	        			
	        			/*
	        			if(isCut)//�����ڼ���״̬ʱ������״ͼ���ļ����������б�Խ�
	        			for (int i = 0; i < seletNote.getParent().getChildren().size(); i++) {
							list.add(seletNote.getParent().getChildren().get(i).getValue());
						}*/
	        		}
	        		//���н���
	        		//�������ڽ��м���
	        		if(!isCut){
	        			//������Ǽ��У�ֱ�Ӷ�ȡ�ļ�����
	        			///if(currentDir!=null)
	        			System.out.println("Normal");
	        			System.out.println(data+" ");
	        			data =FXCollections.observableArrayList(currentDir.getEntries());	        			
	        		}else{
	        			//����Ǽ��У�ʹ����״ͼ���б�
	        			//data =FXCollections.observableArrayList(list);
	        		}
	        		//�����б�����
	        		tableView.setItems(FXCollections.observableArrayList(data));
	        		tableView.refresh();
	        		
	        		 
	        	}
	        	
	        });
	        treeView.setOnMouseClicked(e->{
	        	if(e.getButton().equals(MouseButton.PRIMARY)){
	                if(e.getClickCount() == 2){
	                	//����˫��
	                	if(!seletNote.getValue().isDir()){
	                		//�����ı����ʹ��ļ�
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
	//�����б�
	private static void directoriesGenerate(){
	   Directory rootDir = DocManage.rootDir;
	   //���ø�Ŀ¼���͵�ǰĿ¼
	   rootNode=new TreeItem<>(DocManage.rootDir.getEntry(),rootIcon);
	   curDirNote=rootNode;
		treeView = new TreeView<>(rootNode);
		//�ݹ�������״ͼ
	   iterateDir(rootDir,rootNode);
 }
	//�������ĵݹ麯��
	private static  void iterateDir(Directory directory,TreeItem<Entry> root){
	   //��õ�ǰĿ¼�����б�
		ArrayList<Entry> list = directory.getEntries();
	   for (int i = 0; i < list.size(); i++) {
		   //�������е���ݴ���temp��
		   Entry temp = list.get(i);
		   //��temp�������ڵ�
		   TreeItem<Entry> empLeaf = new TreeItem<>(temp);
		   //������ļ��к��ı�ͼ��
		   if(temp.isDir())
			   empLeaf.setGraphic(new ImageView(new Image(("icon-folder2.png"))));
		   else
			   empLeaf.setGraphic(new ImageView(new Image(("txt-icon3.png"))));
		   //��ǰ�ļ��нڵ�����½ڵ�
		   root.getChildren().add(empLeaf);
		   //��������ļ���
		if(temp.isDir()){
			//���ļ���
			Directory dir = DocManage.openDirectory((DirEntry)list.get(i));
			//�����´��ļ��м����ݹ�������
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
                	 //�ļ��в���
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
	//��������Ѱ�Ҷ�Ӧ��״ͼ�е����ڵ�
	private static TreeItem<Entry> treeNodeFinder(Entry entry){
		TreeItem<Entry> result = curDirNote;
		//��õ�ǰĿ¼���б�
		ObservableList<TreeItem<Entry>> treeItems =curDirNote.getChildren();
		for (int i = 0; i < treeItems.size(); i++) {
			TreeItem<Entry> temp =treeItems.get(i);
			//����б���Ԫ������Ѱ��Ԫ��������ͬ
			if(temp.getValue().getName().equals(entry.getName())){
				//�ļ�������ͬ
				if(temp.getValue().isDir()==entry.isDir())
					result =temp;
					break;
			}
		}
		
		return result;
	}
	//�����ı�·����Ѱ�Ҷ�Ӧ��״ͼ�е����ڵ�
	private static TreeItem<Entry> treeNodeFinder(String path){
		String[] liStrings = path.split("\\\\");
		ObservableList<TreeItem<Entry>> treeItems =rootNode.getChildren();
		TreeItem<Entry> result=rootNode;
		if(!liStrings[0].equals("root:")){
			System.err.println("·������");
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
	//�������ڵ������ļ�·��
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
