package service.interfaces;

import domain.entities.Client;
import domain.entities.Quote;

import java.util.List;
import java.util.Optional;

public interface QuoteService {
    Quote createQuote(Quote quote);

    Optional<Quote> getQuoteById(Integer id);

    List<Quote> getAllQuotes();

    Quote updateQuote(Quote quote);

    void deleteQuote(Integer id);

    List<Quote> getQuoteWithProject(Client client);

    Quote acceptQuote(Quote quote) throws Exception;
}
