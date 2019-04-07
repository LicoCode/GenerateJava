package lico.mdb;
import lico.classutil.ClassMethod;

public class CDOMethod extends ClassMethod {
    public String methodType;

    @Override
    public int hashCode() {
        return methodName.hashCode();
    }
}
