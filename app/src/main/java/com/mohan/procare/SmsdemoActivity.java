package com.mohan.procare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mohan.procare.R;

public class SmsdemoActivity extends AppCompatActivity {
        EditText ph,ms;
        Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsdemo);

        ph =findViewById(R.id.number);
        ms=findViewById(R.id.message);
        send =findViewById(R.id.send)
;


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mobile = ph.getText().toString();
                String message_text =ms.getText().toString();

                Intent my_intent = new Intent(getApplicationContext(),SmsdemoActivity.class);

                PendingIntent my_pi = PendingIntent.getActivity(getApplicationContext(),0,my_intent,0);

                SmsManager mysms =SmsManager.getDefault();
                mysms.sendTextMessage(mobile,null,message_text,my_pi,null);

                Toast.makeText(SmsdemoActivity.this, "successfully send the message", Toast.LENGTH_SHORT).show();

            }
        });







    }
}