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
    public MatriksKecepatan[] FMK;
    public MatriksKecepatan RataMK;
    public LinkedList<Raypath> garis;
    

//    float tobs;     // jarak source-receiver
     
//    float[] jmlRayPerSel;     // N
//    float[] bobotRata;  // Bobot rata-rata per sel
    
//    float velo;          // velocity
    float f;             // frekuensi
//    float lSR;
    
    Point[] arraySrcRcv = {
        new Point(0, 48), 
        new Point(96, 48), 
        new Point(48, 0),
        new Point(48, 96)
    };
    
    /**
     * 
     * @param _MK
     */
    public FresnelVolume (MatriksKecepatan _MK) {
        // TO DO : bikin MK, Source, Receiver supaya jadi array
        MK = new MatriksKecepatan(_MK);
        FMK = new MatriksKecepatan[6];
        for (int i=0; i<FMK.length; i++) {
//            FMK[i] = new MatriksKecepatan(96,96);
//            FMK[i] = new MatriksKecepatan(_MK);
            FMK[i] = new MatriksKecepatan(_MK.getP(),_MK.getL());
        }
//        FMK[0] = _MK;
        
        Point p = new Point(MK.getP()/2, MK.getL()/2);
        
           
//        velo = 1000;      
//        f = 500;
        
        
        Thread t0 = new Thread(new Runnable() {
            public void run() {
                makeEllipse (0, arraySrcRcv[0], arraySrcRcv[1]);
                makeEllipse (1, arraySrcRcv[0], arraySrcRcv[2]);
                
            }
        });
        
        Thread t3 = new Thread(new Runnable() {
            public void run() {
                makeEllipse (2, arraySrcRcv[0], arraySrcRcv[3]);
                makeEllipse (3, arraySrcRcv[1], arraySrcRcv[2]);                
            }
        });
        
        Thread t4 = new Thread(new Runnable() {
            public void run() {
                makeEllipse (4, arraySrcRcv[1], arraySrcRcv[3]);
                makeEllipse (5, arraySrcRcv[2], arraySrcRcv[3]);
            }
        });
        
        t3.start();
        t4.start();
        t0.start();
        
//        makeEllipse (0, arraySrcRcv[0], arraySrcRcv[1]);
//        makeEllipse (1, arraySrcRcv[0], arraySrcRcv[2]);
//        makeEllipse (2, arraySrcRcv[0], arraySrcRcv[3]);
//        makeEllipse (3, arraySrcRcv[1], arraySrcRcv[2]);
//        makeEllipse (4, arraySrcRcv[1], arraySrcRcv[3]);
//        makeEllipse (5, arraySrcRcv[2], arraySrcRcv[3]);
        
//        makeEllipse (2,new Point(60, 20),new Point(30, 70));
        
        
        while(t3.isAlive() ||t4.isAlive() ||t0.isAlive()){          //loop utk memastikan thread sudah mati smua
            
        }
        
        hitungRataMK();
        
        for (int i=0;i<RataMK.getP();i++){
            for(int j=0;j<RataMK.getL();j++){
                int idx = i*RataMK.getL()+j;
                System.out.print( RataMK.data[i][j]+" ");
            }
            System.out.println("");
        } // --- END 1 ---
        System.out.println("Nilai Min akhir: "+RataMK.getminv());
        System.out.println("Nilai Max akhir: "+RataMK.getmaxv());
        System.out.println("P : "+RataMK.getP());
        System.out.println("L : "+RataMK.getL());
        System.out.println("end...");
        
    }
    
    private void makeEllipse (int iFMK, Point p1, Point p2) {
        
        Point Source = new Point(p1);
        Point Receiver = new Point(p2);
        
//        int r = MK.getP()/2;
//        double sdt =  Math.toRadians(90.0);
//        int dx = (int)((float)r*Math.cos(sdt));
//        int dy = (int)((float)r*Math.sin(sdt));
//        Source = new Point(p.x - dx, p.y - dy);
//        Receiver = new Point(p.x + dx, p.y + dy);
        System.out.println(Source.x+" "+Source.y);
        System.out.println(Receiver.x+" "+Receiver.y);
        
        float[] arraySP = new float[MK.getL()*MK.getP()];
        float[] arrayRP = new float[MK.getL()*MK.getP()];
        float[] arrayTCal = new float[MK.getL()*MK.getP()];
        float[] deltaT = new float[MK.getL()*MK.getP()];
        float[] deltaSi = new float[MK.getL()*MK.getP()];
        float[] sigma = new float[MK.getL()*MK.getP()];
        float[] deltaSiRata = new float[MK.getL()*MK.getP()];
        float[] slowness = new float[MK.getL()*MK.getP()];
        float[] slownessAkhir = new float[MK.getL()*MK.getP()];
        float[] slownessAkhirRata = new float[MK.getL()*MK.getP()];
        float[] kecepatanModel = new float[MK.getL()*MK.getP()];
        float[] kecepatanModelRata = new float[MK.getL()*MK.getP()];
        
        float[][]bobotij = new float[MK.getL()*MK.getP()][];
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
                    float tobs = distance(Source,Receiver)/velo_now;           
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
                float velo_now = Float.valueOf(MK.data[j][i]);
                float sik = 1/velo_now;
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
                FMK[iFMK].data[j][i]=Float.toString((float)kecepatanModelRata[idx]);
            }
        } // --- END 2 ---
        FMK[iFMK].setMaxMinVal();
        
        for (int i=0;i<FMK[iFMK].getP();i++){
            for(int j=0;j<FMK[iFMK].getL();j++){
                int idx = i*FMK[iFMK].getL()+j;
                System.out.print( FMK[iFMK].data[i][j]+" ");
            }
            System.out.println("");
        } // --- END 1 ---
        System.out.println("Nilai Min : "+FMK[iFMK].getminv());
        System.out.println("Nilai Max : "+FMK[iFMK].getmaxv());
        System.out.println("P : "+FMK[iFMK].getP());
        System.out.println("L : "+FMK[iFMK].getL());
        
        System.out.println("FMK "+iFMK+" done.....");
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
        return Math.pow((kiri/a),2) + Math.pow((kanan/b),2)<1;
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
    
    public void hitungRataMK () {
        RataMK = new MatriksKecepatan(FMK[0]);
        for (int i=0;i<MK.getP();i++){
            for(int j=0;j<MK.getL();j++){
                
                float total = 0f;
                
                for (int k=0; k<FMK.length; k++) {
                    total += Float.valueOf(FMK[k].data[i][j]);
                }
                total /= FMK.length;
                
                RataMK.data[i][j] = String.valueOf(total);
                
                
            }
        }
        RataMK.setMaxMinVal();
    }
}
