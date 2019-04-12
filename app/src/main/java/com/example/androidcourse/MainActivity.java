package com.example.androidcourse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import java.lang.Boolean;
public class MainActivity extends AppCompatActivity {
    static int board[][] = new int[3][3];
    static Point finishGame[][] = {{new Point(0,0),new Point(0,1),new Point(0,2)},
                                     {new Point(1,0),new Point(1,1),new Point(1,2)},
                                     {new Point(2,0),new Point(2,1),new Point(2,2)},
                                     {new Point(0,0),new Point(1,0),new Point(2,0)},
                                     {new Point(0,1),new Point(1,1),new Point(2,1)},
                                     {new Point(0,2),new Point(1,2),new Point(2,2)},
                                     {new Point(0,0),new Point(1,1),new Point(2,2)},
                                     {new Point(0,2),new Point(1,1),new Point(2,0)}};
    int turn = 0;
    Boolean gameOver = true;
    Point AIPos = new Point();
    Boolean isAi = false;
    // 0 = RED, 1 = YELLOW
    public Point CoordinatesToArrPos(View view)
    {
        Point point = new Point();
        android.support.v7.widget.GridLayout gridLayout = (android.support.v7.widget.GridLayout)findViewById(R.id.gridLayout);
        for(int i = 0;i < gridLayout.getChildCount();i++)
        {
            if(gridLayout.getChildAt(i) == view)
            {
                point.setLocation(i/3,i%3);
                break;
            }
        }
        return point;
    }
    public int AINextMove(int turn,int depth)
    {
        int Val = PlayerWin();
        if(Val == 1)
            return 1;
        if(Val == 0)
            return -1;
        if(boardFull())
            return 0;
        int ret = (turn  == 1 ? -5 : 5);
        for(int i = 0;i < 3;i++)
        {
            for(int j = 0;j < 3;j++)
            {
                if(board[i][j] == -1)
                {
                    board[i][j] = turn;
                    int Tmp = AINextMove(1 - turn,depth + 1);
                    if(depth == 0 && Tmp >= ret)
                        AIPos.setLocation(i,j);
                    if(turn == 1)
                        ret = Math.max(ret,Tmp);
                    else
                        ret = Math.min(ret,Tmp);
                    board[i][j] = -1;
                    if(ret == 1 && turn == 1)
                        return 1;
                    if(ret == -1 && turn == 0)
                        return -1;
                }
            }
        }
        return ret;
    }
    public int PlayerWin()
    {
        for(int i = 0;i < finishGame.length;i++)
        {
            Point P1 = finishGame[i][0],P2 = finishGame[i][1],P3 = finishGame[i][2];
            if(board[P1.getX()][P1.getY()] == board[P2.getX()][P2.getY()] && board[P2.getX()][P2.getY()] == board[P3.getX()][P3.getY()] && board[P2.getX()][P2.getY()] != -1)
                return board[P2.getX()][P2.getY()];

        }
        return -1;
    }
    public Boolean endGame(int turn)
    {
        int gameState = PlayerWin();
        if(gameState >= 0)
        {
            Toast.makeText(getApplicationContext(),"Player " + (turn == 0 ? "Red" : "Yellow") + " Has Won.",Toast.LENGTH_SHORT).show();
            gameOver = true;
            Button Btn = (Button)findViewById(R.id.Button);
            Button Btn2 = (Button) findViewById(R.id.button);
            TextView textView = (TextView)findViewById(R.id.textView);
            textView.setVisibility(View.VISIBLE);
            Btn.setVisibility(View.VISIBLE);
            Btn2.setVisibility(View.VISIBLE);
            textView.setText("Player " + (turn == 0 ? "Red" : "Yellow") + " Has Won.");
            return true;
        }
        if(boardFull())
        {
            Toast.makeText(getApplicationContext(),"Draw!",Toast.LENGTH_SHORT).show();
            gameOver = true;
            Button Btn = (Button)findViewById(R.id.Button);
            Button Btn2 = (Button) findViewById(R.id.button);
            TextView textView = (TextView)findViewById(R.id.textView);
            textView.setVisibility(View.VISIBLE);
            Btn.setVisibility(View.VISIBLE);
            Btn2.setVisibility(View.VISIBLE);
            textView.setText("Draw.");
            return true;
        }
        return false;
    }
    public Boolean boardFull()
    {
        for(int i = 0;i < 3;i++)
        {
            for(int j = 0;j < 3;j++)
            {
                if(board[i][j] == -1)
                    return false;
            }
        }
        return true;
    }
    public void restartGame(View view)
    {
        if(gameOver == false)
            return;
        for(int i = 0;i < 3;i++) {
            for (int j = 0; j < 3; j++)
                board[i][j] = -1;
        }
        Button Btn = (Button)view;
        TextView textView = (TextView)findViewById(R.id.textView);
        turn = 0;
        gameOver = false;
        android.support.v7.widget.GridLayout gridLayout = (android.support.v7.widget.GridLayout)findViewById(R.id.gridLayout);
        for(int i = 0;i < gridLayout.getChildCount();i++)
        {
            if(gridLayout.getChildAt(i) instanceof ImageView)
            {
                ImageView imageView = (ImageView)gridLayout.getChildAt(i);
                imageView.setTag(null);
                imageView.setImageResource(0);
            }
        }
        if(Integer.valueOf(Btn.getTag().toString()) == 0)
            isAi = true;
        else
            isAi = false;
        Button Bt1 = (Button) findViewById(R.id.Button);
        Button Bt2 = (Button) findViewById(R.id.button);
        Bt1.setVisibility(View.INVISIBLE);
        Bt2.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
    }
    public void AddPiece(View view,int T)
    {
        ImageView piece = (ImageView)view;
        Point point = CoordinatesToArrPos(view);
        piece.setY(piece.getY() - 1000);
        if(T == 0)
            piece.setImageResource(R.drawable.red);
        else
            piece.setImageResource(R.drawable.yellow);
        board[point.getX()][point.getY()] = T;
        piece.animate().translationYBy(1000f).setDuration(200);
        piece.setTag(T);
    }
    public void DoAiMove()
    {
        AINextMove(1,0);
        android.support.v7.widget.GridLayout gridLayout = (android.support.v7.widget.GridLayout) findViewById(R.id.gridLayout);
        System.out.println(AIPos.getX() + " " + AIPos.getY());
        ImageView AiView = (ImageView) gridLayout.getChildAt(AIPos.getX() * 3 + AIPos.getY());
        AddPiece(AiView, 1);
        if(endGame(1)) return;

        return;
    }
    public void onClickPiece(View view)
    {
        if (gameOver == true)
            return;
        ImageView piece = (ImageView) view;
        if (piece.getTag() != null)
            return;
        AddPiece(view, turn);
        if(endGame(turn)) return;
        if(isAi)
            DoAiMove();
        else
            turn = 1 - turn;
        return;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(int i = 0;i < 3;i++)
        {
            for(int j = 0;j < 3;j++)
                board[i][j] = -1;
        }
    }
}
