package comp5216.sydney.edu.au.homework3;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MusicList extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> itemsAdapter;
    List<String> mp3Names;
    List<String> mp3urls;
    MarshmallowPermission marshmallowPermission = new MarshmallowPermission(this);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list);

        listView=findViewById(R.id.lstView);



        if (!marshmallowPermission.checkPermissionForReadfiles()) {
            marshmallowPermission.requestPermissionForReadfiles();
        }

        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        mp3Names = new ArrayList<String>();
        mp3urls = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            mp3urls.add(url);
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            mp3Names.add(title);
        }


        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mp3Names);
        listView.setAdapter(itemsAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String map3Name = (String) itemsAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item " + position + ": " + map3Name);

                Intent data = new Intent();
                data.putExtra("name", map3Name);
                data.putExtra("url", mp3urls.get(position));


                setResult(RESULT_OK, data);
                finish();
            }
        });


    }

    public void onBack(View v){
        finish();
    }
}
