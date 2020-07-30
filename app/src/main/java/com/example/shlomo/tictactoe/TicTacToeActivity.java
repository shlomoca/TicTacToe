package com.example.shlomo.tictactoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class TicTacToeActivity extends AppCompatActivity implements View.OnClickListener{
    Button tl;
    Button tm;
    Button tr;
    Button ml;
    Button mm;
    Button mr;
    Button bl;
    Button bm;
    Button br;
    Button reset;
    ImageButton back;
    TextView title;
    TicTacToeGame game;

    int[] players=new int[2] ;
    final int O=0,X=1,CPU=2,HUMAN=3,TIE=2;
    boolean player0active=true,
            player1active=false,
            gameEnd=false;
    ArrayList<Button> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);
        Intent intent =getIntent();

        //setting the buttons by id
        this.tl = findViewById(R.id.tl);
        this.tm = findViewById(R.id.tm);
        this.tr = findViewById(R.id.tr);
        this.ml = findViewById(R.id.ml);
        this.mm = findViewById(R.id.mm);
        this.mr = findViewById(R.id.mr);
        this.bl = findViewById(R.id.bl);
        this.bm = findViewById(R.id.bm);
        this.br = findViewById(R.id.br);
        this.back=findViewById(R.id.back);
        this.reset = findViewById(R.id.reset);
        this.title = findViewById(R.id.title);
        //setting listeners for the buttons
        this.bl.setOnClickListener(this);
        this.bm.setOnClickListener(this);
        this.br.setOnClickListener(this);
        this.ml.setOnClickListener(this);
        this.mm.setOnClickListener(this);
        this.mr.setOnClickListener(this);
        this.tl.setOnClickListener(this);
        this.tm.setOnClickListener(this);
        this.tr.setOnClickListener(this);
        this.reset.setOnClickListener(this);
        back.setOnClickListener(this);
        //adding the buttons to the list
        list.add(tl);
        list.add(tm);
        list.add(tr);
        list.add(ml);
        list.add(mm);
        list.add(mr);
        list.add(bl);
        list.add(bm);
        list.add(br);
        //get user data about the player
        players[0]= (intent.getBooleanExtra(EntryActivity.XINPUT,true))? HUMAN:CPU;
        players[1]= (intent.getBooleanExtra(EntryActivity.OINPUT,true))? HUMAN:CPU;
    }
    protected void onStart() {
        super.onStart();
                StartGame();

    }
        protected void onDestroy() {
            super.onDestroy();
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("saving", serialize());
            editor.commit();
        }
    private void reStartGame(){//starting a new game
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String str =pref.getString("saving", "----------");
        player0active=true;
        player1active=false;
        gameEnd=false;
        game = new TicTacToeGame();
        title.setText(R.string.title);
        for(Button b:list){
            b.setText("");
            b.setOnClickListener(this);
        }
        if(((player0active && players[0] == CPU) || (player1active && players[1] == CPU))&&!gameEnd){
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerGame();
                }
            }, 2000);
        }

        else
            Toast.makeText(this, "Xs turn", Toast.LENGTH_SHORT).show();

    }
    private void StartGame(){//starting a new game
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String str =pref.getString("saving", "----------");
        player0active=deSerialize(str);
        player1active=!player0active;
        game = new TicTacToeGame(str);
        title.setText(R.string.title);
        for(Button b:list){
            b.setOnClickListener(this);
        }
        gameEnd=false;
        if((player0active && players[0] == CPU) || (player1active && players[1] == CPU)){

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerGame();
                }
            }, 2000);
        }

        else
            Toast.makeText(this, "Xs turn", Toast.LENGTH_SHORT).show();

    }

    //calls a computer move from the TicTacToeGame and acts accordingly
    private void computerGame()  {
        while (((player0active && players[0] == CPU) || (player1active && players[1] == CPU)) && !gameEnd) {


            int [] action =game.cpuMove();
        switch (action[3]){
                case -1:
                case 0:
                gameOver(TIE);
               gameEnd=true;
                break;
            case 1:
               gameOver(action[2]);
               gameEnd=true;
               break;
        }
        mark(action[0],action[1],action[2]);
        }
    }

        @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back://send back the Entry activity's toggle values
                Intent intent = new Intent(this, EntryActivity.class);
                intent.putExtra(EntryActivity.XINPUT,players[0]==HUMAN);
                intent.putExtra(EntryActivity.OINPUT,players[1]==HUMAN);
                startActivity(intent);
                finish();
           return;
            case R.id.reset:
     getApplicationContext().getSharedPreferences("MyPref", 0).edit().clear(); //clear sheared preferences
                reStartGame();
                return;
        }

         if(((player0active&&players[0]==HUMAN)||(player1active&&players[1]==HUMAN))){//&&!TurnInProgress){ //if it is a human player's turn
            // TurnInProgress=true;
             findViewById(v.getId()).setOnClickListener(null);
        switch (v.getId()) {
            case R.id.tl:
                go(0,0);
                break;
            case R.id.tm:
                go(0,1);
                break;
            case R.id.tr:
                go(0,2);
                break;
            case R.id.ml:
                go(1,0);
                break;
            case R.id.mm:
                go(1,1);
                break;
            case R.id.mr:
                go(1,2);
                break;
            case R.id.bl:
                go(2,0);
                break;
            case R.id.bm:
                go(2,1);
                break;
            case R.id.br:
                go(2,2);
                break;
         }
        }
        //TurnInProgress=false;
    }
    //go will call a human move from the TicTacToeGame and will act according to the result
    private void go(int r,int c){
        int []a=game.humanMove(r,c);
        if(!gameEnd) {
            switch (a[0]){
                case -1://-1 - illegal move.
                    Toast.makeText(this, "wrong move, try again ", Toast.LENGTH_LONG).show();
                    break;
                case O://already finished game
                case X:
                case TIE:
                    gameOver(a[0]);
                    gameEnd=true;
                    break;

                case 3://regular move
                    mark(r,c,a[1]);
                    break;

                case 4://winning move
                    mark(r,c,a[1]);
                    gameOver(a[1]);
                    gameEnd=true;

                case 5://last tie move
                    mark(r,c,a[1]);
                    gameOver(TIE);
                    gameEnd=true;
                    break;
            }
        }
        else
            gameOver(a[0]);

        if((player0active&&players[0]==CPU)||(player1active&&players[1]==CPU))//if the next player is a computer
        {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerGame();
                }
            }, 2000);
        }
    }

