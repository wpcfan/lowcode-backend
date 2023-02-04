package com.mooc.backend.enumerations;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BlockType {
    PinnedHeader("pinned_header"),
    Slider("slider"),
    ImageRow("image_row"),
    ProductRow("product_row"),
    Waterfall("waterfall");

    private String value;

    BlockType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static BlockType fromValue(String value) {
        for (BlockType blockType : BlockType.values()) {
            if (blockType.value.equals(value)) {
                return blockType;
            }
        }
        throw new IllegalArgumentException("Invalid BlockType value: " + value);
    }
}
