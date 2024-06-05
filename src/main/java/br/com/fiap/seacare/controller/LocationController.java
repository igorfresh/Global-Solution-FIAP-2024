package br.com.fiap.seacare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.seacare.model.Location;
import br.com.fiap.seacare.repository.LocationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("location")
@Tag(name = "Localização", description = "Localização da denúncia")
public class LocationController {
    
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    LocationRepository repository;

    @Autowired
    PagedResourcesAssembler<Location> pageAssembler;

    @GetMapping
    @Operation(
        summary = "Listar Localizações",
        description = "Retorna uma lista paginada de localizações."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Localizações listadas"),
        @ApiResponse(responseCode = "404", description = "Localizações não encontradas")
    })
    public PagedModel<EntityModel<Location>> index(
        @RequestParam(required = false) String uf,
        @PageableDefault(size = 3, sort = "uf", direction = Direction.ASC) Pageable pageable
    ) {
        Page<Location> page = null;
        page = repository.findAll(pageable);
        return pageAssembler.toModel(page);
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar Localização",
        description = "Cadastra uma nova localização."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Localização criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida")
    })
    public ResponseEntity<Location> create(@RequestBody @Valid Location location) {
        repository.save(location);

        return ResponseEntity
        .created(location.toEntityModel().getRequiredLink("self").toUri())
        .body(location);

    }

    @GetMapping("{id}")
    @Operation(
        summary = "Buscar Localização",
        description = "Recupera uma localização pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Localização encontrada"),
        @ApiResponse(responseCode = "404", description = "Localização não encontrada")
    })
    public EntityModel<Location> show (@PathVariable Long id) {
        var location = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("localização não encontrado")
        );

        return location.toEntityModel();
    }

    @DeleteMapping("{id}")
    @Operation(
        summary = "Excluir Localização",
        description = "Remove uma localização pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Localização excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Localização não encontrada")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("localização não encontrado")
        );

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar Localização",
        description = "Atualiza uma localização existente pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Localização atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Localização não encontrada"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida")
    })
    public Location update(@PathVariable Long id, @RequestBody Location location) {
        log.info("atualizando localização com id {}", id, location);

        verifyExistingLocation(id);

        location.setId(id);
        return repository.save(location);
    }

    private void verifyExistingLocation(Long id) {
        repository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe localização com o id informado. Consulte lista em /location"));
    }

}