package framework.custom.models.robot;

import com.google.gson.annotations.SerializedName;

import java.net.Socket;

public class RobotConfiguration {

    @SerializedName("ServerIP")
    private String serverIP;

    @SerializedName("PathMovesArm")
    private String pathMovesArm;

    @SerializedName("PathMovesAxes")
    private String pathMovesAxes;

    @SerializedName("IpModBotones1")
    private String ipModBotones1;

    @SerializedName("IpModBotones2")
    private String ipModBotones2;

    @SerializedName("IpModTeclado")
    private String ipModTeclado;

    @SerializedName("IpModTarjeta")
    private String ipModTarjeta;

    @SerializedName("IpModRecibo")
    private String ipModRecibo;

    @SerializedName("Client")
    private Socket client;

    public RobotConfiguration(){
        this.client=null;}

    /*
    This model implements a design pattern,
    the Builder Pattern.
    To be able to build the object with different representations
     */
    public static class  Builder{
        private final RobotConfiguration robotConfiguration;

        public Builder(){
            robotConfiguration=new RobotConfiguration();
        }

        public Builder setServerIP(String serverIP){
            robotConfiguration.serverIP=serverIP;
            return this;
        }

        public Builder setPathMovesArm(String pathMovesArm){
            robotConfiguration.pathMovesArm=pathMovesArm;
            return this;
        }
        public Builder setPathMovesAxes (String pathMovesAxes){
            robotConfiguration.pathMovesAxes=pathMovesAxes;
            return this;
        }

        public Builder setIpModBotones1(String ipModBotones1){
            robotConfiguration.ipModBotones1=ipModBotones1;
            return this;
        }

        public Builder setIpModBotones2(String ipModBotones2){
            robotConfiguration.ipModBotones2=ipModBotones2;
            return this;
        }

        public Builder setIpModTeclado(String ipModTeclado){
            robotConfiguration.ipModTeclado=ipModTeclado;
            return this;
        }

        public Builder setIpModTarjeta(String ipModTarjeta){
            robotConfiguration.ipModTarjeta=ipModTarjeta;
            return this;
        }

        public Builder setIpModRecibo(String ipModRecibo){
            robotConfiguration.ipModRecibo=ipModRecibo;
            return this;
        }

        public Builder setClient(Socket client){
            robotConfiguration.client=client;
            return this;
        }

        public RobotConfiguration build(){ return robotConfiguration;}
    }// End of Builder Pattern

    public String getServerIP(){return serverIP;}
    public String getPathMovesArm(){return pathMovesArm;}
    public String getPathMovesAxes (){return pathMovesAxes; }
    public String getIpModBotones1(){return ipModBotones1;}
    public String getIpModBotones2(){return ipModBotones2;}
    public String getIpModTeclado(){return ipModTeclado;}
    public String getIpModTarjeta(){return ipModTarjeta;}
    public String getIpModRecibo(){return ipModRecibo;}
    public Socket getClient(){return client;}

    public void setClient(Socket client){this.client=client;}
}
