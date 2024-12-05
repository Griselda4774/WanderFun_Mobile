package com.example.wanderfunmobile.infrastructure.mapper;

import com.example.wanderfunmobile.data.mapper.ObjectMapper;

import org.modelmapper.ModelMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ObjectMapperModule {
    @Provides
    @Singleton
    public ModelMapper provideModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);;
        return modelMapper;
    }

    @Provides
    @Singleton
    public ObjectMapper provideObjectMapper(ModelMapper modelMapper) {
        return new ObjectMapperImpl(modelMapper);
    }
}