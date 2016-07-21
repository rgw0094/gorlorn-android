package gorlorn.UI;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Health Bar user interface element.
 * <p/>
 * Created by Rob on 1/19/2016.
 */
public class HealthBar
{
    private Orientation _orientation;
    private Rect _borderRect;
    private int _thickness;
    private int _length;
    private static Paint _borderPaint;
    private Paint _fillPaint;
    private static Paint _emptyPaint;

    /**
     * Constructs a new HealthBar
     *
     * @param orientation Whether the bar is horizontal or vertical
     * @param x           Left of the bar
     * @param y           Top of the bar
     * @param thickness   Thickness of the bar (width if vertical, height if horizontal)
     * @param length      Length of the bar (width if horizontal, length if vertical)
     */
    public HealthBar(Orientation orientation, int x, int y, int thickness, int length, Paint paint)
    {
        _orientation = orientation;
        _thickness = thickness;
        _length = length;
        _fillPaint = paint;
        _fillPaint.setAntiAlias(true);

        if (_orientation == Orientation.Horizontal)
        {
            _borderRect = new Rect(x, y, x + length, y + thickness);
        }
        else
        {
            _borderRect = new Rect(x, y, x + thickness, y + length);
        }

        if (_borderPaint == null)
        {
            _borderPaint = new Paint();
            _borderPaint.setAntiAlias(true);
            _borderPaint.setARGB(255, 55, 55, 55);

            _emptyPaint = new Paint();
            _emptyPaint.setAntiAlias(true);
            _emptyPaint.setARGB(255, 22, 22, 22);
        }
    }

    /**
     * Draws the health bar.
     *
     * @param canvas        Canvas upon which to draw the health bar
     * @param healthPercent Health percentage to draw
     */
    public void draw(Canvas canvas, float healthPercent)
    {
        Rect fillRect = null;
        Rect emptyRect = null;

        int borderPadding = 2;
        int fillLength = (int) (((float) _length - (float) borderPadding * 2.0f) * healthPercent);

        if (_orientation == Orientation.Horizontal)
        {
            fillRect = new Rect(
                    _borderRect.left + borderPadding,
                    _borderRect.top + borderPadding,
                    _borderRect.left + borderPadding + fillLength,
                    _borderRect.bottom - borderPadding);

            emptyRect = new Rect(
                    _borderRect.left + borderPadding + fillLength,
                    _borderRect.top + borderPadding,
                    _borderRect.right - borderPadding,
                    _borderRect.bottom - borderPadding);
        }
        else
        {
            //TODO:
        }

        canvas.drawRect(_borderRect, _borderPaint);
        canvas.drawRect(fillRect, _fillPaint);
        canvas.drawRect(emptyRect, _emptyPaint);
    }
}