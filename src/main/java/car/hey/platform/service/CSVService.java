package car.hey.platform.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface CSVService {

	/**
	 * save MultipartFile csv
	 * 
	 * @param dealerId
	 * @param csvFile
	 * @throws IOException
	 */
	public void save(Integer dealerId, MultipartFile csvFile) throws IOException;
}
