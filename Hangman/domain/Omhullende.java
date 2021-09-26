package domain;

public class Omhullende {

    private Punt positieLinksBoven;
    private int breedte;
    private int hoogte;


    public Omhullende(Punt linkerBovenhoek, int breedte, int hoogte) throws DomainException {
        if (breedte < 0 || hoogte < 0) {
            throw new DomainException("Rechthoek heeft een breedte en hoogte groter dan 0 nodig.");
        }
        if (linkerBovenhoek == null) {
            throw new DomainException("Rechthoek heeft een linkerbovenhoek nodig.");
        }
        this.positieLinksBoven = linkerBovenhoek;
        this.breedte = breedte;
        this.hoogte = hoogte;
    }

    public Punt getLinkerBovenhoek(){
        return positieLinksBoven;
    }
    public int getBreedte(){
        return breedte;
    }
    public int getHoogte(){
        return hoogte;
    }

    public int getMinimumX(){
        return this.getLinkerBovenhoek().getX();
    }

    public int getMinimumY(){
        return this.getLinkerBovenhoek().getY();
    }

    public int getMaximumX(){
        return this.getMinimumX() + this.getBreedte();
    }

    public int getMaximumY(){
        return this.getMinimumY() + this.getHoogte();
    }

    public boolean equals(Object o) {
        if (o instanceof Omhullende) {
            if (this.getLinkerBovenhoek().equals(((Omhullende) o).getLinkerBovenhoek()) && this.getHoogte() == ((Omhullende) o).getHoogte() && this.getBreedte() == ((Omhullende) o).getBreedte()) {
                return true;
            }
        }
        return false;
    }

    public String toString(){
        String omh = "Rechthoek: positie: "+this.getLinkerBovenhoek().toString()+" - breedte: "+this.getBreedte()+" - hoogte: "+this.getHoogte();
        omh += "\n";
        omh += "Omhullende: "+ this.getLinkerBovenhoek().toString()+" - "+this.getBreedte()+ " - "+this.getHoogte();
        return omh;
    }


}
