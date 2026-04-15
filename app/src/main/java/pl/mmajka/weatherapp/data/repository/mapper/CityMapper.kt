package pl.mmajka.weatherapp.data.repository.mapper

import pl.mmajka.weatherapp.data.local.model.CityEntity
import pl.mmajka.weatherapp.data.remote.model.CityDto
import pl.mmajka.weatherapp.domain.model.City

fun CityDto.toDomain(): City = City(
    name = name,
    country = country,
    lat = lat,
    lon = lon,
    state = state
)

fun CityEntity.toDomain(): City = City(
    name = name,
    country = country,
    state = state,
    lat = lat,
    lon = lon
)

fun City.toEntity(): CityEntity = CityEntity(
    name = name,
    country = country,
    state = state,
    lat = lat,
    lon = lon,
    savedAt = System.currentTimeMillis()
)
