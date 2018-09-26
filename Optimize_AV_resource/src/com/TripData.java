package com;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; 
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;



public class TripData {
	
	private static final List<TripElements> tripDataList = new ArrayList<TripElements>();
	private static final List<Demand> ListofDemand = new ArrayList<Demand>();	
	
	
	public void parseTripData (String tripFilePath) throws Exception{

		
		double csvstartLat;
		double csvstartLong;
		double csvendLat;
		double csvendLong;
		int csvpickupTime;
		int csvdropoffTime;
		double csvtripTime;
		double csvtripDistance;
		int csvnoOfPassenger;
		int csvmedallion;	 
		int csvhackLicense;

		try{
			
//			// 1st, creates a CSV parser with the CSV file format
//		    CSVParser parser = new CSVParser(new FileReader(tripFilePath), CSVFormat.EXCEL);
//		    // 2nd, parses all rows from the CSV file into a 2-dimensional array		    
//		    List<CSVRecord>  csvRecords = parser.getRecords();
//		    //check the value of data, e.g., maximum 50 waypoints, speed >= 0
//		    //if all data are valid, add into the array.
//		    
			System.out.println(tripFilePath);
			CSVReader reader = new CSVReader(new FileReader(tripFilePath));
			
			//output tripDataList for debug purpose
			String outputFile = "tripDataLists.csv";
			CSVWriter csvWriter = new CSVWriter (new FileWriter(outputFile, true), ',');
			String [] writerNextLine = new String[11];
			//start from i = 1 to skip header
			String [] nextLine;
			int cnt = 0;
			int cnt1 = 0;
			while ((nextLine = reader.readNext()) != null) {
				
				
				csvstartLat = Double.parseDouble(nextLine[8]);
				csvstartLong = Double.parseDouble(nextLine[7]);
				csvendLat = Double.parseDouble(nextLine[10]);
				csvendLong = Double.parseDouble(nextLine[9]);
				csvpickupTime = Integer.parseInt(nextLine[2]);
				csvdropoffTime = Integer.parseInt(nextLine[3]);
				csvtripTime = Double.parseDouble(nextLine[5]);;
				csvtripDistance = Double.parseDouble(nextLine[6]);;
				csvnoOfPassenger = Integer.parseInt(nextLine[4]);;
				csvmedallion = Integer.parseInt(nextLine[0]);	
				csvhackLicense = Integer.parseInt(nextLine[1]);
				
	    		//tripDataList.add(new TripElements(csvstartLat, csvstartLong, csvendLat, csvendLong, csvpickupTime, csvdropoffTime, csvtripTime, csvtripDistance, csvnoOfPassenger, csvmedallion, csvhackLicense));
	    		cnt++;
	    		

	    
	    		tripDataList.add(new TripElements(csvstartLat, csvstartLong, csvendLat, csvendLong, csvpickupTime, csvdropoffTime, csvtripTime, csvtripDistance, csvnoOfPassenger, csvmedallion, csvhackLicense));
	    		ListofDemand.add(new Demand(csvstartLat,csvstartLong,csvendLat,csvendLong,csvnoOfPassenger,csvtripTime, csvtripDistance));
		    
			
			}
			reader.close();

		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException ioe) {
			throw ioe;
		}
		
		System.out.println("TripData Input Done!");
		
		
	}
	
	

	
	public List<TripElements> getParsedData(){
		return tripDataList;
	}

	public List<Demand> getDemandData(){
	
		return ListofDemand;
	
	}
	
	public List<Demand> arrangeDemands_Pickuptime(List <Demand> Listof_demands)
	{
		List<Demand> Arranged_Demands = new ArrayList <Demand> ();
		Iterator<Demand> it_d = Listof_demands.iterator();
		int i=0;
		while(it_d.hasNext())
			
			
		{
			
			
			Demand Current_check =  it_d.next();
			
			System.out.println(Current_check.getPickupTime().getTime());
			if(i==0)
			{
				
				Arranged_Demands.add(Current_check);
				
				
			}
			else
			{
				int Last = Arranged_Demands.size()-1;
				
				while(Current_check.getPickupTime().getTime()< Arranged_Demands.get(Last).getPickupTime().getTime() )
				{
					
				if (Last ==Arranged_Demands.size()-1)
					{
						
						System.out.println(Last);
						Arranged_Demands.add(Arranged_Demands.get(Last));
						
					}
				else
					{
						
						Arranged_Demands.set(Last+1,Arranged_Demands.get(Last));
					}
				
				if (Last == 0)
				{
					
					Arranged_Demands.set(Last,Current_check);
					break;
					
					
					
				}
				Last--;
				
				}
			
			if (Last ==Arranged_Demands.size()-1)
			{
				Arranged_Demands.add(Current_check);
			}
			
			else
			{	if(Last != 0)
				{
					Arranged_Demands.set(Last+1,Current_check);
				
				}
			}
			}
			
			i++;
			
		}
				
		
	
		return Arranged_Demands; // change
		
	}
	
	
	
	public List<Demand> arrangeDemands_Droptime(List <Demand> Listof_demands)
	{
		List<Demand> Arranged_Demands = new ArrayList <Demand> ();
		Iterator<Demand> it_d = Listof_demands.iterator();
		int i=0;
		while(it_d.hasNext())
			
			
		{
			
			
			Demand Current_check =  it_d.next();
			
			
			if(i==0)
			{
				
				Arranged_Demands.add(Current_check);
				
				
			}
			else
			{
				int Last = Arranged_Demands.size()-1;
				
				while(Current_check.getDropTime().getTime()< Arranged_Demands.get(Last).getDropTime().getTime() )
				{
					
				if (Last ==Arranged_Demands.size()-1)
					{
						
						System.out.println(Last);
						Arranged_Demands.add(Arranged_Demands.get(Last));
						
					}
				else
					{
						
						Arranged_Demands.set(Last+1,Arranged_Demands.get(Last));
					}
				
				if (Last == 0)
				{
					
					Arranged_Demands.set(Last,Current_check);
					break;
					
					
					
				}
				Last--;
				
				}
			
			if (Last ==Arranged_Demands.size()-1)
			{
				Arranged_Demands.add(Current_check);
			}
			
			else
			{	
				if(Last != 0)
				{
					Arranged_Demands.set(Last+1,Current_check);
				
				}
			}
			}
			
			i++;
			
		}
				
		
	
		return Arranged_Demands; // change
		
	}
	
	
	
	}
	
		

