package com.example.wanderfunmobile.core.di;

import com.example.wanderfunmobile.core.util.LocalDateDeserializer;
import com.example.wanderfunmobile.core.util.LocalDateSerializer;
import com.example.wanderfunmobile.core.util.LocalDateTimeDeserializer;
import com.example.wanderfunmobile.core.util.LocalTimeDeserializer;
import com.example.wanderfunmobile.core.util.typeadapter.LocalDateAdapter;
import com.example.wanderfunmobile.core.util.typeadapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class GsonModule {
    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setDateFormat("yyyy-MM-dd")
                .create();
    }
}