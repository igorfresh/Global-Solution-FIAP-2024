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
                .build(),
                Artifact.builder()
                    .id(2L)
                    .name("metal")
                    .description("latas de refrigerante amassada")
                    .build(),
                Artifact.builder()
                    .id(3L)
                    .name("papel")
                    .description("jornais velho no mar")
                    .build(),
                Artifact.builder()
                    .id(4L)
                    .name("vidro")
                    .description("garrafas de cerveja quebrada")
                    .build(),
                Artifact.builder()
                    .id(5L)
                    .name("pneu")
                    .description("pneus velhos")
                    .build(),
                Artifact.builder()
                    .id(6L)
                    .name("plástico")
                    .description("tampas de garrafa espalhadas")
                    .build(),
                    Artifact.builder()
                        .id(7L)
                        .name("tecido")
                        .description("pedaços de roupa velha")
                        .build()
            )
        );

        locationRepository.saveAll(
            List.of(
                Location.builder()
                .id(1L).uf("SP")
                .city("Santos")
                .referencePoint("Praia do Gonzaga, próximo ao quiosque principal")
                .build(),
                Location.builder()
                    .id(2L)
                    .uf("RJ")
                    .city("Rio de Janeiro")
                    .referencePoint("Copacabana, perto do posto 6")
                    .build(),
                Location.builder()
                    .id(3L)
                    .uf("BA")
                    .city("Salvador")
                    .referencePoint("Praia do Porto da Barra, ao lado do farol")
                    .build(),
                Location.builder()
                    .id(4L)
                    .uf("SC")
                    .city("Florianópolis")
                    .referencePoint("Praia da Joaquina, próximo às dunas")
                    .build(),
                Location.builder()
                    .id(5L)
                    .uf("PE")
                    .city("Recife")
                    .referencePoint("Boa Viagem, em frente ao Parque Dona Lindu")
                    .build()
            )
        );

        userSCRepository.saveAll(
            List.of(
                UserSC.builder()
                .id(1L)
                .name("Igor Silva")
                .phone("(11) 94815-1343")
                .build(),
                UserSC.builder()
                    .id(2L)
                    .name("Ana Souza")
                    .phone("(21) 98765-4321")
                    .build(),
                UserSC.builder()
                    .id(3L)
                    .name("Carlos Oliveira")
                    .phone("(31) 92345-6789")
                    .build(),
                UserSC.builder()
                    .id(4L)
                    .name("Mariana Lima")
                    .phone("(41) 91234-5678")
                    .build(),
                UserSC.builder()
                    .id(5L)
                    .name("Rafael Costa")
                    .phone("(51) 99876-5432")
                    .build(),
                UserSC.builder()
                    .id(6L)
                    .name("Fernanda Alves")
                    .phone("(61) 98765-1234")
                    .build(),
                UserSC.builder()
                    .id(7L)
                    .name("Lucas Pereira")
                    .phone("(71) 97654-3210")
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
                .build(),
                Report.builder()
                    .id(2L)
                    .description("Copacabana com muitos resíduos de alimentos e plásticos")
                    .date(LocalDate.of(2019, 3, 15))
                    .userSc(userSCRepository.findById(2L).get())
                    .location(locationRepository.findById(2L).get())
                    .build(),
                Report.builder()
                    .id(3L)
                    .description("Praia do Porto da Barra com latas e garrafas espalhadas")
                    .date(LocalDate.of(2020, 5, 10))
                    .userSc(userSCRepository.findById(3L).get())
                    .location(locationRepository.findById(3L).get())
                    .build(),
                Report.builder()
                    .id(4L)
                    .description("Praia da Joaquina cheia de pneus e pedaços de vidro")
                    .date(LocalDate.of(2021, 1, 5))
                    .userSc(userSCRepository.findById(4L).get())
                    .location(locationRepository.findById(4L).get())
                    .build(),
                Report.builder()
                    .id(5L)
                    .description("Boa Viagem repleta de sacolas plásticas e restos de comida")
                    .date(LocalDate.of(2019, 8, 21))
                    .userSc(userSCRepository.findById(5L).get())
                    .location(locationRepository.findById(5L).get())
                    .build(),
                Report.builder()
                    .id(6L)
                    .description("Praia do Gonzaga com grande quantidade de bitucas de cigarro")
                    .date(LocalDate.of(2022, 11, 11))
                    .userSc(userSCRepository.findById(6L).get())
                    .location(locationRepository.findById(1L).get())
                    .build(),
                Report.builder()
                    .id(7L)
                    .description("Copacabana com entulhos de construção e plásticos")
                    .date(LocalDate.of(2023, 4, 30))
                    .userSc(userSCRepository.findById(7L).get())
                    .location(locationRepository.findById(2L).get())
                    .build()
            )
        );

        reportedArtifactsRepository.saveAll(
            List.of(
                ReportedArtifacts.builder()
                .id(1L)
                .artifact(artifactRepository.findById(1L).get())
                .report(reportRepository.findById(1L).get())
                .build(),
                ReportedArtifacts.builder()
                    .id(2L)
                    .artifact(artifactRepository.findById(2L).get()) 
                    .report(reportRepository.findById(2L).get())  
                    .build(),
                ReportedArtifacts.builder()
                    .id(3L)
                    .artifact(artifactRepository.findById(3L).get())  
                    .report(reportRepository.findById(3L).get())  
                    .build(),
                ReportedArtifacts.builder()
                    .id(4L)
                    .artifact(artifactRepository.findById(4L).get())  
                    .report(reportRepository.findById(4L).get()) 
                    .build(),
                ReportedArtifacts.builder()
                    .id(5L)
                    .artifact(artifactRepository.findById(5L).get())  
                    .report(reportRepository.findById(5L).get()) 
                    .build(),
                ReportedArtifacts.builder()
                    .id(6L)
                    .artifact(artifactRepository.findById(1L).get())  
                    .report(reportRepository.findById(6L).get()) 
                    .build(),
                ReportedArtifacts.builder()
                    .id(7L)
                    .artifact(artifactRepository.findById(6L).get())  
                    .report(reportRepository.findById(7L).get())  
                    .build()
            )
        );
    }

}
