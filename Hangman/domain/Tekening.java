package domain;
import javafx.scene.layout.Pane;
import ui.Drawable;

import java.util.*;

public class Tekening implements Drawable {
    String naamTekening;
    public static final int MIN_X = 0;public static final int MIN_Y = 0;
    public static final int MAX_X = 399; public static final int MAX_Y = 399;
    ArrayList<Vorm> vormen;

    public Tekening(String naamTekening) {
        if(isValidNaam(naamTekening)){
            this.naamTekening = naamTekening;
        }
        else{
            throw new IllegalArgumentException("Naam mag niet leeg zijn.");
        }
        vormen = new ArrayList<>();
    }

    public static boolean isValidNaam(String naamTekening){
        return naamTekening != null && !naamTekening.equals("") && !naamTekening.equals(" ");
    }

    public String getNaam(){
        return this.naamTekening;
    }

    public void voegToe(Vorm vorm) throws DomainException {
        try{
            if (vorm != null && !this.bevat(vorm)) {
                Omhullende omhullende = vorm.getOmhullende();
                if(omhullende.getMinimumX() >= MIN_X
                        && omhullende.getMinimumY() >= MIN_Y
                        && omhullende.getMaximumX() <= MAX_X
                        && omhullende.getMaximumY() <= MAX_Y){
                    vormen.add(vorm);

                }else{
                    throw new DomainException("Vorm ligt buiten het canvas");
                }
            }
        }catch(DomainException e){

        }

    }

    public Vorm getVorm(int vorm){
        return vormen.get(vorm);
    }

    public int getAantalVormen(){
        return vormen.size();
    }

    public void verwijder(Vorm vorm){
        vormen.remove(vorm);
    }

    public boolean bevat(Vorm vorm){
        return this.vormen.contains(vorm);
    }

    public boolean bevatVorm(Vorm vorm){
        return bevat(vorm);
    }

    public boolean equals(Tekening tekening){
        if(tekening == null){
            return  false;
        }
        else{
            return this.getAantalVormen() == tekening.getAantalVormen() && this.vormen.containsAll(tekening.vormen);
        }

    }

    @Override
    public void teken(Pane root) {
        for (Vorm vorm : this.vormen){
            if (vorm.isZichtbaar()){
                vorm.teken(root);
            }
        }
    }
}
