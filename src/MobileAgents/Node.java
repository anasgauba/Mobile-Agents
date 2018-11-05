package MobileAgents;

import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node extends Thread{
    private LinkedList<LinkedList<Node>> pathsToBaseStation;
    protected LinkedList<Node> neighbors;
    protected LinkedList<Node> liveNeighbors;
    private Status state;
    private Agent agent;
    protected int x;
    protected int y;
    protected Circle circle;
    private int id;
    private boolean killed = false;
    private BlockingQueue<LinkedList<Object>> queue;
    private StatusChecker burner=null;

    public Node(Status state, int x, int y, Circle circle){
        this.circle=circle;
        this.x=x;
        this.y=y;
        pathsToBaseStation = new LinkedList<>();
        this.neighbors=new LinkedList<>();
        this.liveNeighbors = new LinkedList<>();
        this.state = state;
        queue = new LinkedBlockingQueue<LinkedList<Object>>();
        start();
    }

    /**
     * This function will check for the status of the node and if the node is yellow it has a timer
     * to wait for 2 seconds and then turn red. If it turns red it should notify other neighbors.
     * It also updates the color of Circle object.
     * (Note than in the future we might use BlockingQueues, so run will handle them)
     */
    @Override
    public void run(){
        while(!killed) {
            try {
                LinkedList<Object> list = queue.take();
                if (list.get(3) == null) {
                    passIDFromQueue((int) list.get(0), (int) list.get(1), (int) list.get(2), (LinkedList<Node>) list.get(4), (LinkedList<Node>) list.get(5));
                } else {
                    returnIDFromQueue((int) list.get(0), (int) list.get(1), (int) list.get(2), (boolean) list.get(3), (LinkedList<Node>) list.get(4), (LinkedList<Node>) list.get(5));
                }
            } catch (Exception e) {
                System.out.println("Exited");
                killed=true;
            }
        }
    }

    public synchronized Status getStatus(){
        return state;
    }
    public StatusChecker getBurner(){
        return burner;
    }
    public synchronized void setState(Status status){
        System.out.println("Im red:"+id);
        if(status.equals(Status.RED)){
            circle.setFill(Paint.valueOf("red"));
            if(agent!=null){
                agent.kill();
            }
        }
        else if(status.equals(Status.YELLOW)){
            circle.setFill(Paint.valueOf("yellow"));
        }
        state=status;
    }
    /**
     * This function will pass the agent to a RANDOM neighbor
     * (which doesn't have an agent already) and set the agent to null (Not
     * cloning).
     */
    public Node passAgent(){
        int length = liveNeighbors.size();
        Random rnd = new Random();
        boolean stat = false;
        Node node=null;
        while(!stat) {
            int len = rnd.nextInt(length);
            node = liveNeighbors.get(len);
            stat = node.recieveAgent(agent);
        }
        agent = null;
        unmarkNode();
        return node;
    }
    public void setID(int id){
        this.id=id;
    }
    /**
     * Make a unique id for the agent and pass it...
     */
    public void makeAndSendAgentID(){
        sendID(id, x, y);
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
        if(this.agent==null) {
            this.agent = agent;
            markNode();
            return true;
        }
        return false;
    }
    private synchronized void markNode(){

        circle.setStroke(Paint.valueOf("purple"));
        circle.setStrokeWidth(3);
        //circle.setStyle("-fx-border-color: purple; -fx-border-width: 10");
    }
    private synchronized void unmarkNode(){
        circle.setStroke(null);
    }
    /**
     * It clones and sends the clone of the agent to to live nodes that are blue
     * or yellow and do not already have an agent.
     */
    public void sendCloneAgent(){
        LinkedList<Node> tempLiveNeighbors = new LinkedList<>(liveNeighbors);
        for (Node n : tempLiveNeighbors) {
            if (n.state.equals(Status.BLUE) || n.state.equals(Status.YELLOW)
                    && n.agent == null) {
                Agent clone = new Agent(n,false);
                n.recieveClone(clone);
            }
        }
    }

    public synchronized boolean recieveClone(Agent clone){
        if(agent==null){
            System.out.println("Received>>>"+this.getX()+","+this.getY());
            agent=clone;
            makeAndSendAgentID();
            markNode();
            return true;
        }
        else{
            return false;
        }
    }

    public void scream(){
        unmarkNode();
        LinkedList<Node> tempLiveNeighbors = new LinkedList<>(liveNeighbors);
        for(Node node: tempLiveNeighbors){
            node.neigborStausChanged(this);
        }
    }
    private synchronized void neigborStausChanged(Node caller){
        if(this.getStatus().equals(Status.BLUE)) {
            this.setState(Status.YELLOW);
            burner = new StatusChecker(this);
            //System.out.println(agent);
            liveNeighbors.remove(caller);
        }
    }

    public void findPaths(LinkedList<Node> path,Node caller){
        pathsToBaseStation.add(path);
        LinkedList<Node> nextPath = new LinkedList<>(path);
        nextPath.addFirst(this);
        for(Node node: neighbors){
            //if(!node.equals(caller)){
            if(!path.contains(node)){
                node.findPaths(nextPath,this);
            }
        }
    }

    public synchronized void sendID(int id, int x, int y){
        System.out.println("send id..."+id);
        LinkedList<Node> returnPath = new LinkedList<Node>();
        returnPath.add(this);
        LinkedList<Node> path = pathsToBaseStation.getFirst();
        Node nextNode = path.getFirst();
        //System.out.println(liveNeighbors);
        //System.out.println(path);
        //System.out.println(this);
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
        LinkedList<Object> list = new LinkedList<>();
        list.addLast(id);
        list.addLast(x);
        list.addLast(y);
        list.addLast(null);
        list.addLast(path);
        list.addLast(returnPath);
        //System.out.println(list);
        queue.add(list);
    }
    public synchronized void passIDFromQueue(int id, int x, int y, LinkedList<Node> path, LinkedList<Node> returnPath){
        //System.out.println(">>>"+this);
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
    public synchronized void returnID(int id, int x, int y,boolean status, LinkedList<Node> path,LinkedList<Node> returnPath){
        System.out.println(status);
        LinkedList<Object> list = new LinkedList<>();
        list.addLast(id);
        list.addLast(x);
        list.addLast(y);
        list.addLast(status);
        list.addLast(path);
        list.addLast(returnPath);
        queue.add(list);
    }
    public synchronized void returnIDFromQueue (int id, int x, int y,boolean status, LinkedList<Node> path,LinkedList<Node> returnPath){
        //System.out.println(returnPath);
        //System.out.println(this);
        if(!status) {
            pathsToBaseStation.remove(path);
        }
        if (returnPath.size()==0) {
            if(!status){
                System.out.println(this.liveNeighbors);
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
