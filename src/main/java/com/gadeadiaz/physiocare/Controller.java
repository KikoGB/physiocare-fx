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
import com.itextpdf.layout.Document;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller implements CloseController {



    @FXML private ScrollPane recordScrollPaneAppointments;
    @FXML private VBox vBoxRecordAppointments;
    // --- LEFT BAR MENU ---
    @FXML private Label lblLeftBarWelcome;
    @FXML private Button btnLeftBarLogout;
    @FXML private Button btnLeftBarPatients;
    @FXML private Button btnLeftBarPhysios;
    @FXML private Button btnLeftBarRecords;
    @FXML private Button btnLeftBarAddAppointment;

    // -----------------------------------------------------------------------------!!! mirar estos de donde son


    // --- BOTONES ---
    @FXML private Button btnAddUser;

    // --- ITEMS LIST PAGE ---
    @FXML private Label lblTitle;
    @FXML private Label txtPhysiosCount;
    @FXML private Label txtPatientsCount;
    @FXML private Label txtRecordsCount;
    @FXML private Label lblInsuranceLicenseNumList;
    @FXML private TextField txtSearch;

    // --- PANES ---
    @FXML private Pane pnlUsersList;
    @FXML private Pane pnlPatientDetail;
    @FXML private Pane pnlPatientForm;
    @FXML private Pane pnlPhysioDetail;
    @FXML private Pane pnlPhysioForm;
    @FXML private Pane pnlAppointmentForm;
    @FXML private Pane pnlRecordDetail;

    // --- CONTENEDORES ---
    @FXML private HBox hBoxUserList;
    @FXML private HBox hBoxRecordList;
    @FXML private VBox pnItems;
    @FXML private VBox vBoxPatientAppointments;
    @FXML private VBox vBoxPhysioAppointments;
    // ---------------------------------------------------------------!!!!!!!!!!!!!!!!!! hasta aqui

    // --- PATIENT DETAIL ---
    @FXML private Label lblTitlePatientDetail;
    @FXML private Label lblEmailPatientDetail;
    @FXML private Label lblAddressPatientDetail;
    @FXML private Label lblInsuranceNumPatientDetail;
    @FXML private Label lblBirthdatePatientDetail;
    @FXML private ScrollPane patientScrollPaneAppointments;
    @FXML private Label lblNoPatientAppointments;

    // --- PHYSIO DETAIL ---
    @FXML private Label lblTitlePhysioDetail;
    @FXML private Button btnEditPhysio;
    @FXML private Label lblLicenceNumPhysioDetail;
    @FXML private Label lblSpecialtyPhysioDetail;
    @FXML private Label lblEmailPhysioDetail;

    @FXML private Label lblNoPhysioAppointments;
    @FXML private ScrollPane physioScrollPaneAppointments;

    // --- PATIENT FORM ---
    @FXML private Label lblPatientFormTitle;
    @FXML private Label lblNickPatientForm;
    @FXML private TextField tfNickPatientForm;
    @FXML private Label lblPasswordPatientForm;
    @FXML private TextField tfPasswordPatientForm;
    @FXML private TextField tfNamePatientForm;
    @FXML private TextField tfEmailPatientForm;
    @FXML private TextField tfSurnamePatientForm;
    @FXML private TextField tfAddressPatientForm;
    @FXML private TextField tfInsuranceNumPatientForm;
    @FXML private DatePicker dpBirthdatePatientForm;

    // --- PHYSIO FORM ---
    @FXML private Label lblPhysioFormTitle;
    @FXML private TextField tfNickPhysioForm;
    @FXML private PasswordField tfPasswordPhysioForm;
    @FXML private TextField tfNamePhysioForm;
    @FXML private TextField tfSurnamePhysioForm;
    @FXML private TextField tfLicenseNumberPhysioForm;
    @FXML private TextField tfEmailPhysioForm;
    @FXML private Label lblPasswordPhysioForm;
    @FXML private Label lblNickPhysioForm;
    @FXML private ComboBox<String> cBoxSpecialtyPhysioForm;

    // --- APPOINTMENTS FORM ---
    @FXML private DatePicker dpDateAppointmentForm;
    @FXML private TextField tfDiagnosisAppointmentForm;
    @FXML private TextField tfTreatmentAppointmentForm;
    @FXML private TextField tfObservationsAppointmentForm;
    @FXML private ComboBox<Physio> cBoxPhysiosAppointmentForm;
    @FXML private ComboBox<Patient> cBoxPatientsAppointmentForm;


    // --- RECORD DETAIL ---
    @FXML private Label lblTitleRecordDetail;
    @FXML private Label lblEmailRecordDetail;
    @FXML private Label lblInsuranceNumRecordDetail;
    @FXML private Label lblRecordDescription;
    @FXML private Label lblNoRecordAppointments;
    @FXML private ImageView ivRecord; //Patient image

    // --- LÓGICA AUXILIAR ---
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    private final Gson gson = new Gson();
    private Stage stage;
    private Patient selectedPatient;
    private Physio selectedPhysio;
    private Appointment selectedAppointment;
    private Record selectedRecord;

    // --- ENUMS Y VARIABLES DE ESTADO ---
    private enum Entity { PATIENT, PHYSIO, RECORD, APPOINTMENT }
    private Entity selectedListEntity = Entity.PATIENT;


    public void initialize() {
        if (Storage.getInstance().getUserdata().getValue().equals("physio")) {
            PhysioService.getPhysioLogged().thenAccept(physio ->
                    Platform.runLater(() -> lblLeftBarWelcome.setText(
                            String.format("Welcome back %s %s", physio.getName(), physio.getSurname())
                    ))
            ).exceptionally(e -> {
                RequestErrorException ex = (RequestErrorException) e.getCause();
                ErrorResponse errorResponse = ex.getErrorResponse();
                Platform.runLater(() ->
                        Message.showError(errorResponse.getError(), errorResponse.getMessage())
                );
                return null;
            });
        } else {
            lblLeftBarWelcome.setText("Welcome back admin");
        }

        getPatients();
    }

    public void showUsersListPanel(){
        clearPatientForm();
        clearAppointmentForm();
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
        clearAppointmentForm();
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
        clearAppointmentForm();
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
        clearAppointmentForm();
        pnlPatientForm.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlPhysioDetail.setVisible(true);
        pnlPhysioDetail.toFront();
    }

    public void showPatientFormPanel(){
        clearPhysioForm();
        clearAppointmentForm();
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
        clearAppointmentForm();
        pnlPatientForm.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlAppointmentForm.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlPhysioForm.setVisible(true);
        pnlPhysioForm.toFront();
    }

    public void showRecordDetailPanel(){
        clearPatientForm();
        clearPhysioForm();
        pnlPatientForm.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlRecordDetail.setVisible(true);
        pnlRecordDetail.toFront();
    }




//    ---------- PATIENTS ----------
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
                        PatientService.delete(idPatient).thenAccept(_ -> {
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

    public void showPatientDetail(Patient patient) {
        showPatientDetailPanel();
        lblTitlePatientDetail.setText(patient.getName() + " " + patient.getSurname());
        lblEmailPatientDetail.setText(patient.getEmail());
        if (patient.getAddress().isEmpty()) {
            lblAddressPatientDetail.setText("Address not indicated");
        } else {
            lblAddressPatientDetail.setText(patient.getAddress());
        }
        lblInsuranceNumPatientDetail.setText(patient.getInsuranceNumber());
        lblBirthdatePatientDetail.setText(patient.getBirthdate());
        getPatientAppointments(patient.getId());
    }

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
            .thenAccept(patient ->
                Platform.runLater(() -> {
                    selectedPatient = patient;
                    showPatientDetail(patient);
                    System.out.println(selectedPatient);
                })
            ).exceptionally(e -> {
                RequestErrorException ex = (RequestErrorException) e.getCause();
                ErrorResponse errorResponse = ex.getErrorResponse();
                Platform.runLater(() ->
                        Message.showError(errorResponse.getError(), errorResponse.getMessage())
                );
                return null;
            });
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

    public void showPatientForm(Patient patient) {
        clearPatientForm();
        if (patient != null) {
            lblNickPatientForm.setVisible(false);
            tfNickPatientForm.setVisible(false);
            lblPasswordPatientForm.setVisible(false);
            tfPasswordPatientForm.setVisible(false);

            lblPatientFormTitle.setText("Edit patient " + patient.getName() + " " + patient.getSurname());
            tfNamePatientForm.setText(patient.getName());
            tfSurnamePatientForm.setText(patient.getSurname());
            dpBirthdatePatientForm.setValue(LocalDate.parse(patient.getBirthdate()));
            tfAddressPatientForm.setText(patient.getAddress());
            tfInsuranceNumPatientForm.setText(patient.getInsuranceNumber());
            tfEmailPatientForm.setText(patient.getEmail());
        }

        showPatientFormPanel();
    }

    public void btnSavePatient() {
        if (selectedPatient == null) {
            createPatient();
        } else {
            updatePatient();
        }
    }

    public boolean isValidPatientForm(boolean isEdit) {
        String nick = tfNickPatientForm.getText();
        String password = tfPasswordPatientForm.getText();
        String name = tfNamePatientForm.getText();
        String surname = tfSurnamePatientForm.getText();
        LocalDate birthDate = dpBirthdatePatientForm.getValue();
        String email = tfEmailPatientForm.getText();
        String address = tfAddressPatientForm.getText();
        String insuranceNumber = tfInsuranceNumPatientForm.getText();

        StringBuilder errorBuilder = new StringBuilder();

        if (!isEdit) {
            if (nick.length() < 4) {
                errorBuilder.append("- Nick must be at least 4 characters long\n");
            }
            if (password.length() < 7) {
                errorBuilder.append("- Password must be at least 7 characters long\n");
            }
        }
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
        } else if (!birthDate.isBefore(LocalDate.now())) {
            errorBuilder.append("- The birth date must be before today\n");
        }
        if (!errorBuilder.isEmpty()) {
            Message.showError("Validation error", errorBuilder.toString());
            return false;
        }

        return true;
    }

    public void clearPatientForm() {
        for (Node child: pnlPatientForm.getChildren()) {
            if (child instanceof TextField) {
                ((TextField) child).clear();
            }
        }
        dpBirthdatePatientForm.setValue(null);
    }

    public void clearAppointmentForm() {
        for (Node child: pnlAppointmentForm.getChildren()) {
            if (child instanceof TextField) {
                ((TextField) child).clear();
            }
        }
        dpDateAppointmentForm.setValue(null);
    }

    /**
     * Sends a POST request to create a new patient on the server.
     * If the request is successful, the list of patients is refreshed. If there is an error, an error message is displayed.
     */
    public void createPatient() {
        if (isValidPatientForm(false)) {
            Patient newPatient = new Patient(
                    tfNamePatientForm.getText(), tfSurnamePatientForm.getText(),
                    dpBirthdatePatientForm.getValue().format(formatter), tfAddressPatientForm.getText(),
                    tfInsuranceNumPatientForm.getText(), tfEmailPatientForm.getText()
            );

            User newUser = new User(
                    tfNickPatientForm.getText(), tfPasswordPatientForm.getText(), "patient"
            );

            PatientService.create(new PatientPOSTRequest(newUser, newPatient))
                .thenAccept(patient ->
                    Platform.runLater(() -> {
                        selectedPatient = patient;
                        clearPatientForm();
                        showPatientDetail(patient);
                    })
                ).exceptionally(e -> {
                    RequestErrorException ex = (RequestErrorException) e.getCause();
                    ErrorResponse errorResponse = ex.getErrorResponse();
                    Platform.runLater(() ->
                            Message.showError(errorResponse.getError(), errorResponse.getMessage())
                    );
                    return null;
                });
        }
    }

    /**
     * Sends a PUT request to update an existing patient's information on the server.
     * If the update is successful, the patient's details are displayed. Otherwise, an error message is shown.
     */
    public void updatePatient() {
        if (isValidPatientForm(true)) {
            Patient updatedPatient = new Patient(
                selectedPatient.getId(), tfNamePatientForm.getText(), tfSurnamePatientForm.getText(),
                dpBirthdatePatientForm.getValue().format(formatter), tfAddressPatientForm.getText(),
                tfInsuranceNumPatientForm.getText(), tfEmailPatientForm.getText()
            );

            PatientService.update(selectedPatient.getId(), updatedPatient)
                .thenAccept(patient ->
                    Platform.runLater(() -> {
                        clearPatientForm();
                        selectedPatient = patient;
                        showPatientDetail(patient);
                    })
                ).exceptionally(e -> {
                    RequestErrorException ex = (RequestErrorException) e.getCause();
                    ErrorResponse errorResponse = ex.getErrorResponse();
                    Platform.runLater(() ->
                            Message.showError(errorResponse.getError(), errorResponse.getMessage())
                    );
                    return null;
                });
        }
    }

//    ---------- PHYSIOS ----------
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
                            PhysioService.delete(idPhysio).thenAccept(_ -> {
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

    public void showPhysioDetail(Physio physio) {
        showPhysioDetailPanel();

        if (Storage.getInstance().getUserdata().getValue().equals("physio")) {
            btnEditPhysio.setVisible(false);
        }

        lblTitlePhysioDetail.setText(physio.getName() + " " + physio.getSurname());
        lblEmailPhysioDetail.setText(physio.getEmail());
        lblLicenceNumPhysioDetail.setText(physio.getLicenseNumber());
        lblSpecialtyPhysioDetail.setText(physio.getSpecialty());
        getPhysioAppointments(physio.getId());
    }

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
                .thenAccept(physio ->
                    Platform.runLater(() -> {
                        selectedPhysio = physio;
                        showPhysioDetail(physio);
                    })
                ).exceptionally(e -> {
                    RequestErrorException ex = (RequestErrorException) e.getCause();
                    ErrorResponse errorResponse = ex.getErrorResponse();
                    Platform.runLater(() ->
                            Message.showError(errorResponse.getError(), errorResponse.getMessage())
                    );
                    return null;
                });

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

    public void showPhysioForm(Physio physio) {
        clearPhysioForm();
        cBoxSpecialtyPhysioForm.setItems(
                FXCollections.observableList(
                        List.of("Sports", "Neurological", "Pediatric", "Geriatric", "Oncological")
                )
        );
        if (physio == null) {
            lblPhysioFormTitle.setText("New Physio");
            cBoxSpecialtyPhysioForm.setValue("Sports");
        } else {
            lblNickPhysioForm.setVisible(false);
            tfNickPhysioForm.setVisible(false);
            lblPasswordPhysioForm.setVisible(false);
            tfPasswordPhysioForm.setVisible(false);

            lblPhysioFormTitle.setText("Edit physio " + physio.getName() + " " + physio.getSurname());
            tfNamePhysioForm.setText(physio.getName());
            tfSurnamePhysioForm.setText(physio.getSurname());
            cBoxSpecialtyPhysioForm.setValue(physio.getSpecialty());
            tfLicenseNumberPhysioForm.setText(physio.getLicenseNumber());
            tfEmailPhysioForm.setText(physio.getEmail());
        }

        showPhysioFormPanel();
    }

    public void btnSavePhysio() {
        System.out.println(selectedPhysio);
        if (selectedPhysio == null) {
            createPhysio();
        } else {
            updatePhysio();
        }
    }

    public boolean isValidPhysioForm(boolean isEdit) {
        String nick = tfNickPhysioForm.getText();
        String password = tfPasswordPhysioForm.getText();
        String name = tfNamePhysioForm.getText();
        String surname = tfSurnamePhysioForm.getText();
        String specialty = cBoxSpecialtyPhysioForm.getValue();
        String licenseNumber = tfLicenseNumberPhysioForm.getText();
        String email = tfEmailPhysioForm.getText();

        StringBuilder errorBuilder = new StringBuilder();

        if (!isEdit) {
            if (nick.length() < 4) {
                errorBuilder.append("- Nick must be at least 4 characters long\n");
            }
            if (password.length() < 7) {
                errorBuilder.append("- Password must be at least 7 characters long\n");
            }
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
            Message.showError("Validation error", errorBuilder.toString());
            return false;
        }

        return true;
    }

    public void clearPhysioForm() {
        for (Node child: pnlPhysioForm.getChildren()) {
            if (child instanceof TextField) {
                ((TextField) child).clear();
            }
        }
    }

    private void createPhysio() {
        if (isValidPhysioForm(false)) {
            Physio newPhysio = new Physio(
                    tfNamePhysioForm.getText(), tfSurnamePhysioForm.getText(),
                    cBoxSpecialtyPhysioForm.getValue(), tfLicenseNumberPhysioForm.getText(),
                    tfEmailPhysioForm.getText()
            );
            User newUser =  new User(
                    tfNickPhysioForm.getText(), tfPasswordPhysioForm.getText(), "physio"
            );
            PhysioPOSTRequest physioPOSTRequest = new PhysioPOSTRequest(newUser, newPhysio);
            PhysioService.create(physioPOSTRequest).thenAccept(physio -> {
                //Los mensaje de error no van
                // Platform runlater no muestra los alert por algún motivo
                Platform.runLater(() -> {
                    selectedPhysio = physio;
                    clearPhysioForm();
                    showPhysioDetail(physio);
                });
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

    private void updatePhysio() {
        if (isValidPhysioForm(true)) {
            Physio physioToUpdate = new Physio(
                    selectedPhysio.getId(), tfNamePhysioForm.getText(),
                    tfSurnamePhysioForm.getText(), cBoxSpecialtyPhysioForm.getValue(),
                    tfLicenseNumberPhysioForm.getText(), tfEmailPhysioForm.getText()
            );
            PhysioService.update(selectedPhysio.getId(), physioToUpdate).thenAccept(physio ->
                Platform.runLater(() -> {
                    selectedPhysio = physio;
                    clearPhysioForm();
                    showPhysioDetail(physio);
                })
            ).exceptionally(e -> {
                RequestErrorException ex = (RequestErrorException) e;
                ErrorResponse errorResponse = ex.getErrorResponse();
                Platform.runLater(() ->
                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
                );
                return null;
            });
        }
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
        vBoxPatientAppointments.getChildren().clear();
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

                vBoxPatientAppointments.getChildren().add(node);
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
        vBoxPhysioAppointments.getChildren().clear();
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

                vBoxPhysioAppointments.getChildren().add(node);
            } catch (IOException e) {
                System.out.println("Appointment loader fail: " + e.getMessage());
            }
        }
    }

    public void getRecordAppointments(int patientId) {
        RecordService.getRecordAppointments(patientId).thenAccept(appointments -> {
            Platform.runLater(() -> {
                selectedListEntity = Entity.APPOINTMENT;
                recordScrollPaneAppointments.setVisible(true);
                lblNoRecordAppointments.setVisible(true);
                showRecordAppointments(appointments);
            });
        }).exceptionally(e -> {
            RequestErrorException ex = (RequestErrorException) e.getCause();
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() -> {
                lblNoRecordAppointments.setVisible(true);
                recordScrollPaneAppointments.setVisible(false);
//                Message.showError(errorResponse.getError(), errorResponse.getMessage()); Hay que ver que hacer con este error
            });
            return null;
        });
    }

    /**
     * Displays the list of appointments on the UI.
     * @param appointments The list of appointments to be displayed.
     */
    public void showRecordAppointments(List<Appointment> appointments) {
        vBoxRecordAppointments.getChildren().clear();
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

                vBoxRecordAppointments.getChildren().add(node);
            } catch (IOException e) {
                System.out.println("Appointment loader fail: " + e.getMessage());
            }
        }
    }

    public void showCreateAppointmentForm() {
        showAppointmentsFormPanel();
        PatientService.getPatients("").thenAccept(patients ->
                Platform.runLater(() -> {
                    cBoxPatientsAppointmentForm.setValue(patients.get(0));
                    cBoxPatientsAppointmentForm.setItems(FXCollections.observableList(patients));
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
                    cBoxPhysiosAppointmentForm.setValue(patients.get(0));
                    cBoxPhysiosAppointmentForm.setItems(FXCollections.observableList(patients));
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
        LocalDate date = dpDateAppointmentForm.getValue();
        String diagnosis = tfDiagnosisAppointmentForm.getText();
        String treatment = tfTreatmentAppointmentForm.getText();
        String observations = tfObservationsAppointmentForm.getText();
        int physioId = cBoxPhysiosAppointmentForm.getSelectionModel().getSelectedItem().getId();
        int patientId = cBoxPatientsAppointmentForm.getSelectionModel().getSelectedItem().getId();

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


    //    ---------- RECORDS ----------
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
                    selectedPhysio = null;
                    selectedPatient = null;
                    getRecordById(recordId);
                });
                if (record.getMedicalRecord().isEmpty()) {
                    recordItemController.setBtnAddMedicalRecordVisibility(true);
                    recordItemController.setAddMedicalRecordListener(recordId -> {
                        /* TODO(Redirigir formulario para editar el medical record, este boton solo
                            aparecera si el medical record esta vacio) Nota para David: Podria hacerse que solo se modifique el
                            texto del medical record desde su detalle? Así no haria falta la vista del form*/
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

//    Preguntar a David si lo de pasar el el objeto a mostra lo ha implementado el en todos los modelos
//    Se podria obviar y usar el selectedPatient, selectedRecord, etc...
    public void showRecordDetail(Record record) {
        showRecordDetailPanel();
        lblTitleRecordDetail.setText(record.getPatient().getName() + " " + record.getPatient().getSurname());
        lblEmailRecordDetail.setText(record.getPatient().getEmail());
        lblInsuranceNumRecordDetail.setText(record.getPatient().getInsuranceNumber());
        lblRecordDescription.setText(record.getMedicalRecord());
//        ivRecord -> Asignar la imágen del paciente
        getRecordAppointments(record.getId());
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

    public void getRecordById(int id) {
        RecordService.getRecordById(id)
                .thenAccept(record ->
                        Platform.runLater(() -> {
                            selectedRecord = record;
                            showRecordDetail(record);
                            System.out.println(selectedRecord);
                        })
                ).exceptionally(e -> {
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
        if (actionEvent.getSource() == btnLeftBarPatients) {
            hBoxRecordList.setVisible(false);
            hBoxUserList.setVisible(true);
            lblTitle.setText("Patients");
            lblInsuranceLicenseNumList.setText("Insurance Number");
            getPatients();
        }
        if (actionEvent.getSource() == btnLeftBarPhysios) {
            hBoxRecordList.setVisible(false);
            hBoxUserList.setVisible(true);
            lblTitle.setText("Physios");
            lblInsuranceLicenseNumList.setText("License Number");
            getPhysios();
        }
        if (actionEvent.getSource() == btnLeftBarRecords) {
            hBoxUserList.setVisible(false);
            hBoxRecordList.setVisible(true);
            lblTitle.setText("Records");
            getRecords();
        }

        if (actionEvent.getSource() == btnAddUser && selectedListEntity == Entity.PATIENT ) {
            showPatientForm(null);
        }

        if (actionEvent.getSource() == btnAddUser && selectedListEntity == Entity.PHYSIO ) {
            showPhysioForm(null);
        }

        if (actionEvent.getSource() == btnLeftBarAddAppointment) {
            showCreateAppointmentForm();
        }

        if(actionEvent.getSource() == btnLeftBarLogout) {
            Storage.getInstance().clearData();
            openLoginView(stage);
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
        showPatientForm(selectedPatient);
    }

    public void editPhysioClick() {
        showPhysioForm(selectedPhysio);
    }

    public void savePdfClick(ActionEvent actionEvent) {
//        Todo: Restringir en el caso de que esté vacío?
        Pdf.medicalRecordPdfCreator(selectedRecord);
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