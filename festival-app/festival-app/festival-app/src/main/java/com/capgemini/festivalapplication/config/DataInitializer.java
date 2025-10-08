package com.capgemini.festivalapplication.config;

import com.capgemini.festivalapplication.entity.Dj;
import com.capgemini.festivalapplication.entity.Performance;
import com.capgemini.festivalapplication.repository.DjRepository;
import com.capgemini.festivalapplication.repository.PerformanceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * DataInitializer is responsible for populating the database with sample data
 * when the application starts up. This is useful for testing and demonstration purposes.
 * 
 * This component implements CommandLineRunner, which means the run() method
 * will be executed after the Spring Boot application context is loaded.
 * 
 * Note: This component is disabled for test profile to avoid conflicts with test data.
 */
@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    // Using constructor injection instead of @Autowired for better practices
    private final DjRepository djRepository;
    private final PerformanceRepository performanceRepository;

    public DataInitializer(DjRepository djRepository, PerformanceRepository performanceRepository) {
        this.djRepository = djRepository;
        this.performanceRepository = performanceRepository;
    }

    /**
     * Initializes the database with sample data for testing purposes.
     * This method runs automatically when the application starts.
     */
    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty to avoid duplicates
        if (djRepository.count() > 0) {
            logger.info("Database already contains data. Skipping initialization.");
            return;
        }

        logger.info("Initializing database with sample data...");

        // Create sample DJs with diverse genres
        Dj dj1 = new Dj("DJ Shadow", "Hip Hop", "djshadow@festival.com");
        Dj dj2 = new Dj("Armin van Buuren", "Trance", "armin@festival.com");
        Dj dj3 = new Dj("Carl Cox", "Techno", "carlcox@festival.com");

        // Save DJs to database and get the persisted entities with generated IDs
        dj1 = djRepository.save(dj1);
        dj2 = djRepository.save(dj2);
        dj3 = djRepository.save(dj3);
        
        logger.info("Created {} DJs", djRepository.count());

        // Create sample performances with realistic future dates
        LocalDateTime baseDate = LocalDateTime.now().plusDays(30); // 30 days from now
        
        Performance performance1 = new Performance(
            "Summer Vibes Hip Hop Set",
            "An amazing hip hop set with classic and modern beats",
            baseDate.withHour(20).withMinute(0),
            baseDate.withHour(22).withMinute(0),
            dj1
        );

        Performance performance2 = new Performance(
            "Trance State of Mind",
            "Uplifting trance journey through the night",
            baseDate.plusDays(1).withHour(21).withMinute(0),
            baseDate.plusDays(1).withHour(23).withMinute(30),
            dj2
        );

        Performance performance3 = new Performance(
            "Techno Underground",
            "Deep underground techno experience",
            baseDate.plusDays(2).withHour(22).withMinute(0),
            baseDate.plusDays(3).withHour(1).withMinute(0),
            dj3
        );

        // Save performances to database
        performanceRepository.save(performance1);
        performanceRepository.save(performance2);
        performanceRepository.save(performance3);

        logger.info("Database initialization completed successfully!");
        logger.info("Total DJs: {}", djRepository.count());
        logger.info("Total Performances: {}", performanceRepository.count());
    }
}
