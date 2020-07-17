package se.citerus.dddsample.infrastructure.config;

import com.pathfinder.api.GraphTraversalService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.PlatformTransactionManager;
import se.citerus.dddsample.infrastructure.util.SampleDataGenerator;
import se.citerus.dddsample.domain.model.cargo.CargoRepository;
import se.citerus.dddsample.domain.model.handling.HandlingEventRepository;
import se.citerus.dddsample.domain.model.location.LocationRepository;
import se.citerus.dddsample.domain.model.voyage.VoyageRepository;
import se.citerus.dddsample.domain.service.RoutingService;
import se.citerus.dddsample.infrastructure.routing.ExternalRoutingService;

@Configuration
@ImportResource({
        "classpath:context-infrastructure-messaging.xml",
        "classpath:context-infrastructure-persistence.xml"})
public class DDDSampleApplicationContext {

    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    VoyageRepository voyageRepository;

    @Autowired
    GraphTraversalService graphTraversalService;

    @Autowired
    HandlingEventRepository handlingEventRepository;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    PlatformTransactionManager platformTransactionManager;


    @Bean
    public RoutingService routingService() {
        ExternalRoutingService routingService = new ExternalRoutingService();
        routingService.setGraphTraversalService(graphTraversalService);
        routingService.setLocationRepository(locationRepository);
        routingService.setVoyageRepository(voyageRepository);
        return routingService;
    }

    @Bean(initMethod = "generate")
    public SampleDataGenerator sampleDataGenerator() {
        return new SampleDataGenerator(platformTransactionManager, sessionFactory, cargoRepository, voyageRepository, locationRepository, handlingEventRepository);
    }
}
