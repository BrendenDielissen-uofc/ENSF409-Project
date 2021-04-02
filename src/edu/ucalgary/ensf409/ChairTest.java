package edu.ucalgary.ensf409;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChairTest {
	
	@Test
	public void testChairConstructor() {
		String id = "C1320";
		String type = "Kneeling";
		Integer price = 50;
		String manuId = "002";
		boolean legs = true;
		boolean arms = false;
		boolean seat = false;
		boolean cushion = false;
		Chair chair = new Chair(id, type, price, manuId, legs, arms, seat, cushion);
				
	}

}
