/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ruang2D;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author PHKI-01
 */
public class FresnelVolume {
    
    /**
     * 
     */
    public MatriksKecepatan MK;
    public MatriksKecepatan[] FMK;      //array penampung semua kemungkinan matriks source-receiver, misal matriks source x receiver y, matrisk source a receiver b, dst
    public MatriksKecepatan RataMK;     //matriks penampung rata2 dari isi array FMK
    public LinkedList<Raypath> garis;
    
    private long durasi;
    private long waktuMulai;

//    float tobs;     // jarak source-receiver
     
//    float[] jmlRayPerSel;     // N
//    float[] bobotRata;  // Bobot rata-rata per sel
    
//    float velo;          // velocity
    float frekuensi;             // frekuensi
//    float lSR;
    
    Point2D.Double[] arraySrcRcv = {                 //array penampung point yg dipake sbg source & receiver, nambah source-receiver di sini
        new Point2D.Double(0.2, 2.8),           //source-receiver #1
        new Point2D.Double(76, 48),          //source-receiver #2
        new Point2D.Double(48, 0),           //source-receiver #3
        new Point2D.Double(48, 96),          //source-receiver #4
        new Point2D.Double(10, 50),          //source-receiver #5
        new Point2D.Double(30, 10),          //source-receiver #6
         new Point2D.Double(0, 48),           //source-receiver #1
        new Point2D.Double(96, 48),          //source-receiver #2
        new Point2D.Double(48, 0),           //source-receiver #3
        new Point2D.Double(48, 96),          //source-receiver #4
        new Point2D.Double(10, 50),          //source-receiver #5
        new Point2D.Double(30, 10),          //source-receiver #6
         new Point2D.Double(0, 48),           //source-receiver #1
        new Point2D.Double(96, 48),          //source-receiver #2
        new Point2D.Double(48, 0),           //source-receiver #3
        new Point2D.Double(48, 96),          //source-receiver #4
        
//        new Point2D.Double(2, 44)          //dst
    };
    
