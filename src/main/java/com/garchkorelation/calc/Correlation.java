package com.garchkorelation.calc;

import java.util.List;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import com.garchkorelation.model.Stock;
import com.garchkorelation.util.Autocorrelation;

public class Correlation {

	public static double[] correlationCoef(List<Stock> stockList) {
		double[] x = new double[stockList.size()];
		double[] y = new double[x.length];
		int i = 0;
		for (Stock stock : stockList) {
			x[i] = stock.getAsk();
			//y[i] = stock.getBid();
			++i;
			new Autocorrelation().fftAutoCorrelation(x, y, y.length);
		}

		return y;
	}

}
