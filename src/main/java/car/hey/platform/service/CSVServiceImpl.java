package car.hey.platform.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import car.hey.platform.model.VehicleListing;
import car.hey.platform.repository.VehicleListingRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * CSV Service Implementation
 * 
 * @author shijin.raj
 *
 */
@Slf4j
@Service
public class CSVServiceImpl implements CSVService {

	private static final String PRICE = "price";
	private static final String COLOR = "color";
	private static final String YEAR = "year";
	private static final String POWER_IN_PS = "power-in-ps";
	private static final String REGEX = "/";
	private static final String MAKE_MODEL = "make/model";
	private static final String CODE = "code";
	public static String CSV = "csv";
	static String[] HEADERS = { CODE, MAKE_MODEL, POWER_IN_PS, YEAR, COLOR, PRICE };

	@Autowired
	private VehicleListingRepository vehicleListingRepository;

	/*
	 * @see car.hey.platform.service.CSVService#save(java.lang.Integer,
	 * org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public void save(Integer dealerId, MultipartFile csvFile) throws IOException {
		log.info("Saving CSV file {} for the dealer {}", csvFile, dealerId);

		Assert.notNull(dealerId, "Dealer Id cannot be null");
		Assert.notNull(csvFile, "Multipart CSV File cannot be null");

		String extension = FilenameUtils.getExtension(csvFile.getOriginalFilename());

		Assert.isTrue(CSV.equalsIgnoreCase(extension), "Invalid file");

		List<VehicleListing> vehicleListings = csvToVehicleListings(csvFile);

		// Set dealer id in every Vehicle Listing entity
		vehicleListings.stream().forEach(vehicleListing -> {
			vehicleListing.setDealerId(dealerId);
		});

		vehicleListingRepository.saveAll(vehicleListings);

	}

	private List<VehicleListing> csvToVehicleListings(MultipartFile csvFile) throws IOException {

		List<CSVRecord> CSVRecords = CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader()
				.parse(new InputStreamReader(csvFile.getInputStream())).getRecords();

		List<VehicleListing> vehicleListings = CSVRecords.stream().map(this::createVehilcleListing)
				.collect(Collectors.toList());

		return vehicleListings;
	}

	/**
	 * create VehilcleListing from CSVRecord
	 * 
	 * @return
	 */
	private VehicleListing createVehilcleListing(CSVRecord csvRecord) {
		String code = csvRecord.get(CODE);
		String make = csvRecord.get(MAKE_MODEL).split(REGEX)[0];
		String model = csvRecord.get(MAKE_MODEL).split(REGEX)[1];
		String kW = csvRecord.get(POWER_IN_PS);
		String year = csvRecord.get(YEAR);
		String color = csvRecord.get(COLOR);
		String price = csvRecord.get(PRICE);
		return VehicleListing.builder().code(code).make(make).model(model).kW(Integer.valueOf(kW))
				.year(Integer.valueOf(year)).color(color).price(Integer.valueOf(price)).build();
	}

}
