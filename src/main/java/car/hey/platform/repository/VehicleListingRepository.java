package car.hey.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import car.hey.platform.model.VehicleListing;
import car.hey.platform.model.VehicleListingId;

/**
 * VehicleListing Repository
 * 
 * @author shijin.raj
 *
 */
public interface VehicleListingRepository
		extends JpaRepository<VehicleListing, VehicleListingId>, QueryByExampleExecutor<VehicleListing> {

}
