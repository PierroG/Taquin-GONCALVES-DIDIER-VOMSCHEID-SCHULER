/*
 * 
 */
package Login;

import GamePackage.FXMLDocumentController;
import GamePackage.Jdbc;
import static GamePackage.Jdbc.getInstance;
import java.net.URL;
import java.sql.Connection;
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


/**
 * Controller du formulaire d'inscription
 * 
 * 
 * @author Pierre
 */

public class FXMLFormulaireInscriptionController implements Initializable {

    @FXML 
    private TextField usernameTF;
    @FXML 
    private PasswordField passwordTF ,confirmPasswordTF;
    @FXML 
    private Label linfo;
    @FXML 
    private Button cancelButton;
    /**
     * référence de notre controller principal
     */
    private FXMLDocumentController mainController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }

    public void init(FXMLDocumentController c){
        mainController = c;
        
    }
    /**
     * Action du bouton inscription,
     * Vérifie que les donnée rentré sont valide, si oui lance la réquéte d'inscription
     * @param event 
     */
    @FXML
    private void handleButtonInscription(ActionEvent event){
        System.out.println("Inscription");
        String regex = "[a-zA-Z0-9]+";
        if(passwordTF.getText().length()<4){
            linfo.setText("Password trop court, 4 char mini");
        }else if(!passwordTF.getText().equals(confirmPasswordTF.getText())){
            linfo.setText("Ereur de confirmation du Password");
        }else if((usernameTF.getText().matches(regex) && passwordTF.getText().matches(regex))==false){
            linfo.setText("Ereur a-z / A-Z / 0-9 Uniquement");
        
        }else if(usernameTF.getText().equals(passwordTF.getText())){
            linfo.setText("Username et Password ne peuvent pas étre identique");
        //Si on passe toute les vérification c'est que c'est correcte
        }else{
            linfo.setText("Correct");
            //Derniére vérification , si l'Username est déja utiliser par un autre compte ou non
            String s = Jdbc.verifInscription(usernameTF.getText());
            if(s!=""){
                linfo.setText(s);
                }else{
                //on lance la requéte d'inscription
                boolean b = Jdbc.inscription(usernameTF.getText(),passwordTF.getText());
                //si elle a bien était effectuer on ferme le formulaire
                if(b){
                    System.out.println("Inscription Réussie");
                    this.close();
                }else{
                   System.out.println("erreurd'inscription");
                }
            }
        }
            
        
    }
    /**
     * Action bouton CANCEL
     * @param event 
     */
    @FXML
    private void handleButtonCancel(ActionEvent event){
        System.out.println("Cancel");
        this.close();
    }
    //Ferme la fenétre
    /**
     * Ferme la fenétre
     */
    private void close(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
}
