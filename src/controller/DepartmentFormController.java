package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Controller.Util.Alerts;
import Controller.Util.Constraints;
import Controller.Util.Utils;
import Interface.DataChangeListener;
import Model.Components.Department;
import Model.service.DepartmentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class DepartmentFormController implements Initializable {

    private Department entity;

    private DepartmentService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private Label labelError;

    public void setDepartment(Department entity) {
        this.entity = entity;
    }

    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onButtonSaveAction(ActionEvent event) {
        if(entity == null){
            throw new IllegalStateException("Entity was null");
        }
        if(service == null){
            throw new IllegalStateException("Service was null");
        }
        try {     
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (Exception e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Department getFormData() {
        Department obj = new Department();
        obj.setId(Utils.tryParseToInt(txtID.getText()));
        obj.setName(txtName.getText());
        return obj;
    }

    @FXML
    public void onButtonCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializableNodes();
    }

    private void initializableNodes() {
        Constraints.setTextFieldInteger(txtID);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        txtID.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }

}