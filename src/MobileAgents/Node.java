package MobileAgents;

import javafx.application.Platform;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The node provides all the required methods to do all the tasks that
 * agent will need or are needed to propagate the yellow and red nodes. It
 * also has the functionality to send ID, clone, walk, update
 * display and make the nodes red.
 */
public class Node extends Observable implements Runnable{
    /**
     * This class turns yellow nodes red every 3 seconds.
     */
    private class StatusChecker extends Thread {
        private Node node;

        /**
         * this is the constructor that creates this object.
         * @param node passed in node
         */
        public StatusChecker(Node node){
            this.node=node;
            start();
        }

        /**
         * The run just runs the program and waits for 3 seconds,
         * then it turns the node red.
         */
        @Override
        public void run(){
            try {
                sleep(3000);
                node.scream();
                node.kill();
                node.setState(Status.RED);

            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }

    private LinkedList<LinkedList<Node>> pathsToBaseStation;
    protected LinkedList<Node> neighbors;
    protected LinkedList<Node> liveNeighbors;
    private Status state;
    private Agent agent;
    protected int x;
    protected int y;
    protected int id;
    private boolean killed = false;
    private BlockingQueue<LinkedList<Object>> queue;
    private StatusChecker burner=null;


    /**
     * This creates the node object.
     * @param state of the node.
     * @param x nodeX location.
     * @param y nodeY location.
     */
    public Node(Status state, int x, int y){
        this.x=x;
        this.y=y;
        pathsToBaseStation = new LinkedList<>();
        this.neighbors=new LinkedList<>();
        this.liveNeighbors = new LinkedList<>();
        this.state = state;
        queue = new LinkedBlockingQueue<LinkedList<Object>>();
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * This function will check for the status of the node and if the node is
     * yellow it has a timer to wait for 2 seconds and then turn red. If it
     * turns red it should notify other neighbors. It also updates the color
     * of Circle object.(Note than in the future we might use BlockingQueues,
     * so run will handle them)
     */
    @Override
    public void run(){
        while(!killed) {
            try {
                LinkedList<Object> list = queue.take();
                if(list.size()!=0) {
                    if (list.get(3) == null) {
                        passIDFromQueue((int) list.get(0), (int) list.get(1),
                                (int) list.get(2), (LinkedList<Node>) list
                                        .get(4), (LinkedList<Node>) list.get(5));
                    } else {
                        returnIDFromQueue((int)list.get(0),(int)list.get(1),
                                (int) list.get(2), (boolean) list.get(3),
                                (LinkedList<Node>) list.get(4),(LinkedList<Node>) list.get(5));
                    }
                }
            }
            catch (Exception e){
                System.out.println(e);
                killed=true;
            }
        }
    }

    /**
     * Kills the node and agent.
     */
    public void kill(){
        agent.kill();
        killed=true;
        LinkedList<Object> list = new LinkedList<>();
        queue.add(list);
    }

    /**
     * gets the state of the node.
     * @return the status of the node.
     */
    public synchronized Status getStatus(){
        return state;
    }

    /**
     * gets the thread that spreads the fire.
     * @return the statusChecker
     */
    public StatusChecker getBurner(){
        return burner;
    }

    /**
     * Sets the state of the node and calls functions to update screen.
     * @param status the state of the node.
     */
    public synchronized void setState(Status status){
        state=status;
        if(status.equals(Status.RED)){
            updateScreen("red");
        }
        else if(status.equals(Status.YELLOW)){
            updateScreen("yellow");
        }
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
        updateScreen("removeBorder");
        return node;
    }

    /**
     * sets the id for the agent.
     * @param id sets id.
     */
    public void setID(int id){
        this.id=id;
    }
    /**
     * Make a unique id for the agent and pass it...
     */
    public void makeAndSendAgentID(){
        sendID(id, x, y);
    }

    /**
     * this node adds neighbor node to the neighbor list.
     * @param node to add neighbor of
     */
    public void addNeighbor(Node node){
        neighbors.add(node);
        liveNeighbors.add(node);
    }

    /**
     * gets the x position of specific node.
     * @return x location of node.
     */
    public int getX(){
        return x;
    }
    /**
     * gets the y position of specific node.
     * @return y location of node.
     */
    public int getY(){
        return y;
    }

    /**
     * receives the agent on a specific node if the agent is not already there.
     * @param agent on this node,
     * @return true/false if receiveAgent.
     */
    public synchronized boolean recieveAgent(Agent agent){
        if(this.agent==null) {
            this.agent = agent;
            updateScreen("border");
            return true;
        }
        return false;
    }

    /**
     * This function updates the screen element using the observer pattern.
     * @param arg for observer (MobileAgents)
     */
    private synchronized void updateScreen(String arg){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setChanged();
                notifyObservers(arg);
            }
        });
    }
    /**
     * It clones and sends the clone of the agent to to live nodes that are blue
     * or yellow and do not already have an agent.
     */
    public void sendCloneAgent(){
        LinkedList<Node> tempLiveNeighbors = new LinkedList<>(liveNeighbors);
        for (Node n : tempLiveNeighbors) {
            if ((n.getStatus().equals(Status.BLUE) || n.getStatus().equals(Status.YELLOW))
                    && n.agent == null) {
                Agent clone = new Agent(n,false);
                boolean b = n.recieveClone(clone);
                if(!b){
                    clone.kill();
                }
            }
        }
    }
    /**
     * recieves the clone.
     * @param clone of agent
     * @return true/false if node receives clone.
     */
    private synchronized boolean recieveClone(Agent clone){
        if(agent==null){
            agent=clone;
            makeAndSendAgentID();
            updateScreen("border");
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * this node lets its neighbors know that he’s dead and fire is spreading.
     */
    public void scream(){
        updateScreen("removeBorder");
        LinkedList<Node> tempLiveNeighbors = new LinkedList<>(liveNeighbors);
        for(Node node: tempLiveNeighbors){
            node.neigborStausChanged(this);
        }
    }

    /**
     * When a node turns red it tells its neighbors using this, and they turn
     * yellow.
     * @param caller node is calling.
     */
    private synchronized void neigborStausChanged(Node caller){
        if(this.getStatus().equals(Status.BLUE)) {
            this.setState(Status.YELLOW);
            burner = new StatusChecker(this);
            liveNeighbors.remove(caller);
        }
    }

    /**
     * This functions is used in the beginning of the program, to
     * find all possible paths to the base station.
     * @param path of node.
     * @param caller node is calling.
     */
    public void findPaths(LinkedList<Node> path,Node caller){
        pathsToBaseStation.add(path);
        LinkedList<Node> nextPath = new LinkedList<>(path);
        nextPath.addFirst(this);
        for(Node node: neighbors){
            if(!path.contains(node)){
                node.findPaths(nextPath,this);
            }
        }
    }

    /**
     * This one uses the paths to send an id to the next node via calling the
     * pass id function.
     * @param id of agent.
     * @param x nodeX location.
     * @param y nodeY location.
     */
    public synchronized void sendID(int id, int x, int y){
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

    /**
     * This one adds a message to a blocking queue to be passed.
     * @param id of agent.
     * @param x nodeX location.
     * @param y nodeY location.
     * @param path of the node.
     * @param returnPath of baseStation.
     */
    public synchronized void passID(int id, int x, int y
            , LinkedList<Node> path, LinkedList<Node> returnPath){
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

    /**
     * This function will be called from the queue to pass id to the next node.
     * It checks to see if the current path is available,
     * then it sends to next node. If not available, it will return false.
     * @param id of agent.
     * @param x nodeX location.
     * @param y nodeY location.
     * @param path of node.
     * @param returnPath of node.
     */
    public void passIDFromQueue(int id, int x, int y
            , LinkedList<Node> path, LinkedList<Node> returnPath){
        if(path.size()==0){
            path.addFirst(this);
            Node node = returnPath.removeFirst();
            node.returnID(id,x,y,true,path,returnPath);
            return;
        }
        Node nextNode = path.getFirst();
        if(!liveNeighbors.contains(nextNode)){
            pathsToBaseStation.remove(path);
            path.addFirst(this);
            Node node = returnPath.removeFirst();
            node.returnID(id,x,y,false,path,returnPath);
            return;
        }
        path.removeFirst();
        returnPath.addFirst(this);
        nextNode.passID(id,x,y,path,returnPath);
    }

    /**
     * This adds return status to the queue.
     * @param id of agent.
     * @param x nodeX location.
     * @param y nodeY location.
     * @param status of node.
     * @param path of node.
     * @param returnPath of node.
     */
    public synchronized void returnID(int id, int x, int y
            ,boolean status, LinkedList<Node> path,LinkedList<Node> returnPath){
        LinkedList<Object> list = new LinkedList<>();
        list.addLast(id);
        list.addLast(x);
        list.addLast(y);
        list.addLast(status);
        list.addLast(path);
        list.addLast(returnPath);
        queue.add(list);
    }

    /**
     * This returns the success or not success of the function.
     * @param id of agent.
     * @param x nodeX location.
     * @param y nodeY location.
     * @param status of node.
     * @param path of node.
     * @param returnPath of node.
     */
    public void returnIDFromQueue (int id, int x, int y
            ,boolean status, LinkedList<Node> path,LinkedList<Node> returnPath){
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
