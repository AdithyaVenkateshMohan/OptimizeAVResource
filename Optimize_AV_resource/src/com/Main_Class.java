package com;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.*;

public class Main_Class {
	static String folder = "E:/Java/AVSharingITest/testDataSets/New folder/";

	public Main_Class() {
		// TODO Auto-generated constructor stub
	}
	
public static void main(String[] args) 
	
	{
		
		
		// TODO Auto-generated method stub
		
		Date today = new Date();   //System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss a");
		
		Object ven = new Vehicle("adi",today,100,200);
		Vehicle ben = new Vehicle("kadi",today,150,220);
		Vehicle gen = new Vehicle("kadi",today,50,20);
		Source one = new Source(5,100,200,5);
		Source Two = new Source(5,120,80,10);
		Source Three = new Source(5,150,90,15);
		Object Three_copy = Three;
		System.out.println("object");
		System.out.println(((Object)Three_copy == Three));
		
		List <Vehicle> ListofVehicle = new ArrayList<Vehicle> ();
		List <Source> ListofSource = new ArrayList<Source> ();
		ListofVehicle.add(gen);
		ListofVehicle.add( (Vehicle)ven);
		System.out.println(ven==ven);
		ListofVehicle.add(ben);
		ListofSource.add(one);
		ListofSource.add(Two);
		ListofSource.add(Three);
		System.out.println(ListofVehicle);
		System.out.println(ListofSource);
		
		System.out.println("This is instance ... #  " + 1);
		
		String csvFile = "output1"  + ".csv";
		try 
		{
			
			Date date = format.parse("1/1/2013  12:00:00 AM");
			System.out.println(date);
			System.out.println(date.getTime()-today.getTime());
			TripData example = new TripData();
			example.parseTripData(folder + csvFile);
			List<Demand> parsedData  = example.getDemandData();
			List<Demand> Pickup_time = example.arrangeDemands_Pickuptime(parsedData);
			List<Demand> Drop_time = example.arrangeDemands_Droptime(parsedData);
			System.out.println(Pickup_time);
			System.out.println(parsedData);
			NetworkBuilder net = new NetworkBuilder(1,60,1,1000);
			List<Link> List_Link = net.buildNetwork_1(parsedData, ListofSource);
			List <Link> Ordered_Link = net.arrangeDemands_Pickuptime(List_Link);
			
			Iterator <Link> it =List_Link.iterator();
			Iterator <Link> it_o = Ordered_Link.iterator();
			System.out.println("unordered based on send time....");
			while (it.hasNext())
			{	
				System.out.println(it.next());
				
				
				
				
				
			}
//			
			System.out.println("ordered based on send time....");
			
			while (it_o.hasNext())
			{	
				System.out.println(it_o.next());
				
				
				
				
				
			}
			
			
			
			
//			Iterator <Demand> it_p =Pickup_time.iterator();
//			while (it_p.hasNext())
//			{	
//				System.out.println(it_p.next());
//				
//				
//				
//				
//				
//			}
//			
	
		}
	catch(Exception e)
		{
			System.out.println(e);
			System.out.println(e.toString());
		
		}
	}


	public static int getRandomNumber(int max, int min)
		{
			return (int) (Math.random()*(max-min)+min);
		}

}
