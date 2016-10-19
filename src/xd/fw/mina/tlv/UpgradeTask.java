package xd.fw.mina.tlv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.I18n;

import java.io.File;
import java.util.concurrent.Callable;
@Service
public class UpgradeTask implements Callable<Integer>{

    @Value("${version}")
    int version;
    @Override
    public Integer call() throws Exception {
        File[] patches = I18n.getPatches(version);

        return 0;
    }
}
