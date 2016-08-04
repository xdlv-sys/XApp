package xd.fw.test;

import org.junit.Test;
import xd.fw.HttpClientTpl;

public class BasicTest {
    String url = "http://localhost:8080/an/syncUser.cmd";

    @Test
    public void userMember() throws Exception {

        HttpClientTpl.post(url, new String[][]{
                {"jknUser.userId", "4"},
                {"jknUser.userName", "a"},
                {"jknUser.referrer", "3"},
                {"jknUser.telephone", "15951928975"}
        });
    }
}