    /**
     * 
     * @param _MK
     */
    public FresnelVolume (MatriksKecepatan _MK) {
        
        waktuMulai = System.currentTimeMillis();
        
        MK = new MatriksKecepatan(_MK);
        
        int aa = arraySrcRcv.length;    
        int jmlSrcRcv = 0;                  
        while(aa!=0){
            jmlSrcRcv+=aa;
            aa--;
        }
        
        FMK = new MatriksKecepatan[jmlSrcRcv];                          
        for (int i=0; i<FMK.length; i++) {
//            FMK[i] = new MatriksKecepatan(96,96);
//            FMK[i] = new MatriksKecepatan(_MK);
            FMK[i] = new MatriksKecepatan(_MK.getP(),_MK.getL());
        }
        
        Point p = new Point(MK.getP()/2, MK.getL()/2);
        
           
//        velo = 1000;      
//        frekuensi = 500;
        
        
//        System.out.println("aaaaaaa"+jmlSrcRcv);
        
        boolean[][] booleanIJ = new  boolean[arraySrcRcv.length][arraySrcRcv.length];   //boolean buat nentuin source receiver pp sama ppp sudah pernah ktemu ato ngga, true klo udh ktemu
        for(int pp=0;pp<booleanIJ.length;pp++){
            for(int ppp=0;ppp<booleanIJ.length;ppp++){
                booleanIJ[pp][ppp]= false;
            }
        }
        
        /*----------------------------------------loop buat bikin elips ke FMK[iterasiFMK], isi thread nanti hrs dimasukin sini, ato loop ini yg dimasukin ke thread --------------------------------------------*/
        
        // Masukkan kombinasi point-point ke dalam array(list)
        int iterasiFMK=0;
        LinkedList<Point> Hitung = new LinkedList<Point>() {};
        for(int a=0;a<arraySrcRcv.length;a++ ){
            for(int b=0;b<arraySrcRcv.length;b++){
                if(arraySrcRcv[a]!=arraySrcRcv[b] && !booleanIJ[a][b] && !booleanIJ[b][a]){
                    Hitung.add(new Point(a,b));
//                    System.out.println("--------------");
//                    System.out.println(iterasiFMK);
//                    System.out.println("aaaa = "+a);
//                    System.out.println("bbbb = "+b);                        
                    iterasiFMK++;
                    booleanIJ[a][b]=true;
                    booleanIJ[b][a]=true;
                }else{

                }
            }
        }
        
        
        
        
        // Menghtung ellipse per 2 point
//        for (int x=0; x<Hitung.size(); x++) {
//            Point P = Hitung.get(x);
//            makeEllipse(x, arraySrcRcv[(int)P.getX()], arraySrcRcv [(int)P.getY()]);
//        }
        
        
        
        
        
        
        
        //untuk membuat perhitungan 1 thread 1 ellips source-receiver gunakan nthread = n(n-1)/2
//        int nthread = arraySrcRcv.length * (arraySrcRcv.length-1)/2;
        int nthread = 10;
        int nellipse = Hitung.size()/nthread;
//        System.out.println(nellipse);
        
        LinkedList<Thread> Threads = new LinkedList<Thread>() {};
        for (int x=0; x<nthread; x++) {
            final LinkedList<Point> PH = Hitung;
//            makeEllipse(x, arraySrcRcv[(int)P.getX()], arraySrcRcv [(int)P.getY()]);
            final int fnellipse = nellipse;
            final int fx = x;
            Threads.add(
                new Thread(new Runnable() {
                    public void run() {
                        for (int y=(fx*fnellipse); y<(fx*fnellipse)+fnellipse; y++) {
                            Point P = PH.get(y);
                            System.out.println("Thread "+fx+" perhitungan#"+y);
                            System.out.println("y = "+ y + "\nFP.getX() = " + P.getX() + "\nFP.getY() = " + P.getY() );
                            makeEllipse(y, arraySrcRcv[(int)P.getX()], arraySrcRcv [(int)P.getY()]);
                        }                
                    }
                })
            );
            System.out.println("Thread "+fx+" mulai");
            Threads.get(x).start();
        }
        boolean isMasihHidup = true;
        while (isMasihHidup) {
            isMasihHidup = false;
            for (int x=0; x<Threads.size(); x++) {
                isMasihHidup = isMasihHidup || (Threads.get(x).isAlive());
            }
        }        
        
         /*----------------------------------------thread buat makeellips, pindahin ke loop di atas, entah knp max hanya bisa 3 thread --------------------------------------------*/
//        Thread t0 = new Thread(new Runnable() {
//            public void run() {
//                makeEllipse (0, arraySrcRcv[0], arraySrcRcv[1]);
//                makeEllipse (1, arraySrcRcv[0], arraySrcRcv[2]);                
//            }
//        });
//        
//        Thread t3 = new Thread(new Runnable() {
//            public void run() {
//                makeEllipse (2, arraySrcRcv[0], arraySrcRcv[3]);
//                makeEllipse (3, arraySrcRcv[1], arraySrcRcv[2]);                
//            }
//        });
//        
//        Thread t4 = new Thread(new Runnable() {
//            public void run() {
//                makeEllipse (4, arraySrcRcv[1], arraySrcRcv[3]);
//                makeEllipse (5, arraySrcRcv[2], arraySrcRcv[3]);
//            }
//        });
//        
//        t3.start();
//        t4.start();
//        t0.start();
//        
//        while(t3.isAlive() ||t4.isAlive() ||t0.isAlive()){          //loop utk memastikan thread sudah mati smua
//            
//        }                
        /*----------------------------------------pindahin ke loop di atas --------------------------------------------*/
        
        
        // Menghitung rata-rata matriks kecepatan berdasarkan ellips-ellips yang sudah dibuat.
        hitungRataMK();
        // Print isi RataMK
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
        
        durasi = (System.currentTimeMillis() - waktuMulai) / 1000;        
        System.out.println("lama perhitungan total : "+durasi+" seconds");
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
//        float[] arrayTCal = new float[MK.getL()*MK.getP()];
        float[] deltaT = new float[MK.getL()*MK.getP()];
//        float[] deltaSi = new float[MK.getL()*MK.getP()];
        float[] sigma = new float[MK.getL()*MK.getP()];
//        float[] deltaSiRata = new float[MK.getL()*MK.getP()];
        float[] slowness = new float[MK.getL()*MK.getP()];
//        float[] slownessAkhir = new float[MK.getL()*MK.getP()];
//        float[] slownessAkhirRata = new float[MK.getL()*MK.getP()];
//        float[] kecepatanModel = new float[MK.getL()*MK.getP()];
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
//                    arrayTCal[idx] = hitungArrayTCal(arraySP[idx], arrayRP[idx]);     
                    float tobs = distance(Source,Receiver)/velo_now;           
                    deltaT[idx] = hitungDeltaT( hitungArrayTCal(arraySP[idx], arrayRP[idx]), tobs);
                } else {
                    // kalo ga, 0
                    arraySP[idx] = 0/velo_now;
                    arrayRP[idx] = 0/velo_now;   
//                    arrayTCal[idx] = 0;                
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
                            sigma[jdx] = deltaT[jdx]/ hitungArrayTCal(arraySP[jdx], arrayRP[jdx])*bobotij[idx][jdx]/totalw;
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
//                    deltaSi[idx] = totalsigma*slowness[idx];
//                    deltaSi[idx] = (deltaSi[idx]>600)? 600 : deltaSi[idx];
//                    deltaSiRata[idx] = deltaSi[idx]/jumRayPerSel;
//                    slownessAkhir[idx] = slowness[idx] + deltaSi[idx];
//                    slownessAkhirRata[idx] = slowness[idx] + deltaSiRata[idx];
//                    kecepatanModel[idx] = 1/slownessAkhir[idx]; 
//                    kecepatanModelRata[idx] = 1/slownessAkhirRata[idx];
                    kecepatanModelRata[idx] = 1/(slowness[idx] + totalsigma*slowness[idx]/jumRayPerSel);
                } else {
//                    deltaSi[idx] = 0;
//                    deltaSiRata[idx] = 0;
//                    slownessAkhir[idx] = 0;
//                    slownessAkhirRata[idx] = 0;
//                    kecepatanModel[idx] = 0;
                    kecepatanModelRata[idx] = 0;
                }
                FMK[iFMK].data[j][i]=Float.toString((float)kecepatanModelRata[idx]);
            }
        } // --- END 2 ---
        FMK[iFMK].setMaxMinVal();
        
