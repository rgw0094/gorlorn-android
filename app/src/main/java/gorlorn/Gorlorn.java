package gorlorn;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import gorlorn.Entities.Hero;
import gorlorn.Framework.RenderLoopBase;
import gorlorn.UI.Background;
import gorlorn.UI.DeathScreen;
import gorlorn.UI.HUD;

/**
 * The Gorlorn game.
 *
 * Created by Rob on 6/22/2016.
 */
public class Gorlorn extends RenderLoopBase
{
    //region Private Variables

    private boolean _isInitialized;
    private Background _background;
    private DeathScreen _deathScreen;

    //endregion

    //region Constructors

    public Gorlorn(Activity activity)
    {
        super(activity);
    }

    //endregion

    //region Getter/Setters

    public java.util.Random Random = new Random();
    public gorlorn.Entities.Hero Hero;
    public HUD Hud;
    public EnemyManager EnemyManager;
    public BulletManager BulletManager;
    public HeartManager HeartManager;
    public boolean IsDebugMode = false;
    public long Score = 0;
    public long HighScore = 0;

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
        if (Hud != null)
        {
            Hud.handleTouchEvent(me);
        }
    }

    //endregion

    //region Private Methods

    private void Initialize()
    {
        Score = 0;

        _background = new Background(this, false);
        Hero = new Hero(this);
        if (Hud == null) //Don't re-initialize the HUD since it will mess up the input logic
            Hud = new HUD(this);
        EnemyManager = new EnemyManager(this);
        BulletManager = new BulletManager(this);
        HeartManager = new HeartManager(this);
    }

    //endregion
}
