package com.garchkorelation.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stock")
public class Stock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 631095147006950636L;

	private Long id;

	private String date;
	private Double lastPrice;
	private Double bid;
	private Double ask;
	private Double minDayPrice;
	private Double maxDayPrice;
	private Double avgDayPrice;
	private Long dayTotal;
	private String percentDay;

	public Stock() {

	}

	public Stock(String date, Double lastPrice, Double bid, Double ask, Double minDayPrice, Double maxDayPrice,
			Double avgDayPrice, Long dayTotal, String percentDay) {
		super();
		this.date = date;
		this.lastPrice = lastPrice;
		this.bid = bid;
		this.ask = ask;
		this.minDayPrice = minDayPrice;
		this.maxDayPrice = maxDayPrice;
		this.avgDayPrice = avgDayPrice;
		this.dayTotal = dayTotal;
		this.percentDay = percentDay;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(Double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public Double getBid() {
		return bid;
	}

	public void setBid(Double bid) {
		this.bid = bid;
	}

	public Double getAsk() {
		return ask;
	}

	public void setAsk(Double ask) {
		this.ask = ask;
	}

	public Double getMinDayPrice() {
		return minDayPrice;
	}

	public void setMinDayPrice(Double minDayPrice) {
		this.minDayPrice = minDayPrice;
	}

	public Double getMaxDayPrice() {
		return maxDayPrice;
	}

	public void setMaxDayPrice(Double maxDayPrice) {
		this.maxDayPrice = maxDayPrice;
	}

	public Double getAvgDayPrice() {
		return avgDayPrice;
	}

	public void setAvgDayPrice(Double avgDayPrice) {
		this.avgDayPrice = avgDayPrice;
	}

	public Long getDayTotal() {
		return dayTotal;
	}

	public void setDayTotal(Long dayTotal) {
		this.dayTotal = dayTotal;
	}

	public String getPercentDay() {
		return percentDay;
	}

	public void setPercentDay(String percentDay) {
		this.percentDay = percentDay;
	}

}
