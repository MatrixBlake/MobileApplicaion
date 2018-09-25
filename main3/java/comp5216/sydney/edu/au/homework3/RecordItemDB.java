package comp5216.sydney.edu.au.homework3;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {RecordItem.class}, version = 1, exportSchema = false)
public abstract class RecordItemDB extends RoomDatabase {
    private static final String DATABASE_NAME = "recorditem_db";
    private static RecordItemDB DBINSTANCE;

    public abstract RecordItemDao recordItemDao();

    public static RecordItemDB getDatabase(Context context) {
        if (DBINSTANCE == null) {
            synchronized (RecordItemDB.class) {
                DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        RecordItemDB.class, DATABASE_NAME).build();
            }
        }
        return DBINSTANCE;
    }

    public static void destroyInstance() {
        DBINSTANCE = null;
    }
}

