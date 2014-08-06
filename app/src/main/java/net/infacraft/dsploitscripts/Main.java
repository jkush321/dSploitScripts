package net.infacraft.dsploitscripts;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class Main extends ActionBarActivity {

    ListView list;
    public static final String TAG = "dSploitScripts";
    public static String[][] dataString = {{"Chicken","Nuggets"},{"French","Toast"}};
    public static ArrayList<HashMap<String,String>> dataList = new ArrayList<HashMap<String,String>>();
    private SimpleAdapter listAdapater;
    public static Main self;
    public static String API_URL = "https://infacraft.net/projects/dsploitscripts/api/test.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        self = this;

        list = (ListView) findViewById(R.id.listView);
        listAdapater = new SimpleAdapter(this, dataList, android.R.layout.two_line_list_item, new String[]{"line1","line2"}, new int[]{android.R.id.text1,android.R.id.text2});
        list.setAdapter(listAdapater);

        updateList();

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

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Utils.saveScript("test1.js","test");
//                Utils.saveScript("test2","test2");
//                Utils.saveScript("test3","m\nu\nl\nt\ni\nl\ni\nn\ne");
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
