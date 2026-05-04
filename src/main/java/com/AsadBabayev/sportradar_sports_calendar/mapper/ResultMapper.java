package com.AsadBabayev.sportradar_sports_calendar.mapper;

import com.AsadBabayev.sportradar_sports_calendar.dto.Result.*;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.request.*;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import com.AsadBabayev.sportradar_sports_calendar.entity.result.*;
import org.springframework.stereotype.Component;

@Component
public class ResultMapper {

    public Result createEntityBySport(SportType sportType) {
        return switch (sportType) {
            case FOOTBALL -> new FootballResult();
            case TENNIS -> new TennisResult();
            case BASKETBALL -> new BasketballResult();
            case ICE_HOCKEY -> new IceHockeyResult();
        };
    }

    public ResultDTO toDTO(Result entity) {
        if (entity == null) return null;

        String sportName = null;
        Long competitionId = null;

        if (entity.getEvent() != null && entity.getEvent().getCompetition() != null) {
            competitionId = entity.getEvent().getCompetition().getId();
            if (entity.getEvent().getCompetition().getSport() != null) {
                sportName = entity.getEvent().getCompetition().getSport().getName().name();
            }
        }

        if (entity instanceof FootballResult f) {
            return FootballResultDTO.builder()
                    .id(f.getId())
                    .winner(f.getWinner())
                    .message(f.getMessage())
                    .sportName(sportName)
                    .competitionId(competitionId)
                    .homeGoals(f.getHomeGoals())
                    .awayGoals(f.getAwayGoals())
                    .goals(f.getGoals())
                    .yellowCards(f.getYellowCards())
                    .secondYellowCards(f.getSecondYellowCards())
                    .directRedCards(f.getDirectRedCards())
                    .build();
        } else if (entity instanceof TennisResult t) {
            return TennisResultDTO.builder()
                    .id(t.getId())
                    .winner(t.getWinner())
                    .message(t.getMessage())
                    .sportName(sportName)
                    .competitionId(competitionId)
                    .homeSets(t.getHomeSets())
                    .awaySets(t.getAwaySets())
                    .setScores(t.getSetScores())
                    .build();
        } else if (entity instanceof BasketballResult b) {
            return BasketballResultDTO.builder()
                    .id(b.getId())
                    .winner(b.getWinner())
                    .message(b.getMessage())
                    .sportName(sportName)
                    .competitionId(competitionId)
                    .homeTotalPoints(b.getHomeTotalPoints())
                    .awayTotalPoints(b.getAwayTotalPoints())
                    .quarterPoints(b.getQuarterPoints())
                    .build();
        } else if (entity instanceof IceHockeyResult h) {
            return IceHockeyResultDTO.builder()
                    .id(h.getId())
                    .winner(h.getWinner())
                    .message(h.getMessage())
                    .sportName(sportName)
                    .competitionId(competitionId)
                    .homeGoals(h.getHomeGoals())
                    .awayGoals(h.getAwayGoals())
                    .periodScores(h.getPeriodScores())
                    .build();
        }
        return null;
    }

    public void updateEntityFromDto(ResultRequestDTO dto, Result result) {
        if (dto == null || result == null) return;

        result.setWinner(dto.getWinner());
        result.setMessage(dto.getMessage());

        if (result instanceof FootballResult f && dto instanceof FootballResultRequestDTO fDto) {
            f.setHomeGoals(fDto.getHomeGoals());
            f.setAwayGoals(fDto.getAwayGoals());
            f.setGoals(fDto.getGoals());
            f.setYellowCards(fDto.getYellowCards());
            f.setSecondYellowCards(fDto.getSecondYellowCards());
            f.setDirectRedCards(fDto.getDirectRedCards());
        } else if (result instanceof TennisResult t && dto instanceof TennisResultRequestDTO tDto) {
            t.setHomeSets(tDto.getHomeSets());
            t.setAwaySets(tDto.getAwaySets());
            t.setSetScores(tDto.getSetScores());
        } else if (result instanceof BasketballResult b && dto instanceof BasketballResultRequestDTO bDto) {
            b.setHomeTotalPoints(bDto.getHomeTotalPoints());
            b.setAwayTotalPoints(bDto.getAwayTotalPoints());
            b.setQuarterPoints(bDto.getQuarterPoints());
        } else if (result instanceof IceHockeyResult h && dto instanceof IceHockeyResultRequestDTO hDto) {
            h.setHomeGoals(hDto.getHomeGoals());
            h.setAwayGoals(hDto.getAwayGoals());
            h.setPeriodScores(hDto.getPeriodScores());
        }
    }
}