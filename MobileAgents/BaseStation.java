package MobileAgents;

import java.util.LinkedList;

public class BaseStation extends Node{

    private LinkedList<LinkedList<Integer>> agents = new LinkedList<>();
    public BaseStation(Status state,int x, int y){
        super(state,x,y);
    }

    /**
     * This function finds the path from any node to this node and save them
     * there. Note that it is only invoked at the start of the program.
     */
    public void findPaths(){
        for(Node node:neighbors){
            LinkedList<Node> paths = new LinkedList<>();
            paths.add(this);
            node.findPaths(paths,this);
        }
    }
    /**
     * This function will store the returned id and location of the agents
     */
    @Override
    public synchronized void passID(int id, int x, int y
            , LinkedList<Node> path, LinkedList<Node> returnPath){
        LinkedList<Integer> agent = new LinkedList<>();
        agent.addLast(id);
        agent.addLast(x);
        agent.addLast(y);
        agents.add(agent);
        path.addFirst(this);
        Node node = returnPath.removeFirst();
        returnID(id,x,y,true,path,returnPath);
        System.out.println("ID: "+agent.get(0)+", x: "+agent.get(1)+", y: "+agent.get(2));
    }

    /**
     * This one save the id because it don't need to save it.
     * @param id of agent.
     * @param x nodeX location.
     * @param y nodeY location.
     */
    @Override
    public void sendID(int id, int x, int y){
        LinkedList<Integer> agent = new LinkedList<>();
        agent.addLast(id);
        agent.addLast(x);
        agent.addLast(y);
        for(LinkedList<Integer> list: agents){
            if(list.get(0).equals(id)){
                return;
            }
        }
        agents.add(agent);
        System.out.println("ID: "+agent.get(0)+", x: "+agent.get(1)+", y: "+agent.get(2));
    }

    /**
     * this one makes and send agent ID.
     */
    @Override
    public void makeAndSendAgentID(){
        sendID(id, x, y);
        id=-1;
    }

    /**
     * Prints all ID's
     */
    public void printIDs(){
        for(LinkedList<Integer> list: agents){
            System.out.println("ID: "+list.get(0)+", x: "+list.get(1)+", y: "+list.get(2));
        }
    }
}
