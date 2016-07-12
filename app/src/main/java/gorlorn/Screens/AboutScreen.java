package gorlorn.Screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import gorlorn.Bitmaps;
import gorlorn.Gorlorn;
import gorlorn.activities.BuildConfig;

/**
 * Created by Rob on 7/2/2016.
 */
public class AboutScreen extends ScreenBase
{
    private Gorlorn _gorlorn;
    private long _timeEnteredMs;
    private long _timeLeftMs;
    private static int GridAnimationDurationMs = 300;
    private TextPaint _versionPaint;
    private Paint _messagePaint;
    private boolean _adShownYet;


    /**
     * Constructs a new AboutScreen.
     *
     * @param gorlorn
     */
    public AboutScreen(Gorlorn gorlorn)
    {
        super(gorlorn);

        _gorlorn = gorlorn;
        _versionPaint = gorlorn.createTextPaint(0.04f);

        _messagePaint = gorlorn.createTextPaint(0.05f);
        _messagePaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void show(ScreenBase previousScreen)
    {
        _timeEnteredMs = new Date().getTime();
        _gorlorn.getBackground().retractGrid(GridAnimationDurationMs);
    }

    @Override
    public boolean leave()
    {
        _gorlorn.hideAd();
        _gorlorn.getBackground().extendGrid(GridAnimationDurationMs);
        _timeLeftMs = new Date().getTime();
        return false;
    }

    @Override
    public boolean update(float dt)
    {
        long now = new Date().getTime();
        if (!_adShownYet && now - _timeEnteredMs > GridAnimationDurationMs)
        {
            _gorlorn.showAd();
            _adShownYet = true;
        }

        //Return true once leave() has been requested and the background grid has finished extending
        return _timeLeftMs != 0 && now - _timeLeftMs > GridAnimationDurationMs;
    }

    @Override
    public void draw(Canvas canvas)
    {
        _gorlorn.getBackground().draw(canvas);
        canvas.drawBitmap(Bitmaps.Title, _gorlorn.getXFromPercent(0.15f), _gorlorn.getYFromPercent(0.15f), null);

        if (new Date().getTime() - _timeEnteredMs > GridAnimationDurationMs && _timeLeftMs == 0)
        {
            float versionY = _gorlorn.getYFromPercent(0.19f) + Bitmaps.Title.getHeight();
            canvas.drawText("Version " + BuildConfig.VERSION_NAME, _gorlorn.getXFromPercent(0.15f), versionY, _versionPaint);

            canvas.drawText("This simple game was created for fun and to learn Android", _gorlorn.getXFromPercent(0.5f), _gorlorn.getYFromPercent(0.5f), _messagePaint);
            canvas.drawText("development. To check out some REAL (and free!) games, go to:", _gorlorn.getXFromPercent(0.5f), _gorlorn.getYFromPercent(0.57f), _messagePaint);
            canvas.drawText("              www.smileysmazehunt.com", _gorlorn.getXFromPercent(0.5f), _gorlorn.getYFromPercent(0.64f), _messagePaint);
        }
    }
}