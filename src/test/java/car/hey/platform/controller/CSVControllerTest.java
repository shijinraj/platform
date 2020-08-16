package car.hey.platform.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import car.hey.platform.repository.VehicleListingRepository;
import car.hey.platform.service.CSVService;
import car.hey.platform.service.VehicleListingService;

/**
 * CSVController Test
 * 
 * @author shijin.raj
 *
 */
@WebMvcTest
@TestInstance(Lifecycle.PER_CLASS)
class CSVControllerTest {

	private static final String BASE_URL = "/api/upload_csv/";

	private static final String VALID_VEHICLE_LIST_CSV = "ValidVehicleList.csv";

	private static final String INVALID_VEHICLE_LIST_CSV = "InvalidVehicleList.csv";

	private static final String CLASSPATH = "classpath:";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VehicleListingService vehicleListingService;

	@MockBean
	private CSVService csvService;

	@MockBean
	private VehicleListingRepository vehicleListingRepository;

	/**
	 * Test method for
	 * {@link car.hey.platform.controller.CSVController#uploadFile(java.lang.Integer, org.springframework.web.multipart.MultipartFile)}.
	 * 
	 * @throws Exception
	 */
	@Test
	final void testUploadFile() throws Exception {
		// Given
		int dealerId = 123;
		doNothing().when(csvService).save(any(Integer.class), any(MultipartFile.class));

		MockMultipartFile csvFile = new MockMultipartFile("file", VALID_VEHICLE_LIST_CSV, null,
				FileUtils.readFileToByteArray(ResourceUtils.getFile(CLASSPATH + VALID_VEHICLE_LIST_CSV)));

		// When & Then
		mockMvc.perform(
				MockMvcRequestBuilders.multipart(BASE_URL + dealerId).file(csvFile).param("dealer_id", "" + dealerId))
				.andDo(print()).andExpect(status().is(200));
	}

	@Test()
	@DisplayName("Test Upload File with Invalid CSV File")
	final void testUploadFileWithInvalidCSVFile() throws Exception {
		// Given
		int dealerId = 123;

		doThrow(new IllegalArgumentException("Index for header 'price' is 5 but CSVRecord only has 5 values!"))
				.when(csvService).save(any(Integer.class), any(MultipartFile.class));

		MockMultipartFile csvFile = new MockMultipartFile("file", INVALID_VEHICLE_LIST_CSV, null,
				FileUtils.readFileToByteArray(ResourceUtils.getFile(CLASSPATH + INVALID_VEHICLE_LIST_CSV)));

		// When & Then
		mockMvc.perform(
				MockMvcRequestBuilders.multipart(BASE_URL + dealerId).file(csvFile).param("dealer_id", "" + dealerId))
				.andDo(print()).andExpect(status().is(400));

	}

}
