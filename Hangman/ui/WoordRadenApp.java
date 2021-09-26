package ui;

import domain.DomainException;
import domain.HintLetter;
import domain.HintWoord;
import domain.Speler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Random;

public class WoordRadenApp {

    private Alert foutenboodschap = new Alert(Alert.AlertType.WARNING);

    public WoordRadenApp(GridPane root, Speler speler) throws DomainException {

/*        Random rand = new Random();
        String woord = this.hintWoord.getWoord();
        HintLetter[] woordLijst =  this.hintWoord.getHintWoord();
        int randomIndex = rand.nextInt((woord.length() - 0) + 1);
        char randomChar = woord.charAt(randomIndex);
        for(int i = 0; i< woord.length(); i++){
            if(woord.charAt(i) == randomChar){
                woordLijst[i].setGeraden(true);
            }
        }*/

/*
        Label raad = new Label("Geef de naam van de speler ");
*/
        HintWoord hintWoord = new HintWoord("Test");


        Label hintWoordLabel = new Label(hintWoord.toString());
        TextField raadInput = new TextField();

/*
        root.add(raad, 0, 0);
*/
        root.add(raadInput, 1, 1);
        root.add(hintWoordLabel, 0, 0);


        raadInput.setOnAction(eventIngaveNaam -> {
            if(!hintWoord.isGeraden()){
                System.out.println(raadInput.getText().toString());

                char input = raadInput.getText().toString().charAt(0);
                hintWoord.raad(input);
                hintWoordLabel.setText(hintWoord.toString());

                raadInput.clear();
            }





        });

    }
}
