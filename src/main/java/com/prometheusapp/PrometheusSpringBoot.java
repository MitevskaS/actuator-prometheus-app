package com.prometheusapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class PrometheusSpringBoot {
	private final String[] fruits = {
		"Apple",
		"Mango",
		"Peach",
		"Banana",
		"Orange",
		"Grapes",
		"Watermelon",
		"Tomato"
	};
	private final MeterRegistry meterRegistry;

	public PrometheusSpringBoot(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}

	@GetMapping("/2xx")
    public String simulate2xxResponse() {
		String random = (fruits[new Random().nextInt(fruits.length)]);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		String date = new Date(System.currentTimeMillis()).toString();
		// this will increment the api.calls metric with the following tags
		// if there isn't an api.calls metric it will be created
		meterRegistry.counter(
			"api.calls",
			"uri", "/2xx",
			"user", random,
			"method", "GET",
			"time", date
		).increment();
		return random;
	}

	@GetMapping("/5xx")
	public String simulate5xxResponse() {
		String random = (fruits[new Random().nextInt(fruits.length)]);
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		String date = new Date(System.currentTimeMillis()).toString();

		meterRegistry.counter(
			"api.calls",
			"uri", "/5xx",
			"user", random,
			"method", "GET",
			"time", date
		).increment();
		return "Got 5xx Response";
  }

	@PostMapping("/alert-hook")
	public void receiveAlertHook(@RequestBody Map request) throws Exception {
		System.out.println(request);

	}
	public static void main(String[] args) {
		SpringApplication.run(PrometheusSpringBoot.class, args);
	}

}
