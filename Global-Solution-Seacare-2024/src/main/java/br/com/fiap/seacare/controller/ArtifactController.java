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

import br.com.fiap.seacare.model.Artifact;
import br.com.fiap.seacare.repository.ArtifactRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("artifact")
public class ArtifactController {
    
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ArtifactRepository repository;

    @GetMapping
    public Page<Artifact> index(
        @RequestParam(required = false) String name,
        @PageableDefault(size = 3, sort = "name", direction = Direction.ASC) Pageable pageable
    ) {
        return repository.findAll(pageable);
    }

    @PostMapping
    public Artifact create(@RequestBody @Valid Artifact artifact) {
        log.info("cadastrando artefato {}", artifact);
        return repository.save(artifact);

    }

    @GetMapping("{id}")
    public ResponseEntity<Artifact> show (@PathVariable Long id) {
        log.info("buscando artefato por id {}", id);
        return repository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando artefato com id {}");
        verifyExistingArtifact(id);

        repository.deleteById(id);
    }

    @PutMapping("{id}")
    public Artifact update(@PathVariable Long id, @RequestBody Artifact artifact) {
        log.info("atualizando artefato com id {}", id, artifact);

        verifyExistingArtifact(id);

        artifact.setId(id);
        return repository.save(artifact);
    }

    private void verifyExistingArtifact(Long id) {
        repository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe artefato com o id informado. Consulte lista em /artifact"));
    }

}
