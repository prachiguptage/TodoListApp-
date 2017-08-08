package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditItem;
    String text;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        text = getIntent().getExtras().getString("text","");
        position = getIntent().getExtras().getInt("position",0);

        etEditItem = (EditText)findViewById(R.id.etEditItem);
        etEditItem.setText(text);



    }

    public void onSaveItem(View view){
        final Intent data= new Intent();
        String etText=etEditItem.getText().toString();

        data.putExtra("text", etText);
        data.putExtra("position",position);
        setResult(RESULT_OK,data);
        finish();
    }
}
