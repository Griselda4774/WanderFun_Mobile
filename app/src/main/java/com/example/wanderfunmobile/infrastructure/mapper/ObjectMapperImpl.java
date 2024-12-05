package com.example.wanderfunmobile.infrastructure.mapper;

import com.example.wanderfunmobile.data.mapper.ObjectMapper;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ObjectMapperImpl implements ObjectMapper {
    private final ModelMapper modelMapper;

    public ObjectMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public <S, D> D map(S source, Class<D> destinationClass) {
        return modelMapper.map(source, destinationClass);
    }

    @Override
    public <S, D> List<D> mapList(List<S> sourceList, Class<D> destinationClass) {
        return sourceList.stream()
                .map(source -> modelMapper.map(source, destinationClass))
                .collect(Collectors.toList());
    }

    @Override
    public <S, D> void copy(S source, D destination) {
        if (source != null || destination != null) {
            modelMapper.map(source, destination);;
        }
    }
}