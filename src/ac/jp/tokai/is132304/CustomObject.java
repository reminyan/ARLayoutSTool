package ac.jp.tokai.is132304;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.util.GraphicsUtil;

/**
 * An example of an AR object being drawn on a marker.
 * 
 * @author tobi
 * 
 */
public class CustomObject extends ARObject {
	float fSize[] = { 1.0f, 1.0f, 1.0f };
	float CustomColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	float fPosBase[] = { 1.0f, 1.0f };
	double screenSize[] = { 1, 1 };
	String objName;

	public CustomObject(String name, String patternName, double markerWidth,
			double[] markerCenter) {
		super(name, patternName, markerWidth, markerCenter);
		objName = name;
		CustomColor[0] = 0.0f; // R
		CustomColor[1] = 1.0f; // G
		CustomColor[2] = 0.0f; // B
		CustomColor[3] = 1.0f; // A

	}

	public CustomObject(String name, String patternName, double markerWidth,
			double[] markerCenter, float[] customColor) {
		super(name, patternName, markerWidth, markerCenter);
		objName = name;
		CustomColor = customColor; // R

	}

	public CustomObject(String name, String patternName, double markerWidth,
			double[] markerCenter, float[] customColor, float aSize[]) {
		super(name, patternName, markerWidth, markerCenter);
		objName = name;
		CustomColor = customColor; // R
		fSize = aSize;
	}

	public CustomObject(String name, String patternName, double markerWidth,
			double[] markerCenter, float[] customColor, float aSize[],
			double screenRatio[]) {
		super(name, patternName, markerWidth, markerCenter);
		objName = name;
		CustomColor = customColor; // R
		fSize = aSize;
		screenSize = screenRatio;
	}

	/**
	 * Just a box, imported from the AndAR project.
	 */
	// private SimpleBox box = new SimpleBox();
	private WireFrame box;

	/**
	 * Everything drawn here will be drawn directly onto the marker, as the
	 * corresponding translation matrix will already be applied.
	 */
	@Override
	public final void draw(GL10 gl) {
		super.draw(gl);
		// draw the box
		if (super.getId() == getObjID()) {

			System.out.println("touched:ObjNAME=" + getName()
					+ "/ObjID(from Touch)" + getObjID());
			SQLiteHelper sqlHelper;
			if (super.getId() == super.getObjSizeID()) {
				double[] objSize = super.getObjSize();
				box.setSizeX((float) objSize[0]);// d
				box.setSizeY((float) objSize[1]);// w
				box.setSizeZ((float) objSize[2]);// h
			}
		}
		box.draw(gl);
	}

	@Override
	public void init(GL10 gl) {
		box = new WireFrame(fSize, CustomColor);
	}
}

// �ｽ�ｽ�ｽC�ｽ�ｽ�ｽ[�ｽt�ｽ�ｽ�ｽ[�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ驍ｽ�ｽﾟの材暦ｿｽ
class WireFrame {
	private FloatBuffer buffer;
	private float x, y, z;
	private FloatBuffer mat_flash;
	private FloatBuffer mat_ambient;
	private FloatBuffer mat_flash_shiny;
	private FloatBuffer mat_diffuse;
	private float mat_ambientf[] = new float[4];
	private float mat_flashf[] = new float[4];
	private float mat_diffusef[] = new float[4];
	private float mat_flash_shinyf[] = new float[1];
	private final float vertex[] = {
			// �ｽV�ｽ�ｽ
			-10.0f, -10.0f, 20.0f, 10.0f, -10.0f, 20.0f, 10.0f, 10.0f, 20.0f,
			-10.00f,
			10.0f,
			20.0f,
			// �ｽ�ｽ
			-10.00f, -10.0f, 0.0f, 10.0f, -10.0f, 0.0f, 10.0f, 10.0f, 0.0f,
			-10.00f, 10.0f,
			0.0f,
			// �ｽO
			-10.0f, 10.0f, 20.0f, 10.0f, 10.0f, 20.0f, 10.0f, 10.0f, 0.0f,
			-10.0f, 10.0f, 0.0f,
			// �ｽ�ｽ�ｽ�ｽ
			-10.0f, -10.0f, 0.0f, 10.0f, -10.0f, 0.0f, 10.0f, -10.0f, 20.0f,
			-10.0f, -10.0f, 20.0f, };

	public WireFrame(float fSize[], float Color[]) {
		this.setSizeX(fSize[0]);
		this.setSizeY(fSize[1]);
		this.setSizeZ(fSize[2]);

		mat_ambientf = Color;
		mat_flashf = Color;
		mat_diffusef = Color;
		mat_flash_shinyf[0] = 128.0f;

		ByteBuffer vb = ByteBuffer.allocateDirect(vertex.length * 4);
		vb.order(ByteOrder.nativeOrder());
		buffer = vb.asFloatBuffer();
		buffer.put(vertex);
		buffer.position(0);
	}

	public void draw(GL10 gl) {

		mat_ambient = GraphicsUtil.makeFloatBuffer(mat_ambientf);
		mat_flash = GraphicsUtil.makeFloatBuffer(mat_flashf);
		mat_flash_shiny = GraphicsUtil.makeFloatBuffer(mat_flash_shinyf);
		mat_diffuse = GraphicsUtil.makeFloatBuffer(mat_diffusef);

		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, mat_flash);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS,
				mat_flash_shiny);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffuse);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambient);

		gl.glPushMatrix();
		// gl.glTranslatef(1.0f, 1.0f, 5.0f);
		gl.glScalef(getSizeX(), y, z);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, buffer);
		gl.glLineWidth(4.0f);
		// Front
		gl.glNormal3f(0, 0, 1.0f);
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);

		// Back
		gl.glNormal3f(0, 0, -1.0f);
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 4, 4);

		// Left
		gl.glNormal3f(-1.0f, 0, 0);
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 8, 4);
		// right
		gl.glNormal3f(-1.0f, 0, 0);
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 12, 4);
		gl.glPopMatrix();
	}

	/**
	 * @return the x
	 */
	public float getSizeX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setSizeX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getSizeY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setSizeY(float y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public float getSizeZ() {
		return z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setSizeZ(float z) {
		this.z = z;
	}
}
