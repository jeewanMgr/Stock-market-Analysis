
package classprojects;

import java.util.*;
import java.io.*;

public class symbolTester {
	// member variables
	private String mPath, mSymbol;
	private double mLoss, mTarget;
	private tradeArray mTrades;
	private barArray mData;

	public symbolTester(String symbol, String path, double loss, double target) {
		mPath = path;
		mSymbol = symbol;
		mLoss = loss;
		mTarget = target;
		mTrades = new tradeArray(200, 100);
		mData = new barArray(4000);
	}

	public tradeArray getTrades() {
		return mTrades;
	}

	/*
	 * must make an Expansion Break out (XBO). Make a 60-day high and its range must
	 * be the largest range of the previous 9 trading sessions. Make an inside day
	 * method.
	 */

	// ---> check if highest from the past 60 days
	private boolean XBOdayHigh(int i) {

		for (int j = 59; j > 0; j--) {

			if (mData.elementat(i).High() < mData.elementat(i - j).High()) {

				return false;
			}
		}

		return true;
	}
	
	

	// ---> or check if lowest from the past 60 days
	private boolean XBOdayLow(int i) {

		for (int j = 59; j > 0; j--) {
			if (mData.elementat(i).Low() > mData.elementat(i - j).Low()) {
				return false;
			}
		}

		return true;
	}

	// ---> largest range of the previous 9 trading sessions
	private boolean largestRange(int i) {

		for (int j = 9; j > 0; j--) {
			if (mData.elementat(i).range() <= mData.elementat(i - j).range()) {
				return false;
			}
		}
		return true;
	}

	// ---> calculate inside day
	private boolean insideDay(int i) {
		// ---> i high (XBO) > i+1 high(inside day)
		// ---> i low (XBO) < i+1 low(inside day)
		if ((mData.elementat(i + 1).High() <= mData.elementat(i).High())
				&& (mData.elementat(i + 1).Low() >= mData.elementat(i).Low())) {
			return true;
		}
		return false;
	}

	// check if third days high is higher than the first days high(XBO)
	private boolean thirdDayHigher(int i) {
		if (mData.elementat(i + 2).High() > mData.elementat(i).High()) {
			return true;
		}
		return false;
	}

	// check if third days low is lower than the first days low(XBO)
	private boolean thirdDayLower(int i) {
		if (mData.elementat(i + 2).Low() > mData.elementat(i).Low()) {
			return true;
		}
		return false;
	}

	// check if highest from the past 60 days or lowest
	// check if it has the largest range
	// check for the inside day
	// check if (i+2).high() > (i).High() or (i+2).low < i.low();
	// if all patterns return true create a trade

	// ---> check for the pattern for high
	private boolean patternHigh(int i) {

		if (XBOdayHigh(i) && largestRange(i) && insideDay(i) && thirdDayHigher(i)) {
			return true;
		}

		return false;
	}

	// ---> //---> check for the pattern for low
	private boolean patternLow(int i) {

		if (XBOdayLow(i) && largestRange(i) && insideDay(i) && thirdDayLower(i)) {
			return true;
		}

		return false;
	}

