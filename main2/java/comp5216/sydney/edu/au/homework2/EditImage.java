package comp5216.sydney.edu.au.homework2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EditImage extends Activity {

    ImageView imageView;
    EditText cropX;
    EditText cropY;
    EditText cropWidth;
    EditText cropHeight;

    ArrayList<Integer> xs=new ArrayList<Integer>();
    ArrayList<Integer> ys=new ArrayList<Integer>();
    ArrayList<Integer> widths=new ArrayList<Integer>();
    ArrayList<Integer> heights=new ArrayList<Integer>();

    Bitmap originalMap;
    Bitmap newMap;
    String image_path;

    int originalWidth;
    int originalHeight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populate the screen using the layout
        setContentView(R.layout.edit_image);

        image_path = getIntent().getStringExtra("path");

        Log.i("Edit",image_path);

        imageView =(ImageView) findViewById(R.id.imageview);
        originalMap=BitmapFactory.decodeFile(image_path);
        imageView.setImageBitmap(originalMap);

        cropX=findViewById(R.id.cropX);
        cropY=findViewById(R.id.cropY);
        cropWidth=findViewById(R.id.cropWidth);
        cropHeight=findViewById(R.id.cropHeight);

        originalWidth = originalMap.getWidth();
        originalHeight =originalMap.getHeight();

        cropWidth.setText(originalWidth+"");
        cropHeight.setText(originalHeight+"");
        xs.add(0);
        ys.add(0);
        widths.add(originalWidth);
        heights.add(originalHeight);
    }

    public void crop(View v){
        if(cropX.getText().toString().matches("^\\d+$")
            && cropY.getText().toString().matches("^\\d+$")
            && cropWidth.getText().toString().matches("^\\d+$")
            && cropHeight.getText().toString().matches("^\\d+$")){

            int x1= Integer.parseInt(cropX.getText().toString());
            int y1=Integer.parseInt(cropX.getText().toString());
            int width1 = Integer.parseInt(cropWidth.getText().toString());
            int height1 = Integer.parseInt(cropHeight.getText().toString());

            if(x1+width1<=originalWidth && y1+height1<=originalHeight && width1>0 && height1>0){
                xs.add(x1);
                ys.add(y1);
                widths.add(width1);
                heights.add(height1);

                newMap=Bitmap.createBitmap(originalMap, x1, y1, width1, height1);
                imageView.setImageBitmap(newMap);
            }
        }
    }
    public void undo(View v){
        if(xs.size()>1){
            xs.remove(xs.size()-1);
            ys.remove(ys.size()-1);
            widths.remove(widths.size()-1);
            heights.remove(heights.size()-1);

            int x=xs.get(xs.size()-1);
            int y=ys.get(ys.size()-1);
            int width=widths.get(widths.size()-1);
            int height=heights.get(heights.size()-1);

            cropX.setText(x+"");
            cropY.setText(y+"");
            cropWidth.setText(width+"");
            cropHeight.setText(height+"");

            newMap=Bitmap.createBitmap(originalMap, x, y, width, height);
            imageView.setImageBitmap(newMap);
        }

    }

    public void back(View v){
        finish();
    }

    public void save(View v){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String fileName="changed_"+timeStamp;
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() +"/images/";
        try {
            File file = new File(dir + fileName + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            newMap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            Toast.makeText(this, "saved:" + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
