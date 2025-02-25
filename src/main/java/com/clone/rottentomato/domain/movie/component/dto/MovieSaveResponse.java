package com.clone.rottentomato.domain.movie.component.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieSaveResponse {
    List<MovieInfoDto> saveSuccessList = new ArrayList<>();
    List<MovieInfoDto> saveFailList = new ArrayList<>();

    public static MovieSaveResponse of(List<MovieInfoDto> saveSuccessList,  List<MovieInfoDto> saveFailList){
        return new MovieSaveResponse(saveSuccessList, saveFailList);
    }

    public void addResponse(MovieSaveResponse response){
        this.addSaveSuccessList(response.getSaveSuccessList());
        this.addSaveFailList(response.getSaveFailList());
    }

    public void addSaveSuccessList(List<MovieInfoDto> saveSuccessList){
        this.addSaveList(saveSuccessList, true);
    }

    public void addSaveFailList(List<MovieInfoDto> saveFailList){
        this.addSaveList(saveFailList, false);
    }

    private void addSaveList(List<MovieInfoDto> saveList, boolean isSuccess){
        if(CollectionUtils.isEmpty(saveList)) return;
        if (isSuccess) {
            saveSuccessList.addAll(saveList);
            return;
        }
        saveFailList.addAll(saveList);
    }
}
