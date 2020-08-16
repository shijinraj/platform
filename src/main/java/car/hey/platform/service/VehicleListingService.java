package car.hey.platform.service;

import java.util.List;

import car.hey.platform.model.VehicleListing;

/**
 * Vehicle Listing Service
 * 
 * @author shijin.raj
 *
 */
public interface VehicleListingService {

	/**
	 * @return List<VehicleListing> VehicleListings
	 */
	public List<VehicleListing> findAll();

	/**
	 * @param dealerId
	 * @param vehicleListings
	 * @return List<VehicleListing> vehicleListings
	 */
	public List<VehicleListing> saveAll(Integer dealerId, List<VehicleListing> vehicleListings);

	/**
	 * @param make
	 * @param model
	 * @param year
	 * @param color
	 * @return List<VehicleListing> VehicleListings
	 */
	public List<VehicleListing> search(String make, String model, Integer year, String color);

}
