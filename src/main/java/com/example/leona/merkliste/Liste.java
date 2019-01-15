package com.example.leona.merkliste;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;


public class Liste extends ListActivity implements
        SwipeActionAdapter.SwipeActionListener
{
    protected SwipeActionAdapter mAdapter;

    ArrayList<String> merken = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TinyDB tdb = new TinyDB(getApplicationContext());
        merken = tdb.getListString("merkliste");

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    merken.add(sharedText);
                    tdb.putListString("merkliste", merken);
                }
            }
        }

        if(Intent.ACTION_PROCESS_TEXT.equals(action) && type!=null){
            String text = getIntent()
                    .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString();
            merken.add(text);
            tdb.putListString("merkliste", merken);
        }

        ArrayAdapter<String> stringAdapter = new ArrayAdapter<>(
                this,
                R.layout.row_bg,
                R.id.text,
                merken
        );
        mAdapter = new SwipeActionAdapter(stringAdapter);
        mAdapter.setSwipeActionListener(this)
                .setDimBackgrounds(true)
                .setListView(getListView());
        setListAdapter(mAdapter);



        mAdapter.addBackground(SwipeDirection.DIRECTION_FAR_LEFT, R.layout.row_bg_left)
                .addBackground(SwipeDirection.DIRECTION_FAR_RIGHT, R.layout.row_bg_right)
                .addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.row_bg_left)
                .addBackground(SwipeDirection.DIRECTION_NORMAL_RIGHT, R.layout.row_bg_right);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add:
                Intent i = new Intent(getApplicationContext(), addmerk.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id){
        ClipboardManager clipboard0 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip0 = ClipData.newPlainText("wort", merken.get(position));
        clipboard0.setPrimaryClip(clip0);
    }
    
    @Override
    public boolean hasActions(int position, SwipeDirection direction){
        if(direction.isLeft()) return true;
        if(direction.isRight()) return true;
        return false;
    }

    @Override
    public boolean shouldDismiss(int position, SwipeDirection direction){
        return direction == SwipeDirection.DIRECTION_NORMAL_LEFT;
    }

    @Override
    public void onSwipe(int[] positionList, SwipeDirection[] directionList){
        for(int i=0;i<positionList.length;i++) {
            SwipeDirection direction = directionList[i];
            int position = positionList[i];
            String dir = "";

            switch (direction) {
                case DIRECTION_NORMAL_LEFT:
                    editieren(position);
                    break;
                case DIRECTION_NORMAL_RIGHT:
                    merken.remove(position);
                    break;
                case DIRECTION_FAR_LEFT:
                    editieren(position);
                    break;
                case DIRECTION_FAR_RIGHT:
                    merken.remove(position);
                    break;
            }

            mAdapter.notifyDataSetChanged();
            TinyDB tdb = new TinyDB(getApplicationContext());
            tdb.putListString("merkliste", merken);
        }
    }

    private void editieren(int position) {
        Intent i = new Intent(getApplicationContext(), addmerk.class);
        i.putExtra("position", position);
        startActivity(i);
    }
}


