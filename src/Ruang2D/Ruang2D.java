/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ruang2D;

import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.GLUT;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JPanel;
import java.awt.event.*;
import java.io.IOException;

/**
 *
 * @author PANDU
 */
public class Ruang2D  extends JPanel implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener {

    public GLCanvas canvas;
    public float jarak;
    public GL gl;
    public GLU glu = new GLU();
    public GLUT glut = new GLUT();
    public int layarx;
    public int layary;
    public Penggambar penggambar;
    public Camera kamera;
    private final int REFRESH_FPS = 60;  
    public FPSAnimator animator;
    int sbatas;
    
    static float oldx;
    static float oldy;
    static float newx;
    static float newy;
    
    public static int count = 0;

    public Ruang2D() {
        canvas = new GLCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
        canvas.addGLEventListener(this);
        // Run the animation loop using the fixed-rate Frame-per-second animator, which calls back display() at this fixed-rate (FPS).
        animator = new FPSAnimator(canvas, REFRESH_FPS, true);
        
        jarak = 150;
        layarx = 600;
        layary = 480;
        sbatas = 0;
        ReaderExcel Reader = new ReaderExcel();
        Reader.setInputFile("data/model.xls");
        MatriksKecepatan MK = new MatriksKecepatan(96,96);
//        try {
//            MK = Reader.readtomv();
//        } catch (IOException ex) {}
        penggambar = new Penggambar(MK);
        kamera = new Camera(layarx, layary, penggambar.getnx()/2, penggambar.getny()/2, jarak);
    }
    public Ruang2D(String File, int _layarx, int _layary) {
        canvas = new GLCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
        canvas.addGLEventListener(this);
        // Run the animation loop using the fixed-rate Frame-per-second animator, which calls back display() at this fixed-rate (FPS).
        animator = new FPSAnimator(canvas, REFRESH_FPS, true);
        
        jarak = 150;
        layarx = _layarx;
        layary = _layary;

        ReaderExcel Reader = new ReaderExcel();
        Reader.setInputFile(File);
        MatriksKecepatan MK = new MatriksKecepatan(1,1);
        try {
            MK = Reader.readtomv();
        } catch (IOException ex) {}
        penggambar = new Penggambar(MK);
        System.out.println(MK.getL()+" asdasdaasdasdaasdasdaasdasdaasdasdaasdasdaasdasdaasdasdaasdasdaasdasdaasdasdaasdasdaasdasdaasdasdaasdasda ");
        kamera = new Camera(layarx, layary, penggambar.getnx()/2, penggambar.getny()/2, jarak);
    }
    public Ruang2D(int _layarx, int _layary) {
        canvas = new GLCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
        canvas.addGLEventListener(this);
        // Run the animation loop using the fixed-rate Frame-per-second animator, which calls back display() at this fixed-rate (FPS).
        animator = new FPSAnimator(canvas, REFRESH_FPS, true);
        
        jarak = 150;
        layarx = _layarx;
        layary = _layary;

        MatriksKecepatan MK = new MatriksKecepatan(100,100);
        penggambar = new Penggambar(MK);
        kamera = new Camera(layarx, layary, penggambar.getnx()/2, penggambar.getny()/2, jarak);
    }
    
    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline: drawable.setGL(new DebugGL(drawable.getGL()));
        gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        // Setup the drawing area and shading mode
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        // Setup the depth buffer and enable the depth testing
        gl.glClearDepth(1.0f);          // clear z-buffer to the farthest
        gl.glEnable(GL.GL_DEPTH_TEST);  // enables depth testing
        gl.glDepthFunc(GL.GL_LEQUAL);   // the type of depth test to do
        // Do the best perspective correction
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        drawable.addMouseListener(this);
        drawable.addMouseMotionListener(this);
        drawable.addMouseWheelListener(this);
        penggambar.tes();
        
    }
    public void display(GLAutoDrawable drawable) {
        gl = drawable.getGL();
        glu = new GLU();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // Clear the drawing area
        gl.glLoadIdentity(); // Reset the current matrix to the "identity"
        // --------------------------------------------------------\
        // -- Penting! untuk kamera
        kamera.refreshWidthHeight(getWidth(), getHeight());
        kamera.initCamera(gl, glu);
//        penggambar.test_pelangi();
        penggambar.gambar_mk_kotakwarna(gl);
        
        // --------------------------------------------------------
        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }   
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        gl = drawable.getGL();
        height = (height == 0) ? 1 : height; // prevent divide by zero
        float aspect = (float)width / height;
        // Set the current view port to cover full screen
        gl.glViewport(0, 0, width, height);
        // Set up the projection matrix - choose perspective view
        gl.glMatrixMode(GL.GL_PROJECTION);  
        gl.glLoadIdentity(); // reset
        // Angle of view (fovy) is 45 degrees (in the up y-direction). Based on this
        // canvas's aspect ratio. Clipping z-near is 0.1f and z-near is 100.0f.
        glu.gluPerspective(45.0f, aspect, 0.1f, 100.0f); // fovy, aspect, zNear, zFar
        // Enable the model-view transform
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int n = e.getWheelRotation();
//        if(n>0)
//            sbatas +=(int)(penggambar.OMK.getmaxv()/1000);
//        MatriksKecepatan MK2 = penggambar.MK;
//        for (int k=0;k<MK2.getP();k++){
//            for(int l=0;l<MK2.getL();l++){
//                MK2.data[l][k] = (Float.parseFloat(MK2.data[l][k])> MK2.getmaxv()-sbatas)? Float.toString(MK2.getmaxv()-sbatas) : MK2.data[l][k]; 
//            }
//        }
//        penggambar.MK = (sbatas<penggambar.OMK.getmaxv()) ? MK2 : penggambar.OMK;
//        penggambar.MK.setMaxMinVal();
//        System.out.println(sbatas+" "+penggambar.OMK.getmaxv()+" "+penggambar.MK.getmaxv());
        if(n>0)
            kamera.zoom(gl, glu, 5);
        else
            kamera.zoom(gl, glu, -5);
        //System.out.println(kamera.getJarak());
    }
    public void mousePressed(MouseEvent e) {
        newx=e.getXOnScreen();
        newy=e.getYOnScreen();
        oldx=newx;
        oldy=newy;
    }
    public void mouseDragged(MouseEvent e) {
        newx=e.getXOnScreen();
        newy=e.getYOnScreen();
        kamera.geser(gl, glu, (-newx+oldx), (newy-oldy));        
        oldx=newx;
        oldy=newy;
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {} 
}
