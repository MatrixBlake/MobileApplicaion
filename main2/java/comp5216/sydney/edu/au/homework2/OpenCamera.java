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
    private static final int CAMERA_VIEW_WIDTH = 240;
    private static final int CAMERA_VIEW_HEIGHT = 320;

    private byte[] temp;

    private ImageView imageView;
    private File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_camera);

        mCameraView = (CameraView) findViewById(R.id.camera_view);
        mCameraView.setPreviewResolution(CAMERA_VIEW_WIDTH,CAMERA_VIEW_HEIGHT);
        imageView = (ImageView) findViewById(R.id.snap_img);



        mCameraView.setPreviewCallback(new CameraView.PreviewCallback() {
            public void onGetYuvFrame(byte[] data) {
                temp = data.clone();
                Log.e("lin","===lin===>  onGetYuvFrame");
            }
        });

        mCameraView.startCamera();



        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.snap).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //temp=mCameraView.getmYuvPreviewFrame();
                final YuvImage image = new YuvImage(temp, ImageFormat.NV21, 320, 240, null);


                ByteArrayOutputStream os = new ByteArrayOutputStream(temp.length);
                if(!image.compressToJpeg(new Rect(0, 0, 320, 240), 100, os)){
                    return;
                }
                byte[] tmp = os.toByteArray();
                Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0,tmp.length);

                Matrix matrix = new Matrix();
                matrix.setRotate(90f);

                Bitmap newBM = Bitmap.createBitmap(bmp, 0, 0, 320, 240, matrix, false);

                imageView.setImageBitmap(newBM);



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



            }
        });

    }

}
