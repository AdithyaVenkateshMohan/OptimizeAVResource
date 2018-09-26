package com;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Iterator;

public class NetworkBuilder {

	double TotalHorizon_T=0;
	double SmallHorizon_t=0;
	double Buffer_time_Theta=0;
	static final double Parking_Cost = 10;
	static final double Driving_cost =1000;
	static final double Relocation_Cost = 10;
	static final double Max_Buffer_time=600;
	double Max_Buff_dist_mu=0;
	static int Total_no_refresh=0;
	int Refresh_No=0;
	Date Now = new Date();
	List <Demand> Demands_inHorizon = new ArrayList<Demand> ();
	List <Vehicle> Avail_inHorizon = new ArrayList<Vehicle>();
	
	

	
	public NetworkBuilder() {
		// TODO Auto-generated constructor stub
	}

	public NetworkBuilder(double totalHorizon_T, double smallHorizon_t, double max_buff_time, double Max_Buff_dist ) {
		super();
		TotalHorizon_T = totalHorizon_T;
		SmallHorizon_t = smallHorizon_t;
		this.Buffer_time_Theta = max_buff_time;
		this.Max_Buff_dist_mu = Max_Buff_dist;
	}

	public double getTotalHorizon_T() {
		return TotalHorizon_T;
	}

	public void setTotalHorizon_T(double totalHorizon_T) {
		TotalHorizon_T = totalHorizon_T;
	}
	
	

	public double getSmallHorizon_t() {
		return SmallHorizon_t;
	}

	public void setSmallHorizon_t(double smallHorizon_t) {
		SmallHorizon_t = smallHorizon_t;
	}

	public int getRefresh_No() {
		return Refresh_No;
	}

	public void setRefresh_No(int refresh_No) {
		Refresh_No = refresh_No;
	}

	public Date getFrom() {
		return this.Now;
	}

	public void setFrom(Date from) {
		this.Now = from;
	}
	
	public void updateTime()
	{
	
		this.Now = DateUtils.addMinutes(this.Now, (int) this.getSmallHorizon_t());	
			
			
			
	}
	

	
	public List<Demand> getDemandsInHorizonToBeSent(List<Demand> All_demands)
	{	double Diff_time=0;
		List<Demand> In_Horizon = new ArrayList <Demand>();
		Iterator <Demand> it = All_demands.iterator();
		while(it.hasNext())
		{
			Demand Check_demand = it.next();
			Diff_time = this.Now.getTime() -Check_demand.getPickupTime().getTime();
			if((Diff_time >= 0)&& (Diff_time <= this.TotalHorizon_T)&&(!(Check_demand.isStatus_done())))
			{
				In_Horizon.add(Check_demand);
			}
			
		}
		return In_Horizon;
		
	}
	
	public List<Demand> getDemandsInHorizonForChain(List<Demand> All_demands)
	{	double Diff_time=0;
		List<Demand> In_Horizon = new ArrayList <Demand>();
		Iterator <Demand> it = All_demands.iterator();
		while(it.hasNext())
		{
			Demand Check_demand = it.next();
			Diff_time = this.Now.getTime() -Check_demand.getDropTime().getTime();
			if((Diff_time >= 0)&& (Diff_time <= this.TotalHorizon_T))
			{
				In_Horizon.add(Check_demand);
			}
			
		}
		return In_Horizon;
		
	}
	
	public List<Vehicle> getInAvailVehicle(List<Vehicle> All_Vehicles)
	{	List<Vehicle> Listof_Inavail = new ArrayList<Vehicle> ();
		Iterator <Vehicle> Itv = All_Vehicles.iterator(); 
		while(Itv.hasNext())
		{
			Vehicle Checker = Itv.next();
			if (Checker.getStatus()=="Inavail")
			{
				Listof_Inavail.add(Checker);

			}
			
			
		}
		return Listof_Inavail;
	}
	
