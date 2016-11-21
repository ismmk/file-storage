package com.koval.storage.service;

import com.koval.storage.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Created by Volodymyr Kovalenko
 */
@Service
public class HousekeepingService {

    @Autowired
    private FileRepository fileRepository;

    @Value("${housekeeping.thresholdDays}")
    private int thresholdDays;

    @Scheduled(cron = "0 0 5 * * *")
    public void housekeep(){
        fileRepository.deleteBefore(Instant.now().minus(thresholdDays, ChronoUnit.DAYS));
    }
}
