package co.edu.uniquindio.poo.viewController;

import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.poo.App;
import co.edu.uniquindio.poo.controller.VehiculoController;
import co.edu.uniquindio.poo.model.Vehiculo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class VehiculoViewController {

    private VehiculoController vehiculoController;
    private ObservableList<Vehiculo> listVehiculos = FXCollections.observableArrayList();
    private Vehiculo selectedVehiculo;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtMatricula;

    @FXML
    private TextField txtModelo;

    @FXML
    private TextField txtMarca;

    @FXML
    private TextField txtAñoFabricacion;

    @FXML
    private Button btnLimpiar;

    @FXML
    private TableView<Vehiculo> tblListVehiculos;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnActualizarVehiculo;

    @FXML
    private Button btnAgregarVehiculo;

    @FXML
    private TableColumn<Vehiculo, String> tbcMatricula;

    @FXML
    private TableColumn<Vehiculo, String> tbcModelo;

    @FXML
    private TableColumn<Vehiculo, String> tbcMarca;

    @FXML
    private TableColumn<Vehiculo, String> tbcAñoFabricacion;

    private App app;

    @FXML
    void onAgregarVehiculo() {
        agregarVehiculo();
    }

    @FXML
    void onActualizarVehiculo() {
        actualizarVehiculo();
    }

    @FXML
    void onLimpiar() {
        limpiarSeleccion();
    }

    @FXML
    void onEliminar() {
        eliminarVehiculo();
    }

    @FXML
    void initialize() {
        vehiculoController = new VehiculoController(app.empresa);
        initView();
    }

    private void initView() {
        // Traer los datos de los vehículos a la tabla
        initDataBinding();

        // Obtiene la lista
        obtenerVehiculos();

        // Limpiar la tabla
        tblListVehiculos.getItems().clear();

        // Agregar los elementos a la tabla
        tblListVehiculos.setItems(listVehiculos);

        // Seleccionar elemento de la tabla
        listenerSelection();
    }

    private void initDataBinding() {
        tbcMatricula.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMatricula()));
        tbcModelo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModelo()));
        tbcMarca.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMarca()));
        tbcAñoFabricacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAñoFabricacion()));
    }

    private void obtenerVehiculos() {
        listVehiculos.addAll(vehiculoController.obtenerListaVehiculos());
    }

    private void listenerSelection() {
        tblListVehiculos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedVehiculo = newSelection;
            mostrarInformacionVehiculo(selectedVehiculo);
        });
    }

    private void mostrarInformacionVehiculo(Vehiculo vehiculo) {
        if (vehiculo != null) {
            txtMatricula.setText(vehiculo.getMatricula());
            txtModelo.setText(vehiculo.getModelo());
            txtMarca.setText(vehiculo.getMarca());
            txtAñoFabricacion.setText(vehiculo.getAñoFabricacion());
        }
    }

    private void agregarVehiculo() {
        Vehiculo vehiculo = buildVehiculo();
        if (vehiculoController.crearVehiculo(vehiculo)) {
            listVehiculos.add(vehiculo);
            limpiarCamposVehiculo();
        }
    }

    private Vehiculo buildVehiculo() {
        // Aquí puedes agregar lógica para decidir qué tipo de vehículo crear
        // Por simplicidad, creamos un Vehiculo base, pero podrías implementar lógica
        return new Vehiculo(txtMatricula.getText(), txtModelo.getText(), txtMarca.getText(), txtAñoFabricacion.getText());
    }

    private void eliminarVehiculo() {
        if (vehiculoController.eliminarVehiculo(txtMatricula.getText())) {
            listVehiculos.remove(selectedVehiculo);
            limpiarCamposVehiculo();
            limpiarSeleccion();
        }
    }

    private void actualizarVehiculo() {
        if (selectedVehiculo != null &&
                vehiculoController.actualizarVehiculo(selectedVehiculo.getMatricula(), buildVehiculo())) {

            int index = listVehiculos.indexOf(selectedVehiculo);
            if (index >= 0) {
                listVehiculos.set(index, buildVehiculo());
            }

            tblListVehiculos.refresh();
            limpiarSeleccion();
            limpiarCamposVehiculo();
        }
    }

    private void limpiarSeleccion() {
        tblListVehiculos.getSelectionModel().clearSelection();
        limpiarCamposVehiculo();
    }

    private void limpiarCamposVehiculo() {
        txtMatricula.clear();
        txtModelo.clear();
        txtMarca.clear();
        txtAñoFabricacion.clear();
    }

    public void setApp(App app) {
        this.app = app;
    }
}
