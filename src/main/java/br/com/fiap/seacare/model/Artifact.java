package br.com.fiap.seacare.model;

import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.fiap.seacare.controller.ArtifactController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
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
public class Artifact {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "{artifact.name.notblank}")
    @Size(min = 3, message = "{artifact.name.size}")
    private String name;

    @NotBlank(message = "{artifact.description.notblank}")
    @Size(max = 400, message = "{artifact.description.size}")
    private String description;

    public Artifact(Long id) {
        this.id = id;
    }

    public EntityModel<Artifact> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(ArtifactController.class).show(id)).withSelfRel(),
            linkTo(methodOn(ArtifactController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(ArtifactController.class).index(null, null)).withRel("contents")

        );
    }
}
