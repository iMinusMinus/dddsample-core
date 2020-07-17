package se.citerus.dddsample.interfaces.tracking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static se.citerus.dddsample.domain.model.location.SampleLocations.HELSINKI;
import static se.citerus.dddsample.domain.model.location.SampleLocations.STOCKHOLM;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.cargo.CargoRepository;
import se.citerus.dddsample.domain.model.cargo.RouteSpecification;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.handling.HandlingEventRepository;
import se.citerus.dddsample.domain.model.handling.HandlingHistory;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = CargoTrackingControllerTest.TestConfiguration.class)
public class CargoTrackingControllerTest {
    public static class TestConfiguration {

    }

    private MockMvc mockMvc;

    private CargoRepository cargoRepository;

    private HandlingEventRepository handlingEventRepository;

    @Before
    public void setup() throws Exception {
        cargoRepository = Mockito.mock(CargoRepository.class);
        handlingEventRepository = Mockito.mock(HandlingEventRepository.class);

        CargoTrackingController controller = new CargoTrackingController();
        controller.setCargoRepository(cargoRepository);
        controller.setHandlingEventRepository(handlingEventRepository);

        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/jsp/");
        resolver.setSuffix(".jsp");
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(resolver).build();
    }

    @Test
    public void canGetCargo() throws Exception {
        String trackingId = "ABC";
        TrackingId t = new TrackingId(trackingId);
        HandlingHistory handlingHistory = new HandlingHistory(Collections.emptyList());
        Mockito.when(handlingEventRepository.lookupHandlingHistoryOfCargo(t)).thenReturn(handlingHistory);
        RouteSpecification rs = new RouteSpecification(STOCKHOLM, HELSINKI, new Date());
        Cargo cargo = new Cargo(t, rs);
        cargo.deriveDeliveryProgress(handlingHistory);
        Mockito.when(cargoRepository.find(t)).thenReturn(cargo);
        Map<String, Object> model = mockMvc.perform(post("/track").param("trackingId", trackingId)).andReturn().getModelAndView().getModel();
        CargoTrackingViewAdapter cargoTrackingViewAdapter = (CargoTrackingViewAdapter) model.get("cargo");
        assertThat(cargoTrackingViewAdapter.getTrackingId()).isEqualTo(trackingId);
    }

    @Test
    public void cannnotGetUnknownCargo() throws Exception {
        String trackingId = "UNKNOWN";
        TrackingId t = new TrackingId(trackingId);
        Mockito.when(cargoRepository.find(t)).thenReturn(null);
        Map<String, Object> model = mockMvc.perform(post("/track").param("trackingId", trackingId)).andReturn().getModelAndView().getModel();
        Errors errors = (Errors) model.get(BindingResult.MODEL_KEY_PREFIX + "trackCommand");
        FieldError fe = errors.getFieldError("trackingId");
        assertThat(fe.getCode()).isEqualTo("cargo.unknown_id");
        assertThat(fe.getArguments().length).isEqualTo(1);
        assertThat(fe.getArguments()[0]).isEqualTo(trackingId);
    }

}