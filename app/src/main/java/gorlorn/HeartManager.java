package gorlorn;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Iterator;
import java.util.LinkedList;

import gorlorn.Entities.Heart;
import gorlorn.activities.GorlornActivity;
import gorlorn.activities.R;

/**
 * Manages hearts and spawning hearts.
 * <p/>
 * Created by Rob on 1/20/2016.
 */
public class HeartManager
{
    private GorlornActivity _gorlorn;
    private LinkedList<Heart> _hearts = new LinkedList<>();
    private Bitmap _heartSprite;

    public HeartManager(GorlornActivity gorlorn)
    {
        _gorlorn = gorlorn;
        _heartSprite = gorlorn.createBitmapByWidthPercent(R.drawable.heart, Constants.HeartDiameter);
    }

    /**
     * Spawns a new heart at the specified location.
     *
     * @param x
     * @param y
     */
    public void spawnHeart(int x, int y)
    {
        _hearts.add(new Heart(_gorlorn, _heartSprite, x, y));
    }

    /**
     * Updates all hearts
     *
     * @param dt
     */
    public void update(float dt)
    {
        for (Iterator<Heart> iterator = _hearts.iterator(); iterator.hasNext(); )
        {
            Heart heart = iterator.next();
            if (heart.update(dt))
            {
                iterator.remove();
            }
        }
    }

    /**
     * Draws all hearts
     *
     * @param canvas
     */
    public void draw(Canvas canvas)
    {
        for (Heart heart : _hearts)
        {
            heart.draw(canvas);
        }
    }
}
