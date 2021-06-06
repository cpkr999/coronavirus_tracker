package com.tracker.coronavirus.controller;

import com.tracker.coronavirus.service.ApiService;
import com.tracker.coronavirus.entity.LocationStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class ApiController 
{
	 @Autowired 
	 private ApiService ApiService;
	 
	 @GetMapping("/")
     public String homeController(Model model)
     {
    	 List<LocationStats> list = ApiService.getAllStats();
    	 int totalcases = list.stream().mapToInt(stat->stat.getLatestCasesCount()).sum();
    	 model.addAttribute("casesCount", totalcases);
    	 model.addAttribute("casesList", list);
    	 return "home";
     }
}
