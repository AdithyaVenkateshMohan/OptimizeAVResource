package com;

import java.util.*;

public class Source {
	
	int SourceId=0;
	static int TotalSource;
	int No_of_Vechile;
	int Cap_charge=0;
	double LocationLat;
	double LocationLong;
	Date Now = new Date(); // For Now have to be in sync with horizon time peroid
	
	// one to many relationship
	
	List <Vehicle> Listofcars = new ArrayList <Vehicle>();
	
	
	
	public Source(int no_of_Vechile, List<Vehicle> listofcars,double LocationLat, double Locationlong, int charge_cap) {
		super();
		this.TotalSource++;
		this.SourceId = this.TotalSource;
		this.Cap_charge = charge_cap;
		this.LocationLat = LocationLat;
		this.LocationLong = Locationlong;
		No_of_Vechile = no_of_Vechile;
		Listofcars = listofcars;
		
	}

	public Source(int no_of_Vechile,double LocationLat, double Locationlong, int charge_cap) {
		super();
		this.TotalSource++;
		this.SourceId = this.TotalSource;
		this.Cap_charge = charge_cap;
		this.LocationLat = LocationLat;
		this.LocationLong = Locationlong;
		No_of_Vechile = no_of_Vechile;
		
		
	}

	

	public int getNo_of_Vechile() {
		return No_of_Vechile;
	}



	public void setNo_of_Vechile(int no_of_Vechile) {
		No_of_Vechile = no_of_Vechile;
	}



	public int getCap_charge() {
		return Cap_charge;
	}



	public void setCap_charge(int cap_charge) {
		Cap_charge = cap_charge;
	}



	public double getLocationLat() {
		return LocationLat;
	}



	public void setLocationLat(double locationLat) {
		LocationLat = locationLat;
	}



	public double getLocationLong() {
		return LocationLong;
	}



	public void setLocationLong(double locationLong) {
		LocationLong = locationLong;
	}



	public List<Vehicle> getListofcars() {
		return Listofcars;
	}



	public void setListofcars(List<Vehicle> listofcars) {
		Listofcars = listofcars;
	}
	
	



	public int getSourceId() {
		return SourceId;
	}



	public void setSourceId(int sourceId) {
		SourceId = sourceId;
	}



	public Date getNow() {
		return Now;
	}



	public void setNow(Date now) {
		Now = now;
	}



	public Source() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Source [SourceId=" + SourceId + ", No_of_Vechile=" + No_of_Vechile + ", Cap_charge=" + Cap_charge
				+ ", LocationLat=" + LocationLat + ", LocationLong=" + LocationLong + ", Now=" + Now + ", Listofcars="
				+ Listofcars + "]";
	}
	
	public Date getSendTime(Demand d , double theta)
	{
		double Relocation_dist_dir_source = calDistance(this.getLocationLat(),this.getLocationLong(), 
				d.getStartLat(), d.getStartLong());


		double Relocation_Con = d.getCongestion_factor();

		double Relocation_Exp = d.getDistance_expansion();

		double Inter_Move_time = theta *60 *2;  // Intermove_time > buffer time ( will increase in parking in cost)

		double Move_time = Relocation_dist_dir_source*Relocation_Exp*Relocation_Con;

		double Diff_Time = Move_time + Inter_Move_time;

		long Diff_long = (long ) Diff_Time*1000;

		long Send_Time = d.getPickupTime().getTime() - Diff_long;

		Date Send_timestamp = new Date();
		Send_timestamp.setTime((long)Send_Time);

		
		System.out.println(Send_timestamp);
		System.out.println("pick up");
		System.out.println(d.getPickupTime());




// move_time from source to demand loc or demand to demand loc in seconds 


		
		
		return Send_timestamp;
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
	

}
