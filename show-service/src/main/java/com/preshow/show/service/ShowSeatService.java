package com.preshow.show.service;

import com.preshow.show.model.ShowSeat;
import com.preshow.show.repository.ShowSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowSeatService {
    private final ShowSeatRepository repo;

    public ShowSeat create(ShowSeat seat){ return repo.save(seat); }
    public List<ShowSeat> getAll(){ return repo.findAll(); }
    public List<ShowSeat> getByShowId(UUID showId){ return repo.findByShowId(showId); }
    public ShowSeat getById(UUID id){ return repo.findById(id).orElseThrow(); }

    public ShowSeat update(UUID id, ShowSeat data){
        ShowSeat s = getById(id);
        s.setShowId(data.getShowId());
        s.setSeatId(data.getSeatId());
        s.setStatus(data.getStatus());
        return repo.save(s);
    }

    public void delete(UUID id){ repo.deleteById(id); }
}
