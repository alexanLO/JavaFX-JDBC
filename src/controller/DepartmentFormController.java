package controller;

import java.net.URL;
import java.util.ResourceBundle;

import controller.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

<<<<<<< HEAD
public class DepartmentFormController implements Initializable{
    
=======
public class DepartmentFormController implements Initializable {

>>>>>>> 039ab388bab8fb0324e926283ebe89929962116d
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
<<<<<<< HEAD
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private Label labelErrorName;
  
=======
    private Label labelErrorName;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;

>>>>>>> 039ab388bab8fb0324e926283ebe89929962116d
    @FXML
    public void onButtonSaveAction(){
        System.out.println("Save!");
    }
    @FXML
    public void onButtonCancelAction(){
        System.out.println("Cancel!");
    }

<<<<<<< HEAD
    public void initialize(URL location, ResourceBundle resources) {
       InitializableNodes();
        
    }
    
    private void InitializableNodes() {
=======
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes(){
>>>>>>> 039ab388bab8fb0324e926283ebe89929962116d
        Constraints.setTextFieldInteger(txtID);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

<<<<<<< HEAD

=======
>>>>>>> 039ab388bab8fb0324e926283ebe89929962116d
}
