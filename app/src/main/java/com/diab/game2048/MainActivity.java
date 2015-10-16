package com.diab.game2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity implements GameUpdatesListener {

    private Context ctx;
    private OverLayView view ;
    private GamePanelView gamePanelView;
    private TextView lblScore;
    private TextView lblBestScore;
    private TextView lblRestart;
    private int bestScore = 0 ;
    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String bestScoreKey;
    int lastBestScore ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        view = (OverLayView)findViewById(R.id.overLay);
        lblScore = (TextView)findViewById(R.id.lblScore);
        lblBestScore = (TextView)findViewById(R.id.lblBestScore);
        lblRestart = (TextView)findViewById(R.id.lblRestart);
        gamePanelView =(GamePanelView)findViewById(R.id.gamePanel);
        gamePanelView.setGameUpdatesListener(this);
        gamePanelView.setOverLayView(view);
        lblRestart.setOnClickListener(restartClickListener);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        bestScoreKey =getResources().getText(R.string.best_score).toString();
        lastBestScore = sharedPref.getInt(bestScoreKey,0);
        lblBestScore.setText(String.valueOf(lastBestScore));
    }

    @Override
    public void onScoreChanged(int newScore) {
        if (newScore> lastBestScore) {
            editor.putInt(bestScoreKey, newScore);
            editor.commit();
            lblBestScore.setText(String.valueOf(newScore));
        }
        lblScore.setText(String.valueOf(newScore));
    }

    @Override
    public void onGameLoss() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getResources().getString(R.string.you_lost_message));
        dialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gamePanelView.restart();
            }
        });

        dialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getResources().getString(R.string.exit_message));
        dialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        dialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
            }
        });
        dialog.show();
    }

    private View.OnClickListener restartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
            dialog.setMessage(getResources().getString(R.string.restart_sure));
            dialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gamePanelView.restart();
                }
            });

            dialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    };

    @Override
    public void onGameWin() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        dialog.setMessage(getResources().getString(R.string.win_message));
        dialog.setPositiveButton(getResources().getString(R.string.continue_message), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
            }
        });

        dialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               gamePanelView.restart();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
