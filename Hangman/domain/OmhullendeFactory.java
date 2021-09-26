package domain;

import java.util.ArrayList;
import java.util.Collections;

public class OmhullendeFactory {

    public Omhullende getOmhullende(Vorm vorm) throws DomainException {

        /****** Rechthoek *******/
        if(vorm instanceof Rechthoek){
            Rechthoek rechthoek = ((Rechthoek) vorm);
            return new Omhullende(rechthoek.getLinkerBovenhoek(),Math.abs(rechthoek.getBreedte()),Math.abs(rechthoek.getHoogte()));

            /******* Cirkel *******/
        }else if(vorm instanceof Cirkel){
            Cirkel cirkel = ((Cirkel) vorm);
            Punt linkerBovenHoek = new Punt(cirkel.getMiddelPunt().getX() - cirkel.getRadius(), cirkel.getMiddelPunt().getY() - cirkel.getRadius());
            return new Omhullende(linkerBovenHoek,cirkel.getRadius()*2,cirkel.getRadius()*2);

            /******* Driehoek *******/
        }else if(vorm instanceof Driehoek){
            Driehoek driehoek = ((Driehoek) vorm);
            System.out.println(driehoek.getHoekpuntenLijst());
            driehoek.sorteerHoekpunten();
            ArrayList<Punt> hoekpunten =  driehoek.getHoekpuntenLijst();

            Punt linkerBovenHoek = new Punt(hoekpunten.get(2).getX(),hoekpunten.get(2).getY());
            int breedte = hoekpunten.get(2).getX()-hoekpunten.get(0).getX();
            Collections.sort(hoekpunten, Punt.yComparator);
            int hoogte = hoekpunten.get(2).getY()-hoekpunten.get(0).getY();
            return new Omhullende(linkerBovenHoek,Math.abs(breedte),Math.abs(hoogte));

            /******* Lijnstuk *******/
        }else if(vorm instanceof LijnStuk){
            LijnStuk lijn = ((LijnStuk) vorm);
            Punt linkerBovenHoek = new Punt(lijn.getStartPunt().getX(),lijn.getStartPunt().getY());
            int breedte = lijn.getEindPunt().getX() - lijn.getStartPunt().getX();
            int hoogte = lijn.getEindPunt().getY() - lijn.getStartPunt().getY();
            return new Omhullende(linkerBovenHoek, Math.abs(breedte),Math.abs(hoogte));
        }else{
            return null;
        }
    }
}
