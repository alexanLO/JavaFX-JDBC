package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import App.Main;
import Controller.Util.Alerts;
import Controller.Util.Utils;
import Interface.DataChangeListener;
import Model.Components.Department;
import Model.service.DepartmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DepartmentListController implements Initializable, DataChangeListener{

    private DepartmentService departmentService;

    @FXML
    private TableView<Department> tableViewDepartment;
    @FXML
    private TableColumn<Department, Integer> tableColumnID;
    @FXML
    private TableColumn<Department, String> tableColumnName;
    @FXML
    private Button buttonNew;

    private ObservableList<Department> observableList;

    @FXML
    public void onButtonNewAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Department obj = new Department();
        createDialogForm(obj, "/controller/DepartmentForm.fxml", parentStage);
    }

    public void setDepartmentService(DepartmentService departmentService){
        this.departmentService = departmentService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }
    
    private void initializeNodes(){
        //method auxiliar para  iniciar o comportamento das colunas
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        //fazer com que o tableView acompanhe a janela
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if(departmentService == null){
            throw new IllegalStateException("DepartmentService was null");
        }
        List<Department> list = departmentService.findAll();
        observableList = FXCollections.observableArrayList(list);
        tableViewDepartment.setItems(observableList);
    }

    // função para carregar a janela do formulário
    private void createDialogForm(Department obj, String absoluteName, Stage parentStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            DepartmentFormController controllerForm = loader.getController();

            controllerForm.setDepartment(obj);
            controllerForm.setDepartmentService(new DepartmentService());
            controllerForm.subscribeDataChangeListener(this);
            controllerForm.updateFormData();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Department data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
         Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
