package com.garchkorelation.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.garchkorelation.bean.ChartBean;
import com.garchkorelation.calc.Correlation;
import com.garchkorelation.service.StockService;
import com.garchkorelation.util.Autocorrelation;

@RestController
@RequestMapping(path = { "/rest" })
public class StockRESTController {

	@Autowired
	private StockService stockService;

	@RequestMapping(path = "/refreshDB", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void resfreshDB() {
		stockService.clearAll();
		stockService.saveAll();
	}

	@RequestMapping(path = { "/test" }, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void test() {
		for(double item: Correlation.correlationCoef(stockService.getAll())){
			System.out.println(item);	
		}

	}

	@RequestMapping(path = { "/getChart" }, method = RequestMethod.GET)
	@ResponseBody
	public List<ChartBean> getChart() {
		return stockService.getChart();
	}
	
	@RequestMapping(path = { "/getChartByDate" }, method = RequestMethod.GET)
	@ResponseBody
	public List<ChartBean> getChartByDate(@RequestParam("start")String start,@RequestParam("end")String end, Model model)  {
		return stockService.getChartByDate(start, end);
	}
	
	

}
