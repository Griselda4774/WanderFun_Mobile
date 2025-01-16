package com.example.wanderfunmobile.infrastructure.mapper;

import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;

import org.modelmapper.ModelMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class ObjectMapperImpl implements ObjectMapper {
    private final ModelMapper modelMapper;

    @Inject
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
    public <S, D> void copyProperties(S source, D destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination objects must not be null");
        }
        Object destinationIdValue = null;

        try {
            Field idField = destination.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            destinationIdValue = idField.get(destination);
        } catch (NoSuchFieldException | IllegalAccessException e) {}

        modelMapper.map(source, destination);

        if (destinationIdValue != null) {
            try {
                Field idField = destination.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(destination, destinationIdValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {}
        }

        for (Field field : source.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if ("id".equals(field.getName())) {
                    continue;
                }
                if (field.get(source) instanceof List) {
                    Field destField = destination.getClass().getDeclaredField(field.getName());
                    destField.setAccessible(true);

                    List<?> sourceList = (List<?>) field.get(source);
                    List<?> destList = (List<?>) destField.get(destination);

                    mapList(
                            (List)sourceList,
                            (List)destList,
                            Object.class,
                            modelMapper
                    );
                }
            } catch (Exception e) {}
        }
    }

    private  <S, D> void mapList(List<S> sourceList, List<D> destinationList, Class<D> destinationClass, ModelMapper modelMapper) {
        if (destinationList == null) {
            destinationList = new ArrayList<>();
        }

        if (sourceList == null || sourceList.isEmpty()) {
            destinationList.clear();
            return;
        }

        for (int i = 0; i < sourceList.size(); i++) {
            S sourceElement = sourceList.get(i);

            if (i < destinationList.size()) {
                D destinationElement = destinationList.get(i);
                if (sourceElement != null) {
                    modelMapper.map(sourceElement, destinationElement);
                }
            } else {
                if (sourceElement != null) {
                    D newDestinationElement = modelMapper.map(sourceElement, destinationClass);
                    destinationList.add(newDestinationElement);
                }
            }
        }

        while (destinationList.size() > sourceList.size()) {
            destinationList.remove(destinationList.size() - 1);
        }
    }
}
