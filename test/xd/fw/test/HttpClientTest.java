package xd.fw.test;

import org.testng.annotations.Test;
import xd.fw.HttpClientTpl;

import java.io.InputStream;

public class HttpClientTest {
    @Test
    public void testUploadFile() throws Exception {

        byte[] buffer;
        try (InputStream stream = getClass().getResourceAsStream("upload.txt")) {
            buffer = new byte[stream.available()];
            stream.read(buffer);
        }
        String HEADER_AGENT				= "User-Agent";
        String AUTHORIZATION				= "Authorization";
        String NEW_FILE_NAME				= "Filename";

        String ret = HttpClientTpl.executeMulti("http://221.226.241.34:61170",
                new String[][]{
                        {AUTHORIZATION,"etc"},
                        {NEW_FILE_NAME,"1223322.jpg"},
                        {HEADER_AGENT,getUserAgent()}
                },
                new Object[][]{{"1223322.jpg", buffer}});
        System.out.print(ret);
    }

    private static String getUserAgent(){
        StringBuffer buffer = new StringBuffer();
        String sdk = "SZFS/" + "1.0.0";
        String javaVersion = "Java/" + System.getProperty("java.version");
        buffer.append(sdk).append(" (").append(System.getProperty("os.name")).append(" ")
                .append(System.getProperty("os.arch")).append(" ").append(System.getProperty("os.version"))
                .append(") ").append(javaVersion);
        return buffer.toString();
    }
}
