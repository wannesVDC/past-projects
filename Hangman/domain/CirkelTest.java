package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CirkelTest {

    private Punt middelpunt;
    private int radius;
    private Cirkel cirkel;

    @Before
    public void setUp() throws Exception, DomainException {
        middelpunt = new Punt(200,200);
        radius = 20;
        cirkel = new Cirkel(middelpunt, radius);
    }

    @Test
    public void Cirkel_moet_cirkel_aanmaken_met_gegeven_middelpunt_en_straal() throws DomainException {
        Cirkel cirkel = new Cirkel(middelpunt, radius);
        assertTrue(middelpunt.equals(cirkel.getMiddelPunt()));
        assertEquals(radius, cirkel.getRadius());
    }

    @Test
    public void Cirkel_moet_exception_gooien_als_middelpunt_null() throws DomainException {
        new Cirkel(null, radius);
    }

    @Test
    public void Cirkel_moet_exception_gooien_als_radius_kleiner_dan_0() throws DomainException {
        new Cirkel(middelpunt, -1);
    }

    @Test
    public void Cirkel_moet_exception_gooien_als_straal_gelijk_is_aan_0() throws DomainException {
        new Cirkel(middelpunt, 0);
    }

    @Test
    public void Equals_moet_true_teruggeven_als_straal_en_middelpunt_gelijk_zijn() throws DomainException {
        Cirkel zelfdeCirkel = new Cirkel(middelpunt, radius);
        assertTrue(cirkel.equals(zelfdeCirkel));
    }

    @Test
    public void Equals_moet_false_teruggeven_als_parameter_null(){
        assertFalse(cirkel.equals(null));
    }

    @Test
    public void Equals_moet_false_teruggeven_als_middelpunt_verschilt() throws DomainException {
        Punt anderMiddelpunt = new Punt(100, 100);
        Cirkel verschillendeCirkel = new Cirkel(anderMiddelpunt, radius);
        assertFalse(cirkel.equals(verschillendeCirkel));
    }

    @Test
    public void Equals_moet_false_teruggeven_als_straal_verschilt() throws DomainException {
        Cirkel verschillendeCirkel = new Cirkel(middelpunt, radius+10);
        assertFalse(cirkel.equals(verschillendeCirkel));
    }
}