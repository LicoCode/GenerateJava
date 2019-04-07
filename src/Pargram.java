package src;
import lico.file.JavaFile;
import lico.mdb.CDODefinition;
import lico.mdb.MdbHelper;

import java.util.ArrayList;
import java.util.Iterator;


public class Pargram {

    public static void main(String[] args){

        String mdbPath = "C:\\Users\\Administrator\\Desktop\\InSite.mdb";
        String user = "";
        String password ="";
        String javaClassPath = "F:\\Code\\Person\\Java\\Designer\\cdo";
        String functionClassPath = "F:\\Code\\Person\\Java\\Designer\\function";

        MdbHelper mdb = new MdbHelper(mdbPath,user,password);

//        ClassDefinition fun = mdb.getFunction();
//        JavaFile classfile = new JavaFile(functionClassPath,fun);
//        classfile.saveClass();

        ArrayList<CDODefinition> cdos = null;
        int i = 1;
        do{
            cdos = mdb.getClassDef(i,20);
            Iterator iterator = cdos.iterator();
            while (iterator.hasNext()){
                CDODefinition cdo = (CDODefinition) iterator.next();
                cdo.classAttributes = mdb.getClassField(cdo);
            }
            iterator = cdos.iterator();
            while (iterator.hasNext()){
                CDODefinition cdo = (CDODefinition) iterator.next();
                cdo.classMethods= mdb.getMethod(cdo);

            }
            iterator = cdos.iterator();
            while (iterator.hasNext()){
                CDODefinition cdo = (CDODefinition) iterator.next();
                JavaFile classfile = new JavaFile(javaClassPath,cdo);
                classfile.saveClass();

            }

            i+=20;
        }while (cdos.size() > 0);

    }
}