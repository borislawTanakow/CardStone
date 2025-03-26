package app.matchHistory.model;

import jakarta.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class MatchHistory {


        @Column(nullable = false)
        private String status;

        @Column(nullable = false)
        private String opponent;

        @Column(nullable = false)
        private double myPower;

        @Column(nullable = false)
        private double opponentPower;

        @Column(nullable = false)
        private int stoneCoins;


        private UUID userId;

    }

