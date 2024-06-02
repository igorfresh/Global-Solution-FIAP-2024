package br.com.fiap.seacare.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.time.LocalDate;

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

import br.com.fiap.seacare.model.Report;
import br.com.fiap.seacare.repository.ReportRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("report")
public class ReportController {
    
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ReportRepository repository;

    @GetMapping
    public Page<Report> index(
        @RequestParam(required = false) LocalDate date,
        @PageableDefault(size = 3, sort = "date", direction = Direction.ASC) Pageable pageable
    ) {
        return repository.findAll(pageable);
    }

    @PostMapping
    public Report create(@RequestBody @Valid Report report) {
        log.info("cadastrando denúncia {}", report);
        return repository.save(report);

    }

    @GetMapping("{id}")
    public ResponseEntity<Report> show (@PathVariable Long id) {
        log.info("buscando denúncia por id {}", id);
        return repository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando denúncia com id {}");
        verifyExistingReport(id);

        repository.deleteById(id);
    }

    @PutMapping("{id}")
    public Report update(@PathVariable Long id, @RequestBody Report report) {
        log.info("atualizando denúncia com id {}", id, report);

        verifyExistingReport(id);

        report.setId(id);
        return repository.save(report);
    }

    private void verifyExistingReport(Long id) {
        repository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe denúncia com o id informado. Consulte lista em /report"));
    }

}