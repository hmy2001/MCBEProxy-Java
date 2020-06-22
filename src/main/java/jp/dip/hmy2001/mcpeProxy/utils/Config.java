package jp.dip.hmy2001.mcpeProxy.utils;

import java.io.*;

public class Config {
    private final MyProperties properties;

    public Config(){
        properties = new MyProperties();

        if(new File(getFilePath()).exists()){
            readFile();
        }else{
            createFile();
        }
    }

    public String get(String contentName){
        return properties.getProperty(contentName, "");
    }

    private String getFilePath(){
        String path = new File(".").getAbsoluteFile().getParent();

        return path + "/" + "config.properties";
    }

    private void createFile(){
        try {
            properties.setProperty("bindPort", "19132");
            properties.setProperty("serverAddress", "play.lbsg.net");
            properties.setProperty("serverPort", "19132");

            properties.store(new FileOutputStream(getFilePath()));

            properties.load(new FileInputStream(getFilePath()));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile(){
        try {
            properties.load(new FileInputStream(getFilePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
