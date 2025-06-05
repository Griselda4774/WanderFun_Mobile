package com.example.wanderfunmobile.core.di;

import com.example.wanderfunmobile.data.dto.posts.PostCreateDto;
import com.example.wanderfunmobile.data.dto.tripplace.TripPlaceCreateDto;
import com.example.wanderfunmobile.data.mapper.ObjectMapper;
import com.example.wanderfunmobile.data.mapper.impl.ObjectMapperImpl;
import com.example.wanderfunmobile.domain.model.places.Place;
import com.example.wanderfunmobile.domain.model.posts.Post;
import com.example.wanderfunmobile.domain.model.trips.Trip;
import com.example.wanderfunmobile.domain.model.trips.TripPlace;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

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


        Converter<Trip, Long> tripToTripIdConverter = ctx -> {
            Trip trip = ctx.getSource();
            return (trip != null) ? trip.getId() : null;
        };

        modelMapper.typeMap(Post.class, PostCreateDto.class)
                .addMappings(mapper -> mapper.using(tripToTripIdConverter)
                        .map(Post::getTrip, PostCreateDto::setTripId));


        return modelMapper;
    }

    @Provides
    @Singleton
    public ObjectMapper provideObjectMapper(ModelMapper modelMapper) {
        return new ObjectMapperImpl(modelMapper);
    }
}
