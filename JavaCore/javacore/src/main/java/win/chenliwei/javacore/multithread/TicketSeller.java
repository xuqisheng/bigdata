/**
 * @author: ChenLiwei
 * 2017-03-02
 * TicketSeller.java
 * Comments: It uses Multithreaded to simulate the scene of ticket selling in train station
 */
package win.chenliwei.javacore.multithread;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.SortedMap;

public class TicketSeller {

	static Queue<Seat> seats = new ArrayDeque<>();
	static TrainShift trainShift = new TrainShift("G9999", new Station("BeiJing", new Date(), 0), new Station("ShangHai", new Date(), 0), null);
	public static void main(String[] args) {
		//open 100 seats
		for(int i = 1; i < 6; i++) {
			switch(i % 5){
			case 0:	seats.add(new Seat(1,1,SeatNumber.F));break;
			case 1: seats.add(new Seat(1,1,SeatNumber.A));break;
			case 2: seats.add(new Seat(1,1,SeatNumber.B));break;
			case 3: seats.add(new Seat(1,1,SeatNumber.C));break;
			case 4: seats.add(new Seat(1,1,SeatNumber.D));break;
			}
		}
		
		//suppose 200 passengers
		List<Passenger> passengers = new ArrayList<>(7);
		for(int i = 1; i < 8; i++) passengers.add(new Passenger("passenger"+i, "3201234343434344334"+i));
		
		passengers.forEach(p -> {sell(p);});
	}
	
	public static void sell(Passenger p){
		Runnable r = new Runnable(){

			@Override
			public void run() {
				if(! seats.isEmpty()){
					Seat seat = seats.poll();
					Ticket ticket = new Ticket(p, seat, trainShift);
					ticket.print();
				} else {
					System.out.println("All the tickets sold out");
				}
			}
			
		};
		new Thread(r).start();
	}

}

class Ticket{
	private Passenger passenger;
	private Seat seat;
	private TrainShift trainShift;
	
	public Ticket(Passenger passenger, Seat seat, TrainShift trainShift) {
		this.passenger = passenger;
		this.seat = seat;
		this.trainShift = trainShift;
	}

	public  void  print(){
		System.out.println("-------------Train Ticket-----------\n" + trainShift.toString() + seat.toString() + "\n" + passenger.toString());
	}
	
}

class Passenger{
	private String name;
	private String id;
	public Passenger(String name, String id) {
		this.name = name;
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}
	@Override
	public  String toString() {
		return name + "     " +  id.substring(0, 5) + "******" + id.substring(10);
	}
}

class TrainShift{
	private String id;
	private Station departCity;
	private Station destCity;
	private SortedMap<Integer,Station> passStations;

	public String getId() {
		return id;
	}
	public TrainShift(String id, Station departCity, Station destCity, SortedMap<Integer, Station> passStations) {
		this.id = id;
		this.departCity = departCity;
		this.destCity = destCity;
		this.passStations = passStations;
	}
	public Station getDepartCity() {
		return departCity;
	}
	public void setDepartCity(Station departCity) {
		this.departCity = departCity;
	}
	public Station getDestCity() {
		return destCity;
	}
	public void setDestCity(Station destCity) {
		this.destCity = destCity;
	}
	public SortedMap<Integer, Station> getPassStations() {
		return passStations;
	}
	public void addPassStations(int key,Station station) {
		this.passStations.put(key, station);
	}
	@Override
	public  String toString() {
		Calendar c = Calendar.getInstance();
		c.setTime(departCity.getArriveTime());
		c.add(Calendar.MINUTE, departCity.getStopMinutes());
		return id + "   " + departCity.getName() + " -> " + destCity.getName() + " starts at "
				+ c.getTime() ;
	}
	
}

class Station{
	private String name;
	private Date arriveTime;
	private int stopMinutes;
	public Station(String name, Date arriveTime, int stopMinutes) {
		this.name = name;
		this.arriveTime = arriveTime;
		this.stopMinutes = stopMinutes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}
	public int getStopMinutes() {
		return stopMinutes;
	}
	public void setStopMinutes(int stopMinutes) {
		this.stopMinutes = stopMinutes;
	}
}

enum SeatNumber{A,B,C,D,F}

class Seat{
	private int carriage;
	private int array;
	private SeatNumber seat;
	public Seat(int carriage, int array, SeatNumber seatNumber) {
		this.carriage = carriage;
		this.array = array;
		this.seat = seatNumber;
	}
	
	@Override
	public   String toString() {
		return "Seat: carriage=" + carriage + "  array=" + array + "  seat=" + seat;
	}
	
}