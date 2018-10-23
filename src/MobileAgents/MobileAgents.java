package MobileAgents;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MobileAgents extends Application {
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

        File file = new File("C:\\Users\\anasf\\IdeaProjects\\Mobile-Agents\\sample" +
                ".txt");
        Scanner scanIn = new Scanner(file);
        while (scanIn.hasNext()) {
            String x = scanIn.next();
            if (x.equals("node")) {
                node1X = scanIn.nextInt();
                node1Y = scanIn.nextInt();
                node1XPixel = (50*node1X) + 100;
                node1YPixel = (50*node1Y) + 100;
                circle = new Circle(node1XPixel, node1YPixel, 10);
                Node node = new Node(Status.BLUE, node1X, node1Y, circle);
                root.getChildren().add(circle);
            }
            else if (x.equals("edge")) {
                node1X = scanIn.nextInt();
                node1Y = scanIn.nextInt();
                node2X = scanIn.nextInt();
                node2Y = scanIn.nextInt();
                node1XPixel = (50*node1X) + 100;
                node1YPixel = (50*node1Y) + 100;
                node2XPixel = (50*node2X) + 100;
                node2YPixel = (50*node2Y) + 100;
                edge = new Line(node1XPixel, node1YPixel, node2XPixel,
                        node2YPixel);
                root.getChildren().add(edge);
            }
        }
        root.setStyle("-fx-background-color: rgb(72, 103, 178)");
        primaryStage.setTitle("Mobile Agents");
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.show();
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
