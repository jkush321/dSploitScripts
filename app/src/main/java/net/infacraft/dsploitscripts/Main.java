package net.infacraft.dsploitscripts;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;


public class Main extends ActionBarActivity {

    ListView list;
    public static final String TAG = "dSploitScripts";
    public static String[][] dataString = {{"",""}};
    public static ArrayList<HashMap<String,String>> dataList = new ArrayList<HashMap<String,String>>();
    private SimpleAdapter listAdapater;
    public static Main self;
    public static String API_URL = "https://infacraft.net/projects/dsploitscripts/api";
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        self = this;

        list = (ListView) findViewById(R.id.listView);
        listAdapater = new SimpleAdapter(this, dataList, android.R.layout.two_line_list_item, new String[]{"line1","line2"}, new int[]{android.R.id.text1,android.R.id.text2});
        list.setAdapter(listAdapater);

        //updateList();

        Utils.init();
        Utils.asyncJSONToList(API_URL);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String choice = dataString[i][0];
                Log.d(TAG, "Clicked on " + choice + "!!");
                Intent intent = new Intent(Main.self, ScriptView.class);
                intent.putExtra("uid", dataString[i][1]);
                startActivity(intent);
            }
        });

        adView = (AdView) findViewById(R.id.adView);
        AdRequest req = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("29960FD8E337D7EF852D963F21C72840").build();
        adView.loadAd(req);
    }

    public void updateList()
    {
        dataList.clear();
        HashMap<String,String> item;
        for (int i = 0; i < dataString.length; i++)
        {
            item = new HashMap<String,String>();
            item.put("line1",dataString[i][0]);
            item.put("line2",dataString[i][1]);
            dataList.add(item);
        }
        listAdapater.notifyDataSetChanged();
        toast("Refreshed list",Toast.LENGTH_SHORT);
    }
    public void updateListFromOtherThread()
    {
        Handler handler = new Handler(list.getContext().getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateList();
            }
        };
        handler.post(runnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            menuAbout();
            return true;
        }
        if (id == R.id.action_donate)
        {
            menuDonate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toast(String msg, int duration)
    {
        Toast.makeText(getBaseContext(),msg,duration).show();
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

    public void menuDonate()
    {
        browseToURL("http://infacraft.net/donate");
    }
    public void menuAbout()
    {
        browseToURL("https://github.com/jkush321/dSploitScripts/blob/master/README.md#dsploitscripts");
    }
    public void submitScript(View v)
    {
        browseToURL("https://github.com/InfaCraft/dSploitScriptsRepo/blob/master/README.md#submitting-a-script");
    }
    public void browseToURL(String url)
    {
        Log.d(TAG,"Browsing to url [" +url+ "]");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
    public void refresh(View v)
    {
        toast("Refreshing...",Toast.LENGTH_SHORT);
        Utils.asyncJSONToList(API_URL);
    }

}
