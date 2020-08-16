package car.hey.platform.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import car.hey.platform.model.Vehicle;
import car.hey.platform.model.VehicleListing;
import car.hey.platform.service.VehicleListingService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/vehicle_listings")
public class VehicleListingController {

	@Autowired
	private VehicleListingService vehicleListingService;

	@GetMapping
	public List<VehicleListing> getAllVehicleListing() {
		return vehicleListingService.findAll();
	}

	@PostMapping("/{dealer_id}")
	public List<VehicleListing> addVehicles(@Valid @PathVariable(name = "dealer_id") Integer dealerId,
			@RequestBody List<Vehicle> vehicles) {
		log.info("adding VehicleListings with dealerId {} and vehicles {}", dealerId, vehicles);

		return vehicleListingService.saveAll(dealerId, Optional.of(vehicles).map(Collection::stream)
				.orElseGet(Stream::empty).map(createVehicleListings(dealerId)).collect(Collectors.toList()));

	}

	/**
	 * create Vehicle Listings
	 * 
	 * @param dealerId
	 * @return VehicleListing
	 */
	private Function<Vehicle, VehicleListing> createVehicleListings(Integer dealerId) {
		return vehicle -> VehicleListing.builder().dealerId(dealerId).code(vehicle.getCode()).make(vehicle.getMake())
				.model(vehicle.getModel()).kW(vehicle.getKW()).year(vehicle.getYear()).color(vehicle.getColor())
				.price(vehicle.getPrice()).build();
	}

	@GetMapping("/search")
	public List<VehicleListing> searchVehicleListing(@RequestParam(value = "make", required = false) String make,
			@RequestParam(value = "model", required = false) String model,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "color", required = false) String color) {

		log.info("search VehicleListings with make {}, model {}, year {}, model {},", make, model, year, color);

		return vehicleListingService.search(make, model, year, color);
	}
}
