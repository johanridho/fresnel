package Ruang2D;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class ReaderExcel {
    
//  public ReadExcel(){
//      Matriks2000 = new String[100][100];
//  }

  private String inputFile;
  
  
  
//  public String[][] Matriks2000;

  public void setInputFile(String inputFile) {
    this.inputFile = inputFile;
  }

  public void read() throws IOException  {
    int nomor = 1;
    File inputWorkbook = new File(inputFile);
    Workbook w;
    try {
      w = Workbook.getWorkbook(inputWorkbook);
      // Get the first sheet
      Sheet sheet = w.getSheet(0);
      // Loop over first 10 column and lines
      MatriksKecepatan MV = new MatriksKecepatan(sheet.getRows(),sheet.getColumns());
      
      for (int i = 0; i < sheet.getRows(); i++) {
        for (int j = 0; j < sheet.getColumns(); j++) {
            

          Cell cell = sheet.getCell(j, i);
          CellType type = cell.getType();         
          MV.isiMatriks(i, j, cell.getContents());
//          if (cell.getType() == CellType.LABEL) {
//            System.out.println(" "+ cell.getContents());
//          }
//
//          
//          if (cell.getType() == CellType.EMPTY) {
//                System.out.println("");
//            }
//          
//          if (cell.getType() == CellType.NUMBER) {
//            //System.out.print(nomor +") " + cell.getContents());
//            System.out.print(" " + cell.getContents());
//            nomor++;
////            if (cell.getType() == CellType.EMPTY){
////                System.out.print("\n");
////            }
//          }

        }
      }
                     
      MV.printMatriks();
      //MV.printCell(28,4 );
      //System.out.println(MV.MaxNumber());
      //System.out.println(MV.MinNumber());
      
    } catch (BiffException e) {
      e.printStackTrace();
    }
  }

  public MatriksKecepatan readtomv() throws IOException  {
    int nomor = 1;
    File inputWorkbook = new File(inputFile);
    Workbook w;
    MatriksKecepatan MV = new MatriksKecepatan(1,1);
    try {
      w = Workbook.getWorkbook(inputWorkbook);
      // Get the first sheet
      Sheet sheet = w.getSheet(0);
      // Loop over first 10 column and lines
      MV = new MatriksKecepatan(sheet.getRows(),sheet.getColumns());
      
      for (int i = 0; i < sheet.getRows(); i++) {
        for (int j = 0; j < sheet.getColumns(); j++) {
          Cell cell = sheet.getCell(j, i);
          CellType type = cell.getType();         
          MV.isiMatriks(i, j, cell.getContents());
        }
      }
      
    } catch (BiffException e) {
      e.printStackTrace();
    }
                     
      return MV;
  }
  
  public float[] bacaTobs() throws IOException  {
      
      float[] arrayTobs = new float[1];
      
      File inputWorkbook = new File(inputFile);
      Workbook w;
    
      try {
          w = Workbook.getWorkbook(inputWorkbook);          
          Sheet sheet = w.getSheet(0);              // Get the first sheet
          arrayTobs = new float[sheet.getRows()];
                  
          for (int i = 0; i < sheet.getRows(); i++) {            
              Cell cell = sheet.getCell(0, i);
              CellType type = cell.getType();         
              arrayTobs[i] = Float.parseFloat(cell.getContents());
//                arrayTobs[i] = cell.getContents();
          }          
          
      } catch (BiffException e) {
          e.printStackTrace();
        }
      
      return arrayTobs;
  }
  
  // Membaca File Source dan Receiver
    public Point2D.Double[] BacaSR(String File_Name) throws IOException{
        Point2D.Double[] arraySR = new Point2D.Double[0];
        try {
            File InputFile = new File(File_Name);
            Workbook W = Workbook.getWorkbook(InputFile);
            double X;
            double Y;
            arraySR = new Point2D.Double[W.getSheet(0).getRows()];
            for(int i=0;i<W.getSheet(0).getRows();i++){
                X = Double.valueOf(W.getSheet(0).getCell(0/*kolom*/, i/*baris*/).getContents().replace(",","."));
                Y = Double.valueOf(W.getSheet(0).getCell(1/*kolom*/, i/*baris*/).getContents().replace(",","."));
                arraySR[i] =  new Point2D.Double(X,Y);
            }
            
        } catch (BiffException ex) {
//            Logger.getLogger(Pembaca.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arraySR;
    }

}//end class 