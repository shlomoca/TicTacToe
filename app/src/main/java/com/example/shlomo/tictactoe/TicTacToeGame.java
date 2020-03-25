package com.example.shlomo.tictactoe;
/*TicTacToeGame is a game of tic tac toe using the minimax algorithm.
minimax algorithm checks the best moves that someone can play by checking all of the rivals best counter moves.
the rival counter moves are calculated by the bast move that the individual plays and so on in recursion.
the calculation goes by the next scoring:
                                            every winning move by 10 points * available moves
                                            every losing move is -10 points * available moves
                                            tie moves are worth 0 points.
the recursion ends with the best move for a player.
 */
public class TicTacToeGame {

    protected final int O = 0, X = 1, EMPTY = 2, SIZE = 3;
    private int[][] board;
    private int turn;


    public TicTacToeGame() {
        board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = i; j < SIZE; j++) {
                board[i][j] = EMPTY;
                board[j][i] = EMPTY;
            }
        }
        turn = X;
    }

    /*
    humanMove gets an order from a human and checks if it is valid.
    if so it will put it on the board and notify the user by the following return values:
    int[0]=  -1 if the move is not valid. 0/1 sends if winner already exists. 2 if there is already a tie.
             3.if the move is ok. 4.if winning move 5.if this is a tie move
    int[1]= (0/1 =X/O)who's turn was it
     */
    int[] humanMove(int r, int c) {
        int available=availableMoves(this.board);
        int[] action = new int[2];
        action[1] = turn;
        if (didWin(this.board, turn))
            action[0] = turn;
        else if (!isValid(r, c, this.board))
            action[0] = -1;
        else if (available == 0)
            action[0] = 2;
        else {
            this.board[r][c] = turn;
            action[0] = 3;
            if (didWin(this.board, turn))//last winning
                action[0] = 4;
            else if(available==1){//last turn played
                action[0] = 5;
            }
            else
                turn = turn == X ? O : X;
        }
        return action;
    }

    /*  cpuMove will calculate the cpu's best move using the minimax algorithm
        return value:
        int[0]=cpu row move
        int[1]=cpu col move
        int[2]=cpu symbol
        int[3]=error code -1=no moves available, 0= last move, 1= winning move, 2=regular move
     */
     int[] cpuMove() {

        int rivalSymbol = turn == X ? O : X,//rivalSymbol =!turn
                moveValue,
                maxValue = -Integer.MAX_VALUE,
        available=availableMoves(this.board);
        int[][] tempBoard;
        int[] action = new int[4],
                bestAction = new int[4];
        action[3]=2;
        action[2] = turn;
        if (available== 0) {
            action[3] = -1;
            return action;
        }
        if(available== 1)
            action[3] = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValid(i, j, this.board)) {
                    action[0] = i;
                    action[1] = j;
                    tempBoard = dup2dArr(this.board);
                    tempBoard[i][j] = turn;
                    if (didWin(tempBoard, turn)) {
                        this.board[action[0]][action[1]] = turn;
                        action[3]=1;
                        return action;
                    }
                    moveValue = getMinValue(tempBoard, rivalSymbol);
                    if (moveValue > maxValue) {
                        bestAction = dupArr(action);
                        maxValue=moveValue;
                    }
                }
            }
        }
        this.board[bestAction[0]][bestAction[1]] = turn;
        turn = turn == X ? O : X;
        return bestAction;
    }
