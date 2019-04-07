package lico.classutil;

public class MethodParamter {
    public Integer id;
    public String paramterType;
    public String paramterName;
    public String annotation;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(paramterType+" "+paramterName);
        return sb.toString();
    }
}
