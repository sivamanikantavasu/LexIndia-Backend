package com.lexzip.backend.advisory;

import com.lexzip.backend.auth.Profile;
import com.lexzip.backend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "advisory_requests")
public class AdvisoryRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_id")
    private Profile citizen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id")
    private Profile expert;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column
    private String subject;

    @Column
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdvisoryStatus status = AdvisoryStatus.PENDING;

    @Column(nullable = false)
    private boolean urgent;

    @Column(columnDefinition = "TEXT")
    private String response;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Profile getCitizen() {
        return citizen;
    }

    public void setCitizen(Profile citizen) {
        this.citizen = citizen;
    }

    public Profile getExpert() {
        return expert;
    }

    public void setExpert(Profile expert) {
        this.expert = expert;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public AdvisoryStatus getStatus() {
        return status;
    }

    public void setStatus(AdvisoryStatus status) {
        this.status = status;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
