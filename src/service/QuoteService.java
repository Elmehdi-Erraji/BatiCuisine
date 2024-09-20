package service;

import domain.entities.Client;
import domain.entities.Quote;
import repository.Interfaces.QuoteRepository;
import repository.implimentation.QuoteRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteService() {
        this.quoteRepository = new QuoteRepositoryImpl();
    }

    public Quote createQuote(Quote quote) {
        return quoteRepository.save(quote); // Call on the instance
    }

    public Optional<Quote> getQuoteById(Integer id) {
        return quoteRepository.findById(id); // Call on the instance
    }

    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll(); // Call on the instance
    }

    public Quote updateQuote(Quote quote) {
        return quoteRepository.save(quote); // Call on the instance
    }

    public void deleteQuote(Integer id) {
        quoteRepository.deleteById(id); // Call on the instance
    }

    public List<Quote> getQuoteWithProject(Client client) {
        return quoteRepository.findQuoteWithProjectById(client); // Call on the instance
    }

    public Quote acceptQuote(Quote quote) throws Exception {
        Quote newQuote = new Quote();
        boolean isPast = quote.getValidityDate().isAfter(LocalDate.now());

        if (isPast) {
            newQuote.setAccepted(true);
            newQuote.setId(quote.getId());
            return quoteRepository.update(newQuote); // Call on the instance
        } else {
            throw new Exception("Invalid Date");
        }
    }
}
