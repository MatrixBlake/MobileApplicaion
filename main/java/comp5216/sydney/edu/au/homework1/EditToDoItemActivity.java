package comp5216.sydney.edu.au.homework1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Date;


public class EditToDoItemActivity extends Activity {
	public int position=0;
	EditText etItem; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//populate the screen using the layout
		setContentView(R.layout.activity_edit_item);
		
		//Get the data from the main screen
		String origin_name = getIntent().getStringExtra("item");
		String origin_date = getIntent().getStringExtra("date");
		position = getIntent().getIntExtra("position",-1);
		
		// show original content in the text field
		etItem = (EditText)findViewById(R.id.etEditItem);
		etItem.setText(origin_name);


	}

	public void onSubmit(View v) {
	  etItem = (EditText) findViewById(R.id.etEditItem);
	  
	  // Prepare data intent for sending it back
	  Intent data = new Intent();

	  if(etItem.getText().toString()!= null && etItem.getText().toString().length() > 0){
          // Pass relevant data back as a result
          data.putExtra("item", etItem.getText().toString());
          data.putExtra("date",new Date().toString());
          data.putExtra("position", position);


          // Activity finished ok, return the data
          setResult(RESULT_OK, data); // set result code and bundle data for response
          finish(); // closes the activity, pass data to parent
      }

	}

	public void onCancel(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditToDoItemActivity.this);
        builder.setTitle(R.string.dialog_cancel_title)
                .setMessage(R.string.dialog_cancel_msg)
                .setNegativeButton("no", new  DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User cancelled the dialog
                        // Nothing happens
                    }
                })
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        builder.create().show();

    }
}
