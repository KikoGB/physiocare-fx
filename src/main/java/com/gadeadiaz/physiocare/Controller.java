package com.gadeadiaz.physiocare;

import com.gadeadiaz.physiocare.controllers.AppointmentItemController;
import com.gadeadiaz.physiocare.controllers.CloseController;
import com.gadeadiaz.physiocare.controllers.UserItemController;
import com.gadeadiaz.physiocare.exceptions.RequestErrorException;
import com.gadeadiaz.physiocare.models.Appointment;
import com.gadeadiaz.physiocare.responses.ErrorResponse;
import com.gadeadiaz.physiocare.models.Patient;
import com.gadeadiaz.physiocare.services.AppointmentService;
import com.gadeadiaz.physiocare.services.PatientService;
import com.gadeadiaz.physiocare.services.PhysioService;
import com.gadeadiaz.physiocare.utils.Message;
import com.gadeadiaz.physiocare.utils.SceneLoader;
import com.gadeadiaz.physiocare.models.Physio;
import com.gadeadiaz.physiocare.utils.Storage;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
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
    private Button btnDetail;
    @FXML
    private Pane pnlList;
    @FXML
    private Pane pnlDetail;
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
    private Label lblTitle;
    @FXML
    private Label txtPhysiosCount;
    @FXML
    private Label txtPatientsCount;
    @FXML
    private Label lblColumn4;
    @FXML
    private VBox pnItems;
    @FXML
    private VBox pnAppointments;

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
    private final Gson gson = new Gson();
    private Stage stage;
    private Patient selectedPatient;
    private Physio selectedPhysio;
    private Appointment selectedAppointment;

    private enum ENTITIES{PATIENT, PHYSIO, APPOINTMENT}
    private ENTITIES selectedListEntity = ENTITIES.PATIENT;

    public void initialize() {
        getPatients();
    }

    private void showListPanel(){
        pnlDetail.setVisible(false);
        pnlList.setVisible(true);
        pnlPatientDetail.setVisible(false);
        pnlList.toFront();
    }

    private void showPatients(List<Patient> patients) {
        pnItems.getChildren().clear();
        for (Patient patient : patients) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/gadeadiaz/physiocare/user_item.fxml"
            ));
            try {
                Node node = loader.load();
                UserItemController controller = loader.getController();
                //controller.setUserId(patient.getId());
                controller.setLblAttribute1(patient.getName());
                controller.setLblAttribute2(patient.getSurname());
                controller.setLblAttribute3(patient.getEmail());
                controller.setLblAttribute4(patient.getInsuranceNumber());

                node.setOnMouseEntered(_ -> node.setStyle("-fx-background-color : #0A0E3F"));
                node.setOnMouseExited(_ -> node.setStyle("-fx-background-color : #02030A"));

                controller.setDetailListener(_ -> {
                    selectedPatient = patient;
                    selectedPhysio = null;
                    showPatientDetail(patient);
//                    showPatientForm();
                });

                controller.setDeleteListener(_ -> {
                    selectedPatient = patient;
                    deletePatient();
                });

                pnItems.getChildren().add(node);
            } catch (IOException e) {
                System.out.println("Patient loader fail: " + e.getMessage());
            }
        }
        txtPatientsCount.setText(String.valueOf(patients.size()));
    }

    private void getPatients() {
        showListPanel();
        PatientService.getPatients("").thenAccept(patients -> {
            selectedListEntity = ENTITIES.PATIENT;
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

    private void getPatientsBySurname() {
        PatientService.getPatients(txtSearch.getText()).thenAccept(patients -> {
            selectedListEntity = ENTITIES.PATIENT;
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
     * Shows the detail panel in the UI and hides the list panel.
     */
    private void showDetailPanel(){
        radioButtonsListener();
        specialtyListener();
        pnlDetail.setVisible(true);
        pnlList.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlDetail.toFront();

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
                UserItemController controller = loader.getController();

                //controller.setUserId(physio.getId());
                controller.setLblAttribute1(physio.getName());
                controller.setLblAttribute2(physio.getSurname());
                controller.setLblAttribute3(physio.getEmail());
                controller.setLblAttribute4(physio.getLicenseNumber());

                node.setOnMouseEntered(_ -> node.setStyle("-fx-background-color : #0A0E3F"));
                node.setOnMouseExited(_ -> node.setStyle("-fx-background-color : #02030A"));

                controller.setDetailListener(_ -> {
                    selectedPhysio = physio;
                    selectedPatient = null;
                    showPhysio();
                });

                controller.setDeleteListener(_ -> {
                    selectedPhysio = physio;
                    deletePhysio();
                });

                pnItems.getChildren().add(node);
            } catch (IOException e) {
                System.out.println("Patient loader fail: " + e.getMessage());
            }
        }
        txtPhysiosCount.setText(String.valueOf(physios.size()));
    }

    /**
     * Fetches the list of physiotherapists from the server and updates the UI with the retrieved data.
     * If there is an error, an error message is displayed.
     */
    private void getPhysios() {
        showListPanel();
        PhysioService.getPhysios("").thenAccept(physios -> {
            selectedListEntity = ENTITIES.PHYSIO;
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
    private void getPhysiosBySpecialty() {
        PhysioService.getPhysios(txtSearch.getText()).thenAccept(physios -> {
            selectedListEntity = ENTITIES.PHYSIO;
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


                controller.getLblDate().setText(formatter.format(appointment.getDate()));
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

    private void getAppointments(int patientId) {
        PatientService.getPatientAppointments(patientId).thenAccept(appointments -> {
            selectedListEntity = ENTITIES.APPOINTMENT;
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
    private void getRecords() {
        showListPanel();
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

    /**
     * Displays detailed information of the currently selected patient.
     * Sets text fields and labels with patient data and adjusts the UI components to display the patient's details.
     */
    private void showPatientForm() {
        showDetailPanel();
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
        //dpBirthDate.setValue(selectedPatient.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        btnDetail.setText("EDIT");
    }

    private void showPatientDetail(Patient patient){

        pnlDetail.setVisible(false);
        pnlPatientDetail.setVisible(false);
        pnlList.setVisible(false);
        pnlPatientDetail.setVisible(true);
        pnlPatientDetail.toFront();
        lblDetailPatientTitle.setText(patient.getName() + " " + patient.getSurname());
        txtDetailEmail.setText(patient.getEmail());
        if(patient.getAddress().isEmpty()){
            txtDetailAddress.setText("Address not indicated");
        }else{
            txtDetailAddress.setText(patient.getAddress());
        }
        txtDetailInsuranceNumber.setText(patient.getInsuranceNumber());
        txtDetailBirthDate.setText(formatter.format(patient.getBirthdate()));
        getAppointments(patient.getId());
    }

    /**
     * Displays detailed information of the currently selected physio.
     * Sets text fields and labels with physio data and adjusts the UI components to display the physio's details.
     */
    private void showPhysio() {
        showDetailPanel();
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

        btnDetail.setText("EDIT");
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
            lblTitle.setText("Patients");
            lblColumn4.setText("Insurance Number");
            getPatients();
        }
        if (actionEvent.getSource() == btnPhysios) {
            lblTitle.setText("Physios");
            lblColumn4.setText("License Number");
            getPhysios();
        }

        if (actionEvent.getSource() == btnNewUser ) {
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
        for (Node child: pnlDetail.getChildren()) {
            if(child instanceof TextField){
                ((TextField) child).setText("");
            }
        }

        rbPatient.setSelected(true);

        selectedPatient = new Patient();
        selectedPhysio = new Physio();
        editFormTextFields(true);
        splitSpecialty.setVisible(false);
        btnDetail.setText("CREATE");
        showDetailPanel();
    }

    /**
     * Enables or disables the editing capability of text fields in the form, based on the provided boolean parameter.
     *
     * @param editable if true, enables text fields; if false, disables them
     */
    private void editFormTextFields(boolean editable) {
        for (Node child: pnlDetail.getChildren()) {
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
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
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
            item.setOnAction(event -> {
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
    public void btnDetailClick() {
        if(btnDetail.getText().equals("CREATE") ) {
            /*if(selectedPatient.getId() == null && dpBirthDate.isVisible()){
                postPatient();
            }

            if(selectedPhysio.getId() == null && splitSpecialty.isVisible()){
                postPhysio();
            }*/
        } else if(btnDetail.getText().equals("SAVE")) {
            /*if(selectedPatient != null && selectedPatient.getId() != null){
                putPatient();
            }
            if(selectedPhysio != null && selectedPhysio.getId() != null){
                putPhysio();
            }*/
        } else if (btnDetail.getText().equals("EDIT")) {
            btnDetail.setText("SAVE");
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

    /**
     * Sends a POST request to create a new patient on the server.
     * If the request is successful, the list of patients is refreshed. If there is an error, an error message is displayed.
     */
    private void postPatient() {
        /*if(dpBirthDate.getValue() == null){
            Message.showError("Empty Birth Date", "Value of birth date cannot be empty!");
        } else {
            Map<String, Object> postBody = createPatientRequestBody();

            String apiUrl = ServiceUtils.SERVER + "patients";
            String jsonRequest = gson.toJson(postBody);

            ServiceUtils.getResponseAsync(apiUrl, jsonRequest, "POST")
                    .thenApply(json -> gson.fromJson(json, PatientResponse.class)
                    )
                    .thenAccept(response -> {
                        if (response.isOk()) {
                            Platform.runLater(this::getPatients);
                        } else {
                            Platform.runLater(() -> Message.showError("Error Creating Patient", response.getError()));
                        }
                    })
                    .exceptionally(ex -> {
                        Platform.runLater(() -> {
                            Message.showError("Error", "Error creating patient");
                        });
                        return null;
                    });
        }*/
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
     * Creates the request body for a new patient based on the current form data.
     *
     * @return a map containing the key-value pairs for the patient data
     */
    private Map<String, Object> createPatientRequestBody() {
        Map<String, Object> patientReqBody = new HashMap<>();
        patientReqBody.put("name", txtName.getText());
        patientReqBody.put("surname", txtSurname.getText());
        patientReqBody.put("birthDate", dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toString());
        patientReqBody.put("address", txtAddressAndSpecialty.getText());
        patientReqBody.put("insuranceNumber", txtLogin.getText());
        patientReqBody.put("email", txtEmail.getText());
        patientReqBody.put("image", "");
        patientReqBody.put("password", txtPassword.getText());
        return patientReqBody;
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

    public void searchClick(MouseEvent mouseEvent) {
        if(!txtSearch.getText().trim().isEmpty()){
            if(selectedListEntity.equals(ENTITIES.PATIENT)){
                getPatientsBySurname();
            }
            if(selectedListEntity.equals(ENTITIES.PHYSIO)){
                getPhysiosBySpecialty();
            }
        }
    }

}