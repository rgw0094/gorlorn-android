package gorlorn.Entities;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Date;

import gorlorn.Gorlorn;

/**
 * Created by Rob on 1/14/2016.
 */
public class Bullet extends Entity
{
    private Gorlorn _gorlorn;
    private long _lifeTimeMs;
    private long _createdTimeMs;
    private int _chainCount;

    /**
     * Constructs a bullet that will dissapear after the specified number of milliseconds.
     */
    public Bullet(Gorlorn gorlorn, Bitmap sprite, float x, float y, float speed, double angle, int chainCount, long lifetimeMs)
    {
        super(sprite);

        _gorlorn = gorlorn;
        _lifeTimeMs = lifetimeMs;
        _chainCount = chainCount;
        _createdTimeMs = new Date().getTime();

        X = x;
        Y = y;
        Vx = speed * (float) Math.cos(angle);
        Vy = speed * (float) Math.sin(angle);
    }

    /**
     * Returns whether or not this bullet was created as part of a chain reaction, as opposed to being fired
     * by the player.
     *
     * @return
     */
    public boolean isChainBullet()
    {
        return _chainCount > 1;
    }

    /**
     * Gets the "chain count" for this bullet.
     * @return
     */
    public int getChainCount()
    {
        return _chainCount;
    }

    @Override
    public boolean update(float dt)
    {
        //If this bullet has a finite lifetime, check if it has expired.
        if (_lifeTimeMs != 0)
        {
            long now = new Date().getTime();
            if (now - _createdTimeMs > _lifeTimeMs)
            {
                return true;
            }
        }

        super.update(dt);

        //Kill the bullet if its left the game area
        Rect gameArea = new Rect(0, 0, _gorlorn.ScreenWidth, _gorlorn.ScreenHeight);
        return !gameArea.contains(_hitBox);
    }
}