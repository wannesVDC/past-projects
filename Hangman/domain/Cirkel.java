package domain;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import ui.Drawable;

public class Cirkel extends Vorm implements Drawable {
    private Punt middelPunt;
    private int radius;
        public Cirkel(Punt punt, int radius) throws DomainException {
            this.middelPunt = punt;
            if(punt == null){
                throw new DomainException("punt mag niet leeg zijn");
            }
            if(radius <= 0){
               throw new DomainException("int: Radius, kan niet <0 of 0 zijn.");
            }
            else{
                this.radius = radius;
            }
            create_omHullende(this);

        }
    private void setMiddelPunt(Punt punt){
            this.middelPunt = punt;
        }

        private void setRadius(int radius){
            this.radius = radius;
        }

        public Punt getMiddelPunt(){
            return this.middelPunt;
        }

        public int getRadius(){
            return this.radius;
        }

        public boolean equals(Cirkel cirkel){
            if (cirkel == null){
                return false;
            }
            return this.middelPunt.equals(cirkel.getMiddelPunt()) && this.radius == cirkel.radius;
        }
        public String toString(){
            return "Cirkel: middelpunt: ("+middelPunt.getX()+", "+middelPunt.getY()+") - straal:"+radius;
        }

    @Override
    public void teken(Pane root) {
        Circle cirkel = new Circle(this.getMiddelPunt().getX(), this.getMiddelPunt().getY(), this.getRadius());
        cirkel.setFill(Color.WHITE);
        cirkel.setStroke(this.getKleur());

        root.getChildren().add(cirkel);
    }
}