//getMaxValue checks what is the maximum value that the cpu can get on the sent board
    private int getMaxValue(int[][] sentBoard, int cpuSymbol) {
        int i, j,
                tempMoveValue, maxMoveValue = -Integer.MAX_VALUE,
                rivalSymbol = O;
        int[][] tempBoard;
        if (cpuSymbol == O)
            rivalSymbol = X;
        if (availableMoves(sentBoard) == 0)
            return 0;
        for (i = 0; i < SIZE; i++) {
            for (j = 0; j < SIZE; j++) {
                if (isValid(i, j, sentBoard)) {
                    tempBoard = dup2dArr(sentBoard);
                    tempBoard[i][j] = cpuSymbol;
                    if (didWin(tempBoard, cpuSymbol))
                        return 10*(availableMoves(this.board)+1);
                    tempMoveValue = getMinValue(tempBoard, rivalSymbol);
                    if (tempMoveValue > maxMoveValue) {
                        maxMoveValue = tempMoveValue;

                    }
                }
            }
        }
        return maxMoveValue;
    }
    //getMaxValue checks what is the maximum value that the rival can get on the sent board and sends the cpu what is the minimal move among them
    private int getMinValue(int[][] sentBoard, int rivalSymbol) {
        int i, j, tempMoveValue, minMoveValue = Integer.MAX_VALUE,
                cpuSymbol = O;
        int[][] tempBoard;
        if (rivalSymbol == O)
            cpuSymbol = X;
        if (availableMoves(sentBoard) == 0)
            return 0;
        for (i = 0; i < SIZE; i++) {
            for (j = 0; j < SIZE; j++) {
                if (isValid(i, j, sentBoard)) {
                    tempBoard = dup2dArr(sentBoard);
                    tempBoard[i][j] = rivalSymbol;
                    if (didWin(tempBoard, rivalSymbol))
                        return -10*(availableMoves(this.board)+1);
                    tempMoveValue = getMaxValue(tempBoard, cpuSymbol);
                    if (tempMoveValue < minMoveValue) {
                        minMoveValue = tempMoveValue;
                    }
                }
            }
        }
        return minMoveValue;
    }

    private int availableMoves(int[][] sentBoard) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = i; j < SIZE; j++) {
                if (sentBoard[i][j] == EMPTY)
                    count++;
                if (sentBoard[j][i] == EMPTY && j != i)
                    count++;
            }
        }
        return count;
    }

    private boolean didWin(int[][] sentBoard, int symbol) {//didWin checks if symbol won
        return checkRC(sentBoard, symbol) || checkAcross(sentBoard, symbol);
    }

    private boolean isValid(int i, int j, int[][] sentBoard) {return (sentBoard[i][j] == EMPTY);}
//checkRC checks every row and column to see if symbol won
    private boolean checkRC(int[][] sentBoard, int symbol) {//checkRC will check the rows and the coulombs for winning signs
        int rowCounter, colCounter;
        for (int i = 0; i < this.SIZE; i++) {
            rowCounter = 0;
            colCounter = 0;
            for (int j = 0; j < this.SIZE; j++) {
                if (sentBoard[i][j] == symbol)
                    rowCounter++;
                if (sentBoard[j][i] == symbol)
                    colCounter++;
                if (rowCounter == SIZE || colCounter == SIZE)
                    return true;
            }
        }
        return false;
    }
//checkAcross checks the slants to see if symbol won
    private boolean checkAcross(int[][] sentBoard, int symbol) {//checkAcross will check the diagonal lines to see if anyone won
        int right = 0, left = SIZE - 1;
        int rightCounter = 0, leftCounter = 0;
        for (int i = 0; i < this.SIZE; i++) {
            if (sentBoard[right][i] == symbol)
                rightCounter++;
            if (sentBoard[left][i] == symbol)
                leftCounter++;
            right++;
            left--;
        }
        return (leftCounter == SIZE || rightCounter == SIZE);
    }

    private int[][] dup2dArr(int[][] original) {
        int[][] dup = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = i; j < SIZE; j++) {
                dup[i][j] = original[i][j];
                dup[j][i] = original[j][i];
            }
        }
        return dup;
    }
    private int[] dupArr(int[] original) {
        int[] dup = new int[4];
        for (int i = 0; i < 4; i++) {
                dup[i] = original[i];
        }
        return dup;
    }
}