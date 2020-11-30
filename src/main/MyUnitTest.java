package main;
import org.junit.Test;
import static org.junit.Assert.*;


public class MyUnitTest {

	@Test
	public void testCalculatePercentAttendance() {
		int time = 0;
		assertEquals(0, calculatePercentAttendance(time));
	}
	
	@Test
	public void testCalculatePercentAttendance1() {
		int time = 17;
		assertEquals(25, calculatePercentAttendance(time));
	}
	
	@Test
	public void testCalculatePercentAttendance2() {
		int time = 45;
		assertEquals(60, calculatePercentAttendance(time));
	}
	
	@Test
	public void testCalculatePercentAttendance3() {
		int time = 59;
		assertEquals(80, calculatePercentAttendance(time));
	}
	
	@Test
	public void testCalculatePercentAttendance4() {
		int time = 75;
		assertEquals(100, calculatePercentAttendance(time));
	}
	
}
