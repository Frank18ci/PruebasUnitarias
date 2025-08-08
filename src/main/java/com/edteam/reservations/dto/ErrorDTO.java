package com.edteam.reservations.dto;

import java.util.List;
import java.util.Objects;

public class ErrorDTO {
    private String description;
    private List<String> reasons;

    public ErrorDTO(String description, List<String> reasons) {
        this.description = description;
        this.reasons = reasons;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getReasons() {
        return reasons;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        ErrorDTO errorDTO = (ErrorDTO) o;
        return Objects.equals(description, errorDTO.description) && Objects.equals(reasons, errorDTO.reasons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, reasons);
    }
}
