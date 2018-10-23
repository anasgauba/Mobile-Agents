package MobileAgents;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

import java.util.LinkedList;
import java.util.Random;

public class Node extends Thread{
    private LinkedList<LinkedList<Node>> pathsToBaseStation;
    protected LinkedList<Node> neighbors;
    protected LinkedList<Node> liveNeighbors;
    private Status state;
    private Agent agent;
    protected int x;
    protected int y;
    protected Circle circle;

    public Node(Status state, int x, int y, Circle circle){
        this.circle=circle;
        this.x=x;
        this.y=y;
        pathsToBaseStation = new LinkedList<>();
        this.neighbors=new LinkedList<>();
        this.liveNeighbors = neighbors;
        this.state = state;
    }

    /**
     * This function will check for the status of the node and if the node is yellow it has a timer
     * to wait for 2 seconds and then turn red. If it turns red it should notify other neighbors.
     * It also updates the color of Circle object.
     * (Note than in the future we might use BlockingQueues, so run will handle them)
     */
    @Override
    public void run(){

    }

    /**
     * This function will pass the agent to a RANDOM neighbor
     * (which doesn't have an agent already) and set the agent to null (Not
     * cloning).
     */
    private void passAgent(){
        int length = liveNeighbors.size();
        Random rnd = new Random();
        boolean stat = false;
        while(!stat) {
            int len = rnd.nextInt(length);
            Node node = liveNeighbors.get(len);
            stat = node.recieveAgent(agent);
        }
        agent = null;

    }
    public void addNeighbor(Node node){
        neighbors.add(node);
        liveNeighbors.add(node);
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public synchronized boolean recieveAgent(Agent agent){
        if(agent==null) {
            this.agent = agent;
            return true;
        }
        return false;

    }

    /**
     * It clones and sends the clone of the agent to to live nodes that are blue
     * or yellow and do not already have an agent.
     */
    private void sendCloneAgent(){
        Agent clone = new Agent();
        for (Node n : liveNeighbors) {
            if (n.state.equals(Status.BLUE) || n.state.equals(Status.YELLOW)
                    && n.agent == null) {
                recieveClone(clone);
            }
        }
    }

    public synchronized boolean recieveClone(Agent clone){
        if(agent==null){
            agent=clone;
            return true;
        }
        else{
            return false;
        }
    }
    public synchronized void neigborStausChanged(Node caller){
        if(state==Status.BLUE) {
            state = Status.YELLOW;
            liveNeighbors.remove(caller);
        }
    }

    public void findPaths(LinkedList<Node> path,Node caller){
        pathsToBaseStation.add(path);
        path.addFirst(this);
        for(Node node: neighbors){
            //if(!node.equals(caller)){
            if(!path.contains(node)){
                node.findPaths(path,this);
            }
        }
    }

    public void sendID(int id, int x, int y){
        LinkedList<Node> returnPath = new LinkedList<Node>();
        returnPath.add(this);
        LinkedList<Node> path = pathsToBaseStation.getFirst();
        Node nextNode = path.getFirst();
        while(!liveNeighbors.contains(nextNode)){
            pathsToBaseStation.removeFirst();
            if(pathsToBaseStation.size()>=1) {
                path = pathsToBaseStation.getFirst();
                nextNode = path.getFirst();
            }
        }
        path.removeFirst();
        nextNode.passID(id,x,y,path,returnPath);
    }
    //public void passIDToQueue(int id, int x, int y, LinkedList<Node> path, LinkedList<Node> returnPath){
    //
    //}
    public synchronized void passID(int id, int x, int y, LinkedList<Node> path, LinkedList<Node> returnPath){
        System.out.println(x+","+y);
        if(path.size()==0){
            path.addFirst(this);
            Node node = returnPath.removeFirst();
            node.returnID(id,x,y,true,path,returnPath);
        }
        Node nextNode = path.getFirst();
        if(!liveNeighbors.contains(nextNode)){
            pathsToBaseStation.remove(path);
            path.addFirst(this);
            Node node = returnPath.removeFirst();
            node.returnID(id,x,y,false,path,returnPath);
        }
        path.removeFirst();
        returnPath.addFirst(this);
        nextNode.passID(id,x,y,path,returnPath);
    }
    public synchronized void returnID (int id, int x, int y,boolean status, LinkedList<Node> path,LinkedList<Node> returnPath){
        if(!status) {
            pathsToBaseStation.remove(path);
        }
        if (returnPath.size()==0) {
            if(!status){
                sendID(id,x,y);
            }
        }
        else{
            path.addFirst(this);
            Node node = returnPath.removeFirst();
            node.returnID(id,x,y,status,path,returnPath);
        }
    }
}
