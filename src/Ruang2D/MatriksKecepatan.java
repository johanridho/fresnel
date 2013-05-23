/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ruang2D;

/**
 *
 * @author user
 */
public class MatriksKecepatan {

    private int M; // panjang
    private int N; // lebar
    private float MaxVal;
    private float MinVal;
    public String[][] data;
    
    public MatriksKecepatan() {
        M = 1;
        N = 1;
        data = new String[M][N];
        for (int i=0;i<M;i++){
            for(int j=0;j<N;j++){
                data[i][j]="0";
            }
        }
    }
    public MatriksKecepatan(int a, int b) {
        M=a;
        N=b;
        data = new String[M][N];
        for (int i=0;i<M;i++){
            for(int j=0;j<N;j++){
                data[i][j]=Float.toString(0f);
            }
        }
    }
    public MatriksKecepatan(MatriksKecepatan _mk) {
        M=_mk.M;
        N=_mk.N;
        data = new String[M][N];
        for (int i=0;i<M;i++){
            for(int j=0;j<N;j++){
                isiMatriks(i,j, Float.toString(_mk.getFloatData(i, j)));
            }
        }
    }
    public int getIntData(int i, int j){
        int x = Integer.decode(data[i][j]);
        return x;
    }
    public float getFloatData(int i, int j){
        float x = Float.valueOf(data[i][j]);
        return x;
    }
    public void printMatriks(){
        for (int i=0; i<M; i++){
            for (int j=0; j<N; j++){
                System.out.print (" "+ data[i][j]);
            }
            System.out.println("");
        }
    }
    public void printCell(int i, int j){
        System.out.println(data[i][j]);
    }
    public void isiMatriks (int i, int j, String a){
        data[i][j] = a;
    }
    private float MaxNumber(){
        float max=-9999999f;
        
        for (int i=0;i<M;i++){
            for (int j=0;j<N;j++){              
                
                if (getFloatData(i,j) > max){
                    max = getFloatData(i,j) ;                    
                }else{                    
                }
            }
        }
        return max;
    }
    private float MinNumber(){
        float min=9999999f;
        
        for (int i=0;i<M;i++){
            for (int j=0;j<N;j++){              
                
                if (getFloatData(i,j) < min){
                    min = getFloatData(i,j) ;                    
                }else{                    
                }
            }
        }
        return min;
    }
    public void setMaxMinVal() {
        MaxVal = MaxNumber();
        MinVal = MinNumber();
//        System.out.println("asd"+MinVal);
    }
    public float getmaxv() { return MaxVal; }
    public float getminv() { return MinVal; }
    public int getP() { return M;}
    public int getL() { return N;}
    
    
    
    
    
    
}
