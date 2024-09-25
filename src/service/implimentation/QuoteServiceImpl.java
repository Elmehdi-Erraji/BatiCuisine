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
        return quoteRepository.save(quote);
    }

    @Override
    public Optional<Quote> getQuoteById(Integer id) {
        return quoteRepository.findById(id);
    }

    @Override
    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    @Override
    public Quote updateQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    @Override
    public void delete(Quote quote) {
        quoteRepository.delete(quote);
    }

    @Override
    public List<Quote> getQuoteWithProject(Client client) {
        return quoteRepository.findQuoteWithProjectById(client);
    }

    @Override
    public Quote acceptQuote(Quote quote) throws Exception {
        Quote newQuote = new Quote();
        boolean isPast = quote.getValidityDate().isAfter(LocalDate.now());

        if (isPast) {
            newQuote.setAccepted(true);
            newQuote.setId(quote.getId());
            return quoteRepository.update(newQuote);
        } else {
            throw new Exception("Invalid Date");
        }
    }
}
