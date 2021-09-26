package domain;

public class HintWoord {

    public HintLetter[] getHintWoord() {
        return hintWoord;
    }

    private final HintLetter[] hintWoord;
    private final String woord;

    public HintWoord(String woord) throws DomainException {
        if (woord != null){
            hintWoord = new HintLetter[woord.length()];
            if(woord.isBlank()){
                throw new DomainException("Woord mag niet leeg zijn");
            }
            else {

                this.woord = woord;
                for (int i = 0; woord.length() > i; i++){
                    HintLetter letter = new HintLetter(woord.charAt(i));
                    hintWoord[i] = letter;
                }
            }
        }
        else{
            throw new DomainException("Woord mag niet leeg zijn");
        }
    }



    public boolean raad(char letter){
        boolean raad = false;
        for (int i = 0; woord.length() > i; i++){
            if (hintWoord[i].raad(letter)){
                raad = true;
            }
        }
        return raad;
    }

    public boolean isGeraden(){
        for(int i = 0; woord.length() > i; i++){
            if (!hintWoord[i].isGeraden()){
                return false;
            }
        }
        return true;
    }

    public String getWoord(){
        StringBuilder woord = new StringBuilder();
        for(int i = 0; i<this.woord.length(); i++){
            woord.append(hintWoord[i].getLetter());
        }
        return woord.toString();
    }

    public String toString(){
        StringBuilder woord = new StringBuilder();
        for(int i = 0; i<this.woord.length(); i++){
            woord.append(hintWoord[i].toChar()).append(" ");
        }
        StringBuilder sb= new StringBuilder(woord.toString());
        return sb.deleteCharAt(woord.length()-1).toString();
    }
}
