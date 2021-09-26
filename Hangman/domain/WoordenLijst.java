package domain;

import org.w3c.dom.DOMException;

import java.util.ArrayList;

public class WoordenLijst {

    private ArrayList<String> WoordenLijst;

    public WoordenLijst(){
        this.WoordenLijst = new ArrayList<>();

    }


    public int getAantalWoorden() {
        return this.WoordenLijst.size();
    }

    public void voegToe(String s)throws DomainException {
        try{
            if(s == null)throw new DomainException("String mag niet null zijn");

            if(s.trim().isEmpty())throw new DomainException("String mag niet leeg zijn");

            if ( this.WoordenLijst.contains(s) ) throw new DomainException("Woord staat al in de lijst");

            this.WoordenLijst.add(s);


        }catch (DomainException e){
            throw new DomainException("Fout bij ingeven van de String");
        }
    }

    public String getRandomWoord() throws DomainException{

        if(WoordenLijst == null ||  WoordenLijst.size() == 0) throw new DomainException("Woordenlijst is leeg of null");

        int randomIndex = (int)(Math.random() * this.WoordenLijst.size());

        return this.WoordenLijst.get(randomIndex);


    }


}

