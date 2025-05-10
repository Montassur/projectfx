package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import org.example.entities.User;
import org.example.entities.Role;

import java.io.File;

public class LayoutController {

    @FXML private VBox sidebar;
    @FXML private Button btnDashboard, btnUsers, btnTickets, btnReclamations, btnSponsors, btnLogout;
    @FXML private StackPane contentPane;

    @FXML private Label userNameLabel;
    @FXML private Label profileLabel;
    @FXML private ImageView topbarProfileImage;

    private User currentUser;

    protected void onUserSet() {
        Role role = currentUser.getRole();
        String roleName = (role != null) ? role.getName() : "En_attente";

        String fullName = currentUser.getPrenom() + " " + currentUser.getNom();
        profileLabel.setText(fullName);

        // Load profile picture if exists
        if (currentUser.getProfilePicture() != null && !currentUser.getProfilePicture().isEmpty()) {
            File imgFile = new File(System.getProperty("user.dir"), currentUser.getProfilePicture());
            if (imgFile.exists()) {
                topbarProfileImage.setImage(new Image(imgFile.toURI().toString()));
                topbarProfileImage.setFitWidth(30);
                topbarProfileImage.setFitHeight(30);
                Circle clip = new Circle(15, 15, 15);
                topbarProfileImage.setClip(clip);
            }
        }

        btnDashboard.setVisible(true);
        btnLogout.setVisible(true);
        btnUsers.setVisible("admin".equals(roleName));
        btnTickets.setVisible("admin".equals(roleName) || "Gestionnaire_des_tickets".equals(roleName));
        btnReclamations.setVisible("admin".equals(roleName) || "Agent_de_reclamation".equals(roleName));
        btnSponsors.setVisible("admin".equals(roleName) || "Responsable_sponsor".equals(roleName));

        loadContent("/Dashboard.fxml");
    }

    @FXML private void goDashboard()    { loadContent("/Dashboard.fxml"); }
    @FXML private void goUsers()        { loadContent("/UserList.fxml"); }
    @FXML private void goTickets()      { loadContent("/Tickets.fxml"); }
    @FXML private void goReclamations() { loadContent("/Reclamations.fxml"); }
    @FXML private void goSponsors()     { loadContent("/Sponsors.fxml"); }

    @FXML private void goProfile() {
        loadContent("/Profile.fxml");
    }

    @FXML private void logout() {
        try {
            Stage st = (Stage) sidebar.getScene().getWindow();
            st.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Login.fxml"))));
            st.setTitle("Connexion");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadContent(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(node);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadContentNode(Node node) {
        contentPane.getChildren().setAll(node);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        onUserSet();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    @FXML
    private void showUserMenu(MouseEvent event) {
        ContextMenu menu = new ContextMenu();

        MenuItem editProfile = new MenuItem("Modifier Profil");
        MenuItem logoutItem  = new MenuItem("Déconnexion");

        editProfile.setOnAction(e -> goProfile());
        logoutItem.setOnAction(e -> logout());

        menu.getItems().addAll(editProfile, logoutItem);
        menu.show(profileLabel, Side.BOTTOM, 0, 0);
    }
}
