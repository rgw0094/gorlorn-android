package gorlorn.Entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.text.MessageFormat;
import java.util.Date;

import gorlorn.Gorlorn;

/**
 * Displays the number of points gained from killing an enemy to the user.
 * <p/>
 * <p/>
 * Created by Rob on 1/17/2016.
 */
public class Points
{
    private Gorlorn _gorlorn;
    private long _timeCreatedMs;
    private String _pointString;
    private Paint _paint;
    private float _x;
    private float _y;
    private float _speed;
    private long _lifeTimeMs;

    /**
     * @param gorlorn
     * @param points
     * @param x
     * @param y
     */
    public Points(Gorlorn gorlorn, long points, int chainCount, float x, float y)
    {
        _gorlorn = gorlorn;
        _pointString = MessageFormat.format("{0}", points);
        _timeCreatedMs = new Date().getTime();
        _x = x;
        _y = y;

        _paint = new Paint();
        _paint.setStyle(Paint.Style.FILL);
        _paint.setAntiAlias(true);
        _paint.setTextSize(_gorlorn.getYFromPercent(0.03f + (float)chainCount * 0.005f));

        //If its a big chain make the points red
        if (points > Math.pow(2, 5))
        {
            _paint.setARGB(255, 255, 0, 0);
            _lifeTimeMs = 1250;
            _speed = (float) _gorlorn.ScreenWidth * 0.15f;
        }
        else
        {
            _paint.setARGB(255, 255, 255, 255);
            _lifeTimeMs = 800;
            _speed = (float) _gorlorn.ScreenHeight * 0.1f;
        }
    }

    public boolean Update(float dt)
    {
        long now = new Date().getTime();
        if (now - _timeCreatedMs > _lifeTimeMs)
            return true;

        _y += (_speed * dt);

        return false;
    }

    public void Draw(Canvas canvas)
    {
        canvas.drawText(_pointString, _x, _y, _paint);
    }
}
