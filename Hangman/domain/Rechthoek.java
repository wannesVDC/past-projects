package domain;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ui.Drawable;

public class Rechthoek extends Vorm implements Drawable {

    private int breedte;
    private int hoogte;
    private Punt linkerBovenHoek;


    private Omhullende omhullende;

    public Rechthoek(Punt linkerBovenhoek, int breedte, int hoogte) throws DomainException {
        super();
        try {
            if (breedte <= 0 || hoogte <= 0) {
                throw new DomainException("Rechthoek heeft een breedte en hoogte groter dan 0 nodig.");
            }
            if (linkerBovenhoek == null) {
                throw new DomainException("Rechthoek heeft een linkerbovenhoek nodig.");
            }
            this.breedte = breedte;
            this.hoogte = hoogte;
            this.linkerBovenHoek = linkerBovenhoek;
            this.create_omHullende(this);


        }catch(DomainException e){

        }


    }

    public int getBreedte() {
        return this.breedte;
    }

    public int getHoogte() {
        return this.hoogte;
    }

    public Punt getLinkerBovenhoek() {
        return this.linkerBovenHoek;
    }

    public boolean equals(Object o) {
        if (o instanceof Rechthoek) {
            if (this.linkerBovenHoek.equals(((Rechthoek) o).linkerBovenHoek) && this.hoogte == ((Rechthoek) o).hoogte && this.breedte == ((Rechthoek) o).breedte) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        String ret = "Rechthoek: linkerbovenhoek: ";
        ret += this.linkerBovenHoek.toString();
        ret += " - breedte: ";
        ret += this.breedte;
        ret += " - hoogte: ";
        ret += this.hoogte;
        return ret;
    }

    @Override
    public void teken(Pane root) {
        Rectangle rechthoek = new Rectangle(this.getLinkerBovenhoek().getX(), this.getLinkerBovenhoek().getY(), this.getBreedte(), this.getHoogte());
        rechthoek.setFill(this.getKleur());
        rechthoek.setStroke(this.getKleur());
        root.getChildren().add(rechthoek);
    }
}

