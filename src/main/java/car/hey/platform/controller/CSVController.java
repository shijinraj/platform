package car.hey.platform.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import car.hey.platform.service.CSVService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/upload_csv")
public class CSVController {

	@Autowired
	private CSVService fileService;

	@PostMapping("/{dealer_id}")
	public void uploadFile(@PathVariable(name = "dealer_id") Integer dealerId,
			@RequestParam("file") MultipartFile csvFile) throws IOException {
		log.info("Uploading the file : {}", csvFile.getOriginalFilename());
		fileService.save(dealerId, csvFile);
		log.info("Uploaded the file successfully: {}", csvFile.getOriginalFilename());
	}

}
