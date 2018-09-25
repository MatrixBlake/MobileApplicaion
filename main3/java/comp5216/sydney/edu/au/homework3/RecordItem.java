package comp5216.sydney.edu.au.homework3;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "recordlist")
public class RecordItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "recordID")
    private int recordID;

    @ColumnInfo(name = "rundate")
    private String rundate;

    @ColumnInfo(name = "runtime")
    private double runtime;

    @ColumnInfo(name = "distance")
    private double distance;

    @ColumnInfo(name = "pace")
    private double pace;

    @ColumnInfo(name = "speed")
    private double speed;


    public RecordItem( String rundate,  double runtime,  double distance,  double pace,  double speed){
        this.rundate=rundate;
        this.runtime=runtime;
        this.distance=distance;
        this.pace=pace;
        this.speed=speed;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public String getRundate() {
        return rundate;
    }

    public double getRuntime() {
        return runtime;
    }

    public double getDistance(){
        return distance;
    }

    public double getPace(){
        return pace;
    }

    public double getSpeed(){
        return speed;
    }

    public void setRundate(String rundate) {
        this.rundate = rundate;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public void setRuntime(double runtime) {
        this.runtime = runtime;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

}
