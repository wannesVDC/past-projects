package db.domain;
import domain.DomainException;
import domain.WoordenLijst;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class WoordenLezer {

    private String tekstBestand;

    public WoordenLezer(String tekstBestand){
        this.tekstBestand = tekstBestand;
    }

    public WoordenLijst lees(){
        try {
            File myObj = new File(this.tekstBestand);
            Scanner myReader = new Scanner(myObj);

            WoordenLijst woordenLijst = new WoordenLijst();

            while (myReader.hasNextLine()) {
                String woord = myReader.nextLine();
                woordenLijst.voegToe(woord);
            }
            myReader.close();
            return woordenLijst;

        } catch (FileNotFoundException | DomainException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return null;
    }

}
