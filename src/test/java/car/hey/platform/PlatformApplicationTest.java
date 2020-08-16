package car.hey.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;

import car.hey.platform.model.Vehicle;
import car.hey.platform.model.VehicleListing;
import car.hey.platform.repository.VehicleListingRepository;

/**
 * Platform Application Test
 * 
 * @author shijin.raj
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class PlatformApplicationTest {

	private static final String BASE_URL = "/api/vehicle_listings/";
	private static final String LOCAL_HOST = "http://localhost:";
	private static final String BASE_UPLOAD_URL = "/api/upload_csv/";

	private static final String SEARCH = "search?";

	private String baseUrl = null;
	private String baseUploadUrl = null;

	private static Vehicle mercedes = null;
	private static VehicleListing savedMercedes = null;
	private static Vehicle audi = null;
	private static VehicleListing savedAudi = null;
	private static Vehicle vw = null;
	private static VehicleListing savedVw = null;
	private static Vehicle skoda = null;
	private static VehicleListing savedSkoda = null;

	private static final String VALID_VEHICLE_LIST_CSV = "ValidVehicleList.csv";

	private static final String INVALID_VEHICLE_LIST_CSV = "InvalidVehicleList.csv";

	@LocalServerPort
	int randomServerPort;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private VehicleListingRepository vehicleListingRepository;

	@BeforeAll
	void setUpBeforeClass() {
		baseUrl = LOCAL_HOST + randomServerPort + BASE_URL;

		baseUploadUrl = LOCAL_HOST + randomServerPort + BASE_UPLOAD_URL;

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

	@Test
	@Order(1)
	final void testGetAllVehicleListing() {
		// Given
		List<VehicleListing> vehicleListings = vehicleListingRepository
				.saveAll(Arrays.asList(savedMercedes, savedAudi, savedVw, savedSkoda));
		// When
		ResponseEntity<VehicleListing[]> responseActualEntity = restTemplate.getForEntity(baseUrl,
				VehicleListing[].class);
		// Then
		assertEquals(vehicleListings, Arrays.asList(responseActualEntity.getBody()));
	}

	/**
	 * Test method for Add Vehicles
	 * {@link car.hey.platform.controller.VehicleListingController#addVehicleListing(java.lang.Integer, java.util.List)}.
	 * 
	 * @throws Exception
	 * @throws JsonProcessingException
	 */
	@Order(2)
	@DisplayName("Test add valid Vehicles")
	@ParameterizedTest(name = "Test add valid {0} Vehicle with dealer id {1}")
	@MethodSource("provideValidVehicleListings")
	final void testAddVehicles(String make, int dealerId, List<Vehicle> vehicle, List<VehicleListing> vehicleListing)
			throws JsonProcessingException, Exception {
		// Given
		List<VehicleListing> vehicleListings = vehicleListingRepository.saveAll(vehicleListing);
		// When
		ResponseEntity<VehicleListing[]> responseActualEntity = restTemplate.postForEntity(baseUrl + dealerId, vehicle,
				VehicleListing[].class);
		// Then
		assertEquals(vehicleListings, Arrays.asList(responseActualEntity.getBody()));
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.controller.VehicleListingController#searchVehicleListing(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 * 
	 * @throws Exception
	 * @throws JsonProcessingException
	 */
	@Order(3)
	@DisplayName("Test Search with valid seach inputs")
	@ParameterizedTest(name = "Test Search with make {0} model {1} year {2} and color {3}")
	@MethodSource("provideValidSearchInputs")
	final void testSearchVehicleListing(String make, String model, int year, String color,
			List<VehicleListing> vehicleListing) throws JsonProcessingException, Exception {

		// Given
		List<VehicleListing> vehicleListings = vehicleListingRepository.saveAll(vehicleListing);

		// When
		ResponseEntity<VehicleListing[]> responseActualEntity = restTemplate.getForEntity(
				baseUrl + SEARCH + "make={make}&model={model}&year={year}&color={color}", VehicleListing[].class, make,
				model, year, color);
		// Then
		assertEquals(vehicleListings, Arrays.asList(responseActualEntity.getBody()));
	}

	@Order(4)
	@Test
	@DisplayName("Test Upload Valid File")
	final void testUploadValidFile() throws Exception {
		// Given
		int dealerId = 123;
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new ClassPathResource(VALID_VEHICLE_LIST_CSV));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);

		// When
		ResponseEntity<String> result = restTemplate.exchange(baseUploadUrl + dealerId, HttpMethod.POST, requestEntity,
				String.class);

		assertEquals(HttpStatus.OK, result.getStatusCode());

	}

	@Order(5)
	@Test
	@DisplayName("Test Upload Invalid File")
	final void testUploadInvalidFile() throws Exception {
		// Given
		int dealerId = 123;
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new ClassPathResource(INVALID_VEHICLE_LIST_CSV));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);

		// When
		ResponseEntity<String> result = restTemplate.exchange(baseUploadUrl + dealerId, HttpMethod.POST, requestEntity,
				String.class);

		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

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
	 * creates Stream<Arguments> for Valid Color
	 * 
	 * @return Stream<Arguments> Valid Color
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> provideValidSearchInputs() {
		// make, model, year, color
		return Stream.of(Arguments.of("mercedes", "a 180", 2014, "black", Collections.singletonList(savedMercedes)),
				Arguments.of("audi", "a3", 2016, "white", Collections.singletonList(savedAudi)),
				Arguments.of("vw", "golf", 2018, "green", Collections.singletonList(savedVw)),
				Arguments.of("skoda", "octavia", 2018, "green", Collections.singletonList(savedSkoda)));
	}

}
