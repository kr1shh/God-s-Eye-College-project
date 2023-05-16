package com.android.godseye;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home extends AppCompatActivity {
    Button btcalllog,btmsglog,btzone,btbrowsing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btcalllog=(Button) findViewById(R.id.button2);
        btmsglog=(Button) findViewById(R.id.button3);
        btbrowsing=(Button) findViewById(R.id.button4);
        btzone=(Button) findViewById(R.id.button5);

        btcalllog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                serverinsertions s=new serverinsertions(getApplicationContext());
                s.inscall("54545455455","12/3/2022","2.22","incoming","5");

            }
        });

        btmsglog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btbrowsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                serverinsertions s=new serverinsertions(getApplicationContext());
                        s.insbrow("12/2/2022","3.00","chrome");

            }
        });

        btzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                serverinsertions s =new serverinsertions(getApplicationContext());
              //          s.inszone("kozhikkode","23/10/2022","2.33","12");

            }
        });

    }
}