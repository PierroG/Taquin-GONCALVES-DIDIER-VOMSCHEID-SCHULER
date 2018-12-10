/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import GamePackage.FXMLDocumentController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class FXMLFormulaireInscriptionController implements Initializable {

    @FXML 
    private TextField usernameTF;
    @FXML 
    private PasswordField passwordTF ,confirmPasswordTF;
    @FXML 
    private Label linfo;
    @FXML 
    private Button cancelButton;
    private FXMLDocumentController mainController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }

    public void init(FXMLDocumentController c){
        mainController = c;
    }
    @FXML
    private void handleButtonInscription(ActionEvent event){
        System.out.println("Inscription");
            
        if(!passwordTF.getText().equals(confirmPasswordTF.getText())){
            linfo.setText("Confirmation non identique");
        }else if(passwordTF.getText().length()<3){
            linfo.setText("Password trop court, 4 char mini");
            
        }
    }
    @FXML
    private void handleButtonCancel(ActionEvent event){
        System.out.println("Cancel");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
}
