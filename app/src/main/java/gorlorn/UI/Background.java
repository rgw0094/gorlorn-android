package gorlorn.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import gorlorn.activities.GorlornActivity;
import gorlorn.activities.R;

/**
 * The beautiful parallax background behind the gorlorn game area
 * <p/>
 * Created by Rob on 1/19/2016.
 */
public class Background
{
    private Bitmap _cloudLayerSprite;
    private Bitmap _mountainLayerSprite;
    private float _cloudXOffset;
    private float _mountainXOffset;
    private float _cloudSpeed;
    private float _mountainSpeed;
    private float _mountainYOffset;

    /**
     * Constructs a new Background.
     *
     * @param gorlornActivity
     */
    public Background(GorlornActivity gorlornActivity)
    {
        int width = (int) ((float) gorlornActivity.GameArea.height() * (1024.0f / 768.0f));
        int height = gorlornActivity.GameArea.height();

        _cloudLayerSprite = gorlornActivity.createBitmap(R.drawable.clouds, width, height);
        _mountainLayerSprite = gorlornActivity.createBitmap(R.drawable.mountains, width, height);

        _cloudSpeed = (float) gorlornActivity.GameArea.width() * 0.1f;
        _mountainSpeed = (float) gorlornActivity.GameArea.width() * 0.15f;

        _cloudXOffset = _mountainXOffset = gorlornActivity.GameArea.left;
        _mountainYOffset = gorlornActivity.getYFromPercent(0.2f);
    }

    /**
     * Updates the background
     *
     * @param dt The time since the last update
     */
    public void update(float dt)
    {
        _cloudXOffset += (_cloudSpeed * dt);
        if (_cloudXOffset >= _cloudLayerSprite.getWidth())
            _cloudXOffset = 0.0f;

        _mountainXOffset += (_mountainSpeed * dt);
        if (_mountainXOffset >= _mountainLayerSprite.getWidth())
            _mountainXOffset = 0.0f;
    }

    /**
     * Draws the background
     *
     * @param canvas The canvas upon which to draw the background
     */
    public void draw(Canvas canvas)
    {
        //TODO: clip offscreen stuff for better performance, make sprites smaller

        canvas.drawBitmap(_cloudLayerSprite, _cloudXOffset, 0, null);
        canvas.drawBitmap(_cloudLayerSprite, _cloudXOffset - _cloudLayerSprite.getWidth(), 0, null);
        canvas.drawBitmap(_cloudLayerSprite, _cloudXOffset + _cloudLayerSprite.getWidth(), 0, null);

        canvas.drawBitmap(_mountainLayerSprite, _mountainXOffset, _mountainYOffset, null);
        canvas.drawBitmap(_mountainLayerSprite, _mountainXOffset - _mountainLayerSprite.getWidth(), _mountainYOffset, null);
        canvas.drawBitmap(_mountainLayerSprite, _mountainXOffset + _mountainLayerSprite.getWidth(), _mountainYOffset, null);
    }
}
