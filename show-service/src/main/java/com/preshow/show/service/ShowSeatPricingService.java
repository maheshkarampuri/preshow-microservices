package com.preshow.show.service;

import com.preshow.show.model.ShowSeatPricing;
import com.preshow.show.repository.ShowSeatPricingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowSeatPricingService {
    private final ShowSeatPricingRepository repo;

    public ShowSeatPricing create(ShowSeatPricing p){ return repo.save(p); }
    public List<ShowSeatPricing> getAll(){ return repo.findAll(); }
    public List<ShowSeatPricing> getByShowId(UUID showId){ return repo.findByShowId(showId); }
    public ShowSeatPricing getById(UUID id){ return repo.findById(id).orElseThrow(); }

    public ShowSeatPricing update(UUID id, ShowSeatPricing data){
        ShowSeatPricing p = getById(id);
        p.setCategory(data.getCategory());
        p.setPrice(data.getPrice());
        return repo.save(p);
    }

    public void delete(UUID id){ repo.deleteById(id); }
}
