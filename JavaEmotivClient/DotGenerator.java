import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
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

	private SimpleUniverse universe;

	private DotGenerator() {
		// Create universe
		universe = new SimpleUniverse();
	}

	public static DotGenerator getInstance() {
		if (instance == null) {
			instance = new DotGenerator();
		}
		return instance;
	}

	public void drawDot() {
		BranchGroup scene = new BranchGroup();

		// Create the dot shape (a sphere)
		double dotRadius = 0.02; // Adjust the radius to suit your needs
		int dotDetail = 32; // Number of polygon subdivisions for the sphere
		Sphere dot = new Sphere((float) dotRadius, Sphere.GENERATE_NORMALS, dotDetail);

		//get PADVector from head of list
		PadVector vector = LinkedVectors.popVector();
		double x = vector.pad.get(0);
		double y = vector.pad.get(1);
		double z = vector.pad.get(2);
		Vector3d position = new Vector3d(x,y,z);
		System.out.println(position);
		
		// Set the dot appearance (color) depending on quadrant
		Color3f dotColor;
		if(x>0 && y>0 && z>0) { // (+,+,+) white
			dotColor = new Color3f(1.0f, 1.0f, 1.0f);
		} else if (x>0 && y>0 && z<0) { // (+,+,-) bright magenta
			dotColor = new Color3f(.76f, .0f, .71f);
		} else if (x>0 && y<0 && z>0) { // (+,-,+) light pink
			dotColor = new Color3f(.93f, .78f, .93f);
		} else if (x>0 && y<0 && z<0) { // (+,-,-)  dark blue
			dotColor = new Color3f(.0f, .05f, .64f);
		} else if (x<0 && y>0 && z>0) { // (-,+,+) light blue
			dotColor = new Color3f(.51f, .81f, .99f);
		} else if (x<0 && y>0 && z<0) { // (-,+,-) dark green
			dotColor = new Color3f(.17f, .45f, .13f);
		} else if (x<0 && y<0 && z>0) { // (-,-,+) light green
			dotColor = new Color3f(.59f, 1.0f, .67f);
		} else { // (-,-,-) dark orange
			dotColor = new Color3f(.47f, .23f, .11f);
		}
		ColoringAttributes dotColorAttr = new ColoringAttributes(dotColor, ColoringAttributes.SHADE_GOURAUD);
		Appearance dotAppearance = new Appearance();
		dotAppearance.setColoringAttributes(dotColorAttr);

		// Set transparency *not working
		Alpha alpha = new Alpha(1, Alpha.DECREASING_ENABLE, (long) 0, (long) 0, (long) 10, (long) 0, (long) 10,
				(long) 10, (long) 0, (long) 10);
		TransparencyAttributes dotTrans = new TransparencyAttributes();
		TransparencyInterpolator interpolator = new TransparencyInterpolator(alpha, dotTrans);
		interpolator.setSchedulingBounds(new BoundingSphere());
		dotAppearance.setTransparencyAttributes(dotTrans);

		dot.setAppearance(dotAppearance);

		// Create a TransformGroup for the Sphere
		Transform3D dotTransform = new Transform3D();
		TransformGroup dotTransformGroup = new TransformGroup();
		//dotTransform.setTranslation(position);
		double angle = -(Math.PI/6); // Angle in radians
		Vector3d axis = new Vector3d(0, 1, 0); // Rotation axis (here, y-axis)
		dotTransform.setRotation(new AxisAngle4d(axis, angle));
		Vector3d cameraPosition = new Vector3d(0, -0.075, 0);
		dotTransform.setTranslation(new Vector3d(position.x, position.y + cameraPosition.y, position.z));

		dotTransformGroup.setTransform(dotTransform);
		dotTransformGroup.addChild(dot);

		scene.addChild(dotTransformGroup);
		universe.getViewingPlatform().setNominalViewingTransform();
		universe.addBranchGraph(scene);
	}

	public void drawAxis() {
		BranchGroup scene = new BranchGroup();

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
		double angle = -(Math.PI/6); // Angle in radians
		Vector3d axis = new Vector3d(0, 1, 0); // Rotation axis (here, y-axis)
		axisTransform.setRotation(new AxisAngle4d(axis, angle));
		Vector3d cameraPosition = new Vector3d(0, -0.075, 0);
		axisTransform.setTranslation(cameraPosition);

		axisTransformGroup.setTransform(axisTransform);
		axisTransformGroup.addChild(axisShape);

		// Add the axis shape to the scene
		scene.addChild(axisTransformGroup);

		universe.getViewingPlatform().setNominalViewingTransform();
		universe.addBranchGraph(scene);
	}

	@Override
	public void run() {
		drawAxis();
		while (true) {
			
			// Set the dot's position with stack values
			for (int i = 0; i < LinkedVectors.size(); i++) {
				drawDot();
			}

			// Sleep for the specified delay before generating the next dot
			try {
				Thread.sleep(DELAY_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
//	public static void main(String[] args) {
//
//		System.setProperty("sun.awt.noerasebackground", "true");
//
//		ArrayList<String> places = new ArrayList<>(Arrays.asList("1684630130.87430000", "true", "0.54134", "true", "0.470717", "0.0", "true", "0.455599", "true", "0.495421", "true", "0.416905", "true", "0.393129"));
//		ArrayList<String> places2 = new ArrayList<>(Arrays.asList("168123430.87430000", "true", "0.2", "true", "0.2", "0.0", "true", "0.2", "true", "0.1", "true", "0.1", "true", "0.1"));
//		PadVector pad = new PadVector(places);
//		PadVector pad2 = new PadVector(places2);
//		LinkedVectors instance = new LinkedVectors();
//		LinkedVectors.pushVector(pad);
//		LinkedVectors.pushVector(pad2);
//		DotGenerator dotGenerator = DotGenerator.getInstance();
//
//		Thread generatorThread = new Thread(dotGenerator);
//		generatorThread.start();
//	}
}
