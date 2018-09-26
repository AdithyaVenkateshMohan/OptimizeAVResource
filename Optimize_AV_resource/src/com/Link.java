package com;
import java.util.*;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import ilog.concert.*;

public class Link {
	static int Total_Links=0;
	int Link_ID=0;
	Object From;
	Object To;
	double Link_Cost;
	boolean Decision;
	double Link_Distance;
	String Type; // Try to make this Enum [ Relocation Engaged Parking/Waiting]
	double Link_Time;
	double Link_Cap;
	
	IloNumVar Decisions_Link;

	
	public Date getSendTime() {
		return SendTime;
	}

	public void setSendTime(Date sendTime) {
		SendTime = sendTime;
	}

	Date SendTime = new Date();
	
	public Link() {
		// TODO Auto-generated constructor stub
		this.Total_Links++;
		
		this.Link_ID = this.Total_Links;
	}
	
	public Object getFrom() {
		return From;
	}

	public void setFrom(Object from) {
		From = from;
	}

	public Object getTo() {
		return To;
	}

	public void setTo(Object to) {
		To = to;
	}

	public double getLink_Cost() {
		return Link_Cost;
	}
	

	public IloNumVar getDecisions_Link() {
		return Decisions_Link;
	}

	public void setDecisions_Link(IloNumVar decisions_Link) {
		Decisions_Link = decisions_Link;
	}

	public void setLink_Cost(double link_Cost) {
		Link_Cost = link_Cost;
	}

	public double getLink_Distance() {
		return Link_Distance;
	}

	public void setLink_Distance(double link_Distance) {
		Link_Distance = link_Distance;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public double getLink_Time() {
		return Link_Time;
	}

	public void setLink_Time(double link_Time) {
		Link_Time = link_Time;
	}

	public double getLink_Cap() {
		return Link_Cap;
	}

	public void setLink_Cap(double link_Cap) {
		Link_Cap = link_Cap;
	}

	public Link(Object from, Object to, double link_Cost, double link_Distance, String type, double link_Time,
			double link_Cap) {
		super();
		this.Total_Links++;
		this.Link_ID = this.Total_Links;
		From = from;
		To = to;
		Link_Cost = link_Cost;
		Link_Distance = link_Distance;
		Type = type;
		Link_Time = link_Time;
		Link_Cap = link_Cap;
	}
	
	
	
	

	public boolean getDecision() {
		return Decision;
	}

	public void setDecision(boolean decision) {
		Decision = decision;
	}

	@Override
	public String toString() {
		return "Link [From=" + From + ", To=" + To + ", Link_Cost=" + Link_Cost + ", Link_Distance=" + Link_Distance
				+ ", Type=" + Type + ", Link_Time=" + Link_Time + ", Link_Cap=" + Link_Cap + "send time"+ this.SendTime+ "]";
	}
	
	
}
