package br.com.fiap.seacare.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;

import br.com.fiap.seacare.controller.UserSCController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class UserSC {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "{userSC.name.notblank}")
    @Size(min = 3, message = "{userSC.name.size}")
    private String name;

    @Pattern(regexp = "\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}", message = "{userSC.phone.invalid}")
    private String phone;

    public UserSC(Long id) {
        this.id = id;
    }

    public EntityModel<UserSC> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(UserSCController.class).show(id)).withSelfRel(),
            linkTo(methodOn(UserSCController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(UserSCController.class).index(null, null)).withRel("contents")

        );
    }
}
