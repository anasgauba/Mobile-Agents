package MobileAgents;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This is the agent class that at first will find a yellow node, then it clones itself on yellow nodes and on their blue neighbors.
 */
public class Agent extends Thread {
    private Node currentNode;
    private boolean tasks;
    private boolean killed=false;
    private boolean cloned = false;
    private BlockingQueue<Boolean> queue = new LinkedBlockingQueue<>();

    /**
     * This is the constructor that creates the agent with the knowledge
     * @param node
     * @param task
     */
    public Agent(Node node, boolean task) {
        this.currentNode = node;
        this.tasks = task;
        start();
    }

    /**
     * This one kills the agent.
     */
    public synchronized void kill(){
        queue.add(true);
        killed=true;
    }

    /**
     * This function does all the agent related tasks. For the first agent, it goes through the nodes randomly to find a
     * yellow node and stays there. If a node turns yellow, its agent clones itself to the eligible neighbors.
     */
    @Override
    public void run() {
        while (tasks) {
            currentNode = currentNode.passAgent();
            if (currentNode.getStatus().equals(Status.YELLOW)) {
                currentNode.sendCloneAgent();
                tasks = false;
            }
        }
        while(!killed){
            if(!cloned && currentNode.getStatus().equals(Status.YELLOW)){
                currentNode.sendCloneAgent();
                cloned=true;
            }
            else if(cloned && currentNode.getBurner()!=null){
                try {
                    boolean b = queue.take();
                    killed=true;
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
        }
    }
}
