package MobileAgents;

public class StatusChecker extends Thread {
    private boolean dead = false;
    private Node node ;
    public StatusChecker(Node node){
        this.node=node;
        start();
    }
    @Override
    public void run(){
        try {
            sleep(3000);
            node.setState(Status.RED);
            node.scream();
            node.stop();
            //stop();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
