package br.com.fiap.seacare.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @GetMapping
    public Page<Location> index(
        @RequestParam(required = false) String uf,
        @PageableDefault(size = 3, sort = "uf", direction = Direction.ASC) Pageable pageable
    ) {
        return repository.findAll(pageable);
    }

    @PostMapping
    public Location create(@RequestBody @Valid Location location) {
        log.info("cadastrando localização {}", location);
        return repository.save(location);

    }

    @GetMapping("{id}")
    public ResponseEntity<Location> show (@PathVariable Long id) {
        log.info("buscando localização por id {}", id);
        return repository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando localização com id {}");
        verifyExistingLocation(id);

        repository.deleteById(id);
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