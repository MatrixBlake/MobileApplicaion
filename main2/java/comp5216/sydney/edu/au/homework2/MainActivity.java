package comp5216.sydney.edu.au.homework2;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //request codes
    private static final int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 101;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHOTOS = 102;

    MarshmallowPermission marshmallowPermission = new MarshmallowPermission(this);

    final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
    final String orderBy = MediaStore.Images.Media._ID;
    GridView gridview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView) findViewById(R.id.gridview);

        if (!marshmallowPermission.checkPermissionForReadfiles()) {
            marshmallowPermission.requestPermissionForReadfiles();
        }
        if(marshmallowPermission.checkPermissionForExternalStorage()){
            marshmallowPermission.requestPermissionForExternalStorage();
        }

        setAdapater();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();

                Cursor cursor = getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                        null, orderBy);
                cursor.moveToPosition(position);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                //Store the path of the image
                String s= cursor.getString(dataColumnIndex);
                Log.i("Main","s is "+s);

                Intent intent = new Intent(MainActivity.this, EditImage.class);
                intent.putExtra("path",s);
                startActivityForResult(intent, 998);
            }
        });
    }

    public void setAdapater(){
        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i]= cursor.getString(dataColumnIndex);
            Log.i("PATH", arrPath[i]);
        }
    // The cursor should be freed up after use with close()
        cursor.close();

        gridview.setAdapter(new ImageAdapter(this,arrPath));
    }

    public void onOpenCameraClick(View view){
        if (!marshmallowPermission.checkPermissionForCamera()
                || !marshmallowPermission.checkPermissionForExternalStorage()) {
            marshmallowPermission.requestPermissionForCamera();
        }  else {
            Intent intent = new Intent(MainActivity.this, OpenCamera.class);
            this.startActivityForResult(intent,999);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Main","onactivityresult");
        setAdapater();
    }
}
