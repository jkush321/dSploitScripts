package net.infacraft.dsploitscripts;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import net.infacraft.dsploitscripts.R;

public class ScriptView extends ActionBarActivity {

    Script script;
    public static ScriptView self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_view);

        self = this;

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        this.script = Script.getByUID(uid);
        Log.d(Main.TAG, "Started script view activity with UID: "+script.uid);

        ((TextView) findViewById(R.id.scriptName)).setText(script.getName());
        ((RatingBar) findViewById(R.id.ratingBar)).setRating(((float)script.getRating())/2f);
        ((TextView) findViewById(R.id.downloads)).setText("Downloaded " + script.getDownloads() + " times.");
        ((TextView) findViewById(R.id.author)).setText(script.getAuthor());
        ((TextView) findViewById(R.id.description)).setText(script.getDescription());
        findViewById(R.id.scriptfolder_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.downloadScript(script);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.script_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toast(String msg, int duration)
    {
        Toast.makeText(getApplicationContext(), msg, duration);
    }
    public void toastFromOtherThread(String msg, int duration)
    {
        final String fMsg = msg;
        final int fDuration = duration;
        Handler handler = new Handler(getApplicationContext().getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                toast(fMsg,fDuration);
            }
        };
        handler.post(runnable);
    }
}
