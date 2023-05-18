import com.sun.j3d.utils.universe.SimpleUniverse;

import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TransparencyInterpolator;

public class DotGenerator implements Runnable {
    private static DotGenerator instance;
    private static final long DELAY_MS = 1000; // Delay between dot generation in milliseconds

    private int dot;
    
    private DotGenerator() {
        dot = 0;
    }
    
    public static DotGenerator getInstance() {
        if (instance == null) {
            instance = new DotGenerator();
        }
        return instance;
    }
    
	public void drawDot(double x, double y, double z)
	
	{
		// Create universe
		SimpleUniverse universe = new SimpleUniverse();
		// Create the branch group
		BranchGroup scene = new BranchGroup();
		
		// Create the dot shape (a sphere)
		double dotRadius = 0.02; // Adjust the radius to suit your needs
		int dotDetail = 32; // Number of polygon subdivisions for the sphere
		Sphere dot = new Sphere((float)dotRadius, Sphere.GENERATE_NORMALS, dotDetail);
	
		// Set the dot appearance (color)
		Color3f dotColor = new Color3f(1.0f, 1.0f, 1.0f);
		ColoringAttributes dotColorAttr = new ColoringAttributes(dotColor, ColoringAttributes.SHADE_GOURAUD);
		Appearance dotAppearance = new Appearance();
		dotAppearance.setColoringAttributes(dotColorAttr);
		
		// Set transparency
		Alpha alpha = new Alpha(1, Alpha.DECREASING_ENABLE, (long)0, (long)0, (long)10, (long)0, (long)10,
				(long)10, (long)0, (long)10);
		TransparencyAttributes dotTrans = new TransparencyAttributes();		
		TransparencyInterpolator interpolator = new TransparencyInterpolator(alpha, dotTrans);
	    interpolator.setSchedulingBounds(new BoundingSphere());
	    dotAppearance.setTransparencyAttributes(dotTrans);
	    
		dot.setAppearance(dotAppearance);
		Vector3d position = new Vector3d(x, y, z);
		
		// Create a TransformGroup for the Sphere
		Transform3D dotTransform = new Transform3D();
		TransformGroup dotTransformGroup = new TransformGroup();
		dotTransform.setTranslation(position);
	
		dotTransformGroup.setTransform(dotTransform);
		dotTransformGroup.addChild(dot);
	
		scene.addChild(dotTransformGroup);
		
		///////////////////////////////////////////////////////////////////////////
		
	    // Create the axis lines
	    LineArray axisLines = new LineArray(6, LineArray.COORDINATES | LineArray.COLOR_3);
	    axisLines.setCoordinate(0, new Point3d(-1.0, 0.0, 0.0)); // X-axis
	    axisLines.setCoordinate(1, new Point3d(1.0, 0.0, 0.0));  
	    axisLines.setCoordinate(2, new Point3d(0.0, -1.0, 0.0)); // Y-axis 
	    axisLines.setCoordinate(3, new Point3d(0.0, 1.0, 0.0)); 
	    axisLines.setCoordinate(4, new Point3d(0.0, 0.0, -1.0)); // Z-axis 
	    axisLines.setCoordinate(5, new Point3d(0.0, 0.0, 1.0)); 
	
	    // Set colors for the axis lines
	    Color3f xAxisColor = new Color3f(1.0f, 0.0f, 0.0f); // Red
	    Color3f yAxisColor = new Color3f(0.0f, 1.0f, 0.0f); // Green
	    Color3f zAxisColor = new Color3f(0.0f, 0.0f, 1.0f); // Blue
	
	    axisLines.setColor(0, xAxisColor);
	    axisLines.setColor(1, xAxisColor);
	    axisLines.setColor(2, yAxisColor);
	    axisLines.setColor(3, yAxisColor);
	    axisLines.setColor(4, zAxisColor);
	    axisLines.setColor(5, zAxisColor);
	
		TransformGroup axisTransformGroup = new TransformGroup();
	    // Create a Shape3D object for the axis lines
	    Shape3D axisShape = new Shape3D(axisLines);
	    
		Transform3D axisTransform = new Transform3D();
		double angle = Math.PI / 3.0; // Angle in radians
	    Vector3d axis = new Vector3d(0, 1, 0); // Rotation axis (here, y-axis)
		axisTransform.setRotation(new AxisAngle4d(axis, angle));
		Vector3d cameraPosition = new Vector3d(0, -0.1, 0);
		axisTransform.setTranslation(cameraPosition);
	
		axisTransformGroup.setTransform(axisTransform);
	    axisTransformGroup.addChild(axisShape);
	    // Add the axis shape to the scene
	    scene.addChild(axisTransformGroup);
	    
	    /////////////////////////////////////////////////////////////////////////////
	    
	   
	    universe.getViewingPlatform().setNominalViewingTransform();
	    universe.addBranchGraph(scene);
	
	}
	
	@Override
	public void run() {
		while (true) {
			// Generate a random position for the dot (replace later with actual stack)
            double x = (Math.random()*2)-1;
            double y = (Math.random()*2)-1;
            double z = (Math.random()*2)-1;

            // Set the dot's position
            drawDot(x, y, z);

            // Sleep for the specified delay before generating the next dot
            try {
                Thread.sleep(DELAY_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
		
	}
	
	
	public static void main( String[] args ) {
	
	   System.setProperty("sun.awt.noerasebackground", "true");
	
       DotGenerator dotGenerator = DotGenerator.getInstance();

	   Thread generatorThread = new Thread(dotGenerator);
       generatorThread.start();
	   
	   //1. string of values printed, must split() the values to get numbers only -6 booleans and 6 numbers
	   //6 numbers in 6 variables
	   //6 numbers into vectors (the 1 excitement number must become 3 numbers, put it 3 times)
	   //create object of vectors that give 3 numbers - they will all be the same numb but either positive or neg
	   //the 6 numbers in the col -> 3 numbers become x y z
	   //2 threads of execution - 1. 2. reading data structure as it is filled, draw on screen, stack
	   		// both threats 
	   // create singleton class with static methods to push and pop
	   // i am making third class implementing runnable interface with drawing method
	
	}
}
