package com.example.austin.fun8puzzle;

import android.app.Activity;
import android.widget.Toast;

import java.util.HashSet;
import java.util.PriorityQueue;

public class Algorithm {

    private PriorityQueue<Puzzle> myHeap;
    private HashSet<Integer> visitedList;
    private Puzzle initialPuzzle;

    public Algorithm (Puzzle initial){
        initialPuzzle = initial;
        myHeap = new PriorityQueue<>();
        visitedList = new HashSet<>();
    }

    public String getSolution(){
        initialPuzzle = new Puzzle(initialPuzzle.toString());
        Puzzle temp = implementAlgorithm();
        if(temp == null){
            return "";
        } else {
            return temp.getPathToSolution();
        }
    }

    private Puzzle implementAlgorithm(){

        myHeap.add(initialPuzzle);
        Puzzle front = myHeap.poll();


        while(front != null){


            if(visitedList.contains(front.toNumber())){
                front = myHeap.poll();
                continue;
            }

            visitedList.add(front.toNumber());


            if(front.goalMatch()){
                return front;
            }

            Puzzle temp = null;

            if(front.canMoveUp()){

                temp = front.moveUp();


                if(!visitedList.contains(temp.toNumber()) && !front.isLocalLoop(temp.toNumber())){
                    temp.setPreviousState(front.getPreviousState());
                    myHeap.add(temp);
                }
            }

            if(front.canMoveDown()){

                temp = front.moveDown();


                if(!visitedList.contains(temp.toNumber()) && !front.isLocalLoop(temp.toNumber())){
                    temp.setPreviousState(front.getPreviousState());
                    myHeap.add(temp);
                }
            }

            if(front.canMoveLeft()){
                temp = front.moveLeft();


                if(!visitedList.contains(temp.toNumber()) && !front.isLocalLoop(temp.toNumber())){
                    temp.setPreviousState(front.getPreviousState());
                    myHeap.add(temp);
                }
            }

            if(front.canMoveRigh()){
                temp = front.moveRight();


                if(!visitedList.contains(temp.toNumber()) && !front.isLocalLoop(temp.toNumber())){
                    temp.setPreviousState(front.getPreviousState());
                    myHeap.add(temp);
                }
            }

            front = myHeap.poll();
        }
        return null;
    }

}
