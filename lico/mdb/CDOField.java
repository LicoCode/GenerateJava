package lico.mdb;

import lico.classutil.ClassAttribute;

public class CDOField extends ClassAttribute {
    public String defaultValueExpression;
    public String currentValueExpression;

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
        if(defaultValueExpression !=null && !"".equals(defaultValueExpression)){
            sb.append("\t * #defaultValueExpression\t"+defaultValueExpression +"\n");
        }
        if(currentValueExpression !=null && !"".equals(currentValueExpression)){
            sb.append("\t * #currentValueExpression\t"+currentValueExpression +"\n");
        }
        if(defaultValue != null && !"".equals(defaultValue)){
            sb.append("\t * #defaultValue\t"+defaultValue +"\n");
        }
        sb.append("\t */\n");
        sb.append("\t"+(permission!=null&&!"".equals(permission)?permission+" ":"")+
                (isFinal?"final ":"")+(isStatic?"static ":"")+(isList?listType+"<"+attributeType+">":attributeType)+
                " "+attributeName+" ;\n\n");
        return sb.toString();
    }

}
