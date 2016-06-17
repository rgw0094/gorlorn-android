package gorlorn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Activity for the menu that dislpays when the game first starts.
 */
public class MenuActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-8965087743383168~1842466939");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("B002DA6CED0109ADA1321B29C4DEE7B1")  // An example device ID
                .build();
        mAdView.loadAd(adRequest);
    }

    public void startGame(View view)
    {
        Intent intent = new Intent(this, GorlornActivity.class);
        startActivity(intent);
    }
}
