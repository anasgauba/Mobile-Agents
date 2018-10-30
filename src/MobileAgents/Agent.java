package MobileAgents;

public class Agent extends Thread {
    private Node currentNode;
    private boolean tasks;
    private boolean killed=false;
    public Agent(Node node, boolean task) {
        this.currentNode = node;
        this.tasks = task;
//        this.killed = false;
        run();
    }
    public void kill(){
        killed=true;
    }
/*    public void updateCurrentNode(Node node){
        currentNode=node;
    }*/
    @Override
    public void run() {
        while (tasks) {
            Node nextNode = currentNode.passAgent();
            currentNode = nextNode;
            if (currentNode.getStatus().equals(Status.YELLOW)) {
                currentNode.sendCloneAgent();
                tasks = false;
            }
        }
        while(!killed){
            if(currentNode.getStatus().equals(Status.YELLOW)){
                currentNode.sendCloneAgent();
            }
        }
        currentNode.scream();
    }
}
