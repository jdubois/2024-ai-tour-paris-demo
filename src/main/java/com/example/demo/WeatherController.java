package com.example.demo;

import com.example.demo.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class WeatherController {

    List<Country> countries;

    WeatherController(ResourceLoader resourceLoader) {
        Resource resource = resourceLoader.getResource("classpath:weather.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            countries = objectMapper.readValue(resource.getInputStream(), objectMapper.getTypeFactory().constructCollectionType(List.class, Country.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/")
    public List<Country> index() {
        return countries;
    }

    @GetMapping("/countries/{country}/{city}/{month}")
    public Weather monthlyAverage(@PathVariable String country, @PathVariable String city, @PathVariable String month) {
        return countries.stream()
                .filter(c -> c.name.equals(country))
                .flatMap(c -> c.cities.stream())
                .filter(ci -> ci.name.equals(city))
                .map(ci -> ci.weather.get(month))
                .findFirst()
                .orElse(null);
    }
}
