package src.util;
import java.util.function.Consumer;

public class logger {
    private static Consumer<String> uilog;

    public static void register(Consumer<String> callback){
        uilog = callback;
    }

    public static void log(String message){
        String formatMessage = "[ " + java.time.LocalTime.now().toString().substring(0,8) +  "]" + message ;
        
        if(uilog !=null){
        uilog.accept(formatMessage);
    }
    
    }

    
}
