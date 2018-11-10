package MobileAgents;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent extends Thread {
    private Node currentNode;
    private boolean tasks;
    private boolean killed=false;
    private boolean cloned = false;
    private BlockingQueue<Boolean> queue = new LinkedBlockingQueue<>();
    public Agent(Node node, boolean task) {
        this.currentNode = node;
        this.tasks = task;
        start();
    }
    public synchronized void kill(){
        queue.add(true);
        killed=true;
    }

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
