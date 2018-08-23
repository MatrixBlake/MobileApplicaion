package comp5216.sydney.edu.au.homework1;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "todolist")
public class ToDoItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "toDoItemID")
    private int toDoItemID;

    @ColumnInfo(name = "toDoItemName")
    private String toDoItemName;

    @ColumnInfo(name = "toDoItemDate")
    private String toDoItemDate;

    public ToDoItem(String toDoItemName, String toDoItemDate){
        this.toDoItemName = toDoItemName;
        this.toDoItemDate = toDoItemDate;
    }

    public int getToDoItemID() {
        return toDoItemID;
    }

    public void setToDoItemID(int toDoItemID) {
        this.toDoItemID = toDoItemID;
    }

    public String getToDoItemName() {
        return toDoItemName;
    }

    public String getToDoItemDate(){return toDoItemDate;}

    public void setToDoItemName(String toDoItemName) {
        this.toDoItemName = toDoItemName;
    }

    public void setToDoItemDate(String toDoItemDate) {this.toDoItemDate = toDoItemDate;}
}
