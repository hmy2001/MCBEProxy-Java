package jp.dip.hmy2001.mcpeProxy;

import jp.dip.hmy2001.mcpeProxy.utils.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
    private static Main instance = null;
    private Config config;
    private final CommandReader console;
    private final SessionManager sessionManager;
    private static final Log logger = LogFactory.getLog("Main Logger");

    public static void main(String args[]){
        new Main();
    }

    public static Main getInstance(){
        return instance;
    }

    public Main(){
        instance = this;

        this.console = new CommandReader();

        config = new Config();

        this.console.removePromptLine();
        this.console.start();

        sessionManager = new SessionManager(Integer.parseInt(config.get("bindPort")), config.get("serverAddress"), Integer.parseInt(config.get("serverPort")));
        sessionManager.start();

        CommandReader.getInstance().stashLine();
        logger.info("MCBEProxy starting now....");
        CommandReader.getInstance().unstashLine();
    }

    public void onCommand(String command) {
        switch (command){
            case "shutdown":
            case "stop":
                shutdown();
        }
    }

    public void shutdown() {
        console.shutdown();
        console.interrupt();

        sessionManager.shutdown();
        sessionManager.interrupt();

        CommandReader.getInstance().stashLine();
        logger.info("Shutdown system....");
        CommandReader.getInstance().unstashLine();

        CommandReader.getInstance().removePromptLine();
    }

}
