package MobileAgents;

public class Agent extends Thread {
    private Node currentNode;
    private boolean tasks;
    private boolean killed=false;
    private boolean cloned = false;
    public Agent(Node node, boolean task) {
        this.currentNode = node;
        this.tasks = task;
//        this.killed = false;
        start();
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
            //System.out.println(nextNode.getStatus());
            currentNode = nextNode;
            if (currentNode.getStatus().equals(Status.YELLOW)) {
                currentNode.sendCloneAgent();
                tasks = false;
            }
        }
        while(!killed){
            if(!cloned && currentNode.getStatus().equals(Status.YELLOW)){
                System.out.println("hrret");
                currentNode.sendCloneAgent();
                cloned=true;
            }
            else if(cloned && currentNode.getBurner()!=null){
                try {
                    currentNode.getBurner().join();
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
