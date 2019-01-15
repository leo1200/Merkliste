package com.example.leona.merkliste;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;


public class Sortieren extends AppCompatActivity {

    ArrayList<String> merken = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortieren);

        SlideAndDragListView listView = (SlideAndDragListView)findViewById(R.id.liste);

        Menu menu = new Menu(false, 0);//the first parameter is whether can slide over
        menu.addItem(new MenuItem.Builder().setWidth(0)//set Width
                .build());
        listView.setMenu(menu);

        TinyDB tdb = new TinyDB(getApplicationContext());
        merken = tdb.getListString("merkliste");
        ArrayAdapter<String> stringAdapter = new ArrayAdapter<>(
                this,
                R.layout.row_bg,
                R.id.text,
                merken
        );
        listView.setAdapter(stringAdapter);

        listView.setOnDragDropListener(new SlideAndDragListView.OnDragDropListener() {
            String mDraggedEntity = new String();
            @Override
            public void onDragViewStart(int beginPosition) {
                mDraggedEntity = merken.get(beginPosition);
            }

            @Override
            public void onDragDropViewMoved(int fromPosition, int toPosition) {
                merken.remove(fromPosition);
                merken.add(toPosition, mDraggedEntity);
            }

            @Override
            public void onDragViewDown(int finalPosition) {
                //merken.set(finalPosition, mDraggedEntity);
            }
        });


    }
}
