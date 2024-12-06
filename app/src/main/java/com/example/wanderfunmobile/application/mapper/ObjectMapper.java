package com.example.wanderfunmobile.application.mapper;

import java.util.List;

public interface ObjectMapper {
    public <S, D> D map(S source, Class<D> destinationType);
    public <S, D> List<D> mapList(List<S> sourceList, Class<D> destinationType);
    public <S, D> void copy(S source, D destination);
}
