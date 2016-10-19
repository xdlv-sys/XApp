package xd.fw.mina.tlv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.FwUtil;
import xd.fw.I18n;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
@Service
public class UpgradeTask implements Callable<Integer>{

    @Value("${version}")
    int version;
    @Override
    public Integer call() throws Exception {
        File[] patches = I18n.getPatches(version);
        if (patches == null || patches.length < 1){
            return 0;
        }
        Arrays.sort(patches, File::compareTo);
        //prepare patch dest
        File tmp = new File(I18n.getPatchDir(), "tmp");
        if (tmp.exists() && !tmp.delete() && !tmp.mkdirs()){
            throw new Exception("can not clean tmp dir");
        }
        Set<String> deleteFiles = new HashSet<>();
        for (File patch : patches){
            FwUtil.unzip(patch, tmp);
            File deleteFile = new File(tmp,"conf.properties");
            if (deleteFile.exists()){
                try(Scanner scanner = new Scanner(deleteFile)){
                    while (scanner.hasNextLine()){
                        deleteFiles.add(scanner.nextLine());
                    }
                }
            }
        }

        //create bat script
        StringBuilder upgradeText = new StringBuilder();
        try(Scanner scanner = new Scanner(new File(tmp,"upgrade.bat"))){
            while (scanner.hasNextLine()){
                upgradeText.append(scanner.nextLine()).append("\n");
            }
        }
        StringBuilder delText = new StringBuilder();
        for (String del : deleteFiles){
            delText.append("del /Q ").append(del).append("\n");
        }
        int start = upgradeText.indexOf("rem $DEL");
        upgradeText.replace(start,start + 9, delText.toString());

        try(FileWriter fw = new FileWriter(new File(tmp,"upgrade_0.bat"))){
            fw.write(upgradeText.toString());
        }

        return 0;
    }
}
