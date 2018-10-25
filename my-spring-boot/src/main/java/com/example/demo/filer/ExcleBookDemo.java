package com.example.demo.filer;
/**
 * excle表格的导入导出Demo
 * @autor ww
 *
 */
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import com.example.demo.model.Book;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jxl.write.Label;
import jxl.write.WriteException;
public class ExcleBookDemo {
    /** 
     * 针对Book类进行导出的操作 
     * @param list 
     */
    public void excleOut(List<Book> list) {
        WritableWorkbook book = null;
        try {
            // 创建一个excle对象
            book = Workbook.createWorkbook(new File("d:/book.xls"));
            // 通过excle对象创建一个选项卡对象
            WritableSheet sheet = book.createSheet("sheet1", 0);
            // 创建一个单元格对象 列 行 值
            // Label label = new Label(0, 2, "test");
            for (int i = 0; i < list.size(); i++) {
                Book book2 = list.get(i);
                Label label1 = new Label(0, i, String.valueOf(book2.getId()));
                Label label2 = new Label(1, i, book2.getName());
                Label label3 = new Label(2, i, book2.getAuthor());

                // 将创建好的单元格对象放入选项卡中
                sheet.addCell(label1);
                sheet.addCell(label2);
                sheet.addCell(label3);
            }
            // 写入目标路径
            book.write();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                book.close();
            } catch (WriteException | IOException e) {
                e.printStackTrace();
            }
        }

    }
        /**
         * 针对Book类进行导入的操作
         *
         */
        public List<Book> excleIn() {
            List<Book> list = new ArrayList<Book>();
            Workbook workbook = null;
            try {
                // 获取Ecle对象
                workbook = Workbook.getWorkbook(new File("d:/book.xls"));
                // 获取选项卡对象 第0个选项卡
                Sheet sheet = workbook.getSheet(0);
                // 循环选项卡中的值
                for (int i = 0; i < sheet.getRows(); i++) {
                    Book book = new Book();
                    // 获取单元格对象
                    Cell cell0 = sheet.getCell(0, i);
                    // 取得单元格的值,并设置到对象中
                    book.setId(Integer.valueOf(cell0.getContents()));
                    // 获取单元格对象，然后取得单元格的值,并设置到对象中
                    book.setName(sheet.getCell(1, i).getContents());
                    book.setAuthor(sheet.getCell(2, i).getContents());
                    list.add(book);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                workbook.close();

            }
            return list;
        }

    public static void main(String[] args) {

        ExcleBookDemo book = new ExcleBookDemo();
        List<Book> list = new ArrayList<Book>();

        Book book2=new Book();
        book2.setId(1);
        book2.setName("书本名1");
        book2.setAuthor("张三");

        Book book3=new Book();
        book3.setId(2);
        book3.setName("书本名2");
        book3.setAuthor("李四");

        list.add(book2);
        list.add(book3);

        book.excleOut(list);

        List<Book> books = book.excleIn();

        for (Book bo : books) {
            System.out.println(bo.getId() + " " + bo.getName() + " " + bo.getAuthor());
        }
    }


}
