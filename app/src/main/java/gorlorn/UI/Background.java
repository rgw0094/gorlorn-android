package gorlorn.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import gorlorn.Bitmaps;
import gorlorn.Framework.RenderLoopBase;
import gorlorn.activities.R;

/**
 * Renders the beautiful Gorlorn background.
 * <p/>
 * Created by Rob on 1/19/2016.
 */
public class Background
{
    private static int NumPoints = 10;

    //Hacks!
    private static float _backgroundXOffset;
    private static float _backgroundYOffset;
    private static float _horizontalLinesOffset;

    private RenderLoopBase _renderLoop;
    private Paint _linePaint;
    private float[] _horizontalLineStarts = new float[NumPoints];
    private float _time;
    private int _gridAnimationDurationMs;
    private boolean _isRetractingGrid;
    private boolean _isExpandingGrid;
    private float _gridTopY;
    private float _targetGridTopY;

    /**
     * Constructs a new Background.
     *
     * @param renderLoop The render loop used to draw the background.
     */
    public Background(RenderLoopBase renderLoop)
    {
        _renderLoop = renderLoop;
        _linePaint = new Paint();
        _linePaint.setARGB(255, 247, 33, 155);
        _linePaint.setStrokeWidth(_renderLoop.getYFromPercent(0.005f));

        _gridTopY = _targetGridTopY = _renderLoop.getYFromPercent(0.45f);

        for (int i = 0; i < NumPoints; i++)
        {
            _horizontalLineStarts[i] = _renderLoop.getXFromPercent((1.0f / (float) NumPoints) * (float) i);
        }
    }

    /**
     * Retracts the grid portion of the background (lowers it until its off the screen)
     *
     * @param durationMs The duration of the retract animation in milliseconds
     */
    public void retractGrid(int durationMs)
    {
        _gridAnimationDurationMs = durationMs;
        _isRetractingGrid = true;
    }

    /**
     * Extends the grid portion of the background (raises it off the bottom of the screen until it is back to normal).
     *
     * @param durationMs The duration of the retract animation in milliseconds
     */
    public void extendGrid(int durationMs)
    {
        _gridAnimationDurationMs = durationMs;
        _isExpandingGrid = true;
    }

    /**
     * Updates the background
     *
     * @param dt The time since the last update
     */
    public void update(float dt)
    {
        float width = _renderLoop.ScreenWidth;
        float speed = width * 0.1f;
        _time += dt;

        _backgroundXOffset = (float) _renderLoop.getXFromPercent(0.05f) * (float) Math.cos(_time / 2.0f) - (float) _renderLoop.getXFromPercent(0.05f);
        _backgroundYOffset = (float) _renderLoop.getYFromPercent(0.05f) * (float) Math.sin(_time / 2.0f) - (float) _renderLoop.getYFromPercent(0.05f);
        _horizontalLinesOffset += (speed * dt);

        if (_isRetractingGrid)
        {
            _gridTopY += _targetGridTopY / ((float)_gridAnimationDurationMs / 1000.0f) * dt;
            if (_gridTopY > _renderLoop.ScreenHeight)
                _isRetractingGrid = false;
        }
        else if (_isExpandingGrid)
        {
            _gridTopY -= _targetGridTopY / ((float)_gridAnimationDurationMs / 1000.0f) * dt;
            if (_gridTopY <= _targetGridTopY)
            {
                _gridTopY = _targetGridTopY;
                _isExpandingGrid = false;
            }
        }
    }

    /**
     * Draws the background
     *
     * @param canvas The canvas upon which to draw the background
     */
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(Bitmaps.Background, _backgroundXOffset, _backgroundYOffset, null);

        //Draw the vertical lines
        float centerX = _renderLoop.ScreenWidth * 0.5f;
        float bottomY = _renderLoop.ScreenHeight + (_gridTopY - _targetGridTopY);

        for (int i = 0; i < NumPoints; i++)
        {
            float lineStart = (_horizontalLineStarts[i] + _horizontalLinesOffset) % _renderLoop.ScreenWidth;
            float distFromCenter = centerX - lineStart;
            float bottomX = centerX - (distFromCenter * 3.2f);
            canvas.drawLine(lineStart, _gridTopY, bottomX, bottomY, _linePaint);
        }

        //Draw the horizontal lines
        float y = _gridTopY;
        float yInterval = _renderLoop.getYFromPercent(0.05f);
        while (y < _renderLoop.ScreenHeight)
        {
            canvas.drawLine(0, y, _renderLoop.ScreenWidth, y, _linePaint);
            y += yInterval;
            yInterval *= 1.3f;
        }
    }
}