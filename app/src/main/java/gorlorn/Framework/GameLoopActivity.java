package gorlorn.Framework;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Rob on 1/20/2016.
 */
public abstract class GameLoopActivity extends Activity
{
    public abstract void handleInputEvent(MotionEvent me);

    public abstract void update(float dt);

    public abstract void draw(Canvas canvas);

    public abstract void handleException(Exception e);
}