	public List<Demand> getInAvailVehicles_Demand(List<Vehicle> All_Vehicles)
	{	
		List <Demand> Listof_Demand = new ArrayList<Demand> ();
		List<Vehicle> Listof_Inavail = new ArrayList<Vehicle> ();
		Iterator <Vehicle> Itv = All_Vehicles.iterator(); 
		while(Itv.hasNext())
		{
			Vehicle Checker = Itv.next();
			if (Checker.getStatus()=="Inavail"&& Checker.getFrom() instanceof Demand && Checker.getNext()==null)
			{
				Listof_Inavail.add(Checker);

			}
			
		
		}
		return Listof_Demand;
		
	}
	
	
	
	
	
	public List<Link> buildNetwork(List<Demand> InHor , List< Vehicle> Inavail , List<Source> Listofsource) throws Demand_not_met_Exception
	{	
		
		
		List <Link> ListOf_PossibleLinks = new ArrayList <Link> ();
		for (Iterator<Demand> Call = InHor.iterator(); Call.hasNext();)
		{	
			Demand Current_Call = Call.next();
			double Count_Possible_Link=0;
			
			
			for(Iterator <Vehicle> Current_Car=Inavail.iterator(); Current_Car.hasNext();)
			{	
				Vehicle Current_vehicle = Current_Car.next();
				
				double Relocation_Direct_dist = calDistance(Current_vehicle.getLatVehicle().get(Current_vehicle.getLatVehicle().size()-1), 
												Current_vehicle.getLongVehicle().get(Current_vehicle.getLongVehicle().size()-1),
												Current_Call.getStartLat(), Current_Call.getStartLong());
				
				
				double Relocation_Con = Current_Call.getCongestion_factor();
				
				double Relocation_Exp = Current_Call.getDistance_expansion();
				
				double Diff_Time = Current_Call.getPickupTime().getTime()
									-Current_vehicle.Timestamp.get(Current_vehicle.Timestamp.size()-1).getTime(); 
				
				// next ride pick up time - previous ride drop time in milliseconds
				Diff_Time = Diff_Time/1000; // convert to seconds
				
				double Move_time = Relocation_Direct_dist*Relocation_Exp*Relocation_Con;
				
				
				// move_time from source to demand loc or demand to demand loc in seconds 
				
				double Inter_Move_time = Diff_Time - Move_time;
				
				// Considering the time to relocate in seconds
				
				if((Inter_Move_time/60) > this.Buffer_time_Theta && (Inter_Move_time/60) <= NetworkBuilder.Max_Buffer_time && Relocation_Direct_dist <= this.Max_Buff_dist_mu)
				{	
					Count_Possible_Link++;
					Link New_Link = new Link();
					New_Link.setLink_Cap(1);
					New_Link.setLink_Cost(((Move_time/3600)*NetworkBuilder.Relocation_Cost)+((Inter_Move_time/3600)*NetworkBuilder.Parking_Cost));
					New_Link.setType("Relocation");
					New_Link.setLink_Distance(Relocation_Direct_dist*Relocation_Exp);
					New_Link.setFrom(Current_vehicle);
					New_Link.setTo(Current_Call);
					New_Link.setLink_Time(Move_time);
					ListOf_PossibleLinks.add(New_Link);

				}
				
			
			}
			System.out.println("error check 3");
			
			for(Iterator <Source> It_Sor=Listofsource.iterator(); It_Sor.hasNext();)
			{	
				
				Source Current_Sor = It_Sor.next();
				
				double Relocation_dist_dir_source = calDistance(Current_Sor.getLocationLat(),Current_Sor.getLocationLong(), 
													Current_Call.getStartLat(), Current_Call.getStartLong());
				
			
				double Relocation_Con = Current_Call.getCongestion_factor();
				
				double Relocation_Exp = Current_Call.getDistance_expansion();
				
				double Diff_Time = Current_Call.getPickupTime().getTime()
									- Current_Sor.getNow().getTime(); 
				
				// next ride pick up time - previous ride drop time in milliseconds
				
				Diff_Time = Diff_Time/1000; // convert to seconds
				
				double Move_time = Relocation_dist_dir_source*Relocation_Exp*Relocation_Con;
				
				
				// move_time from source to demand loc or demand to demand loc in seconds 
				
				double Inter_Move_time = Diff_Time - Move_time;
				
				// Considering the time to relocate in seconds
				
				// No condition Right Now all links will be formed from source 
				
					Count_Possible_Link++;
					Link New_Link = new Link();
					New_Link.setLink_Cap(1);
					New_Link.setLink_Cost(((Move_time/3600)*NetworkBuilder.Relocation_Cost)+((Inter_Move_time/3600)*NetworkBuilder.Parking_Cost));
					New_Link.setType("Relocation");
					New_Link.setLink_Distance(Relocation_dist_dir_source*Relocation_Exp);
					New_Link.setFrom(Current_Sor);
					New_Link.setTo(Current_Call);
					New_Link.setLink_Time(Move_time);
					ListOf_PossibleLinks.add(New_Link);

				
				
			
			}
			 if (Count_Possible_Link == 0)
				{
					// throw an exception that Demand can't be met // Generate Vechile near by Demand
					Demand_not_met_Exception e = new Demand_not_met_Exception("Demand Can't be met",Current_Call);
					throw e;
				}
			 
			 Link Inter_Link = new Link();
			 // Inter_link to From Start to end End same Demand one per Demand
			 Inter_Link.setFrom(Current_Call);
			 Inter_Link.setTo(Current_Call);
			 Inter_Link.setLink_Cap(1);
			 Inter_Link.setLink_Distance(Current_Call.getTripdistance());
			 Inter_Link.setLink_Cost(0-(Driving_cost*Current_Call.getTripdistance()));
			 Inter_Link.setLink_Time(Current_Call.getTriptime());
			 Inter_Link.setType("Engaged");
			 ListOf_PossibleLinks.add(Inter_Link);
			 
			 
			 
			
			
		}
		
		
		
		
		System.out.println("error check end");
		return ListOf_PossibleLinks;
	}
	
	
	
