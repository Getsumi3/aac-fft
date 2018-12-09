package com.garchkorelation.util;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.garchkorelation.model.Stock;

public class ReadXMLFile {

	public static List<Stock> load() {

		List<Stock> stockList = new ArrayList<Stock>();

		URL url = null;
		try {
			url = new URL(StaticVar.MAIN_XML_URL);
			URLConnection connection = url.openConnection();
			Document doc = parseXML(connection.getInputStream());
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("stock");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String date = TimeUtil.getFormatedDate(eElement.getAttribute("date"));

					Double lastPrice = 0.0;
					Double bid = 0.0;
					Double ask = 0.0;
					Double minDayPrice = 0.0;
					Double maxDayPrice = 0.0;
					Double avgDayPrice = 0.0;
					Long dayTotal = 0L;
					lastPrice = Double.parseDouble(eElement.getAttribute("price").replaceAll(" ", ""));
					bid = Double.parseDouble(eElement.getAttribute("buy").replaceAll(" ", ""));
					ask = Double.parseDouble(eElement.getAttribute("sell").replaceAll(" ", ""));
					if(eElement.getAttribute("min_price").contains("-")) {
						continue;
					}
					minDayPrice = Double.parseDouble(eElement.getAttribute("min_price"));
					maxDayPrice = Double.parseDouble(eElement.getAttribute("max_price"));
					avgDayPrice = Double.parseDouble(eElement.getAttribute("average_price").replaceAll(" ", ""));
					dayTotal = (long) Double.parseDouble(eElement.getAttribute("overturn").replaceAll(" ", ""));
					String percentDay = eElement.getAttribute("change1");
					Stock stock = new Stock(date, lastPrice, bid, ask, minDayPrice, maxDayPrice, avgDayPrice, dayTotal,
							percentDay);
					stockList.add(stock);
				}
			}
		} catch (NumberFormatException ex) {
//			if(!ex.getMessage().equals("empty String")) {
//				ex.printStackTrace();
//			}
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return stockList;
	}

	private static Document parseXML(InputStream stream) throws Exception {
		DocumentBuilderFactory objDocumentBuilderFactory = null;
		DocumentBuilder objDocumentBuilder = null;
		Document doc = null;
		try {
			objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
			doc = objDocumentBuilder.parse(stream);
		} catch (Exception ex) {
			throw ex;
		}
		return doc;
	}

}