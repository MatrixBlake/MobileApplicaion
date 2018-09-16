package comp5216.sydney.edu.au.homework2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OpenCamera extends Activity {


    private CameraView mCameraView;
    private static final int CAMERA_VIEW_WIDTH = 480;
    private static final int CAMERA_VIEW_HEIGHT = 640;

    private byte[] temp;

    private ImageView imageView;
    private File file;
    private Bitmap newBM;
    private Button buttonsave;
    private Button buttoncancel;
    private Button buttonsnap;
    private Button buttonback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_camera);

        mCameraView = (CameraView) findViewById(R.id.camera_view);
        mCameraView.setPreviewResolution(CAMERA_VIEW_WIDTH,CAMERA_VIEW_HEIGHT);
        imageView = (ImageView) findViewById(R.id.snap_img);


        buttonsave=findViewById(R.id.save);
        buttoncancel=findViewById(R.id.cancel);
        buttonsnap=findViewById(R.id.snap);
        buttonback=findViewById(R.id.back);


        imageView.setVisibility(View.INVISIBLE);
        buttonsave.setVisibility(View.INVISIBLE);
        buttoncancel.setVisibility(View.INVISIBLE);



        mCameraView.setPreviewCallback(new CameraView.PreviewCallback() {
            public void onGetYuvFrame(byte[] data) {
                temp = data.clone();
                Log.e("lin","===lin===>  onGetYuvFrame");
            }
        });

        mCameraView.startCamera();


    }

    public void snap(View v){
        //temp=mCameraView.getmYuvPreviewFrame();
        final YuvImage image = new YuvImage(temp, ImageFormat.NV21, CAMERA_VIEW_HEIGHT, CAMERA_VIEW_WIDTH, null);

        ByteArrayOutputStream os = new ByteArrayOutputStream(temp.length);
        if(!image.compressToJpeg(new Rect(0, 0, CAMERA_VIEW_HEIGHT, CAMERA_VIEW_WIDTH), 100, os)){
            return;
        }
        byte[] tmp = os.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0,tmp.length);

        Matrix matrix = new Matrix();
        matrix.setRotate(90f);

        newBM = Bitmap.createBitmap(bmp, 0, 0, CAMERA_VIEW_HEIGHT, CAMERA_VIEW_WIDTH, matrix, false);

        imageView.setImageBitmap(newBM);
        imageView.setVisibility(View.VISIBLE);
        mCameraView.stopCamera();
        mCameraView.setVisibility(View.INVISIBLE);
        buttonsnap.setVisibility(View.INVISIBLE);
        buttonback.setVisibility(View.INVISIBLE);
        buttonsave.setVisibility(View.VISIBLE);
        buttoncancel.setVisibility(View.VISIBLE);

    }

    public void save(View v){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String fileName="homework2_"+timeStamp;
        String dir =Environment.getExternalStorageDirectory().getAbsolutePath() +"/images/";
        Log.i("Opencamera","dir"+dir);
        try {
            file = new File(dir + fileName + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            newBM.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

        imageView.setVisibility(View.INVISIBLE);
        mCameraView.startCamera();
        mCameraView.setVisibility(View.VISIBLE);
        buttonsave.setVisibility(View.INVISIBLE);
        buttoncancel.setVisibility(View.INVISIBLE);
        buttonsnap.setVisibility(View.VISIBLE);
        buttonback.setVisibility(View.VISIBLE);
    }

    public void cancel(View v){
        imageView.setVisibility(View.INVISIBLE);
        mCameraView.startCamera();
        mCameraView.setVisibility(View.VISIBLE);
        buttonsave.setVisibility(View.INVISIBLE);
        buttoncancel.setVisibility(View.INVISIBLE);
        buttonsnap.setVisibility(View.VISIBLE);
        buttonback.setVisibility(View.VISIBLE);
    }

    public void back(View v){
        finish();
    }




}
