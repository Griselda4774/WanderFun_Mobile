package com.example.wanderfunmobile.presentation.mapper;

import java.util.List;

public interface ObjectMapper {
    public <S, D> D map(S source, Class<D> destinationType);
    public <S, D> List<D> mapList(List<S> sourceList, Class<D> destinationType);
    public <S, D> void copyProperties(S source, D destination);
}
