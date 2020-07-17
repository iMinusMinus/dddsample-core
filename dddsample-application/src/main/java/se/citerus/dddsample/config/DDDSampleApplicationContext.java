package se.citerus.dddsample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.citerus.dddsample.application.ApplicationEvents;
import se.citerus.dddsample.application.BookingService;
import se.citerus.dddsample.application.CargoInspectionService;
import se.citerus.dddsample.application.HandlingEventService;
import se.citerus.dddsample.application.impl.BookingServiceImpl;
import se.citerus.dddsample.application.impl.CargoInspectionServiceImpl;
import se.citerus.dddsample.application.impl.HandlingEventServiceImpl;
import se.citerus.dddsample.domain.model.cargo.CargoRepository;
import se.citerus.dddsample.domain.model.handling.HandlingEventFactory;
import se.citerus.dddsample.domain.model.handling.HandlingEventRepository;
import se.citerus.dddsample.domain.model.location.LocationRepository;
import se.citerus.dddsample.domain.model.voyage.VoyageRepository;
import se.citerus.dddsample.domain.service.RoutingService;

@Configuration
public class DDDSampleApplicationContext {

    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    VoyageRepository voyageRepository;

    @Autowired
    RoutingService routingService;

    @Autowired
    HandlingEventFactory handlingEventFactory;

    @Autowired
    HandlingEventRepository handlingEventRepository;

    @Autowired
    ApplicationEvents applicationEvents;


    @Bean
    public BookingService bookingService() {
        return new BookingServiceImpl(cargoRepository, locationRepository, routingService);
    }

    @Bean
    public CargoInspectionService cargoInspectionService() {
        return new CargoInspectionServiceImpl(applicationEvents, cargoRepository, handlingEventRepository);
    }

    @Bean
    public HandlingEventService handlingEventService() {
        return new HandlingEventServiceImpl(handlingEventRepository, applicationEvents, handlingEventFactory);
    }

    @Bean
    public HandlingEventFactory handlingEventFactory() {
        return new HandlingEventFactory(cargoRepository, voyageRepository, locationRepository);
    }

}
