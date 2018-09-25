package comp5216.sydney.edu.au.homework3;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecordItemDao {
    @Query("SELECT * FROM recordlist")
    List<RecordItem> listAll();

    @Insert
    void insert(RecordItem recordItem);

    @Query("DELETE FROM recordlist")
    void deleteAll();
}
