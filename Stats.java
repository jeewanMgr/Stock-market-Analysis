package classprojects;
/* Homework 4
 jeewan Thapa magar*/
public class Stats {
	
	public int numberOfTrades, numberLong, numberShort, numberWinners, numberLosers;
	public int longWinners, longLosers, shortWinners, shortLosers;
	public int numberDays, numberLongDays, numberShortDays;
	double totalWinnings, totalLoss, totalLongWinnings, totalLongLoss, totalShortWinnings;
	double totalShortLoss;
	
	public Stats() {
		numberOfTrades= numberLong= numberShort= numberWinners= numberLosers = 0;
		longWinners= longLosers= shortWinners= shortLosers = 0;
		numberDays= numberLongDays= numberShortDays=0;
		totalWinnings= totalLoss= totalLongWinnings= totalLongLoss= totalShortWinnings = 0.0;
		totalShortLoss = 0.0;
		
	}
	
	//TODO
	public String toString() {
		//display  numberOfTrades PercentWinners(numberWinners/numberOfTrades*100) AverageProfit((totalWinnings+ totalLoss)/numberOfTrades)
		
		//numberLong, PercentLongwinners, averageProfitWinners, numberShort, percent Shortwinners, averageProfitShort, averageHoldingPeriod(numberDays/numberOfTrades)
		//numbers separated by comma.
		
		if(numberOfTrades==0)
		{
			return "no data";
		}

		double PercentWinners = (double)numberWinners/numberOfTrades*100.0;
		double AverageProfit = ((totalWinnings+ totalLoss)/numberOfTrades);
		double PercentLongwinners = (double)totalLongWinnings/numberOfTrades*100.0;
		double averageProfitWinners = totalWinnings/numberOfTrades;
		double percentShortWinners = (double)totalShortWinnings/numberOfTrades*100.0;
		double averageProfitShort = ((totalShortWinnings+ totalShortLoss)/numberOfTrades);
		double averageProfitLong = ((totalLongWinnings+ totalLongLoss)/numberOfTrades);
		double averageHoldingPeriod = (numberDays/numberOfTrades);
		String st = " Number of trades: " + numberOfTrades + " PercentW: " +PercentWinners +  " Averageprofit: "+AverageProfit 
				+ " NumberofLong: "+  numberLong  +" PercentLongWinner: "+ PercentLongwinners + " AverageProfitWinners: " + averageProfitWinners
				+ " NumberofShort: "  + numberShort  + " PercantageShortWinners " + percentShortWinners + " AverageProfitShort: " +  + averageProfitShort
				+ " AverageProfitLong: "  + averageProfitLong + " Averageholdingperiod: " +  + averageHoldingPeriod;
		return st;
		
	}
		
	}


