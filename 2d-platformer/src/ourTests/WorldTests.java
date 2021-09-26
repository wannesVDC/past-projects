package ourTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jumpingalien.model.GeologicalFeature;
import jumpingalien.model.World;

class WorldTests {
	// private static World world_1;
	
	// Variables storing the actual score and the maximum score.
	private static int actualScore = 0;
	private static int maximumScore = 0;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		System.out.println();
		System.out.println("Score: " + actualScore + "/" + maximumScore);
	}

	@BeforeEach
	void setUp() throws Exception {
		// world_1=new World(10,10,1,new int[] {1,1},10,10);
	}
	
	/*
	@AfterEach
	void tearDown() throws Exception {
	}
	*/
	
	@Test
	void validIntToGeoTranslator() throws Exception {
		maximumScore +=7;
		
		assertEquals(GeologicalFeature.AIR,World.intToGeo(0));
		assertEquals(GeologicalFeature.SOLID,World.intToGeo(1));
		assertEquals(GeologicalFeature.WATER,World.intToGeo(2));
		assertEquals(GeologicalFeature.MAGMA,World.intToGeo(3));
		assertEquals(GeologicalFeature.ICE,World.intToGeo(4));
		assertEquals(GeologicalFeature.GAS,World.intToGeo(5));
		assertThrows(AssertionError.class,()->World.intToGeo(6));
		
		actualScore +=7;
	}

}
