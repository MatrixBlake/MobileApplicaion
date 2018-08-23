package comp5216.sydney.edu.au.homework1;
import android.widget.ArrayAdapter;
import java.util.*;
import 	android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;


public class ItemsAdapter extends ArrayAdapter<ItemDate> {
    public ItemsAdapter(Context context, ArrayList<ItemDate> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ItemDate item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_date, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        // Populate the data into the template view using the data object
        tvName.setText(item.name);
        tvDate.setText(item.date);
        // Return the completed view to render on screen
        return convertView;
    }

}