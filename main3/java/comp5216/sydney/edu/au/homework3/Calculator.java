package comp5216.sydney.edu.au.homework3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Calculator extends AppCompatActivity {
    private EditText time;
    private EditText distance;
    private TextView pace;
    private TextView speed;
    double timevalue;
    double distancevalue;
    double pacevalue;
    double speedvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        time = (EditText)findViewById(R.id.time);
        distance=(EditText)findViewById(R.id.distance);
        pace=(TextView)findViewById(R.id.pace);
        speed=(TextView)findViewById(R.id.speed);
    }

    public void calculate(View v){

        if(time.getText().toString().matches("^\\d+(.\\d+)?$") && distance.getText().toString().matches("^\\d+(.\\d+)?$")){
            timevalue=Double.parseDouble(time.getText().toString());
            distancevalue=Double.parseDouble(distance.getText().toString());
            if(timevalue>0 && distancevalue>0){
                pacevalue=(Math.round(timevalue/distancevalue*100))/100.0;
                speedvalue=(Math.round(distancevalue/timevalue*60*100))/100.0;
                pace.setText(pacevalue+"");
                speed.setText(speedvalue+"");
            }
        }
    }

    public void back(View v){
        finish();
    }

}
