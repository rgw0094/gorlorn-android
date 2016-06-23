package gorlorn.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

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

    private Bitmap _backgroundBitmap;
    private Bitmap _titleBitmap;
    private RenderLoopBase _renderLoop;
    private Paint _linePaint;
    private float[] _horizontalLineStarts = new float[NumPoints];
    private float _backgroundXOffset;
    private float _backgroundYOffset;
    private float _time;
    private boolean _drawTitle;

    /**
     * Constructs a new Background.
     *
     * @param renderLoop
     */
    public Background(RenderLoopBase renderLoop, boolean drawTitle)
    {
        _renderLoop = renderLoop;
        _drawTitle = drawTitle;
        _linePaint = new Paint();
        _linePaint.setARGB(255, 247, 33, 155);
        _linePaint.setStrokeWidth(_renderLoop.getYFromPercent(0.005f));
        _backgroundBitmap = _renderLoop.createBitmap(R.drawable.space, _renderLoop.getXFromPercent(1.1f), _renderLoop.getYFromPercent(1.1f));

        for (int i = 0; i < NumPoints; i++)
        {
            _horizontalLineStarts[i] = _renderLoop.getXFromPercent((1.0f / (float) NumPoints) * (float) i);
        }

        if (_drawTitle)
        {
            int titleWidth = renderLoop.getXFromPercent(0.7f);
            int titleHeight = renderLoop.getYFromPercent(0.7f / 4.347826086956522f);
            _titleBitmap = renderLoop.createBitmap(R.drawable.title, titleWidth, titleHeight);
        }
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

        for (int i = 0; i < NumPoints; i++)
        {
            _horizontalLineStarts[i] = (_horizontalLineStarts[i] + speed * dt) % width;
        }
    }

    /**
     * Draws the background
     *
     * @param canvas The canvas upon which to draw the background
     */
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(_backgroundBitmap, _backgroundXOffset, _backgroundYOffset, null);

        if (_drawTitle)
        {
            canvas.drawBitmap(_titleBitmap, _renderLoop.getXFromPercent(0.15f), _renderLoop.getYFromPercent(0.15f), null);
        }

        float topY = _renderLoop.getYFromPercent(0.45f);

        //Draw the vertical lines
        float centerX = _renderLoop.ScreenWidth * 0.5f;
        for (int i = 0; i < NumPoints; i++)
        {
            float distFromCenter = centerX - _horizontalLineStarts[i];
            float bottomX = centerX - (distFromCenter * 3.2f);

            canvas.drawLine(_horizontalLineStarts[i], topY, bottomX, _renderLoop.ScreenHeight, _linePaint);
        }

        //Draw the horizontal lines
        float y = topY;
        float yInterval = _renderLoop.getYFromPercent(0.05f);
        while (y < _renderLoop.ScreenHeight)
        {
            canvas.drawLine(0, y, _renderLoop.ScreenWidth, y, _linePaint);
            y += yInterval;
            yInterval *= 1.3f;
        }
    }
}