package gorlorn;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import gorlorn.Entities.Hero;
import gorlorn.Framework.RenderLoopBase;
import gorlorn.UI.Background;
import gorlorn.UI.DeathScreen;
import gorlorn.UI.GorlornScreen;
import gorlorn.UI.HUD;
import gorlorn.UI.HeroSummonEffect;
import gorlorn.activities.MenuActivity;
import gorlorn.activities.R;

/**
 * The Gorlorn game.
 * <p/>
 * Created by Rob on 6/22/2016.
 */
public class Gorlorn extends RenderLoopBase
{
    //region Private Variables

    private MenuActivity _activity;
    private GorlornScreen _screen;
    private Background _background;
    private DeathScreen _deathScreen;
    private HeroSummonEffect _heroSummonEffect;
    private boolean _isInitialized;

    //endregion

    //region Constructors

    public Gorlorn(MenuActivity activity)
    {
        super(activity);

        _activity = activity;
        _screen = GorlornScreen.Menu;
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
     * Returns which screen is currently active.
     *
     * @return
     */
    public GorlornScreen getCurrentScreen()
    {
        return _screen;
    }

    /**
     * Starts a game.
     */
    public void startGame()
    {
        _deathScreen = null;
        Score = 0;

        Hero = new Hero(this);
        EnemyManager = new EnemyManager(this);
        BulletManager = new BulletManager(this);
        HeartManager = new HeartManager(this);

        if (_screen == GorlornScreen.Menu)
        {
            toggleMenuControlVisibility(false);
        }

        //Show a transition effect based upon which screen we are coming from
        _heroSummonEffect = new HeroSummonEffect(this, _screen == GorlornScreen.Menu);
        _screen = GorlornScreen.Game;
    }

    public void showDeathScreen()
    {
        _deathScreen = new DeathScreen(this);
        _deathScreen.show();
        _screen = GorlornScreen.Death;
    }

    /**
     * Aborts whatever the user is doing and goes to the menu.
     */
    public void showMenu()
    {
        toggleMenuControlVisibility(true);
        _deathScreen = null;
        _screen = GorlornScreen.Menu;
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
            Hud = new HUD(this);
            _isInitialized = true;
            return;
        }

        switch (_screen)
        {
            case Menu:
                _background.update(dt);
                break;

            case Game:
                _background.update(dt);
                if (_heroSummonEffect != null)
                {
                    if (_heroSummonEffect.update(dt))
                    {
                        //The hero summoning effect is complete!
                        _heroSummonEffect = null;
                        return;
                    }
                }
                else
                {
                    EnemyManager.update(dt);
                    BulletManager.update(dt);
                    Hero.update(dt);
                    Hud.update(dt);
                    HeartManager.update(dt);
                }
                break;

            case Death:
                _deathScreen.update(dt);
                break;
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

        if (_screen == GorlornScreen.Menu)
        {
            _background.draw(canvas);
            canvas.drawBitmap(Bitmaps.Title, getXFromPercent(0.15f), getYFromPercent(0.15f), null);
            return;
        }

        _background.draw(canvas);
        BulletManager.Draw(canvas);
        EnemyManager.draw(canvas);

        if (_heroSummonEffect != null)
        {
            _heroSummonEffect.draw(canvas);
        }
        else
        {
            Hud.draw(canvas);
            Hero.draw(canvas);
        }
        HeartManager.draw(canvas);

        if (_screen == GorlornScreen.Death)
        {
            _deathScreen.draw(canvas);
        }
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

    private void toggleMenuControlVisibility(boolean show)
    {
        int visibility = show ? View.VISIBLE : View.GONE;

        _activity.findViewById(R.id.buttonsView).setVisibility(visibility);
        _activity.findViewById(R.id.adView).setVisibility(visibility);
    }

    //endregion
}
