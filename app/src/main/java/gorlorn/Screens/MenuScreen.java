package gorlorn.Screens;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import gorlorn.Bitmaps;
import gorlorn.Gorlorn;
import gorlorn.UI.Button;
import gorlorn.activities.R;

/**
 * Created by Rob on 6/28/2016.
 */
public class MenuScreen extends ScreenBase
{
    private Button _playButton;
    private Button _statisticsButton;
    private Button _aboutButton;
    private Button _quitButton;

    //hacks!
    private static Bitmap _playBitmap;
    private static Bitmap _statisticsBitmap;
    private static Bitmap _aboutBitmap;
    private static Bitmap _quitBitmap;

    public MenuScreen(Gorlorn gorlorn)
    {
        super(gorlorn);

        int buttonHeight = gorlorn.getYFromPercent(0.1f);
        int buttonWidth = (int)(buttonHeight * 5.0f);

        if (_playBitmap == null)
        {
            _playBitmap = gorlorn.createBitmap(R.drawable.button_play, buttonWidth, buttonHeight);
            _statisticsBitmap = gorlorn.createBitmap(R.drawable.button_statistics, buttonWidth, buttonHeight);
            _aboutBitmap = gorlorn.createBitmap(R.drawable.button_about, buttonWidth, buttonHeight);
            _quitBitmap = gorlorn.createBitmap(R.drawable.button_quit, buttonWidth, buttonHeight);
        }

        _playButton = new Button(gorlorn, _playBitmap, _gorlorn.getXFromPercent(0.5f), _gorlorn.getYFromPercent(0.46f));
        _statisticsButton = new Button(gorlorn, _statisticsBitmap, _gorlorn.getXFromPercent(0.5f), _gorlorn.getYFromPercent(0.6f));
        _aboutButton = new Button(gorlorn, _aboutBitmap, _gorlorn.getXFromPercent(0.5f), _gorlorn.getYFromPercent(0.74f));
        _quitButton = new Button(gorlorn, _quitBitmap, _gorlorn.getXFromPercent(0.5f), _gorlorn.getYFromPercent(0.88f));
    }

    @Override
    public void show(ScreenBase previousScreen)
    {
    }

    @Override
    public boolean leave()
    {
        return true;
    }

    @Override
    public boolean update(float dt)
    {
        _playButton.update();
        _statisticsButton.update();
        _aboutButton.update();
        _quitButton.update();

        if (_playButton.isClicked())
        {
            _gorlorn.startGame();
        }
        else if (_statisticsButton.isClicked())
        {
            _gorlorn.showStatistics();
        }
        else if (_aboutButton.isClicked())
        {
            _gorlorn.showAboutScreen();
        }
        else if (_quitButton.isClicked())
        {
            _gorlorn.getActivity().finishAffinity();
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas)
    {
        _gorlorn.getBackground().draw(canvas);
        canvas.drawBitmap(Bitmaps.Title, _gorlorn.getXFromPercent(0.15f), _gorlorn.getYFromPercent(0.15f), null);

        _playButton.draw(canvas);
        _statisticsButton.draw(canvas);
        _aboutButton.draw(canvas);
        _quitButton.draw(canvas);
    }
}
