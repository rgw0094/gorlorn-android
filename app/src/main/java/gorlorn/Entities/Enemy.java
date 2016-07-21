package gorlorn.Entities;

import android.graphics.Bitmap;
import android.graphics.Point;

import gorlorn.Constants;
import gorlorn.Gorlorn;

/**
 * Created by Rob on 1/14/2016.
 */
public class Enemy extends Entity
{
    private Gorlorn _gorlorn;
    private boolean _hasEnteredScreen;

    /**
     * Constructs a new enemy.
     *
     * @param gorlorn
     * @param sprite  The sprite to represent the enemy
     */
    public Enemy(Gorlorn gorlorn, Bitmap sprite)
    {
        super(sprite);

        _gorlorn = gorlorn;
    }

    /**
     * Override the rectangle-based hit detection since the enemies are spheres.
     *
     * @param point
     * @return
     */
    @Override
    public boolean testHit(Point point)
    {
        double centerX = X - (double) Width * 0.5f;
        double centerY = Y - (double) Height * 0.5f;

        double dist = Math.sqrt(Math.pow((double) point.x - centerX, 2) + Math.pow((double) point.y - centerY, 2));

        return Math.abs(dist) < Width;
    }

    @Override
    public boolean update(float dt)
    {
        super.update(dt);

        //Check for collision with hero
        if (_gorlorn.getHero().testHit(this))
        {
            _gorlorn.getHero().dealDamage(Constants.EnemyDamage);
            return true;
        }

        //Bounce off left/right edges
        if (X <= Width * 0.5f || X >= _gorlorn.ScreenWidth - (Width * 0.5f))
        {
            Vx *= -1;
        }

        //Bounce off top/bottom edges - don't check for this until the enemy has fully entered the screen the first time
        if (_hasEnteredScreen)
        {
            if (Y <= Height * 0.5f || Y >= _gorlorn.ScreenHeight - (Height * 0.5f))
            {
                Vy *= -1;
            }

            //Keep the enemy within the game area
            X = Math.min(Math.max(X, Width * 0.5f), _gorlorn.ScreenWidth - (Width * 0.5f));
            Y = Math.min(Math.max(Y, Height * 0.5f), _gorlorn.ScreenHeight - (Height * 0.5f));
        }
        else if (Y > Height)
        {
            _hasEnteredScreen = true;
        }

        return false;
    }
}
