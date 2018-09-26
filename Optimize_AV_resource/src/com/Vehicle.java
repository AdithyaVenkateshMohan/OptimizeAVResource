/**
 * 
 */
package com;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;



/**
 * @author adith
 *
 */
public class Vehicle {

	/**
	 * @param args
	 */
	
	// may be used for id 
	String LiciencePlate;
	static int TotalVehicleNo= 0 ;
	
	// will contain only 4 - [busy - avail - returning - Relocating- Parked] try Enum for Better  
	String status;
	// primarykey - for optimizing using Cplex
	int VehicleNo=0;
	static final double Waiting_time=10;
	double Waiting_counter=0;
	
	double Charge_fuelAmt = 100;
	
	List<String> Status_list = new ArrayList<String>();
	List <Date> Timestamp = new ArrayList<Date>();
	List  <Double> LatVehicle = new ArrayList<Double>();
	List <Double> LongVehicle= new ArrayList<Double>();
	
	// one to one relationships 
	Object From;
	Object Next;
	
	public Vehicle(String liciencePlate, Date timestamp, double latVehicle,double longVehicle)
	{
		super();
		LiciencePlate = liciencePlate;
		this.status = "Avail";
		Vehicle.TotalVehicleNo++;
		VehicleNo = Vehicle.TotalVehicleNo;
		this.Status_list.add(this.status);
		this.Timestamp.add(timestamp);
		this.LatVehicle.add(latVehicle);
		this.LongVehicle.add(longVehicle);
	}
	
	public Vehicle(String liciencePlate, Date timestamp, double latVehicle,double longVehicle, Source Org)
	{
		super();
		LiciencePlate = liciencePlate;
		this.status = "Avail";
		Vehicle.TotalVehicleNo++;
		VehicleNo = Vehicle.TotalVehicleNo;
		this.Timestamp.add(timestamp);
		this.LatVehicle.add(latVehicle);
		this.LongVehicle.add(longVehicle);
		this.From = Org;
		this.LongVehicle.add(Org.getLocationLat());
		this.LatVehicle.add(Org.getLocationLong());
	}
	
	
	
	public boolean updateLocation(Date CurrentTime, double CurrentLat , double CurrentLong , String Cur_status)
	{
		this.Timestamp.add(CurrentTime);
		this.LongVehicle.add(CurrentLong);
		this.LatVehicle.add(CurrentLat);
		this.Status_list.add(Cur_status);
		return true;
	}
	
	
	
	public Object getNext() {
		return Next;
		
	}

	public void setNext(Object customer) {
		if(this.Next   != null)
		{
			
			this.From = this.Next;
			
			
		}
		this.Next = customer;
		if (this.Next instanceof Demand)
		{
			if(((Demand)this.From) != ((Demand)this.Next))
			{
				updateLocation(((Demand) Next).getPickupTime(), ((Demand) Next).getStartLat(),((Demand) Next).getStartLong(),"Busy");
			}
			else
			{
				updateLocation(((Demand) Next).getDropTime(), ((Demand) Next).getDesLat(),((Demand) Next).getDesLong(),"Inavail");
				
			}
		}
		
		if (this.Next instanceof Source)
		{
			this.Waiting_counter++;
			
			if (this.Waiting_counter > Vehicle.Waiting_time)
			{
				updateLocation(((Source) Next).getNow(), ((Source) Next).getLocationLat(),((Source) Next).getLocationLong(),"Relocation");
			}
			
		}
	}
	
	public Object getFrom() {
		return From;
	}

	public void setFrom(Source customer) {
		
		this.From = (Source) customer;
		this.Next=null;
		
	}
	
	public String getLiciencePlate() {
		return LiciencePlate;
	}

	public void setLiciencePlate(String liciencePlate) {
		LiciencePlate = liciencePlate;
	}

	public static int getTotalVehicleNo() {
		return TotalVehicleNo;
	}

	public static void setTotalVehicleNo(int totalVehicleNo) {
		TotalVehicleNo = totalVehicleNo;
	}

	public int getVehicleNo() {
		return VehicleNo;
	}

	public void setVehicleNo(int vehicleNo) {
		VehicleNo = vehicleNo;
	}

	public List<Date> getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(List<Date> timestamp) {
		Timestamp = timestamp;
	}

	public List<Double> getLatVehicle() {
		return LatVehicle;
	}

	public void setLatVehicle(List<Double> latVehicle) {
		LatVehicle = latVehicle;
	}

	public List<Double> getLongVehicle() {
		return LongVehicle;
	}

	public void setLongVehicle(List<Double> longVehicle) {
		LongVehicle = longVehicle;
	}
	
	
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getCharge_fuelAmt() {
		return Charge_fuelAmt;
	}

	public void setCharge_fuelAmt(double charge_fuelAmt) {
		Charge_fuelAmt = charge_fuelAmt;
	}

	

	public List<String> getStatus_list() {
		return Status_list;
	}

	public void setStatus_list(List<String> status_list) {
		Status_list = status_list;
	}

	@Override
	public String toString() {
		return "Vehicle [LiciencePlate=" + LiciencePlate + ", VehicleNo=" + VehicleNo + ", Timestamp=" + Timestamp
				+ ", LatVehicle=" + LatVehicle + ", LongVehicle=" + LongVehicle + "]";
	}

	

}
