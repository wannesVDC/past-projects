package domain;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import ui.Drawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Driehoek extends Vorm implements Drawable {

    public Punt getHoekPunt1() {
        return hoekpunt1;
    }

    public Punt getHoekPunt2() {
        return hoekpunt2;
    }

    public Punt getHoekPunt3() {
        return hoekpunt3;
    }

    private Punt hoekpunt1;
    private Punt hoekpunt2;
    private Punt hoekpunt3;

    public ArrayList<Punt> getHoekpuntenLijst() {
        return this.hoekpuntenLijst;
    }

    private ArrayList<Punt> hoekpuntenLijst;
    public Driehoek(Punt hoekpunt1, Punt hoekpunt2, Punt hoekpunt3) throws DomainException {
        super();
        this.hoekpuntenLijst = new ArrayList<Punt>(Arrays.asList(hoekpunt1, hoekpunt2, hoekpunt3));
        System.out.println("hoekpuntenLijst" + this.hoekpuntenLijst.toString());
        for(Punt punt : hoekpuntenLijst){
            System.out.println(punt);
            if(punt == null){
                throw new DomainException("Hoekpunt is null");
            }

        }
        this.hoekpunt1 = hoekpunt1;
        this.hoekpunt2 = hoekpunt2;
        this.hoekpunt3 = hoekpunt3;
        create_omHullende(this);




    }

    public boolean liggenOpEenLijn(Punt p1,Punt p2,Punt p3){

        ArrayList<Integer> xCompare = new ArrayList<Integer>();
        ArrayList<Integer> yCompare = new ArrayList<Integer>();


        for(Punt punt: hoekpuntenLijst){
            if(xCompare.contains(punt)) xCompare.add(1);
            if(yCompare.contains(punt)) yCompare.add(1);

        }
        if(xCompare.size() == 3 || yCompare.size() == 3){
            return true;
        }

        return false;

    }

    public Boolean equals(Driehoek _driehoek){
        Collections.sort(hoekpuntenLijst, Punt.xComparator);
        Collections.sort(hoekpuntenLijst, Punt.yComparator);
        Collections.sort(_driehoek.hoekpuntenLijst , Punt.xComparator);
        Collections.sort(_driehoek.hoekpuntenLijst, Punt.yComparator);

        if (this.hoekpuntenLijst.get(0).equals(_driehoek.hoekpuntenLijst.get(0)) && this.hoekpuntenLijst.get(1).equals(_driehoek.hoekpuntenLijst.get(1)) && this.hoekpuntenLijst.get(2).equals(_driehoek.hoekpuntenLijst.get(2))){
            return true;
        }
        else {
            return false;
        }
    }

    public void sorteerHoekpunten(){

        Collections.sort(this.hoekpuntenLijst, Punt.xComparator);
        Collections.sort(this.hoekpuntenLijst, Punt.yComparator);

        hoekpunt1 = hoekpuntenLijst.get(0);
        hoekpunt2 = hoekpuntenLijst.get(1);
        hoekpunt3 = hoekpuntenLijst.get(2);

    }

    public String toString() {
        String result = "hoekpunt1: (%d, %d) - hoekpunt2: (%d, %d) - hoekpunt3: (%d,%d)";
        String.format(result, hoekpunt1.getX(),hoekpunt1.getX(),hoekpunt2.getX(),hoekpunt2.getY(),hoekpunt3.getX(),hoekpunt3.getY());
        return result;
    }

    @Override
    public void teken(Pane root) {
        Polyline driehoek = new Polyline();
        driehoek.setFill(this.getKleur());
        driehoek.setStroke(this.getKleur());
        driehoek.getPoints().addAll(new Double[]{(double) this.getHoekPunt1().getX(), (double) this.getHoekPunt1().getY(), (double) this.getHoekPunt2().getX(),
                (double) this.getHoekPunt2().getY(), (double) this.getHoekPunt3().getX(), (double) this.getHoekPunt3().getY()});

        root.getChildren().add(driehoek);
    }
}
