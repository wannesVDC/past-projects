package domain;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class TekeningHangMan extends Tekening{

    private int indexEersteOnzichtbare = 4;

    public TekeningHangMan(){
        super("Hangman");
        try {
            this.voegVormenToe();
        }catch (DomainException e){
        }


    }

    public void voegVormenToe() throws DomainException {
        Vorm galgBodem = new Rechthoek(new Punt(10, 350), 300, 40);
        Vorm galgStaaf = new LijnStuk(new Punt(160, 350), new Punt(160, 50));
        Vorm hangbar = new LijnStuk(new Punt(160, 50), new Punt(280, 50));
        Vorm koord = new LijnStuk(new Punt(280, 50), new Punt(280, 100));
        Vorm hoofd = new Cirkel(new Punt(280, 125), 25);
        Vorm oogLinks = new Cirkel(new Punt(270, 118), 2);
        Vorm oogRechts = new Cirkel(new Punt(290, 118), 2);
        Vorm neus = new Cirkel(new Punt(280, 128), 2);
        Vorm mond = new LijnStuk(new Punt(270, 138), new Punt(290, 138));
        Vorm lijf = new LijnStuk(new Punt(280, 150), new Punt(280, 250));
        Vorm beenLinks = new LijnStuk(new Punt(280, 250), new Punt(240, 310));
        Vorm beenRechts = new LijnStuk(new Punt(280, 250), new Punt(320, 310));
        Vorm voetLinks = new Cirkel(new Punt(240, 310), 5);
        Vorm voetRechts = new Cirkel(new Punt(320, 310), 5);
        Vorm armLinks = new LijnStuk(new Punt(280, 200), new Punt(230, 170));
        Vorm armRechts = new LijnStuk(new Punt(280, 200), new Punt(330, 170));
        Vorm handLinks = new Cirkel(new Punt(230, 170), 5);
        Vorm handRechts = new Cirkel(new Punt(330, 170), 5);

        hoofd.setZichtbaar(false);
        oogLinks.setZichtbaar(false);
        oogRechts.setZichtbaar(false);
        neus.setZichtbaar(false);
        mond.setZichtbaar(false);

        lijf.setZichtbaar(false);
        beenLinks.setZichtbaar(false);
        beenRechts.setZichtbaar(false);
        voetLinks.setZichtbaar(false);
        voetRechts.setZichtbaar(false);
        armLinks.setZichtbaar(false);
        armRechts.setZichtbaar(false);
        handLinks.setZichtbaar(false);
        handRechts.setZichtbaar(false);

        this.vormen.add(galgBodem);
        this.vormen.add(galgStaaf);
        this.vormen.add(hangbar);
        this.vormen.add(koord);

        this.vormen.add(hoofd);
        this.vormen.add(oogLinks);
        this.vormen.add(oogRechts);
        this.vormen.add(neus);
        this.vormen.add(mond);

        this.vormen.add(lijf);
        this.vormen.add(beenLinks);
        this.vormen.add(beenRechts);
        this.vormen.add(voetLinks);
        this.vormen.add(voetRechts);
        this.vormen.add(armLinks);
        this.vormen.add(armRechts);
        this.vormen.add(handLinks);
        this.vormen.add(handRechts);
    }


    public int getAantalOnzichtbaar(){
        int count = 0;
        for (Vorm vorm : this.vormen){
            if (!vorm.isZichtbaar()){
                count++;
            }
        }
        return count;
    }

    public void zetVolgendeZichtbaar() throws DomainException {
        if(this.getAantalOnzichtbaar() > 0){
            Vorm vorm = this.getVorm(this.indexEersteOnzichtbare);
            vorm.setZichtbaar(true);
            this.indexEersteOnzichtbare++;
        }
        else {
            throw new DomainException("Alle vormen zijn al zichtbaar");
        }
    }

    @Override
    public void voegToe(Vorm vorm){

    }
    @Override
    public void verwijder(Vorm vorm){

    }
}
