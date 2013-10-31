/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ruang2D;

import com.sun.opengl.util.GLUT;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author PANDU
 */
public class Penggambar {
    
    public MatriksKecepatan MK;
    public MatriksKecepatan OMK;
    private Point O;
    private LinkedList<Raypath> garis;
     
    public Penggambar () {
        MatriksKecepatan MK = new MatriksKecepatan();
        O = new Point(0,0);
    }
    public Penggambar (MatriksKecepatan _mk) {
        MK = new MatriksKecepatan();
        MK = _mk;
        MK.setMaxMinVal();
        O = new Point(0,0);
    }
    
    public void seto (int x, int y) {O.setLocation(x, y);}
    public int getox () {return (int)O.getX();}
    public int getoy () {return (int)O.getY();}
    public void gesero (int x, int y) {O.setLocation(x+getox(), y+getoy());}
    
    public int getnx() {
        return MK.getP();
    }
    public int getny() {
        return MK.getL();
    }
    public void gambar_garis(GL gl, Point P, Point Q, Color C) {
        gl.glBegin (GL.GL_LINES);
            gl.glColor3f(C.getRed(), C.getGreen(), C.getBlue());
            gl.glVertex3f( (float)P.getX(), (float)P.getY(), 0);
            gl.glVertex3f( (float)Q.getX(), (float)Q.getY(), 0);
        gl.glEnd();
    }
    private void gambar_kotakborder(GL gl, Point P, Color C, int lebarkotak) {
        gl.glBegin (GL.GL_LINES);
            Point Q = new Point(P); // kanan bawah
            Point R = new Point(P); // kanan atas
            Point S = new Point(P); // kiri atas
            Q.translate(0, lebarkotak);
            R.translate(lebarkotak, lebarkotak);
            S.translate(lebarkotak, 0);
            gambar_garis(gl,P,Q,C);
            gambar_garis(gl,Q,R,C);
            gambar_garis(gl,R,S,C);
            gambar_garis(gl,S,P,C);
        gl.glEnd();
    }
    private void gambar_kotakwarna(GL gl, Point P) {
         gl.glBegin(GL.GL_QUADS);
            Point Q = new Point(P); // kanan bawah
            Point R = new Point(P); // kanan atas
            Point S = new Point(P); // kiri atas
            Q.translate(0, 1);
            R.translate(1, 1);
            S.translate(1, 0);
            gl.glColor3f(0f, 1f, 1.0f);                             // blue
            gl.glVertex3f((float)P.getX(), (float)P.getY(), 0.0f);  // kiri bawah
            gl.glColor3f(1f, 1f, 0.0f);                             // green
            gl.glVertex3f((float)Q.getX(), (float)Q.getY(), 0.0f);  // kanan bawah
            gl.glColor3f(1.0f, 0f, 0.0f);                           // red
            gl.glVertex3f((float)R.getX(), (float)R.getY(), 0.0f);  // kanan atas
            gl.glColor3f(0.5f, 0.5f, 1.0f);                         // light blue
            gl.glVertex3f((float)S.getX(), (float)S.getY(), 0.0f);  // kiri atas
        gl.glEnd();
    }
    private void gambar_kotakrandom(GL gl, Point P) {
         gl.glBegin(GL.GL_QUADS);
            Point Q = new Point(P); // kanan bawah
            Point R = new Point(P); // kanan atas
            Point S = new Point(P); // kiri atas
            Q.translate(0, 1);
            R.translate(1, 1);
            S.translate(1, 0);
            float c1 = (float)Math.random();
            float c2 = (float)Math.random();
            float c3 = (float)Math.random();
            gl.glColor3f(c1, c2, c3);               
            gl.glVertex3f((float)P.getX(), (float)P.getY(), 0.0f);  // kiri bawah
            gl.glVertex3f((float)Q.getX(), (float)Q.getY(), 0.0f);  // kanan bawah
            gl.glVertex3f((float)R.getX(), (float)R.getY(), 0.0f);  // kanan atas
            gl.glVertex3f((float)S.getX(), (float)S.getY(), 0.0f);  // kiri atas
        gl.glEnd();
    }
    private void gambar_kotak(GL gl, Point P, float val) {
        float pv = ((float)val-(float)MK.getminv()+1f)/((float)MK.getmaxv()-(float)MK.getminv()+1f);
        Color c = new Color ((int)((float)pv*(float)255));
//         cr = c.getRed()/(float)255;
//         cg = c.getGreen()/(float)255;
//         cb = c.getBlue()/(float)255;
        float cr = (float)1;
        float cg = (float)1;
        float cb = (float)1;        
        float pv6 = pv*(float)6;
        if (pv6 <= 1) { // ungu - biru
            cr = (float)1-pv6;
            cg = (float)0;
            cb = (float)1;            
        } else
        if (pv6 <= 2) { // biru - biru m.
            cr = (float)0;
            cg = pv6-(float)1;
            cb = (float)1;            
        } else
        if (pv6 <= 3) { // biru m. - hijau
            cr = (float)0;
            cg = (float)1;
            cb = (float)3-pv6;
        } else
        if (pv6 <= 4) { // hijau - kuning
            cr = pv6-(float)3;
            cg = (float)1;
            cb = (float)0;
        } else
        if (pv6 <= 5) { // kuning - jingga
            cr = (float)1;
            cg = ((float)5-pv6)/((float)2) + (float)0.5;
            cb = (float)0;
        } else
        if (pv6 <= 6) { // jingga - merah
            cr = (float)1;
            cg = ((float)6-pv6)/((float)2);
            cb = (float)0;
        }        
//        int RGB = Color.HSBtoRGB(1-pv, 1, 1);
//        cr = ((RGB >> 16) & 0xFF)/255.0f;
//        cg = ((RGB >> 8)& 0xFF) / 255.0f;
//        cb = ((RGB) & 0xFF) / 255.0f;
        gl.glBegin(GL.GL_QUADS);
            Point Q = new Point(P); // kanan bawah
            Point R = new Point(P); // kanan atas
            Point S = new Point(P); // kiri atas
            Q.translate(0, 1);
            R.translate(1, 1);
            S.translate(1, 0);
            gl.glColor3f(cr, cg, cb);      
            gl.glVertex3f((float)P.getX(), (float)P.getY(), 0.0f);  // kiri bawah
            gl.glVertex3f((float)Q.getX(), (float)Q.getY(), 0.0f);  // kanan bawah
            gl.glVertex3f((float)R.getX(), (float)R.getY(), 0.0f);  // kanan atas
            gl.glVertex3f((float)S.getX(), (float)S.getY(), 0.0f);  // kiri atas
        gl.glEnd();
    }
    public void gambar_mk_kotakwarna(GL gl) {
        for (int i= getox(); i<MK.getP()+getox(); i++) {    
            for (int j= getoy(); j<MK.getL()+getoy(); j++) {
                Point T = new Point(i,j);
                gambar_kotak(gl, T, MK.getFloatData(j-getoy(),i-getox()));
            }
        }
    }
    public void gambar_mk_border(GL gl, int lebarkotak) {
        Color CBorder = new Color((float)0, (float)0, (float)0);
        for (int i= getox(); i<(MK.getP()+getox())/lebarkotak; i++) {    
            for (int j= getoy(); j<(MK.getL()+getoy())/lebarkotak; j++) {
                Point T = new Point(i*lebarkotak, j*lebarkotak);
                gambar_kotakborder(gl, T, CBorder, lebarkotak);
            }
        }
    }
    public void gambar_mk_kotakrandom(GL gl) {
        Color CBorder = new Color((float)0, (float)0, (float)0);
        for (int i= getox(); i<MK.getP()+getox(); i++) {    
            for (int j= getoy(); j<MK.getL()+getoy(); j++) {
                Point T = new Point(i,j);
                gambar_kotakrandom(gl, T);
            }
        }
    }
    public void gambar_axor(GL gl, GLUT glut, Camera K) {
        Point P = new Point(0,150);
        Point Q = new Point(150,0);
        Point R = new Point(0,-30);
        Point S = new Point(-30,0);
        Color CBorder = new Color((float)0, (float)0, (float)0);
        gambar_garis(gl,P,R,CBorder);
        gambar_garis(gl,Q,S,CBorder);
        
        // --- angka ---
        int iii = 0;
        int mx = (int)(-5.0*(K.getJarak()/150.0));
        int my = (int)(-5.0*(K.getJarak()/150.0));
        gambarTeks (gl, glut, Integer.toString(iii), Color.black, new Point(mx,my));
        for (iii = 1; iii<=15; iii++) {
            mx = (int)(-5.0*(K.getJarak()/150.0));
            my = (int)(-5.0*(K.getJarak()/150.0));
            if (iii>9) mx = (int)(-6.0*(K.getJarak()/150.0));
            gambarTeks (gl, glut, Integer.toString(iii), Color.black, new Point(mx, iii*8));
            gambarTeks (gl, glut, Integer.toString(iii), Color.black, new Point(iii*8, my));
        }
    }
    
    public void gambarTeks( GL gl, GLUT glut, String teks, Color C, Point P) {
        gl.glColor3f(C.getRed(), C.getGreen(), C.getBlue());
        gl.glRasterPos3d(P.getX(), P.getY(), 0);
        glut.glutBitmapString(glut.BITMAP_HELVETICA_12, teks );
    }
    
    public void test_pelangi () {
        for (int i=0;i<MK.getP();i++){
            for(int j=0;j<MK.getL();j++){
                MK.data[i][j]=Integer.toString(i+j);
            }
        }
        MK.setMaxMinVal();
    }
    
    // ------ fresnel volume
    
    public void tes() throws IOException {

        FresnelVolume F = new FresnelVolume(MK);
        OMK = F.MK;
        MK = F.RataMK;
//        garis = F.garis;
                //Raypath(F.getReceiver(), new Point(40,40), F.getSource());
        
    }
    public void gambar_ray(GL gl) {
        for (int i=0; i<garis.size(); i++) {
            Raypath temp = garis.get(i);
            gambar_garis(gl,temp.getA(),temp.getB(), Color.BLACK);
            gambar_garis(gl,temp.getB(),temp.getC(), Color.BLACK);
        }
    }
    
    // -------------------
}
