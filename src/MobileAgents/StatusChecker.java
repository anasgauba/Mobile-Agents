package MobileAgents;

public class StatusChecker extends Thread {
    private boolean dead = false;
    private Node node ;
    public StatusChecker(Node node){
        run();
        this.node=node;
    }
    @Override
    public void run(){
        try {
            sleep(3000);
            node.setState(Status.RED);
        }
        catch(Exception e){
            stop();
        }
    }
}