//        for (int i=0;i<FMK[iFMK].getP();i++){
//            for(int j=0;j<FMK[iFMK].getL();j++){
//                int idx = i*FMK[iFMK].getL()+j;
//                System.out.print( FMK[iFMK].data[i][j]+" ");
//            }
//            System.out.println("");
//        } // --- END 1 ---
//        System.out.println("Nilai Min : "+FMK[iFMK].getminv());
//        System.out.println("Nilai Max : "+FMK[iFMK].getmaxv());
//        System.out.println("P : "+FMK[iFMK].getP());
//        System.out.println("L : "+FMK[iFMK].getL());
        
        durasi = (System.currentTimeMillis() - waktuMulai) / 1000;        
        System.out.println("lama perhitungan FMK "+iFMK+": "+durasi+ " seconds");
        System.out.println("FMK "+iFMK+" done.....");
    }
    
    private float hitungArrayTCal (float SP, float RP) {
        return SP + RP;
    }
    private float hitungDeltaT (float TCal, float tobs) {
        return Math.abs(tobs - TCal);
    }
    private float hitungBobotij (float deltaT) {
        if (deltaT >= (frekuensi/2))
            return 0;
        else
            return 1-2*frekuensi*deltaT;
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
    
    public void hitungRataMK () {               //hitung rata2 dari nilai kecepatan tiap grid dari matriks di dalam array FMK yg nge-intersect matriks di array FMK lain
        RataMK = new MatriksKecepatan(FMK[0]);
        for (int i=0;i<MK.getP();i++){
            for(int j=0;j<MK.getL();j++){
                
                float total = 0f;       
                int jumlah0 = 0;
                
//                for (int k=0; k<FMK.length; k++) {                
//                        total += Float.valueOf(FMK[k].data[i][j]);                    
//                }
//                total /= (FMK.length);
                
                for (int k=0; k<FMK.length; k++) {
                    if(Float.valueOf(FMK[k].data[i][j])==0.0 || Float.valueOf(FMK[k].data[i][j]).isNaN() ){
                        jumlah0++;
                    }else{
                        total += Float.valueOf(FMK[k].data[i][j]);
                    }                    
                }

                total /= (FMK.length-jumlah0);
                
                RataMK.data[i][j] = String.valueOf(total);
                
                
            }
        }
        RataMK.setMaxMinVal();
    }
}
