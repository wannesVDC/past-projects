package domain;

public class HintLetter {
    private char letter;

    public void setGeraden(boolean geraden) {
        isGeraden = geraden;
    }

    private boolean isGeraden;

    public HintLetter(char letter){
        this.letter = letter;
    }

    public char getLetter(){
        return letter;
    }

    public boolean raad(char letter){
        if(isGeraden()){
            return false;
        }
        else if(Character.toLowerCase(this.letter) == Character.toLowerCase(letter)){
            isGeraden = true;
            return true;
        }
        else{
            return false;
        }
    }

    public char toChar(){
        if(isGeraden){
            return letter;
        }
        else {
            return '_';
        }
    }

    public boolean isGeraden(){
        return isGeraden;
    }
}
