package gorlorn.Entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Date;

import gorlorn.Gorlorn;

/**
 * Created by Rob on 1/14/2016.
 */
public class Entity
{
    protected Rect _hitBox;
    private boolean _isBlinking;
    private long _timeCreatedMs;
    protected long _timeStartedBlinkingMs;
    private int _blinkDurationMs;
    private float _blinkOpacityDelta;

    public Bitmap Sprite;
    private Paint _hitBoxPaint;
    public float X;
    public float Y;
    public int Width;
    public int Height;
    public float Opacity;
    public float Vx;
    public float Vy;
    public float MaxV = Float.MAX_VALUE;
    public float Ax;
    public float Ay;

    public Entity(Bitmap sprite)
    {
        _timeCreatedMs = new Date().getTime();
        Sprite = sprite;
        Width = sprite.getWidth();
        Height = sprite.getWidth();
        Opacity = 1.0f;
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
        Vx = Math.max(-MaxV, Math.min(MaxV, Vx + Ax * dt));
        Vy = Math.max(-MaxV, Math.min(MaxV, Vy + Ay * dt));

        _hitBox = new Rect(
                (int) (X - (float) Width * 0.48f),
                (int) (Y - (float) Height * 0.48f),
                (int) (X + (float) Width * 0.48f),
                (int) (Y + (float) Height * 0.48f));

        if (_isBlinking)
        {
            if (new Date().getTime() >= _timeStartedBlinkingMs + _blinkDurationMs)
            {
                Opacity = 1.0f;
                _isBlinking = false;
            }
            else
            {
                Opacity += dt * _blinkOpacityDelta;
                if (Opacity >= 1.0)
                {
                    Opacity = 1.0f;
                    _blinkOpacityDelta *= -1;
                }
                else if (Opacity <= 0.0d)
                {
                    Opacity = 0.0f;
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
        if (Opacity != 1.0f)
        {
            paint = new Paint();
            paint.setARGB((int) (255.0f * Opacity), 255, 255, 255);
        }

        canvas.drawBitmap(Sprite, X - (float) Width / 2.0f, Y - (float) Height / 2.0f, paint);

        if (Gorlorn.IsDebugMode)
        {
            if (_hitBoxPaint == null)
            {
                _hitBoxPaint = new Paint();
                _hitBoxPaint.setARGB(255, 0, 255, 0);
                _hitBoxPaint.setStyle(Paint.Style.STROKE);
            }
            canvas.drawRect(_hitBox, _hitBoxPaint);
        }
    }

    /**
     * Gets the time this Entity was created in milliseconds from the origin.
     *
     * @return
     */
    public long getTimeCreatedMs()
    {
        return _timeCreatedMs;
    }

    /**
     * Returns whether or not this entity is older than the given entity.
     *
     * @param entity
     * @return
     */
    public boolean isOlderThan(Entity entity)
    {
        return _timeCreatedMs < entity.getTimeCreatedMs();
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
        _blinkOpacityDelta = -17.0f;
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
