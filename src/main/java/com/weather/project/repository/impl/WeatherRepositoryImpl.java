package com.weather.project.repository.impl;

import com.weather.project.model.WeatherDetails;
import com.weather.project.repository.WeatherRepository;
import com.weather.project.repository.exception.RepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class WeatherRepositoryImpl implements WeatherRepository {

    public static final String FUNCTION_VARIABLE = "date";
    public static final String FIELD_NAME = "localTime";

    private static final Logger LOGGER = LogManager.getRootLogger();


    private final SessionFactory sessionFactory;

    @Autowired
    public WeatherRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveWeatherData(WeatherDetails weatherDetails) throws RepositoryException {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();
            session.save(weatherDetails);
            transaction.commit();

        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.warn("Problems with adding info to data or another exception occurred", e);
            throw new RepositoryException("Failed to add weather data to database", e);
        }
    }

    public static final String FIND_WEATHER_DETAILS_QUERY = "FROM WeatherDetails wd where wd.localTime BETWEEN :dateFrom AND :dateTo";

    @Override
    public List<WeatherDetails> takeWeatherDetailsByRange(Date dateFrom, Date dateTo) throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {

            Query<WeatherDetails> query = session.createQuery(FIND_WEATHER_DETAILS_QUERY, WeatherDetails.class);
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);

            return query.getResultList();

        } catch (HibernateException e) {
            LOGGER.warn("Problems with getting info to data or another exception occurred", e);
            throw new RepositoryException("Failed to get data from DB or other problems are presented", e);
        }
    }

    public static final String GET_ACTUAL_QUERY = "FROM WeatherDetails ORDER BY localTime DESC";
    @Override
    public WeatherDetails takeLastWeatherResult() throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {
            Query<WeatherDetails> query = session.createQuery(GET_ACTUAL_QUERY, WeatherDetails.class);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (HibernateException e) {
            LOGGER.warn("Problems with getting info to data or another exception occurred", e);
            throw new RepositoryException("Failed to get actual weather data from DB or other problems are presented", e);
        }
    }

}
