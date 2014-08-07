package net.infacraft.dsploitscripts;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jkush321 on 8/5/2014.
 */
public class Utils {

    // FILE MANIPULATION
    // GENERIC
    public static void init()
    {
        try {
            runCommandSU("mkdir -p /sdcard/dsploitscripts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void runCommand(String... cmd) throws Exception
    {
        if (cmd.length==1) {
            Runtime.getRuntime().exec(cmd[0]);
            Log.d(Main.TAG, "Running command: " + cmd);
        }
        else {
            Runtime.getRuntime().exec(cmd);
            Log.d(Main.TAG, "Running command: su -c \"" + cmd[2]+"\"");
        }
    }
    public static void runCommandSU(String cmd) throws Exception
    {
        runCommand(new String[]{"su","-c",cmd});
    }
    public static void saveFile(String file, String text) throws Exception
    {
//      The following line used to be
//              text = text.replaceAll("\\\"","\\\\\"");
//      but was changed because it didn't return the expected results.
//      strangely, this does, replacing single backslashes with triple.
        text = text.replaceAll("\\\\","\\\\\\\\\\\\\\\\");

        runCommandSU("rm -f \"" + file + "\"");
        runCommandSU("echo '" + text + "' >> \"" + file + "\"");
    }
    public static void saveToSD(String file, String text) throws Exception
    {
        saveFile("/sdcard/" + file,text);
    }

    // FILE MANIPULATION
    // SPECIFIC
    public static void saveScript(String name, String code)
    {
        try {
//            code.replaceAll("\"", "\\\"");
//            code.replaceAll("'","\\'");
            name = "dsploitscripts/"+name.toLowerCase();
            if (!name.endsWith(".js")) name+=".js";
            saveToSD(name, code);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // JSON PARSING
    public static String retrieveJSONString(String url)
    {
        Log.d(Main.TAG,"Trying to retrieve string of [" +url+ "]");

        DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/json");
        InputStream inputStream = null;
        String result;
        try
        {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity ent = httpResponse.getEntity();
            inputStream=ent.getContent();
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"),8);
            String response = "";
            String line = null;
            while ((line=r.readLine())!=null)
            {
                response+=line+"\n";
            }
            inputStream.close();
            Log.d(Main.TAG,"Got: "+response);
            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (inputStream!=null) {try{inputStream.close();}catch(Exception e){}}
        Log.e(Main.TAG,"Got NOTHING!");
        return "";
    }
    public static JSONObject retrieveJSON(String url)
    {
        Log.d(Main.TAG,"Trying to retrieve json of [" +url+ "]");
        String string = retrieveJSONString(url);
        Log.d(Main.TAG,"Retrieved json string, converting...");

        if (string!=null && !string.equals(""))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(string);
                return jsonObject;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static Script[] createScriptObjects(JSONArray scriptArray)
    {
        Script.scriptFromUID.clear();
        Script[] scripts = new Script[scriptArray.length()];
        for (int i = 0; i < scriptArray.length(); i++)
        {
            try
            {
                scripts[i] = new Script(scriptArray.getJSONObject(i));
            }
            catch (Exception e)
            {
                Log.e(Main.TAG,"Could not create script object!");
                e.printStackTrace();
            }
        }
        return scripts;
    }
    /**
     * This function does it all, from the url to the list on display
     * @param url URL of the json file to interpret
     */
    public static void jsonToList(String url)
    {
        Log.d(Main.TAG,"Trying to convert to list [" +url+ "]");
        JSONObject jsonObject = retrieveJSON(url);
        Log.d(Main.TAG,"Retrieved, converting...");
        if (jsonObject==null) return;

        try
        {
            JSONArray scripts = jsonObject.getJSONArray("scripts");
            Log.d(Main.TAG,"Created JSONArray [" + scripts.length() + "]");
            Script[] scriptArray = createScriptObjects(scripts);
            Log.d(Main.TAG,"Created Script["+scriptArray.length+"]");
            String[][] listValues = new String[scriptArray.length][2];
            for (int i = 0; i < scriptArray.length; i++)
            {
                listValues[i][0]=scriptArray[i].getName() + " by " + scriptArray[i].getAuthor();
                Log.d(Main.TAG,"Adding to array [name]: " + scriptArray[i].getName());
                listValues[i][1]=scriptArray[i].getUid();
                Log.d(Main.TAG,"Adding to array [uid-]: " + scriptArray[i].getUid());
            }
            Main.dataString = listValues;
            Log.d(Main.TAG,"Updated dataString");
            Main.self.updateListFromOtherThread();
            Log.d(Main.TAG,"Called updateList()");
        }
        catch (Exception e)
        {
            Log.e(Main.TAG,"Could not retrieve 'scripts' from JSON!");
            e.printStackTrace();
        }
    }
    public static void asyncJSONToList(String s)
    {
        (new JSONRetrieveTask()).execute(s);
    }

    static class JSONRetrieveTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... msg)
        {
            Utils.jsonToList(msg[0]);
            return null;
        }
    }

    public static String retrieveCode(String url)
    {
        Log.d(Main.TAG,"Trying to retrieve string of [" +url+ "]");

        DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("Content-type", "application/json");
        InputStream inputStream = null;
        String result;
        try
        {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity ent = httpResponse.getEntity();
            inputStream=ent.getContent();
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"),8);
            String response = "";
            String line = null;
            while ((line=r.readLine())!=null)
            {
                response+=line+"\n";
            }
            inputStream.close();
            Log.d(Main.TAG,"Got: "+response);
            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (inputStream!=null) {try{inputStream.close();}catch(Exception e){}}
        Log.e(Main.TAG,"Got NOTHING!");
        return "";
    }
    public static void asyncDownloadScript(String uid)
    {
        new ScriptRetrieveTask().execute(uid);
    }
    public static void downloadScript(Script script)
    {
        asyncDownloadScript(script.getUid());
    }

    static class ScriptRetrieveTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... msg)
        {
            Script s = Script.getByUID(msg[0]);
            String code = retrieveCode(s.getUrl());
            saveScript(s.getUid(),code);
            ScriptView.self.toastFromOtherThread("Downloaded to /sdcard/dsploitscripts/" + s.getUid() + ".js", Toast.LENGTH_SHORT);
            return null;
        }
    }

    /**
     * Rating is cleansed by the server, don't try rating 10000
     * Rating is also limited to 1 per script per ip address
     */
    public static void rateScript(Script script, float rating)
    {
        new ScriptRateTask().execute(new Object[]{script.getUid(),new Float(rating)});
    }

    static class ScriptRateTask extends AsyncTask<Object[], Void, Void>
    {
        @Override
        protected Void doInBackground(Object[]... msg)
        {
            Object[] obj = msg[0];
            String uid = (String) obj[0];
            Float rating = (Float) obj[1];
            retrieveCode(Main.API_URL+"/rate.php?s="+uid+"&r="+(int)(rating*2));
            return null;
        }
    }
}
