package lico.classutil;

import java.util.ArrayList;
import java.util.HashSet;

public class ClassMethod {
    public Integer id;
    public String permission;
    public String returnType;
    public String methodName;
    public Boolean isStatic = false;
    public Boolean isFinal = false;
    public Boolean isAbstract = false;
    public String annotation;
    public HashSet<MethodParamter> methodParamters;
    public ArrayList<String> methodBody;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\t/**\n");
        if(annotation !=null && !"".equals(annotation)){
            sb.append("\t * "+ annotation+"\n");
        }
        //ToDO paramter annotation
        sb.append("\t */\n");
        sb.append("\t"+(permission!=null&&!"".equals(permission)?permission+" ":"")+
                (isFinal?"final ":"")+(isStatic?"static ":"")+(isAbstract?"abstract ":"")+returnType+" "+methodName+" (");
        if(methodParamters!=null){
            for (MethodParamter methodParamter : methodParamters) {
                sb.append(methodParamter.toString()+" ,");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("){\n");
        if(methodBody!=null){
            for (int i = 0,j = 0;i < methodBody.size();i++) {
                for(int k = 0;k < j;k++)
                    sb.append("\t");
                sb.append("\t\t"+methodBody.get(i)+(methodBody.get(i).equals("{")?"":";")+"\n");
                if(methodBody.get(i).equals("{")){
                    j++;
                }
                if(methodBody.get(i).equals("}")){
                    j--;
                }
            }
        }
        sb.append("\t}\n\n");
        return sb.toString();
    }
    @Override
    public int hashCode() {
        return methodName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if (obj instanceof ClassMethod){
            ClassMethod c = (ClassMethod)obj;
            if(methodName.equals(c.methodName)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
