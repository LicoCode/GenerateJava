package lico.file;

import lico.mdb.CDODefinition;
import lico.mdb.CDOField;
import lico.mdb.CDOMethod;

import java.io.*;
import java.util.Iterator;

public class JavaFile {
    private File classFile;
    private CDODefinition classDefinition;
    public JavaFile(String dir,CDODefinition classDefinition){
        classFile = openFile(dir+"\\"+classDefinition.className +".java");
        this.classDefinition = classDefinition;
    }

    private static File openFile(String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        return file;
    }

    private boolean isOpen(){
        if(classFile == null){
            System.out.println("Class file not opened!");
            return false;
        }else{
            return true;
        }
    }

    public void saveClass(){
        if(isOpen()){
            FileWriter fw;
            try {
                fw = new FileWriter(classFile);
                fw.append(classDefinition.toString());
                fw.flush();
                fw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

        }

    }

}
