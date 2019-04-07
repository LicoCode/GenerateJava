package lico.classutil;

import java.util.HashSet;

public class ClassDefinition {
    public Integer id;
    public String packageName;
    public HashSet<String> importNames;
    public String permission = "public";
    public String className;
    public String parentClass;
    //Todo implement nend;
    public String author = "Lico";
    public String annotation;
    public Boolean isAbstract = false;
    public Boolean isStatic = false;
    public Boolean isFinal = false;
    public String dbTable;
    public HashSet<ClassAttribute> classAttributes;
    public HashSet<ClassMethod> classMethods;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(packageName !=null && !"".equals(packageName)) {
            sb.append("package " + packageName + ";\n\n");
        }
        if(importNames !=null) {
            for (String importName:
                 importNames) {
                sb.append("import " + importName + ";\n\n");
            }
        }
        sb.append("/**\n");
        if(annotation !=null && !"".equals(annotation)){
            sb.append(" * "+ annotation+"\n");
        }
        if(author !=null && !"".equals(author)){
            sb.append(" * @author\t"+author+"\n");
        }
        if(dbTable !=null && !"".equals(dbTable)){
            sb.append(" * #Tabel\t"+dbTable +"\n");
        }
        sb.append(" */\n");
        sb.append(permission+" "+(isFinal ?"final ":"")+(isStatic ?"static ":"")+
                (isAbstract ?"abstract ":"")+"class "+className +
                (parentClass !=null && !"".equals(parentClass)?" extends " +parentClass :"")+" {\n");
        if(classAttributes != null){
            for (ClassAttribute classAttribute:
                    classAttributes) {
                sb.append(classAttribute.toString());
            }
        }
        if(classMethods != null){
            for (ClassMethod classMethod:
                    classMethods) {
                sb.append(classMethod.toString());
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if (obj instanceof ClassDefinition){
            ClassDefinition c = (ClassDefinition)obj;
            if(className.equals(c.className)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
