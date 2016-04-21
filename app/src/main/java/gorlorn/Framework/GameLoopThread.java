package gorlorn.Framework;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.Date;

/**
 * Created by Rob on 1/20/2016.
 */
public class GameLoopThread extends Thread
{
    private SurfaceHolder _surfaceHolder;
    private boolean _running = false;
    private long _lastFrameMillis;
    private GameLoopActivity _activity;

    public GameLoopThread(SurfaceHolder surfaceHolder, GameLoopActivity gorlorn)
    {
        _surfaceHolder = surfaceHolder;
        _activity = gorlorn;
    }

    public void setRunning(boolean running)
    {
        synchronized (_surfaceHolder)
        {
            _running = running;
        }
    }

    @Override
    public void run()
    {
        while (true)
        {
            while (!_running)
            {
                yield();
            }

            Canvas canvas = null;
            try
            {
                canvas = _surfaceHolder.lockCanvas();
                synchronized (_surfaceHolder)
                {
                    long currentMillis = new Date().getTime();
                    float dt = (float) (currentMillis - _lastFrameMillis) / 1000.0f;
                    _lastFrameMillis = currentMillis;

                    _activity.update(dt);

                    if (canvas != null)
                    {
                        _activity.draw(canvas);
                    }
                }
            }
            catch (Exception e)
            {
                _activity.handleException(e);
            }
            finally
            {
                if (canvas != null)
                {
                    _surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}