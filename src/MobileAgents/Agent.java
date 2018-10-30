package MobileAgents;

public class Agent extends Thread{
    private Node currentNode;
    private boolean tasks;
//    private boolean killed;
    public Agent(Node node, boolean task){
        this.currentNode = node;
        this.tasks = task;
//        this.killed = false;
        run();
    }
    @Override
    public void run(){
        while (tasks) {
            currentNode.passAgent();
            if (currentNode.getStatus().equals(Status.YELLOW)) {
                currentNode.sendCloneAgent();
                tasks = false;
            }
        }
        if (currentNode.getStatus().equals(Status.RED)) {
            currentNode.neigborStausChanged(currentNode);
            currentNode.sendCloneAgent();
//                killed = true;
        }
    }

//    public boolean getAgentKilledStatus() {
//        return killed;
//    }
}
