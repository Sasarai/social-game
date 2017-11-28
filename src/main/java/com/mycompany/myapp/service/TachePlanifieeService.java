package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Evenement;
import com.mycompany.myapp.domain.User;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@ComponentScan
@Configuration
@EnableScheduling
@EnableBatchProcessing
public class TachePlanifieeService {

    private final EvenementService evenementService;

    private final MailService mailService;

    public TachePlanifieeService(
        EvenementService evenementService,
        MailService mailService
    ){
        this.evenementService = evenementService;
        this.mailService = mailService;
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional(readOnly = true)
    public void test() {

        ZonedDateTime date = ZonedDateTime.now();

        date = date.minusNanos(date.getNano());
        date = date.minusSeconds(date.getSecond());

        List<Evenement> evenementsANotifier = evenementService.recupererEmailAdministrateurSpherePourEvenementFinDeVote(date);

        for(Evenement evenement : evenementsANotifier){
            mailService.sendEndVoteEmail(evenement.getSphere().getAdministrateur(), evenement.getSphere().getNom());
        }

        System.out.println(evenementsANotifier);

    }

}
