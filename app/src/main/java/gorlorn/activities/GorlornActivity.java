package gorlorn.activities;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import gorlorn.Entities.Hero;
import gorlorn.BulletManager;
import gorlorn.EnemyManager;
import gorlorn.Framework.GameLoopActivity;
import gorlorn.Framework.GameLoopView;
import gorlorn.HeartManager;
import gorlorn.UI.Background;
import gorlorn.UI.DeathScreen;
import gorlorn.UI.HUD;

/**
 * Activity for the main game.
 * <p/>
 * Created by Rob on 1/13/2016.
 */
public class GorlornActivity extends GameLoopActivity
{
    //region Private Variables

    private boolean _isInitialized;
    private Background _background;
    private DeathScreen _deathScreen;

    //endregion

    //region Getter/Setters

    public Random Random = new Random();
    public Hero Hero;
    public HUD Hud;
    public EnemyManager EnemyManager;
    public BulletManager BulletManager;
    public HeartManager HeartManager;
    public int ScreenWidth;
    public int ScreenHeight;
    public boolean IsDebugMode = true;
    public long Score = 0;
    public long HighScore = 0;

    /**
     * Bounds of the game area where the hero, enemies, bullets, etc. can move.
     */
    public Rect GameArea;

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

        GameLoopView view = new GameLoopView(this);
        setContentView(view);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ScreenWidth = size.x;
        ScreenHeight = size.y;
    }

    @Override
    public void update(float dt)
    {
        if (!_isInitialized)
        {
            Initialize();
            _isInitialized = true;
        }
        else if (_deathScreen != null)
        {
            _deathScreen.update(dt);
        }
        else
        {
            _background.update(dt);
            EnemyManager.update(dt);
            BulletManager.update(dt);
            Hero.update(dt);
            Hud.update(dt);
            HeartManager.update(dt);
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        if (!_isInitialized)
        {
            canvas.drawARGB(255, 22, 22, 22);
            return;
        }

        _background.draw(canvas);
        BulletManager.Draw(canvas);
        EnemyManager.draw(canvas);
        Hud.draw(canvas);
        Hero.draw(canvas);
        HeartManager.draw(canvas);

        if (_deathScreen != null)
        {
            _deathScreen.draw(canvas);
        }
    }

    @Override
    public void handleException(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
    }

    @Override
    public void handleInputEvent(MotionEvent me)
    {
        Hud.handleTouchEvent(me);
    }

    //endregion

    //region Public Methods

    /**
     * Immediately ends gameplay and transitions to the death screen.
     */
    public void showDeathScreen()
    {
        _deathScreen = new DeathScreen(this);
        _deathScreen.show();
    }

    /**
     * Starts a new game.
     */
    public void newGame()
    {
        _deathScreen = null;
        Initialize();
    }

    /**
     * Creates a bitmap sized as a percentage of the screen.
     *
     * @param id
     * @param screenWidthPercent
     * @return
     */
    public Bitmap createBitmapByWidthPercent(int id, float screenWidthPercent)
    {
        Resources resources = getApplicationContext().getResources();
        int diameter = getXFromPercent(screenWidthPercent);
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, id), diameter, diameter, true);
    }

    /**
     * Creates a bitmap sized by absolute pixel count.
     *
     * @param id
     * @param width
     * @param height
     * @return
     */
    public Bitmap createBitmap(int id, int width, int height)
    {
        Resources resources = getApplicationContext().getResources();
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, id), width, height, true);
    }

    /**
     * Gets the x pixel coordinate that is the given percent across the screen.
     *
     * @param percent
     * @return
     */
    public int getXFromPercent(float percent)
    {
        return (int) ((float) ScreenWidth * percent);
    }

    /**
     * Gets the y pixel coordinate that is the given percent across the screen.
     *
     * @param percent
     * @return
     */
    public int getYFromPercent(float percent)
    {
        return (int) ((float) ScreenHeight * percent);
    }

    /**
     * Returns a speed based on a percentage of the screen size.
     *
     * @param screenSizePercentage
     * @return
     */
    public float getSpeed(float screenSizePercentage)
    {
        return ((float) GameArea.width() + (float) GameArea.height()) / 2.0f * screenSizePercentage;
    }

    //endregion

    //region Private Methods

    private void Initialize()
    {
        Score = 0;
        GameArea = new Rect(0, 0, ScreenWidth, ScreenHeight);

        _background = new Background(this);
        Hero = new Hero(this);
        if (Hud == null) //Don't re-initialize the HUD since it will mess up the input logic
            Hud = new HUD(this);
        EnemyManager = new EnemyManager(this);
        BulletManager = new BulletManager(this);
        HeartManager = new HeartManager(this);
    }

    //endregion
}