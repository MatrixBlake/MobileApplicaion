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
    EditText startX;
    EditText startY;
    EditText finishX;
    EditText finishY;

    ArrayList<Integer> startXs=new ArrayList<Integer>();
    ArrayList<Integer> startYs=new ArrayList<Integer>();
    ArrayList<Integer> finishXs=new ArrayList<Integer>();
    ArrayList<Integer> finishYs=new ArrayList<Integer>();

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

        startX=findViewById(R.id.startX);
        startY=findViewById(R.id.startY);
        finishX=findViewById(R.id.finishX);
        finishY=findViewById(R.id.finishY);

        originalWidth = originalMap.getWidth();
        originalHeight =originalMap.getHeight();


        startXs.add(0);
        startYs.add(0);
        finishXs.add(100);
        finishYs.add(100);
    }

    public void crop(View v){
        if(startX.getText().toString().matches("^\\d+$")
            && startY.getText().toString().matches("^\\d+$")
            && finishX.getText().toString().matches("^\\d+$")
            && finishY.getText().toString().matches("^\\d+$")){

            int startx= Integer.parseInt(startX.getText().toString());
            int starty=Integer.parseInt(startY.getText().toString());
            int finishx = Integer.parseInt(finishX.getText().toString());
            int finishy = Integer.parseInt(finishY.getText().toString());

            if(finishx<=100 && finishy<=100 && startx>=0 && starty>=0 && finishx>startx && finishy>starty){
                startXs.add(startx);
                startYs.add(starty);
                finishXs.add(finishx);
                finishYs.add(finishy);

                newMap=Bitmap.createBitmap(originalMap, originalWidth*startx/100, originalHeight*starty/100, originalWidth*(finishx-startx)/100, originalHeight*(finishy-starty)/100);
                imageView.setImageBitmap(newMap);
            }
        }
    }
    public void undo(View v){
        if(startXs.size()>1){
            startXs.remove(startXs.size()-1);
            startYs.remove(startYs.size()-1);
            finishXs.remove(finishXs.size()-1);
            finishYs.remove(finishYs.size()-1);

            int startx=startXs.get(startXs.size()-1);
            int starty=startYs.get(startYs.size()-1);
            int finishx=finishXs.get(finishXs.size()-1);
            int finishy=finishYs.get(finishYs.size()-1);

            startX.setText(startx+"");
            startY.setText(starty+"");
            finishX.setText(finishx+"");
            finishY.setText(finishy+"");

            newMap=Bitmap.createBitmap(originalMap, originalWidth*startx/100, originalHeight*starty/100, originalWidth*(finishx-startx)/100, originalHeight*(finishy-starty)/100);
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
