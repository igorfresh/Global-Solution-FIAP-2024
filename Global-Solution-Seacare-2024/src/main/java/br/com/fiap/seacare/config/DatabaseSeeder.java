package br.com.fiap.seacare.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.seacare.model.Artifact;
import br.com.fiap.seacare.model.Location;
import br.com.fiap.seacare.model.Report;
import br.com.fiap.seacare.model.ReportedArtifacts;
import br.com.fiap.seacare.model.UserSC;
import br.com.fiap.seacare.repository.ArtifactRepository;
import br.com.fiap.seacare.repository.LocationRepository;
import br.com.fiap.seacare.repository.ReportRepository;
import br.com.fiap.seacare.repository.ReportedArtifactsRepository;
import br.com.fiap.seacare.repository.UserSCRepository;

@Configuration
public class DatabaseSeeder implements CommandLineRunner{
    
    @Autowired
    ArtifactRepository artifactRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ReportedArtifactsRepository reportedArtifactsRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    UserSCRepository userSCRepository;

    @Override
    public void run(String... args) throws Exception {

        artifactRepository.saveAll(
            List.of(
                Artifact.builder()
                .id(1L)
                .name("plástico")
                .description("pilha de garrafa pet")
                .build()
            )
        );

        locationRepository.saveAll(
            List.of(
                Location.builder()
                .id(1L).uf("SP")
                .city("Santos")
                .referencePoint("Praia do Gonzaga, próximo ao quiosque principal")
                .build()
            )
        );

        userSCRepository.saveAll(
            List.of(
                UserSC.builder()
                .id(1L)
                .name("Igor Silva")
                .phone("(11) 94815-1343")
                .build()
            )
        );

        reportRepository.saveAll(
            List.of(
                Report.builder()
                .id(1L)
                .description("Praia do gonzaga cheia de lixos e garrafas plásticas")
                .date(LocalDate.of(2018, 07, 22))
                .userSc(userSCRepository.findById(1L).get())
                .location(locationRepository.findById(1L).get())
                .build()
            )
        );

        reportedArtifactsRepository.saveAll(
            List.of(
                ReportedArtifacts.builder()
                .id(1L)
                .artifact(artifactRepository.findById(1L).get())
                .report(reportRepository.findById(1L).get())
                .build()
            )
        );
    }

}
