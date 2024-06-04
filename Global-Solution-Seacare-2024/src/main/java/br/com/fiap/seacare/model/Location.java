package br.com.fiap.seacare.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;

import br.com.fiap.seacare.controller.LocationController;
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
public class Location {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "{location.uf.notblank}")
    @Size(min = 2, max = 2, message = "{location.uf.size}")
    private String uf;

    @NotBlank(message = "{location.city.notblank}")
    @Size(min = 3, message = "{location.city.size}")
    private String city;

    @NotBlank(message = "{location.referencePoint.notblank}")
    @Size(max = 400, message = "{location.referencePoint.size}")
    private String referencePoint;

    public Location(Long id) {
        this.id = id;
    }

    public EntityModel<Location> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(LocationController.class).show(id)).withSelfRel(),
            linkTo(methodOn(LocationController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(LocationController.class).index(null, null)).withRel("contents")

        );
    }
}
