package com.tracker.coronavirus.service;

import com.tracker.coronavirus.entity.LocationStats;
import java.util.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class ApiService {

	private List<LocationStats> allStats = new ArrayList<>();
	
	
	public List<LocationStats> getAllStats() {
		return allStats;
	}

//	public void setAllStats(List<LocationStats> allStats) {
//		this.allStats = allStats;
//	}

	private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	
	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchData() throws URISyntaxException, IOException, InterruptedException
	{
		List<LocationStats> newStats = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				  .uri(URI.create(VIRUS_DATA_URL))
				  .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		//System.out.println(response.body());
		
		Reader in = new StringReader(response.body());
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		for (CSVRecord record : records) {
			LocationStats stats = new LocationStats();
			stats.setState(record.get("Province/State"));
		    stats.setCountry(record.get("Country/Region"));
		    stats.setLatestCasesCount(Integer.parseInt(record.get(record.size()-1)));
		    System.out.println(stats);
		    newStats.add(stats);
		}
		this.allStats = newStats;
	}
}
