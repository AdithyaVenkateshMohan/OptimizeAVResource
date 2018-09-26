package com;

public class TripElements {
	
		private double startLat;
		private double startLong;
		private double endLat;
		private double endLong;
		private long pickupTime;
		private long dropoffTime;
		private double tripTime;
		private double tripDistance;
		private int noOfPassenger;
		private int medallion;	 
		private int hackLicense;
		
		public TripElements(double startLat, double startLong, double endLat, double endLong, long pickupTime, long dropoffTime, double tripTime, double tripDistance, int noOfPassenger, int medallion, int hackLicense){
			
			this.startLat = startLat;
			this.startLong = startLong;
			this.endLat = endLat;
			this.endLong = endLong;
			this.pickupTime = pickupTime;
			this.dropoffTime = dropoffTime;
			this.tripTime = tripTime;
			this.tripDistance = tripDistance;
			this.noOfPassenger = noOfPassenger;
			this.medallion = medallion;	 
			this.hackLicense = hackLicense;
		}
		
		public double getstartLat(){
			return startLat;
		}
		
		public double getstartLong(){
			return startLong;
		}
		
		public double getendLat(){
			return endLat;
		}
		
		public double getendLong(){
			return endLong;
		}
		
		public long getpickupTime(){
			return pickupTime;
		}
		
		public long getdropoffTime(){
			return dropoffTime;
		}
		
		public double gettripTime(){
			return tripTime;
		}
		
		public double gettripDistance(){
			return tripDistance;
		}
		
		public int getnoOfPassenger(){
			return noOfPassenger;
		}
		
		public int getmedallion(){
			return medallion;
		}
		
		public int gethackLicense(){
			return hackLicense;
		}

		@Override
		public String toString() {
			return "TripElements [startLat=" + startLat + ", startLong=" + startLong + ", endLat=" + endLat
					+ ", endLong=" + endLong + ", pickupTime=" + pickupTime + ", dropoffTime=" + dropoffTime
					+ ", tripTime=" + tripTime + ", tripDistance=" + tripDistance + ", noOfPassenger=" + noOfPassenger
					+ ", medallion=" + medallion + ", hackLicense=" + hackLicense + "]";
		}
		
		
		

}
