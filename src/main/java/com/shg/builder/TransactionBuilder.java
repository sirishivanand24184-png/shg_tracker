package com.shg.builder;

import com.shg.model.SHGGroup;
import com.shg.model.SHGMember;
import com.shg.model.Transaction;

import java.time.LocalDateTime;

public class TransactionBuilder {

    private final Transaction transaction;

    public TransactionBuilder() {
        this.transaction = new Transaction();
    }

    public TransactionBuilder withType(String type) {
        transaction.setType(type);
        return this;
    }

    public TransactionBuilder withAmount(Double amount) {
        transaction.setAmount(amount);
        return this;
    }

    public TransactionBuilder withDescription(String description) {
        transaction.setDescription(description);
        return this;
    }

    public TransactionBuilder withRecordedBy(String recordedBy) {
        transaction.setRecordedBy(recordedBy);
        return this;
    }

    public TransactionBuilder withTransactionDate(LocalDateTime transactionDate) {
        transaction.setTransactionDate(transactionDate);
        return this;
    }

    public TransactionBuilder withShgGroup(SHGGroup shgGroup) {
        transaction.setShgGroup(shgGroup);
        return this;
    }

    public TransactionBuilder withMember(SHGMember member) {
        transaction.setMember(member);
        return this;
    }

    public Transaction build() {
        return transaction;
    }
}
