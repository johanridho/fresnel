package Ruang2D;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


class ReaderExcel {
    
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

} 