package gorlorn.Entities;

import android.graphics.Bitmap;

import gorlorn.Constants;
import gorlorn.activities.GorlornActivity;

/**
 * Created by Rob on 1/14/2016.
 */
public class Enemy extends Entity
{
    private GorlornActivity _gorlornActivity;
    private boolean _hasEnteredScreen;

    public Enemy(GorlornActivity gorlornActivity, Bitmap sprite)
    {
        super(sprite);

        _gorlornActivity = gorlornActivity;
    }

    @Override
    public boolean update(float dt)
    {
        super.update(dt);

        //Check for collision with hero
        if (_gorlornActivity.Hero.testHit(this))
        {
            _gorlornActivity.Hero.dealDamage(Constants.EnemyDamage);
            return true;
        }

        //Bounce off left/right edges
        if (X <= _gorlornActivity.GameArea.left + Width * 0.5f || X >= _gorlornActivity.GameArea.right - (Width * 0.5f))
        {
            Vx *= -1;
        }

        //Bounce off top/bottom edges - don't check for this until the enemy has fully entered the screen the first time
        if (_hasEnteredScreen)
        {
            if (Y <= _gorlornActivity.GameArea.top + Height * 0.5f || Y >= _gorlornActivity.GameArea.bottom - (Height * 0.5f))
            {
                Vy *= -1;
            }

            //Keep the enemy within the game area
            X = Math.min(Math.max(X, _gorlornActivity.GameArea.left + Width * 0.5f), _gorlornActivity.GameArea.right - (Width * 0.5f));
            Y = Math.min(Math.max(Y, _gorlornActivity.GameArea.top + Height * 0.5f), _gorlornActivity.GameArea.bottom - (Height * 0.5f));
        }
        else if (Y > Height)
        {
            _hasEnteredScreen = true;
        }

        return false;
    }
}
