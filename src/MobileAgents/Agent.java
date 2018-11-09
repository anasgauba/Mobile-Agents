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
//        this.killed = false;
        start();
    }
    public void kill(){
        queue.add(true);
        killed=true;
    }
/*    public void updateCurrentNode(Node node){
        currentNode=node;
    }*/
    @Override
    public void run() {
        while (tasks) {
            Node nextNode = currentNode.passAgent();
            //System.out.println(nextNode.getStatus());
            currentNode = nextNode;
            if (currentNode.getStatus().equals(Status.YELLOW)) {
                currentNode.sendCloneAgent();
                tasks = false;
            }
        }
        while(!killed){
            if(!cloned && currentNode.getStatus().equals(Status.YELLOW)){
                /*System.out.println("**************************************");
                int threadCount = 0;
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                for ( Thread t : threadSet){
                    if ( t.getThreadGroup() == Thread.currentThread().getThreadGroup()){
                        System.out.println("Thread :"+t.getClass()+":"+"state:"+t.getState());
                        ++threadCount;
                    }
                }*/
                currentNode.sendCloneAgent();
                cloned=true;
            }
            else if(cloned && currentNode.getBurner()!=null){
                try {
                    boolean b = queue.take();
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>"+b);
                    killed=true;
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
        }
        //currentNode.scream();
    }
}
