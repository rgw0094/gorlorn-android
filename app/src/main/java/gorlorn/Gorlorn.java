package gorlorn;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Date;
import java.util.Random;

import gorlorn.Entities.Hero;
import gorlorn.Framework.RenderLoopBase;
import gorlorn.Screens.AboutScreen;
import gorlorn.Screens.GameScreen;
import gorlorn.Screens.MenuScreen;
import gorlorn.UI.Background;
import gorlorn.Screens.DeathScreen;
import gorlorn.UI.HUD;
import gorlorn.Screens.ScreenBase;
import gorlorn.Screens.StatisticsScreen;
import gorlorn.activities.GorlornActivity;
import gorlorn.activities.R;

/**
 * The Gorlorn game.
 * <p/>
 * Created by Rob on 6/22/2016.
 */
public class Gorlorn extends RenderLoopBase
{
    public static Paint DebugTextPaint;

    //region Private Variables

    private GorlornActivity _activity;
    private Background _background;
    private ScreenBase _activeScreen;
    private ScreenBase _pendingScreen;
    private gorlorn.Entities.Hero _hero;
    private HUD _hud;
    private EnemyManager _enemyManager;
    private BulletManager _bulletManager;
    private HeartManager _heartManager;
    private boolean _isInitialized;
    private GorlornStats _currentStats;
    private GorlornStats _cumulativeStats;
    private long _timeStartedMs;

    //endregion

    //region Constructors

    public Gorlorn(GorlornActivity activity)
    {
        super(activity);

        _activity = activity;
        setScreen(new MenuScreen(this));
        _currentStats = new GorlornStats();
        _cumulativeStats = GorlornStats.load(activity);
        requestAd();
    }

    //endregion

    //region Getter/Setters

    public Background getBackground()
    {
        return _background;
    }

    public Hero getHero()
    {
        return _hero;
    }

    public HUD getHud()
    {
        return _hud;
    }

    public EnemyManager getEnemyManager()
    {
        return _enemyManager;
    }

    public BulletManager getBulletManager()
    {
        return _bulletManager;
    }

    public HeartManager getHeartManager()
    {
        return _heartManager;
    }

    /**
     * Returns the player's statistics for the current game.
     *
     * @return
     */
    public GorlornStats getGameStats()
    {
        return _currentStats;
    }

    /**
     * Returns the player's all-time highest score.
     *
     * @return
     */
    public long getHighScore()
    {
        return _cumulativeStats.highScore;
    }

    //endregion

    //region Public Methods

    /**
     * Starts a game.
     */
    public void startGame()
    {
        if (Constants.IsDebugMode && DebugTextPaint == null)
        {
            DebugTextPaint = new Paint();
            DebugTextPaint.setColor(Color.WHITE);
            DebugTextPaint.setStyle(Paint.Style.FILL);
            DebugTextPaint.setAntiAlias(true);
            DebugTextPaint.setTextSize(getYFromPercent(0.05f));
        }

        _timeStartedMs = new Date().getTime();
        _currentStats = new GorlornStats();

        _hero = new Hero(this);
        _enemyManager = new EnemyManager(this);
        _bulletManager = new BulletManager(this);
        _heartManager = new HeartManager(this);

        setScreen(new GameScreen(this));
    }

    /**
     * Ends the current game and transitions to the death screen.
     */
    public void die()
    {
        //Update statistics
        boolean newHighScore = _currentStats.score > _cumulativeStats.highScore;
        _currentStats.timePlayedMs = new Date().getTime() - _timeStartedMs;
        _cumulativeStats.add(_currentStats);
        _cumulativeStats.save(_activity);

        setScreen(new DeathScreen(this, newHighScore));
    }

    /**
     * Aborts whatever the user is doing and goes to the menu.
     */
    public void showMenu()
    {
        if (_activeScreen.getClass() == MenuScreen.class)
            return;

        setScreen(new MenuScreen(this));
    }

    /**
     * Shows the About screen!
     */
    public void showAboutScreen()
    {
        setScreen(new AboutScreen(this));
    }

    /**
     * Aborts whatever the user is doing and shows the statistics.
     */
    public void showStatistics()
    {
        setScreen(new StatisticsScreen(this, _cumulativeStats));
    }

    /**
     * Returns a speed based on a percentage of the screen size.
     *
     * @param screenSizePercentage
     * @return
     */
    public float getSpeed(float screenSizePercentage)
    {
        return ((float) ScreenWidth + (float) ScreenHeight) / 2.0f * screenSizePercentage;
    }

    //endregion

    //region RenderLoopBase Overrides

    @Override
    public void update(float dt)
    {
        if (!_isInitialized)
        {
            Bitmaps.Load(this);
            _background = new Background(this);
            _hud = new HUD(this);
            _isInitialized = true;
            return;
        }

        _background.update(dt);

        if (_activeScreen == null || _activeScreen.update(dt))
        {
            _pendingScreen.show(_activeScreen);
            _activeScreen = _pendingScreen;
            _pendingScreen = null;
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        if (!_isInitialized)
        {
            canvas.drawARGB(255, 100, 100, 100);
            return;
        }

        if (_activeScreen != null)
        {
            _activeScreen.draw(canvas);
        }
    }

    @Override
    public void handleInputEvent(MotionEvent me)
    {
        if (_hud != null)
        {
            _hud.handleTouchEvent(me);
        }
    }

    //endregion

    //region Private Methods

    /**
     * Transitions to the given screen.
     *
     * @param screen The screen to which to transition
     */
    private void setScreen(ScreenBase screen)
    {
        //If there is already a screen change pending, abort!
        if (_pendingScreen != null)
            return;

        //If there is no current screen or the current screen says it can leave immediately, switch to the new screen now.
        if (_activeScreen == null || _activeScreen.leave())
        {
            screen.show(_activeScreen);
            _activeScreen = screen;
        }
        else
        {
            //Otherwise the pending screen will become the active screen when the current _activeScreen.update() returns true
            _pendingScreen = screen;
        }
    }

    /**
     * Shows the menu controls in the activity layout.
     */
    public void showMenuButtons()
    {
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                _activity.findViewById(R.id.buttonsView).setVisibility(View.VISIBLE);
                //_activity.findViewById(R.id.adView).setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Hides the menu controls in the activity layout.
     */
    public void hideMenuButtons()
    {
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                _activity.findViewById(R.id.buttonsView).setVisibility(View.GONE);
                //_activity.findViewById(R.id.adView).setVisibility(View.GONE);
            }
        });
    }

    /**
     * Displays an ad at the bottom of the screen.
     */
    public void showAd()
    {
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                _activity.findViewById(R.id.adView).setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideAd()
    {
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                _activity.findViewById(R.id.adView).setVisibility(View.GONE);
            }
        });
    }

    /**
     * Requests an ad so its ready next time we need to display it
     */
    public void requestAd()
    {
        //AdRequest adRequest = new AdRequest.Builder().build(); //Real request!!
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("B002DA6CED0109ADA1321B29C4DEE7B1")
//                .build();
//        ().loadAd(adRequest);
        Thread adThread = new Thread()
        {
            @Override
            public void run()
            {
                loadAd();
            }
        };
        adThread.start();
    }

    private void loadAd()
    {
        // Banner Ad
        final AdView adView = (AdView) _activity.findViewById(R.id.adView);

        // Request for ads
        final AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("B002DA6CED0109ADA1321B29C4DEE7B1")
                .build();

        _activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                adView.loadAd(adRequest);
            }
        });
    }

    //endregion
}
