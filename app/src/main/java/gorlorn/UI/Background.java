package gorlorn.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Date;

import gorlorn.activities.GorlornActivity;
import gorlorn.activities.R;

/**
 * Renders the background of the gameplay area
 * <p/>
 * Created by Rob on 1/19/2016.
 */
public class Background
{
    private static int NumPoints = 10;

    private Bitmap _backgroundBitmap;
    private GorlornActivity _gorlorn;
    private Paint _linePaint;
    private Paint _backgroundPaint;
    private float[] _horizontalLineStarts = new float[NumPoints];
    private float _backgroundXOffset;
    private float _backgroundYOffset;
    private float _time;

    /**
     * Constructs a new Background.
     *
     * @param gorlornActivity
     */
    public Background(GorlornActivity gorlornActivity)
    {
        _gorlorn = gorlornActivity;
        _linePaint = new Paint();
        _linePaint.setARGB(255, 247, 33, 155);
        _linePaint.setStrokeWidth(_gorlorn.getYFromPercent(0.005f));
        _backgroundPaint = new Paint();
        _backgroundPaint.setARGB(255, 24, 20, 37);

        _backgroundBitmap = gorlornActivity.createBitmap(R.drawable.space, gorlornActivity.getXFromPercent(1.1f), gorlornActivity.getYFromPercent(1.1f));

        for (int i = 0; i < NumPoints; i++)
        {
            _horizontalLineStarts[i] = gorlornActivity.getXFromPercent((1.0f / (float) NumPoints) * (float) i);
        }
    }

    /**
     * Updates the background
     *
     * @param dt The time since the last update
     */
    public void update(float dt)
    {
        float width = _gorlorn.ScreenWidth;
        float speed = width * 0.1f;
        _time += dt;

        _backgroundXOffset = (float)_gorlorn.getXFromPercent(0.05f) * (float)Math.cos(_time / 2.0f) - (float)_gorlorn.getXFromPercent(0.05f);
        _backgroundYOffset = (float)_gorlorn.getYFromPercent(0.05f) * (float)Math.sin(_time / 2.0f) - (float)_gorlorn.getYFromPercent(0.05f);

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
        //canvas.drawPaint(_backgroundPaint);

        float topY = _gorlorn.getYFromPercent(0.45f);

        //Draw the vertical lines
        float centerX = _gorlorn.ScreenWidth * 0.5f;
        for (int i = 0; i < NumPoints; i++)
        {
            float distFromCenter = centerX - _horizontalLineStarts[i];
            float bottomX = centerX - (distFromCenter * 3.2f);

            canvas.drawLine(_horizontalLineStarts[i], topY, bottomX, _gorlorn.ScreenHeight, _linePaint);
        }

        //Draw the horizontal lines
        float y = topY;
        float yInterval = _gorlorn.getYFromPercent(0.05f);
        while (y < _gorlorn.ScreenHeight)
        {
            canvas.drawLine(0, y, _gorlorn.ScreenWidth, y, _linePaint);
            y += yInterval;
            yInterval *= 1.3f;
        }
    }
}