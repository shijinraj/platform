package car.hey.platform.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import car.hey.platform.model.VehicleListing;
import car.hey.platform.repository.VehicleListingRepository;

/**
 * VehicleListing Service Test
 * 
 * @author shijin.raj
 *
 */
@ExtendWith(SpringExtension.class)
@DisplayName("VehicleListing Service Test")
@TestInstance(Lifecycle.PER_CLASS)
class VehicleListingServiceTest {

	@Mock
	private VehicleListingRepository vehicleListingRepository;

	@InjectMocks
	private VehicleListingService vehicleListingService = new VehicleListingServiceImpl();

	private static VehicleListing mercedes = null;
	private static VehicleListing audi = null;
	private static VehicleListing vw = null;
	private static VehicleListing skoda = null;

	@BeforeAll
	void setUpBeforeClass() throws Exception {
		mercedes = VehicleListing.builder().dealerId(123).code("1").make("mercedes").model("a 180").kW(123).year(2014)
				.color("black").price(15950).build();
		audi = VehicleListing.builder().dealerId(123).code("1").make("audi").model("a3").kW(111).year(2016)
				.color("white").price(17210).build();
		vw = VehicleListing.builder().dealerId(123).code("1").make("vw").model("golf").kW(86).year(2018).color("green")
				.price(14980).build();
		skoda = VehicleListing.builder().dealerId(123).code("1").make("skoda").model("octavia").kW(86).year(2018)
				.color("green").price(16990).build();
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#findAll()}.
	 */
	@Test
	final void testFindAll() {
		// Given
		List<VehicleListing> vehicleListingsExpected = Collections.singletonList(VehicleListing.builder().dealerId(123)
				.code("1").make("mercedes").model("a 180").kW(123).year(2014).color("black").price(15950).build());
		when(vehicleListingRepository.findAll()).thenReturn(vehicleListingsExpected);
		// When
		List<VehicleListing> vehicleListingsActual = vehicleListingService.findAll();
		// Then
		assertEquals(vehicleListingsExpected, vehicleListingsActual);
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#saveAll(java.lang.Integer, java.util.List)}.
	 */
	@Test
	@DisplayName("Test Save with null inputs")
	final void testSaveAllWithNullInputs() {
		// When both the inputs are null , Then throws IllegalArgumentException
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> vehicleListingService.saveAll(null, null));
		assertEquals("Dealer Id cannot be null", illegalArgumentException.getMessage());
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#saveAll(java.lang.Integer, java.util.List)}.
	 */
	@Test
	@DisplayName("Test Save with null dealer id")
	final void testSaveAllWithNullDealerId() {
		// When & Then throws IllegalArgumentException
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> vehicleListingService.saveAll(null, Collections.emptyList()));
		assertEquals("Dealer Id cannot be null", illegalArgumentException.getMessage());
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#saveAll(java.lang.Integer, java.util.List)}.
	 */
	@Test
	@DisplayName("Test Save with null VehicleListings")
	final void testSaveAllWithNullVehicleListings() {
		// When & Then throws IllegalArgumentException
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> vehicleListingService.saveAll(123, null));
		assertEquals("Vehicle listings cannot be empty", illegalArgumentException.getMessage());
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#saveAll(java.lang.Integer, java.util.List)}.
	 */
	@Test
	@DisplayName("Test Save All")
	final void testSaveAll() {
		// Given
		List<VehicleListing> vehicleListingsExpected = Collections.singletonList(VehicleListing.builder().dealerId(123)
				.code("1").make("mercedes").model("a 180").kW(123).year(2014).color("black").price(15950).build());

		when(vehicleListingRepository.saveAll(vehicleListingsExpected)).thenReturn(vehicleListingsExpected);

		// When
		List<VehicleListing> vehicleListingsActual = vehicleListingService.saveAll(123, vehicleListingsExpected);
		// Then
		assertEquals(vehicleListingsExpected, vehicleListingsActual);
	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#search(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 */
	@Test
	@DisplayName("Test Search with null inputs")
	final void testSearchWithNullInputs() {

		// Given
		List<VehicleListing> vehicleListingsExpected = Collections.singletonList(VehicleListing.builder().dealerId(123)
				.code("1").make("mercedes").model("a 180").kW(123).year(2014).color("black").price(15950).build());
		when(vehicleListingRepository
				.findAll(Example.of(VehicleListing.builder().make(null).model(null).year(null).color(null).build(),
						ExampleMatcher.matchingAll().withIgnoreCase()))).thenReturn(vehicleListingsExpected);

		// When
		List<VehicleListing> vehicleListingsActual = vehicleListingService.search(null, null, null, null);
		// Then
		assertEquals(vehicleListingsExpected, vehicleListingsActual);

	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#search(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 */
	@DisplayName("Test Search with valid models")
	@ParameterizedTest(name = "Test Search with valid model - {0} ")
	@MethodSource("provideValidModels")
	final void testSearchWithValidModels(String model, List<VehicleListing> vehicleListingsExpected) {
		// Given
		when(vehicleListingRepository
				.findAll(Example.of(VehicleListing.builder().make(null).model(model).year(null).color(null).build(),
						ExampleMatcher.matchingAll().withIgnoreCase()))).thenReturn(vehicleListingsExpected);

		// When
		List<VehicleListing> vehicleListingsActual = vehicleListingService.search(null, model, null, null);
		// Then
		assertEquals(vehicleListingsExpected, vehicleListingsActual);

	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#search(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 */
	@DisplayName("Test Search with invalid models")
	@ParameterizedTest(name = "Test Search with invalid model - {0} ")
	@ValueSource(strings = { "", "  ", " a 20" })
	final void testSearchWithInvalidModel(String model) {

		// Given
		when(vehicleListingRepository
				.findAll(Example.of(VehicleListing.builder().make(null).model(model).year(null).color(null).build(),
						ExampleMatcher.matchingAll().withIgnoreCase()))).thenReturn(null);

		// When
		List<VehicleListing> vehicleListingsActual = vehicleListingService.search(null, model, null, null);
		// Then
		assertEquals(null, vehicleListingsActual);

	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#search(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 */
	@DisplayName("Test Search with valid makes")
	@ParameterizedTest(name = "Test Search with valid make - {0} ")
	@MethodSource("provideValidMakes")
	final void testSearchWithValidMakes(String make, List<VehicleListing> vehicleListingsExpected) {
		// Given
		when(vehicleListingRepository
				.findAll(Example.of(VehicleListing.builder().make(make).model(null).year(null).color(null).build(),
						ExampleMatcher.matchingAll().withIgnoreCase()))).thenReturn(vehicleListingsExpected);

		// When
		List<VehicleListing> vehicleListingsActual = vehicleListingService.search(make, null, null, null);
		// Then
		assertEquals(vehicleListingsExpected, vehicleListingsActual);

	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#search(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 */
	@DisplayName("Test Search with invalid makes")
	@ParameterizedTest(name = "Test Search with invalid make - {0} ")
	@ValueSource(strings = { "", "  ", "Maruti" })
	final void testSearchWithInvalidMakes(String make) {

		// Given
		when(vehicleListingRepository
				.findAll(Example.of(VehicleListing.builder().make(make).model(null).year(null).color(null).build(),
						ExampleMatcher.matchingAll().withIgnoreCase()))).thenReturn(null);

		// When
		List<VehicleListing> vehicleListingsActual = vehicleListingService.search(make, null, null, null);
		// Then
		assertEquals(null, vehicleListingsActual);

	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#search(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 */
	@DisplayName("Test Search with valid years")
	@ParameterizedTest(name = "Test Search with valid year - {0} ")
	@MethodSource("provideValidYears")
	final void testSearchWithValidYears(int year, List<VehicleListing> vehicleListingsExpected) {
		// Given
		when(vehicleListingRepository
				.findAll(Example.of(VehicleListing.builder().make(null).model(null).year(year).color(null).build(),
						ExampleMatcher.matchingAll().withIgnoreCase()))).thenReturn(vehicleListingsExpected);

		// When
		List<VehicleListing> vehicleListingsActual = vehicleListingService.search(null, null, year, null);
		// Then
		assertEquals(vehicleListingsExpected, vehicleListingsActual);

	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#search(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 */
	@DisplayName("Test Search with invalid years")
	@ParameterizedTest(name = "Test Search with invalid year - {0} ")
	@ValueSource(ints = { 2000, 2001, 2002 })
	final void testSearchWithInvalidYears(int year) {

		// Given
		when(vehicleListingRepository
				.findAll(Example.of(VehicleListing.builder().make(null).model(null).year(year).color(null).build(),
						ExampleMatcher.matchingAll().withIgnoreCase()))).thenReturn(null);

		// When
		List<VehicleListing> vehicleListingsActual = vehicleListingService.search(null, null, year, null);
		// Then
		assertEquals(null, vehicleListingsActual);

	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#search(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 */
	@DisplayName("Test Search with valid Colors")
	@ParameterizedTest(name = "Test Search with valid color - {0} ")
	@MethodSource("provideValidColors")
	final void testSearchWithValidColors(String color, List<VehicleListing> vehicleListingsExpected) {
		// Given
		when(vehicleListingRepository
				.findAll(Example.of(VehicleListing.builder().make(null).model(null).year(null).color(color).build(),
						ExampleMatcher.matchingAll().withIgnoreCase()))).thenReturn(vehicleListingsExpected);

		// When
		List<VehicleListing> vehicleListingsActual = vehicleListingService.search(null, null, null, color);
		// Then
		assertEquals(vehicleListingsExpected, vehicleListingsActual);

	}

	/**
	 * Test method for
	 * {@link car.hey.platform.service.VehicleListingService#search(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)}.
	 */
	@DisplayName("Test Search with invalid colors")
	@ParameterizedTest(name = "Test Search with invalid color - {0} ")
	@ValueSource(strings = { "", "  ", "Blue" })
	final void testSearchWithInvalidColors(String color) {

		// Given
		when(vehicleListingRepository
				.findAll(Example.of(VehicleListing.builder().make(null).model(null).year(null).color(color).build(),
						ExampleMatcher.matchingAll().withIgnoreCase()))).thenReturn(null);

		// When
		List<VehicleListing> vehicleListingsActual = vehicleListingService.search(null, null, null, color);
		// Then
		assertEquals(null, vehicleListingsActual);
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

}
