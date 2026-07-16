package src.config ;

public class serverConfig{
    public static final int DATA_PORT = 5555;
    public static final int DISCOVERY_PORT = 5556;
    
   
    public static volatile boolean isRunning = true;
    
    
    public static final String BIND_ADDRESS = "0.0.0.0";
    public static final String BROADCAST_ADDRESS = "255.255.255.255";
    public static final String DISCOVERY_SIGNAL = "GAME_CONTROLLER_DISCOVER";
    public static final String SERVER_RESPONSE = "SERVER_HERE";
}