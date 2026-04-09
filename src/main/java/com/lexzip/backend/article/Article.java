package com.lexzip.backend.article;

import com.lexzip.backend.common.BaseEntity;
import com.lexzip.backend.common.StringListJsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "articles")
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private String title;

    @Column(name = "full_text", columnDefinition = "TEXT")
    private String fullText;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "key_points", columnDefinition = "TEXT")
    private List<String> keyPoints;

    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "important_cases", columnDefinition = "TEXT")
    private List<String> importantCases;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public List<String> getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(List<String> keyPoints) {
        this.keyPoints = keyPoints;
    }

    public List<String> getImportantCases() {
        return importantCases;
    }

    public void setImportantCases(List<String> importantCases) {
        this.importantCases = importantCases;
    }
}
