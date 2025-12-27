package com.preshow.theater.service;

import com.preshow.theater.model.Theater;
import com.preshow.theater.repository.TheaterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TheaterService {

    private final TheaterRepository repository;


    public List<Theater> getAll() {
        return repository.findAll();
    }

    public Optional<Theater> getById(UUID id) {
        return repository.findById(id);
    }

    public Theater save(Theater theater) {
        return repository.save(theater);
    }

    public Theater update(UUID id, Theater updated) {
        return repository.findById(id).map(t -> {
            t.setName(updated.getName());
            t.setLocation(updated.getLocation());
            t.setTotalScreens(updated.getTotalScreens());
            return repository.save(t);
        }).orElseThrow(() -> new RuntimeException("Theater not found"));
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}