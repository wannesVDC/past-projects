package domain;

import javafx.scene.paint.Color;
import ui.Drawable;


public abstract class Vorm implements Drawable {
    private Color kleur = Color.BLACK;
    private boolean isZichtbaar = true;
    private Omhullende omhullende;

    private static OmhullendeFactory omhullendeFactory = new OmhullendeFactory();

    public Vorm(){

    }


    public void create_omHullende(Vorm vorm) throws DomainException {
        this.omhullende = omhullendeFactory.getOmhullende(vorm);

    }



    public Omhullende getOmhullende() {
        return this.omhullende;
    }

    public Color getKleur(){
        return kleur;
    }

    public void setKleur(Color kleur){
        this.kleur = kleur;
    }
    /**
     * Van waar komt die omhullende hier?
     */
    //public Omhullende getOmhullende(){
    //
    //}
    public String toString(){
        return "De kleur van deze vorm: "+this.kleur+". Omhullende:";//+this.getOmhullende();
    }

    public boolean isZichtbaar(){
        return this.isZichtbaar;
    }
    public void setZichtbaar(boolean bool){
        this.isZichtbaar = bool;
    }
}