	public List<Link> buildNetwork_2(List<Demand> InHor , List< Vehicle> Inavail , List<Source> Listofsource) throws Demand_not_met_Exception
	{	
		
		
		List <Link> ListOf_PossibleLinks = new ArrayList <Link> ();
		for (Iterator<Demand> Call = InHor.iterator(); Call.hasNext();)
		{	
			Demand Current_Call = Call.next();
			double Count_Possible_Link=0;
			
			List <Demand> Inavail_vehicle = this.getInAvailVehicles_Demand(Inavail);
			
			for(Iterator <Demand> Current_Car=Inavail_vehicle.iterator(); Current_Car.hasNext();)
			{	
				Demand Current_vehicle = Current_Car.next();
				
				if (Current_vehicle == Current_Call)
				{
					continue;
				}
				
				double Relocation_Direct_dist = calDistance(Current_vehicle.getDesLat(), Current_vehicle.getDesLong(),
												Current_Call.getStartLat(), Current_Call.getStartLong());
				
				
				double Relocation_Con = Current_Call.getCongestion_factor();
				
				double Relocation_Exp = Current_Call.getDistance_expansion();
				
				double Diff_Time = Current_Call.getPickupTime().getTime()
									-Current_vehicle.getDropTime().getTime();
				
				// next ride pick up time - previous ride drop time in milliseconds
				Diff_Time = Diff_Time/1000; // convert to seconds
				
				double Move_time = Relocation_Direct_dist*Relocation_Exp*Relocation_Con;
				
				
				// move_time from source to demand loc or demand to demand loc in seconds 
				
				double Inter_Move_time = Diff_Time - Move_time;
				
				// Considering the time to relocate in seconds
				
				if((Inter_Move_time/60) > this.Buffer_time_Theta && (Inter_Move_time/60) <= NetworkBuilder.Max_Buffer_time && Relocation_Direct_dist <= this.Max_Buff_dist_mu)
				{	
					Count_Possible_Link++;
					Link New_Link = new Link();
					New_Link.setLink_Cap(1);
					New_Link.setLink_Cost(((Move_time/3600)*NetworkBuilder.Relocation_Cost)+((Inter_Move_time/3600)*NetworkBuilder.Parking_Cost));
					New_Link.setType("Relocation");
					New_Link.setLink_Distance(Relocation_Direct_dist*Relocation_Exp);
					New_Link.setFrom(Current_vehicle);
					New_Link.setTo(Current_Call);
					New_Link.setLink_Time(Move_time);
					ListOf_PossibleLinks.add(New_Link);

				}
				
			
			}
			System.out.println("error check 3");
			
			for(Iterator <Source> It_Sor=Listofsource.iterator(); It_Sor.hasNext();)
			{	
				
				Source Current_Sor = It_Sor.next();
				
				double Relocation_dist_dir_source = calDistance(Current_Sor.getLocationLat(),Current_Sor.getLocationLong(), 
													Current_Call.getStartLat(), Current_Call.getStartLong());
				
			
				double Relocation_Con = Current_Call.getCongestion_factor();
				
				double Relocation_Exp = Current_Call.getDistance_expansion();
				
				double Diff_Time = Current_Call.getPickupTime().getTime()
									- Current_Sor.getNow().getTime(); 
				
				// next ride pick up time - previous ride drop time in milliseconds
				
				Diff_Time = Diff_Time/1000; // convert to seconds
				
				double Move_time = Relocation_dist_dir_source*Relocation_Exp*Relocation_Con;
				
				
				// move_time from source to demand loc or demand to demand loc in seconds 
				
				double Inter_Move_time = Diff_Time - Move_time;
				
				// Considering the time to relocate in seconds
				
				// No condition Right Now all links will be formed from source 
				
					Count_Possible_Link++;
					Current_Sor.getSendTime(Current_Call, Buffer_time_Theta);
					Link New_Link = new Link();
					New_Link.setLink_Cap(1);
					New_Link.setLink_Cost(((Move_time/3600)*NetworkBuilder.Relocation_Cost)+((Inter_Move_time/3600)*NetworkBuilder.Parking_Cost));
					New_Link.setType("Relocation");
					New_Link.setLink_Distance(Relocation_dist_dir_source*Relocation_Exp);
					New_Link.setFrom(Current_Sor);
					New_Link.setTo(Current_Call);
					New_Link.setLink_Time(Move_time);
					ListOf_PossibleLinks.add(New_Link);

				
				
			
			}
			 if (Count_Possible_Link == 0)
				{
					// throw an exception that Demand can't be met // Generate Vechile near by Demand
					Demand_not_met_Exception e = new Demand_not_met_Exception("Demand Can't be met",Current_Call);
					throw e;
				}
			 
			 Link Inter_Link = new Link();
			 // Inter_link to From Start to end End same Demand one per Demand
			 Inter_Link.setFrom(Current_Call);
			 Inter_Link.setTo(Current_Call);
			 Inter_Link.setLink_Cap(1);
			 Inter_Link.setLink_Distance(Current_Call.getTripdistance());
			 Inter_Link.setLink_Cost(0-(Driving_cost*Current_Call.getTripdistance()));
			 Inter_Link.setLink_Time(Current_Call.getTriptime());
			 Inter_Link.setType("Engaged");
			 ListOf_PossibleLinks.add(Inter_Link);
			 
			 
			 
			
			
		}
		
		
		
		
		System.out.println("error check end");
		return ListOf_PossibleLinks;
	}
	
	
	public List<Link> buildNetwork_1(List<Demand> All_demands , List<Source> Listofsource) throws Demand_not_met_Exception
	{	
		List <Demand> InHor = this.getDemandsInHorizonToBeSent(All_demands);
		
		InHor = All_demands; // remove this is line
		
		System.out.println(All_demands);
		
		List <Link> ListOf_PossibleLinks = new ArrayList <Link> ();
		for (Iterator<Demand> Call = InHor.iterator(); Call.hasNext();)
		{	
			Demand Current_Call = Call.next();
			System.out.println(" check in demand"+ Current_Call);
			double Count_Possible_Link=0;
			// this Demand list contains the vehicles that can for chain link [ Drop time + 10 mins should be in-Horizon] 
			List <Demand> Inavail_vehicle = this.getDemandsInHorizonForChain(All_demands);
			
			for(Iterator <Demand> Current_Car=Inavail_vehicle.iterator(); Current_Car.hasNext();)
			{	
				Demand Current_vehicle = Current_Car.next();
				
				double Relocation_Direct_dist = calDistance(Current_vehicle.getDesLat(), Current_vehicle.getDesLong(),
												Current_Call.getStartLat(), Current_Call.getStartLong());
				
				
				double Relocation_Con = Current_Call.getCongestion_factor();
				
				double Relocation_Exp = Current_Call.getDistance_expansion();
				
				double Diff_Time = Current_Call.getPickupTime().getTime()
									-Current_vehicle.getDropTime().getTime();
				
				// next ride pick up time - previous ride drop time in milliseconds
				Diff_Time = Diff_Time/1000; // convert to seconds
				
				double Move_time = Relocation_Direct_dist*Relocation_Exp*Relocation_Con;
				
				
				// move_time from source to demand loc or demand to demand loc in seconds 
				
				double Inter_Move_time = Diff_Time - Move_time;
				
				// Considering the time to relocate in seconds
				
				if((Inter_Move_time/60) > this.Buffer_time_Theta && (Inter_Move_time/60) <= NetworkBuilder.Max_Buffer_time && Relocation_Direct_dist <= this.Max_Buff_dist_mu)
				{	
					Count_Possible_Link++;
					Link New_Link = new Link();
					New_Link.setLink_Cap(1);
					New_Link.setLink_Cost(((Move_time/3600)*NetworkBuilder.Relocation_Cost)+((Inter_Move_time/3600)*NetworkBuilder.Parking_Cost));
					New_Link.setType("Relocation");
					New_Link.setLink_Distance(Relocation_Direct_dist*Relocation_Exp);
					New_Link.setFrom(Current_vehicle);
					New_Link.setTo(Current_Call);
					New_Link.setLink_Time(Move_time);
					New_Link.setSendTime(Current_vehicle.getDropTime());
					ListOf_PossibleLinks.add(New_Link);

				}
				
			
			}
			
			
			for(Iterator <Source> It_Sor=Listofsource.iterator(); It_Sor.hasNext();)
			{	
				
				Source Current_Sor = It_Sor.next();
				
				System.out.println(Current_Sor.getSourceId() + " to " + Current_Call.getDemandNo());
				
				double Relocation_dist_dir_source = calDistance(Current_Sor.getLocationLat(),Current_Sor.getLocationLong(), 
													Current_Call.getStartLat(), Current_Call.getStartLong());
				
			
				double Relocation_Con = Current_Call.getCongestion_factor();
				
				double Relocation_Exp = Current_Call.getDistance_expansion();
				
				double Diff_Time = Current_Call.getPickupTime().getTime()
									- Current_Sor.getSendTime(Current_Call, Buffer_time_Theta).getTime(); 
				
				// next ride pick up time - previous ride drop time in milliseconds
				
				Diff_Time = Diff_Time/1000; // convert to seconds
				
				double Move_time = Relocation_dist_dir_source*Relocation_Exp*Relocation_Con;
				
				
				// move_time from source to demand loc or demand to demand loc in seconds 
				
				double Inter_Move_time = Diff_Time - Move_time;
				
				// Considering the time to relocate in seconds
				
				// No condition Right Now all links will be formed from source 
				
					Count_Possible_Link++;
					Link New_Link = new Link();
					New_Link.setLink_Cap(1);
					New_Link.setLink_Cost(((Move_time/3600)*NetworkBuilder.Relocation_Cost)+((Inter_Move_time/3600)*NetworkBuilder.Parking_Cost));
					New_Link.setType("Relocation");
					New_Link.setLink_Distance(Relocation_dist_dir_source*Relocation_Exp);
					New_Link.setFrom(Current_Sor);
					New_Link.setTo(Current_Call);
					New_Link.setLink_Time(Move_time);
					New_Link.setSendTime(Current_Sor.getSendTime(Current_Call, Buffer_time_Theta));
					ListOf_PossibleLinks.add(New_Link);
					
					Count_Possible_Link++;
					Link New_Return_Link = new Link();
					New_Link.setLink_Cap(1);
					New_Return_Link.setFrom(Current_Call);
					New_Return_Link.setSendTime(Current_Call.getDropTime());
					New_Return_Link.setTo(Current_Sor);
					New_Return_Link.setType("Relocation");
					New_Return_Link.setLink_Distance(Relocation_dist_dir_source*Relocation_Exp);
					New_Return_Link.setLink_Time(Move_time);
					New_Return_Link.setLink_Cost(((Move_time/3600)*NetworkBuilder.Relocation_Cost));
					ListOf_PossibleLinks.add(New_Return_Link);
				
				
			
			}
			 if (Count_Possible_Link == 0)
				{
					// throw an exception that Demand can't be met // Generate Vechile near by Demand
					Demand_not_met_Exception e = new Demand_not_met_Exception("Demand Can't be met",Current_Call);
					throw e;
				}
			 
			 Link Inter_Link = new Link();
			 // Inter_link to From Start to end End same Demand one per Demand
			 Inter_Link.setFrom(Current_Call);
			 Inter_Link.setTo(Current_Call);
			 Inter_Link.setLink_Cap(1);
			 Inter_Link.setLink_Distance(Current_Call.getTripdistance());
			 Inter_Link.setLink_Cost(0-(Driving_cost*Current_Call.getTripdistance()));
			 Inter_Link.setLink_Time(Current_Call.getTriptime());
			 Inter_Link.setType("Engaged");
			 Inter_Link.setSendTime(Current_Call.getPickupTime());
			 ListOf_PossibleLinks.add(Inter_Link);
			 
			 
			 
			
			
		}
		
		
		
		
		System.out.println("error check end");
		return ListOf_PossibleLinks;
	}
	
	
	public void update_network_after(List<Link> Decision_links, List<Vehicle> Listof_cars , List <Demand>Listof_Demands)
	{
		Iterator<Link> Itl = Decision_links.iterator();
		while(Itl.hasNext())
		{ 
			Link Checker = Itl.next();
			if(Checker.getFrom() instanceof Source && Checker.getTo() instanceof Demand && Checker.getDecision()==true)
			{	
				Source Vehicles_sor = (Source) Checker.getFrom();
				Demand Vehicle_goal = (Demand) Checker.getTo();
				
				
				
				Vehicle Dispatched_one = new Vehicle("VehicleName",Vehicles_sor.getNow(),Vehicles_sor.getLocationLat()
										,Vehicles_sor.getLocationLong(),Vehicles_sor);
			
				
				Dispatched_one.setNext(Vehicle_goal);
				
				Vehicle_goal.setAssignedVechile(Dispatched_one);
				
				Listof_cars.add(Dispatched_one);
				Vehicles_sor.setListofcars(Listof_cars);
				
			}
			else
				if(Checker.getFrom() instanceof Demand && Checker.getTo() instanceof Demand  && Checker.getDecision()==true)
				{
					
					
					
					
					
					
				}
			
			
			
			
			
			
			
			
			
		}
		
		
		
		
		
		
	}
	
	
	
	public List<Link> arrangeDemands_Pickuptime(List <Link> Listof_Links)
	{
		List<Link> Arranged_Demands = new ArrayList <Link> ();
		Iterator<Link> it_d = Listof_Links.iterator();
		int i=0;
		while(it_d.hasNext())
			
			
		{
			
			
			Link Current_check =  it_d.next();
			
			System.out.println(Current_check.getSendTime().getTime());
			if(i==0)
			{
				
				Arranged_Demands.add(Current_check);
				
				
			}
			else
			{
				int Last = Arranged_Demands.size()-1;
				
				while(Current_check.getSendTime().getTime()< Arranged_Demands.get(Last).getSendTime().getTime() )
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
	
	
	
	
	public double calDistance(double lat1, double lon1, double lat2, double lon2)
	
	{
		
		double R = 6372.8;	//meters	
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c * 1000 * 0.000621371; //meters ->  miles
	}
	
	public static int getRandomNumber(int max, int min)
	{
		return (int) (Math.random()*(max-min)+min);
	}

}
