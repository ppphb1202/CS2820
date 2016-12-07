package production;

import java.io.IOException;

/**
 * 
 * @author SCOTT HOEFER
 * @author KANE TEMPLETON (MAIN AUTHOR ON PRODUCTION.JAVA)
 *
 */
public class Production {
    
    private static ControlPanel panel = new ControlPanel();
    private static Visualizer visualizer;

    public static final int SQUARE_SIZE=32;
    public static final int FLOOR_SIZE=20;
    
    
    public static void main(String[] args) throws IOException {
        //panel = new ControlPanel();
        Master M = new Master();
        M.initializeEvents();
        init();
        panel.getMaster().start();
    }
    
    public static void init() throws IOException {
        Constants.initImages();
        visualizer = new Visualizer(FLOOR_SIZE*SQUARE_SIZE,FLOOR_SIZE*SQUARE_SIZE);
    }
    
    public static Master getMaster() {
        return panel.getMaster();
    }
    
    public static ControlPanel controls(){return panel;}
    public static Visualizer getVisualizer() {return visualizer;}

}
