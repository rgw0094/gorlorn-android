package gorlorn.Entities;

import android.graphics.Bitmap;

import java.util.Date;

import gorlorn.activities.GorlornActivity;

/**
 * Created by Rob on 1/14/2016.
 */
public class Bullet extends Entity
{
    private GorlornActivity _gorlornActivity;
    private long _lifeTimeMs;
    private long _createdTimeMs;

    public int ChainCount;

    /**
     * Constructs a bullet that will dissapear after the specified number of milliseconds.
     *
     * @param sprite
     * @param lifetimeMs
     */
    public Bullet(GorlornActivity gorlornActivity, Bitmap sprite, float x, float y, float speed, double angle, int chainCount, long lifetimeMs)
    {
        super(sprite);

        _gorlornActivity = gorlornActivity;
        _lifeTimeMs = lifetimeMs;
        ChainCount = chainCount;
        _createdTimeMs = new Date().getTime();

        X = x;
        Y = y;
        Vx = speed * (float) Math.cos(angle);
        Vy = speed * (float) Math.sin(angle);
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
        if (!_gorlornActivity.GameArea.contains(_hitBox))
            return true;

        return !_gorlornActivity.GameArea.contains(_hitBox);
    }
}
