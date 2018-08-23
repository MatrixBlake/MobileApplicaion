package comp5216.sydney.edu.au.homework1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.util.List;



import java.util.*;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<ItemDate> items;
    ItemsAdapter itemsAdapter;
    ToDoItemDB db;
    ToDoItemDao toDoItemDao;
    public final int EDIT_ITEM_REQUEST_CODE = 647;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lstView);


//        items=new ArrayList<ItemDate>();
//        ItemDate item1 = new ItemDate("test1","date1");
//        ItemDate item2 = new ItemDate("test2","date2");
//        items.add(item1);
//        items.add(item2);

        db = ToDoItemDB.getDatabase(this.getApplication().getApplicationContext());
        toDoItemDao = db.toDoItemDao();
        readItemsFromDatabase();


        itemsAdapter = new ItemsAdapter(this,items);

        listView.setAdapter(itemsAdapter);

        Log.i("MainActivity",itemsAdapter.getCount()+"");

        setupListViewListener();

    }

    public void onAddItemClick(View view) {
        Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);

        intent.putExtra("item", "");
        intent.putExtra("date", "");
        intent.putExtra("position", itemsAdapter.getCount());
        // brings up the second activity
        startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);

        itemsAdapter.notifyDataSetChanged();
    }

    private void setupListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long rowId)
            {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        items.remove(position); // Remove item from the ArrayList
                                        itemsAdapter.notifyDataSetChanged(); // Notify listView adapte to update the list
                                        saveItemsToDatabase();
                                    }
                                })
                        .setNegativeButton(R.string.cancel, new  DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                // User cancelled the dialog
                // Nothing happens
                                    }
                                });
                builder.create().show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemDate updateItem =  itemsAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item " + position + ": " + updateItem.getName());
                Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("item", updateItem.getName());
                    intent.putExtra("date", updateItem.getDate());
                    intent.putExtra("position", position);
                    // brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                    itemsAdapter.notifyDataSetChanged();
                    saveItemsToDatabase();
                }
            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
// Extract name value from result extras
                String editedName = data.getExtras().getString("item");
                String editedDate = data.getExtras().getString("date");
                int position = data.getIntExtra("position", -1);
                boolean added=false;
                if(position>=items.size()){
                    items.add(new ItemDate("",""));
                    added=true;
                }
                for(int i=position;i>0;i--){
                    items.set(i,items.get(i-1));
                }
                items.set(0,new ItemDate(editedName,editedDate));
                //items.set(position, new ItemDate(editedName,editedDate));
                if(added==false){
                    Log.i("Updated Item in list:", editedName + ",position:" + position);
                    Toast.makeText(this, "updated:" + editedName, Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("Created Item in list:", editedName + ",position:" + position);
                    Toast.makeText(this, "created:" + editedName, Toast.LENGTH_SHORT).show();
                }
                itemsAdapter.notifyDataSetChanged();
                saveItemsToDatabase();
            }
        }
    }

    private void readItemsFromDatabase()
    {
//Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
//read items from database
                    List<ToDoItem> itemsFromDB = toDoItemDao.listAll();
                    items = new ArrayList<ItemDate>();
                    if (itemsFromDB != null & itemsFromDB.size() > 0) {
                        for (ToDoItem item : itemsFromDB) {
                            items.add(new ItemDate(item.getToDoItemName(),item.getToDoItemDate()));
                            Log.i("SQLite read item", "ID: " + item.getToDoItemID() + " Name: " + item.getToDoItemName());
                        }
                    }
                    return null;
                }
            }.execute().get();
        }
        catch(Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }
    private void saveItemsToDatabase()
    {
//Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
//delete all items and re-insert
                toDoItemDao.deleteAll();
                for (ItemDate todo : items) {
                    ToDoItem item = new ToDoItem(todo.getName(),todo.getDate());
                    toDoItemDao.insert(item);
                    Log.i("SQLite saved item", todo.getName());
                }
                return null;
            }
        }.execute();
    }
}
