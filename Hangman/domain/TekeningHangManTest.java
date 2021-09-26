package domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

public class TekeningHangManTest {
    private TekeningHangMan hangMan;

    @Before
    public void setUp(){
        hangMan = new TekeningHangMan();
    }

    @Test
    public void toevoegenTest() throws DomainException {
        assertEquals(18,hangMan.getAantalVormen());
        ArrayList<Vorm> vormen = this.genereerAantalVormen();
        hangMan.voegToe(vormen.get(0));
        assertEquals(18,hangMan.getAantalVormen());
        hangMan.voegToe(vormen.get(1));
        assertEquals(18,hangMan.getAantalVormen());
        hangMan.voegToe(vormen.get(2));
        assertEquals(18,hangMan.getAantalVormen());

        assertFalse(hangMan.bevat(vormen.get(0)));
        assertFalse(hangMan.bevat(vormen.get(1)));
        assertFalse(hangMan.bevat(vormen.get(2)));
        hangMan = new TekeningHangMan();
    }

    @Test
    public void verwijderTest(){
        assertEquals(18, hangMan.getAantalVormen());
        ArrayList<Vorm> vormList = new ArrayList<Vorm>();
        for (Vorm vorm : hangMan.vormen){
            hangMan.verwijder(vorm);
            assertEquals(18, hangMan.getAantalVormen());
            vormList.add(vorm);
        }
        for (Vorm vorm : vormList){
            assertTrue(hangMan.bevat(vorm));
        }

    }







    //hulp functies

    //krijg lijst met een aantal vormen
    public ArrayList<Vorm> genereerAantalVormen() throws DomainException {
        ArrayList<Vorm> vormen = new ArrayList<>();

        Rechthoek rechthoek = new Rechthoek(new Punt(5,5),5,5);
        Driehoek driehoek = new Driehoek(new Punt(5,5),new Punt(6,6),new Punt(8,4));
        Cirkel cirkel = new Cirkel(new Punt(5,5),5);

        vormen.add(rechthoek);
        vormen.add(driehoek);
        vormen.add(cirkel);
        return vormen;
    }
}
