/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ruang2D;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author PANDU
 */
public class Camera {
    
    int width;
    int height;
    float camx;
    float camy;
    float jarak;
        
    public Camera () {
        width = 800;
        height = 600;
        camx = 0;
        camy = 0;
    }
    public Camera (int _lx, int _ly, float _cx, float _cy, float _jarak) {
        width = _lx;
        height = _ly;
        camx = _cx;
        camy = _cy;
        jarak = _jarak;
    }
    
    public float getJarak () {
        return jarak;
    }
    
    public void refreshWidthHeight(int x, int y) {
        width = x;
        height = y;
    }
    public void initCamera(GL gl, GLU glu) {
        // Change to projection matrix.
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        // Perspective.
        float widthHeightRatio = (float) width / (float) height;
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        glu.gluLookAt(camx, camy, jarak, camx, camy, 0, 0, 1, 0);
        // Change back to model view matrix.
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    public void zoom(GL gl, GLU glu, int x) {
        jarak += x;
    }
    public void geser(GL gl, GLU glu, float x, float y) {
        camx += x;
        camy += y;
    }
}
