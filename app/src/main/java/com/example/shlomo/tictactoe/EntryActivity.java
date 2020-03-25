package com.example.shlomo.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String XINPUT= "com.example.shlomo.tictactoe.Xinput";
    public static final String OINPUT= "com.example.shlomo.tictactoe.Oinput";
    private Button startButton ;
    private ToggleButton xToggle,oToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        startButton = findViewById(R.id.startGame);
        xToggle = findViewById(R.id.xToggle);
        oToggle = findViewById(R.id.oToggle);
        startButton.setOnClickListener(this);
        Intent intent =getIntent();
        xToggle.setChecked(intent.getBooleanExtra(EntryActivity.XINPUT,true));
        oToggle.setChecked(intent.getBooleanExtra(EntryActivity.OINPUT,true));

    }


    public void onClick(View v) {
        startGame();

    }
private void startGame(){
            Intent intent = new Intent(this,TicTacToeActivity.class);
            intent.putExtra(XINPUT,xToggle.isChecked());
            intent.putExtra(OINPUT,oToggle.isChecked());
            startActivity(intent);
            finish();

}

}
