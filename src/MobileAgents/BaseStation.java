package MobileAgents;

import java.util.LinkedList;

public class BaseStation extends Node{


    public BaseStation(String state, LinkedList<Node> neighbors){
        super(state,neighbors);
    }

    public void findPaths(){
        for(Node node:neighbors){
            LinkedList<Node> paths = new LinkedList<>();
            paths.add(this);
            node.findPaths(paths,this);
        }
    }
}
