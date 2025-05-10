package com.gadeadiaz.physiocare;

import com.gadeadiaz.physiocare.controllers.AppointmentItemController;
import com.gadeadiaz.physiocare.controllers.CloseController;
import com.gadeadiaz.physiocare.controllers.RecordItemController;
import com.gadeadiaz.physiocare.controllers.UserItemController;
import com.gadeadiaz.physiocare.exceptions.RequestErrorException;
import com.gadeadiaz.physiocare.models.*;
import com.gadeadiaz.physiocare.models.Record;
import com.gadeadiaz.physiocare.requests.*;
import com.gadeadiaz.physiocare.models.Appointment;
import com.gadeadiaz.physiocare.models.User;
import com.gadeadiaz.physiocare.responses.ErrorResponse;
import com.gadeadiaz.physiocare.services.AppointmentService;
import com.gadeadiaz.physiocare.services.PatientService;
import com.gadeadiaz.physiocare.services.PhysioService;
import com.gadeadiaz.physiocare.services.RecordService;
import com.gadeadiaz.physiocare.utils.*;
import com.gadeadiaz.physiocare.utils.Storage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller implements CloseController {


    @FXML private VBox physioPnAppointments;
    @FXML private Label lblNoPhysioAppointments;
    @FXML private ScrollPane physioScrollPaneAppointments;
    @FXML private Label txtPhysioDetailLicenceNumber;
    @FXML private Label txtPhysioDetailSpecialty;
    @FXML private Label txtPhysioDetailEmail;
    @FXML private Label lblDetailPhysioTitle;
    @FXML private Pane pnlPhysioDetail;
    // --- BOTONES ---
    @FXML private Button btnAddUser;
//    @FXML private Button btnPhysioForm;
//    @FXML private Button btnPatientForm;
    @FXML private Button btnAppointmentForm;
    @FXML private Button btnLogout;
    @FXML private Button btnPatients;
    @FXML private Button btnPhysios;
    @FXML private Button btnRecords;
    @FXML private Button btnAddAppointment;

    // --- LABELS ---
    @FXML private Label lblDetailPatientTitle;
    @FXML private Label lblPatientFormTitle;
    @FXML private Label lblPhysioFormTitle;
    @FXML private Label txtPatientDetailEmail;
    @FXML private Label txtPatientDetailAddress;
    @FXML private Label txtPatientDetailInsuranceNumber;
    @FXML private Label txtPatientDetailBirthDate;
    @FXML private Label lblBirthDate;
    @FXML private Label lblAddressAndSpecialty;
    @FXML private Label lblLogin;
    @FXML private Label lblPasswordPatientForm;
    @FXML private Label lblPasswordPhysioForm;
    @FXML private Label lblTitle;
    @FXML private Label txtPhysiosCount;
    @FXML private Label txtPatientsCount;
    @FXML private Label txtRecordsCount;
    @FXML private Label lblInsuranceLicenseNumList;

    @FXML private ScrollPane patientScrollPaneAppointments;
    @FXML private Label lblNoPatientAppointments;

    // --- TEXTFIELDS ---
    @FXML private TextField txtSearch;
    @FXML private TextField txtNamePatForm;
    @FXML private TextField txtEmailPatForm;
    @FXML private TextField txtPasswordPatForm;
    @FXML private TextField txtSurnamePatForm;
    @FXML private TextField txtAddressPatForm;
    @FXML private TextField txtInsuranceNumberPatForm;
    @FXML private TextField tfDiagnosis;
    @FXML private TextField tfTreatment;
    @FXML private TextField tfObservations;

    // --- DATEPICKERS ---
    @FXML private DatePicker dpBirthDate;
    @FXML private DatePicker dpDate;

    // --- COMBOBOXES ---
    @FXML private ComboBox<Physio> cBoxPhysios;
    @FXML private ComboBox<Patient> cBoxPatients;
    @FXML private ComboBox<String> cBoxSpecialtyPhysioForm;

    // --- MENÚS ---
    @FXML private SplitMenuButton splitSpecialty;

    // --- PANELES ---
    @FXML private Pane pnlPatientForm;
    @FXML private Pane pnlPatientDetail;
    @FXML private Pane pnlUsersList;
    @FXML private Pane pnlAppointmentForm;
    @FXML private Pane pnlPhysioForm;

    // --- CONTENEDORES ---
    @FXML private HBox hBoxUserList;
    @FXML private HBox hBoxRecordList;
    @FXML private VBox pnItems;
    @FXML private VBox patientPnAppointments;

    // --- CAMPOS DEL FORMULARIO DE PHYSIO ---
    @FXML private TextField tfNickPhysioForm;
    @FXML private PasswordField tfPasswordPhysioForm;
    @FXML private TextField tfNamePhysioForm;
    @FXML private TextField tfSurnamePhysioForm;
    @FXML private TextField tfLicenseNumberPhysioForm;
    @FXML private TextField tfEmailPhysioForm;

    // --- LÓGICA AUXILIAR ---
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    private final Gson gson = new Gson();
    private Stage stage;
    private Patient selectedPatient;
    private Physio selectedPhysio;
    private Appointment selectedAppointment;



    // --- ENUMS Y VARIABLES DE ESTADO ---
    private enum Entity { PATIENT, PHYSIO, RECORD, APPOINTMENT }
    private Entity selectedListEntity = Entity.PATIENT;


    public void initialize() {
        getPatients();
    }

    public void showUsersListPanel(){
        clearPatientForm();
        clearPhysioForm();
        //Primero los que se ocultan
        pnlPatientForm.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlAppointmentForm.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        //Por ultimo, el que se muestra
        pnlUsersList.setVisible(true);
        pnlUsersList.toFront();
    }

    public void showAppointmentsFormPanel(){
        clearPatientForm();
        clearPhysioForm();
        pnlUsersList.setVisible(false);
        pnlPatientForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlAppointmentForm.setVisible(true);
        pnlAppointmentForm.toFront();
    }

    public void showPatientDetailPanel(){
        clearPatientForm();
        clearPhysioForm();
        pnlPatientForm.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlPatientDetail.setVisible(true);
        pnlPatientDetail.toFront();
    }

    public void showPhysioDetailPanel(){
        clearPatientForm();
        clearPhysioForm();
        pnlPatientForm.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlPhysioDetail.setVisible(true);
        pnlPhysioDetail.toFront();
    }

    public void showPatientFormPanel(){
        clearPatientForm();
        pnlPhysioForm.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlAppointmentForm.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlPatientForm.setVisible(true);
        pnlPatientForm.toFront();
    }

    public void showPhysioFormPanel(){
        clearPatientForm();
        pnlPatientForm.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlAppointmentForm.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlPhysioForm.setVisible(true);
        pnlPhysioForm.toFront();
    }




//    ---------- PATIENTS ----------
    public void getPatients() {
        btnAddUser.setVisible(true);
        showUsersListPanel();
        PatientService.getPatients("")
            .thenAccept(patients -> {
                selectedListEntity = Entity.PATIENT;
                Platform.runLater(() -> showPatients(patients));
            }).exceptionally(e -> {
                RequestErrorException ex = (RequestErrorException) e.getCause();
                ErrorResponse errorResponse = ex.getErrorResponse();
                Platform.runLater(() ->
                        Message.showError(errorResponse.getError(), errorResponse.getMessage())
                );
                return null;
            });

    }

    public void getPatientById(int id) {
        PatientService.getPatientById(id)
            .thenAccept(patient -> {
                Platform.runLater(() -> {
                    selectedPatient = patient;
                    showPatientDetail();
                });
            }).exceptionally(e -> {
                RequestErrorException ex = (RequestErrorException) e.getCause();
                ErrorResponse errorResponse = ex.getErrorResponse();
                Platform.runLater(() ->
                        Message.showError(errorResponse.getError(), errorResponse.getMessage())
                );
                return null;
            });

    }
    public void showPatients(List<Patient> patients) {
        pnItems.getChildren().clear();
        for (Patient patient : patients) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/gadeadiaz/physiocare/user_item.fxml"
            ));
            try {
                Node node = loader.load();
                UserItemController userItemController = loader.getController();

                userItemController.setUserId(patient.getId());
                userItemController.setLblAttribute1(patient.getName());
                userItemController.setLblAttribute2(patient.getSurname());
                userItemController.setLblAttribute3(patient.getEmail());
                userItemController.setLblAttribute4(patient.getInsuranceNumber());

                node.setOnMouseEntered(_ -> node.setStyle("-fx-background-color : #0A0E3F"));
                node.setOnMouseExited(_ -> node.setStyle("-fx-background-color : #02030A"));

                userItemController.setDetailListener(_ -> {
                    selectedPhysio = null;
                    getPatientById(patient.getId());
                });

                userItemController.setDeleteListener(idPatient ->
                    PatientService.deletePatient(idPatient).thenAccept(_ -> {
                        getPatients();
                        Platform.runLater(() ->
                                Message.showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Delete",
                                        "Patient deleted",
                                        "Patient successfully deleted"
                                )
                        );
                    }).exceptionally(e -> {
                        RequestErrorException ex = (RequestErrorException) e;
                        ErrorResponse errorResponse = ex.getErrorResponse();
                        Platform.runLater(() ->
                                Message.showError(errorResponse.getError(), errorResponse.getMessage())
                        );
                        return null;
                    })
                );

                pnItems.getChildren().add(node);
            } catch (IOException e) {
                System.out.println("Patient loader fail: " + e.getMessage());
            }
        }
        txtPatientsCount.setText(String.valueOf(patients.size()));
    }

    public void showPatientDetail(){
        showPatientDetailPanel();
        lblDetailPatientTitle.setText(selectedPatient.getName() + " " + selectedPatient.getSurname());
        txtPatientDetailEmail.setText(selectedPatient.getEmail());
        if(selectedPatient.getAddress().isEmpty()){
            txtPatientDetailAddress.setText("Address not indicated");
        }else{
            txtPatientDetailAddress.setText(selectedPatient.getAddress());
        }
        txtPatientDetailInsuranceNumber.setText(selectedPatient.getInsuranceNumber());
        txtPatientDetailBirthDate.setText(selectedPatient.getBirthdate());
        getPatientAppointments(selectedPatient.getId());
    }

    public void getPatientsBySurname() {
        PatientService.getPatients(txtSearch.getText()).thenAccept(patients -> {
            selectedListEntity = Entity.PATIENT;
            Platform.runLater(() -> showPatients(patients));
        }).exceptionally(e -> {
            RequestErrorException ex = (RequestErrorException) e.getCause();
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() ->
                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
            );
            return null;
        });
    }

    public void showCreatePatientForm(){
        selectedPatient = null;
        lblPasswordPatientForm.setVisible(true);
        txtPasswordPatForm.setVisible(true);
        showPatientFormPanel();
    }

    public void btnSavePatient() {
        if(selectedPatient == null){
            postPatient();
        }else{
            putPatient();
        }
    }

    /**
     * Sends a POST request to create a new patient on the server.
     * If the request is successful, the list of patients is refreshed. If there is an error, an error message is displayed.
     */
    public void postPatient() {
        if (isValidPatientForm()) {
            Patient newPatient = new Patient(
                txtNamePatForm.getText(),
                txtSurnamePatForm.getText(),
                dpBirthDate.getValue().format(formatter),
                txtAddressPatForm.getText(),
                txtInsuranceNumberPatForm.getText(),
                txtEmailPatForm.getText());

            User newUser = new User(txtInsuranceNumberPatForm.getText(), txtPasswordPatForm.getText(), "patient");

            PatientService.postPatient(new PatientPOSTRequest(newUser, newPatient))
                .thenAccept(patient -> {
                    Platform.runLater(() -> {
                        selectedPatient = patient;
                        clearPatientForm();
                        showPatientDetail();
                    });
                }).exceptionally(e -> {
                    RequestErrorException ex = (RequestErrorException) e.getCause();
                    ErrorResponse errorResponse = ex.getErrorResponse();
                    Platform.runLater(() ->
                            Message.showError(errorResponse.getError(), errorResponse.getMessage())
                    );
                    return null;
                });
        }
    }

    public boolean isValidPatientForm() {
        String name = txtNamePatForm.getText();
        String surname = txtSurnamePatForm.getText();
        LocalDate birthDate = dpBirthDate.getValue();
        String email = txtEmailPatForm.getText();
        String address = txtAddressPatForm.getText();
        String insuranceNumber = txtInsuranceNumberPatForm.getText();
        String password = txtPasswordPatForm.getText();

        StringBuilder errorBuilder = new StringBuilder();

        if (name.length() < 2 || name.length() > 50) {
            errorBuilder.append("- Name must be between 2 and 50 characters\n");
        }
        if (surname.length() < 2 || surname.length() > 50) {
            errorBuilder.append("- Surname must be between 2 and 50 characters\n");
        }
        if (email.length() > 75) {
            errorBuilder.append("- Email must be 75 characters or fewer\n");
        }
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errorBuilder.append("- Email format is invalid\n");
        }
        if (address.length() > 100) {
            errorBuilder.append("- Address length must be lower or equals than 100 characters\n");
        }
        if (!insuranceNumber.matches("^[0-9A-Z]{9}$")) {
            errorBuilder.append("- Insurance number must be 9 uppercase letters or digits\n");
        }
        if (birthDate == null) {
            errorBuilder.append("- Birth date must not be empty\n");
        }else{
            if (!birthDate.isBefore(LocalDate.now())) {
                errorBuilder.append("- The birth date must be before today\n");
            }
        }
        if (selectedPatient == null && password.length() < 7) {
            errorBuilder.append("- Password must be at least 7 characters long\n");
        }
        if (!errorBuilder.isEmpty()) {
            showValidationError(errorBuilder.toString());
            return false;
        }

        return true;
    }

    public void clearPatientForm(){
        for (Node child: pnlPatientForm.getChildren()) {
            if(child instanceof TextField){
                ((TextField) child).clear();
            }
        }
        lblPatientFormTitle.setText("New Patient");
        dpBirthDate.setValue(null);
    }

    public void showValidationError(String message) {
        Message.showError("Validation Error", message);
    }

    /**
     * Sends a PUT request to update an existing patient's information on the server.
     * If the update is successful, the patient's details are displayed. Otherwise, an error message is shown.
     */
    public void putPatient() {
//      INTERNAL SERVER ERROR si se modifica un paciente y no tien appointments
        if (isValidPatientForm()) {
            Patient updatedPatient = new Patient(
                txtNamePatForm.getText(),
                txtSurnamePatForm.getText(),
                dpBirthDate.getValue().format(formatter),
                txtAddressPatForm.getText(),
                txtInsuranceNumberPatForm.getText(),
                txtEmailPatForm.getText());

            PatientService.putPatient(updatedPatient, selectedPatient.getId())
                .thenAccept(patient -> {
                    Platform.runLater(() -> {
                        clearPatientForm();
                        selectedPatient = patient;
                        showPatientDetail();
                    });
                }).exceptionally(e -> {
                    RequestErrorException ex = (RequestErrorException) e.getCause();
                    ErrorResponse errorResponse = ex.getErrorResponse();
                    Platform.runLater(() -> Message.showError(errorResponse.getError(), errorResponse.getMessage()));
                    return null;
                });
        }
    }

    /**
     * Displays detailed information of the currently selected patient.
     * Sets text fields and labels with patient data and adjusts the UI components to display the patient's details.
     */
    public void showEditPatientForm() {
        showPatientFormPanel();
        lblPasswordPatientForm.setVisible(false);
        txtPasswordPatForm.setVisible(false);

        lblPatientFormTitle.setText(selectedPatient.getName() + " " + selectedPatient.getSurname());
        txtNamePatForm.setText(selectedPatient.getName());
        txtSurnamePatForm.setText(selectedPatient.getSurname());
        txtEmailPatForm.setText(selectedPatient.getEmail());
        lblAddressAndSpecialty.setText("Address");
        txtAddressPatForm.setText(selectedPatient.getAddress());
        lblLogin.setText("Insurance Number");
        txtInsuranceNumberPatForm.setText(selectedPatient.getInsuranceNumber());
        dpBirthDate.setValue(LocalDate.parse(selectedPatient.getBirthdate(), formatter));
    }

    /**
     * Displays detailed information of the currently selected physio.
     * Sets text fields and labels with physio data and adjusts the UI components to display the physio's details.
     */
    public void showEditPhysioForm() {
        showPhysioFormPanel();
        lblPasswordPhysioForm.setVisible(false);
        tfPasswordPhysioForm.setVisible(false);

        lblPhysioFormTitle.setText(selectedPhysio.getName() + " " + selectedPhysio.getSurname());
        txtNamePatForm.setText(selectedPhysio.getName());
        txtSurnamePatForm.setText(selectedPhysio.getSurname());
        txtEmailPatForm.setText(selectedPhysio.getEmail());
        lblAddressAndSpecialty.setText("Specialty");
        txtAddressPatForm.setText(selectedPhysio.getSpecialty());
        lblLogin.setText("Insurance Number");
        txtInsuranceNumberPatForm.setText(selectedPhysio.getLicenseNumber());

        splitSpecialty.setVisible(false);
    }


