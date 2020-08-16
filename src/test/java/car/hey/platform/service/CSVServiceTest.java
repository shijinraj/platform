package car.hey.platform.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import car.hey.platform.repository.VehicleListingRepository;

@ExtendWith(SpringExtension.class)
@DisplayName("CSV Service Test")
@TestInstance(Lifecycle.PER_CLASS)
class CSVServiceTest {

	private static final String INVALID_VEHICLE_LIST_CSV = "InvalidVehicleList.csv";

	private static final String CLASSPATH = "classpath:";

	private static final String VALID_VEHICLE_LIST_CSV = "ValidVehicleList.csv";

	@Mock
	private VehicleListingRepository vehicleListingRepository;

	@InjectMocks
	private CSVService csvService = new CSVServiceImpl();

	@Test
	@DisplayName("Test Save with Null inputs")
	final void testSaveWithNullInputs() throws IOException {
		// When both the inputs are null , Then throws IllegalArgumentException
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> csvService.save(null, null));
		assertEquals("Dealer Id cannot be null", illegalArgumentException.getMessage());

	}

	@Test()
	@DisplayName("Test Save with Null Dealer Id")
	final void testSaveWithNullDealerId() throws IOException {
		// When
		MockMultipartFile csvFile = new MockMultipartFile(VALID_VEHICLE_LIST_CSV, VALID_VEHICLE_LIST_CSV, null,
				FileUtils.readFileToByteArray(ResourceUtils.getFile(CLASSPATH + VALID_VEHICLE_LIST_CSV)));

		// Then throws IllegalArgumentException
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> csvService.save(null, csvFile));
		assertEquals("Dealer Id cannot be null", illegalArgumentException.getMessage());

	}

	@Test()
	@DisplayName("Test Save with Null CSV File")
	final void testSaveWithNullCSVFile() throws IOException {
		// When
		// Then throws IllegalArgumentException
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> csvService.save(1, null));
		assertEquals("Multipart CSV File cannot be null", illegalArgumentException.getMessage());

	}

	@Test()
	@DisplayName("Test Save with Invalid CSV File")
	final void testSaveWithInvalidCSVFile() throws IOException {
		// When
		MockMultipartFile invalidCSVFile = new MockMultipartFile(INVALID_VEHICLE_LIST_CSV, INVALID_VEHICLE_LIST_CSV, null,
				FileUtils.readFileToByteArray(ResourceUtils.getFile(CLASSPATH + INVALID_VEHICLE_LIST_CSV)));
		// Then throws IllegalArgumentException
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> csvService.save(1, invalidCSVFile));
		assertEquals("Index for header 'price' is 5 but CSVRecord only has 5 values!",
				illegalArgumentException.getMessage());

	}

	@Test
	@DisplayName("Test Save")
	final void testSave() throws IOException {
		// Given
		when(vehicleListingRepository.save(any())).thenReturn(null);

		MockMultipartFile csvFile = new MockMultipartFile(VALID_VEHICLE_LIST_CSV, VALID_VEHICLE_LIST_CSV, null,
				FileUtils.readFileToByteArray(ResourceUtils.getFile(CLASSPATH + VALID_VEHICLE_LIST_CSV)));
		// When
		csvService.save(1, csvFile);

		// Then CSV file save call executed
	}

}
