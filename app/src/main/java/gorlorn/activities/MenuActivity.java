package gorlorn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Activity for the menu that dislpays when the game first starts.
 */
public class MenuActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        //final View controlsView = findViewById(R.id.menu_layout);
    }

    public void startGame(View view)
    {
        Intent intent = new Intent(this, GorlornActivity.class);
        startActivity(intent);
    }
}
