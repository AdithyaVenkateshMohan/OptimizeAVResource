package com;
import java.util.*;

import org.apache.commons.lang3.time.DateUtils;

import java.lang.Math;
public class Demand {
	
	static int TotalDemandNo=0;
	int DemandNo=0; 
	double StartLat;
	double StartLong;
	double DesLat;
	double DesLong;
	double tripdistance;
	double triptime=0;
	double Congestion_factor=0;
	double Distance_expansion=0;
	double Total_dist=0;
	boolean Status_done;
	
	
	int NoofPassengers;
	Date PickupTime = new Date();
	Date DropTime = new Date();
	Vehicle AssignedVechile;
	

	public Demand( double startLat, double startLong, double desLat, double desLong,  int NoofPassengers, double tripdistance , double triptime ) 
	{
		super();
		double totaldistance= calDistance(startLat, startLong, desLat, desLong);
		Demand.TotalDemandNo++;
		this.DemandNo = Demand.TotalDemandNo;
		this.StartLat = startLat;
		this.StartLong = startLong;
		this.DesLat = desLat;
		this.DesLong = desLong;
		this.triptime = triptime;
		this.tripdistance = tripdistance;
		this.NoofPassengers=NoofPassengers;
		this.Total_dist = totaldistance;
		this.Congestion_factor = this.triptime / this.tripdistance;
		this.Distance_expansion = this.tripdistance / totaldistance;
		this.Status_done = false;
		this.PickupTime = getRandomDate(new Date());
		this.DropTime = DateUtils.addSeconds(this.PickupTime,(int) this.triptime);
	}
	
	public Vehicle getAssignedVechile() {
		return AssignedVechile;
	}

	public void setAssignedVechile(Vehicle assignedVechile) {
		AssignedVechile = assignedVechile;
		this.Status_done = true;
	}
	

	public static int getTotalDemandNo() {
		return TotalDemandNo;
	}

	public static void setTotalDemandNo(int totalDemandNo) {
		TotalDemandNo = totalDemandNo;
	}

	public int getDemandNo() {
		return DemandNo;
	}

	public void setDemandNo(int demandNo) {
		DemandNo = demandNo;
	}

	public double getStartLat() {
		return StartLat;
	}

	public void setStartLat(double startLat) {
		StartLat = startLat;
	}

	public double getStartLong() {
		return StartLong;
	}

	public void setStartLong(double startLong) {
		StartLong = startLong;
	}

	public double getDesLat() {
		return DesLat;
	}

	public void setDesLat(double desLat) {
		DesLat = desLat;
	}

	public double getDesLong() {
		return DesLong;
	}

	public void setDesLong(double desLong) {
		DesLong = desLong;
	}

	public double getTripdistance() {
		return tripdistance;
	}

	public void setTripdistance(double tripdistance) {
		this.tripdistance = tripdistance;
	}

	public double getTriptime() {
		return triptime;
	}

	public void setTriptime(double triptime) {
		this.triptime = triptime;
	}

	public int getNoofPassengers() {
		return NoofPassengers;
	}

	public void setNoofPassengers(int noofPassengers) {
		NoofPassengers = noofPassengers;
	}

	public Date getPickupTime() {
		return PickupTime;
	}

	public void setPickupTime(Date pickupTime) {
		PickupTime = pickupTime;
	}

	public Date getDropTime() {
		return DropTime;
	}

	public void setDropTime(Date dropTime) {
		DropTime = dropTime;
	}
	
	
	public double getCongestion_factor() {
		return Congestion_factor;
	}

	public void setCongestion_factor(double congestion_factor) {
		Congestion_factor = congestion_factor;
	}

	public double getDistance_expansion() {
		return Distance_expansion;
	}

	public void setDistance_expansion(double distance_expansion) {
		Distance_expansion = distance_expansion;
	}

	public double getTotal_dist() {
		return Total_dist;
	}

	public void setTotal_dist(double total_dist) {
		Total_dist = total_dist;
	}

	public boolean isStatus_done() {
		return Status_done;
	}

	public void setStatus_done(boolean status_done) {
		Status_done = status_done;
	}

	@Override
	public String toString() {
		return "Demand [DemandNo=" + DemandNo + ", StartLat=" + StartLat + ", StartLong=" + StartLong + ", DesLat="
				+ DesLat + ", DesLong=" + DesLong + ", NoofPassengers=" + NoofPassengers + ", PickupTime=" + PickupTime
				+ ", DropTime=" + DropTime + ", AssignedVechile=" + AssignedVechile + "]";
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
	
	public Date getRandomDate(Date Now)
	{
		Now = DateUtils.addHours(Now, getRandomNumber(0,2));
		Now = DateUtils.addMinutes(Now, getRandomNumber(0,59));
		Now = DateUtils.addSeconds(Now,getRandomNumber(0, 60));
		return Now;
	}
	
	
}
