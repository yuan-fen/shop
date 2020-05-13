package com.atguigu.gmall.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledDemo {

    @Scheduled(cron="2 * * * * *")
    public void scheduledTash(){
        System.out.println("excuted..." + new Date());
    }

}
