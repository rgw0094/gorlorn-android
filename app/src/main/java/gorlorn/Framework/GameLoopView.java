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
    private RenderLoopBase _renderLoop;

    public GameLoopView(RenderLoopBase renderLoop)
    {
        super(renderLoop.getActivity().getApplicationContext());

        _renderLoop = renderLoop;

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        _thread = new GameLoopThread(holder, renderLoop);
        _thread.start();

        setFocusable(true);
    }

    public boolean onTouchEvent(MotionEvent me)
    {
        _renderLoop.handleInputEvent(me);
        return true;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        _renderLoop.ScreenWidth = width;
        _renderLoop.ScreenHeight = height;
    }

    public void surfaceCreated(SurfaceHolder holder)
    {
        _renderLoop.ScreenWidth = getMeasuredWidth();
        _renderLoop.ScreenHeight = getMeasuredHeight();

        _thread.setRunning(true);
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        _thread.setRunning(false);
    }
}