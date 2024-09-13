package com.example.prac1.domain.mapper

import com.example.prac1.data.model.FlowerDataModel
import com.example.prac1.domain.model.Flower

class FlowerMapper {
    fun mapToDomain(dataModel: FlowerDataModel): Flower {
        return Flower(
            id = dataModel.flower_id,
            name = dataModel.flower_name,
            description = dataModel.description ?: "No description",
            price = dataModel.price,
        )
    }
}