package car.hey.platform.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import car.hey.platform.model.Vehicle;
import car.hey.platform.model.VehicleListing;
import car.hey.platform.repository.VehicleListingRepository;
import car.hey.platform.service.CSVService;
import car.hey.platform.service.VehicleListingService;

/**
 * VehicleListingController Test
 * 
 * @author shijin.raj
 *
 */
@WebMvcTest
@TestInstance(Lifecycle.PER_CLASS)
class VehicleListingControllerTest {

	private static final String UTF_8 = "utf-8";

	private static final String SEARCH = "search";

	private static final String BASE_URL = "/api/vehicle_listings/";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VehicleListingService vehicleListingService;

	@MockBean
	private CSVService csvService;

	@MockBean
	private VehicleListingRepository vehicleListingRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private static Vehicle mercedes = null;
	private static VehicleListing savedMercedes = null;
	private static Vehicle audi = null;
	private static VehicleListing savedAudi = null;
	private static Vehicle vw = null;
	private static VehicleListing savedVw = null;
	private static Vehicle skoda = null;
	private static VehicleListing savedSkoda = null;

	@BeforeAll
	void setUpBeforeClass() throws Exception {
		mercedes = Vehicle.builder().code("1").make("mercedes").model("a 180").kW(123).year(2014).color("black")
				.price(15950).build();
		savedMercedes = VehicleListing.builder().dealerId(123).code("1").make("mercedes").model("a 180").kW(123)
				.year(2014).color("black").price(15950).build();
		audi = Vehicle.builder().code("1").make("audi").model("a3").kW(111).year(2016).color("white").price(17210)
				.build();
		savedAudi = VehicleListing.builder().dealerId(456).code("1").make("audi").model("a3").kW(111).year(2016)
				.color("white").price(17210).build();
		vw = Vehicle.builder().code("1").make("vw").model("golf").kW(86).year(2018).color("green").price(14980).build();
		savedVw = VehicleListing.builder().dealerId(789).code("1").make("vw").model("golf").kW(86).year(2018)
				.color("green").price(14980).build();
		skoda = Vehicle.builder().code("1").make("skoda").model("octavia").kW(86).year(2018).color("green").price(16990)
				.build();
		savedSkoda = VehicleListing.builder().dealerId(910).code("1").make("skoda").model("octavia").kW(86).year(2018)
				.color("green").price(16990).build();
	}

