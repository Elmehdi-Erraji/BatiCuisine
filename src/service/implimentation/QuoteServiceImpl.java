package service.implimentation;

import domain.entities.Client;
import domain.entities.Quote;
import repository.Interfaces.QuoteRepository;
import repository.implimentation.QuoteRepositoryImpl;
import service.interfaces.QuoteService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class QuoteServiceImpl implements QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteServiceImpl() {
        this.quoteRepository = new QuoteRepositoryImpl();
    }

    @Override
    public Quote createQuote(Quote quote) {
        return quoteRepository.save(quote); // Call on the instance
    }

    @Override
    public Optional<Quote> getQuoteById(Integer id) {
        return quoteRepository.findById(id); // Call on the instance
    }

    @Override
    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll(); // Call on the instance
    }

    @Override
    public Quote updateQuote(Quote quote) {
        return quoteRepository.save(quote); // Call on the instance
    }

    @Override
    public void deleteQuote(Integer id) {
        quoteRepository.deleteById(id); // Call on the instance
    }

    @Override
    public List<Quote> getQuoteWithProject(Client client) {
        return quoteRepository.findQuoteWithProjectById(client); // Call on the instance
    }

    @Override
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
