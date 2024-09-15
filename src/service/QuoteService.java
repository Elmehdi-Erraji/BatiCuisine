package service;

import domain.entities.Quote;
import repository.implimentation.QuoteRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class QuoteService {
    private final QuoteRepositoryImpl quoteRepository;

    public QuoteService(QuoteRepositoryImpl quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public Quote createQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    public Optional<Quote> getQuoteById(Integer id) {
        return quoteRepository.findById(id);
    }

    public List<Quote> getAllQuote() {
        return quoteRepository.findAll();
    }

    public Quote updateQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    public void deleteQuote(Integer id) {
        quoteRepository.deleteById(id);
    }

    public List<Quote> getQuoteByProjectId(Integer projectId) {
        return quoteRepository.findByProjectId(projectId);
    }
}