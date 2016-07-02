package gorlorn.Framework;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.MotionEvent;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Rob on 6/22/2016.
 */
public abstract class RenderLoopBase
{
    private Activity _activity;

    public int ScreenWidth;
    public int ScreenHeight;

    public RenderLoopBase(Activity activity)
    {
        _activity = activity;
    }

    //region Abstract Methods

    public abstract void handleInputEvent(MotionEvent me);

    public abstract void update(float dt);

    public abstract void draw(Canvas canvas);

    //endregion

    //region Public Methods

    public void handleException(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(_activity.getApplicationContext());
        dlgAlert.setMessage(sw.toString());
        dlgAlert.setTitle("ERROR IN PROGRAM");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);

//        dlgAlert.setPositiveButton("Ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //dismiss the dialog
//                    }
//                });

        dlgAlert.create().show();
    }

    public Activity getActivity()
    {
        return _activity;
    }

    /**
     * Creates a bitmap sized as a percentage of the screen.
     *
     * @param id
     * @param screenWidthPercent
     * @return
     */
    public Bitmap createBitmapByWidthPercent(int id, float screenWidthPercent)
    {
        Resources resources = _activity.getApplicationContext().getResources();
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
        Resources resources = _activity.getApplicationContext().getResources();
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, id), width, height, true);
    }

    /**
     * Creates a paint for drawing white text where the size is a given percentage of the screen  height
     * @param yScreenPercent
     * @return
     */
    public TextPaint createTextPaint(float yScreenPercent)
    {
        return createTextPaint(Color.WHITE, yScreenPercent);
    }

    /**
     * Creates a paint for drawing text where the size is a given percentage of the screen  height
     * @param color
     * @param yScreenPercent
     * @return
     */
    public TextPaint createTextPaint(int color, float yScreenPercent)
    {
        TextPaint paint = new TextPaint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextSize(getYFromPercent(yScreenPercent));
        return paint;
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
