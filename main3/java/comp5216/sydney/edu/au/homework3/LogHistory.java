package comp5216.sydney.edu.au.homework3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LogHistory extends AppCompatActivity{
    ListView listView1;
    ListView listView2;
    ArrayList<RunningRecords> items;
    ArrayList<RunningRecords> averageItems;
    ArrayList<RunningRecords> shownItems;
    RecordItemDao recordItemDao;
    RecordItemDB db;
    RecordsAdapter recordsAdapter1;
    RecordsAdapter recordsAdapter2;
    Date d;
    int week;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_history);

        listView1 = (ListView) findViewById(R.id.listView1);
        listView2 = (ListView) findViewById(R.id.listView2);

        db = RecordItemDB.getDatabase(this.getApplication().getApplicationContext());
        recordItemDao = db.recordItemDao();
        readItemsFromDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        averageItems = new ArrayList<>();
        shownItems = new ArrayList<>();
        for(int i=0;i<55;i++){
            averageItems.add(new RunningRecords("",0,0,0,0));
        }
        for(int i=0;i<items.size();i++){
            try{
                d=sdf.parse(items.get(i).getRundate());
            }catch (Exception e){}
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar.setTime(d);
            week=calendar.get(Calendar.WEEK_OF_YEAR);
            averageItems.set(week,new RunningRecords("week"+week+"",averageItems.get(week).getRuntime()+items.get(i).getRuntime(),averageItems.get(week).getDistance()+items.get(i).getDistance(),0,0));
        }
        for(int i=0;i<55;i++){
            if(averageItems.get(i).getDistance()>0){
                String rundate=averageItems.get(i).getRundate();
                double runtime=averageItems.get(i).getRuntime();
                double distance=averageItems.get(i).getDistance();
                double pace=(Math.round(runtime/distance*100))/100.0;
                double speed=(Math.round(distance/runtime*60*100))/100.0;
                shownItems.add(new RunningRecords(rundate,runtime,distance,pace,speed));
            }
        }

        recordsAdapter2 = new RecordsAdapter(this,shownItems);
        listView2.setAdapter(recordsAdapter2);

        recordsAdapter1 = new RecordsAdapter(this,items);
        listView1.setAdapter(recordsAdapter1);


    }

    public void back(View v){
        finish();
    }

    private void readItemsFromDatabase()
    {
//Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
//read items from database
                    List<RecordItem> itemsFromDB = recordItemDao.listAll();
                    items = new ArrayList<RunningRecords>();
                    if (itemsFromDB != null & itemsFromDB.size() > 0) {
                        for (RecordItem item : itemsFromDB) {
                            items.add(new RunningRecords(item.getRundate(),item.getRuntime(),item.getDistance(),item.getPace(),item.getSpeed()));
                        }
                    }
                    return null;
                }
            }.execute().get();
        }
        catch(Exception ex) {}
    }
    private void saveItemsToDatabase()
    {
//Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
//delete all items and re-insert
                recordItemDao.deleteAll();
                for (RunningRecords todo : items) {
                    RecordItem item = new RecordItem(todo.getRundate(),todo.getRuntime(),todo.getDistance(),todo.getPace(),todo.getSpeed());
                    recordItemDao.insert(item);
                }
                return null;
            }
        }.execute();
    }
}
