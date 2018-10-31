package com.coinRank.richList.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ListController {
	@RequestMapping(value="listData", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Map<String, Object> getListData(
			@RequestParam(value="coin", required=false, defaultValue="bitcoin") String coin
			,@RequestParam(value="pagingNum", required=false, defaultValue="1") int pagingNum
			) throws Exception{
		Map<String, Object> result = new HashMap<>();
		
		try {
			//0.validation
			String num = "";
			if( pagingNum > 1 ) {
				num = "-";
				num += String.valueOf(pagingNum);
			}
			
			//1. 접속
			String url = "https://bitinfocharts.com/top-100-richest-"+coin+"-addresses"+num+".html";
			Connection.Response response = Jsoup.connect(url)
	                .method(Connection.Method.GET)
	                .execute();
			
			
			//2. html파싱
			Document listDocument = response.parse();
			Elements table = listDocument.select("#tblOne");
			Elements body = table.select("tbody tr");
			Elements tr = body.select("tr");
			
			
			//3. add list
			List<Map<String, String>> list = new ArrayList<>();
			for( Element es : tr ) {
				Elements tds = es.select("td");
				//0.index
				String index = tds.get(0).text().trim();
				//1.address
				Element addressTd = tds.get(1);
				String address= addressTd.selectFirst("a").text().trim();	
	
				//2.balance
				String balance = tds.get(2).text().trim();
				//3.%of coins
				String ofCoins = tds.get(3).text().trim();
				//4.first in
				String firstIn = tds.get(4).text().trim();
				//5.last In
				String lastIn = tds.get(5).text().trim();
				//6.Number Of ints
				String nbIn = tds.get(6).text().trim();
				//7.first Out
				String firstOut = tds.get(7).text().trim();
				//8.lastOut
				String lastOut = tds.get(8).text().trim();
				//9. Number Of out
				String nmOut = tds.get(9).text().trim();
				
				Map<String, String> map = new HashMap<>();
				map.put("index", index);
				map.put("address", address);
				map.put("balance", balance);
				map.put("ofCoins", ofCoins);
				map.put("firstIn", firstIn);
				map.put("lastIn", lastIn);
				map.put("nbIn", nbIn);
				map.put("firstOut", firstOut);
				map.put("lastOut", lastOut);
				map.put("nmOut", nmOut);
				
				
				list.add(map);
			}
			
			
			Elements table2 = listDocument.select("#tblOne2");
			Elements body2 = table2.select("tbody tr");
			Elements tr2 = body2.select("tr");
			
			for( Element es : tr2 ) {
				Elements tds = es.select("td");
				//0.index
				String index = tds.get(0).text().trim();
				//1.address
				Element addressTd = tds.get(1);
				String address= addressTd.selectFirst("a").text().trim();	
				//2.balance
				String balance = tds.get(2).text().trim();
				//3.%of coins
				String ofCoins = tds.get(3).text().trim();
				//4.first in
				String firstIn = tds.get(4).text().trim();
				//5.last In
				String lastIn = tds.get(5).text().trim();
				//6.Number Of ints
				String nbIn = tds.get(6).text().trim();
				//7.first Out
				String firstOut = tds.get(7).text().trim();
				//8.lastOut
				String lastOut = tds.get(8).text().trim();
				//9. Number Of out
				String nmOut = tds.get(9).text().trim();
				
				Map<String, String> map = new HashMap<>();
				map.put("index", index);
				map.put("address", address);
				map.put("balance", balance);
				map.put("ofCoins", ofCoins);
				map.put("firstIn", firstIn);
				map.put("lastIn", lastIn);
				map.put("nbIn", nbIn);
				map.put("firstOut", firstOut);
				map.put("lastOut", lastOut);
				map.put("nmOut", nmOut);
				
				
				list.add(map);
			}
			
			result.put("rows", list);
			result.put("page", pagingNum);
			result.put("total", 100);
			
		} catch (UnknownHostException e) {
			throw new UnknownHostException();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return result;
	}
	
	@RequestMapping(value="/")
	public String view() {
		return "list/list";
	}

}
