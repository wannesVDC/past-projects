package domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Speler {

    private String naam;
    private int score;

    public Speler(String naam) throws DomainException {
        this.naam = setNaam(naam);
        this.score = score;


    }

    private String setNaam(String _naam) throws DomainException {
        if(_naam == null) throw new DomainException("Naam is null");
        if(_naam.length() == 0) throw new DomainException("De spelernaam moet minstens 1 spatie bevatten");
        Pattern p = Pattern.compile("[^\\s]");
        Matcher m = p.matcher(_naam);
        if(m.find() == false) throw new DomainException("Naam moet minstens een char bevatten");
        return _naam;
    }


    public int getScore() {
        return score;
    }

    public String getNaam() {
        return naam;
    }

    public void addToScore(int _score) throws DomainException {
        if(this.score + _score < 0) throw new DomainException("Score is negatief");
        this.score += _score;
    }

    public boolean equals(Speler _speler){
        if(_speler == null) return false;
        if(this.naam.equals(_speler.naam) && this.score == _speler.score) return true;
        return false;
    }

    public String toString(){
        return "Speler: "+this.naam + " Score:"+Integer.toString(this.score);
    }



}
