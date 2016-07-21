package gorlorn.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.MobileAds;

import gorlorn.Framework.GameLoopView;
import gorlorn.Gorlorn;

/**
 * Activity for the menu that dislpays when the game first starts.
 */
public class GorlornActivity extends Activity
{
    private Gorlorn _gorlorn;

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

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-8965087743383168~1842466939");

        _gorlorn = new Gorlorn(this);

        RelativeLayout root = (RelativeLayout) findViewById(R.id.menu_layout);
        root.addView(new GameLoopView(_gorlorn), 0,
                new WindowManager.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onBackPressed()
    {
        _gorlorn.showMenu();
    }
}