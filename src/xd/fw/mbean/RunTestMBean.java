package xd.fw.mbean;

/**
 * Created by exiglvv on 6/1/2016.
 */
public interface RunTestMBean {

    String runCase(String className, String method, String params) throws Exception;
}
