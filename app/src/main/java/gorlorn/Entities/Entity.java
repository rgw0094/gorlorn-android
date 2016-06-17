package gorlorn.Entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Date;

/**
 * Created by Rob on 1/14/2016.
 */
public class Entity
{
    protected Rect _hitBox;
    private boolean _isBlinking;
    private long _timeStartedBlinkingMs;
    private int _blinkDurationMs;
    private float _blinkOpacityDelta;
    private float _opacity = 1.0f;

    public Bitmap Sprite;
    public float X;
    public float Y;
    public int Width;
    public int Height;
    public float Vx;
    public float Vy;
    public float Ax;
    public float Ay;

    public Entity(Bitmap sprite)
    {
        Sprite = sprite;
        Width = sprite.getWidth();
        Height = sprite.getWidth();
        _hitBox = new Rect(0, 0, 0, 0);
    }

    /**
     * Updates the entity and returns whether or not it died this frame.
     *
     * @param dt
     * @return
     */
    public boolean update(float dt)
    {
        X += Vx * dt;
        Y += Vy * dt;
        Vx += Ax * dt;
        Vy += Ay * dt;

        _hitBox = new Rect((int) X, (int) Y, (int) X + Width, (int) Y + Height);

        if (_isBlinking)
        {
            if (new Date().getTime() >= _timeStartedBlinkingMs + _blinkDurationMs)
            {
                _opacity = 1.0f;
                _isBlinking = false;
            }
            else
            {
                _opacity += dt * _blinkOpacityDelta;
                if (_opacity >= 1.0)
                {
                    _opacity = 1.0f;
                    _blinkOpacityDelta *= -1;
                }
                else if (_opacity <= 0.0d)
                {
                    _opacity = 0.0f;
                    _blinkOpacityDelta *= -1;
                }
            }
        }

        return false;
    }

    /**
     * Draws the entity.
     *
     * @param canvas
     */
    public void draw(Canvas canvas)
    {
        Paint paint = null;
        if (_opacity != 1.0f)
        {
            paint = new Paint();
            paint.setARGB((int)(255.0f * _opacity), 255, 255, 255);
        }

        canvas.drawBitmap(Sprite, X - (float) Width / 2.0f, Y - (float) Height / 2.0f, paint);
    }

    /**
     * Makes the entity blink for the specified duration.
     *
     * @param durationMs
     */
    public void startBlinking(int durationMs)
    {
        _timeStartedBlinkingMs = new Date().getTime();
        _blinkDurationMs = durationMs;
        _isBlinking = true;
        _blinkOpacityDelta = -13.0f;
    }

    /**
     * Returns whether or not the given point intersects with the entity.
     *
     * @param point
     * @return
     */
    public boolean testHit(Point point)
    {
        if (_hitBox == null)
            return false;

        return _hitBox.contains(point.x, point.y);
    }

    /**
     * Returns whether or not the given rectangle intersects with the entity.
     *
     * @param rect
     * @return
     */
    public boolean testHit(Rect rect)
    {
        if (_hitBox == null)
            return false;

        return _hitBox.contains(rect);
    }

    /**
     * Returns whether or not this entity collides with another.
     *
     * @param entity
     * @return
     */
    public boolean testHit(Entity entity)
    {
        return entity._hitBox.intersect(_hitBox);
    }

    public boolean getIsBlinking()
    {
        return _isBlinking;
    }
}
