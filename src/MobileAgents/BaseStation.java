package MobileAgents;

import javafx.scene.shape.Circle;

import java.util.LinkedList;

public class BaseStation extends Node{


    public BaseStation(Status state,int x, int y, Circle circle){
        super(state,x,y,circle);
    }

    public void findPaths(){
        for(Node node:neighbors){
            LinkedList<Node> paths = new LinkedList<>();
            paths.add(this);
            node.findPaths(paths,this);
        }
    }
    /**
     * This function will store the returned id and location of the agents
     */
    @Override
    public synchronized void returnID(int id, int x, int y,boolean status, LinkedList<Node> path,LinkedList<Node> returnPath){

    }
}
