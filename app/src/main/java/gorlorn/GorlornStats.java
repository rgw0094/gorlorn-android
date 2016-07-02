package gorlorn;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class GorlornStats
{
    public long score;
    public long highScore;
    public long shotsFired;
    public long enemiesVanquished;
    public long highestCombo;
    public long gamesPlayed;
    public long heartsCollected;
    public long timePlayedMs;

    /**
     * Constructs a new GorlonStats with default values.
     */
    public GorlornStats()
    {
    }

    /**
     * Constructs a new GorlornStats by parsing JSON.
     *
     * @param json
     */
    public GorlornStats(JSONObject json) throws JSONException
    {
        score = json.getLong("Score");
        highScore = json.getLong("HighScore");
        shotsFired = json.getLong("ShotsFired");
        enemiesVanquished = json.getLong("EnemiesVanquished");
        highestCombo = json.getLong("HighestCombo");
        gamesPlayed = json.getLong("GamesPlayed");
        heartsCollected = json.getLong("HeartsCollected");
        timePlayedMs = json.getLong("TimePlayedMs");
    }

    public void add(GorlornStats stats)
    {
        score += stats.score;
        highScore = Math.max(highScore, stats.score);
        highestCombo = Math.max(highestCombo, stats.highestCombo);
        shotsFired += stats.shotsFired;
        enemiesVanquished += stats.enemiesVanquished;
        gamesPlayed++;
        heartsCollected += stats.heartsCollected;
        timePlayedMs += stats.timePlayedMs;
    }

    /**
     * Persists this Gorlorn stats object to the device.
     *
     * @param activity
     */
    public void save(Activity activity)
    {
        try
        {
            SharedPreferences.Editor editor = activity.getApplicationContext().getSharedPreferences("GorlornStatistics", Context.MODE_PRIVATE).edit();
            editor.putString("GorlornStatistics", toJson().toString());
            editor.commit();
        }
        catch (Exception e)
        {
        }
    }

    /**
     * Serializes this object to JSON.
     *
     * @return
     */
    private JSONObject toJson() throws JSONException
    {
        JSONObject json = new JSONObject();

        json.put("Score", score);
        json.put("HighScore", highScore);
        json.put("ShotsFired", shotsFired);
        json.put("EnemiesVanquished", enemiesVanquished);
        json.put("HighestCombo", highestCombo);
        json.put("GamesPlayed", gamesPlayed);
        json.put("HeartsCollected", heartsCollected);
        json.put("TimePlayedMs", timePlayedMs);

        return json;
    }

    public static GorlornStats load(Activity activity)
    {
        try
        {
            activity.getApplicationContext().getSharedPreferences("GorlornStatistics", 0).edit().clear().commit();

            SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences("GorlornStatistics", Context.MODE_PRIVATE);
            String json = prefs.getString("GorlornStatistics", "");
            if (json != "")
            {
                return new GorlornStats(new JSONObject(json));
            }
            else
            {
                return new GorlornStats();
            }
        }
        catch (Exception e)
        {
            return new GorlornStats();
        }
    }
}