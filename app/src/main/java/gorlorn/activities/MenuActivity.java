package gorlorn.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

/**
 * Activity for the menu that dislpays when the game first starts.
 */
public class MenuActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_menu);

        DisplayBackground();
        DisplayAd();
    }

    public void startGame(View view)
    {
        Intent intent = new Intent(this, GorlornActivity.class);
        startActivity(intent);
    }

    public void viewLeaderBoard(View view)
    {
        //Intent intent = new Intent(t)
    }

    public void quit(View view)
    {
        finishAffinity();
    }

    private void DisplayAd()
    {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-8965087743383168~1842466939");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("B002DA6CED0109ADA1321B29C4DEE7B1")  // An example device ID
                .build();
        mAdView.loadAd(adRequest);
    }

    private void DisplayBackground()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Bitmap background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.space), size.x, size.y, true);
        ImageView backgroundView = (ImageView)findViewById(R.id.backgroundView);
        backgroundView.setImageBitmap(background);

//        Picasso.with(getApplicationContext())
//                .load(R.drawable.space)
//                .into(backgroundView);
    }
}
