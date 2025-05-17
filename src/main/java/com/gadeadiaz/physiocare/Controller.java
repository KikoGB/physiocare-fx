package com.gadeadiaz.physiocare;

import com.gadeadiaz.physiocare.controllers.AppointmentItemController;
import com.gadeadiaz.physiocare.controllers.CloseController;
import com.gadeadiaz.physiocare.controllers.RecordItemController;
import com.gadeadiaz.physiocare.controllers.UserItemController;
import com.gadeadiaz.physiocare.exceptions.RequestErrorException;
import com.gadeadiaz.physiocare.models.Record;
import com.gadeadiaz.physiocare.models.*;
import com.gadeadiaz.physiocare.requests.*;
import com.gadeadiaz.physiocare.responses.ErrorResponse;
import com.gadeadiaz.physiocare.services.AppointmentService;
import com.gadeadiaz.physiocare.services.PatientService;
import com.gadeadiaz.physiocare.services.PhysioService;
import com.gadeadiaz.physiocare.services.RecordService;
import com.gadeadiaz.physiocare.utils.*;
import jakarta.mail.MessageAware;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller implements CloseController {
    // --- LEFT BAR MENU ---
    @FXML private Circle circleAvatarLoggedPhysioLeftBar;
    @FXML private Label lblWelcomeLeftBar;
    @FXML private Button btnMyProfileLeftBar;
    @FXML private Button btnAddAppointmentLeftBar;

    // -----------------------------------------------------------------------------!!! mirar estos de donde son
    // --- BOTONES ---
    @FXML private Button btnAddUser;
    @FXML private Button btnSendPayRolls;
//    @FXML private Button btnPhysioForm;
//    @FXML private Button btnPatientForm;

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
    @FXML private Pane pnlAppointmentDetail;

    // --- CONTENEDORES ---
    @FXML private HBox hBoxUserList;
    @FXML private HBox hBoxRecordList;
    @FXML private VBox vBoxItems;
    // ---------------------------------------------------------------!!!!!!!!!!!!!!!!!! hasta aqui

    // --- PATIENT DETAIL ---
    @FXML private Label lblTitlePatientDetail;
    @FXML private Button btnEditPatient;
    @FXML private Label lblEmailPatientDetail;
    @FXML private Label lblAddressPatientDetail;
    @FXML private Label lblInsuranceNumPatientDetail;
    @FXML private Label lblBirthdatePatientDetail;
    @FXML private ScrollPane patientScrollPaneAppointments;
    @FXML private VBox vBoxPatientAppointments;
    @FXML private Label lblNoPatientAppointments;

    // --- PHYSIO DETAIL ---
    @FXML private Label lblTitlePhysioDetail;
    @FXML private Button btnEditPhysio;
    @FXML private Label lblLicenceNumPhysioDetail;
    @FXML private Label lblSpecialtyPhysioDetail;
    @FXML private Label lblEmailPhysioDetail;
    @FXML private ScrollPane physioScrollPaneAppointments;
    @FXML private VBox vBoxPhysioAppointments;
    @FXML private Label lblNoPhysioAppointments;

    // --- RECORD DETAIL ---
    @FXML private Button btnEditRecordDetail;
    @FXML private Button btnSavePdfRecordDetail;
    @FXML private Label lblTitleRecordDetail;
    @FXML private Label lblEmailRecordDetail;
    @FXML private Label lblInsuranceNumRecordDetail;
    @FXML private Label lblRecordDescription;
    @FXML private Label lblNoRecordAppointments;
    @FXML private ImageView ivRecord; //Patient image
    @FXML private ScrollPane recordScrollPaneAppointments;
    @FXML private VBox vBoxRecordAppointments;

    // --- APPOINTMENT DETAIL ---
    @FXML private Label lblTitleAppointmentDetail;
    @FXML private Label lblDateAppointmentDetail;
    @FXML private Label lblDiagnosisAppointmentDetail;
    @FXML private Label lblTreatmentAppointmentDetail;
    @FXML private Label lblObservationsAppointmentDetail;
    @FXML private Label lblPatientAppointmentDetail;
    @FXML private Label lblPhysioAppointmentDetail;
    @FXML private Label lblConfirmedAppointmentDetail;
    @FXML private Button btnEditAppointmentDetail;

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
    @FXML private Button btnSendPatientForm;

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
    @FXML private Button btnSendPhysioForm;

    // --- APPOINTMENTS FORM ---
    @FXML private Label lblTitleAppointmentForm;
    @FXML private DatePicker dpDateAppointmentForm;
    @FXML private TextField tfDiagnosisAppointmentForm;
    @FXML private TextField tfTreatmentAppointmentForm;
    @FXML private TextField tfObservationsAppointmentForm;
    @FXML private ComboBox<Physio> cBoxPhysiosAppointmentForm;
    @FXML private ComboBox<Patient> cBoxPatientsAppointmentForm;
    @FXML private Label lblPatientAppointmentForm;
    @FXML private Label lblPhysioAppointmentForm;
    @FXML private Button btnAppointmentForm;

    // --- LÓGICA AUXILIAR ---
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Stage stage;
    // Physio object representing the data of the logged physio
    private Physio loggedPhysio;
    private enum Entity { PATIENT, PHYSIO, RECORD }
    private Entity selectedListEntity = Entity.PATIENT;

    private void startServiceSendPatientsEmails() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            PatientService.getPatientsWithAllData().thenAccept(Email::sendPatientsEmails)
                    .exceptionally(e -> {
                        RequestErrorException ex = (RequestErrorException) e;
                        ErrorResponse errorResponse = ex.getErrorResponse();
                        return null;
                    });
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void initialize() {
//        startServiceSendPatientsEmails();
        if (Storage.getInstance().getUserdata().getValue().equals("physio")) {
            PhysioService.getPhysioLogged().thenAccept(physio -> {
                loggedPhysio = physio;
                Platform.runLater(() -> {
                    lblWelcomeLeftBar.setText(
                            String.format("Welcome back %s %s", physio.getName(), physio.getSurname())
                    );
                    btnMyProfileLeftBar.setOnMouseClicked(_ -> showPhysioDetail(physio));
                    try {
                        circleAvatarLoggedPhysioLeftBar.setFill(
                                new ImagePattern(
                                        new Image(String.valueOf(new URL(physio.getAvatar())))
                                )
                        );
                    } catch (IOException _) {}
                });
            }).exceptionally(e -> {
                RequestErrorException ex = (RequestErrorException) e.getCause();
                ErrorResponse errorResponse = ex.getErrorResponse();
                Platform.runLater(() ->
                        Message.showError(errorResponse.getError(), errorResponse.getMessage())
                );
                return null;
            });
        } else {
            circleAvatarLoggedPhysioLeftBar.setStroke(Color.ALICEBLUE);
            circleAvatarLoggedPhysioLeftBar.setFill(
                    new ImagePattern(
                            new Image(
                                    String.valueOf(
                                            getClass().getResource(
                                                    "/com/gadeadiaz/physiocare/images/admin_logo.png"
                                            )
                                    )
                            )
                    )
            );
            lblWelcomeLeftBar.setText("Welcome back admin");
            btnMyProfileLeftBar.setVisible(false);
        }

        btnAddAppointmentLeftBar.setOnMouseClicked(_ -> showAppointmentForm(null));

        getPatients();
    }

    public void showUsersListPanel() {
        clearPatientForm();
        clearAppointmentForm();
        clearPhysioForm();
        //Primero los que se ocultan
        vBoxItems.getChildren().clear();
        pnlPatientForm.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlAppointmentForm.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlRecordDetail.setVisible(false);
        pnlAppointmentDetail.setVisible(false);
        //Por ultimo, el que se muestra
        pnlUsersList.setVisible(true);
        pnlUsersList.toFront();
    }

    public void showAppointmentsFormPanel() {
        pnlUsersList.setVisible(false);
        pnlPatientForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlRecordDetail.setVisible(false);
        pnlAppointmentDetail.setVisible(false);
        pnlAppointmentForm.setVisible(true);
        pnlAppointmentForm.toFront();
    }

    public void showPatientDetailPanel() {
        clearPatientForm();
        clearPhysioForm();
        clearAppointmentForm();
        vBoxPatientAppointments.getChildren().clear();
        pnlPatientForm.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlRecordDetail.setVisible(false);
        pnlAppointmentDetail.setVisible(false);
        pnlPatientDetail.setVisible(true);
        pnlPatientDetail.toFront();
    }

    public void showPhysioDetailPanel() {
        clearPatientForm();
        clearPhysioForm();
        clearAppointmentForm();
        vBoxPhysioAppointments.getChildren().clear();
        pnlPatientForm.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlRecordDetail.setVisible(false);
        pnlAppointmentDetail.setVisible(false);
        pnlPhysioDetail.setVisible(true);
        pnlPhysioDetail.toFront();
    }

    public void showPatientFormPanel() {
        pnlPhysioForm.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlAppointmentForm.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlRecordDetail.setVisible(false);
        pnlAppointmentDetail.setVisible(false);
        pnlPatientForm.setVisible(true);
        pnlPatientForm.toFront();
    }

    public void showPhysioFormPanel() {
        pnlPatientForm.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlAppointmentForm.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlRecordDetail.setVisible(false);
        pnlAppointmentDetail.setVisible(false);
        pnlPhysioForm.setVisible(true);
        pnlPhysioForm.toFront();
    }

    public void showRecordDetailPanel() {
        clearPatientForm();
        clearPhysioForm();
        clearAppointmentForm();
        vBoxRecordAppointments.getChildren().clear();
        pnlPatientForm.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlAppointmentForm.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlAppointmentDetail.setVisible(false);
        pnlRecordDetail.setVisible(true);
        pnlRecordDetail.toFront();
    }

    public void showAppointmentDetailPanel() {
        clearPatientForm();
        clearPhysioForm();
        clearAppointmentForm();
        pnlPatientForm.setVisible(false);
        pnlPhysioForm.setVisible(false);
        pnlAppointmentForm.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPhysioDetail.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlRecordDetail.setVisible(false);
        pnlAppointmentDetail.setVisible(true);
        pnlAppointmentDetail.toFront();
    }


//    ---------- PATIENTS ----------
    public void showPatients(List<Patient> patients) {
        for (Patient patient : patients) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gadeadiaz/physiocare/user_item.fxml")
            );
            try {
                Node node = loader.load();
                UserItemController userItemController = loader.getController();

                userItemController.setUserId(patient.getId());
                userItemController.setLblNameText(patient.getName());
                userItemController.setLblSurnameText(patient.getSurname());
                userItemController.setLblEmailText(patient.getEmail());
                userItemController.setLblLicenseInsuranceNumberText(patient.getInsuranceNumber());

                node.setOnMouseEntered(_ -> node.setStyle("-fx-background-color : #0A0E3F"));
                node.setOnMouseExited(_ -> node.setStyle("-fx-background-color : #02030A"));

                userItemController.setDetailListener(_ -> getPatientById(patient.getId()));

                userItemController.setDeleteListener(idPatient ->
                        PatientService.delete(idPatient).thenAccept(_ ->
                            Platform.runLater(() -> {
                                vBoxItems.getChildren().remove(node);
                                Message.showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Delete",
                                        "Patient deleted",
                                        "Patient successfully deleted"
                                );
                            })
                        ).exceptionally(e -> {
                            RequestErrorException ex = (RequestErrorException) e;
                            ErrorResponse errorResponse = ex.getErrorResponse();
                            Platform.runLater(() ->
                                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
                            );
                            return null;
                        })
                );

                vBoxItems.getChildren().add(node);
            } catch (IOException e) {
                System.out.println("Patient loader fail: " + e.getMessage());
            }
        }
        txtPatientsCount.setText(String.valueOf(patients.size()));
    }

    public void showPatientDetail(Patient patient) {
        showPatientDetailPanel();
        btnEditPatient.setOnMouseClicked(_ -> showPatientForm(patient));
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
        btnSendPayRolls.setVisible(false);
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
                .thenAccept(patient -> Platform.runLater(() -> showPatientDetail(patient)))
                .exceptionally(e -> {
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
        if (patient == null) {
            btnSendPatientForm.setOnMouseClicked(_ -> createPatient());
            lblNickPatientForm.setVisible(true);
            tfNickPatientForm.setVisible(true);
            lblPasswordPatientForm.setVisible(true);
            tfPasswordPatientForm.setVisible(true);
            lblPatientFormTitle.setText("Add patient");
        } else {
            btnSendPatientForm.setOnMouseClicked(_ -> updatePatient(patient));
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
                    .thenAccept(patient -> Platform.runLater(() -> showPatientDetail(patient)))
                    .exceptionally(e -> {
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
    public void updatePatient(Patient patient) {
        if (isValidPatientForm(true)) {
            Patient updatedPatient = new Patient(
                patient.getId(), tfNamePatientForm.getText(), tfSurnamePatientForm.getText(),
                dpBirthdatePatientForm.getValue().format(formatter), tfAddressPatientForm.getText(),
                tfInsuranceNumPatientForm.getText(), tfEmailPatientForm.getText()
            );

            PatientService.update(patient.getId(), updatedPatient)
                    .thenAccept(patientUpdated -> Platform.runLater(() -> showPatientDetail(patientUpdated)))
                    .exceptionally(e -> {
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
        for (Physio physio : physios) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gadeadiaz/physiocare/user_item.fxml")
            );
            try {
                Node node = loader.load();
                UserItemController userItemController = loader.getController();

                userItemController.setUserId(physio.getId());
                userItemController.setLblNameText(physio.getName());
                userItemController.setLblSurnameText(physio.getSurname());
                userItemController.setLblEmailText(physio.getEmail());
                userItemController.setLblLicenseInsuranceNumberText(physio.getLicenseNumber());

                node.setOnMouseEntered(_ -> node.setStyle("-fx-background-color : #0A0E3F"));
                node.setOnMouseExited(_ -> node.setStyle("-fx-background-color : #02030A"));

                userItemController.setDetailListener(_ -> getPhysioById(physio.getId()));

                if (Storage.getInstance().getUserdata().getValue().equals("physio")) {
                    userItemController.setBtnDeleteVisibility(false);

                    userItemController.setDeleteListener(idPhysio ->
                            PhysioService.deletePhysio(idPhysio).thenAccept(_ ->
                                Platform.runLater(() -> {
                                    vBoxItems.getChildren().remove(node);
                                    Message.showMessage(
                                            Alert.AlertType.CONFIRMATION,
                                            "Delete",
                                            "Physio deleted",
                                            "Physio successfully deleted"
                                    );
                                })
                            ).exceptionally(e -> {
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

                vBoxItems.getChildren().add(node);
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

        btnEditPhysio.setOnMouseClicked(_ -> showPhysioForm(physio));
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
        if (Storage.getInstance().getUserdata().getValue().equals("physio")) {
            btnAddUser.setVisible(false);
        } else {
            btnAddUser.setVisible(true);
            btnSendPayRolls.setVisible(true);
        }
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
                .thenAccept(physio -> Platform.runLater(() -> showPhysioDetail(physio)))
                .exceptionally(e -> {
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
        Platform.runLater(() -> cBoxSpecialtyPhysioForm.setItems(
                FXCollections.observableList(
                        List.of("Sports", "Neurological", "Pediatric", "Geriatric", "Oncological")
                )
        ));
        if (physio == null) {
            btnSendPhysioForm.setOnMouseClicked(_ -> createPhysio());
            lblNickPhysioForm.setVisible(true);
            tfNickPhysioForm.setVisible(true);
            lblPasswordPhysioForm.setVisible(true);
            tfPasswordPhysioForm.setVisible(true);
            lblPhysioFormTitle.setText("Add physio");
            cBoxSpecialtyPhysioForm.setValue("Sports");
        } else {
            btnSendPhysioForm.setOnMouseClicked(_ -> updatePhysio(physio));
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
            PhysioService.createPhysio(physioPOSTRequest).thenAccept(physio ->
                    Platform.runLater(() -> showPhysioDetail(physio))
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

    private void updatePhysio(Physio physio) {
        if (isValidPhysioForm(true)) {
            Physio physioToUpdate = new Physio(
                    physio.getId(), tfNamePhysioForm.getText(),
                    tfSurnamePhysioForm.getText(), cBoxSpecialtyPhysioForm.getValue(),
                    tfLicenseNumberPhysioForm.getText(), tfEmailPhysioForm.getText()
            );
            PhysioService.updatePhysio(physio.getId(), physioToUpdate).thenAccept(physioUpdated ->
                Platform.runLater(() -> showPhysioDetail(physioUpdated))
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



//    ---------- APPOINTMENTS ----------
    /**
     * Displays the list of appointments on the UI.
     *
     * @param appointments The list of appointments to be displayed.
     * @param entity The entity displayed in the detail view PATIENT, PHYSIO, RECORD
     * @param physioId The ID of the physio displayed, this parameter is only necessary when is a physio
     *                 the object displayed. This is used to allow physios to accept or deny only their
     *                 appointments. If the object displayed is not a physio put 0 in this parameter
     */
    public void showAppointments(List<Appointment> appointments, Controller.Entity entity, int physioId) {
        VBox vBoxUsed = null;
        switch (entity) {
            case PATIENT -> vBoxUsed = vBoxPatientAppointments;
            case PHYSIO -> vBoxUsed = vBoxPhysioAppointments;
            case RECORD -> vBoxUsed = vBoxRecordAppointments;
        }

        for (Appointment appointment : appointments) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gadeadiaz/physiocare/appointment_item.fxml")
            );
            try {
                Node node = loader.load();
                AppointmentItemController appointmentItemController = loader.getController();

                String icAcceptedAppointment = Objects.requireNonNull(
                        getClass().getResource("/com/gadeadiaz/physiocare/images/ok_appointment.png")
                ).toString();

                appointmentItemController.setAppointment(appointment);

                String rol = Storage.getInstance().getUserdata().getValue();
                if (rol.equals("physio") && loggedPhysio.getId() != physioId) {
                    appointmentItemController.getBtnAcceptAppointmentItem().setVisible(false);
                    appointmentItemController.getBtnDenyAppointmentItem().setVisible(false);
                    appointmentItemController.getHBoxPhysio().setPrefWidth(200);
                    appointmentItemController.getHBoxPhysio().setVisible(true);
                }

                if (appointment.getConfirmed()) {
                    appointmentItemController.setIvAppointmentItemImage(new Image(icAcceptedAppointment));
                    appointmentItemController.getBtnAcceptAppointmentItem().setVisible(false);
                    appointmentItemController.getBtnDenyAppointmentItem().setVisible(false);
                    appointmentItemController.getHBoxPhysio().setPrefWidth(200);
                    appointmentItemController.getHBoxPhysio().setVisible(true);
                }

                appointmentItemController.setLblDateText(appointment.getDate());
                appointmentItemController.setLblDiagnosisText(appointment.getDiagnosis());

                appointmentItemController.setLblPhysioNameText(
                        appointment.getPhysio().getName() + " " + appointment.getPhysio().getSurname()
                );

                node.setOnMouseEntered(_ -> node.setStyle("-fx-background-color : #0A0E3F"));
                node.setOnMouseExited(_ -> node.setStyle("-fx-background-color : #02030A"));

                appointmentItemController.setShowAppointmentDetailCallback(appointmentToShow -> {
                    Platform.runLater(() -> showAppointmentDetail(appointmentToShow));
                });

                appointmentItemController.setAcceptAppointmentCallback(appointmentId ->
                        AppointmentService.confirmAppointment(appointmentId).thenAccept(_ ->
                                Platform.runLater(() -> {
                                    appointmentItemController.setIvAppointmentItemImage(
                                            new Image(icAcceptedAppointment)
                                    );
                                    appointmentItemController.getBtnAcceptAppointmentItem().setVisible(false);
                                    appointmentItemController.getBtnDenyAppointmentItem().setVisible(false);
                                    appointmentItemController.getHBoxPhysio().setPrefWidth(200);
                                    appointmentItemController.getHBoxPhysio().setVisible(true);
                                    Message.showMessage(
                                            Alert.AlertType.INFORMATION,
                                            "Confirmed",
                                            "Appointment confirmed",
                                            "Appointment successfully confirmed"
                                    );
                                })
                        ).exceptionally(e -> {
                            RequestErrorException ex = (RequestErrorException) e;
                            ErrorResponse errorResponse = ex.getErrorResponse();
                            Platform.runLater(() ->
                                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
                            );
                            return null;
                        })
                );

                VBox finalVBoxUsed = vBoxUsed;
                appointmentItemController.setDenyAppointmentCallback(appointmentId ->
                        AppointmentService.deleteAppointment(appointmentId).thenAccept(_ -> {
                            Platform.runLater(() -> {
                                finalVBoxUsed.getChildren().remove(node);
                                Message.showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Denied",
                                        "Appointment denied",
                                        "Appointment successfully denied"
                                );
                            });
                        }).exceptionally(e -> {
                            RequestErrorException ex = (RequestErrorException) e;
                            ErrorResponse errorResponse = ex.getErrorResponse();
                            Platform.runLater(() ->
                                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
                            );
                            return null;
                        })
                );

                vBoxUsed.getChildren().add(node);
            } catch (IOException e) {
                System.out.println("Appointment loader fail: " + e.getMessage());
            }
        }
    }

    public void getPatientAppointments(int patientId) {
        PatientService.getPatientAppointments(patientId).thenAccept(appointments -> {
            Platform.runLater(() -> {
                patientScrollPaneAppointments.setVisible(true);
                lblNoPatientAppointments.setVisible(true);
                showAppointments(appointments, Entity.PATIENT, 0);
            });
        }).exceptionally(e -> {
            RequestErrorException ex = (RequestErrorException) e.getCause();
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() -> {
                lblNoPatientAppointments.setVisible(true);
                patientScrollPaneAppointments.setVisible(false);
            });
            return null;
        });
    }

    public void getPhysioAppointments(int physioId) {
        PhysioService.getPhysioAppointments(physioId).thenAccept(appointments -> {
            Platform.runLater(() -> {
                physioScrollPaneAppointments.setVisible(true);
                lblNoPhysioAppointments.setVisible(true);
                showAppointments(appointments, Entity.PHYSIO, physioId);
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

    public void getRecordAppointments(int recordId) {
        RecordService.getRecordAppointments(recordId).thenAccept(appointments -> {
            Platform.runLater(() -> {
                recordScrollPaneAppointments.setVisible(true);
                lblNoRecordAppointments.setVisible(true);
                showAppointments(appointments, Entity.RECORD, 0);
            });
        }).exceptionally(e -> {
            RequestErrorException ex = (RequestErrorException) e.getCause();
            ErrorResponse errorResponse = ex.getErrorResponse();
            Platform.runLater(() -> {
                lblNoRecordAppointments.setVisible(true);
                recordScrollPaneAppointments.setVisible(false);
            });
            return null;
        });
    }

    public void showAppointmentDetail(Appointment appointment) {
        showAppointmentDetailPanel();
        btnEditAppointmentDetail.setOnMouseClicked(_ -> showAppointmentForm(appointment));
        lblTitleAppointmentDetail.setText(
                String.format(
                        "Appointment of %s with %s",
                        appointment.getPatient().getName() + " " + appointment.getPatient().getSurname(),
                        appointment.getPhysio().getName() + " " + appointment.getPhysio().getSurname()
                )
        );
        lblDateAppointmentDetail.setText(appointment.getDate());
        lblDiagnosisAppointmentDetail.setText(appointment.getDiagnosis());
        lblTreatmentAppointmentDetail.setText(appointment.getTreatment());
        lblObservationsAppointmentDetail.setText(appointment.getObservations());
        lblPatientAppointmentDetail.setText(
                appointment.getPatient().getName() + " " + appointment.getPatient().getSurname()
        );
        lblPhysioAppointmentDetail.setText(
                appointment.getPhysio().getName() + " " + appointment.getPhysio().getSurname()
        );
        lblConfirmedAppointmentDetail.setText(appointment.getConfirmed()? "Yes":"No");
    }

    public void showAppointmentForm(Appointment appointment) {
        clearAppointmentForm();
        if (appointment == null) {

            btnAppointmentForm.setOnMouseClicked(_ -> sendAppointmentForm(null));
            lblPhysioAppointmentForm.setVisible(true);
            cBoxPhysiosAppointmentForm.setVisible(true);
            lblPatientAppointmentForm.setVisible(true);
            cBoxPatientsAppointmentForm.setVisible(true);
            lblTitleAppointmentForm.setText("Add appointment");
            PatientService.getPatients("").thenAccept(patients ->
                    Platform.runLater(() -> {
                        cBoxPatientsAppointmentForm.setValue(patients.get(0));
                        cBoxPatientsAppointmentForm.setItems(FXCollections.observableList(patients));
                    })
            ).exceptionally(e -> {
                // en este caso hay que redirigir en caso de error a una de las vistas por defecto por
                // ejemplo patient o physios
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
                // en este caso hay que redirigir en caso de error a una de las vistas por defecto por
                // ejemplo patient o physios
                RequestErrorException ex = (RequestErrorException) e;
                ErrorResponse errorResponse = ex.getErrorResponse();
                Platform.runLater(() ->
                        Message.showError(errorResponse.getError(), errorResponse.getMessage())
                );
                return null;
            });
        } else {
            btnAppointmentForm.setOnMouseClicked(_ -> sendAppointmentForm(appointment));
            lblPhysioAppointmentForm.setVisible(false);
            cBoxPhysiosAppointmentForm.setVisible(false);
            lblPatientAppointmentForm.setVisible(false);
            cBoxPatientsAppointmentForm.setVisible(false);

            lblTitleAppointmentForm.setText("Edit appointment");
            btnAppointmentForm.setText("Save");
            dpDateAppointmentForm.setValue(LocalDate.parse(appointment.getDate()));
            tfDiagnosisAppointmentForm.setText(appointment.getDiagnosis());
            tfTreatmentAppointmentForm.setText(appointment.getTreatment());
            tfObservationsAppointmentForm.setText(appointment.getObservations());
        }
        showAppointmentsFormPanel();
    }

    private boolean isValidAppointmentForm() {
        LocalDate date = dpDateAppointmentForm.getValue();
        String diagnosis = tfDiagnosisAppointmentForm.getText();
        String treatment = tfTreatmentAppointmentForm.getText();
        String observations = tfObservationsAppointmentForm.getText();

        StringBuilder errorBuilder = new StringBuilder();

        if (date == null) {
            errorBuilder.append("- Date can not be empty\n");
        }
        if (date != null && date.isBefore(LocalDate.now())) {
            errorBuilder.append("- Date can not be before today\n");
        }
        if (diagnosis.length() < 10) {
            errorBuilder.append("- Diagnosis length must be greater or equals than 10 characters\n");
        }
        if (diagnosis.length() > 500) {
            errorBuilder.append("- Diagnosis length must be lower or equals than 500 characters\n");
        }
        if (treatment.length() > 150) {
            errorBuilder.append("- Treatment length must be lower or equals then 150 characters\n");
        }
        if (observations.length() > 500) {
            errorBuilder.append("- Observations length must be lower or equals than 500 characters\n");
        }

        if (!errorBuilder.isEmpty()) {
            Message.showError("Validation error", errorBuilder.toString());
            return false;
        }

        return true;
    }

    public void sendAppointmentForm(Appointment appointment) {
        LocalDate date = dpDateAppointmentForm.getValue();
        String diagnosis = tfDiagnosisAppointmentForm.getText();
        String treatment = tfTreatmentAppointmentForm.getText();
        String observations = tfObservationsAppointmentForm.getText();
        int patientId = 0, physioId = 0;
        if (appointment == null) {
            patientId = cBoxPatientsAppointmentForm.getSelectionModel().getSelectedItem().getId();
            physioId = cBoxPhysiosAppointmentForm.getSelectionModel().getSelectedItem().getId();
        }

        if (isValidAppointmentForm()) {
            if (appointment != null) {
                AppointmentPUTRequest appointmentPUTRequest = new AppointmentPUTRequest(
                        appointment.getId(), date.toString(), diagnosis, treatment, observations,
                        appointment.getPatient().getId(), appointment.getPhysio().getId()
                );
                AppointmentService.updateAppointment(appointment.getId(), appointmentPUTRequest)
                        .thenAccept(appointmentUpdated ->
                                Platform.runLater(() -> showAppointmentDetail(appointmentUpdated))
                        ).exceptionally(e -> {
                            RequestErrorException ex = (RequestErrorException) e;
                            ErrorResponse errorResponse = ex.getErrorResponse();
                            Platform.runLater(() ->
                                    Message.showError(errorResponse.getError(), errorResponse.getMessage())
                            );
                            return null;
                        });
            } else {
                AppointmentPOSTRequest appointmentPOSTRequest = new AppointmentPOSTRequest(
                        date.toString(), diagnosis, treatment, observations, patientId, physioId
                );
                AppointmentService.createAppointment(appointmentPOSTRequest)
                        .thenAccept(appointmentUpdated ->
                                Platform.runLater(() -> showAppointmentDetail(appointmentUpdated))
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
    }


    //    ---------- RECORDS ----------
    public void showRecords(List<Record> records) {
        for (Record record: records) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gadeadiaz/physiocare/record_item.fxml")
            );
            try {
                Node node = loader.load();
                RecordItemController recordItemController = loader.getController();

                recordItemController.setRecord(record);

                recordItemController.setDetailListener(this::getRecordById);

                if (record.getMedicalRecord().isEmpty()) {
                    recordItemController.setBtnAddMedicalRecordVisibility(true);
                    recordItemController.setShowRecordFormCallback(this::updateRecord);
                }
                recordItemController.setLblRecordPatientText(
                        record.getPatient().getName() + " " + record.getPatient().getSurname()
                );
                recordItemController.setLblMedicalRecordText(record.getMedicalRecord());

                node.setOnMouseEntered(_ -> node.setStyle("-fx-background-color : #0A0E3F"));
                node.setOnMouseExited(_ -> node.setStyle("-fx-background-color : #02030A"));

                vBoxItems.getChildren().add(node);
            } catch (Exception e) {
                System.out.println("Patient loader fail: " + e.getMessage());
            }
        }
        txtRecordsCount.setText(String.valueOf(records.size()));
    }

    public void showRecordDetail(Record record) {
        showRecordDetailPanel();
        btnEditRecordDetail.setOnMouseClicked(_ -> updateRecord(record));
        btnSavePdfRecordDetail.setOnMouseClicked(_ -> saveRecordPdf(record));
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
                .thenAccept(record -> Platform.runLater(() -> showRecordDetail(record)))
                .exceptionally(e -> {
                    RequestErrorException ex = (RequestErrorException) e.getCause();
                    ErrorResponse errorResponse = ex.getErrorResponse();
                    Platform.runLater(() ->
                            Message.showError(errorResponse.getError(), errorResponse.getMessage())
                    );
                    return null;
                });
    }

    private void updateRecord(Record record) {
        TextInputDialog dialogRecordForm = new TextInputDialog(record.getMedicalRecord());
        dialogRecordForm.setTitle("Edit record");
        dialogRecordForm.setHeaderText("Add or edit medical record of this record");
        Optional<String> optMedicalRecord = dialogRecordForm.showAndWait();
        if (optMedicalRecord.isPresent()) {
            RecordPUTRequest recordPUTRequest = new RecordPUTRequest(record.getId(), optMedicalRecord.get());
            RecordService.updateRecord(record.getId(), recordPUTRequest)
                    .thenAccept(recordToUpdate -> Platform.runLater(() -> showRecordDetail(recordToUpdate)))
                    .exceptionally(e -> {
                        RequestErrorException ex = (RequestErrorException) e;
                        ErrorResponse errorResponse = ex.getErrorResponse();
                        Platform.runLater(() ->
                            Message.showError(errorResponse.getError(), errorResponse.getMessage())
                        );
                        return null;
                    });
        }
    }

    /**
     * Opens the login view in the specified stage.
     * Handles any potential IOExceptions when loading the view.
     *
     */
    public void openLoginView() {

        try {
            SceneLoader.loadScreen("login.fxml", new Stage(), true);
            this.stage.close();
        } catch (IOException e) {
            System.out.println("Show login error");
        }
    }

    // mirar los nombres estos ya que son los mismo que los metodos que pintas los elementos
    public void showPatients() {
        hBoxRecordList.setVisible(false);
        hBoxUserList.setVisible(true);
        lblTitle.setText("Patients");
        lblInsuranceLicenseNumList.setText("Insurance Number");
        getPatients();
    }

    public void showPhysios() {
        hBoxRecordList.setVisible(false);
        hBoxUserList.setVisible(true);
        lblTitle.setText("Physios");
        lblInsuranceLicenseNumList.setText("License Number");
        getPhysios();
    }

    public void showRecords() {
        hBoxUserList.setVisible(false);
        hBoxRecordList.setVisible(true);
        lblTitle.setText("Records");
        getRecords();
    }

    public void showUserForm() {
        if (selectedListEntity == Entity.PATIENT) {
            showPatientForm(null);
        }
        if (selectedListEntity == Entity.PHYSIO) {
            showPhysioForm(null);
        }
    }

    public void logout() {
        Storage.getInstance().clearData();
        openLoginView();
    }

    public void saveRecordPdf(Record record) {
        Pdf.medicalRecordPdfCreator(record);
    }

    public void sendPayRollsClick() {
        PhysioService.getPhysiosWithAllData().thenAccept(physios -> {
            Platform.runLater(() -> {
                Email.sendPhysiosEmails(physios);
                Message.showMessage(
                        Alert.AlertType.INFORMATION,
                        "Well Done",
                        "Payrolls sent successfully",
                        "Payrolls have been sent to all employees");
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

    public void searchClick() {
        if (!txtSearch.getText().trim().isEmpty()) {
            if (selectedListEntity.equals(Entity.PATIENT)) {
                getPatientsBySurname();
            }
            if (selectedListEntity.equals(Entity.PHYSIO)) {
                getPhysiosBySpecialty();
            }
        }
    }

    @Override
    public void setOnCloseListener(Stage stage) {
        stage.setOnCloseRequest(e -> {
            stage.close();
            e.consume();
        });
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}