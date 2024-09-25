package repository.Interfaces;

import domain.entities.Client;
import domain.entities.Quote;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository {
     Quote save(Quote quote);
     Optional<Quote> findById(Integer id);
     List<Quote> findAll();
     void delete(Quote quote);
     List<Quote> findQuoteWithProjectById(Client client);
     Quote update(Quote quote);
}
