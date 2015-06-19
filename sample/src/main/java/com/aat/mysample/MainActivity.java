package com.aat.mysample;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import java.util.List;
import java.util.Arrays;
import com.infteh.comboseekbar.ComboSeekBar;
import com.infteh.comboseekbar.CustomSeekBar;

public class MainActivity extends ActionBarActivity {
    private ComboSeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        CustomSeekBar t = (CustomSeekBar)this.findViewById(R.id.combo1);
        List<String> seekBarStep = Arrays.asList("First","Second","Third", "Fourth", "Fifth", "Sixth", "Seventh");
        t.setAdapter(seekBarStep);
        t.setOnSeekBarChangeListener(new ComboSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.out.println("----------------------");
                System.out.println(((CustomSeekBar) seekBar).getValue());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
       

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
