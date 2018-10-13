package MobileAgents;

import java.util.LinkedList;

public class Node {
    private LinkedList<LinkedList<Node>> pathsToBaseStation;
    protected LinkedList<Node> neighbors;
    private String state;
    public Node(String state, LinkedList<Node> neighbors){
        pathsToBaseStation = new LinkedList<>();
        this.neighbors=neighbors;
        state = state;
    }

    public void findPaths(LinkedList<Node> path,Node caller){
        pathsToBaseStation.add(path);
        path.addFirst(this);
        for(Node node: neighbors){
            if(!node.equals(caller)){
                node.findPaths(path,this);
            }
        }
    }

    public String getState(){
        return state;
    }
    /**
     * This updates the status and if its red, it will kill the node.
     * @param newStatus
     */
    public void updateStatus(String newStatus){
        state = newStatus;
    }

    /**
     * This clones the Agent using its runnable.
     * @param runnable
     */
    public void clone(Runnable runnable){

    }
    /**
     * This receives the agent.
     * Note that this is NOT cloning.
     */
    public void receiveAgent(Agent agent){

    }

    /**
     * This one passes the agent to a RANDOM neighbor node (using receiveAgent).
     * Note that this is NOT cloning.
     * @param agent
     */
    public void passAgent(Agent agent){

    }

    /**
     * This sends the id and location of each new agent to the base station. This method uses routs
     * stored in this object to find a way to send message to the base station. It tests each of
     * those routs until it finds one that is not blocked.
     * @param id
     * @param location
     */
    public void sendMessageToBaseStation(int id, Node location){

    }

    /**
     * This helps sendMessageToBaseStation. It gets the next node to send the message to and
     * it calls the same functions from the next node and in a chain process sends the message
     * to the base. Note that this function is called from the neighbor nodes.
     * @return true if message reaches the base station and false if it doesn't (path is blocked)
     * @param id
     * @param location
     * @param nextNode
     */
    public boolean sendMessageToNode(int id,Node location, Node nextNode){

        return false;
    }


}
