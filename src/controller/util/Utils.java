package controller.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
 
    //acessar o Stage a onde o controller evento está
    public static Stage currentStage(ActionEvent event){
        return (Stage) ((Node)event.getSource()).getScene().getWindow();
    }

    public static Integer tryParseToInt(String str){
        try {
            return Integer.parseInt(str);    
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
