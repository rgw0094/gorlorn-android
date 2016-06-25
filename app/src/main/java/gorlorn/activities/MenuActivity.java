package gorlorn.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import gorlorn.Bitmaps;
import gorlorn.Framework.GameLoopView;
import gorlorn.Framework.RenderLoopBase;
import gorlorn.UI.Background;

/**
 * Activity for the menu that dislpays when the game first starts.
 */
public class MenuActivity extends Activity
{
    //region Private Variables

    private Background _background;

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

        root.addView(new GameLoopView(new MenuRenderLoop(this)), 0,
                new WindowManager.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));

        DisplayAd();
    }

    //endregion

    //region Public Methods

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

    private class MenuRenderLoop extends RenderLoopBase
    {
        public MenuRenderLoop(Activity activity)
        {
            super(activity);
        }

        @Override
        public void handleInputEvent(MotionEvent me)
        {
        }

        @Override
        public void update(float dt)
        {
            Bitmaps.Load(this);

            if (_background == null)
            {
                _background = new Background(this);
            }
            else
            {
                _background.update(dt);
            }
        }

        @Override
        public void draw(Canvas canvas)
        {
            if (_background != null)
            {
                _background.draw(canvas);
                canvas.drawBitmap(Bitmaps.Title, getXFromPercent(0.15f), getYFromPercent(0.15f), null);
            }
        }

        @Override
        public void handleException(Exception e)
        {
        }
    }
}