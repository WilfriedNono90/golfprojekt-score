package com.example.demo.webservice;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.dao.UbungRepositorie;
import com.example.demo.entities.Ubung;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.payload.UploadFileResponse;

@RestController
@RequestMapping("/service")
public class UbungRestService {
	
	private static final Logger logger = LoggerFactory.getLogger(UbungRestService.class);
	
	@Autowired
	private UbungRepositorie ubungRepositorie;
	
	@Autowired
    private FileStorageService fileStorageService;
	
	//on veut boulo avec kafka, pour creer un producer
	@Autowired
	private KafkaTemplate< String, Ubung> kafkatemplate;
	
	//methode du produceur
	@GetMapping("/testkafka/{id}")
	public String sentMessage(@PathVariable(value = "id") Long id) {
		String topic = "test";
		Ubung ubung = ubungRepositorie.findById(id).get();
		kafkatemplate.send(topic,ubung);
		return "Message Sent";
	}
	
	@GetMapping(path = "/ubung" , produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Ubung> getUbung() {
		return ubungRepositorie.findAll();
	}
	
	@GetMapping(path = "/ubung/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Ubung getOneUbung(@PathVariable(value = "id") Long id) {
		return ubungRepositorie.findById(id).get();
	}
	
	@GetMapping(path = "/ubung/getName/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Ubung getOneUbung(@PathVariable(value = "name") String name) {
		return ubungRepositorie.findByName(name);
	}
	
	@PostMapping(path = "/ubung", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Ubung save(@RequestBody Ubung ubung) {
		//String fileName = fileStorageService.storeFile(file);
		//ubung.setBild(fileName);
		return ubungRepositorie.save(ubung);
	}
	
	@PutMapping(path = "/ubung/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Ubung save(@RequestBody Ubung ubung, @PathVariable(value = "id") Long id) {
		ubung.setId(id);
		return ubungRepositorie.save(ubung);
	}
	
	@DeleteMapping(path = "/ubung/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public boolean delete(@PathVariable Long id) {
		
		try {
			ubungRepositorie.deleteById(id);
			return true;
			
		}catch (Exception e) {
			return false;
		}
	}
	
	 @PostMapping("/uploadFile")
	    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
	        String fileName = fileStorageService.storeFile(file);

	        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	                .path("/service/downloadFile/")
	                .path(fileName)
	                .toUriString();

	        return new UploadFileResponse(fileName, fileDownloadUri,
	                file.getContentType(), file.getSize());
	    }
	 @PostMapping("/uploadMultipleFiles")
	    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
	        return Arrays.asList(files)
	                .stream()
	                .map(file -> uploadFile(file))
	                .collect(Collectors.toList());
	    } 

	    @GetMapping("/downloadFile/{fileName:.+}")
	    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
	    	Ubung ubung = new Ubung(null,"Stuhl-Liegestütze",null,"mit hilfe eines Stuhls liegestütze machen","sehr langsam arbeiten","oft","Stühle","Montag","DOnnerstag","Bären :-)",true);
	        // Load file as Resource
	        Resource resource = fileStorageService.loadFileAsResource(fileName);

	        // Try to determine file's content type
	        String contentType = null;
	        try {
	            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	        } catch (IOException ex) {
	            logger.info("Could not determine file type.");
	        }

	        // Fallback to the default content type if type could not be determined
	        if(contentType == null) {
	            contentType = "application/octet-stream";
	        }

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);
	    }

}