//marks the button that represents the move and switches active player
    private void mark(int r,int c, int symbol) {
     int loc=r*game.SIZE+c;
     String put = "X";
     if(symbol==X)
         this.list.get(loc).setTextColor(0xff0000ff);
       else{
           put="O";
         this.list.get(loc).setTextColor(0xffff0000);
     }
      this.list.get(loc).setText(put);

        player0active=player1active;
       player1active=!player1active;

       if (((player0active&&players[0]==HUMAN)||(player1active&&players[1]==HUMAN))){//if next move is human prompt him accordingly

           put = put.equals("X")?"O":"X";
           Toast.makeText(this, put+"s turn", Toast.LENGTH_SHORT).show();
       }

    }
//notify the user that the game is over. i gets the winner/tie identity
    private void gameOver(int i){
        char c= 'X';
        if(i==O)
            c='O';
        if (i==TIE){
            Toast.makeText(this, "it's a tie, press new game", Toast.LENGTH_LONG).show();
            title.setText(R.string.tie);
        }
        else{
            Toast.makeText(this, c+" wins", Toast.LENGTH_LONG).show();
            title.setText(String.format("%s wins", c));
        }

    }
    private String serialize(){
        String str="";
        for(Button b:list){
             if(b.getText().equals(""))
                 str+="-";
             else
                 str+=b.getText();
        }
        return str;
    }
    private boolean deSerialize(String str) {
        boolean player0=true;
        for (int i = 0; i < game.SIZE * game.SIZE; i++) {
            if (str.charAt(i) == '-')
                list.get(i).setText("");
            else{
                list.get(i).setText(str.charAt(i));
                player0=!player0;
            }
        }
        return player0;
    }


}
