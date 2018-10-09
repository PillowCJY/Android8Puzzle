package com.example.austin.fun8puzzle;

import java.util.HashSet;

public class Puzzle implements Comparable<Puzzle> {

    public int[][] gameBoard = new int[3][3];
    private int[][] goalBoard = new int[3][3];
    private int goalState;
    public int blankX;
    public int blankY;
    private String pathToSolution;
    public int pathLength;

    private HashSet<Integer> previousState = new HashSet<>();


    public Puzzle(String initialState){

        pathLength = 0;
        pathToSolution = "";
        goalBoard[0] = new int[]{1,2,3};
        goalBoard[1] = new int[]{4,5,6};
        goalBoard[2] = new int[]{7,8,0};

        goalState = 12345678;

        previousState.add(toNumber());
        int length = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                gameBoard[i][j] = initialState.charAt(length) - '0';
                if(gameBoard[i][j] == 0){
                    blankX = j;
                    blankY = i;
                }
                length++;
            }
        }
    }

    private Puzzle(Puzzle cpyPuzzle){

        goalBoard[0] = new int[]{1,2,3};
        goalBoard[1] = new int[]{4,5,6};
        goalBoard[2] = new int[]{7,8,0};

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                gameBoard[i][j] = cpyPuzzle.gameBoard[i][j];
                if(gameBoard[i][j] == 0){
                    blankX = j;
                    blankY = i;
                }
            }
        }

        goalState = 12345678;
        pathToSolution = cpyPuzzle.pathToSolution;
        pathLength = cpyPuzzle.pathLength + 1;
    }

    public boolean goalMatch(){
        boolean result=true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gameBoard[i][j] != goalBoard[i][j]) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public String toString(){
        StringBuilder state = new StringBuilder("");
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 3; j++){
                state.append((char)(gameBoard[i][j] + '0'));
            }
        }
        return state.toString();
    }

    public int toNumber() {
        int num = 0;
        for (int i = 0; i < 3;i++) {
            for (int j = 0; j < 3;j++) {
                num = num * 10 + gameBoard[i][j];
            }
        }
        return num;
    }

    public HashSet<Integer> getPreviousState(){
        return previousState;
    }

    public void setPreviousState(HashSet<Integer> set)
    {
        previousState = new HashSet<>(set);
        previousState.add(toNumber());
    }

    public boolean isLocalLoop(int state){
        if(previousState.contains(state)){
            return true;
        } else {
            return false;
        }
    }

    public void setBlankX(int x){
        blankX = x;
    }

    public void setBlankY(int y){
        blankY = y;
    }

    public void addSolutionPath(String direction){
        pathToSolution += direction;
    }

    public String getPathToSolution(){
        return pathToSolution;
    }

    public boolean canMoveUp(){
        return (blankY > 0);
    }

    public boolean canMoveDown(){
        return (blankY < 2);
    }

    public boolean canMoveLeft(){
        return (blankX > 0);
    }

    public boolean canMoveRigh(){
        return (blankX < 2);
    }

    public Puzzle moveUp(){

        if(blankY > 0){
            Puzzle newPuzzle = new Puzzle(this);
            newPuzzle.gameBoard[blankY][blankX] = newPuzzle.gameBoard[blankY - 1][blankX];
            newPuzzle.gameBoard[blankY - 1][blankX] = 0;
            newPuzzle.setBlankY(blankY-1);
            newPuzzle.addSolutionPath("U");
            return newPuzzle;
        } else {
            return null;
        }
    }

    public Puzzle moveDown(){

       if(blankY < 2){
           Puzzle newPuzzle = new Puzzle(this);
           newPuzzle.gameBoard[blankY][blankX] = newPuzzle.gameBoard[blankY+1][blankX];
           newPuzzle.gameBoard[blankY+1][blankX] = 0;
           newPuzzle.setBlankY(blankY+1);
           newPuzzle.addSolutionPath("D");
           return newPuzzle;
       } else {
           return null;
       }
    }

    public Puzzle moveLeft(){
        if(blankX > 0){
            Puzzle newPuzzle = new Puzzle(this);
            newPuzzle.gameBoard[blankY][blankX] = newPuzzle.gameBoard[blankY][blankX-1];
            newPuzzle.gameBoard[blankY][blankX-1] = 0;
            newPuzzle.setBlankX(blankX-1);
            newPuzzle.addSolutionPath("L");
            return newPuzzle;
        } else {
            return null;
        }
    }

    public Puzzle moveRight(){
       if(blankX < 2){
           Puzzle newPuzzle = new Puzzle(this);
           newPuzzle.gameBoard[blankY][blankX] = newPuzzle.gameBoard[blankY][blankX+1];
           newPuzzle.gameBoard[blankY][blankX+1] = 0;
           newPuzzle.setBlankX(blankX+1);
           newPuzzle.addSolutionPath("R");
           return newPuzzle;
       } else {
           return null;
       }
    }

    public int getHCost(){
        return findManhattan() + pathLength;
    }

    public int findManhattan(){
        int sum = 0;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
               sum += findAbsDifferent(gameBoard[i][j], j, i);
            }
        }
        return sum;
    }

    private int findAbsDifferent(int number, int x, int y){
        int difference = 0;
        switch (number){
            case 1:
                difference = x + y;
                break;
            case 2:
                difference = Math.abs(x-1) + y;
                break;
            case 3:
                difference = Math.abs(x-2) + y;
                break;
            case 4:
                difference = x + Math.abs(y-1);
                break;
            case 5:
                difference = Math.abs(x - 1) + Math.abs(y - 1);
                break;
            case 6:
                difference = Math.abs(x - 2) + Math.abs(y - 1);
                break;
            case 7:
                difference = x + Math.abs(y-2);
                break;
            case 8:
                difference = Math.abs(x - 1) + Math.abs(y - 2);
                break;
        }
        return difference;
    }


    @Override
    public int compareTo(Puzzle o) {
        return this.getHCost() - o.getHCost();
    }
}
