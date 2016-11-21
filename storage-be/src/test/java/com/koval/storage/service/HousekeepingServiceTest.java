package com.koval.storage.service;

import com.koval.storage.Application;
import com.koval.storage.repository.FileRepository;
import com.koval.storage.test.SchedulerTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Volodymyr Kovalenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class, SchedulerTestConfig.class})
@ActiveProfiles("test")
public class HousekeepingServiceTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    @Autowired
    private HousekeepingService housekeepingService;

    @Autowired
    @Test
    public void test(){
        initMocks(this);
        ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);

        verify(SchedulerTestConfig.scheduledExecutorService, atLeastOnce()).schedule(argumentCaptor.capture(), any(Long.class), eq(TimeUnit.MILLISECONDS));

        // Update logic in case > 1 scheduled methods
        argumentCaptor.getValue().run();

        verify(fileRepository, atLeastOnce()).deleteBefore(any(Instant.class));
    }

}
