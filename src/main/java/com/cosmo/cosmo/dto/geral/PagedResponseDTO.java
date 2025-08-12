package com.cosmo.cosmo.dto.geral;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PagedResponseDTO<T> extends RepresentationModel<PagedResponseDTO<T>> {

    @JsonProperty("_embedded")
    private Map<String, List<T>> embedded;

    @JsonProperty("page")
    private PageInfo page;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private int size;
        private long totalElements;
        private int totalPages;
        private int number;
    }
}
