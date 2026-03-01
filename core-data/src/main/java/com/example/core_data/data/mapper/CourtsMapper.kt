package com.example.core_data.data.mapper

import com.example.core_domain.domain.model.Courts
import com.example.core_domain.domain.dto.CourtDto


class CourtsMapper {

    fun toDomain(dto: com.example.core_domain.domain.dto.CourtDto, arenaId: String): Courts {
        return Courts(
            id = dto.id,
            arenaId = arenaId,
            name = dto.name,
            description = dto.description,
            capacity = dto.capacity,
            basePrice = dto.basePrice,
            slotDurationMinutes = dto.slotDurationMinutes,
            type = dto.type,
            amenities = dto.amenities
        )
    }

    fun toDto(domain: Courts): com.example.core_domain.domain.dto.CourtDto {
        return com.example.core_domain.domain.dto.CourtDto(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            capacity = domain.capacity,
            basePrice = domain.basePrice,
            slotDurationMinutes = domain.slotDurationMinutes,
            type = domain.type,
            amenities = domain.amenities
        )
    }
}