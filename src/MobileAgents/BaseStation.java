package MobileAgents;

import javafx.scene.shape.Circle;

import java.util.LinkedList;

public class BaseStation extends Node{

    private LinkedList<LinkedList<Object>> agents = new LinkedList<>();
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
    public synchronized void passID(int id, int x, int y, LinkedList<Node> path, LinkedList<Node> returnPath){
        LinkedList<Object> agent = new LinkedList<>();
        agent.addLast(id);
        agent.addLast(x);
        agent.addLast(y);
        agents.add(agent);
        path.addFirst(this);
        Node node = returnPath.removeFirst();
        node.returnID(id,x,y,true,path,returnPath);
        System.out.println(agent.get(0)+", "+agent.get(1)+", "+agent.get(2));
    }
    public void printIDs(){
        for(LinkedList<Object> list: agents){
            System.out.println(list.get(0)+", "+list.get(1)+", "+list.get(2));
        }
    }
}
