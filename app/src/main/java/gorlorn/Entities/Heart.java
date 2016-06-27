package gorlorn.Entities;

import android.graphics.Bitmap;

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

    /**
     * Constructs a new heart.
     *
     * @param gorlorn The game activity
     * @param sprite  The sprite to draw to represent the heart
     * @param x The x coordinate at which to spawn the heart
     * @param y The y coordinate at which to spawn the heart
     */
    public Heart(Gorlorn gorlorn, Bitmap sprite, int x, int y)
    {
        super(sprite);

        _gorlorn = gorlorn;
        X = x;
        Y = y;

        //The heart will fall straight down until it goes off the screen.
        Vy = gorlorn.getSpeed(Constants.HeartSpeed);
    }

    @Override
    public boolean update(float dt)
    {
        super.update(dt);

        //Restore health if we hit the hero then destroy this heart
        if (_gorlorn.Hero.testHit(this))
        {
            _gorlorn.Hero.restoreHealth();
            return true;
        }

        //Disappear when the heart goes off the screen.
        return Y > _gorlorn.ScreenHeight + Height;
    }
}