//    ---------- PHYSIOS ----------
    /**
     * Fetches the list of physiotherapists from the server and updates the UI with the retrieved data.
     * If there is an error, an error message is displayed.
     */
    public void getPhysios() {
        btnAddUser.setVisible(true);
        showUsersListPanel();
        PhysioService.getPhysios("").thenAccept(physios -> {
            selectedListEntity = Entity.PHYSIO;
            Platform.runLater(() -> showPhysios(physios));
        }).exceptionally(e -> {
            RequestErrorException ex = (RequestErrorException) e.getCause();
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() ->
                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
            );
            return null;
        });
    }

    public void getPhysioById(int id) {
        PhysioService.getPhysioById(id)
                .thenAccept(physio -> {
                    Platform.runLater(() -> {
                        selectedPhysio = physio;
                        showPhysioDetail();
                    });
                }).exceptionally(e -> {
                    RequestErrorException ex = (RequestErrorException) e.getCause();
                    ErrorResponse errorResponse = ex.getErrorResponse();
                    Platform.runLater(() ->
                            Message.showError(errorResponse.getError(), errorResponse.getMessage())
                    );
                    return null;
                });

    }

    /**
     * Displays the list of physiotherapists on the UI.
     * @param physios The list of physiotherapists to be displayed.
     */
    public void showPhysios(List<Physio> physios) {
        pnItems.getChildren().clear();
        for (Physio physio : physios) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gadeadiaz/physiocare/user_item.fxml")
            );
            try {
                Node node = loader.load();
                UserItemController userItemController = loader.getController();

                userItemController.setUserId(physio.getId());
                userItemController.setLblAttribute1(physio.getName());
                userItemController.setLblAttribute2(physio.getSurname());
                userItemController.setLblAttribute3(physio.getEmail());
                userItemController.setLblAttribute4(physio.getLicenseNumber());

                node.setOnMouseEntered(_ -> node.setStyle("-fx-background-color : #0A0E3F"));
                node.setOnMouseExited(_ -> node.setStyle("-fx-background-color : #02030A"));

                userItemController.setDetailListener(_ -> {
                    selectedPatient = null;
                    getPhysioById(physio.getId());
                });

                if (Storage.getInstance().getUserdata().getValue().equals("physio")) {
                    userItemController.setBtnDeleteVisibility(false);

                    userItemController.setDeleteListener(idPhysio ->
                            PhysioService.deletePhysio(idPhysio).thenAccept(_ -> {
                                getPhysios();
                                Platform.runLater(() ->
                                        Message.showMessage(
                                                Alert.AlertType.CONFIRMATION,
                                                "Delete",
                                                "Physio deleted",
                                                "Physio successfully deleted"
                                        )
                                );
                            }).exceptionally(e -> {
                                RequestErrorException ex = (RequestErrorException) e;
                                ErrorResponse errorResponse = ex.getErrorResponse();
                                Platform.runLater(() ->
                                        Message.showError(
                                                errorResponse.getError(),
                                                errorResponse.getMessage()
                                        )
                                );
                                return null;
                            })
                    );
                }

                pnItems.getChildren().add(node);
            } catch (IOException e) {
                System.out.println("Patient loader fail: " + e.getMessage());
            }
        }
        txtPhysiosCount.setText(String.valueOf(physios.size()));
    }

    public void showPhysioDetail(){
        showPhysioDetailPanel();
        lblDetailPhysioTitle.setText(selectedPhysio.getName() + " " + selectedPhysio.getSurname());
        txtPhysioDetailEmail.setText(selectedPhysio.getEmail());

        txtPhysioDetailLicenceNumber.setText(selectedPhysio.getLicenseNumber());
        txtPhysioDetailSpecialty.setText(selectedPhysio.getSpecialty());
        getPhysioAppointments(selectedPhysio.getId());
    }


    public void getPhysiosBySpecialty() {
        PhysioService.getPhysios(txtSearch.getText()).thenAccept(physios -> {
            selectedListEntity = Entity.PHYSIO;
            Platform.runLater(() -> showPhysios(physios));
        }).exceptionally(e -> {
            RequestErrorException ex = (RequestErrorException) e.getCause();
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() ->
                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
            );
            return null;
        });
    }

    public void showCreatePhysioForm() {
        selectedPhysio = null;
        tfNickPhysioForm.setVisible(true);
        tfPasswordPhysioForm.setVisible(true);
        cBoxSpecialtyPhysioForm.setValue("Sports");
        cBoxSpecialtyPhysioForm.setItems(
            FXCollections.observableList(
                    List.of("Sports", "Neurological", "Pediatric", "Geriatric", "Oncological")
            )
        );
        showPhysioFormPanel();
    }

    public void btnSavePhysio() {
        if(selectedPhysio == null){
            postPhysio();
        }else{
            putPhysio();
        }
    }
    public void postPhysio(){
        if(isValidPhysioForm()){
            Physio newPhysio = new Physio(
                    tfNamePhysioForm.getText(),
                    tfSurnamePhysioForm.getText(),
                    cBoxSpecialtyPhysioForm.getValue(),
                    tfLicenseNumberPhysioForm.getText(),
                    tfEmailPhysioForm.getText()
            );
            User newUser =  new User(tfNickPhysioForm.getText(), tfPasswordPhysioForm.getText(), "physio");
            PhysioPOSTRequest physioPOSTRequest = new PhysioPOSTRequest(newUser, newPhysio);
            PhysioService.create(physioPOSTRequest).thenAccept(physio -> {

                //Los mensaje de error no van
                // Platform runlater no muestra los alert por algún motivo
                Platform.runLater(() -> {
                    selectedPhysio = physio;
                    clearPhysioForm();
                    getPhysios();
                });

//                showPhysioDetail(); Hay que implementarla
            }).exceptionally(e -> {
                RequestErrorException ex = (RequestErrorException) e;
                ErrorResponse errorResponse = ex.getErrorResponse();
                Platform.runLater(() ->
                        Message.showError(errorResponse.getError(), errorResponse.getMessage())
                );
                return null;
            });
        }
    }

    public void clearPhysioForm(){
        for (Node child: pnlPhysioForm.getChildren()) {
            if(child instanceof TextField){
                ((TextField) child).clear();
            }
        }
        lblPhysioFormTitle.setText("New Physio");
        cBoxSpecialtyPhysioForm.setValue("Sports");
    }

    public boolean isValidPhysioForm() {
        String nick = tfNickPhysioForm.getText();
        String password = tfPasswordPhysioForm.getText();
        String name = tfNamePhysioForm.getText();
        String surname = tfSurnamePhysioForm.getText();
        String specialty = cBoxSpecialtyPhysioForm.getValue();
        String licenseNumber = tfLicenseNumberPhysioForm.getText();
        String email = tfEmailPhysioForm.getText();

        StringBuilder errorBuilder = new StringBuilder();

        if (nick.length() < 4) {
            errorBuilder.append("- Nick must be at least 4 characters long\n");
        }
        if (password.length() < 7) {
            errorBuilder.append("- Password must be at least 7 characters long\n");
        }
        if (name.length() < 2 || name.length() > 50) {
            errorBuilder.append("- Name must be between 2 and 50 characters\n");
        }
        if (surname.length() < 2 || surname.length() > 50) {
            errorBuilder.append("- Surname must be between 2 and 50 characters\n");
        }
        if (specialty == null || specialty.trim().isEmpty()) {
            errorBuilder.append("- Specialty must be selected\n");
        }
        if (!licenseNumber.matches("^[0-9A-Z]{8}$")) {
            errorBuilder.append("- License number must be 8 uppercase letters or digits\n");
        }
        if (email.length() > 75) {
            errorBuilder.append("- Email must be 75 characters or fewer\n");
        }
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errorBuilder.append("- Email format is invalid\n");
        }

        if (!errorBuilder.isEmpty()) {
            showValidationError(errorBuilder.toString());
            return false;
        }

        return true;
    }


    /**
     * Sends a PUT request to update an existing physio's information on the server.
     * If the update is successful, the physio's details are displayed. Otherwise, an error message is shown.
     */
    public void putPhysio() {
        /*Map<String, Object> postBody = createPhysioRequestBody();

        String apiUrl = ServiceUtils.SERVER + "physios/" + selectedPhysio.getId();
        String jsonRequest = gson.toJson(postBody);

        ServiceUtils.getResponseAsync(apiUrl, jsonRequest, "PUT")
                .thenApply(json -> gson.fromJson(json, PhysioResponse.class)
                )
                .thenAccept(response -> {
                    if (response.isOk()) {
                        Platform.runLater(() -> {
                            selectedPhysio = response.getResult();
                            showPhysio();
                        });
                    } else {
                        Platform.runLater(() -> Message.showError("Error updating Physio", response.getError()));
                    }
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        Message.showError("Error", "Error updating Physio");
                        openLoginView(stage);
                    });
                    return null;
                });*/
    }



