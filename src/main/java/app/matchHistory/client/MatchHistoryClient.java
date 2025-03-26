package app.matchHistory.client;

import app.matchHistory.model.MatchHistory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "match-history-client",
        url = "http://localhost:8081/api/v1/history"
)
public interface MatchHistoryClient {

    // GET последните 6 мача за потребител
    @GetMapping("/{userId}/recent")
    List<MatchHistory> getRecentMatches(
            @PathVariable("userId") UUID userId
    );

    // POST нов мач
    @PostMapping
    MatchHistory saveMatchHistory(
            @RequestBody MatchHistory matchHistory
    );

    // GET цялата история (ако е нужно)
    @GetMapping("/{userId}")
    List<MatchHistory> getFullHistory(
            @PathVariable("userId") UUID userId
    );
}