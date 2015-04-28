package com.aat.mysample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.List;
import java.util.Arrays;
import com.infteh.comboseekbar.ComboSeekBar;


public class MainActivity extends ActionBarActivity {
    private ComboSeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mSeekBar = new ComboSeekBar(this);
        System.out.println("............");

        ComboSeekBar t = new ComboSeekBar(this);
        List<String> seekBarStep = Arrays.asList("2","1","5","10","20");
        ComboSeekBar cb = (ComboSeekBar)this.findViewById(R.id.combo);
        cb.setAdapter(seekBarStep);
        cb.setDefaults();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
