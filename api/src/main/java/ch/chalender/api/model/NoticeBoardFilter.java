package ch.chalender.api.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NoticeBoardFilter {
    private List<Integer> genres = new ArrayList<>();

    private String searchTerm;
}
