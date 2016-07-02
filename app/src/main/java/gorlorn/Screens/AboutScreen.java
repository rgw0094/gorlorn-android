package gorlorn.Screens;

import android.graphics.Canvas;
import android.graphics.Color;
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
    private TextView _messageView;
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

        _messageView = new TextView(gorlorn.getActivity().getApplicationContext());
        _messageView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        _messageView.setLayoutParams(llp);
        _messageView.setTextSize(gorlorn.getYFromPercent(0.02f));
        _messageView.setGravity(Gravity.CENTER_HORIZONTAL);

        String message = "This simple game was created for fun and to learn Android development. To check out some REAL (and free!) games, go to: www.smileysmazehunt.com";
        _messageView.setText(message);
        _messageView.setDrawingCacheEnabled(true);
        _messageView.measure(View.MeasureSpec.makeMeasureSpec(gorlorn.getXFromPercent(0.7f), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(gorlorn.ScreenHeight, View.MeasureSpec.EXACTLY));
        _messageView.layout(0, 0, _messageView.getMeasuredWidth(), _messageView.getMeasuredHeight());
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
            canvas.drawBitmap(_messageView.getDrawingCache(), _gorlorn.getXFromPercent(0.15f), versionY + _gorlorn.getYFromPercent(0.1f), null);
        }
    }
}