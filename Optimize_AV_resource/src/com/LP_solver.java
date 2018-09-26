package com;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import ilog.concert.*;


import java.util.ArrayList;

public class LP_solver {
	List <Link> AllPossible_Links = new ArrayList<Link>();
	List <Source> All_Source = new ArrayList <Source>();
	List <Demand> Considered_Demand = new ArrayList<Demand>();
	int TotalFleetSize;

	
	
	public LP_solver() {
		// TODO Auto-generated constructor stub
	}
	
	public LP_solver(List <Link> AllPossible_Links , int Fleet , List <Source> All_source, List<Demand> Considered)
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
	
	
	public List<Link> getAllLinksAddressed_toDemand(Demand Check, List<Link> All_possible, List<Integer> Index_Decision)
	{
		List <Link> Final_List = new ArrayList<Link>();
		int i=0;
		for (Iterator<Link> It_Links = All_possible.iterator();It_Links.hasNext();)
		{
			
		Link Refer = It_Links.next();	
		Demand Target = (Demand)Refer.getTo();
		if (Check == Target)
		{
			
			Final_List.add(Refer);
			
			Index_Decision.add(i);
			
			
		}
			
			
			
			
			i++;
			
		}
		
		
		
		
		
		
		
		return Final_List;
	}
	
	
	public List<Link> getAllLinksAddressed_FromDemand(Demand Check, List<Link> All_possible, List<Integer> Index_Decision)
	{
		List <Link> Final_List = new ArrayList<Link>();
		int i=0;
		for (Iterator<Link> It_Links = All_possible.iterator();It_Links.hasNext();)
		{
			
		Link Refer = It_Links.next();	
		Demand Target = (Demand)Refer.getFrom();
		if (Check == Target)
		{
			
			Final_List.add(Refer);
			
			Index_Decision.add(i);
			
			
		}
			
			
			
			
			i++;
			
		}
		
		
		
		
		
		
		
		return Final_List;
	}
			
