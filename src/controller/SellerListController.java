package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import App.Main;
import Controller.Util.Alerts;
import Controller.Util.Utils;
import DB.DbIntegrityException;
import Interface.DataChangeListener;
import Model.Components.Seller;
import Model.service.DepartmentService;
import Model.service.SellerService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SellerListController implements Initializable, DataChangeListener {

    private SellerService SellerService;

    @FXML
    private TableView<Seller> tableViewSeller;
    @FXML
    private TableColumn<Seller, Integer> tableColumnID;
    @FXML
    private TableColumn<Seller, String> tableColumnName;
    @FXML
    private TableColumn<Seller, String> tableColumnEmail;
    @FXML
    private TableColumn<Seller, Date> tableColumnBirthDate;
    @FXML
    private TableColumn<Seller, Double> tableColumnBaseSalary;

    @FXML
    private TableColumn<Seller, Seller> tableColumnEDIT;
    @FXML
    TableColumn<Seller, Seller> tableColumnREMOVE;
    @FXML
    private Button buttonNew;

    private ObservableList<Seller> observableList;

    @FXML
    public void onButtonNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Seller obj = new Seller();
        createDialogForm(obj, "/controller/SellerForm.fxml", parentStage);
    }

    public void setSellerService(SellerService SellerService) {
        this.SellerService = SellerService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        // method auxiliar para iniciar o comportamento das colunas
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);


        // fazer com que o tableView acompanhe a janela
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (SellerService == null) {
            throw new IllegalStateException("SellerService was null");
        }
        List<Seller> list = SellerService.findAll();
        observableList = FXCollections.observableArrayList(list);
        tableViewSeller.setItems(observableList);
        initEditButtons();
        initRemoveButtons();
    }

    // função para carregar a janela do formulário
    private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            SellerFormController controllerForm = loader.getController();

            controllerForm.setSeller(obj);
            controllerForm.setServices(new SellerService(), new DepartmentService());
            controllerForm.loadAssociatedObjects();
            controllerForm.subscribeDataChangeListener(this);
            controllerForm.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Seller data");
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

    public void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("edit");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/Controller/SellerForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
        private final Button button = new Button("remove");
        @Override
        protected void updateItem(Seller obj, boolean empty) {
        super.updateItem(obj, empty);
        if (obj == null) {
        setGraphic(null);
        return;
        }
        setGraphic(button);
        button.setOnAction(event -> removeEntity(obj));
        }
        });
        }

    private void removeEntity(Seller obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

        if(result.get() == ButtonType.OK){
            if(SellerService == null){
                throw new IllegalStateException("Service was null");
            }

            try {
                SellerService.remove(obj);
                updateTableView();       
            } catch (DbIntegrityException e) {
                Alerts.showAlert("Error removing Seller", null, e.getMessage(), AlertType.ERROR);
            }
        }

    }
}
