package gorlorn;

import android.graphics.Bitmap;

import gorlorn.Framework.RenderLoopBase;
import gorlorn.activities.R;

/**
 * Created by Rob on 6/24/2016.
 */
public class Bitmaps
{
    private static boolean _isLoaded;

    public static Bitmap Hero;
    public static Bitmap Background;
    public static Bitmap Title;

    public static void Load(RenderLoopBase renderLoop)
    {
        if (_isLoaded)
            return;

        Hero = renderLoop.createBitmapByWidthPercent(R.drawable.hero, Constants.HeroDiameter);
        Background = renderLoop.createBitmap(R.drawable.space, renderLoop.getXFromPercent(1.1f), renderLoop.getYFromPercent(1.1f));
        Title = renderLoop.createBitmap(R.drawable.title, renderLoop.getXFromPercent(0.7f), renderLoop.getYFromPercent(0.7f / 4.347826086956522f));

        _isLoaded = true;
    }
}