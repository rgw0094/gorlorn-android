package gorlorn.Entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Rob on 1/14/2016.
 */
public class Entity
{
    protected Rect _hitBox;
    private Bitmap _sprite;

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
        _sprite = sprite;
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

        return false;
    }

    /**
     * Draws the entity.
     *
     * @param canvas
     */
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(_sprite, X - (float) Width / 2.0f, Y - (float) Height / 2.0f, null);
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
}
