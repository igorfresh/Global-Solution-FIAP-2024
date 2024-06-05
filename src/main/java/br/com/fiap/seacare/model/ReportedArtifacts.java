package br.com.fiap.seacare.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;

import br.com.fiap.seacare.controller.ReportedArtifactController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportedArtifacts {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Report report;

    @ManyToOne
    private Artifact artifact;

    public ReportedArtifacts(Long id) {
        this.id = id;
    }

    public EntityModel<ReportedArtifacts> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(ReportedArtifactController.class).show(id)).withSelfRel(),
            linkTo(methodOn(ReportedArtifactController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(ReportedArtifactController.class).index(null, null)).withRel("contents")

        );
    }
}
