package lico.classutil;

import java.util.ArrayList;

public class ClassAttribute {
    public Integer id;
    public String permission;
    public String attributeType;
    public String attributeName;
    public String defaultValue;
    public Boolean isList;
    public String listType;
    public String annotation;
    public Boolean isStatic = false;
    public Boolean isFinal = false;
    public String dbColumn;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\t/**\n");
        if(annotation !=null && !"".equals(annotation)){
            sb.append("\t * "+ annotation+"\n");
        }
        if(dbColumn !=null && !"".equals(dbColumn)){
            sb.append("\t * #Column\t"+dbColumn +"\n");
        }
        sb.append("\t */\n");
        sb.append("\t"+(permission!=null&&!"".equals(permission)?permission+" ":"")+
                    (isFinal?"final ":"")+(isStatic?"static ":"")+(isList?listType+"<"+attributeType+">":attributeType)+
                    " "+attributeName+(defaultValue!=null&&!"".equals(defaultValue)?" = "+defaultValue:"")+" ;\n\n");
        return sb.toString();
    }
    @Override
    public int hashCode() {
        return attributeName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if (obj instanceof ClassAttribute){
            ClassAttribute f = (ClassAttribute)obj;
            if(attributeName.equals(f.attributeName)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
