package controller;

import java.net.URL;
import java.util.ResourceBundle;

import controller.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable{
    
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private Label labelErrorName;
  
    @FXML
    public void onButtonSaveAction(){
        System.out.println("Save!");
    }
    @FXML
    public void onButtonCancelAction(){
        System.out.println("Cancel!");
    }

    public void initialize(URL location, ResourceBundle resources) {
       InitializableNodes();
        
    }
    
    private void InitializableNodes() {
        Constraints.setTextFieldInteger(txtID);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }


}
