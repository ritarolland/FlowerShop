package com.example.prac1.domain.mapper

import com.example.prac1.data.model.CartItemDataModel
import com.example.prac1.domain.model.CartItem

object CartItemMapper {
    fun mapToDomain(dataModel: CartItemDataModel): CartItem {
        return CartItem(
            id = dataModel.id,
            flowerId = dataModel.flower_id,
            quantity = dataModel.quantity
        )
    }
}