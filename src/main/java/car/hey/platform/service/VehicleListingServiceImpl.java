package car.hey.platform.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import car.hey.platform.model.VehicleListing;
import car.hey.platform.repository.VehicleListingRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shijin.raj
 *
 */
@Slf4j
@Service
public class VehicleListingServiceImpl implements VehicleListingService {

	@Autowired
	private VehicleListingRepository vehicleListingRepository;

	@Override
	public List<VehicleListing> findAll() {
		return vehicleListingRepository.findAll();
	}

	@Override
	public List<VehicleListing> saveAll(Integer dealerId, List<VehicleListing> vehicleListings) {
		log.info("saving all vehicleListings {} for the dealer id {}", vehicleListings, dealerId);

		Assert.notNull(dealerId, "Dealer Id cannot be null");
		Assert.notEmpty(vehicleListings, "Vehicle listings cannot be empty");

		Optional.of(vehicleListings).map(Collection::stream).orElseGet(Stream::empty).forEach(vehicleListing -> {
			vehicleListing.setDealerId(dealerId);
		});

		return vehicleListingRepository.saveAll(vehicleListings);
	}

	@Override
	public List<VehicleListing> search(String make, String model, Integer year, String color) {
		return vehicleListingRepository
				.findAll(Example.of(VehicleListing.builder().make(make).model(model).year(year).color(color).build(),
						ExampleMatcher.matchingAll().withIgnoreCase()));
	}

}
