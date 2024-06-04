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
import jakarta.validation.Valid;

@RestController
@RequestMapping("location")
public class LocationController {
    
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    LocationRepository repository;

    @Autowired
    PagedResourcesAssembler<Location> pageAssembler;

    @GetMapping
    public PagedModel<EntityModel<Location>> index(
        @RequestParam(required = false) String uf,
        @PageableDefault(size = 3, sort = "uf", direction = Direction.ASC) Pageable pageable
    ) {
        Page<Location> page = null;
        page = repository.findAll(pageable);
        return pageAssembler.toModel(page);
    }

    @PostMapping
    public ResponseEntity<Location> create(@RequestBody @Valid Location location) {
        repository.save(location);

        return ResponseEntity
        .created(location.toEntityModel().getRequiredLink("self").toUri())
        .body(location);

    }

    @GetMapping("{id}")
    public EntityModel<Location> show (@PathVariable Long id) {
        var location = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("localização não encontrado")
        );

        return location.toEntityModel();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("localização não encontrado")
        );

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
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