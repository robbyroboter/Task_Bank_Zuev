package org.example.task_bank.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Bank bank;

    @Column(nullable = false)
    private LocalDate openDate;

    @Column(nullable = false)
    private Double interestRate;

    @Column(nullable = false)
    private Integer termInMonths;

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Bank getBank() {
        return bank;
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

    public void setClient(Client client) {
        this.client = client;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
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

    @Override
    public String toString() {
        return "Deposit{" +
                "id=" + id +
                ", openDate=" + openDate +
                ", interestRate=" + interestRate +
                ", termInMonths=" + termInMonths +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deposit deposit = (Deposit) o;
        return id != null && id.equals(deposit.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