	public boolean test() {

		// load the data into mData

		if (!load()) {
			return false;
		}

		// ------- for 60 day-high -------//

		try {

			for (int i = 59; i < mData.size() - 2; i++) { // start at day 60

				if (patternHigh(i)) { // if pattern true create a trade

					// ---> gets LONG or SHORT depending if XBO high or XBO low
					Direction direct = Direction.LONG;

					// ---> entry date is the third day after the expansion break out
					Date entryDate = mData.elementat(i + 2).getDate();

					// ---> set entry price determined by XBO high or Low
					// ---> buy the day after the inside day with 1/16 = (0.0625) above the XBO's
					// day high
					double entryPrice = mData.elementat(i).High() + 0.0625;

					// ---> set the target and stop loss
					// ---> if long, (i+2).high() >= target
					// if long, (i+2).low > stop loss
					double target = (1 + mTarget) * entryPrice;
					double stopLoss = (1 - mLoss) * entryPrice;

					Trade trade = new Trade();
					trade.open(mSymbol, entryDate, entryPrice, stopLoss, target, direct);

					outcomeLong(trade, i + 2);
					mTrades.insert(trade);

				}

				// -------- for 60-day low --------//

				else if (patternLow(i)) { // if pattern true create a trade

					// ---> gets LONG or SHORT depending if XBO high or XBO low
					Direction direct = Direction.SHORT;

					// ---> entry date is the third day after the expansion break out
					Date entryDate = mData.elementat(i + 2).getDate();

					// ---> set entry price determined by XBO high or Low
					// ---> buy the day after the inside day with 1/16 = (0.0625) below the XBO's
					// day low
					double entryPrice = mData.elementat(i).High() - 0.0625;

					// ---> set the target and stop loss
					// ---> if short, (i+2).low <=target
					// ---> if short, the (i+2).high() < stop loss
					double target = (1 - mTarget) * entryPrice;
					double stopLoss = (1 + mLoss) * entryPrice;

					// ---> now open a trade
					Trade trade = new Trade();
					trade.open(mSymbol, entryDate, entryPrice, stopLoss, target, direct);

					outcomeShort(trade, i + 2);

					// losing trade or winning trade

					mTrades.insert(trade);

				}

			}

		} catch (ArrayIndexOutOfBoundsException exception) {
			System.out.println(exception.toString());
			return false;
		}

		return true;
	}

	public void outcomeLong(Trade T, int index) {

		int holdingPeriod = 0;

		while (index < mData.size()) {
			if (mData.elementat(index).High() >= T.getTarget()) {
				// we have a winner close the trade
				T.setHoldingPeriod(holdingPeriod);
				T.close(mData.elementat(index).getDate(), T.getTarget());
				return;
			} else if (mData.elementat(index).Low() <= T.getStopLoss()) {
				T.setHoldingPeriod(holdingPeriod);
				T.close(mData.elementat(index).getDate(), T.getStopLoss());
				return;

			} else {

				// ---> if the high never reaches the target or the low never reaches the stop
				// loss
				// increment the index and check the next day
				++holdingPeriod;
				index++;
			}
		}

		T.setHoldingPeriod(holdingPeriod);
		T.close(mData.elementat(mData.size() - 1).getDate(), mData.elementat(mData.size() - 1).Close());
		// insert in the array of trades

	}

	public void outcomeShort(Trade T, int index) {

		int holdingPeriod = 0;

		while (index < mData.size()) {
			if (mData.elementat(index).Low() <= T.getTarget()) {
				// we have a winner close the trade
				T.setHoldingPeriod(holdingPeriod);
				T.close(mData.elementat(index).getDate(), T.getTarget());
				return;
			} else if (mData.elementat(index).High() >= T.getStopLoss()) {
				T.setHoldingPeriod(holdingPeriod);
				T.close(mData.elementat(index).getDate(), T.getStopLoss());
				return;

			} else {

				++holdingPeriod;
				index++;
			}

		}

		T.setHoldingPeriod(holdingPeriod);
		T.close(mData.elementat(mData.size() - 1).getDate(), mData.elementat(mData.size() - 1).Close());
	}

	/*
	 * For XBO HIGH - LONG
	 * 
	 * if (i).high() >= target ; exit if (i).low() <= stoploss ; exit if both false
	 * hold. i++; check i+3 and so on.
	 * 
	 * For XBO LOW - SHORT
	 * 
	 * if (i).low() <= target ;exit if (i).high() >= stoploss ; exit if both false
	 * hold. i++; check i+3 and so on.
	 * 
	 */

	private boolean load() {
		// build a file with path name using mPath and mSymbol
		String fileName = mPath + mSymbol + "_Daily.csv";
		// first String fileName = "/Users/CarolinaVargas/Documents/Data/" + "BAC"
		// +"_Daily.csv";
		// check if the file exists (use FILE object)
		File myfile = new File(fileName);
		if (!myfile.exists()) {
			System.out.println("file does not exist");
			return false;
		}
		try {
			FileReader fr = new FileReader(fileName);
			// open the file FileReader->BufferedReader
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();// discard this line
			while ((line = br.readLine()) != null) {
				Bar b = new Bar(line);
				mData.insert(b);
			}
			br.close();
			return true;
		} catch (IOException e) {
			System.out.println(e.toString());
			return false;
		}
	}
}
