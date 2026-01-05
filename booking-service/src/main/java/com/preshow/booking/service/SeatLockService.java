package com.preshow.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatLockService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration LOCK_TTL = Duration.ofMinutes(8);

    private String key(UUID showId, UUID seatId) {
        return "lock:show:" + showId + ":seat:" + seatId;
    }

    public boolean lockSeat(UUID showId, UUID seatId, UUID userId) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForValue()
                        .setIfAbsent(
                                key(showId, seatId),
                                userId.toString(),
                                LOCK_TTL
                        )
        );
    }

    public void unlockSeat(UUID showId, UUID seatId) {
        redisTemplate.delete(key(showId, seatId));
    }

    public void unlockSeats(UUID showId, List<UUID> seatIds) {
        seatIds.forEach(seatId -> unlockSeat(showId, seatId));
    }
}
