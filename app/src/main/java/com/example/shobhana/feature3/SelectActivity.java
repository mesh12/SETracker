package com.example.shobhana.feature3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectActivity extends AppCompatActivity {
    private static final String TAG = "SelectActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.btn_student) Button _selectstudent;
    @Bind(R.id.btn_driver) Button _selectdriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);

        _selectstudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity for Student
                Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        _selectdriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity for Driver
                Intent intent = new Intent(getApplicationContext(), DriverActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
}
