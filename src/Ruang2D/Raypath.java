/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ruang2D;

import java.awt.Point;

/**
 *
 * @author PHKI-01
 */
public class Raypath {
    
    private Point A;
    private Point B;
    private Point C;

    public Raypath(Point A, Point B, Point C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }

    public void setA(Point A) {
        this.A = A;
    }
    public void setB(Point B) {
        this.B = B;
    }
    public void setC(Point C) {
        this.C = C;
    }
    public Point getA() {
        return A;
    }
    public Point getB() {
        return B;
    }
    public Point getC() {
        return C;
    }
    
    
    
}
