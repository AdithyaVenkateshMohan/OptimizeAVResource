package com;
import java.util.*;

public class Demand_not_met_Exception extends Exception {
	static int Total_Demands_miss=0;
	int Demand_miss_id=0;
	String Error_message;
	Demand Not_satisfied;
	List<Vehicle> Listof_cars = new ArrayList<Vehicle>();
	public Demand_not_met_Exception(String S, Demand d) {
		// TODO Auto-generated constructor stub
		
		this.Error_message=S;
		Demand_not_met_Exception.Total_Demands_miss++;
		this.Demand_miss_id=Demand_not_met_Exception.Total_Demands_miss;
		this.Not_satisfied=d;
		 
	}
	@Override
	public String toString() {
		return "Demand_not_met_Exception [Demand_miss_id=" + Demand_miss_id + ", Error_message=" + Error_message
				+ ", Not_satisfied=" + Not_satisfied + ", Listof_cars=" + Listof_cars + "]";
	}
	
	

}