//    ---------- APPOITNMENTS ----------
    public void getPatientAppointments(int patientId) {
        PatientService.getPatientAppointments(patientId).thenAccept(appointments -> {
            Platform.runLater(() -> {
                selectedListEntity = Entity.APPOINTMENT;
                patientScrollPaneAppointments.setVisible(true);
                lblNoPatientAppointments.setVisible(true);
                showPatientAppointments(appointments);
            });
        }).exceptionally(e -> {
            RequestErrorException ex = (RequestErrorException) e.getCause();
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() -> {
                lblNoPatientAppointments.setVisible(true);
                patientScrollPaneAppointments.setVisible(false);
//                Message.showError(errorResponse.getError(), errorResponse.getMessage()); Hay que ver que hacer con este error
            });
            return null;
        });
    }

    /**
     * Displays the list of appointments on the UI.
     * @param appointments The list of appointments to be displayed.
     */
    public void showPatientAppointments(List<Appointment> appointments) {
        patientPnAppointments.getChildren().clear();
        for (Appointment appointment : appointments) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gadeadiaz/physiocare/appointment_item.fxml")
            );
            try {
                Node node = loader.load();
                AppointmentItemController controller = loader.getController();

                String questionImage = "/com/gadeagiaz/physiocare/images/question.jpg";
                String acceptAppointmentImage = "/com/gadeagiaz/physiocare/images/ok_appointment.jpg";

                controller.setAppointmentId(appointment.getId());
//                if(appointment.getConfirmed()){
//                    controller.getImgAppointment().setImage(new Image(acceptAppointmentImage));
//                }else{
//                    controller.getImgAppointment().setImage(new Image(questionImage));
//                }


                controller.getLblDate().setText(appointment.getDate());
                controller.getLblDiagnosis().setText(appointment.getDiagnosis());

                controller.getLblPhysioName().setText(appointment.getPhysio().getName() + " " + appointment.getPhysio().getSurname());

                node.setOnMouseEntered(_ -> node.setStyle("-fx-background-color : #0A0E3F"));
                node.setOnMouseExited(_ -> node.setStyle("-fx-background-color : #02030A"));

                controller.setAppointmentDetailListener(_ -> {
//                    selectedAppointment = appointment;
//                    selectedPatient = null;
//                    selectedPhysio = null;
                    System.out.println(appointment);
                });

                patientPnAppointments.getChildren().add(node);
            } catch (IOException e) {
                System.out.println("Appointment loader fail: " + e.getMessage());
            }
        }
    }
    public void getPhysioAppointments(int physioId) {
        PhysioService.getPhysioAppointments(physioId).thenAccept(appointments -> {
            Platform.runLater(() -> {
                selectedListEntity = Entity.APPOINTMENT;
                physioScrollPaneAppointments.setVisible(true);
                lblNoPhysioAppointments.setVisible(true);
                showPhysioAppointments(appointments);
            });
        }).exceptionally(e -> {
            RequestErrorException ex = (RequestErrorException) e.getCause();
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() -> {
                lblNoPhysioAppointments.setVisible(true);
                physioScrollPaneAppointments.setVisible(false);
            });
            return null;
        });
    }

    /**
     * Displays the list of appointments on the UI.
     * @param appointments The list of appointments to be displayed.
     */
    public void showPhysioAppointments(List<Appointment> appointments) {
        physioPnAppointments.getChildren().clear();
        for (Appointment appointment : appointments) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gadeadiaz/physiocare/appointment_item.fxml")
            );
            try {
                Node node = loader.load();
                AppointmentItemController controller = loader.getController();

                String questionImage = "/com/gadeagiaz/physiocare/images/question.jpg";
                String acceptAppointmentImage = "/com/gadeagiaz/physiocare/images/ok_appointment.jpg";

                controller.setAppointmentId(appointment.getId());
//                if(appointment.getConfirmed()){
//                    controller.getImgAppointment().setImage(new Image(acceptAppointmentImage));
//                }else{
//                    controller.getImgAppointment().setImage(new Image(questionImage));
//                }


                controller.getLblDate().setText(appointment.getDate());
                controller.getLblDiagnosis().setText(appointment.getDiagnosis());

                controller.getLblPhysioName().setText(appointment.getPhysio().getName() + " " + appointment.getPhysio().getSurname());

                node.setOnMouseEntered(_ -> node.setStyle("-fx-background-color : #0A0E3F"));
                node.setOnMouseExited(_ -> node.setStyle("-fx-background-color : #02030A"));

                controller.setAppointmentDetailListener(_ -> {
//                    selectedAppointment = appointment;
//                    selectedPatient = null;
//                    selectedPhysio = null;
                    System.out.println(appointment);
                });

                physioPnAppointments.getChildren().add(node);
            } catch (IOException e) {
                System.out.println("Appointment loader fail: " + e.getMessage());
            }
        }
    }


    public void showCreateAppointmentForm() {
        showAppointmentsFormPanel();
        PatientService.getPatients("").thenAccept(patients ->
                Platform.runLater(() -> {
                    cBoxPatients.setValue(patients.get(0));
                    cBoxPatients.setItems(FXCollections.observableList(patients));
                })
        ).exceptionally(e -> {
            RequestErrorException ex = (RequestErrorException) e;
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() ->
                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
            );
            return null;
        });
        PhysioService.getPhysios("").thenAccept(patients ->
                Platform.runLater(() -> {
                    cBoxPhysios.setValue(patients.get(0));
                    cBoxPhysios.setItems(FXCollections.observableList(patients));
                })
        ).exceptionally(e -> {
            RequestErrorException ex = (RequestErrorException) e;
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() ->
                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
            );
            return null;
        });
        pnlAppointmentForm.setVisible(true);
    }

    public void btnAppointmentFormClick() {
        LocalDate date = dpDate.getValue();
        String diagnosis = tfDiagnosis.getText();
        String treatment = tfTreatment.getText();
        String observations = tfObservations.getText();
        int physioId = cBoxPhysios.getSelectionModel().getSelectedItem().getId();
        int patientId = cBoxPatients.getSelectionModel().getSelectedItem().getId();

        if (date == null) {
            Message.showError("Error in form", "Date can not be empty");
            return;
        }
        if (date.isBefore(LocalDate.now())) {
            Message.showError("Error in form", "Date can not be before today");
            return;
        }
        if (diagnosis.length() < 10) {
            Message.showError(
                    "Error in form",
                    "Diagnosis length must be greater or equals than 10 characters"
            );
            return;
        }
        if (diagnosis.length() > 500) {
            Message.showError(
                    "Error in form",
                    "Diagnosis length must be lower or equals than 500 characters"
            );
            return;
        }
        if (treatment.length() > 150) {
            Message.showError(
                    "Error in form",
                    "Treatment length must be lower or equals then 150 characters"
            );
            return;
        }
        if (observations.length() > 500) {
            Message.showError(
                    "Error in form",
                    "Observations length must be lower or equals than 500 characters"
            );
            return;
        }

        AppointmentPOSTRequest appointmentPOSTRequest = new AppointmentPOSTRequest(
                date.toString(), diagnosis, treatment, observations, physioId, patientId
        );
        AppointmentService.createAppointment(appointmentPOSTRequest).thenAccept(appointment -> {
            //TODO(Redirigir a detalle appointment)
            System.out.println(appointment);
        }).exceptionally(e -> {
            e.printStackTrace();
            RequestErrorException ex = (RequestErrorException) e;
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() ->
                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
            );
            return null;
        });
    }


    public void showRecords(List<Record> records) {
        pnItems.getChildren().clear();
        for (Record record: records) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gadeadiaz/physiocare/record_item.fxml")
            );
            try {
                Node node = loader.load();
                RecordItemController recordItemController = loader.getController();

                recordItemController.setRecordId(record.getId());
                recordItemController.setDetailListener(recordId -> {
                    // TODO(Redirigir a vista detalle record con el id para hacer la peticion)
                });
                if (record.getMedicalRecord().isEmpty()) {
                    recordItemController.setBtnAddMedicalRecordVisibility(true);
                    recordItemController.setAddMedicalRecordListener(recordId -> {
                        // TODO(Redirigir formulario para editar el medical record, este boton solo
                        //  aparecera si el medical record esta vacio)
                    });
                }
                recordItemController.setLblRecordPatientText(
                        record.getPatient().getName() + " " + record.getPatient().getSurname()
                );
                recordItemController.setLblMedicalRecordText(record.getMedicalRecord());

                node.setOnMouseEntered(_ -> node.setStyle("-fx-background-color : #0A0E3F"));
                node.setOnMouseExited(_ -> node.setStyle("-fx-background-color : #02030A"));

                /*controller.setDetailListener(_ -> {
                    selectedPhysio = physio;
                    selectedPatient = null;
                    showPhysio();
                });

                controller.setDeleteListener(_ -> {
                    selectedPhysio = physio;
                    deletePhysio();
                });*/

                pnItems.getChildren().add(node);
            } catch (Exception e) {
                System.out.println("Patient loader fail: " + e.getMessage());
            }
        }
        txtRecordsCount.setText(String.valueOf(records.size()));
    }

    public void getRecords() {
        btnAddUser.setVisible(false);
        showUsersListPanel();
        RecordService.getRecords("").thenAccept(records -> {
            selectedListEntity = Entity.RECORD;
            Platform.runLater(() -> showRecords(records));
        }).exceptionally(e -> {
            RequestErrorException ex = (RequestErrorException) e.getCause();
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() ->
                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
            );
            return null;
        });
    }



    /**
     * Opens the login view in the specified stage.
     * Handles any potential IOExceptions when loading the view.
     *
     * @param stage the primary stage of the application
     */
    public static void openLoginView(Stage stage) {
        try {
            SceneLoader.loadScreen("login.fxml", stage);
        } catch (IOException ex) {
            stage.setOnCloseRequest(_ -> stage.close());
        }
    }

    /**
     * Handles button click events for navigation between patients, physios, and user actions.
     * Calls the appropriate methods based on the source of the action event.
     *
     * @param actionEvent the action event triggered by the user
     */
    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnPatients) {
            hBoxRecordList.setVisible(false);
            hBoxUserList.setVisible(true);
            lblTitle.setText("Patients");
            lblInsuranceLicenseNumList.setText("Insurance Number");
            getPatients();
        }
        if (actionEvent.getSource() == btnPhysios) {
            hBoxRecordList.setVisible(false);
            hBoxUserList.setVisible(true);
            lblTitle.setText("Physios");
            lblInsuranceLicenseNumList.setText("License Number");
            getPhysios();
        }
        if (actionEvent.getSource() == btnRecords) {
            hBoxUserList.setVisible(false);
            hBoxRecordList.setVisible(true);
            lblTitle.setText("Records");
            getRecords();
        }

        if (actionEvent.getSource() == btnAddUser && selectedListEntity == Entity.PATIENT ) {
            showCreatePatientForm();
        }

        if (actionEvent.getSource() == btnAddUser && selectedListEntity == Entity.PHYSIO ) {
            showCreatePhysioForm();
        }

        if (actionEvent.getSource() == btnAddAppointment) {
            showCreateAppointmentForm();
        }

        if(actionEvent.getSource() == btnLogout) {
            Storage.getInstance().clearData();
            openLoginView(stage);
        }
    }

    /**
     * Sets up the listener for selecting a specialty for the physio.
     * When a specialty is selected from the dropdown, it updates the specialty in the form.
     */
    public void specialtyListener() {
        for(MenuItem item : splitSpecialty.getItems()){
            item.setOnAction(_ -> {
                selectedPhysio.setSpecialty(item.getText());
                splitSpecialty.setText(selectedPhysio.getSpecialty());
                txtAddressPatForm.setText(selectedPhysio.getSpecialty());
            });
        }
    }

    /**
     * Sets up a listener for when the application window is closed. It opens the login view when the window is closed.
     *
     * @param stage the stage to attach the close listener
     */
    @Override
    public void setOnCloseListener(Stage stage) {
        stage.setOnCloseRequest(e -> {
            openLoginView(stage);
            e.consume();
        });
    }

    /**
     * Sets the primary stage for the application.
     *
     * @param stage the primary stage of the application
     */
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void editPatientClick() {
        showEditPatientForm();
    }

    public void editPhysioClick() {
        System.out.println("Edit physio");
    }

    public void searchClick() {
        if(!txtSearch.getText().trim().isEmpty()){
            if(selectedListEntity.equals(Entity.PATIENT)){
                getPatientsBySurname();
            }
            if(selectedListEntity.equals(Entity.PHYSIO)){
                getPhysiosBySpecialty();
            }
        }
    }

}


