package gorlorn.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import gorlorn.Framework.GameLoopView;
import gorlorn.Gorlorn;
import gorlorn.UI.GorlornScreen;

//ROBTODO: rename to GorlornActivity

/**
 * Activity for the menu that dislpays when the game first starts.
 */
public class MenuActivity extends Activity
{
    //region Private Variables

    private Gorlorn _gorlorn;

    //endregion

    //region GameLoopActivity Overrides

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

        RelativeLayout root = (RelativeLayout) findViewById(R.id.menu_layout);

        _gorlorn = new Gorlorn(this);

        root.addView(new GameLoopView(_gorlorn), 0,
                new WindowManager.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));

        DisplayAd();
    }

    //endregion

    //region Public Methods

    @Override
    public void onBackPressed()
    {
        if (_gorlorn.getCurrentScreen() != GorlornScreen.Menu)
        {
            _gorlorn.showMenu();
        }
    }

    public void startGame(View view)
    {
        _gorlorn.startGame();
    }

    public void viewLeaderBoard(View view)
    {
        //Intent intent = new Intent(t)
    }

    public void quit(View view)
    {
        finishAffinity();
    }

    //endregion

    //region Private Methods

    private void DisplayAd()
    {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-8965087743383168~1842466939");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("B002DA6CED0109ADA1321B29C4DEE7B1")
                .build();
        mAdView.loadAd(adRequest);
    }

    //endregion
}