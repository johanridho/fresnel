/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ruang2D;

import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author PHKI-01
 */
public class FresnelVolume {
    
    /**
     * 
     */
    public MatriksKecepatan MK;
    public MatriksKecepatan FMK;
    public LinkedList<Raypath> garis;
    

    public Point getReceiver() {
        return Receiver;
    }
    public Point getSource() {
        return Source;
    }

    Point Source;   // titik posisi source
    Point Receiver; // titik posisi receiver
    float tobs;     // jarak source-receiver

    float[] arraySP;    // jarak source ke P
    float[] arrayRP;    // jarak receiver ke P
    float[] arrayTCal;  // tcal = tsp + trp
    float[] deltaT;     // dT = tobs - tcal
    float[][] bobotij;    // wij
    float[] slowness;   // Si
    float[] slownessAkhir;   // Sik+1
    float[] slownessAkhirRata;   // Sik+1
    float[] kecepatanModel;   // Sik+1
    float[] kecepatanModelRata;   // Sik+1
    
    
    float[] deltaSi;    
    float[] jmlRayPerSel;     // N
    float[] bobotRata;  // Bobot rata-rata per sel
    float[] sigma;
    float[] deltaSiRata;
    
    float velo;          // velocity
    float f;             // frekuensi
    float lSR;
    
    /**
     * 
     * @param _MK
     */
    public FresnelVolume (MatriksKecepatan _MK) {
        // TO DO : bikin MK, Source, Receiver supaya jadi array
        MK = _MK;
        FMK = _MK;
        
        Point p = new Point(MK.getP()/2, MK.getL()/2);
        int r = MK.getP()/2;
        double sdt =  Math.toRadians(90.0);
        int dx = (int)((float)r*Math.cos(sdt));
        int dy = (int)((float)r*Math.sin(sdt));
        Source = new Point(p.x - dx, p.y - dy);
        Receiver = new Point(p.x + dx, p.y + dy);
        System.out.println(Source.x+" "+Source.y);
        System.out.println(Receiver.x+" "+Receiver.y);
                
        arraySP = new float[MK.getL()*MK.getP()];
        arrayRP = new float[MK.getL()*MK.getP()];
        arrayTCal = new float[MK.getL()*MK.getP()];
        deltaT = new float[MK.getL()*MK.getP()];
        deltaSi = new float[MK.getL()*MK.getP()];
        sigma = new float[MK.getL()*MK.getP()];
        deltaSiRata = new float[MK.getL()*MK.getP()];
        slowness = new float[MK.getL()*MK.getP()];
        slownessAkhir = new float[MK.getL()*MK.getP()];
        slownessAkhirRata = new float[MK.getL()*MK.getP()];
        kecepatanModel = new float[MK.getL()*MK.getP()];
        kecepatanModelRata = new float[MK.getL()*MK.getP()];
        
        bobotij = new float[MK.getL()*MK.getP()][];
                
        velo = 1000;      
        f = 500;
        makeEllipse ();
        
    }
    
    private void makeEllipse () {
        
        garis = new LinkedList();
        
        // Loop 1 : Hitung distance dan time
        for (int i=0;i<MK.getP();i++){
            for(int j=0;j<MK.getL();j++){
                int idx = i*MK.getL()+j;
                float velo_now = Float.valueOf(MK.data[j][i]);
                if (isDalamEllips(i,j,Source,Receiver)) {
                    // kalo dalam ellips, hitung
                    arraySP[idx] = distance(Source,new Point(i,j))/velo_now;
                    arrayRP[idx] = distance(Receiver,new Point(i,j))/velo_now;
                    arrayTCal[idx] = hitungArrayTCal(arraySP[idx], arrayRP[idx]);     
                    tobs = distance(Source,Receiver)/velo_now;           
                    deltaT[idx] = hitungDeltaT(arrayTCal[idx], tobs);
                } else {
                    // kalo ga, 0
                    arraySP[idx] = 0/velo_now;
                    arrayRP[idx] = 0/velo_now;   
                    arrayTCal[idx] = 0;                
                    deltaT[idx] = 0; 
                }
                slowness[idx] = 1f/velo_now;
            }
        } // --- END 1 ---
        
        
        // Loop 2
        for (int i=0;i<MK.getP();i++){
            for(int j=0;j<MK.getL();j++){
                
                int jumRayPerSel = 0;
                int idx = i*MK.getL()+j;
                bobotij[idx] = new float[MK.getL()*MK.getP()];
                Point Persegi = new Point(i,j);
                garis.add(new Raypath(Source,Persegi,Receiver));
                
                float totalw = 0f;
                // Loop 2.1 : Cari jumRayPerSel, bobot, totalw
                for (int k=0;k<MK.getP();k++){
                    for(int l=0;l<MK.getL();l++){
                        int jdx = k*MK.getL()+l;
                        Point P = new Point(k,l);
                        if (isDalamEllips(i,j,Source,Receiver)) {
                            if (IsKotakIntersect(Source,P,Persegi)||IsKotakIntersect(Receiver,P,Persegi)) {
                                jumRayPerSel++;
                            }
                            bobotij[idx][jdx] = (hitungBobotij(deltaT[jdx]));
                        } else {
                            bobotij[idx][jdx] = 0;
                        }
                        totalw +=  bobotij[idx][jdx];
                    }
                } // --- END 2.1 ---
                
               // Loop 2.2 : Cari sigma
                float sik = 1/velo;
                for (int k=0;k<MK.getP();k++){
                    for(int l=0;l<MK.getL();l++){
                        int jdx = k*MK.getL()+l;
                        Point P = new Point(k,l);
                        
                        if (isDalamEllips(i,j,Source,Receiver) && IsKotakIntersect(Source,P,Persegi)||IsKotakIntersect(Receiver,P,Persegi)) {
                            sigma[jdx] = deltaT[jdx]/arrayTCal[jdx]*bobotij[idx][jdx]/totalw;
                            if (Double.isNaN(sigma[jdx])) {
                                sigma[jdx] = 0;
                            }
                        } else {
                            sigma[jdx] = 0;
                        }
                            
                    }
                } // --- END 2.2 ---
                
                float totalsigma=0f;
                for (int k=0;k<MK.getP();k++){
                    for(int l=0;l<MK.getL();l++){
                        int jdx = k*MK.getL()+l;
                        if (isDalamEllips(i,j,Source,Receiver))
                            totalsigma += sigma[jdx];
                        else
                            totalsigma +=0;
                    }
                } // --- END 2.2 ---
                
                if (isDalamEllips(i,j,Source,Receiver)) {
                    deltaSi[idx] = totalsigma*slowness[idx];
//                    deltaSi[idx] = (deltaSi[idx]>600)? 600 : deltaSi[idx];
                    deltaSiRata[idx] = deltaSi[idx]/jumRayPerSel;
                    slownessAkhir[idx] = slowness[idx] + deltaSi[idx];
                    slownessAkhirRata[idx] = slowness[idx] + deltaSiRata[idx];
                    kecepatanModel[idx] = 1/slownessAkhir[idx]; 
                    kecepatanModelRata[idx] = 1/slownessAkhirRata[idx];
                } else {
                    deltaSi[idx] = 0;
                    deltaSiRata[idx] = 0;
                    slownessAkhir[idx] = 0;
                    slownessAkhirRata[idx] = 0;
                    kecepatanModel[idx] = 0;
                    kecepatanModelRata[idx] = 0;
                }
                FMK.data[j][i]=Float.toString((float)kecepatanModelRata[idx]);
            }
        } // --- END 2 ---
        FMK.setMaxMinVal();
        
        for (int i=0;i<FMK.getP();i++){
            for(int j=0;j<FMK.getL();j++){
                int idx = i*FMK.getL()+j;
                System.out.print( FMK.data[i][j]+" ");
            }
            System.out.println("");
        } // --- END 1 ---
        System.out.println("Nilai Min : "+FMK.getminv());
        System.out.println("Nilai Max : "+FMK.getmaxv());
        System.out.println("P : "+FMK.getP());
        System.out.println("L : "+FMK.getL());
    }
    
