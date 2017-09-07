package ui;

import javafx.beans.property.SimpleStringProperty;

public class Catalogs {
	 private  SimpleStringProperty name;
     private  SimpleStringProperty part;

	public Catalogs(String name, String part) {
         this.name = new SimpleStringProperty(name);
         this.part = new SimpleStringProperty(part);
     }

     public String getName() {
         return name.get();
     }

     public void setName(String Name) {
         name.set(Name);
     }

     public String getPart() {
         return part.get();
     }

     public void setPart(String Name) {
         part.set(Name);
     }
}
