package MobileAgents;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class MobileAgents extends Application implements Observer {
    private Map<Node,Circle> map = new HashMap<>();
    /**
     * This reads the config file and creates the node objects.
     * It also creates the display elements. The display consists of circles as nodes
     * in x,y of config file. The edges are lines from x1,y1 to x2,y2.
     * IMPORTANT NOTE: The circle objects which are nodes should be passed to node objects when they are being created.
     * The size of canvas is: width=MAX(c*x)+20 ans height=MAX(c*y)+20. where c is a constant.
     * @param args
     */
    public static void main(String [] args){
        launch(args);
    }
    @Override
    public void stop(){
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        int node1X;
        int node1Y;
        int node2X;
        int node2Y;
        double node1XPixel;
        double node1YPixel;
        double node2XPixel;
        double node2YPixel;
        Circle circle;
        Line edge;
        Node onFire=null;
        int id=0;
        File file = new File("sample.txt");
        Scanner scanIn = new Scanner(file);
        LinkedList<Node> nodes = new LinkedList<Node>();
        LinkedList<Circle> circles = new LinkedList<>();
        BaseStation baseStation = null;
        while (scanIn.hasNext()) {
            String x = scanIn.next();
            if (x.equals("node")) {
                node1X = scanIn.nextInt();
                node1Y = scanIn.nextInt();
                if (baseStation != null && baseStation.getX() == node1X && baseStation.getY() == node1Y) {
                    continue;
                }
                node1XPixel = (50 * node1X) + 100;
                node1YPixel = (50 * node1Y) + 100;
                circle = new Circle(node1XPixel, node1YPixel, 10);
                circle.setFill(Paint.valueOf("blue"));
                Node node = new Node(Status.BLUE, node1X, node1Y);
                node.setID(id);
                map.put(node,circle);
                id++;
                //root.getChildren().add(circle);
                circles.add(circle);
                nodes.add(node);
            }
            if (x.equals("station")) {
                node1X = scanIn.nextInt();
                node1Y = scanIn.nextInt();
                node1XPixel = (50 * node1X) + 100;
                node1YPixel = (50 * node1Y) + 100;
                circle = new Circle(node1XPixel, node1YPixel, 10);
                circle.setFill(Paint.valueOf("green"));
                BaseStation node = new BaseStation(Status.BLUE, node1X, node1Y);
                //root.getChildren().add(circle);
                node.setID(id);
                map.put(node,circle);
                id++;
                circles.add(circle);
                baseStation = node;
                //nodes.add(node);

                LinkedList<Node> temp = new LinkedList<>(nodes);
                for (Node n : temp) {
                    if (n.getX() == node1X && n.getY() == node1Y) {
                        nodes.remove(n);
                        map.remove(n);
                    }
                }
            }
        }
        Scanner scanIn2 = new Scanner(file);
        while (scanIn2.hasNext()) {
            String x = scanIn2.next();
            if (x.equals("edge")) {
                node1X = scanIn2.nextInt();
                node1Y = scanIn2.nextInt();
                node2X = scanIn2.nextInt();
                node2Y = scanIn2.nextInt();
                node1XPixel = (50 * node1X) + 100;
                node1YPixel = (50 * node1Y) + 100;
                node2XPixel = (50 * node2X) + 100;
                node2YPixel = (50 * node2Y) + 100;
                Node n1 = null;
                Node n2 = null;
                for (Node n : nodes) {
                    if (n.getX() == node1X && n.getY() == node1Y) {
                        n1 = n;
                    }
                    if (n.getX() == node2X && n.getY() == node2Y) {
                        n2 = n;
                    }
                }
                if(baseStation!=null) {
                    if (baseStation.getX() == node1X && baseStation.getY() == node1Y) {
                        n1 = baseStation;
                    } else if (baseStation.getX() == node2X && baseStation.getY()
                            == node2Y) {
                        n2 = baseStation;
                    }
                }
                if (n1 != null && n2 != null) {
                    n1.addNeighbor(n2);
                    n2.addNeighbor(n1);
                }
                edge = new Line(node1XPixel, node1YPixel, node2XPixel,
                        node2YPixel);
                root.getChildren().add(edge);
            }
            if (x.equals("fire")) {
                node1X = scanIn2.nextInt();
                node1Y = scanIn2.nextInt();
                node1XPixel = (50 * node1X) + 100;
                node1YPixel = (50 * node1Y) + 100;
                for (Node n : nodes) {
                    if (n.getX() == node1X && n.getY() == node1Y) {
                        onFire = n;
//                        onFire.setState(Status.RED);
                    }
                }
                circle = new Circle(node1XPixel, node1YPixel, 10);
                circle.setFill(Paint.valueOf("red"));
                circles.add(circle);
            }

        }
        for(Circle c:circles){
            root.getChildren().add(c);
        }
        root.setStyle("-fx-background-color: gray");
        primaryStage.setTitle("Mobile Agents");
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.show();

        //baseStation.findPaths();
        //Node node = nodes.get(3);
        //node.sendID(public1,node.getX(),node.getY());

        for(Node n:map.keySet()){
            n.addObserver(this);
        }

        if(baseStation!=null && onFire!=null) {
            baseStation.findPaths();
            onFire.setState(Status.RED);
            onFire.scream();
            baseStation.recieveAgent(new Agent(baseStation, true));
        }
        System.out.println("Terminateeeerereerevlsjdhsdjglsd");

    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        //System.out.println("arg");
        Node node = (Node)o;
        Circle circle = map.get(node);
        if(arg.equals("red")){
            circle.setFill(Paint.valueOf("red"));
        }
        else if(arg.equals("yellow")){
            circle.setFill(Paint.valueOf("yellow"));
        }
        else if(arg.equals("border")){
            circle.setStroke(Paint.valueOf("purple"));
            circle.setStrokeWidth(3);
        }
        else if(arg.equals("removeBorder")){
            circle.setStroke(null);
        }
    }

//    public MobileAgents(String file){
//        readFile(file);
//    }

//    public ArrayList<String> readFile(String file){
//
//        ArrayList<String> data = new ArrayList<>();
//
//        ClassLoader classLoader = getClass().getClassLoader();
//        InputStream inputStream = classLoader.getResourceAsStream(file);
//
//        Scanner s1 = new Scanner(inputStream);
//        while(s1.hasNext()){
//            data.add(s1.nextLine());
//        }
//        System.out.println(data);
//        return data;
//    }
}
