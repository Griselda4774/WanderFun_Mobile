package com.example.wanderfunmobile.infrastructure.mapper.di;

import com.example.wanderfunmobile.application.dto.place.PlaceDto;
import com.example.wanderfunmobile.domain.model.Place;
import com.example.wanderfunmobile.presentation.mapper.ObjectMapper;
import com.example.wanderfunmobile.infrastructure.mapper.ObjectMapperImpl;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalTime;

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
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setPropertyCondition(context -> context.getSource() != null)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }

    @Provides
    @Singleton
    public ObjectMapper provideObjectMapper(ModelMapper modelMapper) {
        return new ObjectMapperImpl(modelMapper);
    }
}
