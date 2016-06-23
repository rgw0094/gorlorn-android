package gorlorn.Framework;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Rob on 1/20/2016.
 */
public abstract class GameLoopActivity extends Activity
{
    public int ScreenWidth;
    public int ScreenHeight;

    //region Activity Overrides

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(getActivityView());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ScreenWidth = size.x;
        ScreenHeight = size.y;
    }

    //endregion

    //region Abstract Methods

    public abstract void handleInputEvent(MotionEvent me);

    public abstract void update(float dt);

    public abstract void draw(Canvas canvas);

    public abstract void handleException(Exception e);

    protected abstract View getActivityView();

    //endregion

    //region Public Methods

    /**
     * Creates a bitmap sized as a percentage of the screen.
     *
     * @param id
     * @param screenWidthPercent
     * @return
     */
    public Bitmap createBitmapByWidthPercent(int id, float screenWidthPercent)
    {
        Resources resources = getApplicationContext().getResources();
        int diameter = getXFromPercent(screenWidthPercent);
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, id), diameter, diameter, true);
    }

    /**
     * Creates a bitmap sized by absolute pixel count.
     *
     * @param id
     * @param width
     * @param height
     * @return
     */
    public Bitmap createBitmap(int id, int width, int height)
    {
        Resources resources = getApplicationContext().getResources();
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, id), width, height, true);
    }

    /**
     * Gets the x pixel coordinate that is the given percent across the screen.
     *
     * @param percent
     * @return
     */
    public int getXFromPercent(float percent)
    {
        return (int) ((float) ScreenWidth * percent);
    }

    /**
     * Gets the y pixel coordinate that is the given percent across the screen.
     *
     * @param percent
     * @return
     */
    public int getYFromPercent(float percent)
    {
        return (int) ((float) ScreenHeight * percent);
    }

    //endregion
}
