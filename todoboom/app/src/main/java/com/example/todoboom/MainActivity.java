package com.example.todoboom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText insert_text = (EditText) findViewById(R.id.eText);

        final TextView show_text = (TextView) findViewById(R.id.b_textView);

        Button button = (Button) findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_text.setText(insert_text.getText().toString());
                insert_text.setText("");
            }
        });
    }
}
