package com.shg.service;

import com.shg.model.Transaction;
import com.shg.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    public Transaction createTransaction(Transaction transaction) {
        transaction.setCreatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }
    
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
    
    public List<Transaction> getTransactionsByShgGroupId(Long shgGroupId) {
        return transactionRepository.findByShgGroupId(shgGroupId);
    }
    
    public List<Transaction> getTransactionsByType(String type) {
        return transactionRepository.findByType(type);
    }
    
    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }
    
    public List<Transaction> getTransactionsByShgGroupAndDateRange(Long shgGroupId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByShgGroupIdAndTransactionDateBetween(shgGroupId, startDate, endDate);
    }
    
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}