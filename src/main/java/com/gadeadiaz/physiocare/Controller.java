package com.gadeadiaz.physiocare;

import com.gadeadiaz.physiocare.controllers.AppointmentItemController;
import com.gadeadiaz.physiocare.controllers.CloseController;
import com.gadeadiaz.physiocare.controllers.RecordItemController;
import com.gadeadiaz.physiocare.controllers.UserItemController;
import com.gadeadiaz.physiocare.exceptions.RequestErrorException;
import com.gadeadiaz.physiocare.models.*;
import com.gadeadiaz.physiocare.models.Record;
import com.gadeadiaz.physiocare.requests.PatientPOSTRequest;
import com.gadeadiaz.physiocare.models.Appointment;
import com.gadeadiaz.physiocare.models.Record;
import com.gadeadiaz.physiocare.requests.AppointmentPOSTRequest;
import com.gadeadiaz.physiocare.models.User;
import com.gadeadiaz.physiocare.requests.AppointmentPOSTRequest;
import com.gadeadiaz.physiocare.requests.PatientPOSTRequest;
import com.gadeadiaz.physiocare.responses.ErrorResponse;
import com.gadeadiaz.physiocare.services.AppointmentService;
import com.gadeadiaz.physiocare.services.PatientService;
import com.gadeadiaz.physiocare.services.PhysioService;
import com.gadeadiaz.physiocare.services.RecordService;
import com.gadeadiaz.physiocare.utils.*;
import com.gadeadiaz.physiocare.utils.Storage;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller implements CloseController {

    @FXML
    private Label lblDetailPatientTitle;
    @FXML
    private Label txtDetailEmail;
    @FXML
    private Label txtDetailAddress;
    @FXML
    private Label txtDetailInsuranceNumber;
    @FXML
    private Label txtDetailBirthDate;
    @FXML
    private Pane pnlPatientDetail;
    @FXML
    private TextField txtSearch;
    @FXML
    private Label lblBirthDate;
    @FXML
    private Label lblAddressAndSpecialty;
    @FXML
    private Button btnNewUser;
    @FXML
    private SplitMenuButton splitSpecialty;
    @FXML
    private Button btnUserForm;
    @FXML
    private Pane pnlUsersList;
    @FXML
    private Pane pnlUserForm;
    @FXML
    private RadioButton rbPatient;
    @FXML
    private RadioButton rbPhysio;
    @FXML
    private Label lblLogin;
    @FXML
    private Label lblDetailTitle;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private Label lblPassword;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtSurname;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private TextField txtAddressAndSpecialty;
    @FXML
    private TextField txtLogin;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnPatients;
    @FXML
    private Button btnPhysios;
    @FXML
    private Button btnRecords;
    @FXML
    private Button btnAddAppointment;
    @FXML
    private Label lblTitle;
    @FXML
    private Label txtPhysiosCount;
    @FXML
    private Label txtPatientsCount;
    @FXML
    private Label txtRecordsCount;
    @FXML
    private Label lblInsuranceLicenseNumList;
    @FXML
    private HBox hBoxUserList;
    @FXML
    private HBox hBoxRecordList;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TextField tfDiagnosis;
    @FXML
    private TextField tfTreatment;
    @FXML
    private TextField tfObservations;
    @FXML
    private ComboBox<Physio> cBoxPhysios;
    @FXML
    private ComboBox<Patient> cBoxPatients;
    @FXML
    private Pane pnlAppointmentForm;
    @FXML
    private VBox pnItems;
    @FXML
    private VBox pnAppointments;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Gson gson = new Gson();
    private Stage stage;
    private Patient selectedPatient;
    private Physio selectedPhysio;
    private Appointment selectedAppointment;

    private enum Entity { PATIENT, PHYSIO, RECORD, APPOINTMENT }
    private Entity selectedListEntity = Entity.PATIENT;

    public void initialize() {
        getPatients();
    }

    private void showUsersListPanel(){
        pnlUserForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUsersList.setVisible(true);
        pnlUsersList.toFront();
    }

    private void showAppointmentsFormPanel(){
        pnlUsersList.setVisible(false);
        pnlUserForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlAppointmentForm.setVisible(true);
        pnlAppointmentForm.toFront();
    }

    private void showUsersDetailPanel(){
        pnlUserForm.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUsersList.setVisible(false);
        pnlPatientDetail.setVisible(true);
        pnlPatientDetail.toFront();
    }

    /**
     * Shows the detail panel in the UI and hides the list panel.
     */
    private void showUsersFormPanel(){
        radioButtonsListener();
        specialtyListener();
        pnlUserForm.setVisible(true);
        pnlUsersList.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlUserForm.toFront();
    }

//    ---------- PATIENTS ----------
    private void getPatients() {
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
    private void showPatients(List<Patient> patients) {
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
                    selectedPatient = patient;
                    selectedPhysio = null;
                    showPatientDetail(patient);
//                    showPatientForm();
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
    private void getPatientsBySurname() {
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

    /**
     * Sends a POST request to create a new patient on the server.
     * If the request is successful, the list of patients is refreshed. If there is an error, an error message is displayed.
     */
    private void postPatient() {
        if(dpBirthDate.getValue() == null){
            Message.showError("Empty Birth Date", "Value of birth date cannot be empty!");
        } else {
            PatientPOSTRequest newPatient = createPatientPOSTResponse();

            PatientService.postPatient(newPatient)
                .thenAccept(_ -> {
                    selectedListEntity = Entity.PATIENT;
                    getPatients();
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
    /**
     * Creates the request body for a new patient based on the current form data.
     *
     * @return a map containing the key-value pairs for the patient data
     */
    private PatientPOSTRequest createPatientPOSTResponse() {
        User newUser = new User(txtLogin.getText(), txtPassword.getText(), "patient");
        Patient patient = new Patient(
                txtName.getText(),
                txtSurname.getText(),
                dpBirthDate.getValue().format(formatter),
                txtAddressAndSpecialty.getText(),
                txtLogin.getText(),
                txtEmail.getText());
        System.out.println(patient.getBirthdate());

        return new PatientPOSTRequest(newUser, patient);
    }

    /**
     * Sends a PUT request to update an existing patient's information on the server.
     * If the update is successful, the patient's details are displayed. Otherwise, an error message is shown.
     */
    private void putPatient() {
        /*if(dpBirthDate.getValue() == null){
            Message.showError("Empty Birth Date", "Value of birth date cannot be empty!");
        } else {
            Map<String, Object> postBody = createPatientRequestBody();

            String apiUrl = ServiceUtils.SERVER + "patients/" + selectedPatient.getId();
            String jsonRequest = gson.toJson(postBody);

            ServiceUtils.getResponseAsync(apiUrl, jsonRequest, "PUT")
                    .thenApply(json -> gson.fromJson(json, PatientResponse.class)
                    )
                    .thenAccept(response -> {
                        if (response.isOk()) {
                            Platform.runLater(() -> {
                                selectedPatient = response.getResult();
                                showPatient();
                            });
                        } else {
                            Platform.runLater(() -> Message.showError("Error updating Patient", response.getError()));
                        }
                    })
                    .exceptionally(ex -> {
                        Platform.runLater(() -> {
                            Message.showError("Error", "Error updating patient");
                        });
                        return null;
                    });
        }*/
    }

    /**
     * Deletes the selected patient from the server and updates the patient list.
     * If the deletion fails, an error message is displayed.
     */
    private void deletePatient() {
        /*String apiUrl = ServiceUtils.SERVER + "patients/" + selectedPatient.getId();
        ServiceUtils.getResponseAsync(apiUrl, null, "DELETE")
                .thenApply(json -> gson.fromJson(json, PatientResponse.class))
                .thenAccept(response -> {
                    if (response.isOk()) {
                        Platform.runLater(this::getPatients);
                    } else {
                        Platform.runLater(() -> Message.showError("Delete patient error", response.getError()));
                    }
                })
                .exceptionally(ex -> {
                    System.out.println("Error deleting patients -> " + ex.getMessage());
                    Platform.runLater(() -> {
                        Message.showError("Delete patient error", "Failed to delete patient");
                        openLoginView(stage);
                    });
                    return null;
                });*/
    }

    /**
     * Displays detailed information of the currently selected patient.
     * Sets text fields and labels with patient data and adjusts the UI components to display the patient's details.
     */
    private void showPatientForm() {
        showUsersFormPanel();
        editFormTextFields(false);

        lblDetailTitle.setText(selectedPatient.getName() + " " + selectedPatient.getSurname());
        txtName.setText(selectedPatient.getName());
        txtSurname.setText(selectedPatient.getSurname());
        txtEmail.setText(selectedPatient.getEmail());
        lblAddressAndSpecialty.setText("Address");
        txtAddressAndSpecialty.setText(selectedPatient.getAddress());
        lblLogin.setText("Insurance Number");
        txtLogin.setText(selectedPatient.getInsuranceNumber());
        lblPassword.setVisible(false);
        txtPassword.setVisible(false);
        rbPatient.setVisible(false);
        rbPhysio.setVisible(false);
        splitSpecialty.setVisible(false);
        dpBirthDate.setDisable(true);
        dpBirthDate.setEditable(false);
        btnUserForm.setText("EDIT");
    }

    private void showPatientDetail(Patient patient){

        showUsersDetailPanel();
        lblDetailPatientTitle.setText(patient.getName() + " " + patient.getSurname());
        txtDetailEmail.setText(patient.getEmail());
        if(patient.getAddress().isEmpty()){
            txtDetailAddress.setText("Address not indicated");
        }else{
            txtDetailAddress.setText(patient.getAddress());
        }
        txtDetailInsuranceNumber.setText(patient.getInsuranceNumber());
        txtDetailBirthDate.setText(patient.getBirthdate());
        getAppointments(patient.getId());
    }

//    ---------- PHYSIOS ----------
    /**
     * Fetches the list of physiotherapists from the server and updates the UI with the retrieved data.
     * If there is an error, an error message is displayed.
     */
    private void getPhysios() {
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

    /**
     * Displays the list of physiotherapists on the UI.
     * @param physios The list of physiotherapists to be displayed.
     */
    private void showPhysios(List<Physio> physios) {
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
                    selectedPhysio = physio;
                    selectedPatient = null;
                    showPhysio();
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


    private void getPhysiosBySpecialty() {
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

    /**
     * Sends a POST request to create a new physio on the server.
     * If the request is successful, the list of physios is refreshed. If there is an error, an error message is displayed.
     */
    private void postPhysio() {
        /*Map<String, Object> postBody = createPhysioRequestBody();

        String apiUrl = ServiceUtils.SERVER + "physios";
        String jsonRequest = gson.toJson(postBody);

        ServiceUtils.getResponseAsync(apiUrl, jsonRequest, "POST")
                .thenApply(json -> gson.fromJson(json, PhysioResponse.class)
                )
                .thenAccept(response -> {
                    if (response.isOk()) {
                        Platform.runLater(this::getPhysios);
                    } else {
                        Platform.runLater(() -> Message.showError("Error Creating Physio", response.getError()));
                    }
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        Message.showError("Error", "Error creating Physio");
                        openLoginView(stage);
                    });
                    return null;
                });*/
    }

    /**
     * Sends a PUT request to update an existing physio's information on the server.
     * If the update is successful, the physio's details are displayed. Otherwise, an error message is shown.
     */
    private void putPhysio() {
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



    /**
     * Creates the request body for a new physio based on the current form data.
     *
     * @return a map containing the key-value pairs for the physio data
     */
    private Map<String, Object> createPhysioRequestBody() {
        Map<String, Object> physioReqBody = new HashMap<>();
        physioReqBody.put("name", txtName.getText());
        physioReqBody.put("surname", txtSurname.getText());
        physioReqBody.put("specialty", txtAddressAndSpecialty.getText());
        physioReqBody.put("licenseNumber", txtLogin.getText());
        physioReqBody.put("email", txtEmail.getText());
        physioReqBody.put("password", txtPassword.getText());
        return physioReqBody;
    }

    /**
     * Deletes the currently selected physio by making an asynchronous DELETE request to the server.
     * If the deletion is successful, the list of physios is refreshed. Otherwise, an error message is shown.
     */
    private void deletePhysio() {
        /*String apiUrl = ServiceUtils.SERVER + "physios/" + selectedPhysio.getId();
        ServiceUtils.getResponseAsync(apiUrl, null, "DELETE")
                .thenApply(json -> gson.fromJson(json, PhysioResponse.class)
                )
                .thenAccept(response -> {
                    if (response.isOk()) {
                        Platform.runLater(this::getPhysios);
                    } else {
                        Platform.runLater(() -> Message.showError("Error deleting physio", response.getError()));
                    }
                })
                .exceptionally(ex -> {
                    System.out.println("Error deleting physio -> " + ex.getMessage());
                    Platform.runLater(() -> {
                        Message.showError("Error deleting physio", "Failed to delete physio");
                        openLoginView(stage);
                    });
                    return null;
                });*/
    }

//    ---------- APPOITNMENTS ----------
    private void getAppointments(int patientId) {
        PatientService.getPatientAppointments(patientId).thenAccept(appointments -> {
            selectedListEntity = Entity.APPOINTMENT;
            Platform.runLater(() -> showAppointments(appointments));
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
     * Displays the list of appointments on the UI.
     * @param appointments The list of appointments to be displayed.
     */
    private void showAppointments(List<Appointment> appointments) {
        pnAppointments.getChildren().clear();
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

                pnAppointments.getChildren().add(node);
            } catch (IOException e) {
                System.out.println("Appointment loader fail: " + e.getMessage());
            }
        }
        txtPhysiosCount.setText(String.valueOf(appointments.size()));
    }

    public void showAppointmentForm() {
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

    public void createAppointment() {
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

    /**
     * Displays detailed information of the currently selected physio.
     * Sets text fields and labels with physio data and adjusts the UI components to display the physio's details.
     */
    private void showPhysio() {
        showUsersFormPanel();
        editFormTextFields(false);

        lblDetailTitle.setText(selectedPhysio.getName() + " " + selectedPhysio.getSurname());
        txtName.setText(selectedPhysio.getName());
        txtSurname.setText(selectedPhysio.getSurname());
        txtEmail.setText(selectedPhysio.getEmail());
        lblAddressAndSpecialty.setText("Specialty");
        txtAddressAndSpecialty.setText(selectedPhysio.getSpecialty());
        lblLogin.setText("Insurance Number");
        txtLogin.setText(selectedPhysio.getLicenseNumber());
        dpBirthDate.setVisible(false);
        lblBirthDate.setVisible(false);
        lblPassword.setVisible(false);
        txtPassword.setVisible(false);
        rbPatient.setVisible(false);
        rbPhysio.setVisible(false);
        splitSpecialty.setVisible(false);

        btnUserForm.setText("EDIT");
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

    private void getRecords() {
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

        if (actionEvent.getSource() == btnAddAppointment) {
            showAppointmentForm();
        }

        if (actionEvent.getSource() == btnNewUser) {
            showAddUserForm();
        }

        if(actionEvent.getSource() == btnLogout) {
            Storage.getInstance().clearData();
            openLoginView(stage);
        }
    }

    /**
     * Displays the form for adding a new user by resetting the text fields and setting up default values.
     * It also switches to the appropriate panel for user creation.
     */
    private void showAddUserForm() {
        for (Node child: pnlUserForm.getChildren()) {
            if(child instanceof TextField){
                ((TextField) child).setText("");
            }
        }
        rbPatient.setSelected(true);
        selectedPatient = new Patient();
        selectedPhysio = new Physio();
        editFormTextFields(true);
        splitSpecialty.setVisible(false);
        btnUserForm.setText("CREATE");
        showUsersFormPanel();
    }

    /**
     * Enables or disables the editing capability of text fields in the form, based on the provided boolean parameter.
     *
     * @param editable if true, enables text fields; if false, disables them
     */
    private void editFormTextFields(boolean editable) {
        for (Node child: pnlUserForm.getChildren()) {
            child.setVisible(true);
            if(child instanceof TextField){
                ((TextField) child).setEditable(editable);
            }
        }
    }

    /**
     * Sets up the listeners for the radio buttons (Patient/Physio) to dynamically change the form's properties.
     * Based on the selected radio button, it adjusts the form to either show patient-specific or physio-specific fields.
     */
    public void radioButtonsListener() {
        ToggleGroup group = new ToggleGroup();
        rbPatient.setToggleGroup(group);
        rbPhysio.setToggleGroup(group);

        // Add listeners for each radio button
        group.selectedToggleProperty().addListener((_, _, newValue) -> {
            if (newValue == rbPatient) {
                showAddPatientProperties();
            } else if (newValue == rbPhysio) {
                showAddPhysioProperties();
            }
        });
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
                txtAddressAndSpecialty.setText(selectedPhysio.getSpecialty());
            });
        }
    }

    /**
     * Displays the form properties specific to adding a new patient.
     * Configures fields and labels that are unique to the patient creation process.
     */
    private void showAddPatientProperties() {
        lblDetailTitle.setText("New Patient");
        lblLogin.setText("Insurance Number");
        lblAddressAndSpecialty.setText("Address");
        txtAddressAndSpecialty.setText("");
        dpBirthDate.setVisible(true);
        splitSpecialty.setVisible(false);
    }

    /**
     * Displays the form properties specific to adding a new physio.
     * Configures fields and labels that are unique to the physio creation process.
     */
    private void showAddPhysioProperties() {
        lblDetailTitle.setText("New Physio");
        lblLogin.setText("License Number");
        splitSpecialty.setVisible(true);
        lblAddressAndSpecialty.setText("Specialty");
        txtAddressAndSpecialty.setText("Sports");
        lblBirthDate.setVisible(false);
        dpBirthDate.setVisible(false);
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

    /**
     * Handles the actions for the detail button in the form.
     * Depending on the current state of the button (CREATE, SAVE, EDIT), it performs the appropriate action (create, update, or edit) for either a patient or physio.
     */
    public void btnUserFormClick() {
        if(btnUserForm.getText().equals("CREATE") ) {
            if(selectedPatient.getName() == null && dpBirthDate.isVisible()){
                postPatient();
            }

            /*if(selectedPhysio.getId() == null && splitSpecialty.isVisible()){
                postPhysio();
            }*/
        } else if(btnUserForm.getText().equals("SAVE")) {
            /*if(selectedPatient != null && selectedPatient.getId() != null){
                putPatient();
            }
            if(selectedPhysio != null && selectedPhysio.getId() != null){
                putPhysio();
            }*/
        } else if (btnUserForm.getText().equals("EDIT")) {
            btnUserForm.setText("SAVE");
            editFormTextFields(true);
            rbPatient.setVisible(false);
            rbPhysio.setVisible(false);
            lblPassword.setVisible(false);
            txtPassword.setVisible(false);
            if(selectedPhysio != null){
                splitSpecialty.setVisible(true);
                dpBirthDate.setVisible(false);
                lblBirthDate.setVisible(false);
            } else if(selectedPatient != null){
                splitSpecialty.setVisible(false);
                dpBirthDate.setEditable(true);
                dpBirthDate.setDisable(false);
            }
        }
    }

    public void btnAppointmentForm(ActionEvent actionEvent) {
    }



    public void searchClick(MouseEvent mouseEvent) {
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