package com.weather.project.repository.impl;

import com.weather.project.model.WeatherDetails;
import com.weather.project.repository.WeatherRepository;
import com.weather.project.repository.exception.RepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Repository
@Transactional
public class WeatherRepositoryImpl implements WeatherRepository {

    private static final Logger LOGGER = LogManager.getRootLogger();
    public static final String DATE_FROM = "dateFrom";
    public static final String DATE_TO = "dateTo";

    private final SessionFactory sessionFactory;

    @Autowired
    public WeatherRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveWeatherData(WeatherDetails weatherDetails) throws RepositoryException {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(weatherDetails);
        } catch (HibernateException e) {
            LOGGER.warn("Problems with adding info to data or another exception occurred", e);
            throw new RepositoryException("Failed to add weather data to database", e);
        }
    }

    public static final String FIND_WEATHER_DETAILS_QUERY = "FROM WeatherDetails wd where wd.localTime BETWEEN :dateFrom AND :dateTo";

    @Override
    public List<WeatherDetails> takeWeatherDetailsByRange(Date dateFrom, Date dateTo) throws RepositoryException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query<WeatherDetails> query = session.createQuery(FIND_WEATHER_DETAILS_QUERY, WeatherDetails.class);
            query.setParameter(DATE_FROM, dateFrom);
            query.setParameter(DATE_TO, dateTo);
            return query.getResultList();
        } catch (HibernateException e) {
            LOGGER.warn("Problems with getting info to data or another exception occurred", e);
            throw new RepositoryException("Failed to get data from DB or other problems are presented", e);
        }
    }

    public static final String GET_ACTUAL_QUERY = "FROM WeatherDetails ORDER BY localTime DESC";

    @Override
    public WeatherDetails takeLastWeatherResult() throws RepositoryException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query<WeatherDetails> query = session.createQuery(GET_ACTUAL_QUERY, WeatherDetails.class);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (HibernateException e) {
            LOGGER.warn("Problems with getting info to data or another exception occurred", e);
            throw new RepositoryException("Failed to get actual weather data from DB or other problems are presented", e);
        }
    }
}
