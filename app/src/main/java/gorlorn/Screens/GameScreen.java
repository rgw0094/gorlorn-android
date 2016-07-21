package gorlorn.Screens;

import android.graphics.Canvas;

import gorlorn.Gorlorn;
import gorlorn.UI.HeroSummonEffect;

/**
 * The screen where the game is played.
 * <p/>
 * Created by Rob on 6/28/2016.
 */
public class GameScreen extends ScreenBase
{
    private HeroSummonEffect _heroSummonEffect;

    public GameScreen(Gorlorn gorlorn)
    {
        super(gorlorn);
    }

    @Override
    public void show(ScreenBase previousScreen)
    {
        //Show a transition effect based upon which screen we are coming from
        boolean comingFromMenu = previousScreen != null && previousScreen.getClass() == MenuScreen.class;
        _heroSummonEffect = new HeroSummonEffect(_gorlorn, comingFromMenu);
    }

    @Override
    public boolean leave()
    {
        return true;
    }

    @Override
    public boolean update(float dt)
    {
        if (_heroSummonEffect != null)
        {
            if (_heroSummonEffect.update(dt))
            {
                //The hero summoning effect is complete!
                _heroSummonEffect = null;
                return false;
            }
        }
        else
        {
            //TODO: getters
            _gorlorn.getEnemyManager().update(dt);
            _gorlorn.getBulletManager().update(dt);
            _gorlorn.getHero().update(dt);
            _gorlorn.getHud().update(dt);
            _gorlorn.getHeartManager().update(dt);
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas)
    {
        _gorlorn.getBackground().draw(canvas);
        _gorlorn.getBulletManager().Draw(canvas);
        _gorlorn.getEnemyManager().draw(canvas);

        if (_heroSummonEffect != null)
        {
            _heroSummonEffect.draw(canvas);
        }
        else
        {
            _gorlorn.getHud().draw(canvas);
            _gorlorn.getHero().draw(canvas);
        }
        _gorlorn.getHeartManager().draw(canvas);
    }
}