    private float hitungArrayTCal (float SP, float RP) {
        return SP + RP;
    }
    private float hitungDeltaT (float TCal, float tobs) {
        return Math.abs(tobs - TCal);
    }
    private float hitungBobotij (float deltaT) {
        if (deltaT >= (f/2))
            return 0;
        else
            return 1-2*f*deltaT;
    }
    
    private boolean isDalamEllips (int i, int j, Point P, Point Q) {
        Point pusat = new Point(Math.abs(P.y+Q.y)/2, Math.abs(P.x+Q.x)/2);
        float tinggiEllips = 40f;
        float PQ = distance(P,Q);
        float a = PQ/2;
        float b = tinggiEllips/2;
        float sina = (Q.y-P.y) / PQ;
        float cosa = (Q.x-P.x) / PQ;
        float kiri = (j-pusat.x)*cosa + (i-pusat.y)*sina;
        float kanan = (j-pusat.x)*sina - (i-pusat.y)*cosa;
//        System.out.println(pusat.x+" "+pusat.y);
//        float kiri = (j-pusat.x);
//        float kanan = (i-pusat.y);
        return Math.pow((kiri/b),2) + Math.pow((kanan/a),2)<1;
    }
    private float distance(Point P, Point Q) {
        return  (float)Math.sqrt((P.x-Q.x)*(P.x-Q.x) + (P.y-Q.y)*(P.y-Q.y));
    }
    private boolean IsIntersecting(Point a, Point b, Point c, Point d) {
        
        float denominator = ((b.x - a.x) * (d.y - c.y)) - ((b.y - a.y) * (d.x - c.x));
        float numerator1 = ((a.y - c.y) * (d.x - c.x)) - ((a.x - c.x) * (d.y - c.y));
        float numerator2 = ((a.y - c.y) * (b.x - a.x)) - ((a.x - c.x) * (b.y - a.y));

        // Detect coincident lines (has a problem, read below)
        if (denominator == 0) return numerator1 == 0 && numerator2 == 0;

        float r = numerator1 / denominator;
        float s = numerator2 / denominator;

        return (r >= 0 && r <= 1) && (s >= 0 && s <= 1);
    }
    private boolean IsKotakIntersect(Point O, Point O2, Point Persegi) {

        Point P = new Point(Persegi.x,Persegi.y);
        Point P2 = new Point(Persegi.x,Persegi.y+1);
        Point P3 = new Point(Persegi.x+1,Persegi.y);
        Point P4 = new Point(Persegi.x+1,Persegi.y+1);
        
        return (IsIntersecting(O,O2,P,P2)
                ||IsIntersecting(O,O2,P2,P3)
                ||IsIntersecting(O,O2,P3,P4)
                ||IsIntersecting(O,O2,P4,P));
    }
    public void hitungLSR(){
//        lSR = 1/velo;
    }
    
     public void hitungDeltaSi(){
//        bobotRata[]
//        
//        for int(i=1;i<MK.getL()*MK.getP();i++){
//            deltaSi[i]= (1/jmlRayPerSel[i-1]) + jmlRayPerSel[i-1] * (deltaT[i-1]/arrayTCal) * (bobotRata[i-1]/bobotij[i-1]);
//        }//end for
    }
    
    public void hitungBobotRata(){
        
    }
    
    
}