	public void solveLP(List<Link> AllLinksPossible)
	{
		List <Link> SourceToDemand = new ArrayList<Link> ();
		List <Link> DemandToDemand_paid = new ArrayList<Link> ();
		List <Link> DemandToDemand_chain = new ArrayList<Link> ();
		List <Link> DemandToSource = new ArrayList<Link> ();
		
		List <Integer> SourceToDemandIndex = new ArrayList<Integer> ();
		List <Integer> DemandToDemand_paidIndex = new ArrayList<Integer> ();
		List <Integer> DemandToDemand_chainIndex = new ArrayList<Integer> ();
		List <Integer> DemandToSourceIndex = new ArrayList<Integer> ();
		
		seprateLinkRoutes(AllLinksPossible, SourceToDemand, DemandToDemand_paid, DemandToDemand_chain, DemandToSource);
		try
		{
		IloCplex Cplex = new IloCplex();
		
		IloNumVar [] Decisions_Link = Cplex.boolVarArray(AllLinksPossible.size());
		IloLinearNumExpr obj = Cplex.linearNumExpr();
		Iterator<Link> Itl = AllPossible_Links.iterator();
		int i=0;
		while(Itl.hasNext())
		{ 
			Link Checker = Itl.next();
			obj.addTerm(Checker.getLink_Cost(),Decisions_Link[i]);
		
		if(Checker.getFrom() instanceof Source && Checker.getTo() instanceof Demand)
		{
			
			SourceToDemandIndex.add(i);
		
		}
		
		if(Checker.getFrom() instanceof Demand && Checker.getTo() instanceof Demand)
		{
			if((Demand) Checker.getFrom()==(Demand) Checker.getTo() )
			{
				DemandToDemand_paidIndex.add(i);
				
			}
			else
			{
				DemandToDemand_chainIndex.add(i);
			}

		
		}
		if(Checker.getTo() instanceof Source && Checker.getFrom() instanceof Demand)
		{
			
			DemandToSourceIndex.add(i);
		
		}
		i++;
		
		}
		
		Cplex.addMinimize(obj);
		
		IloLinearNumExpr Constraints;
		Constraints = Cplex.linearNumExpr();
		
		// Link Capacity constraint set to 1
		
		for(int ij=0;ij<AllLinksPossible.size();ij++)
		{
			
			Constraints.addTerm(1,Decisions_Link[ij]);
			
			Cplex.addGe(1, Constraints);
			
			
		}
		
		// All Source Cap constraint set to Total Source Capacity
		
		
		Constraints = Cplex.linearNumExpr();
		
		for (int ic=0 ; ic< SourceToDemand.size()-1 ; ic++)
		{
			
			
			Constraints.addTerm(1,Decisions_Link[SourceToDemandIndex.get(ic)]);
			
			
		}
		
		Cplex.addGe(this.TotalFleetSize, Constraints);
		
		Constraints = Cplex.linearNumExpr();
		
		for (int ic=0 ; ic< DemandToSource.size()-1 ; ic++)
		{
			
			
			Constraints.addTerm(1,Decisions_Link[DemandToSourceIndex.get(ic)]);
			
			
		}
		
		Cplex.addGe(this.TotalFleetSize, Constraints);
		
		// add indiviual Source constraints set to source cap
		
		Constraints = Cplex.linearNumExpr();
		
		Iterator <Source> It_s = this.All_Source.iterator();
		i=0;
		while(It_s.hasNext())
		{
			Source Check_sor = It_s.next();
			Iterator <Link> It_sd = SourceToDemand.iterator();
			while (It_sd.hasNext())
			{
				Link Cur_one = It_sd.next();
				Source Cur_sor = (Source)Cur_one.getFrom();
				if (Cur_sor == Check_sor)
				{
					
					Constraints.addTerm(1,Decisions_Link[SourceToDemandIndex.get(i)]);
					
					
				}
				
				
				
				i++;
				
			}
			
			
			Cplex.addGe(Check_sor.getNo_of_Vechile(), Constraints);
			
			Iterator <Link> It_ds = DemandToSource.iterator();
			i=0;
			while (It_ds.hasNext())
			{
				Link Cur_one = It_ds.next();
				Source Cur_sor = (Source)Cur_one.getTo();
				if (Cur_sor == Check_sor)
				{
					
					Constraints.addTerm(1,Decisions_Link[DemandToSourceIndex.get(i)]);
					
					
				}
				
				
				
				i++;
				
			}
			
			
			Cplex.addGe(Check_sor.getNo_of_Vechile(), Constraints);
			
			
			
		}
		

		
		Iterator <Link> It_sd = SourceToDemand.iterator();
		Iterator <Link> It_dd = DemandToDemand_chain.iterator();
		Iterator <Demand> It_CD = this.Considered_Demand.iterator();
		
		i=0;
		int f=0;
		while (It_CD.hasNext())
		{
			Constraints = Cplex.linearNumExpr();
			Demand Tocheck = It_CD.next();
			
			List <Integer> Index_Deci = new ArrayList<Integer> ();
			
			List <Link> AddressedTo = this.getAllLinksAddressed_toDemand(Tocheck, this.AllPossible_Links, Index_Deci);
			
			Iterator<Link> It_Ad = AddressedTo.iterator();
			
			
			i=0;
			while(It_Ad.hasNext())
			{
				Link SD = It_Ad.next();
				Demand Addressed_Demand = (Demand) SD.getTo();
				Constraints.addTerm(Decisions_Link[Index_Deci.get(i)], 1);
				i++;
			}
			
			Cplex.addEq(1, Constraints);
			
			
			Constraints = Cplex.linearNumExpr();
			
			
			List <Integer> Index_Deci2 = new ArrayList<Integer> ();
			
			List <Link> AddressedFrom = this.getAllLinksAddressed_FromDemand(Tocheck, this.AllPossible_Links, Index_Deci2);
			
			Iterator <Link> It_Afd = AddressedFrom.iterator();
			
			
			
			i=0;
			while(It_Afd.hasNext())
			{
				Link DS = It_Afd.next();
				Demand Addressed_From = (Demand) DS.getFrom();
				
				Constraints.addTerm(Decisions_Link[Index_Deci2.get(i)],1);
				i++;
				
				
				
			}
			
			Cplex.addEq(1, Constraints);
			
			
		}
		
	
	
		
		}
		catch (Exception e)
		{
			
		}
		
		
	}
			
			
			
			
			
			
			
			
	

			}
	
	
	
