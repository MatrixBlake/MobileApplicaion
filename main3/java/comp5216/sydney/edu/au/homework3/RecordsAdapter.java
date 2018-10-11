package comp5216.sydney.edu.au.homework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordsAdapter extends ArrayAdapter<RunningRecords> {
    public RecordsAdapter(Context context, ArrayList<RunningRecords> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RunningRecords item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.running_records, parent, false);
        }
        // Lookup view for data population
        TextView rundate = (TextView) convertView.findViewById(R.id.rundate);
        TextView runtime = (TextView) convertView.findViewById(R.id.runtime);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        TextView pace = (TextView) convertView.findViewById(R.id.pace);
        TextView speed = (TextView) convertView.findViewById(R.id.speed);
        // Populate the data into the template view using the data object
        rundate.setText("Record: "+item.getRundate());
        runtime.setText("Time: "+item.getRuntime()+" min");
        distance.setText("Distance: "+item.getDistance()+" km");
        pace.setText("Pace: "+item.getPace()+" min/km");
        speed.setText("Speed: "+item.getSpeed()+" km/hour");
        // Return the completed view to render on screen
        return convertView;
    }

}