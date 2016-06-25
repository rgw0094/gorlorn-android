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
    private RenderLoopBase _renderLoop;

    public GameLoopThread(SurfaceHolder surfaceHolder, RenderLoopBase renderLoop)
    {
        _surfaceHolder = surfaceHolder;
        _renderLoop = renderLoop;
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
                    boolean isFirstFrame = _lastFrameMillis == 0;
                    long currentMillis = new Date().getTime();
                    float dt = (float) (currentMillis - _lastFrameMillis) / 1000.0f;
                    _lastFrameMillis = currentMillis;

                    _renderLoop.update(isFirstFrame ? 0.0f : dt);

                    if (canvas != null)
                    {
                        _renderLoop.draw(canvas);
                    }
                }
            }
            catch (Exception e)
            {
                _renderLoop.handleException(e);
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