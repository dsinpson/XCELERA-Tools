package framework.custom.utils;

import framework.dataProviders.ConfigFileReader;

public class AppProperties {

    private AppProperties(){}

    static ConfigFileReader reader = new ConfigFileReader("configs/config.properties");


    //Connections
    public static String getStrConexionIseries(){
        return reader.getPropertyByKey("strConexionIs");
    }

    public static String getUserIseries(){
        return reader.getPropertyByKey("userIs");
    }

    public static String getPasswordIseries(){
        return reader.getPropertyByKey("passwordIs");
    }

    public static String getStrConexionAuthentic(){
        return reader.getPropertyByKey("strConexionVa");
    }

    public static String getUserAuthentic(){
        return reader.getPropertyByKey("userVa");
    }

    public static String getPasswordAuthentic(){
        return reader.getPropertyByKey("passwordVa");
    }

    //StarcImagiUtils
    public static String getRutaStarImagiApi(){
        return reader.getPropertyByKey("starcImagiApiIP");
    }

    //CardConfigurations
    public static String getRutaSwitchCards(){return reader.getPropertyByKey("directorioSwitchCards");}

}
