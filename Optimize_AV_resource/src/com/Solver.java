package com;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ilog.cplex.IloCplex;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;


import ilog.concert.*;


public class Solver {
	
	List <Link> AllPossible_Links = new ArrayList<Link>();
	List <Source> All_Source = new ArrayList <Source>();
	List <Demand> Considered_Demand = new ArrayList<Demand>();
	int TotalFleetSize;

	public Solver() {
		// TODO Auto-generated constructor stub
	}
	

	public Solver(List <Link> AllPossible_Links , int Fleet , List <Source> All_source, List<Demand> Considered)
	{
		this.AllPossible_Links = AllPossible_Links;
		this.TotalFleetSize = Fleet;
		this.All_Source = All_source;
		this.Considered_Demand = Considered;
		
		
		
	}
	
	public void seprateLinkRoutes(List <Link> AllPossible_Links , List <Link> SourceToDemand , List<Link> DemandToDemand_same , List <Link> DemandToDemand_Diff, List <Link> DemandToSource) 
	{
		
		
		
		
			
			
			
			Iterator<Link> Itl = AllPossible_Links.iterator();
			while(Itl.hasNext())
			{ 
				Link Checker = Itl.next();
			
			if(Checker.getFrom() instanceof Source && Checker.getTo() instanceof Demand)
			{
				SourceToDemand.add(Checker);

			
			}
			
			if(Checker.getFrom() instanceof Demand && Checker.getTo() instanceof Demand)
			{
				if((Demand) Checker.getFrom()==(Demand) Checker.getTo() )
				{
					DemandToDemand_same.add(Checker);
				}
				else
				{
					DemandToDemand_Diff.add(Checker);
				}

			
			}
			if(Checker.getTo() instanceof Source && Checker.getFrom() instanceof Demand)
			{
				DemandToSource.add(Checker);

			
			}
			
			}
	}
	
	public List<Link> getLinksSpecific_forEqu(List<Link>ALL_poss_ordered, Object Check ,String Type)
	{
		List<Link> Final_Linklist = new ArrayList<Link> ();
		Iterator <Link> It_l = ALL_poss_ordered.iterator();
	
		while(It_l.hasNext())
		{
			Link Check_cur =  It_l.next();
			
			if(Type == "From")
			{
				if ((Object)Check_cur.getFrom() == Check && Check_cur.getFrom()!= Check_cur.getTo() )
				{
					
					Final_Linklist.add(Check_cur);
						
					
				}
				
				
			}
			
			else
			{	
				if (Type == "To")
					{
				
				

					if ((Object)Check_cur.getTo() == Check && Check_cur.getFrom()!= Check_cur.getTo() )
					{
						
						Final_Linklist.add(Check_cur);
							
						
					}
					
				
				
				
				
					}
				
				else
				{
					
					if ( Check_cur.getFrom()== Check_cur.getTo() && (Object)Check_cur.getFrom()== Check )
					{
						
						Final_Linklist.add(Check_cur);
							
						
					}
					
					
					
					
					
				}
			}
				
		}
		
		
		
		
		
		
		
		
		
		
		return Final_Linklist;
	}
	
	
	public List<Link> getLinksGeneric_forEqu(List<Link>ALL_poss_ordered, String Name ,String Type)
	{
		List<Link> Final_Linklist = new ArrayList<Link> ();
		Iterator <Link> It_l = ALL_poss_ordered.iterator();
	
		while(It_l.hasNext())
		{
			Link Check_cur =  It_l.next();
			
			if(Type == "From")
			{
				if ((Object)Check_cur.getFrom() == Check && Check_cur.getFrom()!= Check_cur.getTo() )
				{
					
					Final_Linklist.add(Check_cur);
						
					
				}
				
				
			}
			
			else
			{	
				if (Type == "To")
					{
				
				

					if ((Object)Check_cur.getTo() == Check && Check_cur.getFrom()!= Check_cur.getTo() )
					{
						
						Final_Linklist.add(Check_cur);
							
						
					}
					
				
				
				
				
					}
				
				else
				{
					
					if ( Check_cur.getFrom()== Check_cur.getTo() && (Object)Check_cur.getFrom()== Check )
					{
						
						Final_Linklist.add(Check_cur);
							
						
					}
					
					
					
					
					
				}
			}
				
		}
		
		
		
		
		
		
		
		
		
		
		return Final_Linklist;
	}
	
	
	
	
	
	public void solve_Linear()
	{
		Iterator <Link> It_l = this.AllPossible_Links.iterator();
		try
		{
			IloCplex Cplex = new IloCplex();	
			
			IloLinearNumExpr obj = Cplex.linearNumExpr();
			
			IloLinearNumExpr Constraints;
			Constraints = Cplex.linearNumExpr();
			
		while (It_l.hasNext())
		{
			
			Link Current = It_l.next();
			Current.setDecisions_Link(Cplex.boolVar());
			// Calculating Objective
			
			obj.addTerm(Current.getLink_Cost(),Current.getDecisions_Link());
			
			// adding flow capacity <= 1
			Constraints.addTerm(1,Current.getDecisions_Link());
			
			Cplex.addGe(1, Constraints);
			
			
			
			
		}
		
		
		// minimize objective
		
		Cplex.addMinimize(obj);
		
		Constraints = Cplex.linearNumExpr();
		
		
		
		}
		catch(Exception e)
		{
			
			
			
			
			
			
			
			
		}
		
		
		
		
		
		
		
	}
	
	

}