	/**
	 * creates Stream<Arguments> for Valid Color
	 * 
	 * @return Stream<Arguments> Valid Color
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> provideValidSearchInputs() {
		// make, model, year, color
		return Stream.of(Arguments.of("mercedes", "a 180", 2014, "black", Collections.singletonList(mercedes)),
				Arguments.of("audi", "a3", 2016, "white", Collections.singletonList(audi)),
				Arguments.of("vw", "golf", 2018, "green", Collections.singletonList(vw)),
				Arguments.of("skoda", "octavia", 2018, "green", Collections.singletonList(skoda)));
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.controller.VehicleListingController#getAllVehicleListing()}.
	 * 
	 * @throws Exception
	 * @throws JsonProcessingException
	 */
	@Test
	final void testGetAllVehicleListing() throws JsonProcessingException, Exception {
		// Given
		List<VehicleListing> vehicleListings = Arrays.asList(savedMercedes, savedAudi, savedVw, savedSkoda);
		when(vehicleListingService.findAll()).thenReturn(vehicleListings);

		// When & Then
		mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON_VALUE).characterEncoding(UTF_8))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(objectMapper.writeValueAsString(vehicleListings))));
	}

	@Test
	@DisplayName("Test add Vehicles with Invalid Input")
	final void testAddVehicleswithInvalidInput() throws JsonProcessingException, Exception {
		mockMvc.perform(post(BASE_URL + 123).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
				.andExpect(status().isConflict())
				.andExpect(content().string(containsString("Required request body is missing:")));
	}

	/**
	 * Test method for Add Vehicles
	 * {@link car.hey.platform.controller.VehicleListingController#addVehicleListing(java.lang.Integer, java.util.List)}.
	 * 
	 * @throws Exception
	 * @throws JsonProcessingException
	 */
	@DisplayName("Test add valid Vehicles")
	@ParameterizedTest(name = "Test add valid {0} Vehicle with dealer id {1}")
	@MethodSource("provideValidVehicleListings")
	final void testAddVehicles(String make, int dealerId, List<Vehicle> vehicle, List<VehicleListing> vehicleListing)
			throws JsonProcessingException, Exception {
		// Given
		when(vehicleListingService.saveAll(dealerId, vehicleListing)).thenReturn(vehicleListing);
		// When & Then
		mockMvc.perform(post(BASE_URL + dealerId).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(vehicle)).characterEncoding(UTF_8)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(objectMapper.writeValueAsString(vehicleListing))));
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.controller.VehicleListingController#searchVehicleListing(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 * 
	 * @throws Exception
	 * @throws JsonProcessingException
	 */
	@DisplayName("Test Search with valid seach inputs")
	@ParameterizedTest(name = "Test Search with make {0} model {1} year {2} and color {3}")
	@MethodSource("provideValidSearchInputs")
	final void testSearchVehicleListing(String make, String model, int year, String color,
			List<VehicleListing> vehicleListings) throws JsonProcessingException, Exception {

		// Given
		when(vehicleListingService.search(make, model, year, color)).thenReturn(vehicleListings);

		// When & Then
		mockMvc.perform(get(BASE_URL + SEARCH).param("make", make).param("model", model).param("year", "" + year)
				.param("color", color).contentType(MediaType.APPLICATION_JSON_VALUE).characterEncoding(UTF_8))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(objectMapper.writeValueAsString(vehicleListings))));
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.controller.VehicleListingController#searchVehicleListing(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 * 
	 * @throws Exception
	 * @throws JsonProcessingException
	 */
	@DisplayName("Test Search with valid models")
	@ParameterizedTest(name = "Test Search with valid model - {0} ")
	@MethodSource("provideValidModels")
	final void testSearchWithValidModels(String model, List<VehicleListing> vehicleListings)
			throws JsonProcessingException, Exception {

		// Given
		when(vehicleListingService.search(null, model, null, null)).thenReturn(vehicleListings);

		// When & Then
		mockMvc.perform(get(BASE_URL + SEARCH).param("model", model).contentType(MediaType.APPLICATION_JSON_VALUE)
				.characterEncoding(UTF_8)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(objectMapper.writeValueAsString(vehicleListings))));
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.controller.VehicleListingController#searchVehicleListing(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 * 
	 * @throws Exception
	 * @throws JsonProcessingException
	 */
	@DisplayName("Test Search with valid makes")
	@ParameterizedTest(name = "Test Search with valid make - {0} ")
	@MethodSource("provideValidMakes")
	final void testSearchWithValidMakes(String make, List<VehicleListing> vehicleListings)
			throws JsonProcessingException, Exception {

		// Given
		when(vehicleListingService.search(make, null, null, null)).thenReturn(vehicleListings);

		// When & Then
		mockMvc.perform(get(BASE_URL + SEARCH).param("make", make).contentType(MediaType.APPLICATION_JSON_VALUE)
				.characterEncoding(UTF_8)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(objectMapper.writeValueAsString(vehicleListings))));
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.controller.VehicleListingController#searchVehicleListing(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 * 
	 * @throws Exception
	 * @throws JsonProcessingException
	 */
	@DisplayName("Test Search with valid years")
	@ParameterizedTest(name = "Test Search with valid year - {0} ")
	@MethodSource("provideValidYears")
	final void testSearchWithValidYears(int year, List<VehicleListing> vehicleListings)
			throws JsonProcessingException, Exception {

		// Given
		when(vehicleListingService.search(null, null, year, null)).thenReturn(vehicleListings);

		// When & Then
		mockMvc.perform(get(BASE_URL + SEARCH).param("year", "" + year).contentType(MediaType.APPLICATION_JSON_VALUE)
				.characterEncoding(UTF_8)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(objectMapper.writeValueAsString(vehicleListings))));
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.controller.VehicleListingController#searchVehicleListing(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 * 
	 * @throws Exception
	 * @throws JsonProcessingException
	 */
	@DisplayName("Test Search with valid clors")
	@ParameterizedTest(name = "Test Search with valid color - {0} ")
	@MethodSource("provideValidColors")
	final void testSearchWithValidColors(String color, List<VehicleListing> vehicleListings)
			throws JsonProcessingException, Exception {

		// Given
		when(vehicleListingService.search(null, null, null, color)).thenReturn(vehicleListings);

		// When & Then
		mockMvc.perform(get(BASE_URL + SEARCH).param("color", color).contentType(MediaType.APPLICATION_JSON_VALUE)
				.characterEncoding(UTF_8)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(objectMapper.writeValueAsString(vehicleListings))));
	}

	/**
	 * creates Stream<Arguments> for Vehicle Listings
	 * 
	 * @return Stream<Arguments> Vehicle Listings
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> provideValidVehicleListings() {

		return Stream.of(
				Arguments.of(mercedes.getMake(), savedMercedes.getDealerId(), Collections.singletonList(mercedes),
						Collections.singletonList(savedMercedes)),
				Arguments.of(savedAudi.getMake(), savedAudi.getDealerId(), Collections.singletonList(audi),
						Collections.singletonList(savedAudi)),
				Arguments.of(savedVw.getMake(), savedVw.getDealerId(), Collections.singletonList(vw),
						Collections.singletonList(savedVw)),
				Arguments.of(savedSkoda.getMake(), savedSkoda.getDealerId(), Collections.singletonList(skoda),
						Collections.singletonList(savedSkoda)));
	}

	/**
	 * creates Stream<Arguments> for Valid Models
	 * 
	 * @return Stream<Arguments> Valid Models
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> provideValidModels() {
		return Stream.of(Arguments.of("a 180", Collections.singletonList(mercedes)),
				Arguments.of("a3", Collections.singletonList(audi)),
				Arguments.of("golf", Collections.singletonList(vw)),
				Arguments.of("octavia", Collections.singletonList(skoda)));
	}

	/**
	 * creates Stream<Arguments> for Valid Makes
	 * 
	 * @return Stream<Arguments> Valid Makes
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> provideValidMakes() {
		return Stream.of(Arguments.of("mercedes", Collections.singletonList(mercedes)),
				Arguments.of("audi", Collections.singletonList(audi)),
				Arguments.of("vw", Collections.singletonList(vw)),
				Arguments.of("skoda", Collections.singletonList(skoda)));
	}

	/**
	 * creates Stream<Arguments> for Valid Year
	 * 
	 * @return Stream<Arguments> Valid Year
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> provideValidYears() {
		return Stream.of(Arguments.of(2014, Collections.singletonList(mercedes)),
				Arguments.of(2016, Collections.singletonList(audi)), Arguments.of(2018, Collections.singletonList(vw)),
				Arguments.of(2018, Collections.singletonList(skoda)));
	}

	/**
	 * creates Stream<Arguments> for Valid Color
	 * 
	 * @return Stream<Arguments> Valid Color
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> provideValidColors() {
		return Stream.of(Arguments.of("black", Collections.singletonList(mercedes)),
				Arguments.of("white", Collections.singletonList(audi)),
				Arguments.of("green", Collections.singletonList(vw)),
				Arguments.of("green", Collections.singletonList(skoda)));
	}
}
