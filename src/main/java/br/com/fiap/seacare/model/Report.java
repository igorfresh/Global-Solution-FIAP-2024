package br.com.fiap.seacare.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;

import org.springframework.hateoas.EntityModel;

import br.com.fiap.seacare.controller.ReportController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotBlank(message = "{report.description.notblank}")
    @Size(max = 400, message = "{report.description.size}")
    private String description;


    @NotNull(message = "{report.date.notnull}")
    @PastOrPresent(message = "{report.date.pastorpresent}")
    private LocalDate date;

    @ManyToOne
    private UserSC userSc;

    @ManyToOne
    private Location location;
    
    public Report(Long id) {
        this.id = id;
    }

    public EntityModel<Report> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(ReportController.class).show(id)).withSelfRel(),
            linkTo(methodOn(ReportController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(ReportController.class).index(null, null)).withRel("contents")

        );
    }
}
