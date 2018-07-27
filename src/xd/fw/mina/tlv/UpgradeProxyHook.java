package xd.fw.mina.tlv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.concurrent.*;

public class UpgradeProxyHook extends UnicastRemoteObject implements IUpgradeProxy {
    static int port = 38888;
    static String URL = "rmi://localhost:" + port + "/UpgradeProxyHook";
    static Logger logger = LoggerFactory.getLogger(UpgradeProxyHook.class);
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    protected UpgradeProxyHook() throws RemoteException {
    }

    public static void main(String[] args) throws Exception {
        logger.info("proxy:{}", Arrays.toString(args));
        String action = args != null && args.length > 0 ? args[0] : "start";

        if (action.equals("start")) {
            tryStop();
            Thread.sleep(1000);

            LocateRegistry.createRegistry(port);
            Naming.bind(URL, new UpgradeProxyHook());
            logger.info("proxy started.");

            scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    logger.info("start to check");
                    try {
                        String status = executeCmd(scheduler, new File("."), "sc query Tomcat8".split(" "));
                        if (!status.contains("STATE              : 4  RUNNING")) {
                            executeCmd(scheduler, new File("."), "sc start Tomcat8".split(" "));
                        } else {
                            logger.info(" tomcat8 is running");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 10, 60, TimeUnit.SECONDS);
        }
        if (action.equals("stop")) {
            tryStop();
        }
    }

    public static void tryStop() {
        try {
            IUpgradeProxy upgradeProxy = (IUpgradeProxy) Naming.lookup(URL);
            upgradeProxy.stop();
            logger.info("stop hook already");
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void upgrade(String upgradeBatch) throws Exception {
        IUpgradeProxy upgradeProxy = (IUpgradeProxy) Naming.lookup(URL);
        upgradeProxy.start(upgradeBatch);
    }


    @Override
    public void start(String upgradeBatch) throws RemoteException {
        logger.info("start upgrade");
        try {
            executeCmd(scheduler, new File("."), new String[]{upgradeBatch});
        } catch (Exception e) {
            logger.error("", e);
        }
    }


    public void stop() throws RemoteException {
        System.exit(0);
    }

    public static String executeCmd(ExecutorService taskExecutor, File directory, String[] cmd) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.directory(directory);
        Process process = null;
        BufferedReader br = null;
        BufferedReader br2 = null;
        StringBuilder buffer = new StringBuilder();
        try {
            process = builder.start();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            br2 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            Future<String> info = taskExecutor.submit(new ReaderThread(br));
            Future<String> error = taskExecutor.submit(new ReaderThread(br2));
            buffer.append(info.get(10, TimeUnit.SECONDS)).append(error.get(10, TimeUnit.SECONDS));
        } finally {
            boolean exit = false;
            if (process != null) {
                exit = process.waitFor(8, TimeUnit.SECONDS);
            }
            if (br != null) {
                br.close();
            }
            if (br2 != null) {
                br.close();
            }
            if (!exit && process != null) {
                logger.info("destroy process:");
                process.destroy();
            }
        }
        return buffer.toString();
    }

    static class ReaderThread implements Callable<String> {
        BufferedReader br;

        ReaderThread(BufferedReader br) {
            this.br = br;
        }

        public String call() {
            StringBuilder lines = new StringBuilder();
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    lines.append(line).append("\n");
                    logger.info(line);
                }
            } catch (IOException e) {
                logger.error("", e);
            }
            return lines.toString();
        }
    }
}
