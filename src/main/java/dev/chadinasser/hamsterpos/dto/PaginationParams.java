package dev.chadinasser.hamsterpos.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class PaginationParams {
    @PositiveOrZero(message = "Page must be zero or a positive integer")
    private int page = 0;
    @Positive(message = "Size must be a positive integer")
    private int size = 5;
    private String sortBy = "id";
    private boolean ascending = true;

    public Pageable toPageable() {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }
}
