package com.example.austin.fun8puzzle;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 *  This class will implement A* Algorithm to search from a state to the goal state
 *  @author Junyi Chen
 */
public class Algorithm {


    private PriorityQueue<Puzzle> myHeap; //Priority Queue
    private HashSet<Integer> visitedList; //visited list
    private Puzzle initialPuzzle; //initial state

    //Constructor
    public Algorithm (Puzzle initial){
        initialPuzzle = initial;
        myHeap = new PriorityQueue<>();
        visitedList = new HashSet<>();
    }

    //this method will return a solution if one exists
    public String getSolution(){
        initialPuzzle = new Puzzle(initialPuzzle.toString());
        Puzzle temp = implementAlgorithm();
        if(temp == null){
            return "";
        } else {
            return temp.getPathToSolution();
        }
    }

    //this method will implement the A* Algorithm
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
