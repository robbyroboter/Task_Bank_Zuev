package org.example.task_bank.dto;

import java.time.LocalDate;

public class DepositDTO {
    private Long id;
    private Long clientId;
    private Long bankId;
    private LocalDate openDate;
    private Double interestRate;
    private Integer termInMonths;

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getBankId() {
        return bankId;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public Integer getTermInMonths() {
        return termInMonths;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public void setTermInMonths(Integer termInMonths) {
        this.termInMonths = termInMonths;
    }

    // toString
    @Override
    public String toString() {
        return "DepositDTO{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", bankId=" + bankId +
                ", openDate=" + openDate +
                ", interestRate=" + interestRate +
                ", termInMonths=" + termInMonths +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepositDTO that = (DepositDTO) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
