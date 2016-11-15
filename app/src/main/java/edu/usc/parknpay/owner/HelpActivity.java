package edu.usc.parknpay.owner;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import edu.usc.parknpay.R;
import edu.usc.parknpay.seeker.SearchFilterActivity;
import edu.usc.parknpay.seeker.SeekerMainActivity;
import edu.usc.parknpay.utility.TemplateActivity;

/**
 * Created by Bobo on 11/5/2016.
 */

public class HelpActivity extends TemplateActivity {

    MediaPlayer troll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_menu);

        setUpToolbar("Help");
        troll = MediaPlayer.create(this, R.raw.yunai);

        TextView easteregg = (TextView) findViewById(R.id.easteregg);
        easteregg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                troll.start();
            }
        });
    }
}
