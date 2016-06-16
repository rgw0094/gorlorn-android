package gorlorn.UI;

import android.graphics.Canvas;
import android.graphics.Paint;

import gorlorn.activities.GorlornActivity;

/**
 * Renders the background of the gameplay area
 * <p/>
 * Created by Rob on 1/19/2016.
 */
public class Background
{
    private static int NumPoints = 10;

    private GorlornActivity _gorlorn;
    private Paint _linePaint;
    private Paint _backgroundPaint;
    private float[] _horizontalLineStarts = new float[NumPoints];

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
        canvas.drawPaint(_backgroundPaint);

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