//    public void postPhysio(){
//        String nick = tfNickPhysioForm.getText();
//        String password = tfPasswordPhysioForm.getText();
//        String name = tfNamePhysioForm.getText();
//        String surname = tfSurnamePhysioForm.getText();
//        String specialty = cBoxSpecialtyPhysioForm.getValue();
//        String licenseNumber = tfLicenseNumberPhysioForm.getText();
//        String email = tfEmailPhysioForm.getText();
//
//        if (nick.length() < 4) {
//            Message.showError(
//                    "Validation error",
//                    "Nick length must be greater or equals then 4 characters"
//            );
//            return;
//        }
//        if (password.length() < 7) {
//            Message.showError(
//                    "Validation error",
//                    "Password length must be greater or equals then 7 characters"
//            );
//            return;
//        }
//        if (name.length() < 2) {
//            Message.showError(
//                    "Validation error",
//                    "Name length must be greater or equal than 2 characters"
//            );
//            return;
//        }
//        if (name.length() > 50) {
//            Message.showError(
//                    "Validation error",
//                    "Name length must be lower or equal than 50 characters"
//            );
//            return;
//        }
//        if (surname.length() < 2) {
//            Message.showError(
//                    "Validation error",
//                    "Surname length must be greater or equal than 2 characters"
//            );
//            return;
//        }
//        if (surname.length() > 50) {
//            Message.showError(
//                    "Validation error",
//                    "Surname length must be lower or equal than 50 characters"
//            );
//            return;
//        }
//        if (!licenseNumber.matches("^[0-9A-Z]{8}$")) {
//            Message.showError(
//                    "Validation error",
//                    "License number must be composed of 8 characters (numbers and capital letters)"
//            );
//            return;
//        }
//        if (email.length() > 75) {
//            Message.showError(
//                    "Validation error",
//                    "Email length must be lower or equals than 75 characters"
//            );
//            return;
//        }
//        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
//            Message.showError(
//                    "Validation error",
//                    "Email do not have a correct email format"
//            );
//            return;
//        }
//
//        PhysioPOSTRequest physioPOSTRequest = new PhysioPOSTRequest(
//                new User(nick, password, "patient"),
//                new Physio(name, surname, specialty, licenseNumber, email)
//        );
//        PhysioService.create(physioPOSTRequest).thenAccept(physio -> {
//            System.out.println(physio);
//            // TODO(Redirigir a detail physio)
//        }).exceptionally(e -> {
//            RequestErrorException ex = (RequestErrorException) e;
//            ErrorResponse errorResponse = ex.getErrorResponse();
//            System.out.println("Holaa");
//            Platform.runLater(() ->
//                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
//            );
//            return null;
//        });
//    }