package com.garchkorelation.bean;

public class ChartBean {

	private String date;
	private Double value;

	public ChartBean() {
	}

	public ChartBean(String date, Double value) {
		super();
		this.date = date;
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
