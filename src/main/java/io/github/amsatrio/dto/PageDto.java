package io.github.amsatrio.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageDto<T> {
    private List<T> content = new ArrayList<>();
    private Long totalPages = 0L;
    private Long totalElements = 0L;
    private Boolean first = false;
    private Boolean last = false;
    private Boolean empty = false;

    public void init(List<T> content, Long page, Long size, Long totalElements) {
        this.content = content;
        if (this.content.isEmpty()) {
            this.empty = true;
        }
        this.totalElements = totalElements;
        this.totalPages = (totalElements / size);
        if (totalElements % size != 0) {
            this.totalPages++;
        }

        if (page == 0) {
            this.first = true;
        }
        if (page >= totalPages - 1) {
            this.last = true;
        }
    }
}
