package net.infacraft.dsploitscripts;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by jkush321 on 8/5/2014.
 */
public class Script {
    // ------------------------------------------------------------------------------ //
    // NON-STATIC                                                                     //
    // ------------------------------------------------------------------------------ //

    /**
     * The username of the script creator
     */
    String author;

    /**
     * The url to download the actual script
     */
    String url;

    /**
     * The amount of downloads on the script
     */
    int downloads;

    /**
     * The fancy name of the script
     */
    String name;

    /**
     * The proper name of the script
     */
    String properName;

    /**
     * This is the unique identifier of the script.
     */
    String uid;

    /**
     * The script's rating, out of 10
     */
    int rating;

    /**
     * A short description of the script
     */
    String description;

    public Script(String author, String url, int downloads, String name, String properName, String uid, int rating, String description)
    {
        this.author = author;
        this.url = url;
        this.downloads = downloads;
        this.name = name;
        this.properName = name;
        this.uid = uid;
        this.rating = rating;
        this.description = description;
    }
    public Script(JSONObject jsonObject)
    {
        try
        {
            this.author = jsonObject.getString("author");
            this.url = jsonObject.getString("url");
            this.downloads = jsonObject.getInt("downloads");
            this.name = jsonObject.getString("name");
            this.properName = jsonObject.getString("properName");
            this.uid = jsonObject.getString("uid");
            this.rating = jsonObject.getInt("rating");
            this.description = jsonObject.getString("description");

            addScript(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getRating() {
        return rating;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public int getDownloads() {
        return downloads;
    }

    public String getName() {
        return name;
    }

    public String getProperName() {
        return properName;
    }

    public String getUid() {
        return uid;
    }

    public String getDescription() {
        return description;
    }

    public String getCode()
    {
        // TODO
        // retrieve code from url and return here
        return "";
    }

    // ------------------------------------------------------------------------------ //
    // STATIC                                                                         //
    // ------------------------------------------------------------------------------ //

    /**
     * A map that contains all scripts to be matched with UID
     */
    public static HashMap<String,Script> scriptFromUID = new HashMap<String, Script>();

    /**
     * Add script by UID
     */
    public static void addScript(Script s)
    {
        if (!scriptFromUID.containsKey(s.getUid())) scriptFromUID.put(s.getUid(),s);
    }

    /**
     * Get the Script via UID
     * @param uid The UID of the Script you want
     * @return The Script, or null
     */
    public static Script getByUID(String uid)
    {
        if (scriptFromUID.containsKey(uid)) return scriptFromUID.get(uid);
        return null;
    }
}
