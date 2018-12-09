package com.garchkorelation.service;

import java.util.List;

import com.garchkorelation.bean.ChartBean;
import com.garchkorelation.model.Stock;

public interface StockService {

	public void save(Stock stock);
	
	public void clearAll();
	
	public List<Stock> getAll();
	
	public List<Stock> getByDate(String start, String end);
	
	public void saveAll();
	
	public List<ChartBean> getChart();
	
	public List<ChartBean> getChartByDate(String start, String end);
	
}
