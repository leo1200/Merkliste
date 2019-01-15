package com.example.leona.merkliste;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class addmerk extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmerk);


        TinyDB tdb = new TinyDB(getApplicationContext());
        ArrayList<String> merken = tdb.getListString("merkliste");

        Intent i = getIntent();
        int position = i.getIntExtra("position", -1);
        EditText et = (EditText) findViewById(R.id.editText);
        if(position!=-1){
            et.setText(merken.get(position));
        }

        Button db = (Button)findViewById(R.id.dialogButton);
        db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Liste.class);
                if(!et.getText().toString().equals("")) {
                    if(position!=-1){
                        merken.set(position, et.getText().toString());
                    }
                    else if(!merken.contains(et.getText().toString())){
                        merken.add(et.getText().toString());
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Bereits in der Liste.",
                                Toast.LENGTH_SHORT).show();
                    }

                    tdb.putListString("merkliste", merken);
                }
                startActivity(i);
            }
        });

    }
    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        theme.applyStyle(R.style.Theme_AppCompat_Light, true);
        return theme;
    }
}
