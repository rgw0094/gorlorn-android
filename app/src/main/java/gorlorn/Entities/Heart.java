package gorlorn.Entities;

import android.graphics.Bitmap;

import java.util.Date;

import gorlorn.Constants;
import gorlorn.Gorlorn;

/**
 * A heart that restores the hero's health.
 * <p/>
 * Created by Rob on 1/19/2016.
 */
public class Heart extends Entity
{
    private Gorlorn _gorlorn;
    private long _timeEnteredPhaseMs;
    private Phase _phase;

    private enum Phase
    {
        Descending,
        Paused,
        Blinking
    }

    /**
     * Constructs a new heart.
     *
     * @param gorlorn The game activity
     * @param sprite  The sprite to draw to represent the heart
     * @param x       The x coordinate at which to spawn the heart
     * @param y       The y coordinate at which to spawn the heart
     */
    public Heart(Gorlorn gorlorn, Bitmap sprite, int x, int y)
    {
        super(sprite);

        _gorlorn = gorlorn;
        _phase = Phase.Descending;
        X = x;
        Y = y;
        Vy = _gorlorn.getSpeed(Constants.HeartSpeed);
    }

    @Override
    public boolean update(float dt)
    {
        super.update(dt);

        //Restore health if we hit the hero then destroy this heart
        if (_gorlorn.getHero().testHit(this))
        {
            _gorlorn.getGameStats().heartsCollected++;
            _gorlorn.getHero().restoreHealth();
            return true;
        }

        long now = new Date().getTime();

        if (_phase == Phase.Descending)
        {
            if (Y > _gorlorn.getYFromPercent(0.96f))
            {
                Y = _gorlorn.getYFromPercent(0.96f);
                _timeEnteredPhaseMs = now;
                Vy = 0.0f;
                _phase = Phase.Paused;
            }
        }
        else if (_phase == Phase.Paused)
        {
            if (now - _timeEnteredPhaseMs > 3000)
            {
                _timeEnteredPhaseMs = now;
                startBlinking(2000);
                _phase = Phase.Blinking;
            }
        }
        else if (_phase == Phase.Blinking)
        {
            if (now - _timeEnteredPhaseMs > 2000)
            {
                return true;
            }
        }
        return false;
    }
}
