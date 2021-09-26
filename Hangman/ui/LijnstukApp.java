package ui;

import domain.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class LijnstukApp {
    private Punt startpunt;
    private Punt eindpunt;
    private Label invoerXLabels, invoerYLabels, invoerXLabele, invoerYLabele;
    private TextField invoerXstart, invoerYstart;
    private TextField invoerXeind, invoerYeind;

    private Alert foutenboodschap = new Alert(Alert.AlertType.WARNING);


    public LijnstukApp(GridPane root, Tekening tekening) {

        init(root,1);
        invoerYeind.setOnAction(eventIngaveHoogte -> {
            try {
                this.startpunt = new Punt(Integer.parseInt(invoerXstart.getText()),Integer.parseInt(invoerYstart.getText()));
                this.eindpunt = new Punt(Integer.parseInt(invoerXeind.getText()),Integer.parseInt(invoerYeind.getText()));
                LijnStuk lijn = new LijnStuk(this.startpunt, this.eindpunt);
                tekening.voegToe(lijn);
                cleanUp(root);
                root.getChildren().clear();
                Text uitvoer = new Text();
                uitvoer.setText(lijn.toString());
                root.add(uitvoer,0,0);

            } catch (NumberFormatException ne){
                invoerYeind.clear();
                foutenboodschap.setTitle("Warning");
                foutenboodschap.setContentText("De hoogte moet een geheel getal zijn");
                foutenboodschap.showAndWait();
            }
            catch (DomainException e){
                cleanUp(root);
                foutenboodschap.setTitle("Warning");
                foutenboodschap.setHeaderText(null);
                foutenboodschap.setContentText(e.getMessage());
                foutenboodschap.showAndWait();
            }

        });
    }

    private void init(GridPane root, int teller){
        invoerXLabels =  new Label("Geef de x-coördinaat van de linkerbovenhoek ");
        invoerXstart = new TextField();

        invoerYLabels = new Label("Geef de y-coördinaat van de linkerbovenhoek ");
        invoerYstart = new TextField();

        invoerXLabele =  new Label("Geef de breedte van de rechthoek ");
        invoerXeind = new TextField();

        invoerYLabele = new Label("Geef de y-coördinaat van het eindpunt ");
        invoerYeind = new TextField();


        root.add(invoerXLabels,0,teller);
        root.add(invoerXstart,1,teller);

        invoerXstart.setOnAction(eventIngaveX ->{
            try {
                Integer.parseInt(invoerXstart.getText());
                root.add(invoerYLabels, 0, teller + 1);
                root.add(invoerYstart, 1, teller + 1);
            }
            catch(NumberFormatException e){
                invoerXstart.clear();
                foutenboodschap.setTitle("Warning");
                foutenboodschap.setContentText("x coördinaat moet een geheel getal zijn");
                foutenboodschap.showAndWait();
            }

        });

        invoerYstart.setOnAction(eventIngaveY -> {
            try {
                Integer.parseInt(invoerYstart.getText());
                root.add(invoerXLabele, 0, teller + 2);
                root.add(invoerXeind, 1, teller +  2);
            } catch (NumberFormatException e){
                invoerYstart.clear();
                foutenboodschap.setTitle("Warning");
                foutenboodschap.setContentText("y coördinaat moet een geheel getal zijn");
                foutenboodschap.showAndWait();

            }
        });

        invoerXeind.setOnAction(eventIngaveXeind -> {
            try {
                Integer.parseInt(invoerXeind.getText());
                root.add(invoerYLabele, 0, teller + 3);
                root.add(invoerYeind, 1, teller +  3);
            } catch (NumberFormatException e){
                invoerXeind.clear();
                foutenboodschap.setTitle("Warning");
                foutenboodschap.setContentText("breedte moet een geheel getal zijn");
                foutenboodschap.showAndWait();

            }
        });
    }
    private void  cleanUp(GridPane root){
        root.getChildren().remove(invoerXLabels);
        root.getChildren().remove(invoerXstart);
        root.getChildren().remove(invoerYLabels);
        root.getChildren().remove(invoerYstart);
        root.getChildren().remove(invoerXLabele);
        root.getChildren().remove(invoerXeind);
        root.getChildren().remove(invoerYLabele);
        root.getChildren().remove(invoerYeind);

    }
}
