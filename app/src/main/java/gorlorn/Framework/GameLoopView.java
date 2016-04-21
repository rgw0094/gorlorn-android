package gorlorn.Framework;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Rob on 1/20/2016.
 */
public class GameLoopView extends SurfaceView implements SurfaceHolder.Callback
{
    private GameLoopThread _thread;
    private GameLoopActivity _activity;

    public GameLoopView(GameLoopActivity activity)
    {
        super(activity.getApplicationContext());

        _activity = activity;

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        _thread = new GameLoopThread(holder, activity);
        _thread.start();

        setFocusable(true);
    }

    public boolean onTouchEvent(MotionEvent me)
    {
        _activity.handleInputEvent(me);
        return true;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    public void surfaceCreated(SurfaceHolder holder)
    {
        _thread.setRunning(true);
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        _thread.setRunning(false);
    }
}